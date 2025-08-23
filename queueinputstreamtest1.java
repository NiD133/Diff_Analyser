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

/**
 * Tests for {@link QueueInputStream}.
 */
@DisplayName("QueueInputStream Tests")
public class QueueInputStreamTest {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Provides a stream of test data with varying sizes, including empty, small, and large strings that cross
     * buffer boundaries.
     */
    public static Stream<Arguments> inputData() {
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

    @Nested
    @DisplayName("Read/Write Operations")
    class ReadWriteTest {

        @ParameterizedTest(name = "Input size: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        @DisplayName("Should read back all unbuffered data written")
        void unbufferedReadWrite(final String testData) throws IOException {
            try (QueueInputStream inputStream = new QueueInputStream();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
                writeUnbuffered(outputStream, testData);
                final String actualData = readUnbuffered(inputStream);
                assertEquals(testData, actualData);
            }
        }

        @ParameterizedTest(name = "Input size: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        @DisplayName("Should read back all unbuffered data written within a timeout")
        void unbufferedReadWriteWithTimeout(final String testData) throws IOException {
            final Duration timeout = Duration.ofMinutes(2);
            try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(timeout).get();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
                assertEquals(timeout, inputStream.getTimeout());
                writeUnbuffered(outputStream, testData);
                final String actualData = assertTimeout(Duration.ofSeconds(1), () -> readUnbuffered(inputStream, testData.length()));
                assertEquals(testData, actualData);
            }
        }

        @ParameterizedTest(name = "Input size: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        @DisplayName("Should read back data written through a buffered output stream")
        void bufferedWrites(final String testData) throws IOException {
            final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
            try (QueueInputStream inputStream = new QueueInputStream(queue);
                 BufferedOutputStream outputStream = new BufferedOutputStream(new QueueOutputStream(queue), DEFAULT_BUFFER_SIZE)) {
                outputStream.write(testData.getBytes(UTF_8));
                outputStream.flush();
                final String actualData = readUnbuffered(inputStream);
                assertEquals(testData, actualData);
            }
        }

        @ParameterizedTest(name = "Input size: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        @DisplayName("Should read data using a buffered input stream")
        void bufferedReads(final String testData) throws IOException {
            final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
            try (BufferedInputStream inputStream = new BufferedInputStream(new QueueInputStream(queue));
                 QueueOutputStream outputStream = new QueueOutputStream(queue)) {
                outputStream.write(testData.getBytes(UTF_8));
                final String actualData = IOUtils.toString(inputStream, UTF_8);
                assertEquals(testData, actualData);
            }
        }

        @ParameterizedTest(name = "Input size: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        @DisplayName("Should work correctly with both buffered input and output streams")
        void bufferedReadWrite(final String testData) throws IOException {
            final BlockingQueue<Integer> queue = new LinkedBlockingedBlockingQueue<>();
            try (BufferedInputStream inputStream = new BufferedInputStream(new QueueInputStream(queue));
                 BufferedOutputStream outputStream = new BufferedOutputStream(new QueueOutputStream(queue), DEFAULT_BUFFER_SIZE)) {
                outputStream.write(testData.getBytes(UTF_8));
                outputStream.flush();
                final String dataCopy = IOUtils.toString(inputStream, UTF_8);
                assertEquals(testData, dataCopy);
            }
        }

        @ParameterizedTest(name = "Input size: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        @DisplayName("Should correctly read data line by line")
        void readLineByLine(final String testData) throws IOException {
            final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
            try (QueueInputStream inputStream = QueueInputStream.builder().setBlockingQueue(queue).setTimeout(Duration.ofHours(1)).get();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
                doTestReadLineByLine(testData, inputStream, outputStream);
            }
        }
    }

    @Nested
    @DisplayName("Stream Lifecycle")
    class LifecycleTest {

        @Test
        @DisplayName("available() should always return 0 for an open stream")
        void availableOnOpenStream() throws IOException {
            try (InputStream inputStream = new QueueInputStream()) {
                // available() is always 0 because read() blocks until data is available.
                assertEquals(0, inputStream.available());
                IOUtils.toString(inputStream, UTF_8);
                assertEquals(0, inputStream.available());
            }
        }

        @Test
        @DisplayName("available() should return 0 for a closed stream")
        void availableOnClosedStream() throws IOException {
            final InputStream closedStream;
            // This try-with-resources block is intentionally used to create and then
            // automatically close the stream. We then test the behavior of the
            // stream *after* it has been closed.
            try (InputStream inputStream = new QueueInputStream()) {
                closedStream = inputStream;
            }
            assertEquals(0, closedStream.available());
        }

        @Test
        @DisplayName("read() on a closed stream should return EOF")
        void readOnClosedStream() throws IOException {
            final InputStream closedStream;
            // This try-with-resources block is intentionally used to create and then
            // automatically close the stream.
            try (InputStream inputStream = new QueueInputStream()) {
                closedStream = inputStream;
            }
            assertEquals(IOUtils.EOF, closedStream.read());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCaseTest {

        @Test
        @DisplayName("read(byte[], off, len) with negative offset should throw IndexOutOfBoundsException")
        void readWithNegativeOffsetShouldThrowException() {
            try (final QueueInputStream qis = new QueueInputStream()) {
                assertThrows(IndexOutOfBoundsException.class, () -> qis.read(EMPTY_BYTE_ARRAY, -1, 0));
            }
        }

        @Test
        @DisplayName("read(byte[], off, len) with offset larger than buffer length should throw IndexOutOfBoundsException")
        void readWithOffsetTooLargeShouldThrowException() {
            try (final QueueInputStream qis = new QueueInputStream()) {
                assertThrows(IndexOutOfBoundsException.class, () -> qis.read(EMPTY_BYTE_ARRAY, 1, 0));
            }
        }

        @Test
        @DisplayName("read(byte[], off, len) with negative length should throw IndexOutOfBoundsException")
        void readWithNegativeLengthShouldThrowException() {
            try (final QueueInputStream qis = new QueueInputStream()) {
                assertThrows(IndexOutOfBoundsException.class, () -> qis.read(EMPTY_BYTE_ARRAY, 0, -1));
            }
        }

        @Test
        @DisplayName("read(byte[], off, len) with length larger than buffer capacity should throw IndexOutOfBoundsException")
        void readWithLengthTooLargeShouldThrowException() {
            try (final QueueInputStream qis = new QueueInputStream()) {
                assertThrows(IndexOutOfBoundsException.class, () -> qis.read(EMPTY_BYTE_ARRAY, 0, 1));
            }
        }

        @Test
        @DisplayName("read(byte[], off, len) with zero length should return 0")
        void readWithZeroLength() throws IOException {
            try (final QueueInputStream qis = new QueueInputStream()) {
                assertEquals(0, qis.read(EMPTY_BYTE_ARRAY, 0, 0));
            }
        }
    }

    @Nested
    @DisplayName("Concurrency Scenarios")
    class ConcurrencyTest {

        @ParameterizedTest(name = "Input size: {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        @DisplayName("read(byte[]) should block until data is available")
        void readShouldBlockUntilDataIsAvailable(final String testData) throws IOException {
            assumeFalse(testData.isEmpty(), "Test is not applicable for empty input data");

            // Latches to synchronize the reader (consumer) and writer (producer) threads.
            final CountDownLatch readerIsBlockedLatch = new CountDownLatch(1);
            final CountDownLatch writerFinishedLatch = new CountDownLatch(1);

            // A custom queue to signal when the consumer's read() call blocks on poll().
            final BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>() {
                @Override
                public Integer poll(final long timeout, final TimeUnit unit) throws InterruptedException {
                    // Signal that the reader thread is now waiting for data.
                    readerIsBlockedLatch.countDown();
                    // Wait until the writer thread has finished writing data.
                    assertTrue(writerFinishedLatch.await(5, TimeUnit.SECONDS), "Writer did not finish in time");
                    return super.poll(timeout, unit);
                }
            };

            try (QueueInputStream inputStream = QueueInputStream.builder().setBlockingQueue(queue).setTimeout(Duration.ofHours(1)).get()) {
                final QueueOutputStream outputStream = inputStream.newQueueOutputStream();

                // Producer thread: waits for the reader to block, then writes data.
                final CompletableFuture<Void> writerFuture = CompletableFuture.runAsync(() -> {
                    try {
                        // Wait until the consumer thread calls poll() on the queue.
                        assertTrue(readerIsBlockedLatch.await(5, TimeUnit.SECONDS), "Reader did not block in time");
                        outputStream.write(testData.getBytes(UTF_8));
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        // Signal that writing is complete, unblocking the reader.
                        writerFinishedLatch.countDown();
                    }
                });

                // Consumer (main thread): attempts to read data, which should block.
                final byte[] buffer = new byte[testData.length()];
                final int bytesRead = inputStream.read(buffer, 0, buffer.length);

                // Assertions
                assertEquals(testData.length(), bytesRead);
                assertEquals(testData, new String(buffer, 0, bytesRead, UTF_8));

                // Ensure the writer thread completed without exceptions.
                assertDoesNotThrow(() -> writerFuture.get(5, TimeUnit.SECONDS));
            }
        }
    }

    // -- Helper Methods --

    private void doTestReadLineByLine(final String inputData, final InputStream inputStream, final OutputStream outputStream) throws IOException {
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

    private String readUnbuffered(final InputStream inputStream) throws IOException {
        return readUnbuffered(inputStream, Integer.MAX_VALUE);
    }

    private String readUnbuffered(final InputStream inputStream, final int maxBytes) throws IOException {
        if (maxBytes == 0) {
            return "";
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int n;
        while ((n = inputStream.read()) != -1) {
            byteArrayOutputStream.write(n);
            if (byteArrayOutputStream.size() >= maxBytes) {
                break;
            }
        }
        return byteArrayOutputStream.toString(UTF_8.name());
    }

    private void writeUnbuffered(final QueueOutputStream outputStream, final String inputData) throws IOException {
        final byte[] bytes = inputData.getBytes(UTF_8);
        outputStream.write(bytes, 0, bytes.length);
    }
}