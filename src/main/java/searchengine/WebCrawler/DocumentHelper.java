package searchengine.WebCrawler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import searchengine.Website;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class to extract information from a DOCTYPE html file represented by a org.jsoup.nodes.Document object.
 *
 */
public class DocumentHelper {
    /**
     * Static method that creates a Website object from information that is located in the DOCTYPE html file represented by
     * a org.jsoup.nodes.Document object. The html file is downloaded from the given url, and the
     * Website object will get this url.
     * @param doc A org.jsoup.nodes.Document object that has parsed in the DOCTYPE html file from the given URL
     * @param url URL of the site where the DOCTYPE html file was loaded from.
     * @return A Website object that has the title and words found in the DOCTYPE html file
     */
    public static Website extractWebsiteFromDoc(Document doc, String url){
        Element headElement = doc.getElementsByTag("head").first();
        Element bodyElement = doc.getElementsByTag("body").first();
        Element titleElement = headElement.getElementsByTag("title").first();
        String title = titleElement.text();

        /*If the document has a title, it is a valid page for us to store and use further.*/
        if(title!=null && title.isEmpty()==false) {
            ArrayList<Element> allHeaderElements = new ArrayList<>();
            allHeaderElements.addAll(bodyElement.getElementsByTag("h1"));
            allHeaderElements.addAll(bodyElement.getElementsByTag("h2"));
            allHeaderElements.addAll(bodyElement.getElementsByTag("h3"));

            /*For each header split it's text into list of words, by splitting by space between them.*/
            List<String> words = new ArrayList<>();
            for (Element header : allHeaderElements) {

                if(header.hasText() && header.childNodeSize() > 0){
                    for (Node child: header.childNodes()) {

                        if(child.getClass() == TextNode.class){
                            String headerText = child.toString();
                            String[] wordsInHeader = headerText.split(" ");
                            for (String word : wordsInHeader){
                                String cleanWord = cleanWord(word);
                                if(cleanWord != null)
                                    words.add(cleanWord);
                            }
                        }
                    }
                }
            }
            Website site = new Website(url, title, words);
            return site;
        }
        else
            return null;
    }
    /**
     * Extracts links in the DOCTYPE html file represented by a org.jsoup.nodes.Document object.
     * @param doc A org.jsoup.nodes.Document object that has parsed in the DOCTYPE html file from a given url address.
     * @return a org.jsoup.select.Elements list of links.
     */
    public static Elements extractLinksFromDoc(Document doc){
        Element bodyElement = doc.getElementsByTag("body").first();
        Elements links = bodyElement.getElementsByTag("a");
        return links;
    }

    /**
     * Takes in a String input and removes punctuations from it.
     * @param input The input which should get punctuations removed from it.
     * @return The value of the input without punctuations or if nothing is left, it will return null.
     */
    private static String cleanWord(String input){
        input = input.replaceAll("\\p{Punct}", "").replace("?","");
        if(input.equals(""))
            return null;

        return input;
    }
}
