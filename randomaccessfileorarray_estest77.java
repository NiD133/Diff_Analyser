package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling readLine() after seeking to a negative position
     * throws an ArrayIndexOutOfBoundsException.
     *
     * This behavior is expected because seek() itself does not validate the position,
     * but the subsequent read operation fails when it attempts to access the
     * underlying byte array with a negative index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void readLine_shouldThrowArrayIndexOutOfBoundsException_whenPositionIsNegative() throws Exception {
        // Arrange: Create a RandomAccessFileOrArray backed by a simple byte array.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(sourceData);
        long invalidNegativePosition = -1L;

        // Act: Seek to an invalid negative position. The readLine() call is what
        // is expected to trigger the exception.
        randomAccess.seek(invalidNegativePosition);
        randomAccess.readLine();

        // Assert: The test will pass if an ArrayIndexOutOfBoundsException is thrown,
        // as declared by the @Test(expected=...) annotation.
    }
}