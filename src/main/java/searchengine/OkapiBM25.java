package searchengine;

import java.util.List;

/**
 * The OkapiBM25 ranking algorithm calculates a cumulative score for the relevance of a search string in
 * one website.
 */
public class OkapiBM25 implements Score {

    /**
     * The average document length based on all websites read in from the database
     */
    private static double AVERAGE_DOC_LENGTH = 0.0;

    /**
     * Free parameters used in the Okapi BM25 score calculation for optimisation.
     */

    private static double K_1 = 1.2;
    private static double B = 0.75;

    public OkapiBM25(String term, Website site, Index index) {
        //this.termFrequency = new TFScore().getScore(term, site, index);
        //this.IDF = new TFIDFScore().IDF(term, index);
    }

    /**
     * This takes a query string (that may be made up of one or more words) and calculates a gives a relevance score for
     * that query string on a specific website
     * @param query
     * @param site
     * @param index
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
        double score = okapiScore(count, words, site, index);
        return score;
    }

    /**
     * Takes the list of the total number of websites created from the database and calculates the mean numbers of words
     * per website to assign it to the static variable AVERAGE_DOC_LENGTH.
     * @param sites
     */
    public void setAverageDocLength(List<Website> sites) {
        int totalWords = 0;
        for(Website site : sites) {
            // creates a running total of the number of words from each website created from the database
            totalWords+= site.getWords().size();
        }
        // divides the running total number of words by the number of websites to get the mean
        this.AVERAGE_DOC_LENGTH = totalWords/sites.size();
    }

    /**
     * Sums the scores of each word the query string is comprised of to return a total Okapi BM25 score for the
     * specified website
     * @param count
     * @param words
     * @param site
     * @param index
     * @return
     */
    public double okapiScore(int count, String[] words, Website site, Index index) {
        // the numbers of words on the website
        int docLength = site.getWords().size();
        // the recursive part of the method. if the count is 1 then just calculate the score for the one word making up
        // the query string. if the count is greater than one then it adds the Okapi score of the count to the Okapi
        // of the count - 1 until it gets back down to count = 1.
        if(count == 1) {
            double IDF = new TFIDFScore().IDF(words[count], index);
            double termFrequency = new TFIDFScore().getScore(words[count], site, index);
            double score = IDF*((termFrequency*(K_1 + 1))/(termFrequency + K_1*(1 - B + B*(docLength/AVERAGE_DOC_LENGTH))));
            return score;
        } else {
            return okapiScore(count, words, site, index) + okapiScore(count-1, words, site, index);
        }
    }
}
