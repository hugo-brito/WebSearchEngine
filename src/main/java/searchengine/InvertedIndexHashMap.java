package searchengine;

import java.util.*;

public class InvertedIndexHashMap extends InvertedIndex {

    public InvertedIndexHashMap() {
        this.map = new HashMap<>();
    }

    /**
     * The build method processes a list of websites into the index data structure.
     *
     * @param sites The list of websites that should be indexed
     */
    @Override
    public void build(List<Website> sites) {
        Set<String> words = new HashSet<>();

        // Adds all the unique words of all websites to the set.
        sites.forEach((website -> words.addAll(website.getWords())));

//        for (Website w : sites) {
//            words.addAll(w.getWords());
//        }
//        words.forEach(word -> );

        for (String word : words) {
            List<Website> websites = new ArrayList<>();

            for (Website w : sites) {

                if (w.containsWord(word)) {
                    websites.add(w);
                }
            }
            map.put(word, websites);
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
        return map.get(query);
    }

    @Override
    public String toString() {
        return "InvertedIndexHashMap{" +
                "words=" + map.keySet() +
                "sites=" + map.values() +
                '}';
    }
}