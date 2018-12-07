package searchengine;
import java.util.ArrayList;
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
        for (Website w: sites) {
            // go through every website in the list fetched by the FileHelper
            for (String word : w.getWords()) {
                // ok, I'm the website, and I'm iterating through the list of words
                if (map.containsKey(word)) {
                    // if the word is already in the map
                    if (!map.get(word).contains(w)) {
                        // if the set of word does not contain the website
                        map.get(word).add(w);
                        // add it to the list
                    }
                } else {
                    // meaning, it's not there
                    List<Website> newWebsiteList = new ArrayList<>();
                    // create new list of a certain website
                    newWebsiteList.add(w);
                    // the current website to the set
                    map.put(word, newWebsiteList);
                    // put it in the map
                }
            }
        }
    }

    /**
     * Given a query string, returns a list of all websites that contain the query.
     *
     * @param query The query
     * @return the list of websites that contains the query word.
     */
    @Override
    public List<Website> lookup(String query) {
        if (map.containsKey(query)) {
            return map.get(query);
            // added these lines so it returns an empty list when testing (avoiding the null pointer exception)
        } else {
            return new ArrayList<>();
        }
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

    public Map<String, List<Website>> getIndexMap() {
        return map;
    }

}
