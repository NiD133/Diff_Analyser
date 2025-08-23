package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link ObservableInputStream} focusing on the correct invocation of observer callbacks.
 */
class ObservableInputStreamTest {

    /**
     * An observer that counts the total number of bytes passed through the stream.
     */
    private static final class LengthObserver extends Observer {
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
        public void error(final IOException exception) {
            errorCount++;
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

    /**
     * Tests that observers are correctly notified when data is read from the stream
     * using IOUtils.copy with various buffer sizes.
     *
     * @param copyBufferSize The buffer size to be used by IOUtils.copy().
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 128, 1024, IOUtils.DEFAULT_BUFFER_SIZE})
    void whenStreamIsCopied_thenObserversAreNotifiedCorrectly(final int copyBufferSize) throws IOException {
        // Arrange
        final byte[] testData = new byte[IOUtils.DEFAULT_BUFFER_SIZE];
        final LengthObserver lengthObserver = new LengthObserver();
        final MethodCountObserver methodCountObserver = new MethodCountObserver();
        final InputStream source = new ByteArrayInputStream(testData);

        // Act
        try (ObservableInputStream ois = new ObservableInputStream(source, lengthObserver, methodCountObserver)) {
            final long bytesCopied = IOUtils.copy(ois, NullOutputStream.INSTANCE, copyBufferSize);
            assertEquals(testData.length, bytesCopied, "The number of bytes copied should match the source data length.");
        }

        // Assert
        // 1. Verify the total number of bytes observed.
        assertEquals(testData.length, lengthObserver.getTotalBytes(), "LengthObserver should count all bytes.");

        // 2. Verify the invocation counts for each observer method.
        assertEquals(1, methodCountObserver.getClosedCount(), "closed() should be called once.");
        assertEquals(1, methodCountObserver.getFinishedCount(), "finished() should be called once.");
        assertEquals(0, methodCountObserver.getErrorCount(), "error() should not be called.");
        assertEquals(0, methodCountObserver.getDataByteCount(), "data(int) should not be called by IOUtils.copy.");

        // The number of reads is the ceiling of (total size / buffer size).
        final long expectedDataBufferCalls = (testData.length + copyBufferSize - 1) / copyBufferSize;
        assertEquals(expectedDataBufferCalls, methodCountObserver.getDataBufferCount(),
                "data(byte[], int, int) should be called for each chunk read.");
    }
}