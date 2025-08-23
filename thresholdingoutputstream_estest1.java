package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Unit tests for {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that a newly created stream has not exceeded its threshold
     * before any bytes have been written.
     */
    @Test
    public void isThresholdExceededShouldReturnFalseForNewStream() {
        // Arrange: Define a threshold and create a new stream.
        final int testThreshold = 100;
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(testThreshold);

        // Act: Check if the threshold has been exceeded.
        final boolean isExceeded = stream.isThresholdExceeded();

        // Assert: Verify the initial state is correct.
        assertFalse("A new stream should not report its threshold as exceeded.", isExceeded);
        assertEquals("The threshold should be initialized correctly.", testThreshold, stream.getThreshold());
    }
}