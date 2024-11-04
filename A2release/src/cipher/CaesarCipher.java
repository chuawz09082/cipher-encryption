package cipher;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CaesarCipher implements Cipher{
    private int shift;

    public CaesarCipher(int shift){
        this.shift = shift;
    }

    @Override
    public void encrypt(InputStream in, OutputStream out) throws IOException {
        int bytedata;
        while ((bytedata = in.read()) != -1){
            char c = Character.toLowerCase((char) bytedata);
            if (c >= 'a'  && c <= 'z'){
                char newc = (char) ((c - 'a'+shift)%26+'a');
                out.write(newc);
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
                char newc = (char) ((c - 'a'+26 - shift)%26 + 'a');
                out.write(newc);
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
                char newc = (char)('a' + (c-'a'+shift)%26);
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
        String ciphertext_lower = ciphertext.toLowerCase().replaceAll("[^a-z ]","");
        for (int i = 0; i < ciphertext_lower.length(); i += 1){
            char c = ciphertext_lower.charAt(i);
            if (c >= 'a' && c <= 'z'){
                char newc = (char) ((c - 'a' + 26 - shift)%26+'a');
                decryptedText.append(newc);
            }
            else {
                decryptedText.append(c);
            }
        }
        return decryptedText.toString();
    }

    @Override
    public void save(OutputStream out) throws IOException {
        String ciphertype = "MONO\n";
        out.write(ciphertype.getBytes());

        for (int i = 0; i < 26; i += 1){
            char C = (char) ('A' + (i+shift)%26);
            out.write(C);
        }

    }



}
