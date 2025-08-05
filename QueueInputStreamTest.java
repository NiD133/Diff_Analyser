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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.QueueOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link QueueInputStream}.
 *
 * <p>
 * This test suite is structured using nested classes to group related test cases,
 * improving readability and organization.
 * </p>
 */
public class QueueInputStreamTest {

    private static final int BUFFER_SIZE = 8192;

    /**
     * Provides a stream of strings with various lengths for parameterized tests.
     * The data includes empty, small, and large strings that test edge cases
     * around buffer boundaries.
     */
    public static Stream<Arguments> inputDataProvider() {
        // @formatter:off
        return Stream.of(
            Arguments.of(""),
            Arguments.of("1"),
            Arguments.of("12"),
            Arguments.of("1234"),
            Arguments.of("12345678"),
            Arguments.of(StringUtils.repeat("A", 4095)),
            Arguments.of(StringUtils.repeat("A", 4096)),
            Arguments.of(StringUtils.repeat("A", 4097)),
            Arguments.of(StringUtils.repeat("A", 8191)),
            Arguments.of(StringUtils.repeat("A", 8192)),
            Arguments.of(StringUtils.repeat("A", 8193)),
            Arguments.of(StringUtils.repeat("A", 8192 * 4))
        );
        // @formatter:on
    }

    @Nested
    @DisplayName("Basic Read/Write Operations")
    class BasicReadWriteTest {

        @ParameterizedTest(name = "Input length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("should read data written by an unbuffered output stream")
        void readShouldReturnDataFromUnbufferedWrite(final String inputData) throws IOException {
            try (QueueInputStream inputStream = new QueueInputStream();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {

                outputStream.write(inputData.getBytes(UTF_8));
                final String actualData = IOUtils.toString(inputStream, UTF_8);

                assertEquals(inputData, actualData);
            }
        }

        @ParameterizedTest(name = "Input length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("should read data written by a buffered output stream")
        void readShouldReturnDataFromBufferedWrite(final String inputData) throws IOException {
            try (QueueInputStream inputStream = new QueueInputStream();
                 BufferedOutputStream outputStream = new BufferedOutputStream(inputStream.newQueueOutputStream(), BUFFER_SIZE)) {

                outputStream.write(inputData.getBytes(UTF_8));
                outputStream.flush();
                final String actualData = IOUtils.toString(inputStream, UTF_8);

                assertEquals(inputData, actualData);
            }
        }

        @ParameterizedTest(name = "Input length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("should work correctly with a buffered input stream")
        void bufferedReadShouldReturnAllData(final String inputData) throws IOException {
            try (QueueInputStream qis = new QueueInputStream();
                 BufferedInputStream inputStream = new BufferedInputStream(qis);
                 QueueOutputStream outputStream = qis.newQueueOutputStream()) {

                outputStream.write(inputData.getBytes(UTF_8));
                final String actualData = IOUtils.toString(inputStream, UTF_8);

                assertEquals(inputData, actualData);
            }
        }

        @ParameterizedTest(name = "Input length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("should support synchronized line-by-line read/write")
        void readLineByLineSynchronized(final String inputData) throws IOException {
            // This test verifies a synchronized, line-by-line producer/consumer scenario.
            final String[] lines = inputData.split("\n");

            try (QueueInputStream inputStream = new QueueInputStream();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8))) {

                for (final String expectedLine : lines) {
                    // Producer writes one line
                    outputStream.write(expectedLine.getBytes(UTF_8));
                    outputStream.write('\n');

                    // Consumer reads one line and verifies it
                    final String actualLine = reader.readLine();
                    assertEquals(expectedLine, actualLine);
                }
            }
        }
    }

    @Nested
    @DisplayName("Stream State and Lifecycle")
    class LifecycleTest {

        @Test
        @DisplayName("available() should return 0 on a new stream")
        void availableShouldBeZeroOnNewStream() throws IOException {
            try (QueueInputStream inputStream = new QueueInputStream()) {
                // available() is always 0 because read() blocks until data is present.
                assertEquals(0, inputStream.available(), "available() should be 0 for a new stream.");
            }
        }

        @Test
        @DisplayName("available() should return 0 after the stream is closed")
        void availableShouldBeZeroAfterClose() throws IOException {
            final QueueInputStream inputStream = new QueueInputStream();
            inputStream.close();
            assertEquals(0, inputStream.available(), "available() should be 0 for a closed stream.");
        }

        @Test
        @DisplayName("read() should return EOF after the stream is closed")
        void readShouldReturnEofAfterClose() throws IOException {
            final QueueInputStream inputStream = new QueueInputStream();
            inputStream.close();
            assertEquals(IOUtils.EOF, inputStream.read(), "read() should return EOF on a closed stream.");
        }
    }

    @Nested
    @DisplayName("Timeout and Concurrency")
    class TimeoutAndConcurrencyTest {

        @ParameterizedTest(name = "Input length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("read(byte[]) should block until data is available")
        void bulkReadShouldBlockUntilDataAvailable(final String inputData) throws IOException {
            assumeFalse(inputData.isEmpty(), "This test requires non-empty input data.");

            // Latches to orchestrate the reader (main thread) and writer (background thread).
            final CountDownLatch readerIsWaitingLatch = new CountDownLatch(1);
            final CountDownLatch writerHasFinishedLatch = new CountDownLatch(1);

            // A custom queue to detect when the reader starts waiting (polling).
            final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>() {
                @Override
                public Integer poll(final long timeout, final TimeUnit unit) throws InterruptedException {
                    // 1. Signal that the reader thread has started waiting for data.
                    readerIsWaitingLatch.countDown();
                    // 2. Wait until the writer thread has finished writing.
                    assertTrue(writerHasFinishedLatch.await(5, TimeUnit.SECONDS), "Writer did not finish in time.");
                    // 3. Proceed with the actual poll to retrieve the data.
                    return super.poll(timeout, unit);
                }
            };

            try (QueueInputStream inputStream = QueueInputStream.builder()
                    .setBlockingQueue(queue)
                    .setTimeout(Duration.ofHours(1)) // Long timeout to prevent accidental timeout.
                    .get()) {

                final QueueOutputStream outputStream = inputStream.newQueueOutputStream();

                // Start a writer thread that waits for the reader to block.
                final CompletableFuture<Void> writerFuture = CompletableFuture.runAsync(() -> {
                    try {
                        assertTrue(readerIsWaitingLatch.await(5, TimeUnit.SECONDS), "Reader did not start polling in time.");
                        outputStream.write(inputData.getBytes(UTF_8));
                        writerHasFinishedLatch.countDown();
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                // This read will block inside our custom poll() method until the writer provides data.
                final byte[] buffer = new byte[inputData.length()];
                final int bytesRead = inputStream.read(buffer, 0, buffer.length);

                assertEquals(inputData.length(), bytesRead);
                assertEquals(inputData, new String(buffer, 0, bytesRead, UTF_8));

                // Ensure the writer thread completed without exceptions.
                assertDoesNotThrow(() -> writerFuture.get(5, TimeUnit.SECONDS));
            }
        }

        @Test
        @DisplayName("read() should throw IllegalStateException when interrupted while waiting")
        void readShouldThrowExceptionWhenInterrupted() throws InterruptedException {
            try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(Duration.ofMinutes(1)).get()) {
                final CountDownLatch testFinishedLatch = new CountDownLatch(1);
                final Thread readerThread = new Thread(() -> {
                    assertThrows(IllegalStateException.class, inputStream::read, "read() should throw when thread is interrupted.");
                    assertTrue(Thread.currentThread().isInterrupted(), "Thread interrupted status should be true.");
                    testFinishedLatch.countDown();
                });

                readerThread.start();
                // Give the reader thread time to block in read()
                Thread.sleep(200);
                readerThread.interrupt();

                assertTrue(testFinishedLatch.await(5, TimeUnit.SECONDS), "Test did not complete in time.");
            }
        }

        @Test
        @DisplayName("read() should time out and return EOF if no data arrives")
        void readShouldTimeOutAndReturnEofWhenNoData() {
            final Duration timeout = Duration.ofMillis(200);
            try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(timeout).get()) {
                // Use assertTimeoutPreemptively to prevent test hangs.
                assertTimeoutPreemptively(timeout.plusMillis(300), () -> {
                    final long startTime = System.nanoTime();
                    assertEquals(IOUtils.EOF, inputStream.read(), "read() should return EOF on timeout.");
                    final long elapsedTime = System.nanoTime() - startTime;
                    assertTrue(elapsedTime >= timeout.toNanos(), "Stream should wait for at least the timeout duration.");
                });
            }
        }
    }

    @Nested
    @DisplayName("Argument and Error Handling")
    class ArgumentAndErrorHandlingTest {

        static Stream<Arguments> invalidReadArguments() {
            return Stream.of(
                Arguments.of("Offset too big", EMPTY_BYTE_ARRAY, 1, 0),
                Arguments.of("Offset negative", EMPTY_BYTE_ARRAY, -1, 0),
                Arguments.of("Length too big", EMPTY_BYTE_ARRAY, 0, 1),
                Arguments.of("Length negative", EMPTY_BYTE_ARRAY, 0, -1)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidReadArguments")
        @DisplayName("read(byte[], int, int) should throw for invalid arguments")
        void readWithInvalidArgumentsShouldThrowException(final String testName, final byte[] buffer, final int offset, final int length) {
            try (QueueInputStream inputStream = new QueueInputStream()) {
                assertThrows(IndexOutOfBoundsException.class, () -> inputStream.read(buffer, offset, length));
            }
        }

        @Test
        @DisplayName("read(byte[], int, int) should do nothing for zero-length read")
        void readWithZeroLengthShouldReturnZero() throws IOException {
            try (QueueInputStream inputStream = new QueueInputStream()) {
                final int bytesRead = inputStream.read(EMPTY_BYTE_ARRAY, 0, 0);
                assertEquals(0, bytesRead, "A zero-length read should return 0.");
            }
        }

        @Test
        @DisplayName("Builder should throw exception for negative timeout")
        void builderShouldThrowForNegativeTimeout() {
            assertThrows(IllegalArgumentException.class, () -> QueueInputStream.builder().setTimeout(Duration.ofMillis(-1)).get());
        }

        @Test
        @DisplayName("Builder should use default empty queue when given null")
        void builderShouldUseDefaultQueueForNull() throws IOException {
            try (QueueInputStream inputStream = QueueInputStream.builder().setBlockingQueue(null).get()) {
                assertNotNull(inputStream.getBlockingQueue(), "Queue should not be null.");
                assertTrue(inputStream.getBlockingQueue().isEmpty(), "Queue should be empty.");
            }
        }

        @Test
        @DisplayName("Builder should use default zero timeout when given null")
        void builderShouldUseDefaultTimeoutForNull() throws IOException {
            try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(null).get()) {
                assertEquals(Duration.ZERO, inputStream.getTimeout(), "Timeout should be Duration.ZERO.");
            }
        }
    }
}