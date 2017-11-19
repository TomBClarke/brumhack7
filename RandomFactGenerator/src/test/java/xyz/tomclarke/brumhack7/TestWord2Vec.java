package xyz.tomclarke.brumhack7;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.junit.Test;

/**
 * A test class to let you mess around with Google News and Word2Vec
 * 
 * @author Tom Clarke
 *
 */
public class TestWord2Vec {

    @Test
    public void tryOutWord2Vec() throws Exception {
        System.out.println("Loading Word2Vec...");
        Word2Vec vec = Word2VecProcessor.getWord2Vec();
        System.out.println("Loaded");
        // Command line input
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        List<String> words = new ArrayList<String>();
        while (!exit) {
            System.out.println("Waiting for input");
            String input = scanner.nextLine();
            switch (input) {
            case "stopitnow":
                exit = true;
            case "runnow":
                System.out.println("Processing: " + words);
                System.out.println(Word2VecProcessor.process(words, vec));
                words = new ArrayList<String>();
            default:
                words.add(input);
            }
        }
        scanner.close();
    }

}
