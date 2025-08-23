package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the {@link ThresholdingOutputStream} class.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that the getThreshold() method correctly returns the value
     * provided in the constructor.
     */
    @Test
    public void getThreshold_shouldReturnThresholdSetInConstructor() {
        // Arrange: Define a threshold and create the stream
        final int expectedThreshold = 76;
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(expectedThreshold);

        // Act: Call the method under test
        final int actualThreshold = stream.getThreshold();

        // Assert: Verify the result
        assertEquals("The returned threshold should match the one set in the constructor.",
                     expectedThreshold, actualThreshold);
    }
}