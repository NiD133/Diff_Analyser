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
 * Initially based on TestMultiReadOnlySeekableByteChannel.scala by Tim Underwood.
 */
class MultiReadOnlySeekableByteChannelTest {

    // Test constants
    private static final String TEST_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final byte[] SINGLE_BYTE_DATA = new byte[] { 0 };
    private static final int MAX_BUFFER_SIZE_OFFSET = 5; // Extra buffer size for testing edge cases

    /**
     * Mock channel that throws IOException on close() to test error handling.
     */
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
        public int read(final ByteBuffer dst) throws IOException {
            return -1; // EOF
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

    // Factory methods for creating test channels
    
    private SeekableByteChannel createEmptyChannel() {
        return createSingleChannel(ByteUtils.EMPTY_BYTE_ARRAY);
    }

    private SeekableByteChannel createSingleChannel(final byte[] data) {
        return new SeekableInMemoryByteChannel(data);
    }

    private SeekableByteChannel createMultiChannel(final byte[][] dataChunks) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[dataChunks.length];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = createSingleChannel(dataChunks[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    private SeekableByteChannel createTestChannel() {
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(createEmptyChannel(), createEmptyChannel());
    }

    // Utility methods

    /**
     * Splits input data into chunks of specified size.
     * The last chunk may be smaller if input length is not divisible by chunkSize.
     */
    private byte[][] splitIntoChunks(final byte[] input, final int chunkSize) {
        final List<byte[]> chunks = new ArrayList<>();
        int startIndex = 0;
        
        while (startIndex < input.length) {
            int endIndex = Math.min(startIndex + chunkSize, input.length);
            chunks.add(Arrays.copyOfRange(input, startIndex, endIndex));
            startIndex = endIndex;
        }
        
        return chunks.toArray(new byte[0][]);
    }

    // Core test verification methods

    /**
     * Verifies that the multi-channel behaves identically to a single channel
     * by testing various chunk sizes and read buffer sizes.
     */
    private void verifyChannelBehaviorWithAllChunkSizes(final byte[] expectedData) throws IOException {
        for (int chunkSize = 1; chunkSize <= expectedData.length; chunkSize++) {
            // Test single channel as reference
            try (SeekableByteChannel singleChannel = createSingleChannel(expectedData)) {
                verifyChannelBehaviorWithAllBufferSizes(expectedData, singleChannel);
            }
            
            // Test multi-channel with current chunk size
            byte[][] dataChunks = splitIntoChunks(expectedData, chunkSize);
            try (SeekableByteChannel multiChannel = createMultiChannel(dataChunks)) {
                verifyChannelBehaviorWithAllBufferSizes(expectedData, multiChannel);
            }
        }
    }

    /**
     * Tests channel behavior with various read buffer sizes.
     */
    private void verifyChannelBehaviorWithAllBufferSizes(final byte[] expectedData, final SeekableByteChannel channel) throws IOException {
        int maxBufferSize = expectedData.length + MAX_BUFFER_SIZE_OFFSET;
        for (int bufferSize = 1; bufferSize <= maxBufferSize; bufferSize++) {
            verifyChannelReadBehavior(expectedData, channel, bufferSize);
        }
    }

    /**
     * Verifies complete channel read behavior with a specific buffer size.
     */
    private void verifyChannelReadBehavior(final byte[] expectedData, final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        String testContext = "bufferSize=" + readBufferSize;
        
        // Verify initial state
        assertTrue(channel.isOpen(), testContext);
        assertEquals(expectedData.length, channel.size(), testContext);
        
        // Reset to beginning
        channel.position(0);
        assertEquals(0, channel.position(), testContext);
        
        // Test reading with zero-size buffer
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), testContext);

        // Read all data and verify
        byte[] actualData = readAllDataFromChannel(channel, readBufferSize, expectedData.length);
        assertArrayEquals(expectedData, actualData, testContext);
    }

    /**
     * Reads all data from channel using specified buffer size and validates read behavior.
     */
    private byte[] readAllDataFromChannel(final SeekableByteChannel channel, final int bufferSize, final int expectedDataLength) throws IOException {
        final ByteBuffer resultBuffer = ByteBuffer.allocate(expectedDataLength + 100);
        final ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
        
        int bytesRead = channel.read(readBuffer);
        
        while (bytesRead != -1) {
            int remainingBeforeFlip = readBuffer.remaining();
            
            // Transfer read data to result buffer
            readBuffer.flip();
            resultBuffer.put(readBuffer);
            readBuffer.clear();
            
            // Validate buffer state
            validateReadBufferState(readBuffer, bytesRead, resultBuffer.position(), expectedDataLength, remainingBeforeFlip);
            
            bytesRead = channel.read(readBuffer);
        }

        // Extract final result
        resultBuffer.flip();
        final byte[] result = new byte[resultBuffer.remaining()];
        resultBuffer.get(result);
        return result;
    }

    /**
     * Validates the state of the read buffer after a read operation.
     */
    private void validateReadBufferState(final ByteBuffer readBuffer, final int lastBytesRead, 
                                       final int totalBytesRead, final int expectedDataLength, 
                                       final int remainingBeforeRead) {
        String context = "bufferSize=" + readBuffer.capacity();
        
        // For non-final reads, buffer should be completely filled
        if (totalBytesRead < expectedDataLength) {
            assertEquals(0, remainingBeforeRead, "Buffer should be full for non-final reads: " + context);
        }

        // After EOF, buffer position should be 0 (since we cleared it)
        if (lastBytesRead == -1) {
            assertEquals(0, readBuffer.position(), "Buffer position after EOF: " + context);
        } else {
            assertEquals(lastBytesRead, readBuffer.position(), "Buffer position after read: " + context);
        }
    }

    /**
     * Verifies behavior of an empty channel.
     */
    private void verifyEmptyChannelBehavior(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(10);

        // Test initial state
        assertTrue(channel.isOpen());
        assertEquals(0, channel.size());
        assertEquals(0, channel.position());
        assertEquals(-1, channel.read(buffer));

        // Test reading from position beyond end
        channel.position(5);
        assertEquals(-1, channel.read(buffer));

        // Test closed state
        channel.close();
        assertFalse(channel.isOpen());

        // Verify exceptions on closed channel
        assertThrows(ClosedChannelException.class, () -> channel.read(buffer), 
                    "Should throw ClosedChannelException when reading from closed channel");
        assertThrows(ClosedChannelException.class, () -> channel.position(100), 
                    "Should throw ClosedChannelException when setting position on closed channel");
    }

    // Constructor and factory method tests

    @Test
    void testConstructor_WithNullChannels_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MultiReadOnlySeekableByteChannel(null));
    }

    @Test
    void testForFiles_WithNullArgument_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> MultiReadOnlySeekableByteChannel.forFiles((File[]) null));
    }

    @Test
    void testForSeekableByteChannels_WithNullArgument_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> MultiReadOnlySeekableByteChannel.forSeekableByteChannels((SeekableByteChannel[]) null));
    }

    @Test
    void testForSeekableByteChannels_WithSingleChannel_ReturnsIdenticalInstance() throws IOException {
        try (SeekableByteChannel originalChannel = createEmptyChannel();
             SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(originalChannel)) {
            assertSame(originalChannel, multiChannel);
        }
    }

    // Read-only behavior tests

    @Test
    void testWrite_ThrowsNonWritableChannelException() throws IOException {
        try (SeekableByteChannel channel = createTestChannel()) {
            assertThrows(NonWritableChannelException.class, () -> channel.write(ByteBuffer.allocate(10)));
        }
    }

    @Test
    void testTruncate_ThrowsNonWritableChannelException() throws IOException {
        try (SeekableByteChannel channel = createTestChannel()) {
            assertThrows(NonWritableChannelException.class, () -> channel.truncate(1));
        }
    }

    // Position handling tests

    @Test
    void testPosition_WithNegativeValue_ThrowsIllegalArgumentException() throws IOException {
        try (SeekableByteChannel channel = createTestChannel()) {
            assertThrows(IllegalArgumentException.class, () -> channel.position(-1));
        }
    }

    @Test
    void testPosition_BeyondEndOfChannel_AllowsPositioning() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.position(2);
            assertEquals(2, channel.position());
            
            // Reading from position beyond end should return EOF
            final ByteBuffer buffer = ByteBuffer.allocate(5);
            assertEquals(-1, channel.read(buffer));
        }
    }

    // Closed channel behavior tests

    @Test
    void testClosedChannel_Position_ThrowsClosedChannelException() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.close();
            assertThrows(ClosedChannelException.class, () -> channel.position(0));
        }
    }

    @Test
    void testClosedChannel_Size_ThrowsClosedChannelException() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.close();
            assertThrows(ClosedChannelException.class, () -> channel.size());
        }
    }

    @Test
    void testClose_IsIdempotent() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.close();
            assertFalse(channel.isOpen());
            
            // Second close should have no effect
            channel.close();
            assertFalse(channel.isOpen());
        }
    }

    @Test
    void testClose_WithThrowingChannels_ClosesAllAndPropagatesException() {
        final SeekableByteChannel[] throwingChannels = {
            new ThrowingSeekableByteChannel(), 
            new ThrowingSeekableByteChannel()
        };
        
        final SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(throwingChannels);
        
        assertThrows(IOException.class, multiChannel::close, "Should propagate IOException from close()");
        
        // Verify all channels were closed despite exceptions
        assertFalse(throwingChannels[0].isOpen());
        assertFalse(throwingChannels[1].isOpen());
    }

    // Data reading tests

    @Test
    void testReadBehavior_WithSingleByte() throws IOException {
        verifyChannelBehaviorWithAllChunkSizes(SINGLE_BYTE_DATA);
    }

    @Test
    void testReadBehavior_WithLongString() throws IOException {
        verifyChannelBehaviorWithAllChunkSizes(TEST_STRING.getBytes(UTF_8));
    }

    @Test
    void testEmptyChannel_Behavior() throws IOException {
        verifyEmptyChannelBehavior(createEmptyChannel());
    }

    @Test
    void testTwoEmptyChannels_BehaveLikeOneEmptyChannel() throws IOException {
        try (SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(
                createEmptyChannel(), createEmptyChannel())) {
            verifyEmptyChannelBehavior(multiChannel);
        }
    }

    // Utility method tests

    @Test
    void testSplitIntoChunks_VariousScenarios() {
        // Test with exact division
        assertArrayEquals(
            new byte[][] { {1, 2, 3}, {4, 5, 6} }, 
            splitIntoChunks(new byte[] {1, 2, 3, 4, 5, 6}, 3)
        );
        
        // Test with remainder
        assertArrayEquals(
            new byte[][] { {1, 2, 3}, {4, 5, 6}, {7} }, 
            splitIntoChunks(new byte[] {1, 2, 3, 4, 5, 6, 7}, 3)
        );
        
        // Test with smaller remainder
        assertArrayEquals(
            new byte[][] { {1, 2, 3}, {4, 5} }, 
            splitIntoChunks(new byte[] {1, 2, 3, 4, 5}, 3)
        );
    }

    // Disabled test (as in original)

    /**
     * This test is disabled because the implementation deliberately violates 
     * the SeekableByteChannel contract for position() on closed channels.
     */
    @Test
    @Disabled("Implementation deliberately violates the spec")
    public void testClosedChannel_GetPosition_ShouldThrowClosedChannelException() throws Exception {
        try (SeekableByteChannel channel = createTestChannel()) {
            channel.close();
            // This should throw ClosedChannelException according to spec, but implementation allows it
            channel.position();
        }
    }
}