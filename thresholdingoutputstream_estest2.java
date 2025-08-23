package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that writing a zero-length byte array is a no-op and does not
     * affect the stream's byte count.
     */
    @Test
    public void writeWithZeroLengthShouldNotChangeByteCount() throws IOException {
        // Arrange: Create a stream with a threshold of 0.
        final int threshold = 0;
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(threshold);

        // Act: Attempt to write zero bytes. Per the OutputStream contract,
        // this should do nothing. A null buffer is acceptable for a zero-length write.
        stream.write(null, 0, 0);

        // Assert: Verify that the stream's state is unchanged.
        assertEquals("The threshold should remain the same.", threshold, stream.getThreshold());
        assertEquals("The byte count should remain zero after a zero-length write.", 0L, stream.getByteCount());
    }
}