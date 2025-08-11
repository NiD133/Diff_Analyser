/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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

/**
 * Tests for MultiReadOnlySeekableByteChannel.
 *
 * The tests assert that:
 * - Concatenation behaves like a single SeekableByteChannel for reads and positioning.
 * - Read-only contract is enforced (no writes/truncates).
 * - Close behavior is correct (idempotent, propagates to all parts, and exceptions are surfaced).
 *
 * Initially based on:
 * https://github.com/frugalmechanic/fm-common/blob/master/jvm/src/test/scala/fm/common/TestMultiReadOnlySeekableByteChannel.scala
 */
class MultiReadOnlySeekableByteChannelTest {

    private static final String ALPHANUMERIC =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Test double that throws on close but tracks open/closed state.
     */
    private static final class ThrowingSeekableByteChannel implements SeekableByteChannel {
        private boolean closed;

        @Override
        public void close() throws IOException {
            closed = true;
            throw new IOException("closing failed");
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

    // -----------------------
    // Channel factory helpers
    // -----------------------

    private SeekableByteChannel newEmptyChannel() {
        return newInMemoryChannel(ByteUtils.EMPTY_BYTE_ARRAY);
    }

    private SeekableByteChannel newInMemoryChannel(final byte[] bytes) {
        return new SeekableInMemoryByteChannel(bytes);
    }

    private SeekableByteChannel newConcatenatedChannel(final byte[][] chunks) {
        final SeekableByteChannel[] parts = new SeekableByteChannel[chunks.length];
        for (int i = 0; i < chunks.length; i++) {
            parts[i] = newInMemoryChannel(chunks[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(parts);
    }

    private SeekableByteChannel emptyConcatenatedChannel() {
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(newEmptyChannel(), newEmptyChannel());
    }

    // -----------------------
    // Utility helpers/asserts
    // -----------------------

    /**
     * Split a byte array into equally sized chunks with the last chunk possibly smaller.
     */
    private byte[][] splitIntoChunks(final byte[] input, final int chunkSize) {
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

    /**
     * Asserts that the given channel behaves like an empty channel according to the SeekableByteChannel contract.
     */
    private void assertEmptyChannelBehavior(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(10);

        assertTrue(channel.isOpen());
        assertEquals(0, channel.size());
        assertEquals(0, channel.position());
        assertEquals(-1, channel.read(buf));

        channel.position(5);
        assertEquals(-1, channel.read(buf));

        channel.close();
        assertFalse(channel.isOpen());

        assertThrows(ClosedChannelException.class, () -> channel.read(buf), "read on closed channel");
        assertThrows(ClosedChannelException.class, () -> channel.position(100), "position(long) on closed channel");
    }

    /**
     * Verifies that reading the expected bytes works correctly for:
     * - a single in-memory channel, and
     * - a concatenated channel built from chunks of size 1..expected.length.
     */
    private void verifyReadAllBytes(final byte[] expected) throws IOException {
        for (int chunkSize = 1; chunkSize <= expected.length; chunkSize++) {
            // Sanity check against the reference implementation
            try (SeekableByteChannel reference = newInMemoryChannel(expected)) {
                verifyReadForAllBufferSizes(expected, reference);
            }
            // Actual class under test (concatenated)
            try (SeekableByteChannel concatenated = newConcatenatedChannel(splitIntoChunks(expected, chunkSize))) {
                verifyReadForAllBufferSizes(expected, concatenated);
            }
        }
    }

    /**
     * Verifies reading with a variety of ByteBuffer sizes for a given channel.
     */
    private void verifyReadForAllBufferSizes(final byte[] expected, final SeekableByteChannel channel) throws IOException {
        for (int readBufferSize = 1; readBufferSize <= expected.length + 5; readBufferSize++) {
            verifySingleReadPass(expected, channel, readBufferSize);
        }
    }

    /**
     * Reads the whole channel into a result buffer using the given readBufferSize and asserts:
     * - position/size are consistent
     * - intermediate buffer states are as expected
     * - the final byte sequence matches the expected data
     */
    private void verifySingleReadPass(final byte[] expected, final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        final String msg = "readBufferSize=" + readBufferSize;

        assertTrue(channel.isOpen(), msg);
        assertEquals(expected.length, channel.size(), msg);

        channel.position(0);
        assertEquals(0, channel.position(), msg);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), msg);

        final ByteBuffer allRead = ByteBuffer.allocate(expected.length + 100); // generous headroom
        final ByteBuffer readBuffer = ByteBuffer.allocate(readBufferSize);

        int bytesRead = channel.read(readBuffer);
        while (bytesRead != -1) {
            final int remainingBeforeFlip = readBuffer.remaining();

            readBuffer.flip();
            allRead.put(readBuffer);
            readBuffer.clear();

            // If not at EOF yet, the read buffer should have been filled
            if (allRead.position() < expected.length) {
                assertEquals(0, remainingBeforeFlip, msg);
            }

            bytesRead = channel.read(readBuffer);

            if (bytesRead == -1) {
                // readBuffer should not have advanced for EOF
                assertEquals(0, readBuffer.position(), msg);
            } else {
                // readBuffer's position equals the bytes read
                assertEquals(bytesRead, readBuffer.position(), msg);
            }
        }

        allRead.flip();
        final byte[] actual = new byte[allRead.remaining()];
        allRead.get(actual);

        assertArrayEquals(expected, actual, msg);
    }

    // -----------------------
    // Read/Position tests
    // -----------------------

    @Test
    void readSingleByteAcrossAllChunkSizes() throws IOException {
        verifyReadAllBytes(new byte[] { 0 });
    }

    @Test
    void readAlphanumericStringAcrossAllChunkSizes() throws IOException {
        verifyReadAllBytes(ALPHANUMERIC.getBytes(UTF_8));
    }

    /**
     * "Setting the position to a value that is greater than the current size is legal but does not change the size of the entity.
     * A later attempt to read bytes at such a position will immediately return an end-of-file indication."
     */
    @Test
    void readingAfterPositionBeyondEndReturnsEOF() throws IOException {
        try (SeekableByteChannel c = emptyConcatenatedChannel()) {
            c.position(2);
            assertEquals(2, c.position());
            final ByteBuffer dst = ByteBuffer.allocate(5);
            assertEquals(-1, c.read(dst));
        }
    }

    @Test
    void concatenatingTwoEmptyChannelsBehavesLikeEmptyChannel() throws IOException {
        try (SeekableByteChannel channel = emptyConcatenatedChannel()) {
            assertEmptyChannelBehavior(channel);
        }
    }

    @Test
    void referenceBehaviorForEmptySingleChannel() throws IOException {
        assertEmptyChannelBehavior(newEmptyChannel());
    }

    // -----------------------
    // Exceptions / contract
    // -----------------------

    @Test
    void positionNegativeIsRejected() throws IOException {
        try (SeekableByteChannel s = emptyConcatenatedChannel()) {
            assertThrows(IllegalArgumentException.class, () -> s.position(-1));
        }
    }

    @Test
    void setPositionOnClosedChannelThrowsClosedChannelException() throws IOException {
        try (SeekableByteChannel c = emptyConcatenatedChannel()) {
            c.close();
            assertThrows(ClosedChannelException.class, () -> c.position(0));
        }
    }

    @Test
    void sizeOnClosedChannelThrowsClosedChannelException() throws IOException {
        try (SeekableByteChannel c = emptyConcatenatedChannel()) {
            c.close();
            assertThrows(ClosedChannelException.class, c::size);
        }
    }

    @Test
    void truncateIsNotSupported() throws IOException {
        try (SeekableByteChannel s = emptyConcatenatedChannel()) {
            assertThrows(NonWritableChannelException.class, () -> s.truncate(1));
        }
    }

    @Test
    void writeIsNotSupported() throws IOException {
        try (SeekableByteChannel s = emptyConcatenatedChannel()) {
            assertThrows(NonWritableChannelException.class, () -> s.write(ByteBuffer.allocate(10)));
        }
    }

    // -----------------------
    // Construction utilities
    // -----------------------

    @Test
    void constructorRejectsNullList() {
        assertThrows(NullPointerException.class, () -> new MultiReadOnlySeekableByteChannel(null));
    }

    @Test
    void forFilesRejectsNullArray() {
        assertThrows(NullPointerException.class, () -> MultiReadOnlySeekableByteChannel.forFiles((File[]) null));
    }

    @Test
    void forSeekableByteChannelsRejectsNullArray() {
        assertThrows(NullPointerException.class,
                () -> MultiReadOnlySeekableByteChannel.forSeekableByteChannels((SeekableByteChannel[]) null));
    }

    @Test
    void forSeekableByteChannelsReturnsSameInstanceWhenSingleElement() throws IOException {
        try (SeekableByteChannel single = newEmptyChannel();
             SeekableByteChannel returned = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(single)) {
            assertSame(single, returned);
        }
    }

    // -----------------------
    // Close behavior
    // -----------------------

    /**
     * "If the stream is already closed then invoking this method has no effect."
     */
    @Test
    void closingTwiceIsIdempotent() throws IOException {
        try (SeekableByteChannel c = emptyConcatenatedChannel()) {
            c.close();
            assertFalse(c.isOpen());
            c.close();
            assertFalse(c.isOpen());
        }
    }

    @Test
    void closeClosesAllChannelsEvenIfOneThrows() {
        final SeekableByteChannel[] parts = new ThrowingSeekableByteChannel[] {
                new ThrowingSeekableByteChannel(), new ThrowingSeekableByteChannel()
        };
        final SeekableByteChannel concatenated = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(parts);

        assertThrows(IOException.class, concatenated::close, "expected IOException from close()");
        assertFalse(parts[0].isOpen());
        assertFalse(parts[1].isOpen());
    }

    // -----------------------
    // Small utilities tests
    // -----------------------

    @Test
    void splitIntoChunksProducesExpectedGroups() {
        assertArrayEquals(
                new byte[][] { new byte[] { 1, 2, 3 }, new byte[] { 4, 5, 6 }, new byte[] { 7 } },
                splitIntoChunks(new byte[] { 1, 2, 3, 4, 5, 6, 7 }, 3));

        assertArrayEquals(
                new byte[][] { new byte[] { 1, 2, 3 }, new byte[] { 4, 5, 6 } },
                splitIntoChunks(new byte[] { 1, 2, 3, 4, 5, 6 }, 3));

        assertArrayEquals(
                new byte[][] { new byte[] { 1, 2, 3 }, new byte[] { 4, 5 } },
                splitIntoChunks(new byte[] { 1, 2, 3, 4, 5 }, 3));
    }

    // -----------------------
    // Spec compliance note
    // -----------------------

    /**
     * Spec says position() on a closed channel should throw ClosedChannelException.
     * This implementation deliberately violates the spec and returns the last position,
     * therefore this test is disabled.
     */
    @Test
    @Disabled("This implementation deliberately violates the spec for position() on closed channel")
    void readingPositionOnClosedChannelShouldThrowBySpec() throws Exception {
        try (SeekableByteChannel c = emptyConcatenatedChannel()) {
            c.close();
            c.position(); // should throw by spec
        }
    }
}