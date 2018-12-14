package searchengine;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

/**
 * The search engine. Upon receiving a list of websites, it performs
 * the necessary configuration (i.e. building an index and a query
 * handler) to then be ready to receive search queries.
 *
 * @author Leonid Rusnac
 * @author Martin Aumüller
 * @author Willard Rafnsson
 * @author Ashley Rose Parsons-Trew
 * @author Hugo Delgado de Brito
 * @author Ieva Kangsepa
 * @author Jonas Hartmann Andersen
 */
public class SearchEngine {
    private QueryHandler queryHandler;
    private AutoComplete autoComplete;

    /**
     * Creates a {@code SearchEngine} object from a list of websites.
     *
     * @param sites the list of websites
     */
    public SearchEngine(List<Website> sites) {
        Index idx = new InvertedIndexHashMap(); //Change to preferred Index
        idx.build(sites);
        Score score = new OkapiBM25(idx); //Change to the score you want to use
        queryHandler = new QueryHandler(idx,score);
        autoComplete = new AutoComplete(idx);
    }

    /**
     * Returns the list of websites matching the query.
     *
     * @param query the query
     * @return the list of websites matching the query
     */
    public List<Website> search(String query) {
        if (query == null || query.isEmpty() ) {
            return new ArrayList<>();
        }
        List<Website> resultList = queryHandler.getMatchingWebsites(query);
        return resultList;
    }

    /**
     * Returns a set of words
     * @return a set containing all words from all websites
     */
    public Set<String> source() {
        return autoComplete.getWordsList();
    }
}
