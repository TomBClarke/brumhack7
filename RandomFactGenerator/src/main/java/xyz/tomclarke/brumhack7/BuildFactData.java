package xyz.tomclarke.brumhack7;

import java.io.IOException;
import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import xyz.tomclarke.brumhack7.fact.Fact;
import xyz.tomclarke.brumhack7.fact.Facts;

/**
 * Builds and saves fact data
 * 
 * @author Tom Clarke
 *
 */
public class BuildFactData {

    public static void main(String[] args) {
        // Run to generate facts object
        buildFactData(false);
    }

    /**
     * Either loads in existing fact data, or makes new data.
     * 
     * @param parseNow
     *            Run the annotator now (very slow)
     * @return A set of preprocessed facts
     */
    public static Facts buildFactData(boolean parseNow) {
        // Try and load facts object
        Facts facts = null;
        try {
            facts = Facts.load();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.out.println("Error loading facts, will regenerate...");
        }

        if (facts == null) {
            System.out.println("Creating new facts...");
            // Need to get facts
            facts = new Facts();
            FactScraper scraper = new FactScraper();

            // Build the CoreNLP annotator to use
            Properties props = new Properties();
            props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            for (String fact : scraper.getSpitFacts()) {
                Fact factObj = new Fact(fact);
                if (parseNow) {
                    FactParser parser = new FactParser(fact, pipeline);
                    parser.parse();
                    factObj.setAnnotations(parser.getSentence());
                }
                facts.add(factObj);
            }

            try {
                facts.save();
            } catch (IOException e) {
                System.out.println("Could not save facts, will need to reprocess facts on next run");
                e.printStackTrace();
            }
        }

        return facts;
    }

}
