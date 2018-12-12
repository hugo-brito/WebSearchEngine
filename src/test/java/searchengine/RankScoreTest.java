package searchengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RankScoreTest {
    private InvertedIndex index = null;
    private List<Website> sites = null;
    @BeforeEach
    void setUp() {
        this.sites = new ArrayList<>();
        // 1.com mentions 12 words: Queen= 4; Denmark= 4; of= 4; randomword = 2
        String[] words1 = ("Queen of Denmark Queen of Denmark Queen of Denmark randomword randomword ").toLowerCase().split(" ");
        sites.add(new Website("1.com", "example1", Arrays.asList(words1)));

        // 2.com mentions 13 words: Queen= 2; Denmark= 2; of= 6; randomword = 2
        String[] words2 = ("Queen of Denmark Queen of Denmark of of of of randomword  randomword ").toLowerCase().split(" ");
        sites.add(new Website("2.com", "example2", Arrays.asList(words2)));

        //3.com mentions 14 words: Queen= 1; Denmark= 1; of= 10; randomword = 2
        String[] words3 = ("Queen of Denmark of of of of of of of of of randomword  randomword ").toLowerCase().split(" ");
        sites.add(new Website("3.com", "example3", Arrays.asList(words3)));

        //4.com mentions 9 words: of= 7; randomword = 2 (Website adds noise for word "of")
        String[] words4 = ("of of of of of of of randomword  randomword ").toLowerCase().split(" ");
        sites.add(new Website("4.com", "example4", Arrays.asList(words4)));

        //5.com mentions 9 words: of= 7; randomword = 2 (Website adds noise for word "of")
        String[] words5 = ("of of of of of of of randomword  randomword ").toLowerCase().split(" ");
        sites.add(new Website("5.com", "example5", Arrays.asList(words5)));


        this.index = new InvertedIndexHashMap();
        this.index.build(sites);
    }

    // Test if
    @Test
    void getRankTFScore(){
        TFScore tfScore = new TFScore();
        QueryHandler qh = new QueryHandler(this.index,tfScore);
        List<Website> rankedMatchedSites = qh.getMatchingWebsites("Queen of Denmark");
        assertEquals(sites.get(2),rankedMatchedSites.get(0));
        assertEquals(sites.get(1),rankedMatchedSites.get(1));
        assertEquals(sites.get(0),rankedMatchedSites.get(2));
    }

    // Test if
    @Test
    void getRankTFIDFScore(){
        TFIDFScore tfidfScore = new TFIDFScore(this.index);
        QueryHandler qh = new QueryHandler(this.index,tfidfScore);
        List<Website> rankedMatchedSites = qh.getMatchingWebsites("Queen of Denmark");
        assertEquals(sites.get(0),rankedMatchedSites.get(0));
        assertEquals(sites.get(1),rankedMatchedSites.get(1));
        assertEquals(sites.get(2),rankedMatchedSites.get(2));
    }

}
