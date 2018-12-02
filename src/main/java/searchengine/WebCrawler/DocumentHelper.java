package searchengine.WebCrawler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import searchengine.Website;

import java.util.ArrayList;
import java.util.List;

public class DocumentHelper {
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
            ArrayList<Element> allElements = new ArrayList<>();
            allElements.addAll(bodyElement.getElementsByTag("h1"));
            allElements.addAll(bodyElement.getElementsByTag("h2"));
            allElements.addAll(bodyElement.getElementsByTag("h3"));
            /*For each header split it's text into list of words, by splitting by space between them.*/
            List<String> words = new ArrayList<>();
            for (Element header : allElements) {
                if(header.hasText() && header.childNodeSize() > 0){
                    for (Node child: header.childNodes()) {
                        //there could be multiple nodes in a header,
                        // but we only want the words from the TextNode child.
                        // Should only be one direct child there is TextNode
                        if(child.getClass() == TextNode.class){
                            String headerText = child.toString();
                            String[] wordsInHeader = headerText.split(" ");
                            for (String word : wordsInHeader)
                                words.add(word);
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

    public static Elements extractLinksFromDoc(Document doc){
        Element bodyElement = doc.getElementsByTag("body").first();
        /*Looking for all the a tag elements in the body, a tag is the element that has href*/
        Elements links = bodyElement.getElementsByTag("a");
        return links;
    }
}
