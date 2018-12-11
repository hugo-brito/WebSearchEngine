package searchengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {
    private InvertedIndex index = null;
    private List<Website> sites = null;
    @BeforeEach
    void setUp() {
        this.sites = new ArrayList<>();
        sites.add(new Website("1.com", "example1", Arrays.asList("word1", "word2")));
        sites.add(new Website("2.com", "example2", Arrays.asList("word2", "word3")));
        sites.add(new Website("3.com", "example3", Arrays.asList("word3", "word4", "word5")));
        sites.add(new Website("4.com", "example4", Arrays.asList("word5", "word5", "word6", "word2", "word7")));
        this.index = new InvertedIndexHashMap();
        this.index.build(sites);
    }

    @Test
    void getTFScore() {
        TFScore tfScore = new TFScore();
        // word occurs once on the website specified
        assertEquals(0.5, tfScore.getScore("word1", sites.get(0),index));
        assertEquals(1.0/3.0, tfScore.getScore("word3", sites.get(2), index));
        // word occurs more than once on the website specified
        assertEquals(2.0/5.0, tfScore.getScore("word5", sites.get(3), index));
        // word does not occur on the specified website
        assertEquals(0, tfScore.getScore("word4", sites.get(1), index));
    }

    @Test
    void getTFIDFScore() {
        TFIDFScore tfidfScore = new TFIDFScore(index);
        // word occurs once on the site specified
        assertEquals(0.5*Math.log10(4), tfidfScore.getScore("word1", sites.get(0), index));
        // word occurs more than once on the site specified, and on more than one website
        assertEquals((2.0/5.0)*Math.log10(4.0/2.0), tfidfScore.getScore("word5", sites.get(3), index));
        // word doesn't occur on the specified website
        assertEquals(0, tfidfScore.getScore("word1", sites.get(2), index));
        // word occurs once on more than one website
        assertEquals((1.0/3.0)*Math.log10(4.0/2.0), tfidfScore.getScore("word3", sites.get(2), index));
        // word that occurs on one site will have a a greater TFIDF score than one that occurs on more than one site
        assertTrue(tfidfScore.getScore("word1", sites.get(0), index) > tfidfScore.getScore("word3", sites.get(2), index));
    }

    @Test
    void getOkapiBM25Score() {
        OkapiBM25 okapiBM25 = new OkapiBM25(index);
        // word occurs once on the site specified
        assertEquals(Math.log10(4)*((0.5*(1.2 + 1))/(0.5 + 1.2*(1 - 0.75 + 0.75*(2.0/3.0)))), okapiBM25.getScore("word1", sites.get(0), index));
        // word occurs once on the specified website, and on at least one more website
        assertEquals(Math.log10(4.0/2.0)*(((1.0/3.0)*(1.2 + 1))/((1.0/3.0) + 1.2*(1 - 0.75 + 0.75*(3.0/3.0)))), okapiBM25.getScore("word5", sites.get(2), index));
        // word doesn't occur on the specified website
        assertEquals(0, okapiBM25.getScore("word2", sites.get(2), index));
        // word doesn't occur on any website
        assertEquals(0, okapiBM25.getScore("wrong", sites.get(1), index));
        // multi-word query, occurs once on site specified 6,7

        // multi-word query, occurs once on site specified partially on other sites 5,2

        // multi-word query, doesn't occur on site specified

        // multi-word query, occurs partially on site and partially on other sites
    }




}
