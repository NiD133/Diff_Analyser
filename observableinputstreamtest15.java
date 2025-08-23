package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservableInputStream} focusing on observer notifications during a successful stream read.
 */
public class ObservableInputStreamNotificationTest {

    /**
     * An observer that counts the total number of bytes reported by the 'data' callbacks.
     */
    private static final class LengthObserver extends Observer {
        private long totalBytesObserved;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) {
            this.totalBytesObserved += length;
        }

        @Override
        public void data(final int value) {
            totalBytesObserved++;
        }

        public long getTotalBytesObserved() {
            return totalBytesObserved;
        }
    }

    /**
     * An observer that counts the invocations of each callback method.
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
    }

    private byte[] sourceData;
    private LengthObserver lengthObserver;
    private MethodCountObserver methodCountObserver;

    @BeforeEach
    void setUp() {
        // Use a standard buffer size for the source data.
        sourceData = IOUtils.byteArray();
        lengthObserver = new LengthObserver();
        methodCountObserver = new MethodCountObserver();
    }

    @Test
    void shouldInvokeAllObserverCallbacksCorrectlyOnSuccessfulStreamConsumption() throws IOException {
        // Arrange: Create an ObservableInputStream with our observers.
        // The try-with-resources statement ensures the stream is closed automatically.
        try (final ObservableInputStream observableStream = new ObservableInputStream(
            new ByteArrayInputStream(sourceData), lengthObserver, methodCountObserver)) {

            // Act: Consume the entire stream using a buffered copy.
            final int copyBufferSize = IOUtils.DEFAULT_BUFFER_SIZE;
            final long bytesCopied = IOUtils.copy(observableStream, NullOutputStream.INSTANCE, copyBufferSize);

            // Assert: Verify the number of bytes copied is correct.
            assertEquals(sourceData.length, bytesCopied, "IOUtils.copy should report all bytes were copied.");
        }

        // Assert: Verify the observers were notified correctly.
        assertEquals(sourceData.length, lengthObserver.getTotalBytesObserved(),
            "LengthObserver should have observed all bytes from the stream.");

        // Verify lifecycle callback counts
        assertEquals(1, methodCountObserver.closedCount, "closed() should be called once by try-with-resources.");
        assertEquals(1, methodCountObserver.finishedCount, "finished() should be called once when EOF is reached.");
        assertEquals(0, methodCountObserver.errorCount, "error() should not be called on a successful read.");

        // Verify data callback counts
        assertEquals(0, methodCountObserver.dataByteCount,
            "data(int) should not be called when using a buffered copy method like IOUtils.copy.");

        // Since the source data size and copy buffer size are identical, IOUtils.copy
        // should read the entire stream in a single call to read(byte[]).
        final int expectedBufferReads = 1;
        assertEquals(expectedBufferReads, methodCountObserver.dataBufferCount,
            "data(byte[], int, int) should be called once for a single buffered read.");
    }
}