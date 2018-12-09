package searchengine;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class IndexTest {

    @Rule
    private SimpleIndex simpleIndex = new SimpleIndex();
    // adding testing units for the treemap index

    @Rule
    private InvertedIndex treeIndex = new InvertedIndexTreeMap();
    // adding testing units for the hashmap index

    @Rule
    private InvertedIndex hashIndex = new InvertedIndexHashMap();

    // What if the word start with a capital letter? Can it tell the difference?

    // Use the parseFile to create a method that reads in a lot of words, to test the index. Maybe add that to benchmark. To stresstest.

    @Rule
    List<Website> sites = new ArrayList<Website>();

    @BeforeEach
    void setUp() {
        sites.add(new Website("example1.com", "example1", Arrays.asList("word1", "word2", "word1")));
        sites.add(new Website("example2.com", "example2", Arrays.asList("word2", "word3")));
    }

    @AfterEach
    void tearDown() {
        sites.clear();
    }

    @Test
    void buildSimpleIndex() {
        this.simpleIndex.build(sites);
        assertEquals("SimpleIndex{sites=[Website{title='example1', url='example1.com', words=[word1, word2, word1]}, Website{title='example2', url='example2.com', words=[word2, word3]}]}", simpleIndex.toString());
    }

    @Test
        void buildTreeIndex() {
        this.treeIndex.build(sites);
        assertEquals("Websites with word1:\n - example1: example1.com\nWebsites with word2:\n - example1: example1.com\n - example2: example2.com\nWebsites with word3:\n - example2: example2.com\n", treeIndex.toString());
    }

    @Test
        void buildHashIndex() {
        this.hashIndex.build(sites);
        assertEquals("Websites with word1:\n - example1: example1.com\nWebsites with word3:\n - example2: example2.com\nWebsites with word2:\n - example1: example1.com\n - example2: example2.com\n", hashIndex.toString());
    }

    // Checking the lookup method.
    @Test
    void lookupSimpleIndex() {
        this.simpleIndex.build(sites);
        lookup(this.simpleIndex);
    }

    @Test
    void lookupTreeIndex() {
        this.treeIndex.build(sites);
        lookup(this.treeIndex);
    }

    @Test
    void lookupHashIndex() {
        this.hashIndex.build(sites);
        lookup(this.hashIndex);
    }

    private void lookup(Index index) {
        assertEquals(1, index.lookup("word1").size());
        assertEquals(2, index.lookup("word2").size());
        assertEquals(0, index.lookup("word4").size());
    }

    // checks whether the index has all websites given by fileHelper.
    @Test
    void checkingSimpleIndex() {
        this.simpleIndex.build(this.sites);
        assertIterableEquals(sites, this.simpleIndex.getIndex());
    }

    // checks whether the index has all websites given by fileHelper.
    @Test
    void checkingHashMapIndex() {
        checkIndex(this.hashIndex);
    }

    // checks whether the index has all websites given by fileHelper.
    @Test
    void checkingTreeMapIndex() {
        checkIndex(this.treeIndex);
    }

    private void checkIndex(InvertedIndex index) {
        String tinyFile = "data/enwiki-tiny.txt", smallFile = "data/enwiki-small.txt",
        mediumFile = "data/enwiki-medium.txt";

        index.build(readFile(tinyFile));
        assertEquals(readFile(tinyFile).size(), mapToList(index).size());
        index.build(readFile(smallFile));
        assertEquals(readFile(smallFile).size(), mapToList(index).size());
        index.build(readFile(mediumFile));
        assertEquals(readFile(mediumFile).size(), mapToList(index).size());
    }

// Takes all the websites read and adds them to a list, the hash set deletes duplicates.
    private List<Website> mapToList(InvertedIndex index) {
        List<Website> indexSites = index.getIndexMap().values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return new ArrayList<>(new HashSet<>(indexSites));
    }

    // takes the file and makes it into a list, only made for readability.
    private List<Website> readFile(String file) {
        return FileHelper.parseFile(file);
    }

}