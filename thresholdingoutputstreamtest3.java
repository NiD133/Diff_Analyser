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

public class ThresholdingOutputStreamTestTest3 {

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
    void testSetByteCountOutputStream() throws IOException {
        final AtomicBoolean reached = new AtomicBoolean();
        final int initCount = 2;
        final int threshold = 3;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold) {

            {
                setByteCount(initCount);
            }

            @Override
            protected OutputStream getOutputStream() throws IOException {
                return new ByteArrayOutputStream(4);
            }

            @Override
            protected void thresholdReached() throws IOException {
                reached.set(true);
            }
        }) {
            assertThresholdingInitialState(out, threshold, initCount);
            out.write('a');
            assertFalse(reached.get());
            assertFalse(out.isThresholdExceeded());
            out.write('a');
            assertTrue(reached.get());
            assertTrue(out.isThresholdExceeded());
        }
    }
}
