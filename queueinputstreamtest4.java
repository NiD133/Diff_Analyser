package org.apache.commons.io.input;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_BYTE_ARRAY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

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
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link QueueInputStream}.
 */
@DisplayName("QueueInputStream Tests")
class QueueInputStreamTest {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Provides test data of various sizes, including empty, small, and sizes around buffer boundaries.
     * This helps verify correct handling of different data chunks.
     */
    public static Stream<Arguments> inputDataProvider() {
        // @formatter:off
        return Stream.of(
            Arguments.of(""),
            Arguments.of("1"),
            Arguments.of("12"),
            Arguments.of("1234"),
            Arguments.of("12345678"),
            Arguments.of(StringUtils.repeat("A", 4095)), // Below common buffer size
            Arguments.of(StringUtils.repeat("A", 4096)), // At common buffer size
            Arguments.of(StringUtils.repeat("A", 4097)), // Above common buffer size
            Arguments.of(StringUtils.repeat("A", 8191)), // Below default buffer size
            Arguments.of(StringUtils.repeat("A", 8192)), // At default buffer size
            Arguments.of(StringUtils.repeat("A", 8193)), // Above default buffer size
            Arguments.of(StringUtils.repeat("A", 8192 * 4)) // Multiple buffer sizes
        );
        // @formatter:on
    }

    // --- Helper Methods ---

    private String readUnbuffered(final InputStream inputStream) throws IOException {
        return readUnbuffered(inputStream, Integer.MAX_VALUE);
    }

    private String readUnbuffered(final InputStream inputStream, final int maxBytes) throws IOException {
        if (maxBytes == 0) {
            return "";
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int b;
        while ((b = inputStream.read()) != -1) {
            byteArrayOutputStream.write(b);
            if (byteArrayOutputStream.size() >= maxBytes) {
                break;
            }
        }
        return byteArrayOutputStream.toString(UTF_8.name());
    }

    private void writeUnbuffered(final QueueOutputStream outputStream, final String data) throws IOException {
        final byte[] bytes = data.getBytes(UTF_8);
        outputStream.write(bytes);
    }

    private void verifyLineByLineReadWrite(final String inputData, final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final String[] expectedLines = inputData.split("\n");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8))) {
            for (final String expectedLine : expectedLines) {
                outputStream.write(expectedLine.getBytes(UTF_8));
                outputStream.write('\n');
                final String actualLine = reader.readLine();
                assertEquals(expectedLine, actualLine);
            }
        }
    }

    @Nested
    @DisplayName("Lifecycle and State")
    class LifecycleAndStateTests {

        @Test
        @DisplayName("available() should return 0 on a new, empty stream")
        void shouldReturnZeroAvailableOnOpenStream() throws IOException {
            try (InputStream inputStream = new QueueInputStream()) {
                // `available()` is always 0 because read() is blocking.
                assertEquals(0, inputStream.available());
            }
        }

        @Test
        @DisplayName("available() should return 0 after the stream is closed")
        void shouldReturnZeroAvailableAfterClose() throws IOException {
            final InputStream closedStream;
            // The stream is closed by the try-with-resources block
            try (InputStream inputStream = new QueueInputStream()) {
                closedStream = inputStream;
            }
            // The contract of QueueInputStream states that methods can be called after close.
            assertEquals(0, closedStream.available());
        }

        @Test
        @DisplayName("read() should return EOF (-1) after the stream is closed")
        void shouldReturnEofOnReadAfterClose() throws IOException {
            final InputStream closedStream;
            try (InputStream inputStream = new QueueInputStream()) {
                closedStream = inputStream;
            }
            assertEquals(IOUtils.EOF, closedStream.read());
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        static Stream<Arguments> invalidReadArguments() {
            return Stream.of(
                Arguments.of("Offset too big", EMPTY_BYTE_ARRAY, 1, 0),
                Arguments.of("Offset negative", EMPTY_BYTE_ARRAY, -1, 0),
                Arguments.of("Length too big", EMPTY_BYTE_ARRAY, 0, 1),
                Arguments.of("Length negative", EMPTY_BYTE_ARRAY, 0, -1)
            );
        }

        @ParameterizedTest(name = "[{index}] case: {0}")
        @MethodSource("invalidReadArguments")
        @DisplayName("read(byte[], int, int) should throw IndexOutOfBoundsException for invalid arguments")
        void shouldThrowExceptionForInvalidBulkReadParameters(final String description, final byte[] buffer, final int offset, final int length) {
            try (final QueueInputStream inputStream = new QueueInputStream()) {
                assertThrows(IndexOutOfBoundsException.class, () -> inputStream.read(buffer, offset, length));
            }
        }
    }

    @Nested
    @DisplayName("Read/Write Operations")
    class ReadWriteTests {

        @ParameterizedTest(name = "with data length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("should correctly read and write data without buffering")
        void shouldReadAndWriteDataUnbuffered(final String inputData) throws IOException {
            try (QueueInputStream inputStream = new QueueInputStream();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {

                writeUnbuffered(outputStream, inputData);
                final String actualData = readUnbuffered(inputStream);

                assertEquals(inputData, actualData);
            }
        }

        @ParameterizedTest(name = "with data length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("should correctly read and write data with a buffered input stream")
        void shouldReadAndWriteDataWithBufferedInput(final String inputData) throws IOException {
            try (QueueInputStream qis = new QueueInputStream();
                 BufferedInputStream inputStream = new BufferedInputStream(qis);
                 QueueOutputStream outputStream = qis.newQueueOutputStream()) {

                outputStream.write(inputData.getBytes(UTF_8));
                final String actualData = IOUtils.toString(inputStream, UTF_8);

                assertEquals(inputData, actualData);
            }
        }

        @ParameterizedTest(name = "with data length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("should correctly read and write data with a buffered output stream")
        void shouldReadAndWriteDataWithBufferedOutput(final String inputData) throws IOException {
            try (QueueInputStream inputStream = new QueueInputStream();
                 QueueOutputStream qos = inputStream.newQueueOutputStream();
                 BufferedOutputStream outputStream = new BufferedOutputStream(qos, DEFAULT_BUFFER_SIZE)) {

                outputStream.write(inputData.getBytes(UTF_8));
                outputStream.flush();
                final String actualData = readUnbuffered(inputStream);

                assertEquals(inputData, actualData);
            }
        }

        @ParameterizedTest(name = "with data length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("should correctly read and write data with buffered input and output streams")
        void shouldReadAndWriteDataWithBufferedStreams(final String inputData) throws IOException {
            try (QueueInputStream qis = new QueueInputStream();
                 BufferedInputStream inputStream = new BufferedInputStream(qis);
                 QueueOutputStream qos = qis.newQueueOutputStream();
                 BufferedOutputStream outputStream = new BufferedOutputStream(qos, DEFAULT_BUFFER_SIZE)) {

                outputStream.write(inputData.getBytes(UTF_8));
                outputStream.flush();
                final String actualData = IOUtils.toString(inputStream, UTF_8);

                assertEquals(inputData, actualData);
            }
        }

        @ParameterizedTest(name = "with data length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("should correctly read and write data line by line")
        void shouldReadAndWriteDataLineByLine(final String inputData) throws IOException {
            try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(Duration.ofHours(1)).get();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
                verifyLineByLineReadWrite(inputData, inputStream, outputStream);
            }
        }

        @Test
        @DisplayName("Test helper 'verifyLineByLineReadWrite' should work with files")
        void testHelperReadLineByLineWithFile(@TempDir final Path tempDir) throws IOException {
            final String testData = "line 1\nline 2\nline 3";
            final Path tempFile = Files.createFile(tempDir.resolve("test.txt"));
            try (InputStream inputStream = Files.newInputStream(tempFile);
                 OutputStream outputStream = Files.newOutputStream(tempFile)) {
                verifyLineByLineReadWrite(testData, inputStream, outputStream);
            }
        }
    }

    @Nested
    @DisplayName("Concurrency and Timeouts")
    class ConcurrencyAndTimeoutTests {

        @ParameterizedTest(name = "with data length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("should block and wait for data when reading from an empty stream")
        void shouldWaitForDataWhenReadingFromEmptyStream(final String inputData) throws IOException {
            assumeFalse(inputData.isEmpty(), "This test requires non-empty input data to verify reading.");

            final CountDownLatch readerIsWaitingLatch = new CountDownLatch(1);
            final CountDownLatch writerHasFinishedLatch = new CountDownLatch(1);

            // A custom queue to signal when the reader thread is about to block.
            final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>() {
                @Override
                public Integer poll(final long timeout, final TimeUnit unit) throws InterruptedException {
                    readerIsWaitingLatch.countDown(); // 1. Signal that the reader is now waiting.
                    writerHasFinishedLatch.await(1, TimeUnit.HOURS); // 3. Wait for the writer to finish.
                    return super.poll(timeout, unit);
                }
            };

            try (QueueInputStream qis = QueueInputStream.builder().setBlockingQueue(queue).setTimeout(Duration.ofHours(1)).get()) {
                final QueueOutputStream qos = qis.newQueueOutputStream();

                // Writer thread: waits for the reader to block, then writes data.
                final CompletableFuture<Void> writerFuture = CompletableFuture.runAsync(() -> {
                    try {
                        // 2. Wait until the reader thread has called poll() and is blocked.
                        assertTrue(readerIsWaitingLatch.await(1, TimeUnit.HOURS), "Reader did not start waiting in time.");
                        qos.write(inputData.getBytes(UTF_8));
                        writerHasFinishedLatch.countDown(); // 4. Signal that writing is complete.
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                // Reader (main) thread: attempts to read, which will block until the writer provides data.
                final byte[] buffer = new byte[inputData.length()];
                final int bytesRead = qis.read(buffer, 0, buffer.length); // This blocks.

                // Verification
                assertEquals(inputData.length(), bytesRead);
                final String actualData = new String(buffer, 0, bytesRead, UTF_8);
                assertEquals(inputData, actualData);
                assertDoesNotThrow(() -> writerFuture.get(), "Writer thread threw an exception.");
            }
        }

        @ParameterizedTest(name = "with data length: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputDataProvider")
        @DisplayName("should respect the configured timeout when reading")
        void shouldRespectTimeoutWhenReading(final String inputData) throws IOException {
            final Duration timeout = Duration.ofMinutes(2);
            try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(timeout).get();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {

                assertEquals(timeout, inputStream.getTimeout());
                writeUnbuffered(outputStream, inputData);
                final String actualData = assertTimeout(Duration.ofSeconds(1), () -> readUnbuffered(inputStream, inputData.length()));

                assertEquals(inputData, actualData);
            }
        }

        @Test
        @DisplayName("should throw IllegalStateException when a blocking read is interrupted")
        void shouldThrowExceptionWhenInterruptedDuringBlockingRead() throws Exception {
            try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(Duration.ofMinutes(2)).get()) {
                final AtomicBoolean wasInterruptedCorrectly = new AtomicBoolean(false);
                final CountDownLatch testFinishedLatch = new CountDownLatch(1);

                // 1. Start a background thread that will block on read().
                final Thread readerThread = new Thread(() -> {
                    try {
                        // 3. The read() call should be interrupted and throw.
                        assertThrows(IllegalStateException.class, () -> readUnbuffered(inputStream, 3));
                        // 4. Verify the thread's interrupted status is set.
                        assertTrue(Thread.currentThread().isInterrupted(), "Thread should be in interrupted state.");
                        wasInterruptedCorrectly.set(true);
                    } finally {
                        testFinishedLatch.countDown();
                    }
                });
                readerThread.setDaemon(true);
                readerThread.start();

                // Give the reader thread a moment to enter the blocking read() call.
                Thread.sleep(100);

                // 2. Interrupt the blocked reader thread from the main thread.
                readerThread.interrupt();

                // 5. Wait for the reader thread to finish its assertions.
                assertTrue(testFinishedLatch.await(5, TimeUnit.SECONDS), "Test verification did not complete in time.");
                assertTrue(wasInterruptedCorrectly.get(), "Verification assertions in reader thread failed.");
            }
        }
    }
}