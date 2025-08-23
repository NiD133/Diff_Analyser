package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling readFloat() on an instance created with a null
     * RandomAccessSource throws a NullPointerException.
     *
     * This test ensures that the class correctly handles the invalid state of having
     * no underlying data source when a read operation is attempted.
     */
    @Test(expected = NullPointerException.class)
    public void readFloat_whenSourceIsNull_throwsNullPointerException() {
        // Arrange: Create an instance of RandomAccessFileOrArray with a null data source.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);

        // Act: Attempt to read a float. This action is expected to throw the exception.
        fileOrArray.readFloat();

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // in the @Test annotation. No further assertion is needed.
    }
}