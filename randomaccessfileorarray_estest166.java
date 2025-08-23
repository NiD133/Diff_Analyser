package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;
import java.io.IOException;

/**
 * This class contains tests for the RandomAccessFileOrArray class.
 * Note: The class name and inheritance are preserved from the original EvoSuite-generated code.
 * In a typical, human-written test suite, this class would be named RandomAccessFileOrArrayTest.
 */
public class RandomAccessFileOrArray_ESTestTest166 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that skipBytes() throws a NullPointerException when the RandomAccessFileOrArray
     * is constructed with a null data source. This ensures the method correctly handles an
     * invalid object state and prevents unexpected runtime errors.
     */
    @Test(expected = NullPointerException.class)
    public void skipBytesThrowsNullPointerExceptionWhenConstructedWithNullSource() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance with a null internal source.
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray((RandomAccessSource) null);

        // Act: Attempt to skip bytes. This should fail because the internal source is null.
        // Assert: The @Test(expected) annotation verifies that a NullPointerException is thrown.
        randomAccessFileOrArray.skipBytes(127);
    }
}