package searchengine;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for answering queries to our search engine.
 */

public class QueryHandler {

    /**
     * The index the QueryHandler uses for answering queries.
     */
    private Index idx;
    private Score score;

    /**
     * The constructor
     * @param idx The index used by the QueryHandler.
     */
    public QueryHandler(Index idx, Score score) {
        this.idx = idx;
        this.score = score;
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
        List<String> query = cleanQuery(line);
        // clean the input of any "funky" input, return a list of strings

        Set<Website> results = new HashSet<>();
        //an hashset will prevent duplicates

//        List<Website> results = new ArrayList<>();
//        results.addAll(idx.lookup(line));
        for (String inputs : query) {
            results.addAll(intersectedSearch(inputs));
        }

//        return rankedResults;

        return rankWebsites(results, query);
        // this will return the final result as a list.
    }

    /**
     * The rankWebsites method takes query (which may be in the form "term1 OR term2 OR term3...", where term can have
     * the form "word1 word2 word3" and then calculates a score for each term (the sum of the scores for each word). The
     * score for the website is the maximum of each term score.
     * @param sites
     * @param query
     * @return a list of websites, ranked from highest score to lowest score
     */

    //as far as I understand, this is just an extra helper method on the QueryHandler class to order the results.
    //perhaps it should be a private method then...
    //check for repeated code, I tried take it as much methods and variables from other classes as I could but do not
    //consider it polishec
    public List<Website> rankWebsites(Set<Website> sites, List<String> query) {
        // a TreeMap to so that the keys (the scores) are automatically ordered, using the reverse order comparator to
        // put the highest score first (descending order)
        //!!!!!!!!!!!!Hash map is faster than treeMap, and therefore there is no good reason to use treeMap,
        // !!!!if it does not put sites in order. Swithced the key and value around, becasue Website objects are unique
        // !!!!!!!!and Doubæe objects are not. That is also how it was arranged in Willards code.
        //!!!!!!! We put Wllards code, when it is time to return sorted scores.
        Map<Website, Double> scoredWebsites = new HashMap<>();
        // there's a problem here with this Map -- what if 2 websites have the same score (key), treeMap keeps unique keys


//        String[] terms = query.split(" OR ");
        for (Website site : sites) {
            double siteScore = 0;
            for(String intersectedSearch : query) {
                String[] words = intersectedSearch.split(" ");
                double termScore = 0.0;
                for (String word : words) {
                    //Score score = new TFScore(); //update here to change what ranking algorithm is used
                    double tfScore = score.getScore(word, site, this.idx);
                    // the score should be a field, so the query handler constructor takes it in as a parameter
                    termScore += tfScore;
                }
                if(termScore > siteScore) {
                    siteScore = termScore;
                }
            }
            scoredWebsites.put(site, siteScore);
        }
        //!!!!!Here comes the Willard's code. It uses bruthforce, comparing every two Entry sets(Entry(K,V)) x and y
        //!!!!they are compared. Than form Map it gets all the keys (which is Website) and returns Websites
        //!!!!!as a List.
        return scoredWebsites.entrySet().stream().sorted((x,y) -> y.getValue().compareTo(x.getValue())).map(
                Map.Entry::getKey).collect(Collectors.toList());
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
            //this for each loop should be replace by a while loop where you start from the 2nd element in the list
            //instead of the first. the way it is implemented now, it will always compare the first element with itself
            // = waste of computing power
            // do this only if there's more than one word

            /*for (String queriedWord : queriedWords){
                matches.retainAll(idx.lookup(queriedWord));
            }*/
           for (int i = 1; i < queriedWords.size(); i++) {
               matches.retainAll(idx.lookup(queriedWords.get(i)));
           }
        }
        /*Set<Website> matches = new HashSet<>();
        for (String queriedWord : queriedWords){
            List<Website> matchedSites = idx.lookup(queriedWord);
            //If the matches is empty, than just add all of them (so there is something to work with intersection,
            //in the next loop).
            if(matches.size() == 0)
                matches.addAll(matchedSites);
            matches.retainAll(matchedSites);
        }*/
        return matches;

//        In the getMatchingWebsites method, first, decompose the query into its components. For a single word,
//        the query still retrieves lists of websites from the inverted index. For the multiple words feature,
//        a collection of lists must be checked for websites that appear in all lists.

    }

    /**
     * Auxiliary private method to make sure that the input is free from unaccounted of irrelevant input
     * @param input the input from the query
     * @return returns a list of words to looks for. Every entry of the list consists of a separated intersected query
     */
    private List<String> cleanQuery (String input){
        input = input.replaceAll("\\p{Punct}", " ").replaceAll("\\s+", " ");
        // replace all the punctuation by spaces and then replace 1 or more space characters by a single space character

        List<String> searches = new ArrayList<>(Arrays.asList(input.split("OR")));
        // make a list of terms to search for, the criteria for making a new term search is the "OR" keyword
        // Every element of the list will be an intersected search and

        searches.replaceAll(String::trim);
        // trim all the searches, just in case they start or end with empty spaces

        searches.removeAll(Arrays.asList(""));
        // delete all empty entries
        // while(searches.remove("")){} // this is an alternative method to the above

        searches.replaceAll(String::toLowerCase);
        // make everything lower case, because of the way the websites are crawled

//        System.out.println(searches);
        return searches;
    }

//    public static void main(String[] args) {
//        // a really bad query, just to see how it behaves
//        cleanQuery(" .  OR ?this ? and?that this!is%something&ORORthese_are_some_wordsOROR OR ORlalal OR OR LALA OR th)at OR something ORme&youOR      else OR   ");
//    }
}
