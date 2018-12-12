package searchengine;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueryHandlerTest {

    // used regex101.com and (?:^|\W)law(?:$|\W) to know what was expected.
    // word\n(.*\n)*and

    @Rule
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

    // tests a single word on both the tiny index and the above added examples.
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
         assertEquals(5,qh.getMatchingWebsites("is a it and").size());
     }

     @Test
     void testORQueries() {
         assertEquals(3, qh.getMatchingWebsites("word2 OR word3").size());
         assertEquals(2, qh.getMatchingWebsites("new OR university").size());
         assertEquals(2, qh.getMatchingWebsites("word1 OR word4").size());
         assertEquals(1, qh.getMatchingWebsites("word1 OR word1").size());
     }

     @Test
     void multiWordQueriesWithOr() {
        assertEquals(0, qh.getMatchingWebsites("word1 word5 word3 OR word2 word4").size());
        assertEquals(2, qh.getMatchingWebsites("populous multicultural OR oriented officially").size());
     }

    // Corner case: Does code remove duplicates?
    //     Test for problematic input
    @Test
    void testCornerCases() {
        assertEquals(0, qh.getMatchingWebsites("").size());
        assertEquals(1, qh.getMatchingWebsites("   OR word1 OR word1").size());
        assertEquals(0, qh.getMatchingWebsites("OR").size());
        assertEquals(1, qh.getMatchingWebsites("       OR word1 OR something funky OR").size());
        assertEquals(0, qh.getMatchingWebsites("   ").size());
        assertEquals(1, qh.getMatchingWebsites("OR smallest countries").size());
        assertEquals(1, qh.getMatchingWebsites(".. country japan island").size());
        assertEquals(3, qh.getMatchingWebsites("OR OR OR OR OR OR OR about of as OR OR OR").size());
        assertEquals(6, qh.getMatchingWebsites("OR OR about OR OR OR OR as OR OR OR orc OR asdasd OR of OR OR as OR OR about").size());
        assertEquals(6, qh.getMatchingWebsites("ABOUT OR AS OR OF").size());
        assertEquals(1, qh.getMatchingWebsites("ORcommonlyOROROROR").size());
        assertEquals(1, qh.getMatchingWebsites("ORINDEPENDENT OR therefore").size());
        assertEquals(4, qh.getMatchingWebsites("OR ?ithøjskolen ? is?the it!is%time&ORORscandinavian_a_nordic_islandsOROR OR ORinhabitants" +
                " OR OR ucph OR olde)st OR something ORme&youOR or else OR ").size());
    }
}