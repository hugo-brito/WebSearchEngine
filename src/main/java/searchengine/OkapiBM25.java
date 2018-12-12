package searchengine;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The OkapiBM25 ranking algorithm calculates a cumulative score for the relevance of a search string in
 * one website.
 */
public class OkapiBM25 implements Score {

    private TFScore tfScore;
    private TFIDFScore tfidfScore;

    /**
     * The average document length based on all websites read in from the database
     */
    private static double AVERAGE_DOC_LENGTH = 0.0;

    /**
     * Free parameters used in the Okapi BM25 score calculation for optimisation.
     */

    private static double K_1 = 1.2;
    private static double B = 0.75;

    public OkapiBM25(Index index) {
        setAverageDocLength(index);
        this.tfidfScore = new TFIDFScore(index);
        this.tfScore = new TFScore();
    }

    /**
     * This takes a query string (that may be made up of one or more words) and calculates a gives a relevance score for
     * that query string on a specific website
     * @param query the query passed to the search engine, already separated around "OR"
     * @param site the website being scored based on the relevance of its contents to the query
     * @param index the database of websites
     * @return The relevance score for the query string one a specified website.
     */
    @Override
    public double getScore(String query, Website site, Index index) {
        // since this algorithm works for one or more words in the search string, need to split the string first to get
        // the individual words
        String[] words = query.split(" ");
        // the count for the summation goes from 1 to the number of words in the string, but as the count variable will
        // used as the index of the array, subtract one from the length of the array
        int count = words.length-1;
        // calculate the Okapi score, using a recursive method okapiScore for the summation
        return okapiScore(count, words, site, index);
    }

    /**
     * Takes the list of the total number of websites created from the database and calculates the mean numbers of words
     * per website to assign it to the static variable AVERAGE_DOC_LENGTH.
     * @param index the database of websites
     */
    private void setAverageDocLength(Index index) {
        // returns all the websites of a given index.
        Collection<Website> siteCollection = index.provideIndex();
        double totalWords = 0;
        for(Website site : siteCollection) {
            totalWords+= site.getWords().size();
        }
        // divides the total number of words by the number of websites to get the mean
        this.AVERAGE_DOC_LENGTH = totalWords/siteCollection.size();
    }

    /**
     * Sums the scores of each word the query string is comprised of to return a total Okapi BM25 score for the
     * specified website
     * @param count the number of individual words in the query
     * @param words the array of the query string broken down into individual words
     * @param site the website being scored based on the relevance of its contents to the query
     * @param index the database of websites
     * @return the website score based on the Okapi BM 25 calculation
     */
    private double okapiScore(int count, String[] words, Website site, Index index) {
        // the numbers of words on the website
        int docLength = site.getWords().size();
        // the recursive part of the method. if the count is 1 then just calculate the score for the one word making up
        // the query string. if the count is greater than one then it adds the Okapi score of the count to the Okapi
        // of the count - 1 until it gets back down to count = 0.
        if(count != 0) {
            double IDF = this.tfidfScore.IDF(words[count], index);
            double termFrequency = this.tfScore.getScore(words[count], site, index);
            double score = IDF*((termFrequency*(K_1 + 1))/(termFrequency + K_1*(1 - B + B*(docLength/AVERAGE_DOC_LENGTH))));
            return score + okapiScore(count-1, words, site, index);
        } else {
            double IDF = this.tfidfScore.IDF(words[0], index);
            double termFrequency = this.tfScore.getScore(words[0], site, index);
            return IDF*((termFrequency*(K_1 + 1))/(termFrequency + K_1*(1 - B + B*(docLength/AVERAGE_DOC_LENGTH))));
        }
    }
}
