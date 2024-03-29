package searchengine;
import java.util.Collection;
import java.util.List;

/**
 * The index data structure provides a way to build an index from
 * a list of websites. It allows to lookup the websites that contain a query word.
 *
 * @author Martin Aumüller
 * @author Ashley Rose Parsons-Trew
 * @author Hugo Delgado de Brito
 * @author Ieva Kangsepa
 * @author Jonas Hartmann Andersen
 */
public interface Index {
    /**
     * The build method processes a list of websites into the index data structure.
     *
     * @param sites The list of websites that should be indexed
     */
   void build(List<Website> sites);

    /**
     * Given a query string, returns a list of all websites that contain the query.
     * @param query The query
     * @return the list of websites that contains the query word.
     */
    List<Website> lookup(String query);

    /**
     * Provides all websites in a given Index as a collection.
     * @return a collection of all websites contained by the index.
     */
    Collection<Website> provideIndex();
}