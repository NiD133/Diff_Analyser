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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 *
 * <p>Initially based on <a href=
 * "https://github.com/frugalmechanic/fm-common/blob/master/jvm/src/test/scala/fm/common/TestMultiReadOnlySeekableByteChannel.scala">
 * TestMultiReadOnlySeekableByteChannel.scala</a> by Tim Underwood.</p>
 */
class MultiReadOnlySeekableByteChannelTest {

    @Nested
    @DisplayName("Read Operations")
    class ReadOperations {

        @Test
        @DisplayName("Should read a single byte from a single-chunk channel")
        void canReadSingleByte() throws IOException {
            assertCanReadFullyWithVariousChunkAndBufferSizes(new byte[] { 0 });
        }

        @Test
        @DisplayName("Should read a long string from a multi-chunk channel")
        void canReadLongerContent() throws IOException {
            final byte[] content = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes(UTF_8);
            assertCanReadFullyWithVariousChunkAndBufferSizes(content);
        }

        @Test
        @DisplayName("Concatenating two empty channels should result in an empty channel")
        void concatenatingTwoEmptyChannelsResultsInEmptyChannel() throws IOException {
            try (SeekableByteChannel channel = createFromByteArrays(new byte[0], new byte[0])) {
                assertEmptyChannelBehavior(channel);
            }
        }

        @Test
        @DisplayName("A baseline SeekableInMemoryByteChannel with no content should behave as an empty channel")
        void emptyInMemoryChannelBehavesAsExpected() throws IOException {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                assertEmptyChannelBehavior(channel);
            }
        }

        @Test
        @DisplayName("Reading from a position after the end of the channel should return EOF (-1)")
        void readFromPositionAfterEndShouldReturnEOF() throws Exception {
            // Spec: "A later attempt to read bytes at such a position will immediately return an end-of-file indication"
            try (SeekableByteChannel channel = createChannelFromTwoEmptySources()) {
                channel.position(2);
                assertEquals(2, channel.position());
                final ByteBuffer readBuffer = ByteBuffer.allocate(5);
                assertEquals(-1, channel.read(readBuffer));
            }
        }
    }

    @Nested
    @DisplayName("Positioning Operations")
    class PositioningOperations {

        @Test
        @DisplayName("position(long) should throw IllegalArgumentException for negative positions")
        void positioningToNegativeValueThrowsException() throws IOException {
            // Spec for SeekableByteChannel#position(long): "IOException - If the new position is negative"
            // This implementation throws the more specific IllegalArgumentException.
            try (SeekableByteChannel channel = createChannelFromTwoEmptySources()) {
                assertThrows(IllegalArgumentException.class, () -> channel.position(-1));
            }
        }

        @Test
        @DisplayName("position(long) on a closed channel should throw ClosedChannelException")
        void positioningAClosedChannelThrowsException() throws Exception {
            // Spec: "ClosedChannelException - If this channel is closed"
            try (SeekableByteChannel channel = createChannelFromTwoEmptySources()) {
                channel.close();
                assertThrows(ClosedChannelException.class, () -> channel.position(0));
            }
        }
    }

    @Nested
    @DisplayName("State and Lifecycle")
    class StateAndLifecycle {

        @Test
        @DisplayName("close() should be idempotent")
        void closeIsIdempotent() throws Exception {
            // Spec for Closeable#close(): "If the stream is already closed then invoking this method has no effect."
            try (SeekableByteChannel channel = createChannelFromTwoEmptySources()) {
                channel.close();
                assertFalse(channel.isOpen());
                channel.close(); // Should not throw
                assertFalse(channel.isOpen());
            }
        }

        @Test
        @DisplayName("close() should close all underlying channels, even if one throws an exception")
        void closeShouldCloseAllUnderlyingChannelsEvenIfOneThrows() {
            final ThrowingSeekableByteChannel channel1 = new ThrowingSeekableByteChannel();
            final ThrowingSeekableByteChannel channel2 = new ThrowingSeekableByteChannel();
            final SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channel1, channel2);

            assertThrows(IOException.class, multiChannel::close, "Expected an IOException from the underlying channels");

            assertFalse(channel1.isOpen(), "First channel should be closed");
            assertFalse(channel2.isOpen(), "Second channel should be closed");
        }

        @Test
        @DisplayName("size() on a closed channel should throw ClosedChannelException")
        void sizeOnClosedChannelThrowsException() throws Exception {
            // Spec: "ClosedChannelException - If this channel is closed"
            try (SeekableByteChannel channel = createChannelFromTwoEmptySources()) {
                channel.close();
                assertThrows(ClosedChannelException.class, channel::size);
            }
        }

        @Test
        @Disabled("This behavior deliberately violates the SeekableByteChannel spec")
        @DisplayName("position() on a closed channel returns last position (Spec Violation)")
        void positionOnClosedChannelDoesNotThrow() throws Exception {
            // The spec for SeekableByteChannel#position() says it throws ClosedChannelException if the channel is closed.
            // This implementation deliberately violates this for performance, returning the last known position instead.
            try (SeekableByteChannel channel = createChannelFromTwoEmptySources()) {
                channel.position(10);
                channel.close();
                assertEquals(10, channel.position()); // Does not throw
            }
        }
    }

    @Nested
    @DisplayName("Unsupported Write Operations")
    class UnsupportedWriteOperations {

        @Test
        @DisplayName("truncate() should throw NonWritableChannelException")
        void truncateIsNotSupported() throws IOException {
            try (SeekableByteChannel channel = createChannelFromTwoEmptySources()) {
                assertThrows(NonWritableChannelException.class, () -> channel.truncate(1));
            }
        }

        @Test
        @DisplayName("write() should throw NonWritableChannelException")
        void writeIsNotSupported() throws IOException {
            try (SeekableByteChannel channel = createChannelFromTwoEmptySources()) {
                assertThrows(NonWritableChannelException.class, () -> channel.write(ByteBuffer.allocate(10)));
            }
        }
    }

    @Nested
    @DisplayName("Factory Methods and Constructor")
    class FactoryAndConstructor {

        @Test
        @DisplayName("Constructor should throw NullPointerException for null list")
        void constructorThrowsOnNull() {
            assertThrows(NullPointerException.class, () -> new MultiReadOnlySeekableByteChannel(null));
        }

        @Test
        @DisplayName("forFiles() should throw NullPointerException for null array")
        void forFilesThrowsOnNull() {
            assertThrows(NullPointerException.class, () -> MultiReadOnlySeekableByteChannel.forFiles((File[]) null));
        }

        @Test
        @DisplayName("forSeekableByteChannels() should throw NullPointerException for null array")
        void forSeekableByteChannelsThrowsOnNull() {
            assertThrows(NullPointerException.class, () -> MultiReadOnlySeekableByteChannel.forSeekableByteChannels((SeekableByteChannel[]) null));
        }

        @Test
        @DisplayName("forSeekableByteChannels() should return the original channel when given a single channel")
        void forSeekableByteChannelsReturnsIdentityForSingleChannel() throws IOException {
            try (SeekableByteChannel singleChannel = new SeekableInMemoryByteChannel()) {
                final SeekableByteChannel result = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(singleChannel);
                assertSame(singleChannel, result);
            }
        }
    }

    @Nested
    @DisplayName("Internal Helper Method Tests")
    class InternalHelperMethodTests {
        @Test
        @DisplayName("grouped() helper should correctly split byte arrays into chunks")
        void verifyGroupedHelper() {
            assertArrayEquals(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7 } },
                grouped(new byte[] { 1, 2, 3, 4, 5, 6, 7 }, 3));
            assertArrayEquals(new byte[][] { { 1, 2, 3 }, { 4, 5, 6 } },
                grouped(new byte[] { 1, 2, 3, 4, 5, 6 }, 3));
            assertArrayEquals(new byte[][] { { 1, 2, 3 }, { 4, 5 } },
                grouped(new byte[] { 1, 2, 3, 4, 5 }, 3));
        }
    }

    // --- Test Helper Methods and Classes ---

    /**
     * A mock channel that throws an IOException when closed, used for testing error handling.
     */
    private static final class ThrowingSeekableByteChannel implements SeekableByteChannel {
        private boolean closed;

        @Override
        public void close() throws IOException {
            closed = true;
            throw new IOException("mock close exception");
        }

        @Override
        public boolean isOpen() {
            return !closed;
        }

        @Override
        public long position() { return 0; }

        @Override
        public SeekableByteChannel position(final long newPosition) { return this; }

        @Override
        public int read(final ByteBuffer dst) { return -1; }

        @Override
        public long size() { return 0; }

        @Override
        public SeekableByteChannel truncate(final long size) { return this; }

        @Override
        public int write(final ByteBuffer src) { return 0; }
    }

    /**
     * Exhaustively tests that a channel's content matches the expected bytes. It does this by splitting the
     * expected content into multiple underlying channels of varying sizes, and then reading from the combined
     * channel with varying buffer sizes.
     *
     * @param expected The full byte content expected from the channel.
     */
    private void assertCanReadFullyWithVariousChunkAndBufferSizes(final byte[] expected) throws IOException {
        for (int channelChunkSize = 1; channelChunkSize <= expected.length; channelChunkSize++) {
            // Test with a standard SeekableInMemoryByteChannel as a baseline
            try (SeekableByteChannel single = new SeekableInMemoryByteChannel(expected)) {
                assertChannelContents(expected, single);
            }
            // Test with our MultiReadOnlySeekableByteChannel
            try (SeekableByteChannel multi = createFromByteArrays(grouped(expected, channelChunkSize))) {
                assertChannelContents(expected, multi);
            }
        }
    }

    /**
     * Verifies that reading from the given channel produces the expected byte array.
     * This check is performed exhaustively with various read buffer sizes to catch edge cases.
     *
     * @param expected the expected content
     * @param channel the channel to read from
     */
    private void assertChannelContents(final byte[] expected, final SeekableByteChannel channel) throws IOException {
        for (int readBufferSize = 1; readBufferSize <= expected.length + 5; readBufferSize++) {
            assertChannelContents(expected, channel, readBufferSize);
        }
    }

    /**
     * Verifies that reading from the given channel with a specific buffer size produces the expected byte array.
     *
     * @param expected the expected content
     * @param channel the channel to read from
     * @param readBufferSize the size of the buffer to use for each read operation
     */
    private void assertChannelContents(final byte[] expected, final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        final String context = "readBufferSize=" + readBufferSize;
        assertTrue(channel.isOpen(), context);
        assertEquals(expected.length, channel.size(), context);

        channel.position(0);
        assertEquals(0, channel.position(), context);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), context);

        final ByteBuffer resultBuffer = ByteBuffer.allocate(expected.length + 100);
        final ByteBuffer readBuf = ByteBuffer.allocate(readBufferSize);

        int bytesRead;
        while ((bytesRead = channel.read(readBuf)) != -1) {
            final int remaining = readBuf.remaining();
            readBuf.flip();
            resultBuffer.put(readBuf);
            readBuf.clear();

            if (resultBuffer.position() < expected.length) {
                assertEquals(0, remaining, "Buffer should be full on intermediate reads. " + context);
            }
            assertEquals(bytesRead, readBuf.position(), "Buffer position should match bytes read. " + context);
        }

        resultBuffer.flip();
        final byte[] actual = new byte[resultBuffer.remaining()];
        resultBuffer.get(actual);
        assertArrayEquals(expected, actual, context);
    }

    /**
     * Asserts that a channel correctly implements the contract for an empty channel.
     */
    private void assertEmptyChannelBehavior(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(10);

        assertTrue(channel.isOpen());
        assertEquals(0, channel.size());
        assertEquals(0, channel.position());
        assertEquals(-1, channel.read(buf), "Read on empty channel should return -1");

        channel.position(5);
        assertEquals(-1, channel.read(buf), "Read after positioning past end should return -1");

        channel.close();
        assertFalse(channel.isOpen());

        assertThrows(ClosedChannelException.class, () -> channel.read(buf), "read on closed channel");
        assertThrows(ClosedChannelException.class, () -> channel.position(100), "position on closed channel");
    }

    /**
     * Splits a byte array into a 2D array of smaller chunks.
     */
    private byte[][] grouped(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        for (int idx = 0; idx < input.length; idx += chunkSize) {
            final int end = Math.min(input.length, idx + chunkSize);
            groups.add(Arrays.copyOfRange(input, idx, end));
        }
        return groups.toArray(new byte[0][]);
    }

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from one or more byte arrays.
     */
    private SeekableByteChannel createFromByteArrays(final byte[]... arrays) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[arrays.length];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = new SeekableInMemoryByteChannel(arrays[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from two empty sources.
     */
    private SeekableByteChannel createChannelFromTwoEmptySources() {
        return createFromByteArrays(ByteUtils.EMPTY_BYTE_ARRAY, ByteUtils.EMPTY_BYTE_ARRAY);
    }
}