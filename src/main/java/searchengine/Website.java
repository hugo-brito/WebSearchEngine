package searchengine;
import java.util.List;

/**
 * A website is the basic entity of the search engine. It has a url, a title, and a list of words.
 *
 * @author Martin Aumüller
 * @author Ashley Rose Parsons-Trew
 * @author Hugo Delgado de Brito
 * @author Ieva Kangsepa
 * @author Jonas Hartmann Andersen
 */
public class Website implements Comparable<Website> {

    private String title;
    private String url;
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
    Boolean containsWord(String word) {
        return words.contains(word);
    }

    /**
     * Overrides the inherited toString method so it can print the website in a meaningful way.
     * @return String representation of the given Website.
     */
    @Override
    public String toString() {
        return "Website{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", words=" + words +
                '}';
    }

    /**
     * Overrides the Comparable interface compareTo method, so that websites are able to be compared.
     * @param site a given website that needs to be compared.
     * @return 1, 0 or -1 based on natural order of the 2 titles being compared.
     */
    @Override
    public int compareTo(Website site) {
        return this.title.compareTo(site.getTitle());
    }
}