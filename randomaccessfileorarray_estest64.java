package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link RandomAccessFileOrArray} focusing on behavior with invalid seek positions.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read from the stream after seeking to a negative
     * position results in an ArrayIndexOutOfBoundsException.
     *
     * This tests the boundary conditions of the read operations when the internal
     * pointer is in an invalid state.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void readUnsignedIntLE_withNegativeSeekPosition_throwsArrayIndexOutOfBoundsException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray backed by a simple byte array.
        byte[] data = new byte[10];
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(data);

        // Act: Seek to an invalid negative position and then attempt to read.
        randomAccess.seek(-1L);
        randomAccess.readUnsignedIntLE(); // This action is expected to throw the exception.

        // Assert: The test passes if an ArrayIndexOutOfBoundsException is thrown,
        // which is handled by the @Test(expected) annotation.
    }
}