package xyz.tomclarke.brumhack7.play;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.deeplearning4j.models.word2vec.Word2Vec;

import xyz.tomclarke.brumhack7.Word2VecProcessor;

/**
 * A test class to let you mess around with Google News and Word2Vec
 * 
 * @author Tom Clarke
 *
 */
public class TestWord2Vec {

    public static void main(String[] args) throws Exception {
        System.out.println("Loading Word2Vec...");
        Word2Vec vec = Word2VecProcessor.getWord2Vec();
        System.out.println("Loaded");
        // Command line input
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        List<String> words = new ArrayList<String>();
        System.out.println("Type words (one at a time) to add together in Word2Vec. "
                + "Use 'runnow' to evaluate inputs and 'stopitnow' to cleanly exit.");
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
