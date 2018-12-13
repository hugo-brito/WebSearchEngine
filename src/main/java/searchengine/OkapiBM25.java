package searchengine;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Ranking algorithm that calculates a cumulative score for the relevance of a search string in
 * one website based on the OkapiBM25 formula.
 */
public class OkapiBM25 implements Score {

    private TFScore tfScore;
    private TFIDFScore tfidfScore;
    private static double AVERAGE_DOC_LENGTH;

    /*
     * Free parameters used in the Okapi BM25 score calculation for optimisation.
     */
    private static double K_1 = 1.2;
    private static double B = 0.75;

    /**
     * Instantiates an OkapiBM25 object from an index, initialising the TFIDF- and TFScore fields while setting the
     * average number of words over the entire website index
     * @param index an index of websites
     */
    OkapiBM25(Index index) {
        setAverageDocLength(index);
        this.tfidfScore = new TFIDFScore(index);
        this.tfScore = new TFScore();
    }

    /**
     * Takes a query string (that may be made up of one or more words) and calculates a gives a relevance score for
     * that query string on a specific website
     * @param query the query passed to the search engine, already separated around "OR"
     * @param site the website being scored based on the relevance of its contents to the query
     * @param index the index containing the website
     * @return The summation of all relevance score for the query string on a specified website.
     */
    @Override
    public double getScore(String query, Website site, Index index) {
        String[] words = query.split(" ");
        // the count for the summation goes from 1 to the number of words in the string, but as the count variable will
        // used as the index of the array, subtract one from the length of the array
        int count = words.length-1;
        return okapiScore(count, words, site, index);
    }

    /**
     * Takes the list of the total number of websites created from the index and calculates the mean numbers of words
     * per website to assign it to the static variable AVERAGE_DOC_LENGTH.
     * @param index the data structure of websites
     */
    private static void setAverageDocLength(Index index) {
        Collection<Website> siteCollection = index.provideIndex();
        double totalWords = 0;
        for(Website site : siteCollection) {
            totalWords+= site.getWords().size();
        }
        AVERAGE_DOC_LENGTH = totalWords/siteCollection.size();
    }

    /**
     * Sums the scores of each word the query string is comprised of to return a total Okapi BM25 score for the
     * specified website. If the query is comprised of one single word then it will just calculate the score for that one
     * word. For more than one word it adds each successive Okapi score to the previous, decreasing the count each time
     * until the count = 0.
     * @param count the number of individual words in the query
     * @param words the array of the query string broken down into individual words
     * @param site the website being scored based on the relevance of its contents to the query
     * @param index the database of websites
     * @return the website score over the entire query based on the Okapi BM 25 calculation
     */
    private double okapiScore(int count, String[] words, Website site, Index index) {
        int docLength = site.getWords().size();
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
