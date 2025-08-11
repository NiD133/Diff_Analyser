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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_BYTE_ARRAY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.QueueOutputStream;
import org.apache.commons.io.output.QueueOutputStreamTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.base.Stopwatch;

/**
 * Test {@link QueueInputStream}.
 *
 * @see QueueOutputStreamTest
 */
public class QueueInputStreamTest {

    // Buffer sizes used in tests
    private static final int BUFFER_SIZE_4096 = 4096;
    private static final int BUFFER_SIZE_8192 = 8192;
    private static final Duration LONG_TIMEOUT = Duration.ofHours(1);
    private static final Duration SHORT_TIMEOUT = Duration.ofMillis(500);
    private static final Duration TEST_TIMEOUT = Duration.ofSeconds(1);

    /**
     * Provides test data for parameterized tests. Includes:
     * - Empty string
     * - Small strings (1, 2, 4, 8 bytes)
     * - Strings around buffer boundaries (4095, 4096, 4097 bytes)
     * - Strings around larger buffer boundaries (8191, 8192, 8193 bytes)
     * - Large string (32768 bytes)
     */
    public static Stream<Arguments> provideInputData() {
        // @formatter:off
        return Stream.of(
            Arguments.of(""),  // Empty input
            Arguments.of("1"),
            Arguments.of("12"),
            Arguments.of("1234"),
            Arguments.of("12345678"),
            Arguments.of(StringUtils.repeat("A", BUFFER_SIZE_4096 - 1)),
            Arguments.of(StringUtils.repeat("A", BUFFER_SIZE_4096)),
            Arguments.of(StringUtils.repeat("A", BUFFER_SIZE_4096 + 1)),
            Arguments.of(StringUtils.repeat("A", BUFFER_SIZE_8192 - 1)),
            Arguments.of(StringUtils.repeat("A", BUFFER_SIZE_8192)),
            Arguments.of(StringUtils.repeat("A", BUFFER_SIZE_8192 + 1)),
            Arguments.of(StringUtils.repeat("A", BUFFER_SIZE_8192 * 4))
        );
        // @formatter:on
    }

    @TestFactory
    public DynamicTest[] testBulkReadErrorHandling() {
        final QueueInputStream queueInputStream = new QueueInputStream();
        return new DynamicTest[] {
            dynamicTest("Throws IndexOutOfBoundsException when offset exceeds array size", () ->
                assertThrows(IndexOutOfBoundsException.class, () ->
                    queueInputStream.read(EMPTY_BYTE_ARRAY, 1, 0))),

            dynamicTest("Throws IndexOutOfBoundsException when offset is negative", () ->
                assertThrows(IndexOutOfBoundsException.class, () ->
                    queueInputStream.read(EMPTY_BYTE_ARRAY, -1, 0))),

            dynamicTest("Throws IndexOutOfBoundsException when length exceeds array capacity", () ->
                assertThrows(IndexOutOfBoundsException.class, () ->
                    queueInputStream.read(EMPTY_BYTE_ARRAY, 0, 1))),

            dynamicTest("Throws IndexOutOfBoundsException when length is negative", () ->
                assertThrows(IndexOutOfBoundsException.class, () ->
                    queueInputStream.read(EMPTY_BYTE_ARRAY, 0, -1))),
        };
    }

    private int defaultBufferSize() {
        return BUFFER_SIZE_8192;
    }

    /**
     * Helper method to test reading line-by-line.
     * Writes each line to output stream and verifies it can be read from input stream.
     */
    private void verifyLineByLineReading(final String inputData, 
                                        final InputStream inputStream, 
                                        final OutputStream outputStream) throws IOException {
        final String[] lines = inputData.split("\n");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8))) {
            for (final String line : lines) {
                // Write line to output stream
                outputStream.write(line.getBytes(UTF_8));
                outputStream.write('\n');

                // Read and verify line
                final String actualLine = reader.readLine();
                assertEquals(line, actualLine);
            }
        }
    }

    /**
     * Reads all data from input stream one byte at a time.
     */
    private String readUnbuffered(final InputStream inputStream) throws IOException {
        return readUnbuffered(inputStream, Integer.MAX_VALUE);
    }

    /**
     * Reads up to maxBytes from input stream one byte at a time.
     */
    private String readUnbuffered(final InputStream inputStream, final int maxBytes) throws IOException {
        if (maxBytes == 0) {
            return "";
        }

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int n;
        int bytesRead = 0;
        while ((n = inputStream.read()) != -1 && bytesRead < maxBytes) {
            byteArrayOutputStream.write(n);
            bytesRead++;
        }
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
    }

    // ========================== Available Tests ==========================

    @ParameterizedTest(name = "inputData={0}")
    @MethodSource("provideInputData")
    void testAvailableReturnsZeroAfterClose(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        final InputStream shadow;
        try (InputStream inputStream = new QueueInputStream(queue)) {
            shadow = inputStream;
        }
        assertEquals(0, shadow.available());
    }

    @ParameterizedTest(name = "inputData={0}")
    @MethodSource("provideInputData")
    void testAvailableReturnsZeroWhenNoDataIsAvailable(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (InputStream inputStream = new QueueInputStream(queue)) {
            // Should be 0 when no data is available
            assertEquals(0, inputStream.available());
            
            // Read all data (blocks until stream is closed)
            IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            
            // Should be 0 after reading all data
            assertEquals(0, inputStream.available());
        }
    }

    // ===================== Buffered Read/Write Tests =====================

    @ParameterizedTest(name = "inputData={0}")
    @MethodSource("provideInputData")
    void testBufferedInputStreamReading(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (BufferedInputStream inputStream = new BufferedInputStream(new QueueInputStream(queue));
             QueueOutputStream outputStream = new QueueOutputStream(queue)) {
            outputStream.write(inputData.getBytes(StandardCharsets.UTF_8));
            final String actualData = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            assertEquals(inputData, actualData);
        }
    }

    @ParameterizedTest(name = "inputData={0}")
    @MethodSource("provideInputData")
    void testBufferedInputAndOutputStreams(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (BufferedInputStream inputStream = new BufferedInputStream(new QueueInputStream(queue));
             BufferedOutputStream outputStream = new BufferedOutputStream(
                 new QueueOutputStream(queue), defaultBufferSize())) {
            outputStream.write(inputData.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            final String dataCopy = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            assertEquals(inputData, dataCopy);
        }
    }

    @ParameterizedTest(name = "inputData={0}")
    @MethodSource("provideInputData")
    void testBufferedOutputStreamWriting(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (QueueInputStream inputStream = new QueueInputStream(queue);
             BufferedOutputStream outputStream = new BufferedOutputStream(
                 new QueueOutputStream(queue), defaultBufferSize())) {
            outputStream.write(inputData.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            final String actualData = readUnbuffered(inputStream);
            assertEquals(inputData, actualData);
        }
    }

    // ========================== Bulk Read Tests ==========================

    @ParameterizedTest(name = "inputData={0}")
    @MethodSource("provideInputData")
    void testBulkReadWaitsForData(final String inputData) throws Exception {
        // Skip empty input since we need data to test waiting
        assumeFalse(inputData.isEmpty(), "Skipping empty input for bulk read test");

        final CountDownLatch onPollLatch = new CountDownLatch(1);
        final CountDownLatch afterWriteLatch = new CountDownLatch(1);
        
        // Custom queue that triggers latches to control read/write timing
        final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>() {
            @Override
            public Integer poll(final long timeout, final TimeUnit unit) throws InterruptedException {
                onPollLatch.countDown();  // Signal that read is waiting
                afterWriteLatch.await(1, TimeUnit.HOURS);  // Wait for write
                return super.poll(timeout, unit);
            }
        };

        try (QueueInputStream queueInputStream = QueueInputStream.builder()
                .setBlockingQueue(queue)
                .setTimeout(LONG_TIMEOUT)
                .get()) {
            final QueueOutputStream queueOutputStream = queueInputStream.newQueueOutputStream();
            
            // Write data in background thread
            final CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    onPollLatch.await(1, TimeUnit.HOURS);
                    queueOutputStream.write(inputData.getBytes(UTF_8));
                    afterWriteLatch.countDown();
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            });

            // Read data (should wait for background write)
            final byte[] data = new byte[inputData.length()];
            final int read = queueInputStream.read(data, 0, data.length);
            
            // Verify read data
            assertEquals(inputData.length(), read);
            final String outputData = new String(data, 0, read, StandardCharsets.UTF_8);
            assertEquals(inputData, outputData);
            
            // Ensure background task completed
            assertDoesNotThrow(() -> future.get());
        }
    }

    @Test
    void testBulkReadZeroLengthArrayReturnsZero() {
        final QueueInputStream queueInputStream = new QueueInputStream();
        final int read = queueInputStream.read(EMPTY_BYTE_ARRAY, 0, 0);
        assertEquals(0, read);
    }

    // ========================== Builder Tests ==========================

    @Test
    void testBuilderThrowsOnInvalidArguments() {
        assertThrows(IllegalArgumentException.class, 
            () -> QueueInputStream.builder().setTimeout(Duration.ofMillis(-1)).get(), 
            "Should throw when negative timeout is set");
    }

    @SuppressWarnings("resource")
    @ParameterizedTest(name = "inputData={0}")
    @MethodSource("provideInputData")
    void testReadReturnsEndOfStreamAfterClose(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        final InputStream shadow;
        try (InputStream inputStream = new QueueInputStream(queue)) {
            shadow = inputStream;
        }
        assertEquals(IOUtils.EOF, shadow.read());
    }

    // ===================== Line-by-Line Read Tests =====================

    @ParameterizedTest(name = "inputData={0}")
    @MethodSource("provideInputData")
    void testReadLineByLineWithFileStreams(final String inputData) throws IOException {
        final Path tempFile = Files.createTempFile(getClass().getSimpleName(), ".txt");
        try (InputStream inputStream = Files.newInputStream(tempFile);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {
            verifyLineByLineReading(inputData, inputStream, outputStream);
        } finally {
            Files.delete(tempFile);
        }
    }

    @ParameterizedTest(name = "inputData={0}")
    @MethodSource("provideInputData")
    void testReadLineByLineWithQueueStreams(final String inputData) throws IOException {
        final String[] lines = inputData.split("\n");
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (QueueInputStream inputStream = QueueInputStream.builder()
                .setBlockingQueue(queue)
                .setTimeout(LONG_TIMEOUT)
                .get();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
            verifyLineByLineReading(inputData, inputStream, outputStream);
        }
    }

    @Test
    void testBuilderHandlesNullArguments() throws IOException {
        // Verify default values when setting null timeout
        try (QueueInputStream queueInputStream = QueueInputStream.builder().setTimeout(null).get()) {
            assertEquals(Duration.ZERO, queueInputStream.getTimeout());
            assertEquals(0, queueInputStream.getBlockingQueue().size());
        }
        
        // Verify default values when setting null queue
        try (QueueInputStream queueInputStream = QueueInputStream.builder().setBlockingQueue(null).get()) {
            assertEquals(Duration.ZERO, queueInputStream.getTimeout());
            assertEquals(0, queueInputStream.getBlockingQueue().size());
        }
    }

    // ========================== Timeout Tests ==========================

    @Test
    @DisplayName("Reading thread throws IllegalStateException when interrupted during timeout")
    void testReadThrowsWhenInterruptedDuringTimeout() throws Exception {
        try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(LONG_TIMEOUT).get();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {

            // Start read operation in background thread
            final AtomicBoolean result = new AtomicBoolean();
            final CountDownLatch latch = new CountDownLatch(1);
            final Thread thread = new Thread(() -> {
                // Verify interrupted state handling
                assertThrows(IllegalStateException.class, () -> readUnbuffered(inputStream, 3));
                assertTrue(Thread.currentThread().isInterrupted());
                result.set(true);
                latch.countDown();
            });
            thread.setDaemon(true);
            thread.start();

            // Interrupt read operation
            thread.interrupt();
            
            // Verify thread handled interruption
            latch.await(500, TimeUnit.MILLISECONDS);
            assertTrue(result.get());
        }
    }

    @Test
    @DisplayName("Read times out when data is not available")
    void testReadTimesOutWhenDataUnavailable() throws IOException {
        try (QueueInputStream inputStream = QueueInputStream.builder()
                .setTimeout(SHORT_TIMEOUT).get();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
            
            final Stopwatch stopwatch = Stopwatch.createStarted();
            final String actualData = assertTimeout(TEST_TIMEOUT, () -> 
                readUnbuffered(inputStream, 3));
            stopwatch.stop();
            
            assertEquals("", actualData);
            assertTrue(stopwatch.elapsed(TimeUnit.MILLISECONDS) >= SHORT_TIMEOUT.toMillis(),
                "Should wait full timeout duration");
        }
    }

    // ===================== Unbuffered Read/Write Tests =====================

    @ParameterizedTest(name = "inputData={0}")
    @MethodSource("provideInputData")
    void testUnbufferedReadWrite(final String inputData) throws IOException {
        try (QueueInputStream inputStream = new QueueInputStream();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
            writeUnbuffered(outputStream, inputData);
            final String actualData = readUnbuffered(inputStream);
            assertEquals(inputData, actualData);
        }
    }

    @ParameterizedTest(name = "inputData={0}")
    @MethodSource("provideInputData")
    void testUnbufferedReadWriteWithTimeout(final String inputData) throws IOException {
        final Duration timeout = Duration.ofMinutes(2);
        try (QueueInputStream inputStream = QueueInputStream.builder()
                .setTimeout(timeout).get();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
            
            assertEquals(timeout, inputStream.getTimeout());
            writeUnbuffered(outputStream, inputData);
            
            final String actualData = assertTimeout(TEST_TIMEOUT, () -> 
                readUnbuffered(inputStream, inputData.length()));
            
            assertEquals(inputData, actualData);
        }
    }

    /**
     * Writes data to output stream without buffering.
     */
    private void writeUnbuffered(final QueueOutputStream outputStream, 
                                final String inputData) throws IOException {
        final byte[] bytes = inputData.getBytes(StandardCharsets.UTF_8);
        outputStream.write(bytes, 0, bytes.length);
    }
}