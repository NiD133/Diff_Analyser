package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link ObservableInputStream}.
 * This suite focuses on observer notifications and error handling scenarios.
 */
public class ObservableInputStreamTest {

    // =================================================================================
    // Test Methods
    // =================================================================================

    @Test
    @DisplayName("read() on a broken stream should throw IOException")
    void read_onBrokenStream_throwsIOException() {
        try (ObservableInputStream ois = createBrokenObservableInputStream()) {
            assertThrows(IOException.class, ois::read, "Reading from a broken stream should fail.");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 128, IOUtils.DEFAULT_BUFFER_SIZE, IOUtils.DEFAULT_BUFFER_SIZE + 1})
    @DisplayName("Observers should be correctly notified during a full stream copy")
    void copy_notifiesObserversCorrectly(final int copyBufferSize) throws IOException {
        // Arrange
        final byte[] data = new byte[IOUtils.DEFAULT_BUFFER_SIZE]; // A buffer of known size
        final LengthObserver lengthObserver = new LengthObserver();
        final MethodCountObserver methodCountObserver = new MethodCountObserver();

        // Act
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(data), lengthObserver, methodCountObserver)) {
            IOUtils.copy(ois, NullOutputStream.INSTANCE, copyBufferSize);
        }

        // Assert
        final long expectedBufferDataCallbacks = (data.length + copyBufferSize - 1) / copyBufferSize;

        assertAll("Verify observer notifications",
            () -> assertEquals(data.length, lengthObserver.getTotalBytes(), "LengthObserver should count all bytes."),
            () -> assertEquals(1, methodCountObserver.getClosedCount(), "closed() should be called once."),
            () -> assertEquals(1, methodCountObserver.getFinishedCount(), "finished() should be called once."),
            () -> assertEquals(0, methodCountObserver.getErrorCount(), "error() should not be called."),
            () -> assertEquals(0, methodCountObserver.getSingleByteDataCount(), "data(int) should not be called for buffered copy."),
            () -> assertEquals(expectedBufferDataCallbacks, methodCountObserver.getBufferDataCount(), "data(byte[],...) should be called for each buffer read.")
        );
    }

    // =================================================================================
    // Helper Methods
    // =================================================================================

    /**
     * Creates an {@link ObservableInputStream} that wraps a {@link BrokenInputStream},
     * which throws an {@link IOException} on any read operation.
     *
     * @return A new instance of {@link ObservableInputStream} for error testing.
     */
    private ObservableInputStream createBrokenObservableInputStream() {
        return new ObservableInputStream(BrokenInputStream.INSTANCE);
    }

    // =================================================================================
    // Helper Inner Classes
    // =================================================================================

    /**
     * An observer that calculates the total number of bytes observed from the stream.
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
        private long bufferDataCount;
        private long singleByteDataCount;
        private long errorCount;
        private long finishedCount;

        @Override
        public void closed() {
            closedCount++;
        }

        @Override
        public void data(final byte[] buffer, final int offset, final int length) {
            bufferDataCount++;
        }

        @Override
        public void data(final int value) {
            singleByteDataCount++;
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

        public long getBufferDataCount() {
            return bufferDataCount;
        }

        public long getSingleByteDataCount() {
            return singleByteDataCount;
        }

        public long getErrorCount() {
            return errorCount;
        }

        public long getFinishedCount() {
            return finishedCount;
        }
    }
}