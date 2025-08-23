package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the data notification functionality of {@link ObservableInputStream}.
 *
 * This refactoring focuses on splitting a single, complex test case into multiple,
 * focused tests that are easier to read, understand, and maintain.
 */
public class ObservableInputStreamDataNotificationTest {

    private static final byte[] TEST_DATA = "0123456789".getBytes(StandardCharsets.UTF_8);

    private ByteArrayInputStream inputStream;
    private DataViewObserver observer;

    @BeforeEach
    void setUp() {
        inputStream = new ByteArrayInputStream(TEST_DATA);
        observer = new DataViewObserver();
    }

    /**
     * A test-specific observer that captures the arguments passed to the
     * data notification callbacks.
     */
    private static class DataViewObserver extends Observer {

        private byte[] buffer;
        private int offset = -1;
        private int length = -1;
        private int lastValue = -1;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) {
            this.buffer = buffer;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public void data(final int value) {
            lastValue = value;
        }

        public byte[] getBuffer() {
            return buffer;
        }

        public int getOffset() {
            return offset;
        }

        public int getLength() {
            return length;
        }
    }

    @Test
    @DisplayName("Reading from stream before adding an observer should not trigger notifications")
    void testReadBeforeAddingObserverDoesNotNotify() throws IOException {
        try (ObservableInputStream ois = new ObservableInputStream(inputStream)) {
            // Act: Read some data before the observer is attached
            final byte[] tempBuffer = new byte[2];
            ois.read(tempBuffer);

            // Assert: Observer state is still initial (null)
            assertNull(observer.getBuffer(), "Observer should not have been notified yet.");

            // Act: Add the observer and read more data
            ois.add(observer);
            final byte[] readBuffer = new byte[5];
            final int bytesRead = ois.read(readBuffer);

            // Assert: Observer is now notified with data from the second read
            assertEquals(5, bytesRead);
            assertSame(readBuffer, observer.getBuffer(), "Observer should be notified with the correct buffer instance.");
            assertEquals(0, observer.getOffset(), "Observer should be notified with the correct offset.");
            assertEquals(bytesRead, observer.getLength(), "Observer should be notified with the correct length.");
        }
    }

    @Test
    @DisplayName("read(byte[]) should notify observer with the correct buffer, offset, and length")
    void testReadWithBufferNotifiesObserver() throws IOException {
        // Arrange
        try (ObservableInputStream ois = new ObservableInputStream(inputStream, observer)) {
            final byte[] readBuffer = new byte[TEST_DATA.length];

            // Act
            final int bytesRead = ois.read(readBuffer);

            // Assert
            assertEquals(TEST_DATA.length, bytesRead);
            assertArrayEquals(TEST_DATA, readBuffer);

            // Assert observer notifications
            assertSame(readBuffer, observer.getBuffer(), "Observer should be notified with the correct buffer instance.");
            assertEquals(0, observer.getOffset(), "Observer should be notified with an offset of 0.");
            assertEquals(bytesRead, observer.getLength(), "Observer should be notified with the number of bytes read.");
        }
    }

    @Test
    @DisplayName("read(byte[], int, int) should notify observer with the correct buffer, offset, and length")
    void testReadWithBufferOffsetAndLengthNotifiesObserver() throws IOException {
        // Arrange
        try (ObservableInputStream ois = new ObservableInputStream(inputStream, observer)) {
            final byte[] readBuffer = new byte[TEST_DATA.length];
            final int offset = 2;
            final int lengthToRead = 5;

            // Act
            final int bytesRead = ois.read(readBuffer, offset, lengthToRead);

            // Assert
            assertEquals(lengthToRead, bytesRead);
            // Verify that the correct data was read into the correct part of the buffer
            final byte[] expectedDataInSlice = Arrays.copyOfRange(TEST_DATA, 0, lengthToRead);
            final byte[] actualDataInSlice = Arrays.copyOfRange(readBuffer, offset, offset + bytesRead);
            assertArrayEquals(expectedDataInSlice, actualDataInSlice);

            // Assert observer notifications
            assertSame(readBuffer, observer.getBuffer(), "Observer should be notified with the correct buffer instance.");
            assertEquals(offset, observer.getOffset(), "Observer should be notified with the correct offset.");
            assertEquals(bytesRead, observer.getLength(), "Observer should be notified with the number of bytes read.");
        }
    }
}