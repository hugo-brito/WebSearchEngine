package searchengine;

import java.util.Collections;

/**
 * The TF (term frequency) ranking algorithm calculates a score for how relevant a word is on a given website by
 * counting how often the work appears on the website, and then normalises the score by dividing by the number of words
 * the website in total.
 */

public class TFScore implements Score{

    /**
     * Performs the TF score calculation for the given word with the given website.
     * @param word the query
     * @param site the website being scored
     * @param index the index containing the website
     * @return the TF score for the word on the given website
     */
    public double getScore(String word, Website site, Index index) {
        double frequency = Collections.frequency(site.getWords(), word);
        double documentLength = site.getWords().size();
        return frequency/documentLength;
    }
}