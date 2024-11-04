package cipher;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Command line interface to allow users to interact with your ciphers.
 *
 * <p>We have provided some infrastructure to parse most of the arguments. It is your responsibility
 * to implement the appropriate actions according to the assignment specifications. You may choose
 * to "fill in the blanks" or rewrite this class.
 *
 * <p>Regardless of which option you choose, remember to minimize repetitive code. You are welcome
 * to add additional methods or alter the provided code to achieve this.
 */
public class Main {
    private static final int ENCRYPTED_CHUNK_SIZE = 128;
    private static Cipher cipher;
    private static String msg;
    private static String inputFile;
    private static String outputFile;
    private static String saveFile;
    private static String outFile;
    public static void main(String[] args) {
        Main main = new Main();
        try {
            int pos = 0;
            pos = main.parseCipherType(args,pos);
            pos = main.parseCipherFunction(args,pos);
            pos = main.parseOutputOptions(args,pos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // TODO implement
    }

    /**
     * Set up the cipher type based on the options found in args starting at position pos, and
     * return the index into args just past any cipher type options.
     */
    private int parseCipherType(String[] args, int pos) throws IllegalArgumentException, IOException {
        // check if arguments are exhausted
        if (pos == args.length) return pos;

        String cmdFlag = args[pos++];
        CipherFactory cipherFactory = new CipherFactory();
        switch (cmdFlag) {
            case "--caesar":
                int shift = Integer.parseInt(args[pos++]);
                cipher = cipherFactory.getCaesarCipher(shift);
                // TODO create a Caesar cipher object with the given shift parameter
                break;
            case "--random":
                cipher = cipherFactory.getRandomSubstitutionCipher();
                // TODO create a random substitution cipher object
                break;
            case "--monoLoad":
                String encrAlph = "";
                try (BufferedReader reader = new BufferedReader(new FileReader(args[pos++]))) {
                    reader.readLine();
                    encrAlph = reader.readLine();
                }
                catch (IOException e) {
                    System.err.println("File could not be read." );
                }

                cipher = cipherFactory.getMonoCipher(encrAlph);
                // TODO load a monoaphabetic substitution cipher from a file
                break;
            case "--vigenere":
                cipher = cipherFactory.getVigenereCipher(args[pos++]);
                // TODO create a new Vigenere Cipher with the given key
                break;
            case "--vigenereLoad":
                String key = "";
                try (BufferedReader reader = new BufferedReader(new FileReader(args[pos++]))) {
                    reader.readLine();
                    key  = reader.readLine();
                }
                catch (IOException e) {
                    System.err.println("File could not be read." );
                }

                cipher = cipherFactory.getVigenereCipher(key);
                // TODO create a Vigenere cipher with key loaded from the given file
                break;
            case "--rsa":
                cipher = cipherFactory.getRSACipher();
                // TODO create new RSA cipher
                break;
            case "--rsaLoad":
                BigInteger d = null;
                BigInteger e = null;
                BigInteger n = null;
                try (BufferedReader reader = new BufferedReader(new FileReader(args[pos++]))) {
                    reader.readLine();
                    d = new BigInteger(reader.readLine());
                    e = new BigInteger(reader.readLine());
                    n = new BigInteger(reader.readLine());

                }
                System.out.println("Private Key (d): " + d);
                System.out.println("Public Exponent (e): " + e);
                System.out.println("Modulus (n): " + n);
                cipher = cipherFactory.getRSACipher(d, e, n);
                // TODO load an RSA key from the given file
                break;
            default:
                throw new IllegalArgumentException("Unknown cipher type: " + cmdFlag);// TODO
        }
        return pos;
    }

    /**
     * Parse the operations to be performed by the program from the command-line arguments in args
     * starting at position pos. Return the index into args just past the parsed arguments.
     */
    private int parseCipherFunction(String[] args, int pos) throws IllegalArgumentException {
        // check if arguments are exhausted
        if (pos == args.length) return pos;

        switch (args[pos++]) {
            case "--em":
                msg = cipher.encrypt(args[pos++]);
                // TODO encrypt the given string
                break;
            case "--ef":
                msg = "";
                try (InputStream in = new FileInputStream(args[pos++]);
                        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    cipher.encrypt(in, out);
                    msg = new String(out.toByteArray());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // TODO encrypt the contents of the given file
                break;
            case "--dm":
                msg = cipher.decrypt(args[pos++]);
                // TODO decrypt the given string -- substitution ciphers only
                break;
            case "--df":
                inputFile = args[pos++];  // Get the encrypted file (encr.txt)
                System.out.println("Decrypting file: " + inputFile);
                msg = "";  // Initialize msg for the decrypted content

                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                    String fileContent = reader.readLine();  // Read the encrypted hex string from the file
                    System.out.println("Contents of encr.txt (hex string): " + fileContent);

                    // Check if the cipher is RSA, which requires hex string conversion
                    if (cipher instanceof RSACipher) {
                        // Trim and verify the hex string to ensure it is clean of whitespace/newlines
                        fileContent = fileContent.trim();
                        if (fileContent.length() % 2 != 0) {
                            throw new RuntimeException("Hex string length is not valid. It should be even.");
                        }

                        // Convert hex string to byte array for RSA decryption
                        byte[] encryptedBytes = hexStringToByteArray(fileContent);
                        System.out.println("Converted byte array (encrypted): " + Arrays.toString(encryptedBytes));

                        // Ensure we have a reasonable length for RSA decryption (128 bytes for 1024-bit RSA)
                        if (encryptedBytes.length % ENCRYPTED_CHUNK_SIZE != 0) {
                            System.out.println("Warning: Encrypted byte array length (" + encryptedBytes.length +
                                    ") may not align with RSA chunk size.");
                        }

                        // Decrypt the byte array using RSA
                        cipher.decrypt(new ByteArrayInputStream(encryptedBytes), out);
                        byte[] decryptedBytes = out.toByteArray();
                        System.out.println("Decrypted byte array: " + Arrays.toString(decryptedBytes));

                        // Convert decrypted bytes back to string (UTF-8 encoded text)
                        msg = new String(decryptedBytes, StandardCharsets.UTF_8).trim();
                        System.out.println("Decrypted message: " + msg);
                    } else {
                        // For Mono, Caesar, or other substitution ciphers, handle as a regular string
                        msg = cipher.decrypt(fileContent);  // Decrypt the string directly
                        System.out.println("Decrypted message (non-RSA): " + msg);
                    }

                    System.out.println("Decryption completed.");

                } catch (IOException e) {
                    throw new RuntimeException("IO error during decryption: " + e.getMessage());
                }
                break;
            default:
                // TODO
        }
        return pos;
    }

    /**
     * Parse options for output, starting within {@code args} at index {@code argPos}. Return the
     * index in args just past such options.
     */
    private int parseOutputOptions(String[] args, int pos) throws IllegalArgumentException {
        // check if arguments are exhausted
        if (pos == args.length) return pos;

        String cmdFlag;
        while (pos < args.length) {
            switch (cmdFlag = args[pos++]) {
                case "--print":
                    // TODO print result of applying the cipher to the console -- substitution
                    // ciphers only
                    System.out.println(msg);
                    break;
                case "--out":
                    // TODO output result of applying the cipher to a file
                    outFile = args[pos++];
                    try (OutputStream out = new FileOutputStream(outFile)) {
                        if (cipher instanceof RSACipher) {
                            // RSA: Write the hexadecimal string directly to the output file
                            out.write(msg.getBytes(StandardCharsets.UTF_8));  // Write hex string to file
                            System.out.println("Hexadecimal output written to: " + outFile);
                        } else {
                            // Mono/Other Ciphers: Write msg as a regular string
                            out.write(msg.getBytes(StandardCharsets.UTF_8));  // Write regular string to file
                            System.out.println("Regular string output written to: " + outFile);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Error writing to output file: " + e.getMessage());
                    }
                    break;
                case "--save":
                    saveFile = args[pos++];
                    try (OutputStream out = new FileOutputStream(saveFile)){
                        cipher.save(out);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // TODO save the cipher key to a file
                    break;
                default:
                    // TODO
            }
        }
        return pos;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String byteArrayToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));  // Format byte as two hexadecimal digits
        }
        return sb.toString();
    }

    public static void testHexConversion() {
        String hexString = "5f72f295b917d5e29f554e35e359eafa";  // Add part of your hex string
        byte[] byteArray = hexStringToByteArray(hexString);
        System.out.println("Hex string: " + hexString);
        System.out.println("Byte array: " + Arrays.toString(byteArray));
    }
}
