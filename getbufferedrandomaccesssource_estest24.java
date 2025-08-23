package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link GetBufferedRandomAccessSource} class.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that the length() method correctly returns the length of the
     * underlying data source.
     */
    @Test
    public void length_shouldReturnLengthOfUnderlyingSource() {
        // Arrange: Create an underlying source with a known length.
        final int expectedLength = 8;
        byte[] sourceData = new byte[expectedLength];
        RandomAccessSource underlyingSource = new ArrayRandomAccessSource(sourceData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(underlyingSource);

        // Act: Call the length() method on the buffered source.
        long actualLength = bufferedSource.length();

        // Assert: The returned length should match the underlying source's length.
        assertEquals(expectedLength, actualLength);
    }
}