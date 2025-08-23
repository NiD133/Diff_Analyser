package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ThresholdingOutputStream}, focusing on behavior with negative thresholds.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Asserts the initial state of a {@link ThresholdingOutputStream}.
     *
     * @param stream The stream to test.
     * @param expectedThreshold The expected threshold.
     * @param expectedByteCount The expected byte count.
     */
    private static void assertInitialState(final ThresholdingOutputStream stream, final int expectedThreshold, final long expectedByteCount) {
        assertFalse(stream.isThresholdExceeded(), "Stream should not start in an exceeded state.");
        assertEquals(expectedThreshold, stream.getThreshold(), "Initial threshold should match the configured value.");
        assertEquals(expectedByteCount, stream.getByteCount(), "Initial byte count should be zero.");
    }

    @Test
    @DisplayName("A negative threshold should be treated as 0 and trigger on the first write")
    void negativeThresholdIsTreatedAsZeroAndTriggersOnFirstWrite() throws IOException {
        // Arrange: Create a stream with a negative threshold and a flag to track the callback.
        final AtomicBoolean thresholdCallbackCalled = new AtomicBoolean(false);
        try (final ThresholdingOutputStream stream = new ThresholdingOutputStream(-1) {
            @Override
            protected void thresholdReached() {
                thresholdCallbackCalled.set(true);
            }
        }) {
            // Assert initial state: A negative threshold is normalized to 0.
            assertInitialState(stream, 0, 0);
            assertFalse(thresholdCallbackCalled.get(), "thresholdReached() should not be called before any write operation.");

            // Act: Write a single byte, which should exceed the 0-byte threshold.
            stream.write(1);

            // Assert: Verify the threshold was triggered and the stream state was updated correctly.
            assertTrue(thresholdCallbackCalled.get(), "thresholdReached() callback should have been invoked.");
            assertTrue(stream.isThresholdExceeded(), "isThresholdExceeded() should return true after the threshold is met.");
            assertEquals(1, stream.getByteCount(), "Byte count should be 1 after writing one byte.");
            assertInstanceOf(NullOutputStream.class, stream.getOutputStream(),
                "The underlying stream should switch to NullOutputStream by default when the threshold is reached.");
        }
    }
}