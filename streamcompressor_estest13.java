package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.zip.ZipEntry;

/**
 * Contains tests for the {@link StreamCompressor} class.
 * This class focuses on improving a specific auto-generated test case.
 */
public class StreamCompressor_ESTestTest13 extends StreamCompressor_ESTest_scaffolding {

    /**
     * Verifies that the write() method throws a NullPointerException
     * when called with a null byte array buffer. The public contract
     * of the method should prevent null inputs, and this test ensures
     * that contract is enforced.
     */
    @Test(expected = NullPointerException.class)
    public void writeWithNullBufferShouldThrowNullPointerException() throws IOException {
        // Arrange: Create a StreamCompressor with a dummy output stream.
        // A PipedOutputStream is used here as a simple, valid OutputStream.
        OutputStream dummyOutputStream = new PipedOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(dummyOutputStream);

        byte[] nullBuffer = null;
        int offset = 0;
        int length = 0;

        // Act & Assert: Calling write with a null buffer should throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        streamCompressor.write(nullBuffer, offset, length, ZipEntry.DEFLATED);
    }
}