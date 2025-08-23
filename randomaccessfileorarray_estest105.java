package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * This test suite focuses on the constructor behavior of the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArray_ESTestTest105 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that the constructor {@link RandomAccessFileOrArray#RandomAccessFileOrArray(String)}
     * throws a NullPointerException when the filename argument is null.
     * This is the expected behavior as the underlying file handling mechanism cannot operate on a null path.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullFilename_shouldThrowNullPointerException() throws IOException {
        // Act: Attempt to create an instance with a null filename.
        // The cast to (String) is necessary to resolve ambiguity between multiple constructors.
        new RandomAccessFileOrArray((String) null);
        
        // Assert: The @Test(expected) annotation automatically verifies that a 
        // NullPointerException is thrown, causing the test to pass.
        // If no exception is thrown, the test will fail.
    }
}