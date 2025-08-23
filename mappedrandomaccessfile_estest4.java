package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Unit tests for the {@link MappedRandomAccessFile} class.
 */
public class MappedRandomAccessFileTest {

    /**
     * Verifies that the constructor throws a {@link NullPointerException}
     * when the filename argument is null.
     *
     * This test ensures that the constructor correctly handles invalid null input
     * for the file path, preventing potential NullPointerExceptions in downstream code.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_shouldThrowNullPointerException_whenFilenameIsNull() throws IOException {
        // Act & Assert
        // Attempt to create a MappedRandomAccessFile with a null filename.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        new MappedRandomAccessFile(null, "rw");
    }
}