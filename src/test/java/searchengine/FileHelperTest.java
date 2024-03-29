package searchengine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

class FileHelperTest {

    // file is empty
    @Test
    void parseEmptyFile() {
        List<Website> sites = FileHelper.parseFile("data/test-file1.txt");
        assertEquals(0, sites.size());
    }

    // file has only one line, a word
    @Test
    void parseBadFile1() {
        List<Website> sites = FileHelper.parseFile("data/test-file2.txt");
        assertEquals(0, sites.size());
    }

    // file has only two lines, a url and a title
    @Test
    void parseBadFile2() {
        List<Website> sites = FileHelper.parseFile("data/test-file3.txt");
        assertEquals(0, sites.size());
    }

    /*
    file has at least three lines but contents is not in the expected format of
    urls, title, word(s)
     */
    @Test
    void parseBadFile3() {
        List<Website> sites = FileHelper.parseFile("data/test-file-errors.txt");
        assertEquals(2, sites.size());
        assertEquals("Title1", sites.get(0).getTitle());
        assertEquals("Title2", sites.get(1).getTitle());
        assertTrue(sites.get(0).containsWord("word1"));
        assertFalse(sites.get(0).containsWord("word3"));
    }

    /*
    file has at least three lines and contents is in expected format of
    urls, title, word(s)
     */
    @Test
    void parseGoodFile() {
        List<Website> sites = FileHelper.parseFile("data/test-file4.txt");
        assertEquals(2, sites.size());
        assertEquals("Title1", sites.get(0).getTitle());
        assertEquals("Title2", sites.get(1).getTitle());
        assertTrue(sites.get(0).containsWord("word1"));
        assertFalse(sites.get(0).containsWord("word3"));
    }
}
