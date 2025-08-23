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

public class ThresholdingOutputStreamTestTest7 {

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
    void testThresholdIOConsumerUncheckedException() throws IOException {
        final int threshold = 1;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, os -> {
            throw new IllegalStateException("Threshold reached.");
        }, os -> new ByteArrayOutputStream(4))) {
            assertThresholdingInitialState(out, threshold, 0);
            out.write('a');
            assertFalse(out.isThresholdExceeded());
            assertThrows(IllegalStateException.class, () -> out.write('a'));
            assertFalse(out.isThresholdExceeded());
            assertInstanceOf(ByteArrayOutputStream.class, out.getOutputStream());
            assertFalse(out.isThresholdExceeded());
        }
    }
}
