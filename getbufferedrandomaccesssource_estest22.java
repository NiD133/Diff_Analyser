package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Test suite for the {@link GetBufferedRandomAccessSource} class.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that the get(long) method returns the correct byte value,
     * especially when the data is read from the internal buffer after an initial read.
     */
    @Test
    public void get_whenPositionIsAlreadyBuffered_returnsCorrectByte() throws IOException {
        // Arrange: Create a source with known data (an array of zeros) and wrap it
        // with the GetBufferedRandomAccessSource.
        byte[] sourceData = new byte[15]; // Default value for each element is 0.
        RandomAccessSource underlyingSource = new ArrayRandomAccessSource(sourceData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(underlyingSource);

        // Act:
        // 1. The first 'get' call populates the internal buffer from the underlying source.
        bufferedSource.get(0L);

        // 2. The second 'get' call should read directly from the now-populated buffer.
        int actualByte = bufferedSource.get(0L);

        // Assert: The byte read from the buffer should match the data in the source array.
        int expectedByte = 0;
        assertEquals("The byte read from the buffered source should be 0.", expectedByte, actualByte);
    }
}