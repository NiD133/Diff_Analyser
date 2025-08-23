package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that the constructor treats a negative threshold value as zero,
     * which is the specified behavior for invalid inputs.
     */
    @Test
    public void constructorShouldSetNegativeThresholdToZero() {
        // Arrange
        final int negativeThreshold = -2596;
        final int expectedThreshold = 0;

        // Act
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(negativeThreshold);

        // Assert
        assertEquals("A negative threshold should be corrected to 0.",
                     expectedThreshold, stream.getThreshold());
    }
}