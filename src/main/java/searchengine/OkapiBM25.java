package searchengine;

import java.util.List;

public class OkapiBM25 implements Score {
    private float termFrequency;
    private float IDF;
    private static float AVERAGE_DOC_LENGTH;

    public OkapiBM25(String term, Website site, Index index) {
        this.termFrequency = new TFScore().getScore(term, site, index);
        this.IDF = new TFIDFScore().IDF(term, index);
    }

    @Override
    public float getScore(String word, Website site, Index index) {

        return 0;
    }

    public void setAverageDocLength(List<Website> sites) {
        int totalWords = 0;
        for(Website site : sites) {
            totalWords+= site.getWords().size();
        }
        this.AVERAGE_DOC_LENGTH = totalWords/sites.size();
    }
}
