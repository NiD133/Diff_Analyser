package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.apache.commons.io.test.MessageDigestInputStreamTest; // Assuming this utility is available
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservableInputStream}, focusing on dynamic observer management.
 */
public class ObservableInputStreamTest {

    /**
     * A test-specific observer that counts method calls and captures the last
     * data received from single-byte read notifications.
     */
    private static class DataViewObserver extends MethodCountObserver {
        private int lastValue = -1;

        @Override
        public void data(final int value) throws IOException {
            super.data(value);
            lastValue = value;
        }
    }

    /**
     * A base observer for testing purposes that counts the number of times each
     * callback method is invoked.
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

        public long getFinishedCount() {
            return finishedCount;
        }
    }

    /**
     * Tests that an observer added after reading has started is only notified for
     * subsequent reads.
     */
    @Test
    void observerAddedMidStreamReceivesNotificationsForSubsequentReads() throws Exception {
        // Arrange: Set up test data, an observer, and the observable stream.
        final byte[] testData = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        final DataViewObserver dataObserver = new DataViewObserver();

        try (final ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(testData))) {
            // Act & Assert: Read one byte before adding the observer to ensure it's not notified.
            ois.read();
            assertEquals(-1, dataObserver.lastValue, "Observer should not be notified before it is added.");

            // Act: Add the observer mid-stream.
            ois.add(dataObserver);

            // Act & Assert: Read the rest of the stream byte by byte.
            // Start from the second byte (index 1) since the first was already read.
            for (int i = 1; i < testData.length; i++) {
                final int result = ois.read();

                // Assert that the stream returns the correct byte.
                assertEquals(testData[i], (byte) result, "Stream should return the correct byte.");
                // Assert that the observer was notified with the same byte.
                assertEquals(result, dataObserver.lastValue, "Observer should be notified with the value read.");
                // Assert that the stream has not yet finished or closed.
                assertEquals(0, dataObserver.getFinishedCount(), "finished() should not be called before EOF.");
                assertEquals(0, dataObserver.getClosedCount(), "closed() should not be called before close().");
            }

            // Act & Assert: Read at the end of the stream to trigger the 'finished' notification.
            final int eofResult = ois.read();
            assertEquals(-1, eofResult, "Stream should return EOF (-1) at the end.");
            assertEquals(1, dataObserver.getFinishedCount(), "finished() should be called once at EOF.");
            assertEquals(0, dataObserver.getClosedCount(), "closed() should not be called yet.");
        } // The stream is automatically closed here by the try-with-resources block.

        // Assert: Verify the 'closed' notification was sent after the stream was closed.
        assertEquals(1, dataObserver.getFinishedCount(), "finished() count should remain 1 after close.");
        assertEquals(1, dataObserver.getClosedCount(), "closed() should be called once after close().");
    }
}