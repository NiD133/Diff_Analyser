package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.test.CustomIOException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ObservableInputStream}.
 */
class ObservableInputStreamTest {

    /**
     * Observer that tracks data read events.
     */
    private static final class DataViewObserver extends MethodCountObserver {
        private byte[] buffer;
        private int lastValue = -1;
        private int length = -1;
        private int offset = -1;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            this.buffer = buffer;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public void data(final int value) throws IOException {
            super.data(value);
            lastValue = value;
        }
    }

    /**
     * Observer that tracks the total length of data read.
     */
    private static final class LengthObserver extends Observer {
        private long total;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            this.total += length;
        }

        @Override
        public void data(final int value) throws IOException {
            total++;
        }

        public long getTotal() {
            return total;
        }
    }

    /**
     * Observer that counts method invocations.
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

        public long getDataBufferCount() {
            return dataBufferCount;
        }

        public long getDataCount() {
            return dataCount;
        }

        public long getErrorCount() {
            return errorCount;
        }

        public long getFinishedCount() {
            return finishedCount;
        }
    }

    private ObservableInputStream createBrokenObservableInputStream() {
        return new ObservableInputStream(BrokenInputStream.INSTANCE);
    }

    private InputStream createRandomInputStream() {
        final byte[] buffer = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        return new ByteArrayInputStream(buffer);
    }

    private ObservableInputStream createObservableInputStream(final InputStream origin) {
        return new ObservableInputStream(origin);
    }

    @Test
    void testAfterReadConsumer() throws Exception {
        final AtomicBoolean wasRead = new AtomicBoolean();
        try (InputStream inputStream = new ObservableInputStream.Builder()
                .setCharSequence("Hi")
                .setAfterRead(i -> wasRead.set(true))
                .get()) {
            IOUtils.consume(inputStream);
        }
        assertTrue(wasRead.get());

        final String exceptionMessage = "test exception message";
        try (InputStream inputStream = new ObservableInputStream.Builder()
                .setCharSequence("Hi")
                .setAfterRead(i -> {
                    throw new CustomIOException(exceptionMessage);
                })
                .get()) {
            assertEquals(exceptionMessage, assertThrowsExactly(CustomIOException.class, () -> IOUtils.consume(inputStream)).getMessage());
        }
    }

    @Test
    void testAvailableAfterClose() throws Exception {
        final InputStream shadowStream;
        try (InputStream inputStream = createRandomInputStream()) {
            assertTrue(inputStream.available() > 0);
            shadowStream = inputStream;
        }
        assertEquals(0, shadowStream.available());
    }

    @Test
    void testAvailableAfterOpen() throws Exception {
        try (InputStream inputStream = createRandomInputStream()) {
            assertTrue(inputStream.available() > 0);
            assertNotEquals(IOUtils.EOF, inputStream.read());
            assertTrue(inputStream.available() > 0);
        }
    }

    @Test
    void testBrokenInputStreamRead() throws IOException {
        try (ObservableInputStream observableInputStream = createBrokenObservableInputStream()) {
            assertThrows(IOException.class, observableInputStream::read);
        }
    }

    @Test
    void testBrokenInputStreamReadBuffer() throws IOException {
        try (ObservableInputStream observableInputStream = createBrokenObservableInputStream()) {
            assertThrows(IOException.class, () -> observableInputStream.read(new byte[1]));
        }
    }

    @Test
    void testBrokenInputStreamReadSubBuffer() throws IOException {
        try (ObservableInputStream observableInputStream = createBrokenObservableInputStream()) {
            assertThrows(IOException.class, () -> observableInputStream.read(new byte[2], 0, 1));
        }
    }

    @Test
    void testDataByteCalled_add() throws Exception {
        final byte[] buffer = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        final DataViewObserver observer = new DataViewObserver();
        try (ObservableInputStream observableInputStream = new ObservableInputStream(new ByteArrayInputStream(buffer))) {
            assertEquals(-1, observer.lastValue);
            observableInputStream.read();
            assertEquals(-1, observer.lastValue);
            assertEquals(0, observer.getFinishedCount());
            assertEquals(0, observer.getClosedCount());
            observableInputStream.add(observer);
            for (int i = 1; i < buffer.length; i++) {
                final int result = observableInputStream.read();
                assertEquals((byte) result, buffer[i]);
                assertEquals(result, observer.lastValue);
                assertEquals(0, observer.getFinishedCount());
                assertEquals(0, observer.getClosedCount());
            }
            final int result = observableInputStream.read();
            assertEquals(-1, result);
            assertEquals(1, observer.getFinishedCount());
            assertEquals(0, observer.getClosedCount());
            observableInputStream.close();
            assertEquals(1, observer.getFinishedCount());
            assertEquals(1, observer.getClosedCount());
        }
    }

    @Test
    void testDataByteCalled_ctor() throws Exception {
        final byte[] buffer = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        final DataViewObserver observer = new DataViewObserver();
        try (ObservableInputStream observableInputStream = new ObservableInputStream(new ByteArrayInputStream(buffer), observer)) {
            assertEquals(-1, observer.lastValue);
            observableInputStream.read();
            assertNotEquals(-1, observer.lastValue);
            assertEquals(0, observer.getFinishedCount());
            assertEquals(0, observer.getClosedCount());
            for (int i = 1; i < buffer.length; i++) {
                final int result = observableInputStream.read();
                assertEquals((byte) result, buffer[i]);
                assertEquals(result, observer.lastValue);
                assertEquals(0, observer.getFinishedCount());
                assertEquals(0, observer.getClosedCount());
            }
            final int result = observableInputStream.read();
            assertEquals(-1, result);
            assertEquals(1, observer.getFinishedCount());
            assertEquals(0, observer.getClosedCount());
            observableInputStream.close();
            assertEquals(1, observer.getFinishedCount());
            assertEquals(1, observer.getClosedCount());
        }
    }

    @Test
    void testDataBytesCalled() throws Exception {
        final byte[] buffer = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
                ObservableInputStream observableInputStream = createObservableInputStream(byteArrayInputStream)) {
            final DataViewObserver observer = new DataViewObserver();
            final byte[] readBuffer = new byte[23];
            assertNull(observer.buffer);
            observableInputStream.read(readBuffer);
            assertNull(observer.buffer);
            observableInputStream.add(observer);
            while (true) {
                if (byteArrayInputStream.available() >= 2048) {
                    final int result = observableInputStream.read(readBuffer);
                    if (result == -1) {
                        observableInputStream.close();
                        break;
                    }
                    assertEquals(readBuffer, observer.buffer);
                    assertEquals(0, observer.offset);
                    assertEquals(readBuffer.length, observer.length);
                } else {
                    final int res = Math.min(11, byteArrayInputStream.available());
                    final int result = observableInputStream.read(readBuffer, 1, 11);
                    if (result == -1) {
                        observableInputStream.close();
                        break;
                    }
                    assertEquals(readBuffer, observer.buffer);
                    assertEquals(1, observer.offset);
                    assertEquals(res, observer.length);
                }
            }
        }
    }

    @Test
    void testGetObservers0() throws IOException {
        try (ObservableInputStream observableInputStream = new ObservableInputStream(new NullInputStream())) {
            assertTrue(observableInputStream.getObservers().isEmpty());
        }
    }

    @Test
    void testGetObservers1() throws IOException {
        final DataViewObserver observer = new DataViewObserver();
        try (ObservableInputStream observableInputStream = new ObservableInputStream(new NullInputStream(), observer)) {
            assertEquals(observer, observableInputStream.getObservers().get(0));
        }
    }

    @Test
    void testGetObserversOrder() throws IOException {
        final DataViewObserver observer1 = new DataViewObserver();
        final DataViewObserver observer2 = new DataViewObserver();
        try (ObservableInputStream observableInputStream = new ObservableInputStream(new NullInputStream(), observer1, observer2)) {
            assertEquals(observer1, observableInputStream.getObservers().get(0));
            assertEquals(observer2, observableInputStream.getObservers().get(1));
        }
    }

    private void testNotificationCallbacks(final int bufferSize) throws IOException {
        final byte[] buffer = IOUtils.byteArray();
        final LengthObserver lengthObserver = new LengthObserver();
        final MethodCountObserver methodCountObserver = new MethodCountObserver();
        try (ObservableInputStream observableInputStream = new ObservableInputStream(new ByteArrayInputStream(buffer), lengthObserver, methodCountObserver)) {
            assertEquals(IOUtils.DEFAULT_BUFFER_SIZE, IOUtils.copy(observableInputStream, NullOutputStream.INSTANCE, bufferSize));
        }
        assertEquals(IOUtils.DEFAULT_BUFFER_SIZE, lengthObserver.getTotal());
        assertEquals(1, methodCountObserver.getClosedCount());
        assertEquals(1, methodCountObserver.getFinishedCount());
        assertEquals(0, methodCountObserver.getErrorCount());
        assertEquals(0, methodCountObserver.getDataCount());
        assertEquals(buffer.length / bufferSize, methodCountObserver.getDataBufferCount());
    }

    @Test
    void testNotificationCallbacksBufferSize1() throws Exception {
        testNotificationCallbacks(1);
    }

    @Test
    void testNotificationCallbacksBufferSize2() throws Exception {
        testNotificationCallbacks(2);
    }

    @Test
    void testNotificationCallbacksBufferSizeDefault() throws Exception {
        testNotificationCallbacks(IOUtils.DEFAULT_BUFFER_SIZE);
    }

    @Test
    void testReadAfterClose_ByteArrayInputStream() throws Exception {
        try (InputStream inputStream = createRandomInputStream()) {
            inputStream.close();
            assertNotEquals(IOUtils.EOF, inputStream.read());
        }
    }

    @Test
    void testReadAfterClose_ChannelInputStream() throws Exception {
        try (InputStream inputStream = createObservableInputStream(Files.newInputStream(Paths.get("src/test/resources/org/apache/commons/io/abitmorethan16k.txt")))) {
            inputStream.close();
            assertThrows(IOException.class, inputStream::read);
        }
    }
}