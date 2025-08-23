package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Tests for {@link GetBufferedRandomAccessSource}.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Tests that the get() method correctly handles a read request that extends
     * beyond the end of the underlying source. It should read all available bytes
     * up to the end of the source and return that count, rather than throwing an error.
     */
    @Test
    public void get_whenRequestedLengthExceedsSourceSize_shouldReadUntilEndOfSource() throws IOException {
        // Arrange
        final int sourceSize = 16;
        byte[] sourceData = new byte[sourceSize];
        // ArrayRandomAccessSource is a simple implementation of RandomAccessSource backed by a byte array.
        RandomAccessSource underlyingSource = new ArrayRandomAccessSource(sourceData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(underlyingSource);

        byte[] destinationBuffer = new byte[sourceSize];
        int offset = 0;
        int requestedLength = 610; // A length much larger than the source size.

        // Act
        int bytesRead = bufferedSource.get(0L, destinationBuffer, offset, requestedLength);

        // Assert
        assertEquals("The number of bytes read should be limited by the source's actual size.",
                sourceSize, bytesRead);
    }
}