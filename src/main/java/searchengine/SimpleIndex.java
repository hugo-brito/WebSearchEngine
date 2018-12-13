package searchengine;
import java.util.ArrayList;
import java.util.List;

/**
 * An index comprised of a list of websites.
 */
public class SimpleIndex implements Index {

    private List<Website> sites = null;

    /**
     * The build method processes a list of websites into the index data structure.
     *
     * @param sites The list of websites that should be indexed
     */
    @Override
    public void build(List<Website> sites) {
        this.sites = sites;
    }

    /**
     * Given a query string, returns a list of all websites that contain the query.
     *
     * @param query The query
     * @return the list of websites that contains the query word.
     */
    @Override
    public List<Website> lookup(String query) {
        List<Website> result = new ArrayList<Website>();
        for (Website w: sites) {
            if (w.containsWord(query)) {
                result.add(w);
            }
        }
        return result;
    }

    /**
     * Overrides the inherited toString method so it becomes something that can be compared.
     * @return String representation of the given SimpleIndex
     */
    @Override
    public String toString() {
        return "SimpleIndex{" +
                "sites=" + sites +
                '}';
    }

    /**
     * Provides all websites in a the SimpleIndex as a list.
     * @return a list of all websites contained by the index.
     */
    @Override
    public List<Website> provideIndex() {
        return this.sites;
    }
}