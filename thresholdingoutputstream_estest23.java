package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that the constructor correctly handles a negative threshold
     * by treating it as zero, as specified in the class documentation.
     */
    @Test
    public void shouldTreatNegativeThresholdAsZeroOnConstruction() {
        // Arrange: Define a negative threshold value.
        final int negativeThreshold = -100;

        // Act: Create the stream with the negative threshold.
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(negativeThreshold);

        // Assert: Verify that the threshold was set to 0 and the initial byte count is correct.
        assertEquals("A negative threshold should be treated as 0.", 0, stream.getThreshold());
        assertEquals("The initial byte count should be 0.", 0L, stream.getByteCount());
    }
}