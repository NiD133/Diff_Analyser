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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 *
 * <p>This test suite is organized into nested classes, each focusing on a specific
 * aspect or method of the SeekableInMemoryByteChannel API for better readability
 * and maintainability.</p>
 */
class SeekableInMemoryByteChannelTest {

    private final byte[] testData = "Some data".getBytes(UTF_8);

    @Nested
    @DisplayName("Channel Closing and State")
    class CloseAndStateTests {

        @Test
        @DisplayName("close() should be idempotent")
        void shouldBeIdempotent() throws IOException {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                channel.close();
                assertFalse(channel.isOpen());
                channel.close(); // second close should have no effect
                assertFalse(channel.isOpen());
            }
        }

        @Test
        @DisplayName("Reading from a closed channel should throw ClosedChannelException")
        void shouldThrowExceptionOnReadingClosedChannel() throws IOException {
            final SeekableByteChannel channel = new SeekableInMemoryByteChannel();
            channel.close();
            assertThrows(ClosedChannelException.class, () -> channel.read(ByteBuffer.allocate(1)));
        }

        @Test
        @DisplayName("Writing to a closed channel should throw ClosedChannelException")
        void shouldThrowExceptionOnWritingToClosedChannel() throws IOException {
            final SeekableByteChannel channel = new SeekableInMemoryByteChannel();
            channel.close();
            assertThrows(ClosedChannelException.class, () -> channel.write(ByteBuffer.allocate(1)));
        }

        @Test
        @DisplayName("Setting position on a closed channel should throw ClosedChannelException")
        void shouldThrowExceptionOnSettingPositionOnClosedChannel() throws IOException {
            final SeekableByteChannel channel = new SeekableInMemoryByteChannel();
            channel.close();
            assertThrows(ClosedChannelException.class, () -> channel.position(0));
        }

        @Test
        @Disabled("Deliberately violates the spec: truncate() on a closed channel should throw an exception")
        @DisplayName("Truncating a closed channel should throw ClosedChannelException (Spec Violation)")
        void shouldThrowExceptionOnTruncatingClosedChannel() throws IOException {
            final SeekableByteChannel channel = new SeekableInMemoryByteChannel();
            channel.close();
            assertThrows(ClosedChannelException.class, () -> channel.truncate(0));
        }

        @Test
        @Disabled("Deliberately violates the spec: position() on a closed channel should throw an exception")
        @DisplayName("Getting position on a closed channel should throw ClosedChannelException (Spec Violation)")
        void shouldThrowExceptionOnGettingPositionOnClosedChannel() throws IOException {
            final SeekableByteChannel channel = new SeekableInMemoryByteChannel();
            channel.close();
            assertThrows(ClosedChannelException.class, channel::position);
        }

        @Test
        @Disabled("Deliberately violates the spec: size() on a closed channel should throw an exception")
        @DisplayName("Getting size on a closed channel should throw ClosedChannelException (Spec Violation)")
        void shouldThrowExceptionOnGettingSizeOnClosedChannel() throws IOException {
            final SeekableByteChannel channel = new SeekableInMemoryByteChannel();
            channel.close();
            assertThrows(ClosedChannelException.class, channel::size);
        }
    }

    @Nested
    @DisplayName("Read Operations")
    class ReadTests {

        @Test
        @DisplayName("should read all contents into a perfectly sized buffer")
        void shouldReadContentsIntoMatchingBuffer() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
                final ByteBuffer readBuffer = ByteBuffer.allocate(testData.length);
                final int readCount = channel.read(readBuffer);

                assertEquals(testData.length, readCount);
                assertArrayEquals(testData, readBuffer.array());
                assertEquals(testData.length, channel.position());
            }
        }

        @Test
        @DisplayName("should read all contents into a buffer larger than the data")
        void shouldReadContentsIntoLargerBuffer() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
                final ByteBuffer readBuffer = ByteBuffer.allocate(testData.length + 1);
                final int readCount = channel.read(readBuffer);

                assertEquals(testData.length, readCount);
                final byte[] actualData = Arrays.copyOf(readBuffer.array(), testData.length);
                assertArrayEquals(testData, actualData);
                assertEquals(testData.length, channel.position());
            }
        }

        @Test
        @DisplayName("should read data from a specified position")
        void shouldReadFromSpecifiedPosition() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
                channel.position(5L);
                final ByteBuffer readBuffer = ByteBuffer.allocate(4);
                final int readCount = channel.read(readBuffer);

                assertEquals(4, readCount);
                assertEquals("data", new String(readBuffer.array(), UTF_8));
                assertEquals(testData.length, channel.position());
            }
        }

        @Test
        @DisplayName("should return -1 (EOF) when reading from a position after the end")
        void shouldReturnEofWhenReadingFromPositionAfterEnd() throws IOException {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) { // Empty channel
                channel.position(2);
                final ByteBuffer readBuffer = ByteBuffer.allocate(5);
                assertEquals(-1, channel.read(readBuffer));
            }
        }

        @Test
        @DisplayName("should return -1 (EOF) when reading at the end of the channel")
        void shouldReturnEofWhenReadingAtEnd() throws IOException {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
                channel.position(testData.length);
                final ByteBuffer readBuffer = ByteBuffer.allocate(1);
                assertEquals(-1, channel.read(readBuffer));
            }
        }
    }

    @Nested
    @DisplayName("Write Operations")
    class WriteTests {

        @Test
        @DisplayName("should write data to an empty channel and advance position")
        void shouldWriteDataAndAdvancePosition() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
                final ByteBuffer inData = ByteBuffer.wrap(testData);
                final int writeCount = channel.write(inData);

                assertEquals(testData.length, writeCount);
                assertEquals(testData.length, channel.size());
                assertEquals(testData.length, channel.position());
                assertArrayEquals(testData, Arrays.copyOf(channel.array(), (int) channel.size()));
            }
        }

        @Test
        @DisplayName("should overwrite existing data from a specified position")
        void shouldOverwriteDataFromSpecifiedPosition() throws IOException {
            final byte[] initialData = "Initial data".getBytes(UTF_8);
            final byte[] dataToWrite = "new".getBytes(UTF_8);
            final byte[] expectedData = "Initialnewta".getBytes(UTF_8);

            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(initialData)) {
                channel.position(8L);
                final int writeCount = channel.write(ByteBuffer.wrap(dataToWrite));

                assertEquals(dataToWrite.length, writeCount);
                assertEquals(initialData.length, channel.size());
                assertEquals(8L + dataToWrite.length, channel.position());
                assertArrayEquals(expectedData, channel.array());
            }
        }

        @Test
        @DisplayName("should grow the channel when writing past the end")
        void shouldGrowChannelWhenWritingPastEnd() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
                final long newPosition = testData.length + 2;
                final byte[] dataToWrite = "extra".getBytes(UTF_8);

                channel.position(newPosition);
                final int writeCount = channel.write(ByteBuffer.wrap(dataToWrite));

                final long expectedSize = newPosition + writeCount;
                assertEquals(dataToWrite.length, writeCount);
                assertEquals(expectedSize, channel.size());
                assertEquals(expectedSize, channel.position());

                // Verify the written data
                final byte[] writtenData = new byte[dataToWrite.length];
                channel.position(newPosition);
                channel.read(ByteBuffer.wrap(writtenData));
                assertArrayEquals(dataToWrite, writtenData);
            }
        }
    }

    @Nested
    @DisplayName("Position Operations")
    class PositionTests {

        @Test
        @DisplayName("should set and return the correct position")
        void shouldSetAndReturnCorrectPosition() throws IOException {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
                assertEquals(4L, channel.position(4L).position());
                assertEquals(testData.length, channel.position(testData.length).position());
                assertEquals(testData.length + 1L, channel.position(testData.length + 1L).position());
            }
        }

        @Test
        @DisplayName("should throw IOException when setting a negative position")
        void shouldThrowExceptionForNegativePosition() throws IOException {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                assertThrows(IOException.class, () -> channel.position(-1));
            }
        }

        @Test
        @DisplayName("should throw IOException when setting a position larger than Integer.MAX_VALUE")
        void shouldThrowExceptionForPositionBeyondMaxInt() {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                assertThrows(IOException.class, () -> channel.position((long) Integer.MAX_VALUE + 1L));
            }
        }
    }

    @Nested
    @DisplayName("Truncate Operations")
    class TruncateTests {

        @Test
        @DisplayName("should shorten the channel and its contents")
        void shouldShortenChannelAndContent() {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
                channel.truncate(4);

                assertEquals(4, channel.size());
                final byte[] truncatedBytes = Arrays.copyOf(channel.array(), (int) channel.size());
                assertEquals("Some", new String(truncatedBytes, UTF_8));
            }
        }

        @Test
        @DisplayName("should move position to the new size if it was beyond it")
        void shouldAdjustPositionIfItWasBeyondNewSize() throws IOException {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
                channel.position(testData.length); // Position at the end
                channel.truncate(4L);

                assertEquals(4L, channel.size());
                assertEquals(4L, channel.position());
            }
        }

        @Test
        @DisplayName("should not change position if it is within the new size")
        void shouldNotChangePositionIfItIsWithinNewSize() throws IOException {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
                channel.position(1);
                channel.truncate(testData.length - 1);

                assertEquals(testData.length - 1, channel.size());
                assertEquals(1, channel.position());
            }
        }

        @ParameterizedTest(name = "when truncating to size {0} bytes larger than current")
        @ValueSource(longs = {0, 1})
        @DisplayName("should not change channel when truncating to current or larger size")
        void shouldNotChangeChannelWhenTruncatingToSizeNotSmallerThanCurrent(long sizeDelta) throws IOException {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(testData)) {
                final long originalSize = channel.size();
                channel.truncate(originalSize + sizeDelta);

                assertEquals(originalSize, channel.size());
                final ByteBuffer readBuffer = ByteBuffer.allocate(testData.length);
                channel.read(readBuffer);
                assertArrayEquals(testData, readBuffer.array());
            }
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when truncating to a negative size")
        void shouldThrowExceptionWhenTruncatingToNegativeSize() {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                assertThrows(IllegalArgumentException.class, () -> channel.truncate(-1));
            }
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when truncating to a size larger than Integer.MAX_VALUE")
        void shouldThrowExceptionWhenTruncatingBeyondMaxInt() {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                assertThrows(IllegalArgumentException.class, () -> channel.truncate((long) Integer.MAX_VALUE + 1L));
            }
        }
    }
}