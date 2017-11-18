package xyz.tomclarke.brumhack7;

import edu.stanford.nlp.semgraph.SemanticGraph;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        FactParser factParser = new FactParser("that Miroslava Breach, a Mexican investigative journalist known for exposing human rights violations and political corruption, was murdered in March 2017?");
        factParser.parse();
        SemanticGraph dependencies = factParser.getDependencies();

    	FactScraper factinator = new FactScraper();
        String singleFact = factinator.getSpitFact();
        factParser.setToParse(singleFact);
        factParser.parse();

        System.out.println("Fact: "+ singleFact);
        System.out.println("Parse Tree\n" + factParser.getTree());

        // Timed test with a predetermined string
        System.out.println("Please enter a new fact: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        long startTime = System.currentTimeMillis();
        //String s = "at the L Street Bridge does not carry or cross over L Street";
        factParser.parse(input);
        System.out.println("Parse Tree\n" + factParser.getTree());

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("That took " + totalTime + " milliseconds to parse");

        scanner.close();
    }
}
