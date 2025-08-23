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

public class ThresholdingOutputStreamTestTest10 {

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

    /**
     * Tests the case where no bytes are written.
     * The threshold is not reached until something is written to the stream.
     */
    @Test
    void testThresholdZeroWrite() throws IOException {
        final AtomicBoolean reached = new AtomicBoolean();
        final int threshold = 7;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold) {

            @Override
            protected void thresholdReached() throws IOException {
                super.thresholdReached();
                reached.set(true);
            }
        }) {
            assertThresholdingInitialState(out, threshold, 0);
            assertFalse(reached.get());
            out.write(new byte[0]);
            assertFalse(out.isThresholdExceeded());
            assertFalse(reached.get());
            assertInstanceOf(NullOutputStream.class, out.getOutputStream());
            assertFalse(out.isThresholdExceeded());
        }
    }
}
