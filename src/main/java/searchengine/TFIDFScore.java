package searchengine;

import java.util.List;

/**
 * The TFIDF (term frequency - inverse document frequency) ranking algorithm calculates a score for how relevant a word
 * is on a given website by multiplying the term frequency (given by the TFScore class) with the inverse document
 * frequency.
 */
public class TFIDFScore implements Score{

    private static double WEB_COLLECTION_SIZE;
    private TFScore tfScore;

    TFIDFScore(Index index) {
        setWebsiteCollectionSize(index);
        this.tfScore = new TFScore();
    }

    /**
     * Performs the TFIDF score calculation for the given word with the given website.
     * @param word a word from the search query
     * @param site the website being scored against the search string
     * @param index the index containing the website
     * @return The TFIDF score for the word on the given website
     */
    @Override
    public double getScore(String word, Website site, Index index) {
        double IDF = IDF(word, index);
        double TF = this.tfScore.getScore(word, site, index);
        return IDF*TF;
    }

    /**
     * Calculates the IDF (inverse document frequency) score for the given word over the collection of
     * websites that contain that word.
     * @param word a word from the search query
     * @param index the index containing the website
     * @return the IDF value for that word over the website collection
     */
    double IDF(String word, Index index) {
        List<Website> resultsContainingWord = index.lookup(word);
        double numberOfResults = resultsContainingWord.size();
        // as the IDF calculation involves a division and it's possible for the denominator to be zero,
        // need to take care not to divide by zero
        if(numberOfResults == 0.0) {
            return 0;
        }
        return Math.log(WEB_COLLECTION_SIZE/numberOfResults)/Math.log(2);
    }

    /**
     * Uses the search engine index to calculate how many websites in total
     * there are in the collection.
     * @param index the index of websites
     */
    private static void setWebsiteCollectionSize(Index index) {
        WEB_COLLECTION_SIZE = index.provideIndex().size();
    }
}