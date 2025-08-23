package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * This test class contains tests for the {@link RandomAccessFileOrArray} class.
 * This specific test focuses on the behavior of its constructor when handling invalid file paths.
 */
// The original class name and inheritance are preserved to maintain context.
public class RandomAccessFileOrArray_ESTestTest165 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that the constructor throws an IOException when an empty file name is provided.
     * An empty string is not a valid file path, so the constructor is expected to fail
     * with an I/O-related exception.
     */
    @Test(expected = IOException.class)
    public void constructor_withEmptyFileName_shouldThrowIOException() throws IOException {
        // Attempting to instantiate with an empty file name should fail.
        new RandomAccessFileOrArray("");
    }
}