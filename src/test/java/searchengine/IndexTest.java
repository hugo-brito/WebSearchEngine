
package searchengine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IndexTest {
    Index simpleIndex = null;
    // adding testing units for the treemap index
    Index treeIndex = null;

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<Website>();
        sites.add(new Website("example1.com", "example1", Arrays.asList("word1", "word2", "word1")));
        sites.add(new Website("example2.com", "example2", Arrays.asList("word2", "word3")));
        simpleIndex = new SimpleIndex();
        simpleIndex.build(sites);
        // do the same for the treeIndex
        treeIndex = new InvertedIndexTreeMap();
        treeIndex.build(sites);

    }

    @AfterEach
    void tearDown() {
        simpleIndex = null;
        treeIndex = null;
    }

    @Test
    void buildSimpleIndex() {
        assertEquals("SimpleIndex{sites=[Website{title='example1', url='example1.com', words=[word1, word2, word1]}, Website{title='example2', url='example2.com', words=[word2, word3]}]}", simpleIndex.toString());
    }

    @Test
    void buildTreeIndex() {
        assertEquals("InvertedIndexTreeMap{sites=[Website{title='example1', url='example1.com', words=[word1, word2, word1]}, Website{title='example2', url='example2.com', words=[word2, word3]}]}", treeIndex.toString());
    }

    @Test
    void lookupSimpleIndex() {
        lookup(simpleIndex);
    }

    @Test
    void lookupTreeIndex() {
        lookup(treeIndex);
    }

    private void lookup(Index index) {
        assertEquals(1, index.lookup("word1").size());
        assertEquals(2, index.lookup("word2").size());
        assertEquals(0, index.lookup("word4").size());
    }

}