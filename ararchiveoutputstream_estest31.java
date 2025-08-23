package org.apache.commons.compress.archivers.ar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Unit tests for the {@link ArArchiveOutputStream} class.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Verifies that calling close() on an already closed ArArchiveOutputStream
     * is an idempotent operation and does not throw an exception.
     *
     * @throws IOException if an I/O error occurs during the first close.
     */
    @Test
    public void closeCanBeCalledMultipleTimesWithoutError() throws IOException {
        // Arrange: Create an ArArchiveOutputStream and close it.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(outputStream);
        arOutputStream.close();

        // Act: Call close() again on the already closed stream.
        arOutputStream.close();

        // Assert: The test succeeds if no exception is thrown during the second call to close().
        // This confirms the idempotency of the close() method.
    }
}