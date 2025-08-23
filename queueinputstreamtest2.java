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

@DisplayName("QueueInputStream Tests")
public class QueueInputStreamTest {

    /**
     * Provides test data with descriptive names for various input sizes.
     */
    public static Stream<Arguments> createInputData() {
        // @formatter:off
        return Stream.of(
            Arguments.of("Empty string", ""),
            Arguments.of("1 char", "1"),
            Arguments.of("8 chars", "12345678"),
            // Test data sizes around a common buffer size (4096)
            Arguments.of("4095 chars (buffer - 1)", StringUtils.repeat("A", 4095)),
            Arguments.of("4096 chars (buffer)", StringUtils.repeat("A", 4096)),
            Arguments.of("4097 chars (buffer + 1)", StringUtils.repeat("A", 4097)),
            // Test data sizes around the default buffer size (8192)
            Arguments.of("8191 chars (default buffer - 1)", StringUtils.repeat("A", 8191)),
            Arguments.of("8192 chars (default buffer)", StringUtils.repeat("A", 8192)),
            Arguments.of("8193 chars (default buffer + 1)", StringUtils.repeat("A", 8193)),
            // Test with a larger data size
            Arguments.of("32768 chars (default buffer * 4)", StringUtils.repeat("A", 8192 * 4))
        );
        // @formatter:on
    }

    @Nested
    @DisplayName("Error Handling and Argument Validation")
    class ErrorHandlingTest {

        private final QueueInputStream queueInputStream = new QueueInputStream();

        @Test
        @DisplayName("read(byte[], int, int) should throw for negative offset")
        void readBulkWithNegativeOffset() {
            assertThrows(IndexOutOfBoundsException.class, () -> queueInputStream.read(EMPTY_BYTE_ARRAY, -1, 0));
        }

        @Test
        @DisplayName("read(byte[], int, int) should throw for offset larger than buffer")
        void readBulkWithOffsetTooLarge() {
            assertThrows(IndexOutOfBoundsException.class, () -> queueInputStream.read(EMPTY_BYTE_ARRAY, 1, 0));
        }

        @Test
        @DisplayName("read(byte[], int, int) should throw for negative length")
        void readBulkWithNegativeLength() {
            assertThrows(IndexOutOfBoundsException.class, () -> queueInputStream.read(EMPTY_BYTE_ARRAY, 0, -1));
        }

        @Test
        @DisplayName("read(byte[], int, int) should throw for length larger than buffer")
        void readBulkWithLengthTooLarge() {
            assertThrows(IndexOutOfBoundsException.class, () -> queueInputStream.read(EMPTY_BYTE_ARRAY, 0, 1));
        }

        @Test
        @DisplayName("builder().setTimeout() should throw for negative duration")
        void builderWithNegativeTimeout() {
            assertThrows(IllegalArgumentException.class, () -> QueueInputStream.builder().setTimeout(Duration.ofMillis(-1)).get(),
                "waitTime must not be negative");
        }
    }

    @Nested
    @DisplayName("Stream Lifecycle")
    class LifecycleTest {

        @DisplayName("available() should return 0 after stream is closed")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#createInputData")
        void testAvailableAfterClose(final String name, final String inputData) throws IOException {
            final InputStream closedStream;
            try (InputStream inputStream = new QueueInputStream()) {
                closedStream = inputStream;
            }
            // We are intentionally testing the behavior of a closed stream,
            // so using the 'closedStream' variable after the try-with-resources block is expected.
            @SuppressWarnings("resource")
            final int available = closedStream.available();
            assertEquals(0, available);
        }

        @DisplayName("read() should return EOF after stream is closed")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#createInputData")
        void testReadAfterClose(final String name, final String inputData) throws IOException {
            final InputStream closedStream;
            try (InputStream inputStream = new QueueInputStream()) {
                closedStream = inputStream;
            }
            // We are intentionally testing the behavior of a closed stream,
            // so using the 'closedStream' variable after the try-with-resources block is expected.
            @SuppressWarnings("resource")
            final int read = closedStream.read();
            assertEquals(IOUtils.EOF, read);
        }

        @DisplayName("available() should always return 0 on an open stream")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#createInputData")
        void testAvailableOnOpenStream(final String name, final String inputData) throws IOException {
            try (QueueInputStream inputStream = new QueueInputStream()) {
                // available() is always 0 because read() is designed to block until data is available.
                assertEquals(0, inputStream.available());
                // Even after a read operation, it should be 0.
                IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                assertEquals(0, inputStream.available());
            }
        }
    }

    @Nested
    @DisplayName("Unbuffered Read/Write Operations")
    class UnbufferedReadWriteTest {

        @DisplayName("should write and read data correctly")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#createInputData")
        void testUnbufferedReadWrite(final String name, final String inputData) throws IOException {
            try (QueueInputStream inputStream = new QueueInputStream();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {

                writeAll(outputStream, inputData);
                final String actualData = readAll(inputStream);

                assertEquals(inputData, actualData);
            }
        }

        @DisplayName("should write and read data correctly with a timeout")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#createInputData")
        void testUnbufferedReadWriteWithTimeout(final String name, final String inputData) throws IOException {
            final Duration timeout = Duration.ofMinutes(2);
            try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(timeout).get();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {

                assertEquals(timeout, inputStream.getTimeout());

                writeAll(outputStream, inputData);
                final String actualData = assertTimeout(Duration.ofSeconds(1), () -> readAll(inputStream, inputData.length()));

                assertEquals(inputData, actualData);
            }
        }
    }

    @Nested
    @DisplayName("Buffered Read/Write Operations")
    class BufferedReadWriteTest {

        private static final int BUFFER_SIZE = 8192;

        @DisplayName("should read correctly when wrapped in BufferedInputStream")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#createInputData")
        void testBufferedReads(final String name, final String inputData) throws IOException {
            final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
            try (BufferedInputStream inputStream = new BufferedInputStream(new QueueInputStream(queue));
                 QueueOutputStream outputStream = new QueueOutputStream(queue)) {

                outputStream.write(inputData.getBytes(StandardCharsets.UTF_8));
                final String actualData = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

                assertEquals(inputData, actualData);
            }
        }

        @DisplayName("should write correctly when wrapped in BufferedOutputStream")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#createInputData")
        void testBufferedWrites(final String name, final String inputData) throws IOException {
            final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
            try (QueueInputStream inputStream = new QueueInputStream(queue);
                 BufferedOutputStream outputStream = new BufferedOutputStream(new QueueOutputStream(queue), BUFFER_SIZE)) {

                outputStream.write(inputData.getBytes(StandardCharsets.UTF_8));
                outputStream.flush(); // Important for buffered streams
                final String actualData = readAll(inputStream);

                assertEquals(inputData, actualData);
            }
        }

        @DisplayName("should work correctly when both streams are buffered")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#createInputData")
        void testBufferedReadWrite(final String name, final String inputData) throws IOException {
            final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
            try (BufferedInputStream inputStream = new BufferedInputStream(new QueueInputStream(queue));
                 BufferedOutputStream outputStream = new BufferedOutputStream(new QueueOutputStream(queue), BUFFER_SIZE)) {

                outputStream.write(inputData.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                final String dataCopy = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

                assertEquals(inputData, dataCopy);
            }
        }
    }

    @Nested
    @DisplayName("Concurrency and Blocking Behavior")
    class ConcurrencyTest {

        @DisplayName("read(byte[]) should block until data is available")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#createInputData")
        void testBulkReadBlocksUntilDataAvailable(final String name, final String inputData) throws IOException {
            assumeFalse(inputData.isEmpty(), "This test is not meaningful for empty input");

            final CountDownLatch readerIsWaitingLatch = new CountDownLatch(1);
            final CountDownLatch writerHasFinishedLatch = new CountDownLatch(1);

            // A custom queue to control the interaction between reader and writer threads.
            final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>() {
                @Override
                public Integer poll(final long timeout, final TimeUnit unit) throws InterruptedException {
                    // Signal that the reader thread is now waiting for data.
                    readerIsWaitingLatch.countDown();
                    // Wait until the writer thread has finished writing.
                    assertTrue(writerHasFinishedLatch.await(1, TimeUnit.HOURS), "Writer did not finish in time");
                    return super.poll(timeout, unit);
                }
            };

            try (QueueInputStream queueInputStream = QueueInputStream.builder()
                    .setBlockingQueue(queue)
                    .setTimeout(Duration.ofHours(1))
                    .get()) {

                final QueueOutputStream queueOutputStream = queueInputStream.newQueueOutputStream();

                // In a separate thread, write data to the output stream.
                final CompletableFuture<Void> writerFuture = CompletableFuture.runAsync(() -> {
                    try {
                        // 1. Wait until the reader thread is blocked on poll().
                        assertTrue(readerIsWaitingLatch.await(1, TimeUnit.HOURS), "Reader did not start waiting in time");
                        // 2. Write the data.
                        queueOutputStream.write(inputData.getBytes(UTF_8));
                        // 3. Signal that writing is complete, which will unblock the reader.
                        writerHasFinishedLatch.countDown();
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                // The main thread acts as the reader. This call to read() will block.
                final byte[] data = new byte[inputData.length()];
                final int bytesRead = queueInputStream.read(data, 0, data.length);

                // Assert that all data was read correctly after being unblocked.
                assertEquals(inputData.length(), bytesRead);
                final String outputData = new String(data, 0, bytesRead, StandardCharsets.UTF_8);
                assertEquals(inputData, outputData);

                // Ensure the writer thread completed without exceptions.
                assertDoesNotThrow(() -> writerFuture.get());
            }
        }
    }

    @Nested
    @DisplayName("Line-by-Line Reading")
    class LineByLineReadTest {

        @DisplayName("should read line-by-line from a QueueInputStream")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#createInputData")
        void testReadLineByLineWithQueue(final String name, final String inputData) throws IOException {
            final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
            try (QueueInputStream inputStream = QueueInputStream.builder().setBlockingQueue(queue).setTimeout(Duration.ofHours(1)).get();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
                // This helper simulates a producer/consumer scenario, writing and reading one line at a time.
                assertReadsLineByLine(inputData, inputStream, outputStream);
            }
        }

        @DisplayName("should read line-by-line from a file (helper test)")
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#createInputData")
        void testReadLineByLineWithFile(final String name, final String inputData) throws IOException {
            final Path tempFile = Files.createTempFile(getClass().getSimpleName(), ".txt");
            try (InputStream inputStream = Files.newInputStream(tempFile);
                 OutputStream outputStream = Files.newOutputStream(tempFile)) {
                // This test validates the assertReadsLineByLine helper method itself using a standard file stream.
                assertReadsLineByLine(inputData, inputStream, outputStream);
            } finally {
                Files.delete(tempFile);
            }
        }
    }

    // =================================================================
    // Helper Methods
    // =================================================================

    private void assertReadsLineByLine(final String inputData, final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final String[] lines = inputData.split("\n");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8))) {
            for (final String line : lines) {
                outputStream.write(line.getBytes(UTF_8));
                outputStream.write('\n');
                final String actualLine = reader.readLine();
                assertEquals(line, actualLine);
            }
        }
    }

    private String readAll(final InputStream inputStream) throws IOException {
        return readAll(inputStream, Integer.MAX_VALUE);
    }

    private String readAll(final InputStream inputStream, final int maxBytes) throws IOException {
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
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
    }

    private void writeAll(final QueueOutputStream outputStream, final String inputData) throws IOException {
        final byte[] bytes = inputData.getBytes(StandardCharsets.UTF_8);
        outputStream.write(bytes, 0, bytes.length);
    }
}