package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;

/**
 * Tests for {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that a negative threshold is treated as zero, causing the threshold
     * to be exceeded immediately upon the first write.
     */
    @Test
    public void testNegativeThresholdIsTreatedAsZeroAndExceededOnWrite() throws IOException {
        // Arrange: Create a stream with a negative threshold.
        // The constructor treats any negative threshold as 0.
        final int negativeThreshold = -1;
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(negativeThreshold);
        final byte[] dataToWrite = { 1 };

        // Act: Write a single byte to the stream.
        stream.write(dataToWrite);

        // Assert: Verify the byte count and that the threshold has been exceeded.
        assertEquals("The byte count should be 1 after writing one byte.", 1L, stream.getByteCount());
        assertTrue("The threshold should be exceeded since it's treated as 0.", stream.isThresholdExceeded());
    }
}