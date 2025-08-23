package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.OutputStream;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the StreamCompressor class.
 * The original class name StreamCompressor_ESTestTest34 is kept to reflect its origin
 * from a generated test suite.
 */
public class StreamCompressor_ESTestTest34 extends StreamCompressor_ESTest_scaffolding {

    /**
     * Verifies that a newly created StreamCompressor reports zero bytes read
     * before any data has been processed.
     */
    @Test(timeout = 4000)
    public void getBytesReadShouldReturnZeroForNewCompressor() {
        // Arrange: Create a new StreamCompressor instance.
        // A PipedOutputStream is a simple way to satisfy the factory method's requirement.
        OutputStream dummyOutputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(dummyOutputStream);

        // Act: Get the number of bytes read from the new instance.
        long bytesRead = compressor.getBytesRead();

        // Assert: The number of bytes read should be zero.
        assertEquals("A newly created StreamCompressor should have zero bytes read.", 0L, bytesRead);
    }
}