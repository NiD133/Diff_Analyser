package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on edge cases
 * related to file pointer manipulation.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray
     * after seeking to a negative position results in an ArrayIndexOutOfBoundsException.
     *
     * This behavior is expected when the underlying data source is a byte array,
     * as a negative position translates to an invalid negative array index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void readShortLE_afterSeekingToNegativeOffset_throwsArrayIndexOutOfBoundsException() throws IOException {
        // Arrange: Create an instance backed by a simple byte array.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(sourceData);

        // Act: Move the internal pointer to an invalid negative position.
        // The seek() operation itself might not throw an exception, but it sets up the invalid state.
        randomAccess.seek(-1L);

        // Assert: Attempting to read from the invalid position should throw.
        // The @Test(expected=...) annotation handles the assertion, making the test fail
        // if this line does not throw the specified exception.
        randomAccess.readShortLE();
    }
}