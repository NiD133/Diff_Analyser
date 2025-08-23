package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;

import java.io.IOException;

/**
 * This test class contains tests for the {@link RandomAccessFileOrArray} class.
 * This specific test focuses on its behavior when initialized with a null source.
 */
public class RandomAccessFileOrArray_ESTestTest76 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that calling readLong() on an instance created with a null RandomAccessSource
     * throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void readLongThrowsNullPointerExceptionWhenSourceIsNull() throws IOException {
        // Arrange: Create an instance of RandomAccessFileOrArray with a null underlying source.
        // The cast is necessary to resolve constructor ambiguity.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);

        // Act & Assert: Attempting to read from the null source should immediately throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        fileOrArray.readLong();
    }
}