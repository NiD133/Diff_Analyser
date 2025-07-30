package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ThresholdingOutputStream}.
 * 
 * This test suite verifies the behavior of the ThresholdingOutputStream class, 
 * including its threshold handling and byte count management.
 */
class ThresholdingOutputStreamTest {

    /**
     * Verifies the initial state of a ThresholdingOutputStream instance.
     *
     * @param out the stream to test.
     * @param expectedThreshold the expected threshold.
     * @param expectedByteCount the expected byte count.
     */
    static void verifyInitialState(final ThresholdingOutputStream out, final int expectedThreshold, final int expectedByteCount) {
        assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded initially.");
        assertEquals(expectedThreshold, out.getThreshold(), "Threshold mismatch.");
        assertEquals(expectedByteCount, out.getByteCount(), "Byte count mismatch.");
    }

    @Test
    void testResetByteCount() throws IOException {
        final int threshold = 1;
        final AtomicInteger resetCounter = new AtomicInteger();

        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, tos -> {
                 resetCounter.incrementAndGet();
                 tos.resetByteCount();
             }, o -> os)) {

            verifyInitialState(out, threshold, 0);
            assertEquals(0, resetCounter.get(), "Reset counter should be zero initially.");

            out.write('a');
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after first write.");
            out.write('a');
            assertEquals(1, resetCounter.get(), "Reset counter should increment after threshold is reached.");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after reset.");
            out.write('a');
            out.write('a');
            assertEquals(3, resetCounter.get(), "Reset counter should increment with each threshold breach.");
        }
    }

    @Test
    void testResetByteCountWithBrokenOutputStream() {
        final int threshold = 1;
        final AtomicInteger resetCounter = new AtomicInteger();

        IOException exception = assertThrows(IOException.class, () -> {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                 ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, tos -> {
                     resetCounter.incrementAndGet();
                     tos.resetByteCount();
                 }, o -> BrokenOutputStream.INSTANCE)) {

                verifyInitialState(out, threshold, 0);
                assertEquals(0, resetCounter.get(), "Reset counter should be zero initially.");

                assertThrows(IOException.class, () -> out.write('a'));
                assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after failed write.");
                assertThrows(IOException.class, () -> out.write('a'));
                assertEquals(0, resetCounter.get(), "Reset counter should not increment on failed writes.");
            }
        });

        assertEquals("Broken output stream: close()", exception.getMessage(), "Exception message mismatch.");
    }

    @Test
    void testSetByteCountWithOutputStream() throws IOException {
        final AtomicBoolean thresholdReached = new AtomicBoolean();
        final int initialByteCount = 2;
        final int threshold = 3;

        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold) {
            {
                setByteCount(initialByteCount);
            }

            @Override
            protected OutputStream getOutputStream() throws IOException {
                return new ByteArrayOutputStream(4);
            }

            @Override
            protected void thresholdReached() throws IOException {
                thresholdReached.set(true);
            }
        }) {
            verifyInitialState(out, threshold, initialByteCount);
            out.write('a');
            assertFalse(thresholdReached.get(), "Threshold should not be reached after first write.");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded.");
            out.write('a');
            assertTrue(thresholdReached.get(), "Threshold should be reached after second write.");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded.");
        }
    }

    @Test
    void testThresholdWithIOConsumer() throws IOException {
        final int threshold = 1;

        // Test with null threshold consumer
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, null, os -> new ByteArrayOutputStream(4))) {
            verifyInitialState(out, threshold, 0);
            out.write('a');
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after first write.");
            out.write('a');
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded after second write.");
        }

        // Test with null output stream function
        final AtomicBoolean thresholdReached = new AtomicBoolean();
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, os -> thresholdReached.set(true), null)) {
            verifyInitialState(out, threshold, 0);
            out.write('a');
            assertFalse(thresholdReached.get(), "Threshold should not be reached after first write.");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded.");
            out.write('a');
            assertTrue(thresholdReached.get(), "Threshold should be reached after second write.");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded.");
        }

        // Test with non-null inputs
        thresholdReached.set(false);
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, os -> thresholdReached.set(true), os -> new ByteArrayOutputStream(4))) {
            verifyInitialState(out, threshold, 0);
            out.write('a');
            assertFalse(thresholdReached.get(), "Threshold should not be reached after first write.");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded.");
            out.write('a');
            assertTrue(thresholdReached.get(), "Threshold should be reached after second write.");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded.");
        }
    }

    @Test
    void testThresholdIOConsumerIOException() throws IOException {
        final int threshold = 1;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, os -> {
            throw new IOException("Threshold reached.");
        }, os -> new ByteArrayOutputStream(4))) {
            verifyInitialState(out, threshold, 0);
            out.write('a');
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after first write.");
            assertThrows(IOException.class, () -> out.write('a'), "IOException expected on threshold breach.");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after exception.");
        }
    }

    @Test
    void testThresholdIOConsumerUncheckedException() throws IOException {
        final int threshold = 1;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold, os -> {
            throw new IllegalStateException("Threshold reached.");
        }, os -> new ByteArrayOutputStream(4))) {
            verifyInitialState(out, threshold, 0);
            out.write('a');
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after first write.");
            assertThrows(IllegalStateException.class, () -> out.write('a'), "IllegalStateException expected on threshold breach.");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after exception.");
            assertInstanceOf(ByteArrayOutputStream.class, out.getOutputStream(), "Output stream type mismatch.");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after exception.");
        }
    }

    @Test
    void testThresholdLessThanZero() throws IOException {
        final AtomicBoolean thresholdReached = new AtomicBoolean();
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(-1) {
            @Override
            protected void thresholdReached() throws IOException {
                thresholdReached.set(true);
            }
        }) {
            verifyInitialState(out, 0, 0);
            assertFalse(thresholdReached.get(), "Threshold should not be reached initially.");
            out.write(89);
            assertTrue(thresholdReached.get(), "Threshold should be reached after first write.");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded.");
            assertInstanceOf(NullOutputStream.class, out.getOutputStream(), "Output stream type mismatch.");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded.");
        }
    }

    @Test
    void testThresholdZero() throws IOException {
        final AtomicBoolean thresholdReached = new AtomicBoolean();
        final int threshold = 0;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold) {
            @Override
            protected void thresholdReached() throws IOException {
                thresholdReached.set(true);
            }
        }) {
            verifyInitialState(out, threshold, 0);
            out.write(89);
            assertTrue(thresholdReached.get(), "Threshold should be reached after first write.");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded.");
            assertInstanceOf(NullOutputStream.class, out.getOutputStream(), "Output stream type mismatch.");
            assertTrue(out.isThresholdExceeded(), "Threshold should be exceeded.");
        }
    }

    @Test
    void testThresholdZeroWrite() throws IOException {
        final AtomicBoolean thresholdReached = new AtomicBoolean();
        final int threshold = 7;
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold) {
            @Override
            protected void thresholdReached() throws IOException {
                super.thresholdReached();
                thresholdReached.set(true);
            }
        }) {
            verifyInitialState(out, threshold, 0);
            assertFalse(thresholdReached.get(), "Threshold should not be reached initially.");
            out.write(new byte[0]);
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after zero-byte write.");
            assertFalse(thresholdReached.get(), "Threshold should not be reached after zero-byte write.");
            assertInstanceOf(NullOutputStream.class, out.getOutputStream(), "Output stream type mismatch.");
            assertFalse(out.isThresholdExceeded(), "Threshold should not be exceeded after zero-byte write.");
        }
    }
}