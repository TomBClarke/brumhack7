package xyz.tomclarke.brumhack7;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;

public class FactParser{
    private String toParse;
    private Tree tree;
    private CoreMap sentence;
    private SemanticGraph dependencies;
    private StanfordCoreNLP pipeline;

    public FactParser(String str, StanfordCoreNLP pipeline){
        toParse = str;
        this.pipeline = pipeline;
    }

    public void setToParse(String toParse) {
        this.toParse = toParse;
    }


    public void parse(){
        parse(this.toParse);
    }

    public void parse(String str){
        String toParse = str;

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(toParse);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        this.sentence =  sentences.get(0);


        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
//            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
//                // this is the text of the token
//                String word = token.get(TextAnnotation.class);
//                // this is the POS tag of the token
//                String pos = token.get(PartOfSpeechAnnotation.class);
//                // this is the NER label of the token
//                String ne = token.get(NamedEntityTagAnnotation.class);
//
//                System.out.println("word: " + word + " pos: " + pos + " ne:" + ne);
//
//            }

        // this is the parse tree of the current sentence
        this.tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//        System.out.println("parse tree:\n" + this.tree);

        // this is the Stanford dependency graph of the current sentence
        this.dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
//        System.out.println("dependency graph:\n" + this.dependencies);
    }
    }

    public Tree getTree(){
        return this.tree;
    }

    public Tree getTree(CoreMap sentence){
        return sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
    }


    public CoreMap getSentence(){
        return this.sentence;
    }

    public SemanticGraph getDependencies(){
        return this.dependencies;
    }
}