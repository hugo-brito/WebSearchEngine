package searchengine;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Responsible for answering queries to our search engine.
 *
 * @author Ashley Rose Parsons-Trew
 * @author Hugo Delgado de Brito
 * @author Ieva Kangsepa
 * @author Jonas Hartmann Andersen
 */

class QueryHandler {

    private Index idx;
    private Score score;

    /**
     * Initialises a QueryHandler object and assigns the parameters as field variables.
     * @param idx An index of websites.
     * @param score A ranking algorithm used for scoring.
     */

    QueryHandler(Index idx, Score score) {
        this.idx = idx;
        this.score = score;
    }
    /**
     * Answers queries of both 1 worded queries and of the type
     * "subquery1 OR subquery2 OR subquery3 ...". A "subquery"
     * has the form "word1 word2 word3 ...". A website matches
     * a subquery if all the words occur on the website. A website
     * matches the whole query, if it matches at least one subquery.
     *
     * @param line the query string
     * @return the list of websites that matches the query ranked in order of relevance.
     */

    List<Website> getMatchingWebsites(String line) {
        List<String> query = cleanQuery(line);
        Set<Website> results = new HashSet<>();

        for (String inputs : query) {
            results.addAll(intersectedSearch(inputs));
        }

        return rankWebsites(results, query);
    }

    /**
     * Calculates the score for each website based on the query. This can be either a single word query or
     * a compound query.
     * @param sites Set of websites
     * @param query A list of the query, split by OR
     * @return A list of websites, ranked in descending order
     */

    private List<Website> rankWebsites(Set<Website> sites, List<String> query) {
        Map<Website, Double> scoredWebsites = new HashMap<>();

        for (Website site : sites) {
            double siteScore = 0.0;
            for(String intersectedSearch : query) {
                String[] words = intersectedSearch.split(" ");
                double termScore = 0.0;
                for (String word : words) {
                    double tfScore = score.getScore(word, site, this.idx);
                    termScore += tfScore;
                }
                if(termScore > siteScore) {
                    siteScore = termScore;
                }
            }
            scoredWebsites.put(site, siteScore);
        }
        // the following line was provided in the project description and it makes use of a stream to order the list.
        return scoredWebsites.entrySet().stream().sorted((x,y) -> y.getValue().compareTo(x.getValue())).map(
                Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * Finds websites that match simultaneously all the words in the list provided in the parameter.
     * @param input the query
     * @return the list of websites that matches the query
     */
    private Set<Website> intersectedSearch(String input){
        List<String> queriedWords = new ArrayList<>(Arrays.asList(input.split(" ")));
        Set<Website> matches = new HashSet<>(idx.lookup(queriedWords.get(0)));

        if (queriedWords.size() > 1) {
            for (int i = 1; i < queriedWords.size(); i++) {
               matches.retainAll(idx.lookup(queriedWords.get(i)));
            }
        }
        return matches;
    }

    /**
     * Makes sure that the input is free from unaccounted or irrelevant input.
     * @param input the input from the query
     * @return a list of words that consists of a space separated intersected query
     */
    private List<String> cleanQuery (String input){
        input = input.replaceAll("\\p{Punct}", " ").replaceAll("\\s+", " ");
        List<String> searches = new ArrayList<>(Arrays.asList(input.split("OR")));

        searches.replaceAll(String::trim);

        searches.removeAll(Arrays.asList(""));

        searches.replaceAll(String::toLowerCase);

        return searches;
    }
}
