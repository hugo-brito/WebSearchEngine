package searchengine.WebCrawler;

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

    WebScraper scaper;
    List<String> urlsToLookAt;

    public WebCrawler() {
        scaper = new WebScraper();
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
        HashSet<String> visitedSites =  new HashSet<>();
        for (String url:urlsToLookAt) {
                try {
                URL linkUrl = new URL(url);
                String urlValue = linkUrl.getHost() + linkUrl.getPath();
                visitedSites.add(urlValue);
                scaper.fetchWebsiteRecursive(url,visitedSites);
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
        defaultUrls.add("https://tv2.dk");
        defaultUrls.add("https://www.dr.dk");
        return defaultUrls;
    }


}
