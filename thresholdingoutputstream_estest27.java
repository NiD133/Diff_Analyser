package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

/**
 * Contains tests for the {@link ThresholdingOutputStream} class.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that the configured threshold value can still be retrieved
     * after the stream has been closed.
     */
    @Test
    public void shouldReturnThresholdAfterStreamIsClosed() throws IOException {
        // Arrange: Create a stream with a specific threshold.
        final int expectedThreshold = 76;
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(expectedThreshold);

        // Act: Close the stream.
        stream.close();

        // Assert: Verify that the threshold remains unchanged.
        final int actualThreshold = stream.getThreshold();
        assertEquals("The threshold should not change after the stream is closed.", expectedThreshold, actualThreshold);
    }
}