package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public class ThresholdingOutputStreamTestTest5 {

    /**
     * Asserts initial state without changing it.
     *
     * @param out the stream to test.
     * @param expectedThreshold the expected threshold.
     * @param expectedByeCount the expected byte count.
     */
    static void assertThresholdingInitialState(final ThresholdingOutputStream out, final int expectedThreshold, final int expectedByeCount) {
        assertFalse(out.isThresholdExceeded());
        assertEquals(expectedThreshold, out.getThreshold());
        assertEquals(expectedByeCount, out.getByteCount());
    }

    @Test
    void testThresholdIOConsumer() throws IOException {
        final int threshold = 1;
        // Null threshold consumer
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, null, os -> new ByteArrayOutputStream(4))) {
            assertThresholdingInitialState(out, threshold, 0);
            out.write('a');
            assertFalse(out.isThresholdExceeded());
            out.write('a');
            assertTrue(out.isThresholdExceeded());
        }
        // Null output stream function
        final AtomicBoolean reached = new AtomicBoolean();
        reached.set(false);
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, os -> reached.set(true), null)) {
            assertThresholdingInitialState(out, threshold, 0);
            out.write('a');
            assertFalse(reached.get());
            assertFalse(out.isThresholdExceeded());
            out.write('a');
            assertTrue(reached.get());
            assertTrue(out.isThresholdExceeded());
        }
        // non-null inputs.
        reached.set(false);
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, os -> reached.set(true), os -> new ByteArrayOutputStream(4))) {
            assertThresholdingInitialState(out, threshold, 0);
            out.write('a');
            assertFalse(reached.get());
            assertFalse(out.isThresholdExceeded());
            out.write('a');
            assertTrue(reached.get());
            assertTrue(out.isThresholdExceeded());
        }
    }
}
