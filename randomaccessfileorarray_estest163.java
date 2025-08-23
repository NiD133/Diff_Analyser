package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;

import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray
     * that was initialized with a null data source results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void readMethods_shouldThrowNullPointerException_whenSourceIsNull() throws IOException {
        // Arrange: Create an instance with a null internal data source.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);

        // Act: Attempt to read from the instance.
        // This is expected to throw a NullPointerException because the internal 'byteSource' is null.
        fileOrArray.readDoubleLE();

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected) annotation.
    }
}