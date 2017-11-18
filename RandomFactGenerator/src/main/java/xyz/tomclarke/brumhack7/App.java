package xyz.tomclarke.brumhack7;

import edu.stanford.nlp.semgraph.SemanticGraph;

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
        factinator.getSpitFact();
    }
}
