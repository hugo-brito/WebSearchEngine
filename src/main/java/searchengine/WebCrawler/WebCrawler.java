package searchengine.WebCrawler;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Class that contains a list of default url addresses to visit. Recursively start "crawling" the web by using
 * its default list, and make use of a WebScraper object to scrape information from the page and
 * recursively crawl to other web sites from there.
 */
public class WebCrawler {

    private WebScraper scraper;
    private List<String> urlsToLookAt;

    public WebCrawler() {
        scraper = new WebScraper();
        urlsToLookAt = this.getUrlsToLookAt();
    }

    /**
     * Static void main method to start a WebCrawler
     * @param args
     */
    public static void main(String[] args){
        WebCrawler crawler = new WebCrawler();

        crawler.crawl();

    }


    /**
     * It start "crawling" by taking urls form the urlsToLookAt field, on at the time.
     * It initialize a HashSet visitedSites that till contain all the visited sites,
     * to makes sure we do not visit the same site again.
     * Each url along with the HashSet will be passed to the fetchWebsiteRecursive method
     * in the webScraper class.
     */
    public void crawl()  {
        //Clean up
        File f = new File("data/real_data_file.txt");
        if(f.exists())
            f.delete();

        HashSet<String> visitedSites =  new HashSet<>();
        for (String url:urlsToLookAt) {
            try {
                //URL is form Java library, it "understands" how urls are composed and can
                //fetch you things you want form it.
                URL linkUrl = new URL(url);
                //We get both host and path, to compare theese values with new sites later on, to know,
                //if we already seen this case. To avoid http and https lookups.
                String urlValue = linkUrl.getHost() + linkUrl.getPath();
                visitedSites.add(urlValue);
                scraper.fetchWebsiteRecursive(url,visitedSites);
            }
            catch (MalformedURLException e)
            {

            }
        }

    }

    /**
     * Simple get method that will return the list of all the urls that WebCrawler should always visit.
     * @return List of Strings
     */
    private List<String> getUrlsToLookAt(){
        List<String> defaultUrls = new ArrayList<> ();
        //Urls should not end with "/", because the rest of the logic will remove the "/" if the url ends with it
        // before putting it into visitedSites.
        defaultUrls.add("https://edition.cnn.com");
        defaultUrls.add("https://www.nytimes.com");
        defaultUrls.add("https://tv2.dk");
        defaultUrls.add("https://www.dr.dk");
        return defaultUrls;
    }


}
