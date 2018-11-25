package searchengine;
import java.util.*;

public class InvertedIndexTreeMap extends InvertedIndex {

    /**
     * this class uses the inverted index technique to provide websites given a certain word (opposed to given a word,
     * search for all websites that contain that word). so words will be mapped to websites. And in this case, since
     * the natural order of the "words" is alphabetical, the map will be sorted alphabetically by key -word-
     * to sites
     */

    private Map<String, Set<Website>> wordToWebsites;

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
                    if (!wordToWebsites.get(word).contains(w)) {
                        // if the set of word does not contain the website
                        wordToWebsites.get(word).add(w);
                        // add it to the set
                    }
                } else {
                    // meaning, it's not there
                    TreeSet<Website> newWebsiteSet = new TreeSet<>();
                    // create new set of a certain website mapped to its ranking
                    newWebsiteSet.add(w);
                    // the current website to the set
                    wordToWebsites.put(word, newWebsiteSet);
                    // put it in the map
                }
            }
        }
    }

    @Override
    public List<Website> lookup(String query) {
        // this can be smarter (e.g. provide the list already by sorted by the number of occurrences)
        List<Website> result = new ArrayList<>();
        for (Website w : wordToWebsites.get(query)) {
            result.add(w);
        }
        return result;
    }

    @Override
    public String toString() {
        String returnString = "";
        for (String word : wordToWebsites.keySet()) {
            returnString = returnString + "Word =" + word + "\n + Websites with " + word + ":\n";
            for (Website w : wordToWebsites.get(word)) {
                returnString = returnString + " - " + w.getTitle() + ": " + w.getUrl() + "\n";
            }
        }
        return returnString;
    }
}
