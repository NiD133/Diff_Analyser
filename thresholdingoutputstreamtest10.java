package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ThresholdingOutputStreamTestTest10 {

    private static final int TEST_THRESHOLD = 7;

    /**
     * Asserts initial state without changing it.
     *
     * @param out the stream to test.
     * @param expectedThreshold the expected threshold.
     * @param expectedByteCount the expected byte count.
     */
    static void assertThresholdingInitialState(final ThresholdingOutputStream out, final int expectedThreshold, final long expectedByteCount) {
        assertFalse(out.isThresholdExceeded(), "Stream should not start in an exceeded state.");
        assertEquals(expectedThreshold, out.getThreshold(), "Initial threshold should match the constructor argument.");
        assertEquals(expectedByteCount, out.getByteCount(), "Initial byte count should be zero.");
    }

    /**
     * Tests that writing a zero-length byte array does not trigger the threshold
     * or change the stream's state in any way.
     */
    @Test
    @DisplayName("Writing an empty byte array should not change stream state or trigger threshold")
    void writingEmptyByteArrayDoesNotTriggerThreshold() throws IOException {
        // Arrange: Create a stream that uses a flag to track if the thresholdReached() callback is invoked.
        final AtomicBoolean thresholdCallbackInvoked = new AtomicBoolean(false);
        try (final ThresholdingOutputStream stream = new ThresholdingOutputStream(TEST_THRESHOLD) {
            @Override
            protected void thresholdReached() throws IOException {
                super.thresholdReached();
                thresholdCallbackInvoked.set(true);
            }
        }) {
            assertThresholdingInitialState(stream, TEST_THRESHOLD, 0);
            assertFalse(thresholdCallbackInvoked.get(), "Callback should not be invoked before any data is written.");

            // Act: Write a zero-length byte array. This action should be a no-op.
            stream.write(new byte[0]);

            // Assert: The stream's state should be unchanged, and the callback should not have been invoked.
            assertAll("After writing an empty array",
                () -> assertFalse(stream.isThresholdExceeded(), "isThresholdExceeded should remain false."),
                () -> assertEquals(0, stream.getByteCount(), "Byte count should remain zero."),
                () -> assertFalse(thresholdCallbackInvoked.get(), "thresholdReached() callback should not have been invoked."),
                () -> assertInstanceOf(NullOutputStream.class, stream.getOutputStream(), "Underlying stream should not have changed from the default.")
            );
        }
    }
}