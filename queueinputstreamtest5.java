package org.apache.commons.io.input;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_BYTE_ARRAY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.base.Stopwatch;
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
public class QueueInputStreamTest {

    /**
     * Provides a stream of strings with various lengths for parameterized tests,
     * covering empty, small, and buffer-sized edge cases.
     */
    public static Stream<Arguments> inputData() {
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
    @DisplayName("Tests for read(byte[], int, int)")
    class ReadByteArrayTests {

        private static Stream<Arguments> invalidReadArguments() {
            return Stream.of(
                Arguments.of("Offset too big for empty buffer", 1, 0),
                Arguments.of("Negative offset", -1, 0),
                Arguments.of("Length too big for empty buffer", 0, 1),
                Arguments.of("Negative length", 0, -1)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidReadArguments")
        void whenReadWithInvalidParameters_thenThrowsIndexOutOfBounds(final String testName, final int offset, final int length) {
            try (final QueueInputStream inputStream = new QueueInputStream()) {
                assertThrows(IndexOutOfBoundsException.class, () -> inputStream.read(EMPTY_BYTE_ARRAY, offset, length));
            }
        }

        @ParameterizedTest(name = "inputData length = {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        void whenDataNotImmediatelyAvailable_thenWaitsAndReadsSuccessfully(final String inputData) throws IOException {
            if (inputData.isEmpty()) {
                return; // Test requires data to be written.
            }

            final CountDownLatch readerReadyLatch = new CountDownLatch(1);
            final CountDownLatch writerDoneLatch = new CountDownLatch(1);

            // Use a custom queue to coordinate the reader and writer threads.
            final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>() {
                @Override
                public Integer poll(final long timeout, final TimeUnit unit) throws InterruptedException {
                    // Signal that the reader is now waiting for data.
                    readerReadyLatch.countDown();
                    // Wait until the writer has finished writing.
                    writerDoneLatch.await(1, TimeUnit.HOURS);
                    return super.poll(timeout, unit);
                }
            };

            try (QueueInputStream queueInputStream = QueueInputStream.builder().setBlockingQueue(queue).setTimeout(Duration.ofHours(1)).get()) {
                final QueueOutputStream queueOutputStream = queueInputStream.newQueueOutputStream();

                // Writer thread: waits for the reader to be ready, then writes data.
                final CompletableFuture<Void> writerFuture = CompletableFuture.runAsync(() -> {
                    try {
                        readerReadyLatch.await(1, TimeUnit.HOURS);
                        queueOutputStream.write(inputData.getBytes(UTF_8));
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        writerDoneLatch.countDown();
                    }
                });

                // Reader (main thread): attempts to read, which will block until the writer provides data.
                final byte[] buffer = new byte[inputData.length()];
                final int bytesRead = queueInputStream.read(buffer, 0, buffer.length);

                assertEquals(inputData.length(), bytesRead);
                final String outputData = new String(buffer, 0, bytesRead, UTF_8);
                assertEquals(inputData, outputData);

                // Ensure the writer thread completed without exceptions.
                assertDoesNotThrow(() -> writerFuture.get());
            }
        }
    }

    @Nested
    @DisplayName("Lifecycle and State Tests")
    class LifecycleAndStateTests {

        @Test
        void whenStreamIsClosed_thenReadReturnsEof() throws IOException {
            // This variable is needed to test the stream's behavior after it has been closed by the try-with-resources block.
            final InputStream closedStream;
            try (InputStream streamToClose = new QueueInputStream()) {
                closedStream = streamToClose;
            }
            assertEquals(IOUtils.EOF, closedStream.read());
        }

        @Test
        void whenStreamIsOpen_thenAvailableReturnsZero() throws IOException {
            try (InputStream inputStream = new QueueInputStream()) {
                // available() is always 0 because the only way to know if data is present is to block on read().
                assertEquals(0, inputStream.available());
            }
        }

        @Test
        void whenStreamIsClosed_thenAvailableReturnsZero() throws IOException {
            final InputStream closedStream;
            try (InputStream streamToClose = new QueueInputStream()) {
                closedStream = streamToClose;
            }
            assertEquals(0, closedStream.available());
        }
    }

    @Nested
    @DisplayName("Timeout Behavior Tests")
    class TimeoutTests {

        @Test
        @DisplayName("If no data is available, read() waits for the timeout period")
        void whenNoDataAvailable_thenReadWaitsForTimeout() {
            final Duration timeout = Duration.ofMillis(500);
            try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(timeout).get()) {
                final Stopwatch stopwatch = Stopwatch.createStarted();
                final String actualData = assertTimeout(Duration.ofSeconds(1), () -> readUnbuffered(inputStream, 3));
                stopwatch.stop();

                assertEquals("", actualData, "Should not read any data");
                assertTrue(stopwatch.elapsed(TimeUnit.MILLISECONDS) >= timeout.toMillis(),
                    "Read should block for at least the timeout duration. Elapsed: " + stopwatch);
            }
        }

        @ParameterizedTest(name = "inputData length = {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        void whenDataAvailable_thenReadWithTimeoutCompletesSuccessfully(final String inputData) {
            final Duration timeout = Duration.ofMinutes(1);
            try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(timeout).get();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {

                assertEquals(timeout, inputStream.getTimeout());
                writeUnbuffered(outputStream, inputData);

                final String actualData = assertTimeout(Duration.ofSeconds(1), () -> readUnbuffered(inputStream, inputData.length()));
                assertEquals(inputData, actualData);
            }
        }
    }

    @Nested
    @DisplayName("Integration: Read/Write scenarios")
    class IntegrationTests {

        @ParameterizedTest(name = "inputData length = {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        void unbufferedReadWrite_copiesDataCorrectly(final String inputData) throws IOException {
            try (QueueInputStream inputStream = new QueueInputStream();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
                writeUnbuffered(outputStream, inputData);
                final String actualData = readUnbuffered(inputStream);
                assertEquals(inputData, actualData);
            }
        }

        @ParameterizedTest(name = "inputData length = {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        void bufferedRead_copiesDataCorrectly(final String inputData) throws IOException {
            try (BufferedInputStream inputStream = new BufferedInputStream(new QueueInputStream());
                 QueueOutputStream outputStream = ((QueueInputStream) inputStream.getWrappedStream()).newQueueOutputStream()) {
                outputStream.write(inputData.getBytes(UTF_8));
                final String actualData = IOUtils.toString(inputStream, UTF_8);
                assertEquals(inputData, actualData);
            }
        }

        @ParameterizedTest(name = "inputData length = {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        void bufferedWrite_copiesDataCorrectly(final String inputData) throws IOException {
            try (QueueInputStream inputStream = new QueueInputStream();
                 BufferedOutputStream outputStream = new BufferedOutputStream(inputStream.newQueueOutputStream())) {
                outputStream.write(inputData.getBytes(UTF_8));
                outputStream.flush();
                final String actualData = readUnbuffered(inputStream);
                assertEquals(inputData, actualData);
            }
        }

        @ParameterizedTest(name = "inputData length = {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        void bufferedReadWrite_copiesDataCorrectly(final String inputData) throws IOException {
            try (QueueInputStream queueInputStream = new QueueInputStream();
                 BufferedInputStream inputStream = new BufferedInputStream(queueInputStream);
                 BufferedOutputStream outputStream = new BufferedOutputStream(queueInputStream.newQueueOutputStream())) {
                outputStream.write(inputData.getBytes(UTF_8));
                outputStream.flush();
                final String dataCopy = IOUtils.toString(inputStream, UTF_8);
                assertEquals(inputData, dataCopy);
            }
        }

        @ParameterizedTest(name = "inputData length = {0}")
        @MethodSource("org.apache.commons.io.input.QueueInputStreamTest#inputData")
        void lineByLineReadWrite_copiesDataCorrectly(final String inputData) throws IOException {
            try (QueueInputStream inputStream = QueueInputStream.builder().setTimeout(Duration.ofHours(1)).get();
                 QueueOutputStream outputStream = inputStream.newQueueOutputStream()) {
                doTestReadLineByLine(inputData, inputStream, outputStream);
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
                outputStream.flush(); // Ensure data is sent for each line
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
        int b;
        while ((b = inputStream.read()) != -1) {
            byteArrayOutputStream.write(b);
            if (byteArrayOutputStream.size() >= maxBytes) {
                break;
            }
        }
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
    }

    private void writeUnbuffered(final QueueOutputStream outputStream, final String inputData) throws IOException {
        final byte[] bytes = inputData.getBytes(UTF_8);
        outputStream.write(bytes);
    }
}