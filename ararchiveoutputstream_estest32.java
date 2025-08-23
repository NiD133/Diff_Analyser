package org.apache.commons.compress.archivers.ar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for {@link ArArchiveOutputStream}.
 * This class focuses on handling archive entries with invalid sizes.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Tests that attempting to write an archive entry with a size larger than the
     * AR format's header supports (10 decimal digits) throws an IOException.
     */
    @Test(timeout = 4000)
    public void writingEntryWithTooLargeSizeThrowsIOException() {
        // Arrange: Create an archive entry with a size that exceeds the format's limit.
        // The AR header uses a 10-byte field for size, so Long.MAX_VALUE is guaranteed to be too large.
        final ArArchiveEntry entryWithMaxSize = new ArArchiveEntry("test.txt", Long.MAX_VALUE);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream);

        // Act & Assert: Attempting to add the entry should fail with a specific IOException.
        try {
            arOut.putArchiveEntry(entryWithMaxSize);
            fail("Expected an IOException because the entry size is too large for the AR format.");
        } catch (final IOException e) {
            // Verify that the exception message clearly indicates the problem.
            assertEquals("Size too long", e.getMessage());
        }
    }
}