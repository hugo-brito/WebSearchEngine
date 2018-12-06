package searchengine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The TFIDF (term frequency - inverse document frequency) ranking algorithm calculates a score for how relevant a word
 * is on a given website by multiplying the term frequency (given by the TFScore class) and the inverse document
 * frequency.
 * @author Ashley Parsons-Trew
 */
public class TFIDFScore implements Score{

    private double websiteCollectionSize;
    private TFScore tfScore;

    public TFIDFScore(InvertedIndex index) {
        setWebsiteCollectionSize(index);
        this.tfScore = new TFScore();
    }

    /**
     * The getScore method performs the TFIDF score calculation for the given word with the given website.
     * @param word
     * @param site
     * @param index
     * @return The TFIDF score for the word on the given website
     */
    @Override
    public double getScore(String word, Website site, InvertedIndex index) {
        // calculating the IDF
        double IDF = IDF(word, index);
        // calculating the TF using the TFScore class
        double TF = tfScore.getScore(word, site, index);
        //calculating the TFIDF score
        return IDF*TF;
    }

    /**
     * This method calculates the IDF score for the given word over the collection of websites that contain that word.
     * @param term
     * @param index
     * @return
     */
    public double IDF(String term, InvertedIndex index) {
        // creating a list of websites in which the search term is present
        List<Website> resultsContainingWord = index.lookup(term);
        // getting the size of this list
        double numberOfResults = resultsContainingWord.size();
        // as the IDF calculation involves a division and it's possible for the denominator to be zero,
        // need to take care not to divide by zero
        if(numberOfResults == 0.0) {
            return 0;
        }
        // calculates the score. should be in the range of zero upwards
        return Math.log10(this.websiteCollectionSize/numberOfResults);
    }

    private void setWebsiteCollectionSize(InvertedIndex index) {
        Set<Website> siteCollection = new HashSet<>();
        Set<String> words = index.getIndexMap().keySet();
        for(String word : words) {
            List<Website> sites = index.getIndexMap().get(word);
            siteCollection.addAll(sites);
        }
        this.websiteCollectionSize = siteCollection.size();
    }
}
