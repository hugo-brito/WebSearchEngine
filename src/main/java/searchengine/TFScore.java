package searchengine;


public class TFScore implements Score{

    public double getScore(String word, Website site, Index index) {
        double frequency = termFrequency(site, word);
        double documentLength = site.getWords().size();
        return frequency/documentLength;
    }

    public double termFrequency(Website site, String term) {
        double frequency = 0;
        for(String word : site.getWords()) {
            if(word.equals(term)) {
                frequency++;
            }
        }

        return frequency;
    }
}
