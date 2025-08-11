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

class SeekableInMemoryByteChannelTest {

    private static final byte[] TEST_DATA = "Some data".getBytes(UTF_8);

    @Test
    void closeIsIdempotent() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.close();
            assertFalse(channel.isOpen());
            channel.close();
            assertFalse(channel.isOpen());
        }
    }

    @Test
    void readReturnsEofWhenReadingAfterEnd() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Arrange
            channel.position(2);
            
            // Act
            ByteBuffer readBuffer = ByteBuffer.allocate(5);
            int bytesRead = channel.read(readBuffer);
            
            // Assert
            assertEquals(-1, bytesRead);
        }
    }

    @Test
    void readFullDataWhenBufferSizeMatches() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Arrange
            ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
            
            // Act
            int bytesRead = channel.read(readBuffer);
            
            // Assert
            assertEquals(TEST_DATA.length, bytesRead);
            assertArrayEquals(TEST_DATA, readBuffer.array());
            assertEquals(TEST_DATA.length, channel.position());
        }
    }

    @Test
    void readFullDataWhenBufferIsLarger() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Arrange
            ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length + 1);
            
            // Act
            int bytesRead = channel.read(readBuffer);
            
            // Assert
            assertEquals(TEST_DATA.length, bytesRead);
            assertArrayEquals(TEST_DATA, Arrays.copyOf(readBuffer.array(), TEST_DATA.length));
            assertEquals(TEST_DATA.length, channel.position());
        }
    }

    @Test
    void readFromSetPosition() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Arrange
            ByteBuffer readBuffer = ByteBuffer.allocate(4);
            channel.position(5L);
            
            // Act
            int bytesRead = channel.read(readBuffer);
            
            // Assert
            assertEquals(4, bytesRead);
            assertEquals("data", new String(readBuffer.array(), UTF_8));
            assertEquals(TEST_DATA.length, channel.position());
        }
    }

    @Test
    void positionSetCorrectly() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Act & Assert
            assertEquals(4L, channel.position(4L).position());
            assertEquals(TEST_DATA.length, channel.position(TEST_DATA.length).position());
            assertEquals(TEST_DATA.length + 1L, channel.position(TEST_DATA.length + 1L).position());
        }
    }

    @Test
    void truncateAdjustsPositionWhenTruncatingBeyondPosition() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Arrange
            channel.position(TEST_DATA.length);
            
            // Act
            channel.truncate(4L);
            
            // Assert
            assertEquals(4L, channel.position());
            assertEquals(4L, channel.size());
        }
    }

    @Test
    void readReturnsEofWhenPositionAtEnd() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Arrange
            ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
            channel.position(TEST_DATA.length + 1);
            
            // Act
            int firstRead = channel.read(readBuffer);
            int secondRead = channel.read(readBuffer);
            
            // Assert
            assertEquals(0, readBuffer.position());
            assertEquals(-1, firstRead);
            assertEquals(-1, secondRead);
        }
    }

    @Test
    void readThrowsClosedChannelExceptionWhenClosed() {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();
        
        // Act & Assert
        assertThrows(ClosedChannelException.class, () -> channel.read(ByteBuffer.allocate(1)));
    }

    @Test
    void writeThrowsClosedChannelExceptionWhenClosed() {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();
        
        // Act & Assert
        assertThrows(ClosedChannelException.class, () -> channel.write(ByteBuffer.allocate(1)));
    }

    @Test
    void positionSetThrowsIOExceptionWhenSettingBeyondMaxInteger() {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Act & Assert
            assertThrows(IOException.class, () -> channel.position(Integer.MAX_VALUE + 1L));
        }
    }

    @Test
    void truncateThrowsIllegalArgumentExceptionWhenSizeTooLarge() {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> channel.truncate(Integer.MAX_VALUE + 1L));
        }
    }

    @Test
    void truncateTruncatesDataCorrectly() {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Act
            channel.truncate(4);
            
            // Assert
            byte[] bytes = Arrays.copyOf(channel.array(), (int) channel.size());
            assertEquals("Some", new String(bytes, UTF_8));
        }
    }

    @Test
    void writeWritesFullData() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Arrange
            ByteBuffer inData = ByteBuffer.wrap(TEST_DATA);
            
            // Act
            int bytesWritten = channel.write(inData);
            
            // Assert
            assertEquals(TEST_DATA.length, bytesWritten);
            assertArrayEquals(TEST_DATA, Arrays.copyOf(channel.array(), (int) channel.size()));
            assertEquals(TEST_DATA.length, channel.position());
        }
    }

    @Test
    void writeWritesFromSetPosition() throws IOException {
        try (SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Arrange
            ByteBuffer inData = ByteBuffer.wrap(TEST_DATA);
            ByteBuffer expectedData = ByteBuffer.allocate(TEST_DATA.length + 5)
                .put(TEST_DATA, 0, 5)
                .put(TEST_DATA);
            channel.position(5L);
            
            // Act
            int bytesWritten = channel.write(inData);
            
            // Assert
            assertEquals(TEST_DATA.length, bytesWritten);
            assertArrayEquals(expectedData.array(), Arrays.copyOf(channel.array(), (int) channel.size()));
            assertEquals(TEST_DATA.length + 5, channel.position());
        }
    }

    @Test
    void positionSetThrowsClosedChannelExceptionWhenClosed() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.close();
            assertThrows(ClosedChannelException.class, () -> channel.position(0));
        }
    }

    @Test
    void truncateThrowsIllegalArgumentExceptionForNegativeSize() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            assertThrows(IllegalArgumentException.class, () -> channel.truncate(-1));
        }
    }

    @Test
    void positionSetThrowsIOExceptionForNegativeValue() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            assertThrows(IOException.class, () -> channel.position(-1));
        }
    }

    @Test
    void truncateDoesNotChangePositionWhenPositionLessThanNewSize() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Arrange
            channel.position(1);
            
            // Act
            channel.truncate(TEST_DATA.length - 1);
            
            // Assert
            assertEquals(TEST_DATA.length - 1, channel.size());
            assertEquals(1, channel.position());
        }
    }

    @Test
    void truncateToLargerSizeAdjustsPositionWhenPositionBeyondNewSize() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Arrange
            channel.position(2 * TEST_DATA.length);
            
            // Act
            channel.truncate(TEST_DATA.length + 1);
            
            // Assert
            assertEquals(TEST_DATA.length, channel.size());
            assertEquals(TEST_DATA.length + 1, channel.position());
        }
    }

    @Test
    void truncateToSameSizeAdjustsPositionWhenPositionBeyondSize() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Arrange
            channel.position(2 * TEST_DATA.length);
            
            // Act
            channel.truncate(TEST_DATA.length);
            
            // Assert
            assertEquals(TEST_DATA.length, channel.size());
            assertEquals(TEST_DATA.length, channel.position());
        }
    }

    @Test
    void truncateToSmallerSizeAdjustsPositionWhenPositionBeyondNewSize() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Arrange
            channel.position(4);
            
            // Act
            channel.truncate(3);
            
            // Assert
            assertEquals(3, channel.size());
            assertEquals(3, channel.position());
        }
    }

    @Test
    void truncateToLargerSizeDoesNotChangeData() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Act
            channel.truncate(TEST_DATA.length + 1);
            
            // Assert
            assertEquals(TEST_DATA.length, channel.size());
            ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
            assertEquals(TEST_DATA.length, channel.read(readBuffer));
            assertArrayEquals(TEST_DATA, Arrays.copyOf(readBuffer.array(), TEST_DATA.length));
        }
    }

    @Test
    void truncateToSameSizeDoesNotChangeData() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel(TEST_DATA)) {
            // Act
            channel.truncate(TEST_DATA.length);
            
            // Assert
            assertEquals(TEST_DATA.length, channel.size());
            ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
            assertEquals(TEST_DATA.length, channel.read(readBuffer));
            assertArrayEquals(TEST_DATA, Arrays.copyOf(readBuffer.array(), TEST_DATA.length));
        }
    }

    @Test
    @Disabled("Deliberate specification violation: position() doesn't throw when closed")
    void positionGetThrowsClosedChannelExceptionWhenClosed() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.close();
            channel.position();
        }
    }

    @Test
    @Disabled("Deliberate specification violation: size() doesn't throw when closed")
    void sizeGetThrowsClosedChannelExceptionWhenClosed() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.close();
            channel.size();
        }
    }

    @Test
    @Disabled("Deliberate specification violation: truncate() doesn't throw when closed")
    void truncateThrowsClosedChannelExceptionWhenClosed() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            channel.close();
            channel.truncate(0);
        }
    }

    @Test
    void writeGrowsChannelWhenWritingBeyondEnd() throws Exception {
        try (SeekableByteChannel channel = new SeekableInMemoryByteChannel()) {
            // Arrange
            channel.position(2);
            ByteBuffer inData = ByteBuffer.wrap(TEST_DATA);
            
            // Act
            int bytesWritten = channel.write(inData);
            
            // Assert
            assertEquals(TEST_DATA.length, bytesWritten);
            assertEquals(TEST_DATA.length + 2, channel.size());
            
            channel.position(2);
            ByteBuffer readBuffer = ByteBuffer.allocate(TEST_DATA.length);
            channel.read(readBuffer);
            assertArrayEquals(TEST_DATA, Arrays.copyOf(readBuffer.array(), TEST_DATA.length));
        }
    }
}