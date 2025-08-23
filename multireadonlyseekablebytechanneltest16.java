package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MultiReadOnlySeekableByteChannelTestTest16 {

    private void check(final byte[] expected) throws IOException {
        for (int channelSize = 1; channelSize <= expected.length; channelSize++) {
            // Sanity check that all operations work for SeekableInMemoryByteChannel
            try (SeekableByteChannel single = makeSingle(expected)) {
                check(expected, single);
            }
            // Checks against our MultiReadOnlySeekableByteChannel instance
            try (SeekableByteChannel multi = makeMulti(grouped(expected, channelSize))) {
                check(expected, multi);
            }
        }
    }

    private void check(final byte[] expected, final SeekableByteChannel channel) throws IOException {
        for (int readBufferSize = 1; readBufferSize <= expected.length + 5; readBufferSize++) {
            check(expected, channel, readBufferSize);
        }
    }

    private void check(final byte[] expected, final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        assertTrue(channel.isOpen(), "readBufferSize " + readBufferSize);
        assertEquals(expected.length, channel.size(), "readBufferSize " + readBufferSize);
        channel.position(0);
        assertEquals(0, channel.position(), "readBufferSize " + readBufferSize);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), "readBufferSize " + readBufferSize);
        // Will hold the entire result that we read
        final ByteBuffer resultBuffer = ByteBuffer.allocate(expected.length + 100);
        // Used for each read() method call
        final ByteBuffer buf = ByteBuffer.allocate(readBufferSize);
        int bytesRead = channel.read(buf);
        while (bytesRead != -1) {
            final int remaining = buf.remaining();
            buf.flip();
            resultBuffer.put(buf);
            buf.clear();
            bytesRead = channel.read(buf);
            // If this isn't the last read() then we expect the buf
            // ByteBuffer to be full (i.e. have no remaining)
            if (resultBuffer.position() < expected.length) {
                assertEquals(0, remaining, "readBufferSize " + readBufferSize);
            }
            if (bytesRead == -1) {
                assertEquals(0, buf.position(), "readBufferSize " + readBufferSize);
            } else {
                assertEquals(bytesRead, buf.position(), "readBufferSize " + readBufferSize);
            }
        }
        resultBuffer.flip();
        final byte[] arr = new byte[resultBuffer.remaining()];
        resultBuffer.get(arr);
        assertArrayEquals(expected, arr, "readBufferSize " + readBufferSize);
    }

    private void checkEmpty(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(10);
        assertTrue(channel.isOpen());
        assertEquals(0, channel.size());
        assertEquals(0, channel.position());
        assertEquals(-1, channel.read(buf));
        channel.position(5);
        assertEquals(-1, channel.read(buf));
        channel.close();
        assertFalse(channel.isOpen());
        assertThrows(ClosedChannelException.class, () -> channel.read(buf), "expected a ClosedChannelException");
        assertThrows(ClosedChannelException.class, () -> channel.position(100), "expected a ClosedChannelException");
    }

    private byte[][] grouped(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        int idx = 0;
        for (; idx + chunkSize <= input.length; idx += chunkSize) {
            groups.add(Arrays.copyOfRange(input, idx, idx + chunkSize));
        }
        if (idx < input.length) {
            groups.add(Arrays.copyOfRange(input, idx, input.length));
        }
        return groups.toArray(new byte[0][]);
    }

    private SeekableByteChannel makeEmpty() {
        return makeSingle(ByteUtils.EMPTY_BYTE_ARRAY);
    }

    private SeekableByteChannel makeMulti(final byte[][] arr) {
        final SeekableByteChannel[] s = new SeekableByteChannel[arr.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = makeSingle(arr[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(s);
    }

    private SeekableByteChannel makeSingle(final byte[] arr) {
        return new SeekableInMemoryByteChannel(arr);
    }

    private SeekableByteChannel testChannel() {
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(makeEmpty(), makeEmpty());
    }

    private static final class ThrowingSeekableByteChannel implements SeekableByteChannel {

        private boolean closed;

        @Override
        public void close() throws IOException {
            closed = true;
            throw new IOException("foo");
        }

        @Override
        public boolean isOpen() {
            return !closed;
        }

        @Override
        public long position() {
            return 0;
        }

        @Override
        public SeekableByteChannel position(final long newPosition) {
            return this;
        }

        @Override
        public int read(final ByteBuffer dst) throws IOException {
            return -1;
        }

        @Override
        public long size() throws IOException {
            return 0;
        }

        @Override
        public SeekableByteChannel truncate(final long size) {
            return this;
        }

        @Override
        public int write(final ByteBuffer src) throws IOException {
            return 0;
        }
    }

    /*
     * <q>IOException - If the new position is negative</q>
     */
    @Test
    void testThrowsIOExceptionWhenPositionIsSetToANegativeValue() throws Exception {
        try (SeekableByteChannel c = testChannel()) {
            assertThrows(IllegalArgumentException.class, () -> c.position(-1));
        }
    }
}
