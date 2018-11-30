package searchengine;

// JMH Imports
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

// Other Imports
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.List;

/**
 * The Indexing benchmark. This prototype benchmarks the efficiency of
 * the search engine's index by querying it multiple times and taking
 * time measurements. The benchmark is implemented with JMH, which
 * e.g. takes care of warming up the JVM. It is in part inspired by
 * {@code BenchmarkModes} and {@code States} in the JMH Samples. This
 * does not start a web server.
 * 
 * @author Willard Rafnsson
 * @see <a href="https://openjdk.java.net/projects/code-tools/jmh/">JMH</a>
 * @see <a href="https://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/">JMH Samples</a>
 * @see <a href="https://appdoc.app/artifact/org.openjdk.jmh/jmh-core/1.10.2/org/openjdk/jmh/annotations/package-summary.html">JMH Annotations Documentation</a>
 */
public class IndexBenchmark {

    /**
     * The state used by all each run of the benchmark. This is JMH's
     * way to share state between benchmark runs; you do not need to
     * know how it works (this is an "inner class"). This is where we
     * create an instance of {@code SearchEngine}. We want multiple
     * queries to the same {@code SearchEngine} instance, since
     * otherwise, it would be hard to argue that the time measurements
     * we get are the index look-ups specifically, and not e.g. the
     * time it takes to read a file, obtain a {@code SearchEngine}
     * instance, and so on.
     */
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        public SearchEngine searchengine;
        public String[] words;
        public BenchmarkState(){
            // Executed each time "# Fork: X of 5" appears in the output.
            //Changed to String array
            List<Website> sites = FileHelper.parseFile(new String[0]); //is this return empty element of the string array?
            searchengine = new SearchEngine(sites);
            //To switch index go to Main/Java/SearchEngine/SearchEngine

            //moved the reading of the config file of words to here, which is where the
            // initial setup of th test takes place.
            // If the readConfigWords() was called in the measureAvgTime as before
            // the readConfigWords will have an effect on the test result, and
            // we only want to test the searchengine.search(s) method.
            words = readConfigWords();
        }
    }
    
    /**
     * Measures the average execution time of this method. 
     *
     * How it works: JMH continuously calls this method in a fixed
     * (rather long) window of time, measures the time each method
     * call took, and counts how many times the method was called. JMH
     * then uses this data to compute the average execution time.
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void measureAvgTime(BenchmarkState state) throws InterruptedException {
        // Loks through array words and calls search for a word s.
        String[] words= state.words;
        if (words!=null){
            for (String s:words) {
                state.searchengine.search(s);
            }
        }
    }

    /**
     * JMH-magic. This needs to be here, but this {@code main} is
     * actually never run. JMH generates lots of other classes, and
     * then runs those instead.
     */
    public static void main(String[] args) throws RunnerException {
        
        Options opt = new OptionsBuilder()
                .include(IndexBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
//Helper method copied form helper class and changed to look up words we will search in our test
    public static String[] readConfigWords(){
        //Created config_words.properties, where we can type in words we want to search
        String config   = "config_words.properties";
        Properties prop = new Properties();
        String words = null;
        String[] wordsArray=null;
        try (InputStream inputStream = new FileInputStream(config)) {
            prop.load(inputStream);
            words = prop.getProperty("words");
            wordsArray= words.split(",");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordsArray;
    }
}
