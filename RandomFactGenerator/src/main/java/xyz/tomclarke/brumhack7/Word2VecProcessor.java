package xyz.tomclarke.brumhack7;

import java.io.FileNotFoundException;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.deeplearning4j.ui.standalone.ClassPathResource;

/**
 * Uses word2vec
 * 
 * @author tbc452
 *
 */
public class Word2VecProcessor {

    public static void main(String[] args) throws Exception {
        Word2Vec vec = process();
        System.out.println("output:");
        System.out.println(vec.wordsNearest("United States", 10));
    }

    /**
     * Processes papers with word2vec
     * 
     * @param papers
     *            The papers to process
     * @return The trained Word2Vec instance
     * @throws FileNotFoundException
     */
    public static Word2Vec process() throws Exception {
        // System.setProperty("java.library.path", "");

        // Setup the iterator holding the data
        SentenceIterator iter = new BasicLineIterator(
                new ClassPathResource("facts.txt").getFile().getAbsolutePath());
        iter.setPreProcessor(new SentencePreProcessor() {
            private static final long serialVersionUID = 1L;

            @Override
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
        });

        // Setup the tokenizer
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        // Setup the word2vec instance
        Word2Vec vec = new Word2Vec.Builder().minWordFrequency(5).iterations(100).layerSize(100).seed(42).windowSize(5)
                .iterate(iter).tokenizerFactory(t).build();

        vec.fit();

        return vec;
    }
}
