package searchengine;
import java.util.*;

/**
 * The Inverted Index technique is used to map words to lists of websites containing them.
 */
abstract class InvertedIndex implements Index {

    protected Map<String, List<Website>> map;
    /**
     * The build method processes a list of websites into the index data structure.
     *
     * @param sites The list of websites that should be indexed
     */
    @Override
    public void build(List<Website> sites) {
        for (Website w: sites) {
            for (String word : w.getWords()) {
                if (map.containsKey(word)) {
                    if (!map.get(word).contains(w)) {
                        map.get(word).add(w);
                    }
                } else {
                    List<Website> newWebsiteList = new ArrayList<>();
                    newWebsiteList.add(w);
                    map.put(word, newWebsiteList);
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
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Overrides the inherited toString method so it becomes something that can be compared.
     * @return String representation of the given InvertedIndex.
     */
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

    /**
     * Provides all websites in a given InvertedIndex as a set.
     * @return a set of all websites contained in the index.
     */
    @Override
    public Set<Website> provideIndex() {
        Set<Website> siteCollection = new HashSet<>();
        for(String word : this.map.keySet()) {
            List<Website> sites = this.map.get(word);
            siteCollection.addAll(sites);
        }
        return siteCollection;
    }
}