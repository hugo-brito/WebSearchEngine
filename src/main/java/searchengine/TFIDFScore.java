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
     * @param word a word from the search query
     * @param site the website being scored against the search string
     * @param index the index of websites
     * @return The TFIDF score for the word on the given website
     */
    @Override
    public double getScore(String word, Website site, InvertedIndex index) {
        // calculating the IDF
        double IDF = IDF(word, index);
        // calculating the TF using the TFScore class
        double TF = this.tfScore.getScore(word, site, index);
        //calculating the TFIDF score
        return IDF*TF;
    }

    /**
     * This method calculates the IDF (inverse document frequency) score for the given word over the collection of
     * websites that contain that word.
     * @param word a word from the search query
     * @param index the index of websites
     * @return the IDF value for that word over the website collection
     */
    public double IDF(String word, InvertedIndex index) {
        // creating a list of websites in which the search term is present
        List<Website> resultsContainingWord = index.lookup(word);
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

    /**
     * The setWebsiteCollectionSize method uses the search engine index to calculate how many websites in total
     * there are in the collection
     * @param index the index of websites
     */
    private void setWebsiteCollectionSize(InvertedIndex index) {
        // a new hashset to store the websites in
        Set<Website> siteCollection = new HashSet<>();
        // getting the keys (words) from the index map
        Set<String> words = index.getIndexMap().keySet();
        // Looping through the words to get the mapped list of websites and add it to the hashset of websites
        // hashset removes all duplicates
        for(String word : words) {
            List<Website> sites = index.getIndexMap().get(word);
            siteCollection.addAll(sites);
        }
        this.websiteCollectionSize = siteCollection.size();
    }
}
