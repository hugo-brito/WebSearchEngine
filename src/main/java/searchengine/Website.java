package searchengine;
import java.util.List;

/**
 * A website is the basic entity of the search engine. It has a url, a title, and a list of words.
 *
 * @author Martin Aumüller
 */
public class Website implements Comparable<Website> {

    /**
     * the website's title
     */
    private String title;

    /**
     * the website's url
     */
    private String url;

    /**
     * a list of words storing the words on the website
     */
    private List<String> words;

    /**
     * Creates a {@code Website} object from a url, a title, and a list of words
     * that are contained on the website.
     *
     * @param url the website's url
     * @param title the website's title
     * @param words the website's list of words
     */
    public Website(String url, String title, List<String> words) {
        this.url = url;
        this.title = title;
        this.words = words;
    }

    /**
     * Returns the website's title.
     *
     * @return the website's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the website's url.
     *
     * @return the website's url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * returns the list of words that the instance contains
     * @return List-String- words
     */
    public List<String> getWords(){
        return words;
    }

    /**
     * Checks whether a word is present on the website or not.
     *
     * @param word the query word
     * @return True, if the word is present on the website
     */
    public Boolean containsWord(String word) {
        return words.contains(word);
    }

    @Override
    public String toString() {
        return "Website{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", words=" + words +
                '}';
    }

    /**
     * Implemented this interface so the websites can be sorted in TreeMaps and TreeSets (they gain the ability to compare themselves
     * @param w - a website to compare itself to
     * @return
     */
    @Override
    public int compareTo(Website w) {
        return this.title.compareTo(w.getTitle());
    }
}
