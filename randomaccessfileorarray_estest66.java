package com.itextpdf.text.pdf;

import com.itextpdf.text.io.IndependentRandomAccessSource;
import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;

import java.io.IOException;

/**
 * This test case verifies the behavior of the RandomAccessFileOrArray class
 * when it is initialized with a null underlying data source.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readUnsignedByte() throws a NullPointerException if the
     * underlying RandomAccessSource is null.
     *
     * This ensures that the class fails fast when its dependencies are not
     * correctly initialized, rather than masking the problem or causing
     * unexpected behavior later.
     */
    @Test(expected = NullPointerException.class)
    public void readUnsignedByte_whenSourceIsNull_throwsNullPointerException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance backed by a source
        // that wraps a null delegate. This simulates an invalid state.
        IndependentRandomAccessSource sourceWithNullDelegate = new IndependentRandomAccessSource((RandomAccessSource) null);
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceWithNullDelegate);

        // Act & Assert: Attempting to read from the fileOrArray should propagate
        // the NullPointerException from the underlying source. The @Test annotation
        // expects this exception, so the test will pass if it's thrown.
        fileOrArray.readUnsignedByte();
    }
}