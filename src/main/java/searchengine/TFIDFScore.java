package searchengine;

import java.util.List;

public class TFIDFScore implements Score{

    @Override
    public float getScore(String word, Website site, Index index) {
        float IDF = IDF(word, index);
        TFScore termFrequency = new TFScore();
        float TF = termFrequency.getScore(word, site, index);
        return IDF*TF;
    }

    public float IDF(String term, Index index) {
        List<Website> containsWord = index.lookup(term);
        float x = containsWord.size();
        float websiteCollectionSize = FileHelper.getNumberOfWebsites();
        if(x == 0) {
            return 0;
        }
        return (float)Math.log10(websiteCollectionSize/x);
    }
}
