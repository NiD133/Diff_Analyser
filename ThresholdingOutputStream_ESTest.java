package org.apache.commons.io.output;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;
import org.junit.Test;

/**
 * Readable, behavior-focused tests for ThresholdingOutputStream.
 *
 * These tests avoid EvoSuite scaffolding and aim to document the expected
 * behavior of ThresholdingOutputStream clearly:
 * - Threshold normalization (negative -> 0)
 * - Byte counting
 * - Threshold-crossing semantics (exceeding vs reaching)
 * - Delegation to underlying OutputStream
 * - Argument validation and exceptions
 */
public class ThresholdingOutputStreamTest {

    /**
     * Simple OutputStream that records data written and whether flush/close were called.
     */
    private static class RecordingOutputStream extends OutputStream {
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        private boolean flushed;
        private boolean closed;

        @Override
        public void write(int b) throws IOException {
            buffer.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            buffer.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            flushed = true;
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        byte[] toByteArray() {
            return buffer.toByteArray();
        }

        boolean isFlushed() {
            return flushed;
        }

        boolean isClosed() {
            return closed;
        }
    }

    /**
     * Testable subclass exposing protected mutators for byte count.
     */
    private static class TestableThresholdingOutputStream extends ThresholdingOutputStream {
        TestableThresholdingOutputStream(int threshold, IOConsumer<ThresholdingOutputStream> c,
                                         IOFunction<ThresholdingOutputStream, OutputStream> g) {
            super(threshold, c, g);
        }

        void publicSetByteCount(long n) {
            setByteCount(n);
        }

        void publicResetByteCount() {
            resetByteCount();
        }
    }

    @Test
    public void constructor_normalizesNegativeThresholdToZero() {
        ThresholdingOutputStream tos = new ThresholdingOutputStream(-5);
        assertEquals(0, tos.getThreshold());
        assertEquals(0L, tos.getByteCount());
        assertFalse(tos.isThresholdExceeded());
    }

    @Test
    public void constructor_keepsPositiveThreshold() {
        ThresholdingOutputStream tos = new ThresholdingOutputStream(76);
        assertEquals(76, tos.getThreshold());
        assertEquals(0L, tos.getByteCount());
        assertFalse(tos.isThresholdExceeded());
    }

    @Test
    public void byteCount_startsAtZero_andIncrementsOnWrites() throws IOException {
        RecordingOutputStream ros = new RecordingOutputStream();
        ThresholdingOutputStream tos = new ThresholdingOutputStream(
                100,
                IOConsumer.noop(),
                os -> ros);

        tos.write(1);
        assertEquals(1L, tos.getByteCount());

        byte[] data = new byte[9];
        tos.write(data);
        assertEquals(10L, tos.getByteCount());

        tos.write(data, 2, 5);
        assertEquals(15L, tos.getByteCount());

        // Also verify that data really went to the underlying stream.
        assertEquals(1 + 9 + 5, ros.toByteArray().length);
    }

    @Test
    public void isThresholdExceeded_falseBeforeWrite_trueAfterWriteWhenThresholdZero() throws IOException {
        ThresholdingOutputStream tos = new ThresholdingOutputStream(0);
        assertFalse(tos.isThresholdExceeded());

        tos.write(123);
        assertTrue(tos.isThresholdExceeded());
        assertEquals(1L, tos.getByteCount());
    }

    @Test
    public void thresholdConsumer_invokedWhenExceedingThreshold_andOnEverySubsequentWrite() throws IOException {
        AtomicInteger events = new AtomicInteger(0);
        RecordingOutputStream ros = new RecordingOutputStream();

        // Threshold = 2 means exceeding happens when total would become 3 or more.
        ThresholdingOutputStream tos = new ThresholdingOutputStream(
                2,
                os -> events.incrementAndGet(),
                os -> ros);

        // Write exactly to the threshold: no event yet.
        tos.write(new byte[] {1, 2});
        assertEquals(2L, tos.getByteCount());
        assertFalse(tos.isThresholdExceeded());
        assertEquals(0, events.get());

        // Next write exceeds: event fires and thresholdExceeded becomes true.
        tos.write(3);
        assertEquals(3L, tos.getByteCount());
        assertTrue(tos.isThresholdExceeded());
        assertEquals(1, events.get());

        // Every subsequent write also fires the event.
        tos.write(4);
        tos.write(new byte[] {5, 6});
        assertEquals(6L, tos.getByteCount());
        assertTrue(tos.isThresholdExceeded());
        assertEquals(3, events.get());
    }

    @Test
    public void write_zeroLengthDoesNotChangeCountOrTriggerEvent() throws IOException {
        AtomicInteger events = new AtomicInteger(0);
        ThresholdingOutputStream tos = new ThresholdingOutputStream(
                0,
                os -> events.incrementAndGet(),
                os -> new RecordingOutputStream());

        tos.write(new byte[0]);
        assertEquals(0L, tos.getByteCount());
        assertFalse(tos.isThresholdExceeded());
        assertEquals(0, events.get());

        tos.write(new byte[10], 5, 0);
        assertEquals(0L, tos.getByteCount());
        assertFalse(tos.isThresholdExceeded());
        assertEquals(0, events.get());
    }

    @Test
    public void write_nullArray_throwsNullPointerException() {
        ThresholdingOutputStream tos = new ThresholdingOutputStream(10);
        try {
            tos.write((byte[]) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    @Test
    public void write_nullArrayWithOffLen_throwsNullPointerException() throws IOException {
        ThresholdingOutputStream tos = new ThresholdingOutputStream(10);
        try {
            tos.write((byte[]) null, 0, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    @Test
    public void write_withInvalidBounds_throwsIndexOutOfBoundsException() throws IOException {
        ThresholdingOutputStream tos = new ThresholdingOutputStream(10);

        byte[] data = new byte[3];

        // Negative offset
        try {
            tos.write(data, -1, 1);
            fail("Expected IndexOutOfBoundsException for negative offset");
        } catch (IndexOutOfBoundsException expected) {
            // ok
        }

        // Negative length
        try {
            tos.write(data, 0, -1);
            fail("Expected IndexOutOfBoundsException for negative length");
        } catch (IndexOutOfBoundsException expected) {
            // ok
        }

        // off + len beyond array length
        try {
            tos.write(data, 2, 2);
            fail("Expected IndexOutOfBoundsException for off+len > length");
        } catch (IndexOutOfBoundsException expected) {
            // ok
        }
    }

    @Test
    public void flushAndClose_delegateToUnderlyingStream() throws IOException {
        RecordingOutputStream ros = new RecordingOutputStream();
        ThresholdingOutputStream tos = new ThresholdingOutputStream(
                1,
                IOConsumer.noop(),
                os -> ros);

        tos.write(1); // acquire underlying stream
        assertFalse(ros.isFlushed());
        assertFalse(ros.isClosed());

        tos.flush();
        assertTrue(ros.isFlushed());
        assertFalse(ros.isClosed());

        tos.close();
        assertTrue(ros.isClosed());
    }

    @Test
    public void setAndResetByteCount_workAsExpected() throws IOException {
        RecordingOutputStream ros = new RecordingOutputStream();
        TestableThresholdingOutputStream tos = new TestableThresholdingOutputStream(
                5,
                IOConsumer.noop(),
                os -> ros);

        assertEquals(0L, tos.getByteCount());

        tos.publicSetByteCount(42);
        assertEquals(42L, tos.getByteCount());

        tos.publicResetByteCount();
        assertEquals(0L, tos.getByteCount());
        assertFalse(tos.isThresholdExceeded());
    }
}