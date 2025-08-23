package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ThresholdingOutputStream}.
 *
 * Note: The original class name 'ThresholdingOutputStreamTestTest4' was simplified to
 * 'ThresholdingOutputStreamTest' for clarity and standard naming conventions.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that the thresholding logic correctly accounts for a pre-set byte count.
     * This scenario simulates re-opening or continuing a stream that already contains data.
     */
    @Test
    void whenByteCountIsPreset_thresholdingObeysInitialCount() throws IOException {
        // Arrange
        final int THRESHOLD = 3;
        final int INITIAL_BYTE_COUNT = 2;
        final AtomicBoolean thresholdReachedCallbackFired = new AtomicBoolean(false);

        // Create a stream with a threshold and a callback to track when it's reached.
        // We use an anonymous class to override protected methods for testing purposes.
        try (final ThresholdingOutputStream out = new ThresholdingOutputStream(THRESHOLD) {
            // This instance initializer sets the byte count *before* any writes occur,
            // simulating a stream that already has content.
            {
                setByteCount(INITIAL_BYTE_COUNT);
            }

            @Override
            protected OutputStream getStream() {
                // A simple sink for the written bytes is sufficient for this test.
                return new ByteArrayOutputStream();
            }

            @Override
            protected void thresholdReached() {
                // This callback is fired when the threshold is exceeded.
                thresholdReachedCallbackFired.set(true);
            }
        }) {
            // Assert: Verify the initial state is as expected before any new writes.
            assertEquals(THRESHOLD, out.getThreshold(), "Initial threshold should be set correctly.");
            assertEquals(INITIAL_BYTE_COUNT, out.getByteCount(), "Initial byte count should be set correctly.");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded initially.");
            assertFalse(thresholdReachedCallbackFired.get(), "Callback should not be fired on initialization.");

            // Act: Write one byte. Total bytes = 2 (initial) + 1 (write) = 3.
            // This meets the threshold but does not exceed it.
            out.write('a');

            // Assert: The threshold should not be triggered yet.
            assertFalse(thresholdReachedCallbackFired.get(), "Callback should not fire when byte count equals threshold.");
            assertFalse(out.isThresholdExceeded(), "isThresholdExceeded should be false when byte count equals threshold.");
            assertEquals(3, out.getByteCount(), "Byte count should now equal the threshold.");

            // Act: Write another byte. Total bytes = 3 + 1 = 4.
            // This write causes the stream to exceed the threshold.
            out.write('a');

            // Assert: The threshold should now be triggered.
            assertTrue(thresholdReachedCallbackFired.get(), "Callback should fire once byte count exceeds threshold.");
            assertTrue(out.isThresholdExceeded(), "isThresholdExceeded should be true once byte count exceeds threshold.");
            assertEquals(4, out.getByteCount(), "Byte count should reflect the second write.");
        }
    }
}