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
        // store the give query in a auxiliary query
        List<String> query = new ArrayList<>(Arrays.asList(line.split(" ")));
        // line.split would give me an Array to work with. but an arraylist is a lot more convenient.
        List<Website> results;
        if (query.size() == 1) {
            // if the query consists of one word, nothing changes
            results = new ArrayList<>();
            results.addAll(idx.lookup(line));
            // i can just take the parameter because I know it's a string on only one word
        } else {
            if (query.contains("OR")){
                for (int i = 0; i < query.size(); i++){

                }
            }
            // then there is definitely more than word
            Set<Website> matches = new HashSet<>();
            // using an hashset to prevent duplicates
            for (String queriedWord : query){
                matches.addAll(idx.lookup(queriedWord));
            }
            results = new ArrayList<>(matches);
        }

        return results;
    }

    /**
     * Auxiliary private method that returns websites that match simultaneously all the words in the list provided in the parameter
     * @query
     * @return the list of websites that matches the query
     */
    private List<Website> multiQuery(List<String> query){

    }

    /**
     * Auxiliary private method that returns websites that match the word provided in the parameter
     * @param query
     * @return the list of websites that matches the query
     */
    private List<Website> singleQuery(String query){

    }


}
