package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link ParallelScatterZipCreator}.
 */
public class ParallelScatterZipCreatorTest {

    /**
     * Verifies that addArchiveEntry() throws an IllegalArgumentException
     * when the ZipArchiveEntry does not have a compression method set.
     */
    @Test
    public void addArchiveEntryShouldThrowExceptionForEntryWithUnsetMethod() {
        // Arrange
        final ParallelScatterZipCreator scatterCreator = new ParallelScatterZipCreator();
        final String entryName = "entry-with-unset-method";
        final ZipArchiveEntry entry = new ZipArchiveEntry(entryName);

        // A new ZipArchiveEntry has its compression method as METHOD_UNSPECIFIED (-1).
        // The addArchiveEntry method requires a specific compression method (e.g., DEFLATED or STORED)
        // to be set before an entry can be processed.
        // The InputStreamSupplier can be null because the method check occurs before it is used.

        // Act & Assert
        try {
            scatterCreator.addArchiveEntry(entry, null);
            fail("Expected an IllegalArgumentException because the compression method was not set.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is clear and correct.
            final String expectedMessage = "Method must be set on zipArchiveEntry: " + entryName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}