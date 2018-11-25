package searchengine;
import java.util.List;
import java.util.Map;

abstract public class InvertedIndex implements Index {

    protected Map<String, List<Website>> map;
    /**
     * The build method processes a list of websites into the index data structure.
     *
     * @param sites The list of websites that should be indexed
     */
    @Override
    public void build(List<Website> sites) {
    }

    /**
     * Given a query string, returns a list of all websites that contain the query.
     *
     * @param query The query
     * @return the list of websites that contains the query word.
     */
    @Override
    public List<Website> lookup(String query) {
        return map.get(query);
    }

    @Override
    public String toString() {
        String returnString = "";
        for (String word : map.keySet()) {
            returnString = returnString + "Websites with " + word + ":\n";
            for (Website w : map.get(word)) {
                returnString = returnString + " - " + w.getTitle() + ": " + w.getUrl() + "\n";
            }
        }
        return returnString;
    }
}
