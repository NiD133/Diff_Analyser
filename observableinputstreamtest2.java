package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.test.CustomIOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link ObservableInputStream}.
 */
@DisplayName("ObservableInputStream")
class ObservableInputStreamTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 128, 1024, IOUtils.DEFAULT_BUFFER_SIZE})
    @DisplayName("should notify observers correctly on successful stream consumption")
    void observersAreNotifiedCorrectlyOnSuccessfulStreamConsumption(final int copyBufferSize) throws IOException {
        // Arrange
        final byte[] testData = IOUtils.byteArray(); // Creates a 4KB array of 'X's
        final LengthObserver lengthObserver = new LengthObserver();
        final MethodCountObserver methodCountObserver = new MethodCountObserver();

        // Act
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(testData), lengthObserver, methodCountObserver)) {
            IOUtils.copy(ois, NullOutputStream.INSTANCE, copyBufferSize);
        }

        // Assert
        assertEquals(testData.length, lengthObserver.getTotalBytes(), "LengthObserver should count all bytes.");
        assertEquals(1, methodCountObserver.getClosedCount(), "closed() should be called once.");
        assertEquals(1, methodCountObserver.getFinishedCount(), "finished() should be called once.");
        assertEquals(0, methodCountObserver.getErrorCount(), "error() should not be called on success.");
        assertEquals(0, methodCountObserver.getDataByteCount(), "data(int) should not be called by IOUtils.copy.");

        int expectedDataBufferInvocations = testData.length / copyBufferSize;
        assertEquals(expectedDataBufferInvocations, methodCountObserver.getDataBufferCount(),
            "data(byte[],...) should be called for each buffer read.");
    }

    @Test
    @DisplayName("should notify observers correctly when an error occurs during read")
    void observersAreNotifiedOfErrors() throws IOException {
        // Arrange
        final MethodCountObserver observer = new MethodCountObserver();
        // The try-with-resources ensures close() is called even if read() throws an exception.
        try (final ObservableInputStream ois = new ObservableInputStream(BrokenInputStream.INSTANCE, observer)) {
            // Act & Assert
            assertThrows(CustomIOException.class, () -> IOUtils.copy(ois, NullOutputStream.INSTANCE),
                "Reading from a broken stream should throw an exception.");
        }

        // Assert observer state after the operation
        assertEquals(1, observer.getErrorCount(), "error() should be called once.");
        assertEquals(1, observer.getClosedCount(), "closed() should be called once.");
        assertEquals(0, observer.getFinishedCount(), "finished() should not be called on error.");
        assertEquals(0, observer.getDataByteCount(), "data(int) should not be called.");
        assertEquals(0, observer.getDataBufferCount(), "data(byte[],...) should not be called.");
    }

    @Test
    @DisplayName("available() should return 0 after the stream is closed")
    void availableShouldReturnZeroAfterStreamIsClosed() throws Exception {
        // Arrange
        final InputStream closedStream;
        try (InputStream stream = new ObservableInputStream(new ByteArrayInputStream(IOUtils.byteArray()))) {
            assertTrue(stream.available() > 0, "Stream should have data available before close.");
            // Keep a reference to the stream after the try-with-resources block closes it.
            closedStream = stream;
        }

        // Act & Assert
        // The @SuppressWarnings("resource") on the original test was for this line,
        // as static analysis can't tell the stream is intentionally used after close.
        assertEquals(0, closedStream.available(), "available() should return 0 on a closed stream.");
    }

    //
    // Test-specific Observer implementations
    //

    /**
     * An observer that counts the total number of bytes passed to its data methods.
     */
    private static class LengthObserver extends Observer {
        private long totalBytes;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) {
            this.totalBytes += length;
        }

        @Override
        public void data(final int value) {
            totalBytes++;
        }

        public long getTotalBytes() {
            return totalBytes;
        }
    }

    /**
     * An observer that counts the number of times each callback method is invoked.
     */
    private static class MethodCountObserver extends Observer {
        private long closedCount;
        private long dataBufferCount;
        private long dataByteCount;
        private long errorCount;
        private long finishedCount;

        @Override
        public void closed() {
            closedCount++;
        }

        @Override
        public void data(final byte[] buffer, final int offset, final int length) {
            dataBufferCount++;
        }

        @Override
        public void data(final int value) {
            dataByteCount++;
        }



        @Override
        public void error(final IOException exception) throws IOException {
            errorCount++;
            // Re-throw to allow the application's error handling to proceed.
            throw exception;
        }

        @Override
        public void finished() {
            finishedCount++;
        }

        public long getClosedCount() {
            return closedCount;
        }

        public long getDataBufferCount() {
            return dataBufferCount;
        }

        public long getDataByteCount() {
            return dataByteCount;
        }

        public long getErrorCount() {
            return errorCount;
        }

        public long getFinishedCount() {
            return finishedCount;
        }
    }
}