package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ThresholdingOutputStream} class.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that the getThreshold() method correctly returns the value
     * that was set in the constructor.
     */
    @Test
    public void getThresholdShouldReturnThresholdSetInConstructor() {
        // Arrange
        final int expectedThreshold = 0;
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(expectedThreshold);

        // Act
        final int actualThreshold = stream.getThreshold();

        // Assert
        assertEquals("The threshold should match the value provided in the constructor.",
                expectedThreshold, actualThreshold);
    }
}