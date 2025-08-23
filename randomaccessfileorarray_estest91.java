package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling readByte() on an instance created with a null source
     * results in a NullPointerException. This ensures the class handles invalid
     * initial states gracefully by failing fast.
     */
    @Test(expected = NullPointerException.class)
    public void readByte_whenConstructedWithNullSource_throwsNullPointerException() throws Exception {
        // Arrange: Create an instance of RandomAccessFileOrArray with a null underlying source.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);

        // Act: Attempt to read a byte from the null source.
        // Assert: A NullPointerException is expected, as specified by the @Test annotation.
        fileOrArray.readByte();
    }
}