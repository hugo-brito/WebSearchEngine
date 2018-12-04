package searchengine;

/**
 * The score data structure provides a way of
 * ranking the relevance of a website to the query
 * word(s)
 *
 * @author Ashley Parsons-Trew
 */

public interface Score {

    /**
     * The getScore method calculates a score for a website based
     * on the query word
     * @param word
     * @param site
     * @param index
     * @return the relevance score of the website with respect
     * to the query word
     */

    double getScore(String word, Website site, InvertedIndex index);
}
