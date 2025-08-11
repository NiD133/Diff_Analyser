/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.input;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.output.QueueOutputStream;
import org.junit.Test;

/**
 * Tests for {@link QueueInputStream}.
 */
public class QueueInputStreamTest {

    // --- Builder Tests ---

    @Test
    public void testBuilderSetTimeoutShouldBeFluent() {
        final QueueInputStream.Builder builder = QueueInputStream.builder();
        assertSame("Builder set a timeout should return the same builder instance",
                builder, builder.setTimeout(Duration.ofMillis(100)));
    }

    @Test
    public void testBuilderSetBlockingQueueShouldBeFluent() {
        final QueueInputStream.Builder builder = QueueInputStream.builder();
        assertSame("Builder set a queue should return the same builder instance",
                builder, builder.setBlockingQueue(new LinkedBlockingQueue<>()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderSetTimeout_withNegativeDuration_shouldThrowException() {
        QueueInputStream.builder().setTimeout(Duration.ofMillis(-1));
    }

    @Test(expected = NullPointerException.class)
    public void testBuilderSetTimeout_withNull_thenGetShouldThrowException() {
        // The timeout is checked for null only when the stream is built.
        QueueInputStream.builder().setTimeout(null).get();
    }

    @Test
    public void testBuilderGet_shouldCreateDefaultInstance() {
        final QueueInputStream inputStream = QueueInputStream.builder().get();
        assertNotNull(inputStream);
    }

    // --- Constructor Tests ---

    @Test
    @SuppressWarnings("deprecation") // Testing deprecated constructor for backward compatibility
    public void testConstructor_withNullQueue_shouldUseDefaultQueue() {
        // The deprecated constructor should handle a null queue by creating a default one.
        final QueueInputStream inputStream = new QueueInputStream(null);
        assertNotNull("A non-null queue should be created", inputStream.getBlockingQueue());
    }

    // --- Read Method Tests ---

    @Test
    public void testRead_whenQueueIsEmpty_shouldReturnEof() throws Exception {
        // Arrange
        final QueueInputStream inputStream = new QueueInputStream();

        // Act
        final int result = inputStream.read();

        // Assert
        assertEquals(-1, result);
    }



    @Test
    public void testRead_whenQueueHasData_shouldReturnBytesInOrder() throws Exception {
        // Arrange
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        queue.put(42);
        queue.put(43);
        final QueueInputStream inputStream = new QueueInputStream(queue);

        // Act & Assert
        assertEquals(42, inputStream.read());
        assertEquals(43, inputStream.read());
        assertEquals("Stream should be empty after reading all bytes", -1, inputStream.read());
    }

    @Test
    public void testRead_shouldReturnLowOrderByteOfInteger() throws Exception {
        // Arrange
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        // The stream should only consider the low-order byte of the integer.
        // 256 decimal is 0x100 hex. The low-order byte is 0.
        queue.put(256);
        // -1 decimal is 0xFFFFFFFF hex. The low-order byte is 255 (0xFF).
        queue.put(-1);
        final QueueInputStream inputStream = new QueueInputStream(queue);

        // Act & Assert
        assertEquals(0, inputStream.read());
        assertEquals(255, inputStream.read());
    }

    // --- Read(byte[]) Method Tests ---

    @Test
    public void testReadBuffer_whenQueueIsEmpty_shouldReturnEof() throws Exception {
        // Arrange
        final QueueInputStream inputStream = new QueueInputStream();
        final byte[] buffer = new byte[10];

        // Act
        final int bytesRead = inputStream.read(buffer, 0, buffer.length);

        // Assert
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadBuffer_whenQueueHasData_shouldReadBytes() throws Exception {
        // Arrange
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        queue.put(10);
        queue.put(20);
        final QueueInputStream inputStream = new QueueInputStream(queue);
        final byte[] buffer = new byte[5];

        // Act
        final int bytesRead = inputStream.read(buffer, 1, 2);

        // Assert
        assertEquals("Should read 2 bytes", 2, bytesRead);
        // The buffer should be modified at the specified offset.
        assertArrayEquals(new byte[]{0, 10, 20, 0, 0}, buffer);
    }

    @Test
    public void testReadBuffer_withZeroLength_shouldReturnZero() throws Exception {
        // Arrange
        final QueueInputStream inputStream = new QueueInputStream();
        final byte[] buffer = new byte[10];
        final byte[] originalBuffer = buffer.clone();

        // Act
        final int bytesRead = inputStream.read(buffer, 0, 0);

        // Assert
        assertEquals(0, bytesRead);
        assertArrayEquals("Buffer should not be modified", originalBuffer, buffer);
    }

    // --- Read(byte[]) Exception Tests ---

    @Test(expected = NullPointerException.class)
    public void testReadBuffer_withNullBuffer_shouldThrowException() throws Exception {
        new QueueInputStream().read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBuffer_withNegativeOffset_shouldThrowException() throws Exception {
        new QueueInputStream().read(new byte[10], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBuffer_withNegativeLength_shouldThrowException() throws Exception {
        new QueueInputStream().read(new byte[10], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBuffer_withOffsetOutOfBounds_shouldThrowException() throws Exception {
        // Tries to write up to index 11 (5 + 6) in a 10-byte array
        new QueueInputStream().read(new byte[10], 5, 6);
    }

    // --- Skip Method Test ---

    @Test
    public void testSkip_shouldSkipBytesFromQueue() throws Exception {
        // Arrange
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        queue.put(1);
        queue.put(2);
        queue.put(3);
        final QueueInputStream inputStream = new QueueInputStream(queue);

        // Act
        final long skipped = inputStream.skip(2);

        // Assert
        assertEquals(2L, skipped);
        assertEquals("Only the third byte should remain", 3, inputStream.read());
    }

    // --- Integration Test ---

    @Test
    public void testNewQueueOutputStream_shouldBeConnectedToInputStream() throws IOException {
        // Arrange
        final QueueInputStream inputStream = new QueueInputStream();
        // The QueueOutputStream is created from the InputStream and is connected to it.
        final QueueOutputStream outputStream = inputStream.newQueueOutputStream();
        final byte[] dataToWrite = {1, 2, 3, 4, 5};

        // Act: Write data to the output stream
        outputStream.write(dataToWrite);

        // Assert: Read the same data from the input stream
        final byte[] dataRead = new byte[5];
        final int bytesRead = inputStream.read(dataRead);

        assertEquals("Should read the same number of bytes written", dataToWrite.length, bytesRead);
        assertArrayEquals("Data read should match data written", dataToWrite, dataRead);
        assertEquals("Stream should now be empty", -1, inputStream.read());
    }
}