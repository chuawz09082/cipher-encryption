package cipher;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Factory class for creating cipher objects. */
public class CipherFactory {

    /**
     * Returns: a monoalphabetic substitution cipher with the English alphabet mapped to the
     * provided alphabet.<br>
     * Requires: {@code encrAlph} contains exactly one occurrence of each English letter and nothing
     * more. No requirement is made on case.
     *
     * @param encrAlph the encrypted alphabet
     */
    public Cipher getMonoCipher(String encrAlph) {
        return new MonoalphabeticCipher(encrAlph); // TODO implement
    }

    /**
     * Returns a new Caesar cipher with the given shift parameter.
     *
     * @param shift the cipher's shift parameter
     */
    public Cipher getCaesarCipher(int shift) {
        return new CaesarCipher(shift); // TODO implement
    }

    /**
     * Returns a Vigenere cipher (with multiple shifts).
     *
     * @param key the cipher's shift parameters. Note that a is a shift of 1.
     */
    public Cipher getVigenereCipher(String key) {

        return new Vigenere(key); // TODO implement
    }

    /** Returns a new monoalphabetic substitution cipher with a randomly generated mapping. */
    public Cipher getRandomSubstitutionCipher() {
        List<Character> Charlist = new ArrayList<>();
        for (int i = 0; i < 26; i += 1){
            Charlist.add((char) ('A' + i));
        }

        Collections.shuffle(Charlist);

        StringBuilder encrAlphbuild = new StringBuilder();
        for (int i = 0; i < 26; i += 1){
            encrAlphbuild.append(Charlist.get(i));
        }

        return getMonoCipher(encrAlphbuild.toString()); // TODO implement
    }

    /** Returns a new RSA cipher with a randomly generated keys. */
    public Cipher getRSACipher() {
        /** with a 1024-bit RSA key, each encrypted chunk will be 128 bytes (1024 bits = 128 bytes)*/

        SecureRandom rand = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(512,rand);
        BigInteger q = BigInteger.probablePrime(512,rand);
        BigInteger n = p.multiply(q);

        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.probablePrime(256,rand);

        while (phi.gcd(e).intValue() > 1){
            e = e.add(BigInteger.ONE);
        }

        BigInteger d = e.modInverse(phi);
        return new RSACipher(d,e,n); // TODO implement
    }

    /**
     * Returns a new RSA cipher with given key.
     *
     * @param e encryption key
     * @param n modulus
     * @param d decryption key
     */
    public Cipher getRSACipher(BigInteger e, BigInteger n, BigInteger d) {
        return new RSACipher(d,e,n); // TODO implement
    }
}
