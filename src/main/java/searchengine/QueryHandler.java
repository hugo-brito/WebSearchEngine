package searchengine;
import java.util.*;

/**
 * This class is responsible for answering queries to our search engine.
 */

public class QueryHandler {

    /**
     * The index the QueryHandler uses for answering queries.
     */
    private Index idx = null;

    /**
     * The constructor
     * @param idx The index used by the QueryHandler.
     */
    public QueryHandler(Index idx) {
        this.idx = idx;
    }

    /**
     * getMachingWebsites answers queries of the type
     * "subquery1 OR subquery2 OR subquery3 ...". A "subquery"
     * has the form "word1 word2 word3 ...". A website matches
     * a subquery if all the words occur on the website. A website
     * matches the whole query, if it matches at least one subquery.
     *
     * @param line the query string
     * @return the list of websites that matches the query
     */
    public List<Website> getMatchingWebsites(String line) {
        List<Website> results = new ArrayList<>();
        results.addAll(idx.lookup(line));
        List<Website> rankedResults = rankWebsites(results, line, idx);
        return rankedResults;
    }

    /**
     * The rankWebsites method takes query (which may be in the form "term1 OR term2 OR term3...", where term can have
     * the form "word1 word2 word3" and then calculates a score for each term (the sum of the scores for each word). The
     * score for the website is the maximum of each term score.
     * @param sites
     * @param query
     * @param index
     * @return a list of websites, ranked from highest score to lowest score
     */
    public List<Website> rankWebsites(List<Website> sites, String query, Index index) {
        // a TreeMap to so that the keys (the scores) are automatically ordered, using the reverse order comparator to
        // put the highest score first (descending order)
        Map<Double, Website> scoredWebsites = new TreeMap<>(Comparator.reverseOrder());
        String[] terms = query.split(" OR ");
        for (Website site : sites) {
            double siteScore = 0;
            for(String term : terms) {
                String[] words = term.split(" ");
                double termScore = 0.0;
                for (String word : words) {
                    Score score = new TFScore(); //update here to change what ranking algorithm is used
                    double tfScore = score.getScore(word, site, index);
                    termScore += tfScore;
                }
                if(termScore > siteScore) {
                    siteScore = termScore;
                }
            }
            scoredWebsites.put(siteScore, site);
        }
        List<Website> rankedWebsites = new ArrayList<>();
        rankedWebsites.addAll(scoredWebsites.values());
        return rankedWebsites;
    }

}
