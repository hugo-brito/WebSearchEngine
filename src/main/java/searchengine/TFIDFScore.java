package searchengine;

import java.util.List;

/**
 * The TFIDF (term frequency - inverse document frequency) ranking algorithm calculates a score for how relevant a word
 * is on a given website by multiplying the term frequency (given by the TFScore class) and the inverse document
 * frequency.
 * @author Ashley Parsons-Trew
 */
public class TFIDFScore implements Score{

    // should this be made static? Since it is the same regardless of how many object of TFIDFScore is created.
    private double websiteCollectionSize;
    private TFScore tfScore;

    public TFIDFScore(Index index) {
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
    public double getScore(String word, Website site, Index index) {
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
    public double IDF(String word, Index index) {
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
    private void setWebsiteCollectionSize(Index index) {
        // gives the setWebsiteCollectionSize the size of a given index, with no duplicates.
        this.websiteCollectionSize = index.provideIndex().size();
    }
}
