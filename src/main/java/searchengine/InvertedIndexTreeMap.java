package searchengine;
import java.util.*;

/**
 * TreeMap implementation of the InvertedIndex.
 */
class InvertedIndexTreeMap extends InvertedIndex {


    /**
     * Instantiates this InvertedIndexTreeMap, assigning a TreeMap to the superclass field variable.
     */
    InvertedIndexTreeMap() {
        this.map = new TreeMap<>();
    }

}
