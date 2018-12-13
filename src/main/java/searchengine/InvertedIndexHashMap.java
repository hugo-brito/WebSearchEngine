package searchengine;

import java.util.*;

/**
 * HashMap implementation of the InvertedIndex.
 */
class InvertedIndexHashMap extends InvertedIndex {

    /**
     * Instantiates this InvertedIndexHashMap, assigning a HashMap to the superclass field variable.
     */
    InvertedIndexHashMap() {
        this.map = new HashMap<>();
    }

}