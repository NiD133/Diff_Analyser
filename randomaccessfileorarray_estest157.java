package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that the constructor correctly propagates an IOException
     * when the provided InputStream fails during a read operation.
     *
     * The constructor for RandomAccessFileOrArray attempts to read the entire
     * content of the given InputStream. This test supplies an unconnected
     * PipedInputStream, which is guaranteed to throw an IOException upon the
     * first read attempt, ensuring the constructor's error handling is robust.
     */
    @Test
    public void constructorWithFailingInputStreamShouldThrowIOException() {
        // Arrange: Create an input stream that is known to fail on read.
        // A PipedInputStream that is not connected to a PipedOutputStream
        // will throw an IOException("Pipe not connected") on any read attempt.
        PipedInputStream failingInputStream = new PipedInputStream();

        try {
            // Act: Attempt to construct the object with the failing stream.
            new RandomAccessFileOrArray(failingInputStream);
            
            // Assert: If the constructor completes without an exception, the test fails.
            fail("Expected an IOException to be thrown because the input stream cannot be read.");
        } catch (IOException e) {
            // Assert: Verify that the correct IOException was thrown.
            // The message "Pipe not connected" is the specific behavior of PipedInputStream.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}