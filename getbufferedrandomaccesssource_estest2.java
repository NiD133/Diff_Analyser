package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Unit tests for the {@link GetBufferedRandomAccessSource} class.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that the length() method correctly returns 0 when the underlying source is empty.
     * This confirms that the length calculation is properly delegated to the wrapped source.
     */
    @Test
    public void length_whenSourceIsEmpty_returnsZero() throws IOException {
        // Arrange: Create an empty underlying source and wrap it.
        // Using ByteArrayRandomAccessSource with an empty array is a clear and direct
        // way to represent a source with a length of zero.
        RandomAccessSource emptySource = new ByteArrayRandomAccessSource(new byte[0]);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(emptySource);

        // Act: Get the length from the buffered source.
        long actualLength = bufferedSource.length();

        // Assert: The length should be 0, matching the underlying source.
        assertEquals(0L, actualLength);
    }
}