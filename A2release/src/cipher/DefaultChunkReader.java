package cipher;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class DefaultChunkReader implements ChunkReader{
    private final InputStream in;
    private boolean hasmoreData = true;
    private final int chunkSize;

    public DefaultChunkReader(InputStream in, int chunkSize){
        this.in = in;
        this.chunkSize = chunkSize;
    }
    @Override
    public int chunkSize() {
        return 0;
    }

    @Override
    public boolean hasNext() {
        return hasmoreData;
    }

    @Override
    public int nextChunk(byte[] data) throws EOFException, IOException {
        return 0;
    }
}
