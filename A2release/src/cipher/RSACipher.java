package cipher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RSACipher implements Cipher{
    private static final int CHUNK_SIZE = 127;
    private static final int ENCRYPTED_CHUNK_SIZE = 128;

    private BigInteger d;
    private BigInteger e;
    private BigInteger n;

    public RSACipher(BigInteger d, BigInteger e, BigInteger n){
        this.d = d;
        this.e = e;
        this.n = n;
    }

    @Override
    public void encrypt(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = in.readAllBytes();
        byte[] encryptedBytes = encryptBytes(buffer);
        out.write(encryptedBytes);
    }

    @Override
    public void decrypt(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = in.readAllBytes();
        byte[] decryptedBytes = decryptBytes(buffer);
        out.write(decryptedBytes);
    }

    @Override
    public String encrypt(String plaintext) {
        try {
            byte[] plaintextBytes = plaintext.getBytes();
            byte[] encryptedBytes = encryptBytes(plaintextBytes);
            return new BigInteger(1,encryptedBytes).toString(16);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public String decrypt(String ciphertext) {
        try {
            byte[] ciphertextBytes = new BigInteger(ciphertext,16).toByteArray();
            byte[] decryptedBytes = decryptBytes(ciphertextBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void save(OutputStream out) throws IOException {
        String ciphertype = "RSA\n";
        out.write(ciphertype.getBytes());
        out.write((d.toString() + "\n").getBytes());
        out.write((e.toString() + "\n").getBytes());
        out.write((n.toString() + "\n").getBytes());
    }

    private byte[] encryptBytes(byte[] message) throws IOException {
        List<byte[]> chunks = chunkMessage(message);
        List<BigInteger> encryptedChunks = new ArrayList<>();
        for (byte[] chunk: chunks){
            BigInteger chunkBigInt = new BigInteger(1,chunk);
            BigInteger encryptedChunk = chunkBigInt.modPow(e,n);
            encryptedChunks.add(encryptedChunk);
        }
        return combineChunks(encryptedChunks);
    }

    private byte[] decryptBytes(byte[] encryptedMessage){
        List<BigInteger> encryptedChunks = splitChunks(encryptedMessage);
        List<byte[]> decryptedChunks = new ArrayList<>();
        for (BigInteger encryptedChunk: encryptedChunks){
            BigInteger decryptedBigInt = encryptedChunk.modPow(d,n);
            byte[] decryptedChunk = decryptedBigInt.toByteArray();
            System.out.println("Decrypted Chunk: " + Arrays.toString(decryptedChunk));
            if (decryptedChunk.length < CHUNK_SIZE){
                byte[] paddedChunk = new byte[CHUNK_SIZE];
                System.arraycopy(decryptedChunk,0,paddedChunk,CHUNK_SIZE - decryptedChunk.length,decryptedChunk.length);
                decryptedChunks.add(paddedChunk);
            }
            else {
                decryptedChunks.add(decryptedChunk);
            }
        }
        return unchunkMessage(decryptedChunks);
    }

    private List<byte[]> chunkMessage(byte[] message){
        List<byte[]> chunks = new ArrayList<>();
        int totallength = message.length;
        int offset = 0;

        while (offset < totallength){
            byte[] chunk = new byte[CHUNK_SIZE];
            int datalength = Math.min(126,totallength - offset);
            chunk[0] = (byte) datalength;
            System.arraycopy(message,offset,chunk,1,datalength);
            chunks.add(chunk);
            offset += datalength;
        }
        return chunks;
    }

    private byte[] unchunkMessage(List<byte[]> decryptedChunks) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (byte[] chunk: decryptedChunks){
            int datalength = chunk[0];
            outputStream.write(chunk,1,datalength);
        }
        return outputStream.toByteArray();

    }

    private byte[] combineChunks(List<BigInteger> encryptedChunks) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (BigInteger encryptedChunk: encryptedChunks){
            byte[] chunkBytes = encryptedChunk.toByteArray();
            if (chunkBytes.length < ENCRYPTED_CHUNK_SIZE){
                byte[] paddedChunk = new byte[ENCRYPTED_CHUNK_SIZE];
                System.arraycopy(chunkBytes,0,paddedChunk,ENCRYPTED_CHUNK_SIZE - chunkBytes.length,chunkBytes.length);
                outputStream.write(paddedChunk);
            }
            else {
                outputStream.write(chunkBytes);
            }
        }
        return outputStream.toByteArray();
    }

    private List<BigInteger> splitChunks(byte[] encryptedMessage){
        List<BigInteger> encryptedChunks = new ArrayList<>();
        int totalLength = encryptedMessage.length;
        int offset = 0;
        while (offset < totalLength){
            byte[] chunk = new byte[ENCRYPTED_CHUNK_SIZE];
            System.arraycopy(encryptedMessage,offset,chunk,0,ENCRYPTED_CHUNK_SIZE);
            encryptedChunks.add(new BigInteger(1,chunk));
            offset += ENCRYPTED_CHUNK_SIZE;
        }
        return encryptedChunks;
    }
}
