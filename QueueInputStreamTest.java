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

    private static final Duration DEFAULT_TEST_TIMEOUT = Duration.ofHours(1);
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Provides test data of various sizes to test edge cases around buffer boundaries.
     * Tests empty strings, small strings, and strings around common buffer sizes (4KB, 8KB).
     */
    public static Stream<Arguments> testDataProvider() {
        // @formatter:off
        return Stream.of(
                Arguments.of(""),
                Arguments.of("1"),
                Arguments.of("12"),
                Arguments.of("1234"),
                Arguments.of("12345678"),
                Arguments.of(StringUtils.repeat("A", 4095)),  // Just under 4KB
                Arguments.of(StringUtils.repeat("A", 4096)),  // Exactly 4KB
                Arguments.of(StringUtils.repeat("A", 4097)),  // Just over 4KB
                Arguments.of(StringUtils.repeat("A", 8191)),  // Just under 8KB
                Arguments.of(StringUtils.repeat("A", 8192)),  // Exactly 8KB
                Arguments.of(StringUtils.repeat("A", 8193)),  // Just over 8KB
                Arguments.of(StringUtils.repeat("A", 8192 * 4)) // Large data (32KB)
        );
        // @formatter:on
    }

    // ========== Constructor and Builder Tests ==========

    @Test
    @DisplayName("Builder should reject negative timeout values")
    void builderShouldRejectNegativeTimeout() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> QueueInputStream.builder().setTimeout(Duration.ofMillis(-1)).get());
        
        assertTrue(exception.getMessage().contains("waitTime must not be negative"));
    }

    @Test
    @DisplayName("Builder should handle null values by resetting to defaults")
    void builderShouldHandleNullValuesByResettingToDefaults() throws IOException {
        // Test null timeout resets to Duration.ZERO
        try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(null).get()) {
            assertEquals(Duration.ZERO, inputStream.getTimeout());
            assertEquals(0, inputStream.getBlockingQueue().size());
        }
        
        // Test null blocking queue resets to new LinkedBlockingQueue
        try (QueueInputStream inputStream = QueueInputStream.builder().setBlockingQueue(null).get()) {
            assertEquals(Duration.ZERO, inputStream.getTimeout());
            assertEquals(0, inputStream.getBlockingQueue().size());
        }
    }

    // ========== Basic Read/Write Tests ==========

    @ParameterizedTest(name = "Data: ''{0}''")
    @MethodSource("testDataProvider")
    @DisplayName("Should read and write data without buffering")
    void shouldReadAndWriteDataWithoutBuffering(String testData) throws IOException {
        try (QueueInputStream inputStream = new QueueInputStream();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
            
            // Write test data to output stream
            writeDataToStream(outputStream, testData);
            
            // Read all data from input stream
            String actualData = readAllDataFromStream(inputStream);
            
            assertEquals(testData, actualData);
        }
    }

    @ParameterizedTest(name = "Data: ''{0}''")
    @MethodSource("testDataProvider")
    @DisplayName("Should read and write data with timeout configured")
    void shouldReadAndWriteDataWithTimeoutConfigured(String testData) throws IOException {
        Duration timeout = Duration.ofMinutes(2);
        
        try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(timeout).get();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
            
            assertEquals(timeout, inputStream.getTimeout());
            
            writeDataToStream(outputStream, testData);
            
            String actualData = assertTimeout(Duration.ofSeconds(1), 
                () -> readDataFromStream(inputStream, testData.length()));
            
            assertEquals(testData, actualData);
        }
    }

    // ========== Buffered Read/Write Tests ==========

    @ParameterizedTest(name = "Data: ''{0}''")
    @MethodSource("testDataProvider")
    @DisplayName("Should work correctly with buffered input streams")
    void shouldWorkCorrectlyWithBufferedInputStreams(String testData) throws IOException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        
        try (BufferedInputStream inputStream = new BufferedInputStream(new QueueInputStream(queue));
             QueueOutputStream outputStream = new QueueOutputStream(queue)) {
            
            outputStream.write(testData.getBytes(StandardCharsets.UTF_8));
            String actualData = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            
            assertEquals(testData, actualData);
        }
    }

    @ParameterizedTest(name = "Data: ''{0}''")
    @MethodSource("testDataProvider")
    @DisplayName("Should work correctly with buffered output streams")
    void shouldWorkCorrectlyWithBufferedOutputStreams(String testData) throws IOException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        
        try (QueueInputStream inputStream = new QueueInputStream(queue);
             BufferedOutputStream outputStream = new BufferedOutputStream(
                 new QueueOutputStream(queue), DEFAULT_BUFFER_SIZE)) {
            
            outputStream.write(testData.getBytes(StandardCharsets.UTF_8));
            outputStream.flush(); // Important: must flush buffered output
            
            String actualData = readAllDataFromStream(inputStream);
            assertEquals(testData, actualData);
        }
    }

    @ParameterizedTest(name = "Data: ''{0}''")
    @MethodSource("testDataProvider")
    @DisplayName("Should work correctly with both input and output buffered")
    void shouldWorkCorrectlyWithBothInputAndOutputBuffered(String testData) throws IOException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        
        try (BufferedInputStream inputStream = new BufferedInputStream(new QueueInputStream(queue));
             BufferedOutputStream outputStream = new BufferedOutputStream(
                 new QueueOutputStream(queue), DEFAULT_BUFFER_SIZE)) {
            
            outputStream.write(testData.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            
            String actualData = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            assertEquals(testData, actualData);
        }
    }

    // ========== Line-by-Line Reading Tests ==========

    @ParameterizedTest(name = "Data: ''{0}''")
    @MethodSource("testDataProvider")
    @DisplayName("Should read data line by line from queue streams")
    void shouldReadDataLineByLineFromQueueStreams(String testData) throws IOException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        
        try (QueueInputStream inputStream = QueueInputStream.builder()
                .setBlockingQueue(queue)
                .setTimeout(DEFAULT_TEST_TIMEOUT)
                .get();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {

            readAndVerifyLinesFromStreams(testData, inputStream, outputStream);
        }
    }

    @ParameterizedTest(name = "Data: ''{0}''")
    @MethodSource("testDataProvider")
    @DisplayName("Should read data line by line from file streams (comparison test)")
    void shouldReadDataLineByLineFromFileStreams(String testData) throws IOException {
        Path tempFile = Files.createTempFile(getClass().getSimpleName(), ".txt");
        
        try (InputStream inputStream = Files.newInputStream(tempFile);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            readAndVerifyLinesFromStreams(testData, inputStream, outputStream);
        } finally {
            Files.delete(tempFile);
        }
    }

    // ========== Bulk Read Tests ==========

    @Test
    @DisplayName("Bulk read should return 0 when length is 0")
    void bulkReadShouldReturnZeroWhenLengthIsZero() {
        QueueInputStream inputStream = new QueueInputStream();
        
        int bytesRead = inputStream.read(EMPTY_BYTE_ARRAY, 0, 0);
        
        assertEquals(0, bytesRead);
    }

    @TestFactory
    @DisplayName("Bulk read should validate parameters and throw appropriate exceptions")
    public DynamicTest[] bulkReadShouldValidateParameters() {
        QueueInputStream inputStream = new QueueInputStream();
        
        return new DynamicTest[] {
                dynamicTest("Should throw IndexOutOfBoundsException when offset is too large", () ->
                        assertThrows(IndexOutOfBoundsException.class, () ->
                                inputStream.read(EMPTY_BYTE_ARRAY, 1, 0))),

                dynamicTest("Should throw IndexOutOfBoundsException when offset is negative", () ->
                        assertThrows(IndexOutOfBoundsException.class, () ->
                                inputStream.read(EMPTY_BYTE_ARRAY, -1, 0))),

                dynamicTest("Should throw IndexOutOfBoundsException when length exceeds buffer size", () ->
                        assertThrows(IndexOutOfBoundsException.class, () ->
                                inputStream.read(EMPTY_BYTE_ARRAY, 0, 1))),

                dynamicTest("Should throw IndexOutOfBoundsException when length is negative", () ->
                        assertThrows(IndexOutOfBoundsException.class, () ->
                                inputStream.read(EMPTY_BYTE_ARRAY, 0, -1))),
        };
    }

    @ParameterizedTest(name = "Data: ''{0}''")
    @MethodSource("testDataProvider")
    @DisplayName("Bulk read should wait for data when none is immediately available")
    void bulkReadShouldWaitForDataWhenNoneIsImmediatelyAvailable(String testData) throws IOException {
        assumeFalse(testData.isEmpty(), "Test requires non-empty data");

        // Create a custom queue that signals when polling starts
        CountDownLatch readStartedLatch = new CountDownLatch(1);
        CountDownLatch dataWrittenLatch = new CountDownLatch(1);
        LinkedBlockingQueue<Integer> queue = createQueueThatSignalsOnPoll(readStartedLatch, dataWrittenLatch);

        try (QueueInputStream inputStream = QueueInputStream.builder()
                .setBlockingQueue(queue)
                .setTimeout(DEFAULT_TEST_TIMEOUT)
                .get()) {
            
            QueueOutputStream outputStream = inputStream.newQueueOutputStream();
            
            // Start async task that waits for read to begin, then writes data
            CompletableFuture<Void> writeTask = CompletableFuture.runAsync(() -> {
                try {
                    readStartedLatch.await(1, TimeUnit.HOURS);
                    outputStream.write(testData.getBytes(UTF_8));
                    dataWrittenLatch.countDown();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            // Perform bulk read - this should wait until data is available
            byte[] buffer = new byte[testData.length()];
            int bytesRead = inputStream.read(buffer, 0, buffer.length);
            
            assertEquals(testData.length(), bytesRead);
            String actualData = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            assertEquals(testData, actualData);
            
            assertDoesNotThrow(() -> writeTask.get());
        }
    }

    // ========== Available() Method Tests ==========

    @ParameterizedTest(name = "Data: ''{0}''")
    @MethodSource("testDataProvider")
    @DisplayName("available() should always return 0 because read() blocks")
    void availableShouldAlwaysReturnZeroBecauseReadBlocks(String testData) throws IOException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        
        try (InputStream inputStream = new QueueInputStream(queue)) {
            // available() always returns 0 because read() blocks until data is available
            assertEquals(0, inputStream.available());
            
            IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            assertEquals(0, inputStream.available());
        }
    }

    @SuppressWarnings("resource")
    @ParameterizedTest(name = "Data: ''{0}''")
    @MethodSource("testDataProvider")
    @DisplayName("available() should return 0 after stream is closed")
    void availableShouldReturnZeroAfterStreamIsClosed(String testData) throws IOException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        InputStream inputStream = new QueueInputStream(queue);
        
        inputStream.close();
        
        assertEquals(0, inputStream.available());
    }

    // ========== Stream Closure Tests ==========

    @SuppressWarnings("resource")
    @ParameterizedTest(name = "Data: ''{0}''")
    @MethodSource("testDataProvider")
    @DisplayName("read() should return EOF after stream is closed")
    void readShouldReturnEofAfterStreamIsClosed(String testData) throws IOException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        InputStream inputStream = new QueueInputStream(queue);
        
        inputStream.close();
        
        assertEquals(IOUtils.EOF, inputStream.read());
    }

    // ========== Timeout Tests ==========

    @Test
    @DisplayName("Should timeout when no data is available within timeout period")
    void shouldTimeoutWhenNoDataIsAvailableWithinTimeoutPeriod() throws IOException {
        Duration timeout = Duration.ofMillis(500);
        
        try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(timeout).get();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
            
            Stopwatch stopwatch = Stopwatch.createStarted();
            
            String actualData = assertTimeout(Duration.ofSeconds(1), 
                () -> readDataFromStream(inputStream, 3));
            
            stopwatch.stop();
            
            assertEquals("", actualData);
            assertTrue(stopwatch.elapsed(TimeUnit.MILLISECONDS) >= 500, 
                () -> "Expected at least 500ms wait time, but was: " + stopwatch);
        }
    }

    @Test
    @DisplayName("Should throw IllegalStateException when read is interrupted while waiting")
    void shouldThrowIllegalStateExceptionWhenReadIsInterruptedWhileWaiting() throws Exception {
        try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(Duration.ofMinutes(2)).get();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {

            AtomicBoolean exceptionThrown = new AtomicBoolean(false);
            CountDownLatch testCompletedLatch = new CountDownLatch(1);
            
            // Start read operation in background thread
            Thread readerThread = new Thread(() -> {
                assertThrows(IllegalStateException.class, 
                    () -> readDataFromStream(inputStream, 3));
                assertTrue(Thread.currentThread().isInterrupted());
                exceptionThrown.set(true);
                testCompletedLatch.countDown();
            });
            readerThread.setDaemon(true);
            readerThread.start();

            // Interrupt the reader thread
            readerThread.interrupt();
            
            // Verify that the exception was thrown
            testCompletedLatch.await(500, TimeUnit.MILLISECONDS);
            assertTrue(exceptionThrown.get());
        }
    }

    // ========== Helper Methods ==========

    /**
     * Writes string data to an output stream as UTF-8 bytes.
     */
    private void writeDataToStream(QueueOutputStream outputStream, String data) throws IOException {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        outputStream.write(bytes, 0, bytes.length);
    }

    /**
     * Reads all available data from an input stream and returns it as a UTF-8 string.
     */
    private String readAllDataFromStream(InputStream inputStream) throws IOException {
        return readDataFromStream(inputStream, Integer.MAX_VALUE);
    }

    /**
     * Reads up to maxBytes from an input stream and returns it as a UTF-8 string.
     */
    private String readDataFromStream(InputStream inputStream, int maxBytes) throws IOException {
        if (maxBytes == 0) {
            return "";
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int byteRead;
        while ((byteRead = inputStream.read()) != -1 && buffer.size() < maxBytes) {
            buffer.write(byteRead);
        }
        
        return buffer.toString(StandardCharsets.UTF_8.name());
    }

    /**
     * Reads data line by line from input stream, writing each line to output stream first.
     * This simulates interactive line-by-line processing.
     */
    private void readAndVerifyLinesFromStreams(String testData, InputStream inputStream, 
                                             OutputStream outputStream) throws IOException {
        String[] expectedLines = testData.split("\n");
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8))) {
            for (String expectedLine : expectedLines) {
                // Write line to output stream
                outputStream.write(expectedLine.getBytes(UTF_8));
                outputStream.write('\n');

                // Read line from input stream and verify
                String actualLine = reader.readLine();
                assertEquals(expectedLine, actualLine);
            }
        }
    }

    /**
     * Creates a LinkedBlockingQueue that signals when poll() is called and waits for a signal before proceeding.
     * This is used to test scenarios where data is not immediately available.
     */
    private LinkedBlockingQueue<Integer> createQueueThatSignalsOnPoll(CountDownLatch onPollLatch, 
                                                                     CountDownLatch afterWriteLatch) {
        return new LinkedBlockingQueue<Integer>() {
            @Override
            public Integer poll(long timeout, TimeUnit unit) throws InterruptedException {
                onPollLatch.countDown();
                afterWriteLatch.await(1, TimeUnit.HOURS);
                return super.poll(timeout, unit);
            }
        };
    }
}