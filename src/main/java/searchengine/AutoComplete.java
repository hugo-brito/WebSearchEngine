package searchengine;

import java.util.*;

/**
 * A data source for the autocomplete feature built using the Index
 */
public class AutoComplete {
    private Set<String> wordsList;

    /**
     * Initialises an AutoComplete object and creates and assigns a set of words to its field variable.
     * @param index the index of websites
     */
    public AutoComplete(Index index) {
        setWordList(index);
    }

    /**
     * Builds a set of all words from all websites
     * @param index the index of websites
     */
    private void setWordList(Index index) {
        Collection<Website> sites = index.provideIndex();
        Set<String> words = new HashSet<>();
        for(Website site : sites) {
            words.addAll(site.getWords());
        }
        this.wordsList = words;
    }


    public Set<String> getWordsList() {
        return wordsList;
    }
}
