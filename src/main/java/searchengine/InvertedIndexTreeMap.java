package searchengine;
import java.util.*;

public class InvertedIndexTreeMap extends InvertedIndex {

    /**
     * this class uses the inverted index technique to provide websites given a certain word (opposed to given a word,
     * search for all websites that contain that word). so words will be mapped to websites. And in this case, since
     * the natural order of the "words" is alphabetical, the map will be sorted alphabetically by key -word-
     * to sites
     */


//    protected Map<String, List<Website>> map;

    /**
     * initialize the treemap
     */
    public InvertedIndexTreeMap() {
        this.map = new TreeMap<>();
    }

    /**
     * let's populate that nice-looking treeMap
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
                        // add it to the set
                    }
                } else {
                    // meaning, it's not there
                    TreeSet<Website> newWebsiteSet = new TreeSet<>();
                    // create new set of a certain website mapped to its ranking
                    newWebsiteSet.add(w);
                    // the current website to the set
                    map.put(word, newWebsiteSet);
                    // put it in the map
                }
            }
        }
    }

//    @Override
//    public List<Website> lookup(String query) {
//        return map.get(query);
//    }

}
