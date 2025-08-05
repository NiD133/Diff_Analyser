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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit tests for {@link MultiReadOnlySeekableByteChannel}.
 */
public class MultiReadOnlySeekableByteChannelTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File file1;
    private File file2;
    private final byte[] file1Content = "01234".getBytes(StandardCharsets.UTF_8);
    private final byte[] file2Content = "56789".getBytes(StandardCharsets.UTF_8);

    @Before
    public void setUp() throws IOException {
        file1 = tempFolder.newFile("file1.txt");
        Files.write(file1.toPath(), file1Content);

        file2 = tempFolder.newFile("file2.txt");
        Files.write(file2.toPath(), file2Content);
    }

    @Test
    public void forFilesFactoryCreatesCorrectChannel() throws IOException {
        // Arrange
        File[] files = {file1, file2};

        // Act
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(files)) {
            // Assert
            assertNotNull(channel);
            assertTrue(channel.isOpen());
            assertEquals(file1Content.length + file2Content.length, channel.size());
        }
    }

    @Test
    public void forPathsFactoryCreatesCorrectChannel() throws IOException {
        // Arrange
        Path[] paths = {file1.toPath(), file2.toPath()};

        // Act
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forPaths(paths)) {
            // Assert
            assertNotNull(channel);
            assertTrue(channel.isOpen());
            assertEquals(file1Content.length + file2Content.length, channel.size());
        }
    }

    @Test
    public void forSeekableByteChannelsFactoryCreatesCorrectChannel() throws IOException {
        // Arrange
        try (SeekableByteChannel channel1 = Files.newByteChannel(file1.toPath());
             SeekableByteChannel channel2 = Files.newByteChannel(file2.toPath())) {
            SeekableByteChannel[] channels = {channel1, channel2};

            // Act
            SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);

            // Assert
            assertNotNull(multiChannel);
            assertTrue(multiChannel.isOpen());
            assertEquals(file1Content.length + file2Content.length, multiChannel.size());
            // Note: The new channel takes ownership, so we don't close it in this try-with-resources block.
            // It will be closed along with the underlying channels when they are closed.
        }
    }

    @Test
    public void sizeShouldReturnSumOfAllChannelSizes() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1, file2)) {
            long expectedSize = file1Content.length + file2Content.length;
            assertEquals(expectedSize, channel.size());
        }
    }

    @Test
    public void readShouldCrossChannelBoundaries() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1, file2)) {
            ByteBuffer buffer = ByteBuffer.allocate(file1Content.length + file2Content.length);

            int bytesRead = channel.read(buffer);

            assertEquals(buffer.capacity(), bytesRead);
            buffer.flip();
            byte[] expectedContent = "0123456789".getBytes(StandardCharsets.UTF_8);
            byte[] actualContent = new byte[buffer.remaining()];
            buffer.get(actualContent);
            assertArrayEquals(expectedContent, actualContent);
        }
    }

    @Test
    public void positionShouldSeekAcrossChannelBoundaries() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1, file2)) {
            // Seek into the second file
            long newPosition = file1Content.length + 2;
            channel.position(newPosition);
            assertEquals(newPosition, channel.position());

            // Read the rest of the content
            ByteBuffer buffer = ByteBuffer.allocate(3);
            int bytesRead = channel.read(buffer);
            assertEquals(3, bytesRead);
            assertArrayEquals("789".getBytes(StandardCharsets.UTF_8), buffer.array());
        }
    }

    @Test
    public void positionWithChannelAndOffsetShouldSeekToCorrectGlobalPosition() throws IOException {
        try (MultiReadOnlySeekableByteChannel channel = (MultiReadOnlySeekableByteChannel) MultiReadOnlySeekableByteChannel.forFiles(file1, file2)) {
            // Seek to offset 2 within the second channel (index 1)
            channel.position(1, 2);

            // Position should be size of first file (5) + offset in second file (2)
            long expectedPosition = file1Content.length + 2;
            assertEquals(expectedPosition, channel.position());
        }
    }

    @Test
    public void isOpenShouldReturnFalseAfterClose() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1, file2);
        assertTrue(channel.isOpen());
        channel.close();
        assertFalse(channel.isOpen());
    }

    @Test(expected = NonWritableChannelException.class)
    public void writeShouldThrowNonWritableChannelException() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1)) {
            channel.write(ByteBuffer.allocate(1));
        }
    }

    @Test(expected = NonWritableChannelException.class)
    public void truncateShouldThrowNonWritableChannelException() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1)) {
            channel.truncate(1);
        }
    }

    @Test(expected = ClosedChannelException.class)
    public void readOnClosedChannelThrowsException() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1);
        channel.close();
        channel.read(ByteBuffer.allocate(1));
    }

    @Test(expected = ClosedChannelException.class)
    public void sizeOnClosedChannelThrowsException() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1);
        channel.close();
        channel.size();
    }

    @Test(expected = ClosedChannelException.class)
    public void positionOnClosedChannelThrowsException() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1);
        channel.close();
        channel.position(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void positionWithNegativeOffsetThrowsIllegalArgumentException() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1)) {
            channel.position(-1L);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void positionWithInvalidChannelIndexThrowsException() throws IOException {
        try (MultiReadOnlySeekableByteChannel channel = (MultiReadOnlySeekableByteChannel) MultiReadOnlySeekableByteChannel.forFiles(file1, file2)) {
            // There are 2 files, so valid indices are 0 and 1. Index 2 is out of bounds.
            channel.position(2, 0);
        }
    }

    @Test(expected = NoSuchFileException.class)
    public void forFilesWithNonExistentFileThrowsException() throws IOException {
        File nonExistent = new File("non-existent-file.tmp");
        MultiReadOnlySeekableByteChannel.forFiles(nonExistent);
    }

    @Test(expected = NullPointerException.class)
    public void forFilesWithNullInArrayThrowsException() throws IOException {
        File[] files = {file1, null};
        MultiReadOnlySeekableByteChannel.forFiles(files);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullListThrowsException() {
        new MultiReadOnlySeekableByteChannel(null);
    }

    @Test
    public void operationOnChannelWithNullMemberThrowsNullPointerException() {
        // A channel list containing null is not valid and should fail fast.
        SeekableByteChannel nullChannel = null;
        try (MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(Collections.singletonList(nullChannel))) {
            channel.size();
            fail("Expected a NullPointerException");
        } catch (IOException | NullPointerException e) {
            // Expected
            assertTrue(e instanceof NullPointerException);
        }
    }
    
    @Test
    public void forSeekableByteChannelsShouldIgnoreNullsInInputArray() throws IOException {
        // Arrange
        try (SeekableByteChannel channel1 = Files.newByteChannel(file1.toPath())) {
            SeekableByteChannel[] channels = {null, channel1, null};

            // Act
            try (SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels)) {
                // Assert
                assertNotNull(multiChannel);
                assertEquals(file1Content.length, multiChannel.size());
            }
        }
    }
}