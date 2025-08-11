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
 * Tests {@link MultiReadOnlySeekableByteChannel}.
 * <p>
 * Originally based on TestMultiReadOnlySeekableByteChannel.scala by Tim Underwood.
 * </p>
 */
class MultiReadOnlySeekableByteChannelTest {

    // Simulates a channel that throws IOException on close
    private static final class ThrowingSeekableByteChannel implements SeekableByteChannel {
        private boolean closed;

        @Override
        public void close() throws IOException {
            closed = true;
            throw new IOException("Simulated close failure");
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
        public int read(final ByteBuffer dst) {
            return -1;
        }

        @Override
        public long size() {
            return 0;
        }

        @Override
        public SeekableByteChannel truncate(final long size) {
            return this;
        }

        @Override
        public int write(final ByteBuffer src) {
            return 0;
        }
    }

    // Helper method to split input into chunks
    private byte[][] splitIntoChunks(final byte[] input, final int chunkSize) {
        final List<byte[]> chunks = new ArrayList<>();
        int start = 0;
        while (start < input.length) {
            int end = Math.min(start + chunkSize, input.length);
            chunks.add(Arrays.copyOfRange(input, start, end));
            start = end;
        }
        return chunks.toArray(new byte[0][]);
    }

    // Creates channel from single byte array
    private SeekableByteChannel createSingleChannel(final byte[] data) {
        return new SeekableInMemoryByteChannel(data);
    }

    // Creates multi-channel from multiple byte arrays
    private SeekableByteChannel createMultiChannel(final byte[][] chunks) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[chunks.length];
        for (int i = 0; i < chunks.length; i++) {
            channels[i] = createSingleChannel(chunks[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    // Creates empty channel
    private SeekableByteChannel createEmptyChannel() {
        return createSingleChannel(ByteUtils.EMPTY_BYTE_ARRAY);
    }

    // Creates test channel with two empty sub-channels
    private SeekableByteChannel createTestChannel() {
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(
            createEmptyChannel(), createEmptyChannel());
    }

    // Verifies channel content matches expected byte array
    private void assertChannelContent(final byte[] expected, final SeekableByteChannel channel) 
            throws IOException {
        for (int channelSize = 1; channelSize <= expected.length; channelSize++) {
            // Test with different chunking strategies
            try (SeekableByteChannel multi = createMultiChannel(splitIntoChunks(expected, channelSize))) {
                verifyChannelBehavior(expected, multi);
            }
        }
    }

    // Verifies channel behavior with different read buffer sizes
    private void verifyChannelBehavior(final byte[] expected, final SeekableByteChannel channel) 
            throws IOException {
        for (int bufferSize = 1; bufferSize <= expected.length + 5; bufferSize++) {
            verifyReadOperations(expected, channel, bufferSize);
        }
    }

    // Performs actual read operations and verifies results
    private void verifyReadOperations(final byte[] expected, final SeekableByteChannel channel,
            final int bufferSize) throws IOException {
        assertTrue(channel.isOpen(), "Buffer size: " + bufferSize);
        assertEquals(expected.length, channel.size(), "Buffer size: " + bufferSize);
        channel.position(0);
        assertEquals(0, channel.position(), "Buffer size: " + bufferSize);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), "Buffer size: " + bufferSize);

        final ByteBuffer resultBuffer = ByteBuffer.allocate(expected.length + 100);
        final ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);

        int bytesRead;
        while ((bytesRead = channel.read(readBuffer)) != -1) {
            readBuffer.flip();
            resultBuffer.put(readBuffer);
            readBuffer.clear();
        }

        resultBuffer.flip();
        final byte[] actual = new byte[resultBuffer.remaining()];
        resultBuffer.get(actual);
        assertArrayEquals(expected, actual, "Buffer size: " + bufferSize);
    }

    // Verifies behavior of empty channel
    private void verifyEmptyChannelBehavior(SeekableByteChannel channel) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(10);

        // Verify initial state
        assertTrue(channel.isOpen());
        assertEquals(0, channel.size());
        assertEquals(0, channel.position());
        assertEquals(-1, channel.read(buffer));

        // Verify positioning beyond content
        channel.position(5);
        assertEquals(-1, channel.read(buffer));

        // Verify close behavior
        channel.close();
        assertFalse(channel.isOpen());
        
        // Verify operations on closed channel
        assertThrows(ClosedChannelException.class, () -> channel.read(buffer));
        assertThrows(ClosedChannelException.class, () -> channel.position(100));
    }

    // ===== CONTENT TESTS =====

    @Test
    void testEmptyChannel() throws IOException {
        verifyEmptyChannelBehavior(createEmptyChannel());
    }

    @Test
    void testSingleByteContent() throws IOException {
        assertChannelContent(new byte[]{0}, createSingleChannel(new byte[]{0}));
    }

    @Test
    void testAlphanumericStringContent() throws IOException {
        final byte[] data = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            .getBytes(UTF_8);
        assertChannelContent(data, createSingleChannel(data));
    }

    @Test
    void testTwoEmptyChannels() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(
                createEmptyChannel(), createEmptyChannel())) {
            verifyEmptyChannelBehavior(channel);
        }
    }

    // ===== CHANNEL OPERATION TESTS =====

    @Test
    void testPositionBeyondContentReturnsEOF() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.position(2);
            assertEquals(2, channel.position());
            assertEquals(-1, channel.read(ByteBuffer.allocate(5)));
        }
    }

    @Test
    void testPositionToNegativeValueThrows() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            assertThrows(IllegalArgumentException.class, () -> channel.position(-1));
        }
    }

    // ===== CHANNEL STATE TESTS =====

    @Test
    void testCloseIsIdempotent() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.close();
            assertFalse(channel.isOpen());
            channel.close();  // Second close should be no-op
            assertFalse(channel.isOpen());
        }
    }

    @Test
    void testOperationsOnClosedChannel() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.close();
            
            // All operations should fail on closed channel
            assertThrows(ClosedChannelException.class, () -> channel.position(0));
            assertThrows(ClosedChannelException.class, () -> channel.position());
            assertThrows(ClosedChannelException.class, () -> channel.size());
            
            final ByteBuffer buffer = ByteBuffer.allocate(1);
            assertThrows(ClosedChannelException.class, () -> channel.read(buffer));
        }
    }

    // ===== WRITE/TRUNCATE TESTS =====

    @Test
    void testWriteOperationThrows() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            assertThrows(NonWritableChannelException.class, 
                () -> channel.write(ByteBuffer.allocate(10)));
        }
    }

    @Test
    void testTruncateOperationThrows() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            assertThrows(NonWritableChannelException.class, 
                () -> channel.truncate(1));
        }
    }

    // ===== EDGE CASE TESTS =====

    @Test
    void testClosePropagatesExceptionsButClosesAll() {
        final ThrowingSeekableByteChannel[] channels = {
            new ThrowingSeekableByteChannel(), 
            new ThrowingSeekableByteChannel()
        };
        final SeekableByteChannel multiChannel = 
            MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
        
        // Close should throw but mark all channels as closed
        assertThrows(IOException.class, multiChannel::close);
        assertFalse(channels[0].isOpen());
        assertFalse(channels[1].isOpen());
    }

    @Test
    void testConstructorValidatesParameters() {
        // Null channel list
        assertThrows(NullPointerException.class, 
            () -> new MultiReadOnlySeekableByteChannel(null));
        
        // Null files array
        assertThrows(NullPointerException.class, 
            () -> MultiReadOnlySeekableByteChannel.forFiles((File[]) null));
        
        // Null channels array
        assertThrows(NullPointerException.class, 
            () -> MultiReadOnlySeekableByteChannel.forSeekableByteChannels((SeekableByteChannel[]) null));
    }

    @Test
    void testSingleChannelOptimization() throws Exception {
        try (SeekableByteChannel single = createEmptyChannel();
             SeekableByteChannel wrapped = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(single)) {
            // Should return original channel when wrapping single channel
            assertSame(single, wrapped);
        }
    }

    // ===== HELPER VALIDATION TESTS =====

    @Test
    void testChunkSplittingHelper() {
        // Verify chunk splitting logic
        byte[] input = {1, 2, 3, 4, 5, 6, 7};
        byte[][] expected = {{1, 2, 3}, {4, 5, 6}, {7}};
        assertArrayEquals(expected, splitIntoChunks(input, 3));
        
        input = new byte[]{1, 2, 3, 4, 5, 6};
        expected = new byte[][]{{1, 2, 3}, {4, 5, 6}};
        assertArrayEquals(expected, splitIntoChunks(input, 3));
        
        input = new byte[]{1, 2, 3, 4, 5};
        expected = new byte[][]{{1, 2, 3}, {4, 5}};
        assertArrayEquals(expected, splitIntoChunks(input, 3));
    }

    // ===== SPEC VIOLATION TEST (disabled) =====
    
    /**
     * Disabled because our implementation deliberately violates the spec by
     * allowing position() to be called on a closed channel.
     */
    @Test
    @Disabled("Deliberate spec violation")
    void testPositionReadOnClosedChannelThrows() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.close();
            assertThrows(ClosedChannelException.class, channel::position);
        }
    }
}