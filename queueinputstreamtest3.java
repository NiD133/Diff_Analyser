package org.apache.commons.io.input;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_BYTE_ARRAY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link QueueInputStream}.
 */
public class QueueInputStreamTest {

    /**
     * The default buffer size used in some tests, mirroring common buffer sizes.
     */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Provides string data for parameterized tests.
     * Includes empty, small, and large strings, with sizes around common buffer boundaries.
     */
    public static Stream<Arguments> stringTestCases() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of("1"),
            Arguments.of("1234"),
            Arguments.of(StringUtils.repeat("A", 4095)), // Below buffer size
            Arguments.of(StringUtils.repeat("A", 4096)), // At buffer size
            Arguments.of(StringUtils.repeat("A", 4097)), // Above buffer size
            Arguments.of(StringUtils.repeat("A", DEFAULT_BUFFER_SIZE - 1)),
            Arguments.of(StringUtils.repeat("A", DEFAULT_BUFFER_SIZE)),
            Arguments.of(StringUtils.repeat("A", DEFAULT_BUFFER_SIZE + 1)),
            Arguments.of(StringUtils.repeat("A", DEFAULT_BUFFER_SIZE * 2))
        );
    }

    @Test
    @DisplayName("read() should throw IndexOutOfBoundsException for a negative offset")
    void read_shouldThrowIndexOutOfBounds_whenOffsetIsNegative() {
        try (final QueueInputStream queueInputStream = new QueueInputStream()) {
            assertThrows(IndexOutOfBoundsException.class, () -> queueInputStream.read(EMPTY_BYTE_ARRAY, -1, 0));
        }
    }

    @Test
    @DisplayName("read() should throw IndexOutOfBoundsException for an offset larger than the buffer")
    void read_shouldThrowIndexOutOfBounds_whenOffsetIsTooLarge() {
        try (final QueueInputStream queueInputStream = new QueueInputStream()) {
            assertThrows(IndexOutOfBoundsException.class, () -> queueInputStream.read(EMPTY_BYTE_ARRAY, 1, 0));
        }
    }

    @Test
    @DisplayName("read() should throw IndexOutOfBoundsException for a negative length")
    void read_shouldThrowIndexOutOfBounds_whenLengthIsNegative() {
        try (final QueueInputStream queueInputStream = new QueueInputStream()) {
            assertThrows(IndexOutOfBoundsException.class, () -> queueInputStream.read(EMPTY_BYTE_ARRAY, 0, -1));
        }
    }

    @Test
    @DisplayName("read() should throw IndexOutOfBoundsException for a length larger than the buffer")
    void read_shouldThrowIndexOutOfBounds_whenLengthIsTooLarge() {
        try (final QueueInputStream queueInputStream = new QueueInputStream()) {
            assertThrows(IndexOutOfBoundsException.class, () -> queueInputStream.read(EMPTY_BYTE_ARRAY, 0, 1));
        }
    }

    @DisplayName("available() should return 0 after the stream is closed")
    @ParameterizedTest(name = "Input data: {0}")
    @MethodSource("stringTestCases")
    void available_shouldReturnZero_afterStreamIsClosed(final String inputData) throws IOException {
        // Arrange
        final InputStream closedInputStream;
        try (InputStream inputStream = new QueueInputStream()) {
            // This reference will be used after the stream is closed by the try-with-resources block.
            closedInputStream = inputStream;
        }

        // Act & Assert
        // We are intentionally checking the behavior of a closed stream.
        @SuppressWarnings("resource")
        final int available = closedInputStream.available();
        assertEquals(0, available);
    }

    @DisplayName("available() should always return 0 on an open stream because read() blocks")
    @ParameterizedTest(name = "Input data: {0}")
    @MethodSource("stringTestCases")
    void available_shouldReturnZero_onOpenStream(final String inputData) throws IOException {
        try (InputStream inputStream = new QueueInputStream()) {
            // Assert: available() is 0 before any operation.
            assertEquals(0, inputStream.available(), "available() should be 0 initially.");

            // Act: Attempt to read from the stream (which will be empty).
            IOUtils.toString(inputStream, UTF_8);

            // Assert: available() is still 0 after the read attempt.
            assertEquals(0, inputStream.available(), "available() should be 0 after reading.");
        }
    }

    @DisplayName("read() should return EOF after the stream is closed")
    @ParameterizedTest(name = "Input data: {0}")
    @MethodSource("stringTestCases")
    void read_shouldReturnEof_afterStreamIsClosed(final String inputData) throws IOException {
        // Arrange
        final InputStream closedInputStream;
        try (InputStream inputStream = new QueueInputStream()) {
            closedInputStream = inputStream;
        }

        // Act & Assert
        // We are intentionally checking the behavior of a closed stream.
        @SuppressWarnings("resource")
        final int result = closedInputStream.read();
        assertEquals(IOUtils.EOF, result);
    }

    @DisplayName("Data should be readable through a BufferedInputStream")
    @ParameterizedTest(name = "Input data: {0}")
    @MethodSource("stringTestCases")
    void testBufferedReads(final String inputData) throws IOException {
        // Arrange
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (BufferedInputStream inputStream = new BufferedInputStream(new QueueInputStream(queue));
             QueueOutputStream outputStream = new QueueOutputStream(queue)) {
            outputStream.write(inputData.getBytes(UTF_8));

            // Act
            final String actualData = IOUtils.toString(inputStream, UTF_8);

            // Assert
            assertEquals(inputData, actualData);
        }
    }

    @DisplayName("Data should be transferable with buffered streams")
    @ParameterizedTest(name = "Input data: {0}")
    @MethodSource("stringTestCases")
    void testBufferedReadWrite(final String inputData) throws IOException {
        // Arrange
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (BufferedInputStream inputStream = new BufferedInputStream(new QueueInputStream(queue));
             BufferedOutputStream outputStream = new BufferedOutputStream(new QueueOutputStream(queue), DEFAULT_BUFFER_SIZE)) {
            outputStream.write(inputData.getBytes(UTF_8));
            outputStream.flush(); // Ensure data is pushed to the underlying QueueOutputStream

            // Act
            final String actualData = IOUtils.toString(inputStream, UTF_8);

            // Assert
            assertEquals(inputData, actualData);
        }
    }

    @DisplayName("Data written via a BufferedOutputStream should be readable")
    @ParameterizedTest(name = "Input data: {0}")
    @MethodSource("stringTestCases")
    void testBufferedWrites(final String inputData) throws IOException {
        // Arrange
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (QueueInputStream inputStream = new QueueInputStream(queue);
             BufferedOutputStream outputStream = new BufferedOutputStream(new QueueOutputStream(queue), DEFAULT_BUFFER_SIZE)) {
            outputStream.write(inputData.getBytes(UTF_8));
            outputStream.flush();

            // Act
            final String actualData = readUnbuffered(inputStream);

            // Assert
            assertEquals(inputData, actualData);
        }
    }

    @DisplayName("Bulk read should wait for data to become available")
    @ParameterizedTest(name = "Input data: {0}")
    @MethodSource("stringTestCases")
    void testBulkReadWaiting(final String inputData) throws IOException {
        assumeFalse(inputData.isEmpty(), "This test requires non-empty input data to verify reading.");

        // Arrange: Set up a synchronized scenario where the reader must wait for the writer.
        final CountDownLatch readerIsWaitingForData = new CountDownLatch(1);
        final CountDownLatch writerHasFinished = new CountDownLatch(1);

        // A custom queue to coordinate the reader and writer threads.
        final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>() {
            @Override
            public Integer poll(final long timeout, final TimeUnit unit) throws InterruptedException {
                // Signal that the reader thread has called poll() and is now waiting.
                readerIsWaitingForData.countDown();
                // Wait until the writer thread signals that it has written the data.
                writerHasFinished.await(1, TimeUnit.HOURS);
                return super.poll(timeout, unit);
            }
        };

        try (QueueInputStream queueInputStream = QueueInputStream.builder().setBlockingQueue(queue).setTimeout(Duration.ofHours(1)).get()) {
            final QueueOutputStream queueOutputStream = queueInputStream.newQueueOutputStream();

            // Act: Start a producer thread to write data after the reader has started waiting.
            final CompletableFuture<Void> producerFuture = CompletableFuture.runAsync(() -> {
                try {
                    // Wait for the reader to be ready to poll.
                    readerIsWaitingForData.await(1, TimeUnit.HOURS);
                    // Write the data and signal that writing is complete.
                    queueOutputStream.write(inputData.getBytes(UTF_8));
                    writerHasFinished.countDown();
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            });

            // The read call will block until the producer writes data, coordinated by the latches.
            final byte[] data = new byte[inputData.length()];
            final int bytesRead = queueInputStream.read(data, 0, data.length);
            final String outputData = new String(data, 0, bytesRead, UTF_8);

            // Assert
            assertEquals(inputData.length(), bytesRead);
            assertEquals(inputData, outputData);
            assertDoesNotThrow(() -> producerFuture.get(), "Producer thread should complete without exceptions.");
        }
    }

    @DisplayName("Reading line-by-line from a file should work as a baseline")
    @ParameterizedTest(name = "Input data: {0}")
    @MethodSource("stringTestCases")
    void testReadLineByLineWithFile(final String inputData) throws IOException {
        // Arrange
        final Path tempFile = Files.createTempFile(getClass().getSimpleName(), ".txt");
        try (InputStream inputStream = Files.newInputStream(tempFile);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {
            // Act & Assert
            doTestReadLineByLine(inputData, inputStream, outputStream);
        } finally {
            Files.delete(tempFile);
        }
    }

    @DisplayName("Reading line-by-line from a queue should work correctly")
    @ParameterizedTest(name = "Input data: {0}")
    @MethodSource("stringTestCases")
    void testReadLineByLineWithQueue(final String inputData) throws IOException {
        // Arrange
        try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(Duration.ofHours(1)).get();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
            // Act & Assert
            doTestReadLineByLine(inputData, inputStream, outputStream);
        }
    }

    @DisplayName("Data should be transferable with unbuffered streams")
    @ParameterizedTest(name = "Input data: {0}")
    @MethodSource("stringTestCases")
    void testUnbufferedReadWrite(final String inputData) throws IOException {
        // Arrange
        try (QueueInputStream inputStream = new QueueInputStream();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
            writeUnbuffered(outputStream, inputData);

            // Act
            final String actualData = readUnbuffered(inputStream);

            // Assert
            assertEquals(inputData, actualData);
        }
    }

    @DisplayName("Data should be transferable within the specified timeout")
    @ParameterizedTest(name = "Input data: {0}")
    @MethodSource("stringTestCases")
    void testUnbufferedReadWriteWithTimeout(final String inputData) throws IOException {
        // Arrange
        final Duration timeout = Duration.ofMinutes(1);
        try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(timeout).get();
             QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
            assertEquals(timeout, inputStream.getTimeout());
            writeUnbuffered(outputStream, inputData);

            // Act & Assert
            final String actualData = assertTimeout(Duration.ofSeconds(5),
                () -> readUnbuffered(inputStream, inputData.length()),
                "Read should complete well within the timeout.");
            assertEquals(inputData, actualData);
        }
    }

    @Test
    @DisplayName("Builder should use a default queue when the provided queue is null")
    void builder_shouldUseDefaultQueue_whenSetToNull() throws IOException {
        try (QueueInputStream queueInputStream = QueueInputStream.builder().setBlockingQueue(null).get()) {
            assertNotNull(queueInputStream.getBlockingQueue());
            assertEquals(0, queueInputStream.getBlockingQueue().size());
        }
    }

    @Test
    @DisplayName("Builder should use a default timeout (ZERO) when the provided timeout is null")
    void builder_shouldUseDefaultTimeout_whenSetToNull() throws IOException {
        try (QueueInputStream queueInputStream = QueueInputStream.builder().setTimeout(null).get()) {
            assertEquals(Duration.ZERO, queueInputStream.getTimeout());
        }
    }

    // --- Helper Methods ---

    /**
     * Writes data to an output stream and reads it back from an input stream,
     * asserting that each line matches.
     */
    private void doTestReadLineByLine(final String inputData, final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final String[] lines = inputData.split("\n");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8))) {
            for (final String expectedLine : lines) {
                outputStream.write(expectedLine.getBytes(UTF_8));
                outputStream.write('\n');
                final String actualLine = reader.readLine();
                assertEquals(expectedLine, actualLine);
            }
        }
    }

    /**
     * Reads all available bytes from an input stream without buffering.
     */
    private String readUnbuffered(final InputStream inputStream) throws IOException {
        return readUnbuffered(inputStream, Integer.MAX_VALUE);
    }

    /**
     * Reads up to a maximum number of bytes from an input stream without buffering.
     */
    private String readUnbuffered(final InputStream inputStream, final int maxBytes) throws IOException {
        if (maxBytes == 0) {
            return "";
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int byteRead;
        while ((byteRead = inputStream.read()) != -1) {
            byteArrayOutputStream.write(byteRead);
            if (byteArrayOutputStream.size() >= maxBytes) {
                break;
            }
        }
        return byteArrayOutputStream.toString(UTF_8.name());
    }

    /**
     * Writes a string to a QueueOutputStream byte by byte.
     */
    private void writeUnbuffered(final QueueOutputStream outputStream, final String inputData) throws IOException {
        final byte[] bytes = inputData.getBytes(UTF_8);
        outputStream.write(bytes, 0, bytes.length);
    }
}