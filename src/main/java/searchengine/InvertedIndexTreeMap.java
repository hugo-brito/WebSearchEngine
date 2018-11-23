package searchengine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class InvertedIndexTreeMap extends InvertedIndex {

    /**
     * this class uses the inverted index technique to provide websites given a certain word (oposed to given a word,
     * search for all websites that contain that word). so words will be mapped to websites. And in this case, since
     * the natural order of the "words" is alphabetical, the map will be sorted alphabetically by key -word-
     * to sites
     */

    private Map<String, List<Website>> wordToWebsites;

    /** the map of words to website which starts empty
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
                    wordToWebsites.get(word).add(w);
                    //get the list value mapped to that word key & add that website to the list
                } else {
                    // meaning, it's not there
                    List<Website> websites = new ArrayList<>();
                    websites.add(w)
                    wordToWebsites.put()
                }
            }
        }
    }
}
