package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import java.nio.channels.SeekableByteChannel;
import java.util.zip.Deflater;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link StreamCompressor} class.
 */
public class StreamCompressorTest {

    /**
     * Verifies that a newly created StreamCompressor instance correctly initializes
     * its "bytes read" counter to zero.
     */
    @Test
    public void newlyCreatedCompressorShouldHaveZeroBytesRead() {
        // Arrange
        Deflater deflater = new Deflater();
        // The factory method is tested with a null output channel, which creates
        // a valid compressor instance.
        SeekableByteChannel nullOutputChannel = null;

        // Act
        StreamCompressor compressor = StreamCompressor.create(nullOutputChannel, deflater);

        // Assert
        long expectedBytesRead = 0L;
        long actualBytesRead = compressor.getBytesRead();
        assertEquals("A new StreamCompressor should report zero bytes read upon creation.",
                     expectedBytesRead, actualBytesRead);
    }
}