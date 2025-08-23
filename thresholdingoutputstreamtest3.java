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
 */
class ThresholdingOutputStreamTest {

    @Test
    void setByteCount_whenUsed_correctlyAffectsThresholdTrigger() throws IOException {
        // Arrange: Set up a stream with a threshold and a flag to track when it's reached.
        final int threshold = 3;
        final int initialByteCount = 2;
        final AtomicBoolean thresholdReachedFlag = new AtomicBoolean(false);

        // Create an anonymous subclass to monitor the thresholdReached() callback.
        // The try-with-resources block ensures the stream is closed automatically.
        try (final ThresholdingOutputStream out = new ThresholdingOutputStream(threshold) {
            @Override
            protected OutputStream getOutputStream() {
                // The actual output destination is not important for this test, so we discard it.
                return NullOutputStream.INSTANCE;
            }

            @Override
            protected void thresholdReached() {
                // Set the flag when the threshold is exceeded.
                thresholdReachedFlag.set(true);
            }
        }) {
            // Manually set the byte count to simulate a stream that already contains data.
            out.setByteCount(initialByteCount);

            // Assert: Verify the initial state is correct after setting the byte count.
            assertFalse(out.isThresholdExceeded(), "Pre-condition: Threshold should not be exceeded.");
            assertEquals(initialByteCount, out.getByteCount(), "Pre-condition: Byte count should match the set value.");
            assertFalse(thresholdReachedFlag.get(), "Pre-condition: thresholdReached() should not have been called yet.");

            // Act: Write one byte. The new count will be 3, which equals the threshold.
            // The threshold is only exceeded if count > threshold, not count >= threshold.
            out.write('a');

            // Assert: The threshold has not been exceeded yet.
            assertFalse(thresholdReachedFlag.get(), "Callback should not trigger when byte count equals threshold.");
            assertFalse(out.isThresholdExceeded(), "isThresholdExceeded should be false when count equals threshold.");
            assertEquals(3, out.getByteCount(), "Byte count should now be 3.");

            // Act: Write another byte. The new count will be 4, which is > threshold.
            out.write('b');

            // Assert: The threshold is now exceeded.
            assertTrue(thresholdReachedFlag.get(), "Callback should trigger when byte count exceeds threshold.");
            assertTrue(out.isThresholdExceeded(), "isThresholdExceeded should be true when count exceeds threshold.");
            assertEquals(4, out.getByteCount(), "Byte count should now be 4.");
        }
    }
}