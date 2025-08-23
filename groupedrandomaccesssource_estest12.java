package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;

/**
 * This test class contains tests for {@link GroupedRandomAccessSource}.
 * This specific test case was refactored for improved readability.
 */
public class GroupedRandomAccessSource_ESTestTest12 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Verifies that the get() method throws an ArrayIndexOutOfBoundsException
     * when the provided offset for the destination buffer is out of bounds.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void get_withOffsetOutOfBoundsForDestinationArray_throwsArrayIndexOutOfBoundsException() throws IOException {
        // Arrange: Set up a GroupedRandomAccessSource and a destination buffer.
        int sourceLength = 7;
        byte[] sourceBytes = new byte[sourceLength];
        RandomAccessSource singleSource = new ArrayRandomAccessSource(sourceBytes);
        RandomAccessSource[] sources = { singleSource };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        byte[] destinationBuffer = new byte[sourceLength];
        long readPosition = 1L;
        int readLength = 1;
        // Use an offset that is clearly outside the bounds of the destination buffer.
        int invalidOffset = destinationBuffer.length + 1;

        // Act: Attempt to read from the source into the destination buffer at an invalid offset.
        // This call is expected to throw an ArrayIndexOutOfBoundsException because the offset
        // for the destinationBuffer is invalid.
        groupedSource.get(readPosition, destinationBuffer, invalidOffset, readLength);

        // Assert: The exception is implicitly verified by the @Test(expected = ...) annotation.
    }
}