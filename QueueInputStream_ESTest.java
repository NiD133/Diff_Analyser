package org.apache.commons.io.input;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.output.QueueOutputStream;
import org.junit.Test;

/**
 * Readable, behavior-focused tests for QueueInputStream.
 *
 * These tests avoid EvoSuite scaffolding and aim to clearly describe the
 * expected behavior of QueueInputStream and its Builder.
 */
public class QueueInputStreamTest {

    // ----------------------------
    // Builder API
    // ----------------------------

    @Test
    public void builder_setTimeout_isFluent_and_appliedToStream() {
        // Given a specific timeout
        final Duration timeout = Duration.ofMillis(123);

        // When configuring a builder
        final QueueInputStream.Builder builder = QueueInputStream.builder();
        final QueueInputStream.Builder returned = builder.setTimeout(timeout);

        // Then the builder API is fluent
        assertSame(builder, returned);

        // And the constructed stream reflects the timeout
        final QueueInputStream in = builder.get();
        assertEquals(timeout, in.getTimeout());
    }

    @Test
    public void builder_setTimeout_nullMeansDefaultZero() {
        final QueueInputStream.Builder builder = QueueInputStream.builder();

        // Should not throw and should reset to default
        builder.setTimeout(null);

        final QueueInputStream in = builder.get();
        assertEquals(Duration.ZERO, in.getTimeout());
    }

    @Test
    public void builder_setTimeout_negative_throwsIAE() {
        final QueueInputStream.Builder builder = QueueInputStream.builder();
        assertThrows(IllegalArgumentException.class, () -> builder.setTimeout(Duration.ofSeconds(-1)));
    }

    @Test
    public void builder_setBlockingQueue_isFluent() {
        final QueueInputStream.Builder builder = new QueueInputStream.Builder();
        final QueueInputStream.Builder returned = builder.setBlockingQueue(new LinkedBlockingQueue<>());

        // Fluent API
        assertSame(builder, returned);
    }

    @Test
    public void builder_get_returnsStream() {
        final QueueInputStream in = QueueInputStream.builder().get();
        assertNotNull(in);
    }

    // ----------------------------
    // Construction and accessors
    // ----------------------------

    @Test
    public void constructor_withNullQueue_createsInternalQueue() {
        final QueueInputStream in = new QueueInputStream((BlockingQueue<Integer>) null);
        assertNotNull(in.getBlockingQueue());
    }

    // ----------------------------
    // Read semantics
    // ----------------------------

    @Test
    public void read_returnsMinusOneWhenNoDataAndZeroTimeout() {
        // Default timeout is zero, so an empty queue yields -1 immediately.
        final QueueInputStream in = new QueueInputStream();
        assertEquals(-1, in.read());
    }

    @Test
    public void read_returnsUnsignedByteValue_fromQueueElement() {
        final LinkedBlockingQueue<Integer> q = new LinkedBlockingQueue<>();
        final QueueInputStream in = new QueueInputStream(q);

        // Enqueue values directly as the stream reads from a BlockingQueue<Integer>
        q.add(0);
        q.add(-728); // -728 mod 256 == 40

        assertEquals(0, in.read());
        assertEquals(40, in.read());
        // No more data
        assertEquals(-1, in.read());
    }

    @Test
    public void read_intoBuffer_readsAvailableBytesHonoringOffsetAndLength() throws IOException {
        final QueueInputStream in = new QueueInputStream();
        final QueueOutputStream out = in.newQueueOutputStream();

        // Write 3 bytes: 10, 20, 30
        out.write(new byte[] {10, 20, 30});
        out.close(); // Closing the output stream has no effect on the input stream

        final byte[] buffer = new byte[5];
        // Fill with sentinel values to verify only target region is written
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) 0xEE;
        }

        // Read at offset 1, length 2 -> should read first 2 bytes (10, 20)
        final int n1 = in.read(buffer, 1, 2);
        assertEquals(2, n1);
        assertArrayEquals(new byte[] {(byte) 0xEE, 10, 20, (byte) 0xEE, (byte) 0xEE}, buffer);

        // Read remaining 1 byte (30)
        final int n2 = in.read(buffer, 0, 5);
        assertEquals(1, n2);
        // The first position now holds 30
        assertEquals(30, buffer[0] & 0xFF);

        // Now empty -> -1 on subsequent read attempts (first byte honors timeout)
        assertEquals(-1, in.read(buffer, 0, 5));
    }

    @Test
    public void read_withZeroLength_returnsZero() {
        final QueueInputStream in = new QueueInputStream();
        final byte[] buffer = new byte[8];

        // Zero-length read must return 0 and not consult the queue
        assertEquals(0, in.read(buffer, 0, 0));
    }

    // ----------------------------
    // Argument validation for read(byte[], off, len)
    // ----------------------------

    @Test
    public void read_throwsNPE_whenBufferIsNull() {
        final QueueInputStream in = new QueueInputStream();
        assertThrows(NullPointerException.class, () -> in.read(null, 0, 1));
    }

    @Test
    public void read_throwsIOOBE_whenOffsetNegative() {
        final QueueInputStream in = new QueueInputStream();
        final byte[] b = new byte[1];
        assertThrows(IndexOutOfBoundsException.class, () -> in.read(b, -1, 1));
    }

    @Test
    public void read_throwsIOOBE_whenLengthNegative() {
        final QueueInputStream in = new QueueInputStream();
        final byte[] b = new byte[1];
        assertThrows(IndexOutOfBoundsException.class, () -> in.read(b, 0, -1));
    }

    @Test
    public void read_throwsIOOBE_whenOffsetPlusLengthExceedsBuffer() {
        final QueueInputStream in = new QueueInputStream();
        final byte[] b = new byte[4];
        assertThrows(IndexOutOfBoundsException.class, () -> in.read(b, 3, 2));
    }

    // ----------------------------
    // skip()
    // ----------------------------

    @Test
    public void skip_skipsAvailableBytes() throws IOException {
        final LinkedBlockingQueue<Integer> q = new LinkedBlockingQueue<>();
        final QueueInputStream in = new QueueInputStream(q);

        q.add(123);

        // One element available; request a larger skip
        assertEquals(1L, in.skip(10));

        // Queue is now empty
        assertEquals(-1, in.read());
    }

    // ----------------------------
    // Bridging with QueueOutputStream
    // ----------------------------

    @Test
    public void newQueueOutputStream_bridgesWritesToInput() throws IOException {
        final QueueInputStream in = new QueueInputStream();
        final QueueOutputStream out = in.newQueueOutputStream();

        // Write bytes including a value > 127 to verify unsigned handling
        out.write(new byte[] {1, 2, (byte) 255});
        out.flush();

        assertEquals(1, in.read());
        assertEquals(2, in.read());
        assertEquals(255, in.read());
        assertEquals(-1, in.read());
    }
}