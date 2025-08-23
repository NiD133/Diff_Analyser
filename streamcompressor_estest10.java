package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link StreamCompressor} class.
 */
public class StreamCompressorTest {

    /**
     * Verifies that the writeCounted method throws an exception when provided with a negative offset.
     * The underlying OutputStream is responsible for this bounds check.
     */
    @Test
    public void writeCountedShouldThrowExceptionForNegativeOffset() throws IOException {
        // Arrange: Create a StreamCompressor and prepare test data with an invalid negative offset.
        OutputStream outputStream = new ByteArrayOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(outputStream);

        byte[] data = new byte[10];
        int invalidNegativeOffset = -1;
        int validLength = 5;

        // Act & Assert: Expect an ArrayIndexOutOfBoundsException when calling writeCounted.
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            streamCompressor.writeCounted(data, invalidNegativeOffset, validLength);
        });
    }
}