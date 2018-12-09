package searchengine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueryHandlerTest {
    private QueryHandler qh = null;
    @BeforeEach
    void setUp() {
        List<Website> sites = FileHelper.parseFile("data/enwiki-tiny.txt");
        sites.add(new Website("1.com","example1", Arrays.asList("word1", "word2")));
        sites.add(new Website("2.com","example2", Arrays.asList("word2", "word3")));
        sites.add(new Website("3.com","example3", Arrays.asList("word3", "word4", "word5")));

        InvertedIndex idx = new InvertedIndexHashMap();

        idx.build(sites);
        Score score = new TFScore();
        qh = new QueryHandler(idx, score);
    }

    @Test
    void testSingleWord() {
        assertEquals(1, qh.getMatchingWebsites("word1").size());
        assertEquals(4, qh.getMatchingWebsites("denmark").size());
        assertEquals("example1", qh.getMatchingWebsites("word1").get(0).getTitle());
        assertEquals("United States", qh.getMatchingWebsites("america").get(0).getTitle());
        assertEquals(2, qh.getMatchingWebsites("word2").size());
    }

     @Test
     void testMultipleWords() {
         assertEquals(1, qh.getMatchingWebsites("word1 word2").size());
         assertEquals(1, qh.getMatchingWebsites("denmark europe").size());
         assertEquals(1, qh.getMatchingWebsites("word3 word4").size());
         assertEquals(1, qh.getMatchingWebsites("word4 word3 word5").size());
         assertEquals(2, qh.getMatchingWebsites("the capital copenhagen").size());
     }

     @Test
     void testORQueries() {
         assertEquals(3, qh.getMatchingWebsites("word2 OR word3").size());
         assertEquals(2, qh.getMatchingWebsites("word1 OR word4").size());
         // Corner case: Does code remove duplicates?
         assertEquals(1, qh.getMatchingWebsites("word1 OR word1").size());

     }

     @Test
     void multiWordQueriesWithOr() {
        assertEquals(0, qh.getMatchingWebsites("word1 word5 word3 OR word2 word4").size());
     }

    //     Test for problematic input
    @Test
    void testCornerCases() {
        assertEquals(0, qh.getMatchingWebsites("").size());
        assertEquals(1, qh.getMatchingWebsites("   OR word1 OR word1").size());
        assertEquals(0, qh.getMatchingWebsites("OR").size());
        assertEquals(1, qh.getMatchingWebsites("       OR word1 OR something funky OR").size());
    }

    @Test
    void testSth() {

    }
}