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

/**
 * Tests {@link ThresholdingOutputStream}. See also the subclass {@link DeferredFileOutputStream}.
 *
 * @see DeferredFileOutputStream
 */
class ThresholdingOutputStreamTest {

    private static final int BYTE_A = 'a';
    private static final int SMALL_CAPACITY = 4;

    /**
     * Asserts initial state without changing it.
     *
     * @param out the stream to test.
     * @param expectedThreshold the expected threshold.
     * @param expectedByteCount the expected byte count.
     */
    static void assertInitialState(final ThresholdingOutputStream out, final int expectedThreshold, final int expectedByteCount) {
        assertFalse(out.isThresholdExceeded(), "Threshold must not be exceeded initially");
        assertEquals(expectedThreshold, out.getThreshold(), "Unexpected threshold");
        assertEquals(expectedByteCount, out.getByteCount(), "Unexpected initial byte count");
    }

    @Test
    void testResetByteCount() throws IOException {
        // Given a threshold of 1 and a consumer that resets the byte count when the threshold is reached
        final int threshold = 1;
        final AtomicInteger thresholdReachedCount = new AtomicInteger();

        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             ThresholdingOutputStream out = new ThresholdingOutputStream(
                 threshold,
                 tos -> {
                     thresholdReachedCount.incrementAndGet();
                     tos.resetByteCount();
                 },
                 o -> os)) {

            assertInitialState(out, threshold, 0);
            assertEquals(0, thresholdReachedCount.get());

            // First byte does not exceed threshold
            out.write(BYTE_A);
            assertFalse(out.isThresholdExceeded());

            // Second byte would exceed threshold -> consumer runs and resets byte count
            out.write(BYTE_A);
            assertEquals(1, thresholdReachedCount.get());
            assertFalse(out.isThresholdExceeded());

            // Each next write that would exceed threshold triggers again because byte count gets reset each time
            out.write(BYTE_A);
            out.write(BYTE_A);
            assertEquals(3, thresholdReachedCount.get());
        }
    }

    @Test
    void testResetByteCountWithBrokenOutputStream() {
        // Given a threshold of 1 but the underlying stream is broken (throws on close)
        final int threshold = 1;
        final AtomicInteger thresholdReachedCount = new AtomicInteger();

        final IOException closeException = assertThrows(IOException.class, () -> {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                 ThresholdingOutputStream out = new ThresholdingOutputStream(
                     threshold,
                     tos -> {
                         thresholdReachedCount.incrementAndGet();
                         tos.resetByteCount();
                     },
                     o -> BrokenOutputStream.INSTANCE)) {

                assertInitialState(out, threshold, 0);
                assertEquals(0, thresholdReachedCount.get());

                // All write attempts throw immediately; threshold is never marked as exceeded,
                // and the consumer is never invoked.
                assertThrows(IOException.class, () -> out.write(BYTE_A));
                assertFalse(out.isThresholdExceeded());

                assertThrows(IOException.class, () -> out.write(BYTE_A));
                assertEquals(0, thresholdReachedCount.get());
                assertFalse(out.isThresholdExceeded());

                assertThrows(IOException.class, () -> out.write(BYTE_A));
                assertThrows(IOException.class, () -> out.write(BYTE_A));
                assertEquals(0, thresholdReachedCount.get());
            }
        });

        // The only thrown IOException is from closing the broken stream at the end of try-with-resources
        assertEquals("Broken output stream: close()", closeException.getMessage());
    }

    /**
     * A ThresholdingOutputStream that:
     * - sets an initial byte count
     * - signals when the threshold is reached
     * - provides the underlying stream via getOutputStream().
     */
    private static final class SettableCountUsingGetOutput extends ThresholdingOutputStream {
        private final AtomicBoolean reached;

        SettableCountUsingGetOutput(final int threshold, final long initialCount, final AtomicBoolean reached) {
            super(threshold);
            this.reached = reached;
            setByteCount(initialCount);
        }

        @Override
        protected OutputStream getOutputStream() {
            return new ByteArrayOutputStream(SMALL_CAPACITY);
        }

        @Override
        protected void thresholdReached() {
            reached.set(true);
        }
    }

    /**
     * A ThresholdingOutputStream that:
     * - sets an initial byte count
     * - signals when the threshold is reached
     * - provides the underlying stream via the deprecated getStream() method.
     */
    @SuppressWarnings("deprecation")
    private static final class SettableCountUsingGetStream extends ThresholdingOutputStream {
        private final AtomicBoolean reached;

        SettableCountUsingGetStream(final int threshold, final long initialCount, final AtomicBoolean reached) {
            super(threshold);
            this.reached = reached;
            setByteCount(initialCount);
        }

        @Override
        protected OutputStream getStream() {
            return new ByteArrayOutputStream(SMALL_CAPACITY);
        }

        @Override
        protected void thresholdReached() {
            reached.set(true);
        }
    }

    @Test
    void testSetByteCountUsingGetOutputStream() throws IOException {
        final AtomicBoolean reached = new AtomicBoolean(false);
        final int initCount = 2;
        final int threshold = 3;

        try (ThresholdingOutputStream out = new SettableCountUsingGetOutput(threshold, initCount, reached)) {
            assertInitialState(out, threshold, initCount);

            out.write(BYTE_A);
            assertFalse(reached.get());
            assertFalse(out.isThresholdExceeded());

            out.write(BYTE_A);
            assertTrue(reached.get());
            assertTrue(out.isThresholdExceeded());
        }
    }

    @Test
    void testSetByteCountUsingDeprecatedGetStream() throws IOException {
        final AtomicBoolean reached = new AtomicBoolean(false);
        final int initCount = 2;
        final int threshold = 3;

        try (ThresholdingOutputStream out = new SettableCountUsingGetStream(threshold, initCount, reached)) {
            assertInitialState(out, threshold, initCount);

            out.write(BYTE_A);
            assertFalse(reached.get());
            assertFalse(out.isThresholdExceeded());

            out.write(BYTE_A);
            assertTrue(reached.get());
            assertTrue(out.isThresholdExceeded());
        }
    }

    @Test
    void testThresholdConsumerAndStreamFactoryVariants() throws IOException {
        final int threshold = 1;

        // 1) Null consumer, non-null stream supplier
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(
            threshold,
            null,
            os -> new ByteArrayOutputStream(SMALL_CAPACITY))) {

            assertInitialState(out, threshold, 0);
            out.write(BYTE_A);
            assertFalse(out.isThresholdExceeded());

            out.write(BYTE_A);
            assertTrue(out.isThresholdExceeded());
        }

        // 2) Non-null consumer, null stream supplier
        final AtomicBoolean reached = new AtomicBoolean(false);
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(
            threshold,
            os -> reached.set(true),
            null)) {

            assertInitialState(out, threshold, 0);
            out.write(BYTE_A);
            assertFalse(reached.get());
            assertFalse(out.isThresholdExceeded());

            out.write(BYTE_A);
            assertTrue(reached.get());
            assertTrue(out.isThresholdExceeded());
        }

        // 3) Both non-null
        reached.set(false);
        try (ThresholdingOutputStream out = new ThresholdingOutputStream(
            threshold,
            os -> reached.set(true),
            os -> new ByteArrayOutputStream(SMALL_CAPACITY))) {

            assertInitialState(out, threshold, 0);
            out.write(BYTE_A);
            assertFalse(reached.get());
            assertFalse(out.isThresholdExceeded());

            out.write(BYTE_A);
            assertTrue(reached.get());
            assertTrue(out.isThresholdExceeded());
        }
    }

    @Test
    void testThresholdConsumerThrowsCheckedIOException() throws IOException {
        final int threshold = 1;

        try (ThresholdingOutputStream out = new ThresholdingOutputStream(
            threshold,
            os -> { throw new IOException("Threshold reached."); },
            os -> new ByteArrayOutputStream(SMALL_CAPACITY))) {

            assertInitialState(out, threshold, 0);
            out.write(BYTE_A); // safe
            assertFalse(out.isThresholdExceeded());

            // Next write tries to cross threshold and consumer throws
            assertThrows(IOException.class, () -> out.write(BYTE_A));
            assertFalse(out.isThresholdExceeded());
        }
    }

    @Test
    void testThresholdConsumerThrowsUncheckedException() throws IOException {
        final int threshold = 1;

        try (ThresholdingOutputStream out = new ThresholdingOutputStream(
            threshold,
            os -> { throw new IllegalStateException("Threshold reached."); },
            os -> new ByteArrayOutputStream(SMALL_CAPACITY))) {

            assertInitialState(out, threshold, 0);
            out.write(BYTE_A); // safe
            assertFalse(out.isThresholdExceeded());

            // Next write tries to cross threshold and consumer throws
            assertThrows(IllegalStateException.class, () -> out.write(BYTE_A));
            assertFalse(out.isThresholdExceeded());

            // Stream supplier is still accessible; threshold still not exceeded
            assertInstanceOf(ByteArrayOutputStream.class, out.getOutputStream());
            assertFalse(out.isThresholdExceeded());
        }
    }

    /**
     * A negative threshold is treated as 0; the threshold isn't considered exceeded until a write occurs.
     */
    @Test
    void testNegativeThresholdBehavesAsZeroUntilFirstWrite() throws IOException {
        final AtomicBoolean reached = new AtomicBoolean(false);

        try (ThresholdingOutputStream out = new ThresholdingOutputStream(
            -1,
            os -> reached.set(true),
            null)) {

            assertInitialState(out, 0, 0);
            assertFalse(reached.get());

            out.write(89);
            assertTrue(reached.get());
            assertTrue(out.isThresholdExceeded());
            assertInstanceOf(NullOutputStream.class, out.getOutputStream());
            assertTrue(out.isThresholdExceeded());
        }
    }

    @Test
    void testZeroThresholdTriggersOnFirstWrite() throws IOException {
        final AtomicBoolean reached = new AtomicBoolean(false);
        final int threshold = 0;

        try (ThresholdingOutputStream out = new ThresholdingOutputStream(
            threshold,
            os -> reached.set(true),
            null)) {

            assertInitialState(out, threshold, 0);

            out.write(89);
            assertTrue(reached.get());
            assertTrue(out.isThresholdExceeded());
            assertInstanceOf(NullOutputStream.class, out.getOutputStream());
            assertTrue(out.isThresholdExceeded());
        }
    }

    /**
     * Writing zero bytes should not trigger the threshold.
     */
    @Test
    void testWriteZeroBytesDoesNotTriggerThreshold() throws IOException {
        final AtomicBoolean reached = new AtomicBoolean(false);
        final int threshold = 7;

        try (ThresholdingOutputStream out = new ThresholdingOutputStream(threshold) {
            @Override
            protected void thresholdReached() throws IOException {
                super.thresholdReached();
                reached.set(true);
            }
        }) {
            assertInitialState(out, threshold, 0);
            assertFalse(reached.get());

            out.write(new byte[0]);

            assertFalse(out.isThresholdExceeded());
            assertFalse(reached.get());
            assertInstanceOf(NullOutputStream.class, out.getOutputStream());
            assertFalse(out.isThresholdExceeded());
        }
    }
}