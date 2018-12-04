package searchengine.WebCrawler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import searchengine.Website;

import java.util.ArrayList;
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
     * @return A Website object that has the first title and words found in the DOCTYPE html file
     */
    public static Website extractWebsiteFromDoc(Document doc, String url){
        /*Retreived head tag element of the document, where the title should be contained. */
        Element headElement = doc.getElementsByTag("head").first();
        /*Get body element of the html document*/
        Element bodyElement = doc.getElementsByTag("body").first();
        /*Get the title form the head tag*/
        Element titleElement = headElement.getElementsByTag("title").first();
        /*Get the text value of the title tag*/
        String title = titleElement.text();
        /*If the document has a title, it is a valid page for us to store and use further.*/
        if(title!=null && title.isEmpty()==false) {
            /*Retreived all the header tag elements from h1-h3 in the body, */
            ArrayList<Element> allHeaderElements = new ArrayList<>();
            allHeaderElements.addAll(bodyElement.getElementsByTag("h1"));
            allHeaderElements.addAll(bodyElement.getElementsByTag("h2"));
            allHeaderElements.addAll(bodyElement.getElementsByTag("h3"));
            /*For each header split it's text into list of words, by splitting by space between them.*/
            List<String> words = new ArrayList<>();
            for (Element header : allHeaderElements) {
                if(header.hasText() && header.childNodeSize() > 0){
                    for (Node child: header.childNodes()) {
                        //there could be multiple nodes in a header,
                        // but we only want the words from the TextNode child.
                        // Should only be one direct child which is a TextNode
                        if(child.getClass() == TextNode.class){
                            String headerText = child.toString();
                            String[] wordsInHeader = headerText.split(" ");
                            for (String word : wordsInHeader){
                                //TODO reqex or string operations on the word to filter out symbols
                                words.add(word);
                            }
                        }
                    }

                }

            }
            /*Used url title and list of words to create the Website object*/
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
        /*Looking for all the a tag elements in the body, a tag is the element that has href*/
        Elements links = bodyElement.getElementsByTag("a");
        return links;
    }
}
