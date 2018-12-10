package searchengine.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.Website;
import java.io.*;
import java.net.URL;
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
    //visited sites could be exchanged with database tha could be querried in real life, instead of this HashSet
    //filling up memory.
    public void fetchWebsiteRecursive(String url, HashSet<String> visitedSites){

        //Using https://jsoup.org/download
        // Runned into difficulties using reg-ex, found for other solution
        // Jsoup is better solution than reg-ex to read html, because Jsoup is a parser made for HTML.
        //https://www.w3schools.com/html/   HTML explained
       try {
           /*Used Jsoup library to connect to url and to create Document object, that has the entire html document in it.*/
           Document doc = Jsoup.connect(url).get();
           Website site = DocumentHelper.extractWebsiteFromDoc(doc,url);
           if(site != null) {
               /*Appends single site information to the database file, by calling AppendsSiteToFile()method*/
               AppendSiteToFile(site);
               /*Used for printing, to have an overlook, which links are saved*/
               System.out.println("Done saving " + site.getUrl());

               Elements links = DocumentHelper.extractLinksFromDoc(doc);

               /*forEach loop goes through all the elements and looks for the atribute of the href, which is the actual link*/
               for (Element link : links) {
                   /*https://jsoup.org/*/
                   //now we select url link form links (<a> tag) and .absUrl gives full url.
                   //this is necessary becaseu some websites writes the whole link in href, some only the path.
                   String linkHref = link.absUrl("href");

                   /*We first look for links that contains http*/
                   if (linkHref.contains("http")) {
                       try {
                           URL linkUrl = new URL(linkHref);
                           String urlValue = linkUrl.getHost();
                           //We only want to add path to the urlValue if the path is not empty and not equal to "/"
                           if (!linkUrl.getPath().isEmpty() && !linkUrl.getPath().equals(("/")))
                               urlValue += linkUrl.getPath();

                           if (urlValue != null && visitedSites.contains(urlValue) == false) {
                               Thread.sleep(30000);
                               visitedSites.add(urlValue);
                               fetchWebsiteRecursive(linkHref, visitedSites);
                           }

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
    public void AppendSiteToFile(Website site){

      /*  *PAGE:https://en.wikipedia.org/wiki/United_States   *PAGE:url
        United States                                          title
        the                                                  a new line for each word   */


        //https://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
        //"true" means that I am appending the file, not overwrite it.
        try(FileWriter fw = new FileWriter("data//real_data_file.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {

            out.println("*PAGE:"+ site.getUrl());
            out.println(site.getTitle());
            List<String> words = site.getWords();
            for (String word: words)
            {
                out.println(word);
            }

        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
