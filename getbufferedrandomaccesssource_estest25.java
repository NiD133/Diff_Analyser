package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains unit tests for the {@link GetBufferedRandomAccessSource} class.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that the get() method correctly returns 0 when asked to read zero bytes.
     * This is an important edge case to ensure the method handles "do nothing" requests gracefully.
     */
    @Test
    public void get_whenReadingZeroBytes_returnsZero() throws IOException {
        // Arrange: Set up the source and the object under test.
        byte[] sourceData = new byte[15];
        RandomAccessSource source = new ArrayRandomAccessSource(sourceData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(source);

        byte[] destinationBuffer = new byte[15];
        int lengthToRead = 0;

        // Act: Call the method with a request to read zero bytes.
        int bytesRead = bufferedSource.get(0L, destinationBuffer, 0, lengthToRead);

        // Assert: Verify that the number of bytes read is 0.
        assertEquals("The method should return 0 when the requested read length is 0.", 0, bytesRead);
    }
}