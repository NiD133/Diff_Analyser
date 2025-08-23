package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the {@link GetBufferedRandomAccessSource} class.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that the get(long, byte[], int, int) method throws an
     * ArrayIndexOutOfBoundsException when called with a negative offset.
     * A negative offset is invalid for array access and should be rejected.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getWithNegativeOffsetThrowsArrayIndexOutOfBoundsException() throws IOException {
        // Arrange: Set up the buffered source and the parameters for the get() call.
        byte[] sourceData = new byte[10];
        ArrayRandomAccessSource underlyingSource = new ArrayRandomAccessSource(sourceData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(underlyingSource);

        byte[] destinationBuffer = new byte[10];
        final long positionToReadFrom = 0L;
        final int negativeOffset = -1; // The invalid parameter being tested.
        final int lengthToRead = 5;

        // Act: Attempt to read from the source with the invalid negative offset.
        // This call is expected to throw the exception.
        bufferedSource.get(positionToReadFrom, destinationBuffer, negativeOffset, lengthToRead);

        // Assert: The test passes if an ArrayIndexOutOfBoundsException is thrown,
        // which is declared by the @Test(expected=...) annotation.
    }
}