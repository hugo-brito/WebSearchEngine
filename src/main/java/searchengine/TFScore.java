package searchengine;


public class TFScore implements Score{

    public float getScore(String word, Website site, Index index) {
        float frequency = termFrequency(site, word);
        float documentLength = site.getWords().size();
        return frequency/documentLength;
    }

    public float termFrequency(Website site, String term) {
        float frequency = 0;
        for(String word : site.getWords()) {
            if(word.equals(term)) {
                frequency++;
            }
        }

        return frequency;
    }
}
