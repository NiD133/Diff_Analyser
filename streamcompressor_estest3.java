package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link StreamCompressor} class.
 * Note: The original test class name "StreamCompressor_ESTestTest3" was preserved,
 * but a more descriptive name like "StreamCompressorTest" would be preferable.
 */
public class StreamCompressor_ESTestTest3 {

    /**
     * Verifies that calling writeCounted() correctly updates the total number of bytes written.
     */
    @Test
    public void writeCountedShouldUpdateTotalBytesWritten() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(outputStream);

        byte[] dataToWrite = new byte[]{10, 20, 30, 40};
        int offset = 2;
        int length = 2; // We are writing the last two bytes: {30, 40}

        // Act
        streamCompressor.writeCounted(dataToWrite, offset, length);

        // Assert
        long expectedBytesWritten = 2L;
        assertEquals("The total bytes written should match the specified length.",
                expectedBytesWritten, streamCompressor.getTotalBytesWritten());
    }
}