package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the static utility methods of the {@link MappedRandomAccessFile} class.
 */
public class MappedRandomAccessFileTest {

    /**
     * Verifies that the clean() method gracefully handles a null ByteBuffer input.
     *
     * The method is expected to return false to indicate that the cleanup operation
     * was not successful, rather than throwing a NullPointerException.
     */
    @Test
    public void clean_whenBufferIsNull_shouldReturnFalse() {
        // Arrange: Define a null ByteBuffer to pass to the method.
        java.nio.ByteBuffer nullBuffer = null;

        // Act: Call the static clean method with the null buffer.
        boolean wasCleaned = MappedRandomAccessFile.clean(nullBuffer);

        // Assert: The method should return false, indicating the operation failed.
        assertFalse("The clean() method should return false for a null input.", wasCleaned);
    }
}