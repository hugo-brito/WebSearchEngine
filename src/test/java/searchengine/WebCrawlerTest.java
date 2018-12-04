package searchengine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import searchengine.WebCrawler.DocumentHelper;
import org.jsoup.select.Elements;


import static junit.framework.TestCase.assertEquals;

class WebCrawlerTest {
    Document doc = null;
    String url= null;

    @BeforeEach
    void setUp() {
        url = "https://tv2.dk";
        String html =
                "<html><head><title>Page Title</title></head>"
                + "<body>" +
                        "<a href=\"https://stackoverflow.com\">Last Link</a>"+
                        "<h1>" +
                          "Parsed HTML into a doc." +
                        "</h1>" +
                        "<h2>" +
                            "And added some more text here." +
                        "</h2>" +
                        "<h3>" +
                            "Last but not least, some text with h3 was added here" +
                        "<a href=\"https://tv2.dk/nyheder\">Last Link</a>"+
                        "</h3>" +
                        "<a href=\"https://www.w3schools.com/html/\">Last Link</a>"+
                  "</body>" +
                "</html>";
        doc = Jsoup.parse(html);
    }
    // file is empty
    @Test
    void correctTitle() {

        Website site = DocumentHelper.extractWebsiteFromDoc(doc,url);
        assertEquals("Page Title", site.getTitle());
    }
    @Test
    void correctNumberOfWords() {

        Website site = DocumentHelper.extractWebsiteFromDoc(doc,url);
        assertEquals(22, site.getWords().size());
    }
    @Test
    void correctUrl() {

        Website site = DocumentHelper.extractWebsiteFromDoc(doc,url);
        assertEquals("https://tv2.dk", site.getUrl());
    }

    @Test
    void correctNumberOfLinks() {
        Elements links = DocumentHelper.extractLinksFromDoc(doc);
        assertEquals(3, links.size());
    }



}
