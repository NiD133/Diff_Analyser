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

/**
 * Tests for QueueInputStream.
 *
 * The tests favor clarity:
 * - Consistent Arrange-Act-Assert structure.
 * - Clear names and comments explaining why specific behavior is asserted.
 * - Small helper methods to reduce noise around UTF-8 conversions and buffering.
 *
 * @see QueueOutputStreamTest
 */
class QueueInputStreamTest {

    // Common test constants
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final Duration SHORT_TIMEOUT = Duration.ofMillis(500);
    private static final Duration LONG_TIMEOUT = Duration.ofHours(1);
    private static final Duration TWO_MIN_TIMEOUT = Duration.ofMinutes(2);

    // --------------------------
    // Test input data provider
    // --------------------------

    static Stream<Arguments> inputData() {
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
    }

    // --------------------------
    // Argument validation
    // --------------------------

    @TestFactory
    DynamicTest[] bulkReadErrorHandlingTests() {
        // Each DynamicTest uses a fresh instance for isolation even though state is not mutated.
        return new DynamicTest[] {
            dynamicTest("read(byte[], off, len): offset > array length throws IndexOutOfBoundsException",
                () -> assertThrows(IndexOutOfBoundsException.class,
                    () -> new QueueInputStream().read(EMPTY_BYTE_ARRAY, 1, 0))),

            dynamicTest("read(byte[], off, len): negative offset throws IndexOutOfBoundsException",
                () -> assertThrows(IndexOutOfBoundsException.class,
                    () -> new QueueInputStream().read(EMPTY_BYTE_ARRAY, -1, 0))),

            dynamicTest("read(byte[], off, len): length overflows array throws IndexOutOfBoundsException",
                () -> assertThrows(IndexOutOfBoundsException.class,
                    () -> new QueueInputStream().read(EMPTY_BYTE_ARRAY, 0, 1))),

            dynamicTest("read(byte[], off, len): negative length throws IndexOutOfBoundsException",
                () -> assertThrows(IndexOutOfBoundsException.class,
                    () -> new QueueInputStream().read(EMPTY_BYTE_ARRAY, 0, -1)))
        };
    }

    @Test
    @DisplayName("read(b, 0, 0) returns 0")
    void bulkReadZeroLengthReturnsZero() {
        assertEquals(0, new QueueInputStream().read(EMPTY_BYTE_ARRAY, 0, 0));
    }

    @Test
    @DisplayName("Builder rejects negative timeout")
    void invalidArguments() {
        assertThrows(IllegalArgumentException.class,
            () -> QueueInputStream.builder().setTimeout(Duration.ofMillis(-1)).get());
    }

    // --------------------------
    // available() and close() behavior
    // --------------------------

    @SuppressWarnings("resource")
    @ParameterizedTest(name = "available() after close, inputData length={0}")
    @MethodSource("inputData")
    void availableIsZeroAfterClose(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        final InputStream closedStream;
        try (InputStream inputStream = new QueueInputStream(queue)) {
            closedStream = inputStream;
        }
        assertEquals(0, closedStream.available());
    }

    @ParameterizedTest(name = "available() while open, inputData length={0}")
    @MethodSource("inputData")
    @DisplayName("available() is 0 on an open QueueInputStream because read() blocks")
    void availableIsZeroWhenOpen(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (InputStream inputStream = new QueueInputStream(queue)) {
            assertEquals(0, inputStream.available());

            // Consume (will immediately return EOF because no data and timeout is zero)
            IOUtils.toString(inputStream, UTF_8);

            assertEquals(0, inputStream.available());
        }
    }

    @SuppressWarnings("resource")
    @ParameterizedTest(name = "read() after close returns EOF, inputData length={0}")
    @MethodSource("inputData")
    void readReturnsEofAfterClose(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        final InputStream closedStream;
        try (InputStream inputStream = new QueueInputStream(queue)) {
            closedStream = inputStream;
        }
        assertEquals(IOUtils.EOF, closedStream.read());
    }

    // --------------------------
    // Buffered read/write scenarios
    // --------------------------

    @ParameterizedTest(name = "Buffered input only, inputData length={0}")
    @MethodSource("inputData")
    void bufferedReads(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (BufferedInputStream in = new BufferedInputStream(new QueueInputStream(queue));
             QueueOutputStream out = new QueueOutputStream(queue)) {

            writeUtf8(out, inputData);
            assertEquals(inputData, readAllUtf8(in));
        }
    }

    @ParameterizedTest(name = "Buffered input and output, inputData length={0}")
    @MethodSource("inputData")
    void bufferedReadWrite(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (BufferedInputStream in = new BufferedInputStream(new QueueInputStream(queue));
             BufferedOutputStream out = new BufferedOutputStream(new QueueOutputStream(queue), DEFAULT_BUFFER_SIZE)) {

            writeUtf8(out, inputData);
            out.flush();
            assertEquals(inputData, readAllUtf8(in));
        }
    }

    @ParameterizedTest(name = "Buffered output only, inputData length={0}")
    @MethodSource("inputData")
    void bufferedWrites(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (QueueInputStream in = new QueueInputStream(queue);
             BufferedOutputStream out = new BufferedOutputStream(new QueueOutputStream(queue), DEFAULT_BUFFER_SIZE)) {

            writeUtf8(out, inputData);
            out.flush();
            assertEquals(inputData, readUnbuffered(in));
        }
    }

    // --------------------------
    // Unbuffered read/write scenarios
    // --------------------------

    @ParameterizedTest(name = "Unbuffered read/write, inputData length={0}")
    @MethodSource("inputData")
    void unbufferedReadWrite(final String inputData) throws IOException {
        try (QueueInputStream in = new QueueInputStream();
             QueueOutputStream out = in.newQueueOutputStream()) {

            writeUtf8(out, inputData);
            assertEquals(inputData, readUnbuffered(in));
        }
    }

    @ParameterizedTest(name = "Unbuffered with timeout, inputData length={0}")
    @MethodSource("inputData")
    void unbufferedReadWriteWithTimeout(final String inputData) throws IOException {
        try (QueueInputStream in = QueueInputStream.builder().setTimeout(TWO_MIN_TIMEOUT).get();
             QueueOutputStream out = in.newQueueOutputStream()) {

            assertEquals(TWO_MIN_TIMEOUT, in.getTimeout());
            writeUtf8(out, inputData);
            final String actual = assertTimeout(Duration.ofSeconds(1),
                () -> readUnbuffered(in, inputData.length()));
            assertEquals(inputData, actual);
        }
    }

    // --------------------------
    // Bulk read: waiting behavior
    // --------------------------

    @ParameterizedTest(name = "Bulk read waits for first byte, then consumes remainder, inputData length={0}")
    @MethodSource("inputData")
    void bulkReadWaitsForDataThenReadsRemainder(final String inputData) throws IOException {
        assumeFalse(inputData.isEmpty(), "This test requires non-empty input");

        final CountDownLatch startedPolling = new CountDownLatch(1);
        final CountDownLatch releasePoll = new CountDownLatch(1);

        // Custom queue that signals when poll starts; then blocks until we release it.
        final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>() {
            @Override
            public Integer poll(final long timeout, final TimeUnit unit) throws InterruptedException {
                startedPolling.countDown();
                releasePoll.await(1, TimeUnit.HOURS);
                return super.poll(timeout, unit);
            }
        };

        try (QueueInputStream in = QueueInputStream.builder()
            .setBlockingQueue(queue)
            .setTimeout(LONG_TIMEOUT) // ensure read(b,off,len) waits
            .get()) {

            final QueueOutputStream out = in.newQueueOutputStream();

            // Arrange: once the reader starts polling, write all data and release the poll.
            final CompletableFuture<Void> writer = CompletableFuture.runAsync(() -> {
                try {
                    startedPolling.await(1, TimeUnit.HOURS);
                    writeUtf8(out, inputData);
                    releasePoll.countDown();
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            });

            // Act
            final byte[] buf = new byte[inputData.length()];
            final int read = in.read(buf, 0, buf.length);

            // Assert
            assertEquals(inputData.length(), read);
            assertEquals(inputData, new String(buf, 0, read, UTF_8));
            assertDoesNotThrow(writer::join);
        }
    }

    // --------------------------
    // Timeout behavior
    // --------------------------

    @Test
    @DisplayName("If data is unavailable and timeout elapses, read() returns -1 and does not block indefinitely")
    void timeoutWhenDataUnavailable() throws IOException {
        // Arrange
        try (QueueInputStream in = QueueInputStream.builder().setTimeout(SHORT_TIMEOUT).get();
             QueueOutputStream ignored = in.newQueueOutputStream()) {

            final long startNanos = System.nanoTime();

            // Act: read a few bytes (nothing available)
            final String actual = assertTimeout(Duration.ofSeconds(1), () -> readUnbuffered(in, 3));

            final long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);

            // Assert
            assertEquals("", actual);
            assertTrue(elapsedMillis >= SHORT_TIMEOUT.toMillis(), () -> "Elapsed: " + elapsedMillis + "ms");
        }
    }

    @Test
    @DisplayName("If read is interrupted while waiting, IllegalStateException is thrown and interrupt flag is set")
    void timeoutInterrupted() throws Exception {
        try (QueueInputStream in = QueueInputStream.builder().setTimeout(TWO_MIN_TIMEOUT).get();
             QueueOutputStream ignored = in.newQueueOutputStream()) {

            final AtomicBoolean verified = new AtomicBoolean();
            final CountDownLatch done = new CountDownLatch(1);

            final Thread t = new Thread(() -> {
                assertThrows(IllegalStateException.class, () -> readUnbuffered(in, 3));
                assertTrue(Thread.currentThread().isInterrupted(), "Interrupt flag should be set");
                verified.set(true);
                done.countDown();
            });
            t.setDaemon(true);
            t.start();

            // Interrupt the read and wait for verification
            t.interrupt();
            done.await(500, TimeUnit.MILLISECONDS);
            assertTrue(verified.get(), "Reader thread did not complete verification in time");
        }
    }

    // --------------------------
    // Builder defaults and resets
    // --------------------------

    @Test
    @DisplayName("Builder resets null timeout and null queue to defaults")
    void builderResetsArgumentsToDefaults() throws IOException {
        try (QueueInputStream in = QueueInputStream.builder().setTimeout(null).get()) {
            assertEquals(Duration.ZERO, in.getTimeout());
            assertEquals(0, in.getBlockingQueue().size());
        }
        try (QueueInputStream in = QueueInputStream.builder().setBlockingQueue(null).get()) {
            assertEquals(Duration.ZERO, in.getTimeout());
            assertEquals(0, in.getBlockingQueue().size());
        }
    }

    // --------------------------
    // Line-by-line read utility: Compare behavior with files and with queues.
    // --------------------------

    @ParameterizedTest(name = "Read line-by-line using file IO, inputData length={0}")
    @MethodSource("inputData")
    void readLineByLineFile(final String inputData) throws IOException {
        final Path temp = Files.createTempFile(getClass().getSimpleName(), ".txt");
        try (InputStream in = Files.newInputStream(temp);
             OutputStream out = Files.newOutputStream(temp)) {
            doTestReadLineByLine(inputData, in, out);
        } finally {
            Files.delete(temp);
        }
    }

    @ParameterizedTest(name = "Read line-by-line using QueueInputStream, inputData length={0}")
    @MethodSource("inputData")
    void readLineByLineQueue(final String inputData) throws IOException {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        try (QueueInputStream in = QueueInputStream.builder()
            .setBlockingQueue(queue)
            .setTimeout(LONG_TIMEOUT)
            .get();
             QueueOutputStream out = in.newQueueOutputStream()) {

            doTestReadLineByLine(inputData, in, out);
        }
    }

    // --------------------------
    // Helpers
    // --------------------------

    private static void writeUtf8(final OutputStream out, final String s) throws IOException {
        out.write(s.getBytes(UTF_8));
    }

    private static String readAllUtf8(final InputStream in) throws IOException {
        return IOUtils.toString(in, UTF_8);
    }

    private static String readUnbuffered(final InputStream in) throws IOException {
        return readUnbuffered(in, Integer.MAX_VALUE);
    }

    /**
     * Reads up to maxBytes using single-byte reads, returning a UTF-8 string.
     */
    private static String readUnbuffered(final InputStream in, final int maxBytes) throws IOException {
        if (maxBytes == 0) {
            return "";
        }
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int n;
        while ((n = in.read()) != -1) {
            baos.write(n);
            if (baos.size() >= maxBytes) {
                break;
            }
        }
        return baos.toString(UTF_8.name());
    }

    /**
     * Writes the given data line-by-line with a trailing '\n' and verifies that readLine() returns the same lines.
     * Note: BufferedReader.readLine() strips the trailing newline.
     */
    private static void doTestReadLineByLine(final String inputData, final InputStream in, final OutputStream out) throws IOException {
        final String[] lines = inputData.split("\n");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, UTF_8))) {
            for (final String line : lines) {
                out.write(line.getBytes(UTF_8));
                out.write('\n'); // readLine() expects line terminators to be omitted in the returned string
                final String actual = reader.readLine();
                assertEquals(line, actual);
            }
        }
    }
}