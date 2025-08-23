package org.apache.commons.compress.archivers.ar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Unit tests for the {@link ArArchiveOutputStream} class.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Verifies that calling putArchiveEntry() with a null argument
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void putArchiveEntryShouldThrowNullPointerExceptionWhenEntryIsNull() throws IOException {
        // Arrange: Create a valid ArArchiveOutputStream instance.
        // Using a ByteArrayOutputStream is better practice than passing null,
        // as it ensures the stream object itself is in a valid state.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ArArchiveOutputStream arArchiveOutputStream = new ArArchiveOutputStream(outputStream);

        // Act: Attempt to add a null entry to the archive.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        arArchiveOutputStream.putArchiveEntry(null);
    }
}