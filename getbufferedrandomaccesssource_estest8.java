package com.itextpdf.text.io;

import org.junit.Test;

import java.io.IOException;

/**
 * Unit tests for {@link GetBufferedRandomAccessSource}.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that the get(long, byte[], int, int) method throws a NullPointerException
     * when the provided destination byte array is null.
     */
    @Test(expected = NullPointerException.class)
    public void getWithNullBufferThrowsNullPointerException() throws IOException {
        // Arrange: Create a source and the buffered source wrapper to test.
        byte[] sourceData = new byte[16];
        RandomAccessSource source = new ArrayRandomAccessSource(sourceData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(source);

        // Act & Assert: Call the get method with a null buffer.
        // This is expected to throw a NullPointerException.
        // Other arguments are set to valid values to isolate the null buffer as the cause.
        bufferedSource.get(0L, null, 0, 0);
    }
}