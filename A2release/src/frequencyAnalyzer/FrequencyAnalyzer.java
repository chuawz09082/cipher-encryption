package frequencyAnalyzer;

import cipher.AbstractCipher;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Analyzes the frequency of different characters in some plaintext or ciphertext in a list of
 * files.
 */
public class FrequencyAnalyzer {

    public void analyzeFrequency(String... filenames) throws FileNotFoundException, IOException {
        // TODO implement
    }

    /**
     * Adds an occurrence of a character to the analyzer if it is a letter, otherwise ignore.
     *
     * @param c the character to be odded
     */
    public void addChar(char c) {
        // TODO implement
    }

    /**
     * Returns the given character's current frequency in the analyzer.
     *
     * @return the frequency of the given character
     */
    public int getFrequency(char c) {
        return -1; // TODO implement
    }

    /**
     * Returns a Caesar cipher constructed using data from the two analyzers.
     *
     * @param base an analyzer containing the results of scanning some plaintext
     * @param encrypted an analyzer containing the results of scanning encrypted text(s) in the
     *     encrypted
     * @return a Caesar cipher that is a best estimate of how {@code encrypted} was generated
     */
    public static AbstractCipher getCipher(FrequencyAnalyzer base, FrequencyAnalyzer encrypted) {
        return null; // TODO implement
    }
}
