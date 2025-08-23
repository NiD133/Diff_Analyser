package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
// The original test class name and inheritance are preserved.
public class RandomAccessFileOrArray_ESTestTest69 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that calling readString() on an instance created with a null
     * RandomAccessSource throws a NullPointerException.
     * <p>
     * This is the expected behavior, as the underlying source is required for
     * any read operations.
     */
    @Test(expected = NullPointerException.class)
    public void readStringWithNullSourceThrowsNullPointerException() throws Exception {
        // Arrange: Create an instance of RandomAccessFileOrArray with a null data source.
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);

        // Act: Attempt to read a string. This action is expected to throw the exception.
        // The arguments for length and encoding are representative values; the NPE
        // should occur before they are actually used.
        randomAccessFileOrArray.readString(10, "UTF-8");

        // Assert: The @Test(expected) annotation automatically verifies that a
        // NullPointerException was thrown. No further assertions are needed.
    }
}