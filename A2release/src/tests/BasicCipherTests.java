package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cipher.Cipher;
import cipher.CipherFactory;
import org.junit.jupiter.api.Test;

import java.io.*;

public class BasicCipherTests {

    private final CipherFactory cipherFactory = new CipherFactory();

    @Test
    void testBasicCaesar() {
        Cipher caesar = cipherFactory.getCaesarCipher(5);
        assertEquals("wmnst", caesar.encrypt("rhino"));
        assertEquals("btrgfy", caesar.encrypt("wombat"));
    }

    @Test
    void testBasicVigenere() {
        Cipher vigenere = cipherFactory.getVigenereCipher("abc");
        assertEquals("agesc", vigenere.encrypt("zebra"));
        assertEquals("uqpptupy", vigenere.encrypt("tomorrow"));
    }

    @Test
    void testBasicRandom() {
        Cipher random = cipherFactory.getRandomSubstitutionCipher();
        String s = "albatross";
        assertEquals(s, random.decrypt(random.encrypt(s)));
        assertEquals(s, random.encrypt(random.decrypt(s)));
    }

    @Test
    void testBasicRSA() throws Exception {
        Cipher rsa = cipherFactory.getRSACipher();
        String s = "This is a larger test";
        File f = new File("examples/temp.rsa");
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        f.createNewFile();
        rsa.encrypt(new ByteArrayInputStream(s.getBytes()), fileOutputStream);
        fileOutputStream.flush();
        FileInputStream fileInputStream = new FileInputStream(f);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(3);
        rsa.decrypt(fileInputStream, arrayOutputStream);
        arrayOutputStream.flush();
        assertEquals(s, arrayOutputStream.toString().trim());
        f.delete();
    }
}
