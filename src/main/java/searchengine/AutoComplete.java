package searchengine;

import java.util.*;


public class AutoComplete {
    private Set<String> wordList;

    public AutoComplete(Index index) {
        setWordList(index);
    }

    private void setWordList(Index index) {
        Collection<Website> sites = index.provideIndex();
        Set<String> words = new HashSet<>();
        for(Website site : sites) {
            words.addAll(site.getWords());
        }
        this.wordList = words;
    }

    public List<String> findMatches(String query) {
        List<String> matches = new ArrayList<>();
        query = query.toLowerCase();
        for(String word : this.wordList) {
            if(word.contains(query)) {
                matches.add(word);
            }
        }
        return matches;
    }

    public static void main(String[] args) {
        List<Website> sites = new ArrayList<>();
        sites.add(new Website("1.com", "example1", Arrays.asList("atlantic", "mars")));
        sites.add(new Website("2.com", "example2", Arrays.asList("germany", "rhode island")));
        sites.add(new Website("3.com", "example3", Arrays.asList("latvia", "denmark", "england")));
        sites.add(new Website("4.com", "example4", Arrays.asList("portugal", "spain", "australia", "japan", "hong kong")));
        InvertedIndex index = new InvertedIndexHashMap();
        index.build(sites);
        AutoComplete autoComplete = new AutoComplete(index);
        List<String> results = autoComplete.findMatches("Lan");
        System.out.println(results);
    }
}
