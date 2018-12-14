package searchengine;

/**
 * The score provides a way of ranking the relevance of a website to the query
 * word(s) by assigning a score value to a website
 *
 * @author Ashley Rose Parsons-Trew
 * @author Hugo Delgado de Brito
 * @author Ieva Kangsepa
 * @author Jonas Hartmann Andersen
 */

public interface Score {

    /**
     * Calculates a score for a website based on the query word
     * @param word the query
     * @param site the website being scored
     * @param index the index containing the website
     * @return the relevance score of the website with respect
     * to the query word
     */

    double getScore(String word, Website site, Index index);
}
