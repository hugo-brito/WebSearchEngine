package searchengine;

/**
 * The TF (term frequency) ranking algorithm calculates a score for how relevant a word is on a given website by
 * counting how often the work appears on the website, and then normalises the score by dividing by the number of words
 * the website in total.
 * @author Ashley Parsons-Trew
 */

public class TFScore implements Score{

    /**
     * The getScore method performs the TF score calculation for the given word with the given website.
     * @param word a word from the search query
     * @param site the website being scored against the search string
     * @param index the index of websites
     * @return The TF score for the word on the given website
     */
    public double getScore(String word, Website site, Index index) {
        // calculates the term frequency
        double frequency = termFrequency(site, word);
        // the document length will be equal to how many words are stored in the website's word List
        double documentLength = site.getWords().size();
        // the TF score calculation. as there will be no websites with no words, no need to take care that division by
        // zero doesn't occur
        return frequency/documentLength;
    }

    /**
     * This method calculates how many times the specified word occurs on the given website
     * @param site the website being scored against the search string
     * @param term a word from the search query
     * @return The number of times the term appears on the site
     */
    private double termFrequency(Website site, String term) {
        // creates a local variable to store the frequency count
        double frequency = 0;
        // loops through each word in the site's list of words
        for(String word : site.getWords()) {
            // increases the frequency count if the word in the website is the specified word
            if(word.equals(term)) {
                frequency++;
            }
        }

        return frequency;
    }
}
