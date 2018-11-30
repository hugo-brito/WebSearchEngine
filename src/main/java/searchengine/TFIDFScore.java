package searchengine;

import java.util.List;

public class TFIDFScore implements Score{

    /**
     * 
     * @param word
     * @param site
     * @param index
     * @return
     */
    @Override
    public double getScore(String word, Website site, Index index) {
        double IDF = IDF(word, index);
        TFScore termFrequency = new TFScore();
        double TF = termFrequency.getScore(word, site, index);
        return IDF*TF;
    }

    public double IDF(String term, Index index) {
        List<Website> containsWord = index.lookup(term);
        double x = containsWord.size();
        double websiteCollectionSize = FileHelper.getNumberOfWebsites();
        if(x == 0.0) {
            return 0;
        }
        return Math.log10(websiteCollectionSize/x);
    }
}
