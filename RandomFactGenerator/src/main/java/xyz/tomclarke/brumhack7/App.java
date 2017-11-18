package xyz.tomclarke.brumhack7;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // creates a parsing pipeline
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        FactParser factParser = new FactParser("that Miroslava Breach, a Mexican investigative journalist known for exposing human rights violations and political corruption, was murdered in March 2017?", pipeline);
        factParser.parse();

    	FactScraper factinator = new FactScraper();
        String singleFact = factinator.getSpitFact();
        factParser.setToParse(singleFact);
        factParser.parse();

        System.out.println("Fact: "+ singleFact);
        System.out.println("Parse Tree\n" + factParser.getTree());

        System.out.println(factParser.getTree());

    }
}
