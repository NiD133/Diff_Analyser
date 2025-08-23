package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This test class contains tests for the constructors of the {@link RandomAccessFileOrArray} class.
 */
// The original class name and scaffolding are kept to match the provided context.
public class RandomAccessFileOrArray_ESTestTest160 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that the constructor throws a FileNotFoundException when attempting
     * to open a file with an empty string as its path. An empty string is an
     * invalid file path, and the operation is expected to fail.
     */
    @Test(timeout = 4000, expected = FileNotFoundException.class)
    public void constructorWithEmptyFileName_throwsFileNotFoundException() throws IOException {
        // Arrange: An empty string representing an invalid file path.
        String invalidFileName = "";
        boolean forceRead = true;
        boolean plainRandomAccess = true;

        // Act & Assert: Attempting to create a RandomAccessFileOrArray with this path
        // should throw a FileNotFoundException. The @Test(expected=...) annotation handles the assertion.
        new RandomAccessFileOrArray(invalidFileName, forceRead, plainRandomAccess);
    }
}