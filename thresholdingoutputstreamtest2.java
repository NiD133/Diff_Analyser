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

public class ThresholdingOutputStreamTestTest2 {

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
    void testResetByteCountBrokenOutputStream() {
        final int threshold = 1;
        final AtomicInteger counter = new AtomicInteger();
        final IOException e = assertThrows(IOException.class, () -> {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, tos -> {
                    counter.incrementAndGet();
                    tos.resetByteCount();
                }, o -> BrokenOutputStream.INSTANCE)) {
                assertThresholdingInitialState(out, threshold, 0);
                assertEquals(0, counter.get());
                assertThrows(IOException.class, () -> out.write('a'));
                assertFalse(out.isThresholdExceeded());
                assertThrows(IOException.class, () -> out.write('a'));
                assertEquals(0, counter.get());
                assertFalse(out.isThresholdExceeded());
                assertThrows(IOException.class, () -> out.write('a'));
                assertThrows(IOException.class, () -> out.write('a'));
                assertEquals(0, counter.get());
            }
        });
        // Should only happen on close
        assertEquals("Broken output stream: close()", e.getMessage());
    }
}
