package searchengine.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.Website;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Class that can recursively traverse a single web site and exploring it's links.
 * It "Scrapes" the Title, words and links from a DOCTYPE html file
 */
public class WebScraper {

    /**
     * Recursive method for traversing a single web site and exploring it's links
     * It takes in url string to look at and a HahsSet of pages already visited.
     * For each web site it will create Website object and append it to the index file.
     * @param url String value of the url address that should be visited
     * @param visitedSites HashSet of already visited url addresses.
     */
    public void fetchWebsiteRecursive(String url, HashSet<String> visitedSites){
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Document doc = Jsoup.connect(url).get();
            Website site = DocumentHelper.extractWebsiteFromDoc(doc,url);
            if(site != null) {
                AppendSiteToFile(site);
                System.out.println("Done saving " + site.getUrl());

                Elements links = DocumentHelper.extractLinksFromDoc(doc);

                for (Element link : links) {
                    String linkHref = link.absUrl("href");

                    if (linkHref.contains("http")) {
                        try {
                            URL linkUrl = new URL(linkHref);
                            String urlValue = linkUrl.getHost();
                            if (!linkUrl.getPath().isEmpty() && !linkUrl.getPath().equals(("/")))
                                urlValue += linkUrl.getPath();

                            if (urlValue != null && !visitedSites.contains(urlValue)) {
                                visitedSites.add(urlValue);
                                fetchWebsiteRecursive(linkHref, visitedSites);
                            }
//If we are not able to locate the host or url, we do not want to continue the recursive call, we stop here and go to the previous
                        } catch (Exception e) {
                            return;
                        }
                    }


                }
            }
        }catch (IOException ioException){
            return;
        }
        catch (Exception e){
            return;
        }
    }

    /**
     * Appending the values of a Website object to the file real_data_file.txt in the data folder
     * @param site Object of type Website that should be added to the file.
     */
    public void AppendSiteToFile(Website site) {
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream("data//real_data_file.txt", true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter os = new OutputStreamWriter(fo, StandardCharsets.UTF_8 );
        BufferedWriter out = new BufferedWriter(os);
        try{
            out.append("*PAGE:"+ site.getUrl().trim());
            out.newLine();
            String title = site.getTitle().trim();

            if(title.length()> 1)
                title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
            else
                title = title.toUpperCase();
            out.append(title);
            out.newLine();
            List<String> words = site.getWords();
            for (String word: words)
            {
                out.append(word.trim().toLowerCase());
                out.newLine();
            }

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
