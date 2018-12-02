package searchengine.WebCrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WebCrawler {

    WebScraper scaper;
    List<String> urlsToLookAt;

    public WebCrawler() {
        scaper = new WebScraper();
        urlsToLookAt = getUrlsToLookAt();
    }

    public static void main(String[] args){
        WebCrawler crawler = new WebCrawler();
        crawler.crawl();
    }

    public void crawl()  {
        HashSet<String> visitedSites =  new HashSet<String>();
        for (String url:urlsToLookAt) {
            try {
                URL linkUrl = new URL(url);
                String urlValue = linkUrl.getHost() + linkUrl.getPath();
                visitedSites.add(urlValue);
                scaper.fetchWebsiteRecursive(url,visitedSites);
            } catch (MalformedURLException e) {

            }
            visitedSites.add(url);
            scaper.fetchWebsiteRecursive(url,visitedSites);
        }

    }

    private List<String> getUrlsToLookAt(){
        List<String> defaultUrls = new ArrayList<String> ();
        defaultUrls.add("https://tv2.dk");
        defaultUrls.add("https://www.dr.dk");
        return defaultUrls;
    }


}
