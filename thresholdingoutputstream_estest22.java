package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ThresholdingOutputStream}.
 * This class focuses on the constructor's handling of threshold values.
 */
public class ThresholdingOutputStream_ESTestTest22 { // Note: Original class name preserved.

    /**
     * Tests that the constructor correctly handles a negative threshold value
     * by setting it to zero, as documented in the class.
     */
    @Test
    public void constructorShouldSetNegativeThresholdToZero() {
        // Arrange: Define a negative threshold and the expected outcome.
        final int negativeThreshold = -100;
        final int expectedThreshold = 0;

        // Act: Create a ThresholdingOutputStream with the negative threshold.
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(negativeThreshold);
        final int actualThreshold = stream.getThreshold();

        // Assert: The threshold should be clamped to zero.
        assertEquals("A negative threshold input should be treated as 0.", expectedThreshold, actualThreshold);
    }
}