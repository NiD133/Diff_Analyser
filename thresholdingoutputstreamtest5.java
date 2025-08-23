package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ThresholdingOutputStream} focusing on the constructor that accepts
 * an IOConsumer and IOFunction.
 */
class ThresholdingOutputStreamTest {

    /**
     * Asserts the initial state of a ThresholdingOutputStream.
     *
     * @param stream the stream to test.
     * @param expectedThreshold the expected threshold.
     */
    private void assertInitialState(final ThresholdingOutputStream stream, final int expectedThreshold) {
        assertFalse(stream.isThresholdExceeded(), "Threshold should not be exceeded initially.");
        assertEquals(expectedThreshold, stream.getThreshold(), "Initial threshold should match the constructor argument.");
        assertEquals(0, stream.getByteCount(), "Initial byte count should be zero.");
    }

    @Test
    @DisplayName("Thresholding should work correctly when the threshold consumer is null")
    void shouldExceedThresholdWhenThresholdConsumerIsNull() throws IOException {
        // Given
        final int threshold = 1;
        // A null thresholdConsumer should be handled gracefully (as a no-op).
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, null, os -> new ByteArrayOutputStream(4))) {
            assertInitialState(out, threshold);

            // When: write one byte, meeting the threshold but not exceeding it
            out.write('a');

            // Then: the threshold is not yet exceeded
            assertFalse(out.isThresholdExceeded(), "Threshold is not exceeded at the boundary.");
            assertEquals(1, out.getByteCount());

            // When: write a second byte, crossing the threshold
            out.write('a');

            // Then: the threshold is now exceeded
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded after writing past the boundary.");
            assertEquals(2, out.getByteCount());
        }
    }

    @Test
    @DisplayName("Threshold consumer should be invoked when the stream getter is null")
    void shouldInvokeConsumerWhenStreamGetterIsNull() throws IOException {
        // Given
        final int threshold = 1;
        final AtomicBoolean consumerCalled = new AtomicBoolean(false);
        // A null outputStreamGetter should default to a NullOutputStream.
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, os -> consumerCalled.set(true), null)) {
            assertInitialState(out, threshold);

            // When: write one byte, meeting the threshold
            out.write('a');

            // Then: the consumer has not been called yet
            assertFalse(consumerCalled.get(), "Consumer should not be called at the threshold boundary.");
            assertFalse(out.isThresholdExceeded());

            // When: write a second byte, crossing the threshold
            out.write('a');

            // Then: the consumer is invoked and the threshold is exceeded
            assertTrue(consumerCalled.get(), "Consumer should be called after exceeding the threshold.");
            assertTrue(out.isThresholdExceeded());
        }
    }

    @Test
    @DisplayName("Threshold consumer should be invoked with non-null constructor arguments")
    void shouldInvokeConsumerWithNonNullArguments() throws IOException {
        // Given
        final int threshold = 1;
        final AtomicBoolean consumerCalled = new AtomicBoolean(false);
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, os -> consumerCalled.set(true), os -> new ByteArrayOutputStream(4))) {
            assertInitialState(out, threshold);

            // When: write one byte, meeting the threshold
            out.write('a');

            // Then: the consumer has not been called yet
            assertFalse(consumerCalled.get(), "Consumer should not be called at the threshold boundary.");
            assertFalse(out.isThresholdExceeded());

            // When: write a second byte, crossing the threshold
            out.write('a');



            // Then: the consumer is invoked and the threshold is exceeded
            assertTrue(consumerCalled.get(), "Consumer should be called after exceeding the threshold.");
            assertTrue(out.isThresholdExceeded());
        }
    }
}