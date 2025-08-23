package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.OutputStream;
import org.junit.Test;

/**
 * Tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that calling close() on an ArArchiveOutputStream constructed with a
     * null OutputStream throws a NullPointerException.
     */
    @Test
    public void closeWithNullOutputStreamThrowsException() throws IOException {
        // Arrange: Create an instance with a null underlying stream.
        ArArchiveOutputStream archiveOutputStream = new ArArchiveOutputStream((OutputStream) null);

        // Act & Assert: Verify that calling close() throws a NullPointerException.
        // The exception is expected because the stream tries to close a null output stream.
        assertThrows(NullPointerException.class, archiveOutputStream::close);
    }

    /**
     * Alternative implementation using JUnit 4's 'expected' parameter.
     * This is a common pattern in older JUnit 4 codebases.
     */
    @Test(expected = NullPointerException.class)
    public void closeWithNullOutputStreamThrowsException_withExpected() throws IOException {
        // Arrange
        ArArchiveOutputStream archiveOutputStream = new ArArchiveOutputStream((OutputStream) null);

        // Act: This call is expected to throw the NullPointerException.
        archiveOutputStream.close();
    }
}