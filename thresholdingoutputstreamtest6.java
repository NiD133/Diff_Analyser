package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ThresholdingOutputStream} focusing on its threshold-triggered event handling.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Asserts the initial state of a {@link ThresholdingOutputStream}.
     *
     * @param stream the stream to test.
     * @param expectedThreshold the expected threshold.
     * @param expectedByteCount the expected byte count.
     */
    private static void assertInitialState(final ThresholdingOutputStream stream, final int expectedThreshold, final long expectedByteCount) {
        assertFalse(stream.isThresholdExceeded(), "Initially, threshold should not be exceeded.");
        assertEquals(expectedThreshold, stream.getThreshold(), "Initial threshold should match constructor argument.");
        assertEquals(expectedByteCount, stream.getByteCount(), "Initial byte count should be zero.");
    }

    @Test
    @DisplayName("Consumer throwing IOException when threshold is exceeded should propagate the exception")
    void consumerThrowingIOExceptionIsPropagatedWhenThresholdExceeded() throws IOException {
        // Arrange
        final int threshold = 1;
        final String expectedExceptionMessage = "Threshold reached.";
        
        // The stream is configured to throw an IOException via its consumer when the threshold is exceeded.
        // The try-with-resources ensures the stream is closed automatically.
        try (ThresholdingOutputStream stream = new ThresholdingOutputStream(threshold, os -> {
            throw new IOException(expectedExceptionMessage);
        }, os -> new ByteArrayOutputStream(4))) {

            assertInitialState(stream, threshold, 0);

            // Act: Write one byte. This meets the threshold but does not exceed it.
            stream.write('a');

            // Assert: The state after the first write is as expected.
            assertEquals(1, stream.getByteCount(), "Byte count should be 1 after writing one byte.");
            assertFalse(stream.isThresholdExceeded(), "Threshold is met, but not yet exceeded.");

            // Act & Assert: The next write should exceed the threshold, trigger the consumer,
            // and propagate the consumer's exception.
            final IOException thrown = assertThrows(IOException.class, () -> stream.write('b'));

            // Assert: Verify the exception and the final state of the stream.
            assertEquals(expectedExceptionMessage, thrown.getMessage(), "The exception message should match the one from the consumer.");
            assertTrue(stream.isThresholdExceeded(), "The threshold exceeded flag should be set even if the consumer throws an exception.");
            assertEquals(1, stream.getByteCount(), "The byte count should not increase because the write that triggered the exception failed.");
        }
    }
}