package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Unit tests for the {@link ArArchiveOutputStream} class.
 *
 * Note: The original test class name "ArArchiveOutputStream_ESTestTest11" and
 * method name "test10" were auto-generated and have been renamed for clarity.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Verifies that calling the write() method with a null buffer
     * throws a NullPointerException, as expected by the general contract of
     * {@link java.io.OutputStream#write(byte[], int, int)}.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void writeWithNullBufferShouldThrowNullPointerException() throws IOException {
        // Arrange: Create an ArArchiveOutputStream backed by an in-memory stream.
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ArArchiveOutputStream arOutStream = new ArArchiveOutputStream(output);

        // Act: Attempt to write from a null buffer.
        // The @Test(expected=...) annotation will assert that a NullPointerException is thrown.
        arOutStream.write(null, 1, 1);
    }
}