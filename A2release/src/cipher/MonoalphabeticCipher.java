package cipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MonoalphabeticCipher implements  Cipher{
    private String encrAlph;
    private String encryAlph_lower;
    private String decryAlph_lower;

    public MonoalphabeticCipher(String encrAlph){
        this.encrAlph = encrAlph;
        this.encryAlph_lower = encrAlph.toLowerCase();

        char[] decryptchar = new char[26];

        for (int i = 0; i < 26; i += 1){
            int decr = encryAlph_lower.charAt(i) - 'a';
            decryptchar[decr] = (char) ('a' + i);
        }

        decryAlph_lower = new String(decryptchar);
    }
    @Override
    public void encrypt(InputStream in, OutputStream out) throws IOException {
        int bytedata;
        while ((bytedata = in.read()) != -1){
            char c = Character.toLowerCase((char) bytedata);
            if (c >= 'a' && c <= 'z'){
                out.write(encryAlph_lower.charAt(c - 'a'));
            }
            else if (c == ' '){
                out.write(c);
            }
        }
    }

    @Override
    public void decrypt(InputStream in, OutputStream out) throws IOException {
        int bytedata;
        while ((bytedata = in.read()) != -1){
            char c = Character.toLowerCase((char) bytedata);
            if (c >= 'a' && c <= 'z'){
                out.write(decryAlph_lower.charAt(c - 'a'));
            }
            else if (c == ' '){
                out.write(c);
            }
        }

    }

    @Override
    public String encrypt(String plaintext) {
        StringBuilder encryptedText = new StringBuilder();
        String plaintext_lower = plaintext.toLowerCase().replaceAll("[^a-z ]", "");
        for (int i = 0; i < plaintext_lower.length(); i += 1){
            char c = plaintext_lower.charAt(i);
            if (c >= 'a' && c <= 'z'){
                char newc = encryAlph_lower.charAt(c - 'a');
                encryptedText.append(newc);
            }
            else {
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }

    @Override
    public String decrypt(String ciphertext) {
        StringBuilder decryptedText = new StringBuilder();
        String ciphertext_lower = ciphertext.toLowerCase().replaceAll("[^a-z ]", "");
        for (int i = 0; i < ciphertext_lower.length(); i += 1){
            char c = ciphertext_lower.charAt(i);
            if (c >= 'a' && c <= 'z'){
                char newc = decryAlph_lower.charAt(c - 'a');
                decryptedText.append(newc);
            }
            else if (c == ' '){
                decryptedText.append(c);
            }
        }
        return decryptedText.toString();
    }

    @Override
    public void save(OutputStream out) throws IOException {
        String ciphertype = "MONO\n";
        out.write(ciphertype.getBytes());
        out.write(encrAlph.getBytes());
    }
}
