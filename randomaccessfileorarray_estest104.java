package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the constructors of the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayConstructorTest {

    /**
     * Verifies that the constructor throws an IOException when an empty string is provided
     * as a filename. An empty string is not a valid file path, so the constructor
     * is expected to fail.
     *
     * This test covers the deprecated constructor:
     * {@code RandomAccessFileOrArray(String filename, boolean forceRead, boolean plainRandomAccess)}
     */
    @Test(expected = IOException.class)
    public void constructor_shouldThrowIOException_whenFilenameIsEmpty() throws IOException {
        // Act & Assert
        // Attempt to create an instance with an empty string for the filename.
        // This is expected to throw an IOException because the path is invalid.
        new RandomAccessFileOrArray("", false, false);
    }
}