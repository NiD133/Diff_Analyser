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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

class SeekableInMemoryByteChannelTest {

    private static final byte[] TEST_DATA = "Some data".getBytes(UTF_8);
    private static final String EXPECTED_PARTIAL_DATA = "data";
    private static final String EXPECTED_TRUNCATED_DATA = "Some";
    private static final int EOF = -1;

    @Nested
    class ChannelLifecycleTests {

        @Test
        void closeIsIdempotent() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                channel.close();
                assertFalse(channel.isOpen());
                
                // Second close should have no effect
                channel.close();
                assertFalse(channel.isOpen());
            }
        }

        @Test
        void readFromClosedChannelThrowsException() {
            SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
            channel.close();
            
            ByteBuffer buffer = ByteBuffer.allocate(1);
            assertThrows(ClosedChannelException.class, () -> channel.read(buffer));
        }

        @Test
        void writeToClosedChannelThrowsException() {
            SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
            channel.close();
            
            ByteBuffer buffer = ByteBuffer.allocate(1);
            assertThrows(ClosedChannelException.class, () -> channel.write(buffer));
        }

        @Test
        void setPositionOnClosedChannelThrowsException() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                channel.close();
                assertThrows(ClosedChannelException.class, () -> channel.position(0));
            }
        }
    }

    @Nested
    class ReadOperationTests {

        @Test
        void readAllDataWithExactSizeBuffer() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                ByteBuffer buffer = ByteBuffer.allocate(TEST_DATA.length);
                
                int bytesRead = channel.read(buffer);
                
                assertEquals(TEST_DATA.length, bytesRead);
                assertArrayEquals(TEST_DATA, buffer.array());
                assertEquals(TEST_DATA.length, channel.position());
            }
        }

        @Test
        void readAllDataWithOversizedBuffer() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                ByteBuffer buffer = ByteBuffer.allocate(TEST_DATA.length + 1);
                
                int bytesRead = channel.read(buffer);
                
                assertEquals(TEST_DATA.length, bytesRead);
                byte[] actualData = Arrays.copyOf(buffer.array(), TEST_DATA.length);
                assertArrayEquals(TEST_DATA, actualData);
                assertEquals(TEST_DATA.length, channel.position());
            }
        }

        @Test
        void readFromSpecificPosition() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                ByteBuffer buffer = ByteBuffer.allocate(4);
                
                channel.position(5L);
                int bytesRead = channel.read(buffer);
                
                assertEquals(4L, bytesRead);
                assertEquals(EXPECTED_PARTIAL_DATA, new String(buffer.array(), UTF_8));
                assertEquals(TEST_DATA.length, channel.position());
            }
        }

        @Test
        void readFromPositionBeyondEndReturnsEOF() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                channel.position(2);
                assertEquals(2, channel.position());
                
                ByteBuffer buffer = ByteBuffer.allocate(5);
                int bytesRead = channel.read(buffer);
                
                assertEquals(EOF, bytesRead);
            }
        }

        @Test
        void readFromEndOfChannelReturnsEOF() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                ByteBuffer buffer = ByteBuffer.allocate(TEST_DATA.length);
                
                channel.position(TEST_DATA.length + 1);
                int bytesRead = channel.read(buffer);
                
                assertEquals(0L, buffer.position());
                assertEquals(EOF, bytesRead);
                assertEquals(EOF, channel.read(buffer)); // Subsequent reads should also return EOF
            }
        }
    }

    @Nested
    class WriteOperationTests {

        @Test
        void writeDataToEmptyChannel() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
                ByteBuffer inputBuffer = ByteBuffer.wrap(TEST_DATA);
                
                int bytesWritten = channel.write(inputBuffer);
                
                assertEquals(TEST_DATA.length, bytesWritten);
                byte[] actualData = Arrays.copyOf(channel.array(), (int) channel.size());
                assertArrayEquals(TEST_DATA, actualData);
                assertEquals(TEST_DATA.length, channel.position());
            }
        }

        @Test
        void writeDataAtSpecificPosition() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                ByteBuffer inputBuffer = ByteBuffer.wrap(TEST_DATA);
                int writePosition = 5;
                
                channel.position(writePosition);
                int bytesWritten = channel.write(inputBuffer);
                
                assertEquals(TEST_DATA.length, bytesWritten);
                
                // Expected data: first 5 bytes of original + all of TEST_DATA
                ByteBuffer expectedBuffer = ByteBuffer.allocate(TEST_DATA.length + writePosition)
                    .put(TEST_DATA, 0, writePosition)
                    .put(TEST_DATA);
                assertArrayEquals(expectedBuffer.array(), Arrays.copyOf(channel.array(), (int) channel.size()));
                assertEquals(TEST_DATA.length + writePosition, channel.position());
            }
        }

        @Test
        void writeToPositionBeyondEndGrowsChannel() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                int writePosition = 2;
                channel.position(writePosition);
                assertEquals(writePosition, channel.position());
                
                ByteBuffer inputBuffer = ByteBuffer.wrap(TEST_DATA);
                int bytesWritten = channel.write(inputBuffer);
                
                assertEquals(TEST_DATA.length, bytesWritten);
                assertEquals(TEST_DATA.length + writePosition, channel.size());

                // Verify written data can be read back correctly
                channel.position(writePosition);
                ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
                channel.read(readBuffer);
                assertArrayEquals(TEST_DATA, Arrays.copyOf(readBuffer.array(), TEST_DATA.length));
            }
        }
    }

    @Nested
    class PositionManagementTests {

        @Test
        void setValidPositions() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                long positionAtFour = channel.position(4L).position();
                long positionAtEnd = channel.position(TEST_DATA.length).position();
                long positionBeyondEnd = channel.position(TEST_DATA.length + 1L).position();
                
                assertEquals(4L, positionAtFour);
                assertEquals(channel.size(), positionAtEnd);
                assertEquals(TEST_DATA.length + 1L, positionBeyondEnd);
            }
        }

        @Test
        void setNegativePositionThrowsException() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                assertThrows(IOException.class, () -> channel.position(-1));
            }
        }

        @Test
        void setPositionBeyondMaxIntegerThrowsException() {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
                long invalidPosition = Integer.MAX_VALUE + 1L;
                assertThrows(IOException.class, () -> channel.position(invalidPosition));
            }
        }
    }

    @Nested
    class TruncateOperationTests {

        @Test
        void truncateToSmallerSize() {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                int truncateSize = 4;
                
                channel.truncate(truncateSize);
                
                byte[] actualData = Arrays.copyOf(channel.array(), (int) channel.size());
                assertEquals(EXPECTED_TRUNCATED_DATA, new String(actualData, UTF_8));
            }
        }

        @Test
        void truncateAdjustsPositionWhenNecessary() throws IOException {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                int truncateSize = 4;
                
                channel.position(TEST_DATA.length);
                channel.truncate(truncateSize);
                
                assertEquals(truncateSize, channel.position());
                assertEquals(truncateSize, channel.size());
            }
        }

        @Test
        void truncatePreservesPositionWhenSmaller() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                int smallPosition = 1;
                int truncateSize = TEST_DATA.length - 1;
                
                channel.position(smallPosition);
                channel.truncate(truncateSize);
                
                assertEquals(truncateSize, channel.size());
                assertEquals(smallPosition, channel.position());
            }
        }

        @Test
        void truncateToCurrentSizeDoesNothing() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                assertEquals(TEST_DATA.length, channel.size());
                
                channel.truncate(TEST_DATA.length);
                
                assertEquals(TEST_DATA.length, channel.size());
                verifyChannelContainsOriginalData(channel);
            }
        }

        @Test
        void truncateToLargerSizeDoesNothing() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                assertEquals(TEST_DATA.length, channel.size());
                
                channel.truncate(TEST_DATA.length + 1);
                
                assertEquals(TEST_DATA.length, channel.size());
                verifyChannelContainsOriginalData(channel);
            }
        }

        @Test
        void truncateWithPositionBeyondNewSize() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                int newSize = 3;
                
                channel.position(4);
                channel.truncate(newSize);
                
                assertEquals(newSize, channel.size());
                assertEquals(newSize, channel.position());
            }
        }

        @Test
        void truncateWithLargePositionAndNewSize() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                int largePosition = 2 * TEST_DATA.length;
                int newSize = TEST_DATA.length + 1;
                
                channel.position(largePosition);
                channel.truncate(newSize);
                
                assertEquals(TEST_DATA.length, channel.size());
                assertEquals(newSize, channel.position());
            }
        }

        @Test
        void truncateWithLargePositionToCurrentSize() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
                int largePosition = 2 * TEST_DATA.length;
                
                channel.position(largePosition);
                channel.truncate(TEST_DATA.length);
                
                assertEquals(TEST_DATA.length, channel.size());
                assertEquals(TEST_DATA.length, channel.position());
            }
        }

        @Test
        void truncateToNegativeSizeThrowsException() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                assertThrows(IllegalArgumentException.class, () -> channel.truncate(-1));
            }
        }

        @Test
        void truncateBeyondMaxIntegerThrowsException() {
            try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
                long invalidSize = Integer.MAX_VALUE + 1L;
                assertThrows(IllegalArgumentException.class, () -> channel.truncate(invalidSize));
            }
        }

        private void verifyChannelContainsOriginalData(SeekableByteChannel channel) throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(TEST_DATA.length);
            assertEquals(TEST_DATA.length, channel.read(buffer));
            assertArrayEquals(TEST_DATA, Arrays.copyOf(buffer.array(), TEST_DATA.length));
        }
    }

    @Nested
    @Disabled("Tests for spec violations that are deliberately allowed")
    class SpecViolationTests {

        @Test
        void readPositionFromClosedChannelShouldThrowException() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                channel.close();
                // This should throw ClosedChannelException but implementation allows it
                channel.position();
            }
        }

        @Test
        void readSizeFromClosedChannelShouldThrowException() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                channel.close();
                // This should throw ClosedChannelException but implementation allows it
                channel.size();
            }
        }

        @Test
        void truncateClosedChannelShouldThrowException() throws Exception {
            try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
                channel.close();
                // This should throw ClosedChannelException but implementation allows it
                channel.truncate(0);
            }
        }
    }
}