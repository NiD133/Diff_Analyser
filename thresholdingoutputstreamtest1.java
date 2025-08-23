package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.function.IOConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ThresholdingOutputStream}.
 */
class ThresholdingOutputStreamTest {

    @Test
    @DisplayName("Threshold should be triggered repeatedly if the byte count is reset by the consumer")
    void shouldTriggerThresholdMultipleTimesWhenByteCountIsReset() throws IOException {
        // Arrange
        final int threshold = 1;
        final AtomicInteger thresholdReachedCounter = new AtomicInteger(0);

        // This consumer is called when the threshold is exceeded.
        // It increments a counter and then resets the stream's byte count,
        // allowing the threshold to be triggered again.
        final IOConsumer<ThresholdingOutputStream> thresholdConsumer = stream -> {
            thresholdReachedCounter.incrementAndGet();
            stream.resetByteCount();
        };

        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
             final ThresholdingOutputStream thresholdingStream = new ThresholdingOutputStream(threshold, thresholdConsumer, s -> baos)) {

            // Assert initial state before any writes
            assertFalse(thresholdingStream.isThresholdExceeded(), "Threshold should not be exceeded initially.");
            assertEquals(0, thresholdingStream.getByteCount(), "Initial byte count should be zero.");
            assertEquals(0, thresholdReachedCounter.get(), "Counter should be zero initially.");

            // Act & Assert: Write 1 byte. The count now equals the threshold.
            // The trigger occurs only when the count *exceeds* the threshold, so it shouldn't fire yet.
            thresholdingStream.write('a');
            assertEquals(0, thresholdReachedCounter.get(), "Counter should not increment when count equals threshold.");
            assertEquals(1, thresholdingStream.getByteCount(), "Byte count should be 1 after one write.");
            assertFalse(thresholdingStream.isThresholdExceeded(), "isThresholdExceeded should remain false.");

            // Act & Assert: Write a 2nd byte. The pending write (1 byte) will make the total (1+1=2)
            // exceed the threshold (1). This triggers the consumer. The consumer increments the
            // counter and resets the byte count to 0. The write then completes, making the new count 1.
            thresholdingStream.write('b');
            assertEquals(1, thresholdReachedCounter.get(), "Counter should increment on first threshold breach.");
            assertEquals(1, thresholdingStream.getByteCount(), "Byte count should be 1 after reset and second write.");
            assertFalse(thresholdingStream.isThresholdExceeded(), "isThresholdExceeded should be false after reset.");

            // Act & Assert: Write a 3rd byte. This again exceeds the threshold (1+1 > 1),
            // triggering the consumer for a second time.
            thresholdingStream.write('c');
            assertEquals(2, thresholdReachedCounter.get(), "Counter should increment on second threshold breach.");
            assertEquals(1, thresholdingStream.getByteCount(), "Byte count should be 1 after second reset and third write.");

            // Act & Assert: Write a 4th byte, triggering the consumer for a third time.
            thresholdingStream.write('d');
            assertEquals(3, thresholdReachedCounter.get(), "Counter should increment on third threshold breach.");
            assertEquals(1, thresholdingStream.getByteCount(), "Byte count should be 1 after third reset and fourth write.");
        }
    }
}