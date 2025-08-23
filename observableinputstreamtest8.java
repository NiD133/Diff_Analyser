package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.apache.commons.io.test.MessageDigestInputStreamTest; // Assuming this is a shared test utility
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservableInputStream}.
 * This class focuses on verifying that observers are correctly notified during the stream's lifecycle.
 */
public class ObservableInputStreamTest {

    // region Test Helper Classes

    /**
     * An observer that counts invocations of its lifecycle methods.
     * Base class for more specific test observers.
     */
    private static class MethodCountObserver extends Observer {

        private long closedCount;
        private long dataBufferCount;
        private long dataCount;
        private long errorCount;
        private long finishedCount;

        @Override
        public void closed() throws IOException {
            closedCount++;
        }

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            dataBufferCount++;
        }

        @Override
        public void data(final int value) throws IOException {
            dataCount++;
        }

        @Override
        public void error(final IOException exception) throws IOException {
            errorCount++;
        }

        @Override
        public void finished() throws IOException {
            finishedCount++;
        }

        public long getClosedCount() {
            return closedCount;
        }

        public long getDataCount() {
            return dataCount;
        }

        public long getFinishedCount() {
            return finishedCount;
        }
    }

    /**
     * An observer that captures the last byte value passed to the data(int) method.
     */
    private static final class DataViewObserver extends MethodCountObserver {

        private int lastValue = -1;

        @Override
        public void data(final int value) throws IOException {
            super.data(value);
            lastValue = value;
        }
    }

    // endregion

    /**
     * Tests that when reading a stream byte-by-byte, the observer is correctly notified
     * for each byte read, as well as for the 'finished' and 'closed' events.
     */
    @Test
    void singleByteReadShouldNotifyObserverOfEachByteAndStreamLifecycle() throws IOException {
        // Arrange: Create test data, an observer, and the observable stream.
        final byte[] testData = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        final DataViewObserver observer = new DataViewObserver();

        // Assert: Verify the observer's initial state before any reading occurs.
        assertEquals(-1, observer.lastValue, "Observer's last value should be uninitialized.");
        assertEquals(0, observer.getDataCount(), "data() should not have been called yet.");
        assertEquals(0, observer.getFinishedCount(), "finished() should not have been called yet.");
        assertEquals(0, observer.getClosedCount(), "closed() should not have been called yet.");

        try (final InputStream observableStream = new ObservableInputStream(new ByteArrayInputStream(testData), observer)) {
            // Act & Assert: Read each byte and verify the stream's output and the observer's state.
            for (int i = 0; i < testData.length; i++) {
                final int byteRead = observableStream.read();

                // Assert that the stream proxies the data correctly.
                assertEquals(testData[i], (byte) byteRead, "Byte at index " + i + " should match source data.");
                // Assert that the observer was notified with the correct byte.
                assertEquals(byteRead, observer.lastValue, "Observer should be notified with the correct byte value.");
            }

            // Assert: After reading all data, check the observer's state before EOF is reached.
            assertEquals(testData.length, observer.getDataCount(), "data() should have been called for each byte.");
            assertEquals(0, observer.getFinishedCount(), "finished() should not be called before EOF is reached.");

            // Act & Assert: Read past the end of the stream to trigger the 'finished' notification.
            assertEquals(IOUtils.EOF, observableStream.read(), "Reading past the end should return EOF.");
            assertEquals(1, observer.getFinishedCount(), "finished() should be called once after EOF is reached.");
            assertEquals(0, observer.getClosedCount(), "closed() should not be called until the stream is closed.");
        } // The stream is automatically closed here by the try-with-resources statement.

        // Assert: After the stream is closed, verify the 'closed' notification was sent.
        assertEquals(1, observer.getFinishedCount(), "Finished count should remain 1 after close.");
        assertEquals(1, observer.getClosedCount(), "closed() should be called once the stream is closed.");
    }
}