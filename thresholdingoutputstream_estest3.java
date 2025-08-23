package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStreamTest {

    private static final int TEST_THRESHOLD = 661;

    /**
     * Tests that directly invoking the {@code thresholdReached()} method does not
     * alter the configured threshold value of the stream.
     *
     * <p>The {@code thresholdReached()} method is a protected hook for subclasses to
     * react when the threshold is exceeded. This test ensures that the default
     * implementation has no unexpected side effects on the stream's configuration.</p>
     */
    @Test
    public void thresholdReachedShouldNotChangeThresholdValue() throws IOException {
        // Arrange: Create a stream with a specific threshold.
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(TEST_THRESHOLD);
        assertEquals("Precondition: The initial threshold should be correctly set.",
                TEST_THRESHOLD, stream.getThreshold());

        // Act: Manually trigger the threshold reached event.
        stream.thresholdReached();

        // Assert: Verify that the threshold value remains unchanged after the event.
        assertEquals("The threshold value should not change after thresholdReached() is called.",
                TEST_THRESHOLD, stream.getThreshold());
    }
}