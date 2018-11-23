package searchengine;
import java.util.*;

public class InvertedIndexTreeMap extends InvertedIndex {

    /**
     * this class uses the inverted index technique to provide websites given a certain word (opposed to given a word,
     * search for all websites that contain that word). so words will be mapped to websites. And in this case, since
     * the natural order of the "words" is alphabetical, the map will be sorted alphabetically by key -word-
     * to sites
     */

    private Map<String, Map<Website, Integer>> wordToWebsites;

    /** the map of words to website, and its rank within that word, which starts empty
     */

    /**
     * initialize the treemap
     */
    public InvertedIndexTreeMap() {
        this.wordToWebsites = new TreeMap<>();
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
                if (wordToWebsites.containsKey(word)) {
                    // if the word is already in the map
                    if (wordToWebsites.get(word).containsKey(w)) {
                        // if the map from website to rank contains the website
                        wordToWebsites.get(word).put(w, wordToWebsites.get(word).get(w) + 1);
                        // increase the rank of such website within that word
                    } else {
                        // meaning the website hasn't been added to the word
                        Map <Website, Integer> rankedWebsite = new HashMap<>();
                        // I have to create a new hashmap
                        rankedWebsite.put(w, 1);
                        // starts with the website as key and the ranking 1
                        wordToWebsites.put(word, rankedWebsite);
                        // and put it back in the map

                    }
                } else {
                    // meaning, it's not there
                    Map<Website, Integer> rankedWebsite = new HashMap<>();
                    // create new hashmap of a certain website mapped to its ranking
                    rankedWebsite.put(w, 1);
                    // the ranking starts a 1
                    wordToWebsites.put(word, rankedWebsite);
                    // put it in the map
                }
            }
        }
    }

    @Override
    public List<Website> lookup(String query) {
        // this can be smarter (e.g. provide the list already by sorted by the number of occurrences)
        List<Website> result = new ArrayList<>();
        for (Website w : wordToWebsites.get(query).keySet()) {
            result.add(w);
        }
        return result;
    }

    // I need the toString method and to write tests for it


}
