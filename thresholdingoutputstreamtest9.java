package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ThresholdingOutputStream}.
 */
class ThresholdingOutputStreamTest {

    /**
     * Asserts the initial state of a ThresholdingOutputStream.
     *
     * @param stream the stream to test.
     * @param expectedThreshold the expected threshold.
     * @param expectedByteCount the expected byte count.
     */
    private void assertInitialState(final ThresholdingOutputStream stream, final int expectedThreshold, final long expectedByteCount) {
        assertFalse(stream.isThresholdExceeded(), "Stream should not start in an exceeded state.");
        assertEquals(expectedThreshold, stream.getThreshold(), "Initial threshold should match the constructor argument.");
        assertEquals(expectedByteCount, stream.getByteCount(), "Initial byte count should be zero.");
    }

    @Test
    void write_whenThresholdIsZero_triggersThresholdImmediately() throws IOException {
        // Arrange
        final int threshold = 0;
        final AtomicBoolean thresholdReachedCallbackFired = new AtomicBoolean(false);

        // Use the modern constructor with a lambda for the threshold consumer,
        // which is cleaner than an anonymous inner class.
        // The default output stream getter is used, which returns NullOutputStream.
        try (ThresholdingOutputStream stream = new ThresholdingOutputStream(threshold, os -> thresholdReachedCallbackFired.set(true), null)) {
            // Assert initial state (pre-conditions)
            assertInitialState(stream, threshold, 0);

            // Act: Write a single byte. Since the threshold is zero, this should
            // immediately trigger the threshold consumer.
            stream.write(1);

            // Assert
            assertTrue(thresholdReachedCallbackFired.get(), "The thresholdReached callback should have been fired.");
            assertTrue(stream.isThresholdExceeded(), "The stream's state should be marked as threshold exceeded.");
            assertEquals(1, stream.getByteCount(), "Byte count should be updated after the write.");
            assertInstanceOf(NullOutputStream.class, stream.getOutputStream(),
                "The underlying stream should switch to NullOutputStream by default after the threshold is reached.");
        }
    }
}