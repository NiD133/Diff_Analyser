package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservableInputStream} focusing on observer notifications.
 */
public class ObservableInputStreamNotificationTest {

    /**
     * An observer that counts the total number of bytes observed through its callbacks.
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
     * Tests that when an ObservableInputStream is read in chunks using a custom-sized
     * buffer, the observer's data(byte[], int, int) method is called for each chunk.
     * It also verifies that other lifecycle callbacks (closed, finished) are invoked
     * correctly.
     */
    @Test
    void whenStreamIsCopiedWithCustomBuffer_thenObserverIsCalledForEveryChunk() throws IOException {
        // Arrange
        final int copyBufferSize = 2;
        final byte[] sourceData = IOUtils.byteArray(); // Creates a 4096-byte array
        final int streamSize = sourceData.length;

        final LengthObserver lengthObserver = new LengthObserver();
        final MethodCountObserver methodCountObserver = new MethodCountObserver();

        // Act
        long bytesCopied;
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(sourceData), lengthObserver, methodCountObserver)) {
            bytesCopied = IOUtils.copy(ois, NullOutputStream.INSTANCE, copyBufferSize);
        }

        // Assert
        assertEquals(streamSize, bytesCopied, "The number of bytes copied should match the source stream size.");

        final int expectedDataBufferCallbackCount = streamSize / copyBufferSize;

        assertAll("Observer callbacks should be invoked correctly",
            () -> assertEquals(streamSize, lengthObserver.getTotalBytes(),
                "LengthObserver should have counted all bytes."),
            () -> assertEquals(1, methodCountObserver.getClosedCount(),
                "closed() should be called once after the stream is closed."),
            () -> assertEquals(1, methodCountObserver.getFinishedCount(),
                "finished() should be called once when the end of the stream is reached."),
            () -> assertEquals(0, methodCountObserver.getErrorCount(),
                "error() should not be called in a successful read."),
            () -> assertEquals(0, methodCountObserver.getDataByteCount(),
                "data(int) for single-byte reads should not be called."),
            () -> assertEquals(expectedDataBufferCallbackCount, methodCountObserver.getDataBufferCount(),
                "data(byte[]) for buffer reads should be called for each chunk.")
        );
    }
}