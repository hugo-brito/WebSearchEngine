package searchengine;


import java.util.List;
import java.util.Scanner;

public class TestClass {
    public static void main(String[] args) {
        System.out.println("Welcome to the SearchEngine!");
        args = new String[]{"C:\\Users\\ieva.kangsepa\\IdeaProjects\\TEST\\data\\enwiki-small.txt"};
        System.out.println("Reading database...");
        List<Website> sites = FileHelper.parseFile(new String[0]);
        System.out.println("Sites count : " + sites.size());

        System.out.println("Building the search engine...");
        SearchEngine searchengine;
        searchengine = new SearchEngine(sites);

        System.out.println("Search engine is ready to receive queries.");
        System.out.println("Starting web server:");

        Scanner scan =new Scanner(System.in);
        boolean run = true;
        while(run){
            System.out.println("Write Query...");
            String query = scan.nextLine();
            if(query.toLowerCase().equals("q"))
                run = false;
            else{
                List<Website> resultList = searchengine.search(query);
                System.out.println("Found " + resultList.size() + " websites.");
            }

        }


        // run the search engine

    }
}
