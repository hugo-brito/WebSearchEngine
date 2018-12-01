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
        List<String> query = queryTrimmer(line);
        // clean the input of any "funky" input, return a list of strings

        Set<Website> results = new HashSet<>();
        // an hashset will prevent duplicates

        for (String inputs : query) {
                results.addAll(intersectedSearch(inputs));
        }
        return new ArrayList<>(results);
    }


    /**
     * Auxiliary private method that returns websites that match simultaneously all the words in the list provided in the parameter.
     * Since it returns a set, it won't have duplicates
     * @query
     * @return the list of websites that matches the query
     */
    private Set<Website> intersectedSearch(String input){
        // provide a method that retrieves websites that contain ALL the words provided in the list
        List<String> queriedWords = new ArrayList<>(Arrays.asList(input.split(" ")));
        // get the list of words
        // line.split would give me an Array to work with. but an arraylist is a lot more convenient.
        Set<Website> matches = new HashSet<>(idx.lookup(queriedWords.get(0)));
        // using an hashset to prevent duplicates
        if (queriedWords.size() > 1) {
            // do this only if there's more than one word
            for (String queriedWord : queriedWords){
                matches.retainAll(idx.lookup(queriedWord));
            }
        }
        return matches;

//        In the getMatchingWebsites method, first, decompose the query into its components. For a single word,
//        the query still retrieves lists of websites from the inverted index. For the multiple words feature,
//        a collection of lists must be checked for websites that appear in all lists.

    }

    /**
     * Helper method to make sure that the input is free from unaccounted of irrelevant input
     * @param input the input from the query
     * @return returns a list of words to looks for. Every entry of the list consists of a separated intersected query
     */
    private List<String> queryTrimmer (String input){
        input = input.trim();
        // clean the input
        if (input.startsWith("OR ")){
            input = input.substring(3);
        } // delete the or at the beginning if any
        if (input.endsWith(" OR")){
            input = input.substring(0, input.length()-3);
        } // delete the or at the end if any
        List<String> searches = new ArrayList<>(Arrays.asList(input.split("OR")));
        searches.replaceAll(String::trim);
        // clean all white spaces in the beginning and in the end
//        searches.removeAll(Arrays.asList(""));

        while(searches.remove(""))
            // delete all empty entries
        System.out.println(searches);
        return searches;
    }

//    public static void main(String[] args) {
//        queryTrimmer("   OR this and that  OR ORlalal OR OR LALA OR that OR something else OR   ");
//    }


//    public List<Website> getMatchingWebsites(String line) {
//        List<Website> results = new ArrayList<>();
//        results.addAll(idx.lookup(line));
//        return results;
//    }
}
