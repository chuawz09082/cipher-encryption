package cipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Vigenere implements Cipher{
    private String key;
    private int keylength;
    private int count;

    public Vigenere(String key){
        this.key = key.toLowerCase();
        this.keylength = key.length();
        this.count = 0;

    }

    @Override
    public void encrypt(InputStream in, OutputStream out) throws IOException {
        int bytedata;
        count = 0;
        while ((bytedata = in.read()) != -1){
            char c = (char) bytedata;
            if (Character.isLetter(c)){
                char lowerc = Character.toLowerCase(c);
                char newc = (char) ('a' + (lowerc - 'a' + key.charAt(count%keylength) - 'a' + 1)%26);
                out.write(newc);
                count += 1;
            }
            else if (c == ' '){
                out.write(c);
            }
        }
    }

    @Override
    public void decrypt(InputStream in, OutputStream out) throws IOException {
        int bytedata;
        count = 0;
        while ((bytedata = in.read()) != -1){
            char c = (char) bytedata;
            if (Character.isLetter(c)){
                char lowerc = Character.toLowerCase(c);
                char newc = (char) ((lowerc - 'a' +26-key.charAt(count%keylength) + 'a' - 1)%26+'a');
                out.write(newc);
                count += 1;
            }
            else if (c == ' '){
                out.write(c);
            }
        }

    }

    @Override
    public String encrypt(String plaintext) {
        String plaintext_lower = plaintext.toLowerCase().replaceAll("[^a-z ]", "");
        StringBuilder encryptedText = new StringBuilder();
        count = 0;

        for (int i = 0; i < plaintext_lower.length(); i += 1){
            char c = plaintext_lower.charAt(i);
            if (c >= 'a' && c <= 'z'){
                char newc = (char)((c - 'a'+key.charAt(count%keylength) -'a'+1)%26+'a');
                encryptedText.append(newc);
                count += 1;
            }
            else if (c == ' '){
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }

    @Override
    public String decrypt(String ciphertext) {
        String ciphertext_lower = ciphertext.toLowerCase().replaceAll("[^a-z ]","");
        StringBuilder decryptedText = new StringBuilder();
        count = 0;
        for (int i = 0; i < ciphertext_lower.length(); i += 1){
            char c = ciphertext_lower.charAt(i);
            if (c >= 'a' && c <= 'z'){
                char newc = (char) ((c - 'a' + 26 - (key.charAt(count)-'a'+1))%26 +'a');
                decryptedText.append(newc);
                count += 1;
            }
            else if (c == ' '){
                decryptedText.append(c);
            }
        }
        return decryptedText.toString();
    }

    @Override
    public void save(OutputStream out) throws IOException {
        String ciphertype = "Vigenere\n";
        out.write(ciphertype.getBytes());
        out.write(key.toUpperCase().getBytes());
    }
}
