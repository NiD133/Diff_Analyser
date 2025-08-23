package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * This class contains tests for {@link RandomAccessFileOrArray}.
 * This specific test was improved for clarity and maintainability.
 */
public class RandomAccessFileOrArray_ESTestTest93 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that attempting to read from the stream after seeking to a negative
     * position results in an {@link ArrayIndexOutOfBoundsException}. This ensures
     * that the underlying buffer access is properly bounds-checked.
     */
    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void readBoolean_whenPositionIsNegative_throwsArrayIndexOutOfBoundsException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray with a small byte array.
        byte[] sourceBytes = new byte[1];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceBytes);
        long invalidNegativePosition = -1L;

        // Act: Move the file pointer to an invalid negative position.
        fileOrArray.seek(invalidNegativePosition);
        
        // Assert: The next read operation is expected to throw an exception,
        // which is handled by the @Test(expected=...) annotation.
        fileOrArray.readBoolean();
    }
}