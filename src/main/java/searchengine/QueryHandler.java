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
        if (query.get(0).equals("OR")) query.remove(0);
        // if the first word is "OR", just remove it
        Set<Website> results = new HashSet<>();
        // an hashset will prevent duplicates
        while (query.indexOf("OR") > 0){
            // this means that it exists an "OR" in my query
            results.addAll(search(query.subList(0, query.indexOf("OR")))); // this one is a set, and I should keep adding it
            // get all the words until you find "OR", make a list with it, call the search method on it, add to the set of results
            query.removeAll(query.subList(0, query.indexOf("OR")));
            // and then remove previous search from the list and restart
        }
        return new ArrayList<>(results);
    }

    /**
     * Auxiliary private method that returns websites that match simultaneously all the words in the list provided in the parameter.
     * Since it returns a set, it won't have duplicates
     * @query
     * @return the list of websites that matches the query
     */
    private Set<Website> search(List<String> query){
        // provide a method that retrieves websites that contain ALL the words provided in the list
        Set<Website> matches = new HashSet<>(idx.lookup(query.get(0)));
        // using an hashset to prevent duplicates
        for (String queriedWord : query){
            matches.retainAll(idx.lookup(queriedWord));
        }
        return matches;

//        In the getMatchingWebsites method, first, decompose the query into its components. For a single word,
//        the query still retrieves lists of websites from the inverted index. For the multiple words feature,
//        a collection of lists must be checked for websites that appear in all lists.

    }


//    public List<Website> getMatchingWebsites(String line) {
//        List<Website> results = new ArrayList<>();
//        results.addAll(idx.lookup(line));
//        return results;
//    }
}
