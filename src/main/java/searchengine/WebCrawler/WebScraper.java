package searchengine.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.Website;
import java.io.*;
import java.net.URL;
import java.util.*;


public class WebScraper {


/*Recursive method for traversing single website and exploring it's links
* It takes in url string to look at and a HahsSet of pages already visited.
**/
    public void fetchWebsiteRecursive(String url, HashSet<String> visitedSites){

        //Using https://jsoup.org/download
        // Runned into difficulties using reg-ex, found for other solution
        // Jsoup is better solution than reg-ex to read html, because Jsoup is a parser made for HTML.
        //https://www.w3schools.com/html/   HTML explained
       try {
           /*Used Jsoup library to connect to url and to create Document object, that has the entire html document in it.*/
           Document doc = Jsoup.connect(url).get();
           /*Retreived head tag element of the document, where the title should be contained. */
           Element headElement = doc.getElementsByTag("head").first();
           /*Get body element of the html document*/
           Element bodyElement = doc.getElementsByTag("body").first();
           /*Get the title form the head tag*/
           Element titleElement = headElement.getElementsByTag("title").first();
           /*Get the text value of the title tag*/
           String title = titleElement.text();
           /*If the document has a title, it is a valid page for us to store and use further.*/
           if(title!=null && title.isEmpty()==false)
           {
               /*Retreived all the header tag elements from h1-h3 in the body, */
               ArrayList<Element> allElements = new ArrayList<>();
               allElements.addAll(bodyElement.getElementsByTag("h1"));
               allElements.addAll(bodyElement.getElementsByTag("h2"));
               allElements.addAll(bodyElement.getElementsByTag("h3"));
               /*For each header split it's text into list of words, by splitting by space between them.*/
               List<String> words = new ArrayList<>();
               for (Element header : allElements) {
                   String headerText = header.text();
                   // fetchWebsite(linkHref);
                   String[] wordsInHeader = headerText.split(" ");
                   for (String word: wordsInHeader)
                    words.add(word);
               }
               /*Used url title and list of words to create the Website object*/
               Website site = new Website(url, title, words);
               /*Appends single site information to the database file, by calling AppendsSiteToFile()method*/
               AppendSiteToFile(site);
               /*Used for printing, to have an overlook, which links are saved*/
               System.out.println("Done saving " + site.getUrl());
           }
           /*Looking for all the a tag elements in the body, a tag is the element that has href*/
           Elements links = bodyElement.getElementsByTag("a");
           /*forEach loop goes through all the elements and looks for the atribute of the href, which is the actual link*/
           for (Element link : links) {
               /*https://jsoup.org/*/
               String linkHref = link.absUrl("href");

               /*We first look for links that contains http*/
               String newLink = null;
               if(linkHref.contains("http"))
               {
                   newLink = linkHref;
               }
               try {
                   URL linkUrl = new URL(newLink);
                   String urlValue = linkUrl.getHost();
                   if(!linkUrl.getPath().isEmpty() && !linkUrl.getPath().equals(("/")))
                       urlValue += linkUrl.getPath();

                   if(urlValue != null && visitedSites.contains(urlValue) == false ){
                       Thread.sleep(30000);
                       visitedSites.add(urlValue);
                       fetchWebsiteRecursive(newLink, visitedSites);
                   }

               } catch (Exception e) {
                   return;
               }


           }
       }catch (IOException ioException){

       }
       catch (Exception e){

       }
    }

    private void AppendSiteToFile(Website site){

      /*  *PAGE:https://en.wikipedia.org/wiki/United_States   *PAGE:url
        United States                                          title
        the                                                  a new line for each word   */


        //https://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
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
