/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * 
 * The tests use small helper observers to record what notifications are sent and
 * to count how often each callback is invoked.
 */
class ObservableInputStreamTest {

    // --------------------
    // Test helpers
    // --------------------

    private static final int RANDOM_DATA_SIZE = IOUtils.DEFAULT_BUFFER_SIZE;
    private static final int READ_BUFFER_SIZE = 23;
    private static final int PARTIAL_READ_LEN = 11;
    private static final String RESOURCE_MORE_THAN_16K =
            "src/test/resources/org/apache/commons/io/abitmorethan16k.txt";

    /** Records callback invocations for easier assertions. */
    private static class CountingObserver extends Observer {
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

        long getClosedCount() {
            return closedCount;
        }

        long getDataBufferCount() {
            return dataBufferCount;
        }

        long getDataCount() {
            return dataCount;
        }

        long getErrorCount() {
            return errorCount;
        }

        long getFinishedCount() {
            return finishedCount;
        }
    }

    /** Records the last values passed to data(...) callbacks. */
    private static final class RecordingObserver extends CountingObserver {
        private byte[] lastBuffer;
        private int lastOffset = -1;
        private int lastLength = -1;
        private int lastValue = -1;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            super.data(buffer, offset, length);
            lastBuffer = buffer;
            lastOffset = offset;
            lastLength = length;
        }

        @Override
        public void data(final int value) throws IOException {
            super.data(value);
            lastValue = value;
        }
    }

    /** Sums up bytes observed through either data(int) or data(byte[],o,l). */
    private static final class ByteCountingObserver extends Observer {
        private long total;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            total += length;
        }

        @Override
        public void data(final int value) throws IOException {
            total++;
        }

        long getTotal() {
            return total;
        }
    }

    private static byte[] randomData() {
        return MessageDigestInputStreamTest.generateRandomByteStream(RANDOM_DATA_SIZE);
    }

    private static ObservableInputStream observableFrom(final InputStream in, final Observer... observers) {
        return new ObservableInputStream(in, observers);
    }

    private static ObservableInputStream observableFromRandomBytes(final Observer... observers) {
        return observableFrom(new ByteArrayInputStream(randomData()), observers);
    }

    private static ObservableInputStream brokenObservableInputStream() {
        return new ObservableInputStream(BrokenInputStream.INSTANCE);
    }

    // --------------------
    // Tests
    // --------------------

    /**
     * Verifies that the builder's afterRead callback is invoked and that any
     * exception thrown by it is propagated.
     */
    @Test
    void builder_afterReadConsumer_isInvoked_andExceptionsPropagate() throws Exception {
        final AtomicBoolean called = new AtomicBoolean(false);

        try (InputStream in = new ObservableInputStream.Builder()
                .setCharSequence("Hi")
                .setAfterRead(i -> called.set(true))
                .get()) {
            IOUtils.consume(in);
        }
        assertTrue(called.get(), "afterRead consumer should have been invoked");

        final String message = "test exception message";
        try (InputStream in = new ObservableInputStream.Builder()
                .setCharSequence("Hi")
                .setAfterRead(i -> { throw new CustomIOException(message); })
                .get()) {
            assertEquals(message,
                    assertThrowsExactly(CustomIOException.class, () -> IOUtils.consume(in)).getMessage());
        }
    }

    /**
     * available() should return 0 after close on a ByteArrayInputStream-based ObservableInputStream.
     */
    @SuppressWarnings("resource")
    @Test
    void available_returnsZero_afterClose() throws Exception {
        final InputStream shadow;
        try (InputStream in = observableFromRandomBytes()) {
            assertTrue(in.available() > 0);
            shadow = in;
        }
        assertEquals(0, shadow.available());
    }

    /**
     * available() should be positive before first read and remain positive after reading one byte.
     */
    @Test
    void available_isPositive_beforeAndAfterFirstRead() throws Exception {
        try (InputStream in = observableFromRandomBytes()) {
            assertTrue(in.available() > 0);
            assertNotEquals(IOUtils.EOF, in.read());
            assertTrue(in.available() > 0);
        }
    }

    /**
     * read() on a broken underlying stream should propagate IOException.
     */
    @Test
    void read_throws_onBrokenStream_singleByte() throws IOException {
        try (ObservableInputStream ois = brokenObservableInputStream()) {
            assertThrows(IOException.class, ois::read);
        }
    }

    /**
     * read(byte[]) on a broken underlying stream should propagate IOException.
     */
    @Test
    void read_throws_onBrokenStream_buffer() throws IOException {
        try (ObservableInputStream ois = brokenObservableInputStream()) {
            assertThrows(IOException.class, () -> ois.read(new byte[1]));
        }
    }

    /**
     * read(byte[],o,l) on a broken underlying stream should propagate IOException.
     */
    @Test
    void read_throws_onBrokenStream_subBuffer() throws IOException {
        try (ObservableInputStream ois = brokenObservableInputStream()) {
            assertThrows(IOException.class, () -> ois.read(new byte[2], 0, 1));
        }
    }

    /**
     * Observer#data(int) should be called for single-byte reads when the observer is added after some reads.
     */
    @Test
    void observerNotified_singleByteReads_whenAddedAfterFirstRead() throws Exception {
        final byte[] data = randomData();
        final RecordingObserver observer = new RecordingObserver();

        try (ObservableInputStream ois = observableFrom(new ByteArrayInputStream(data))) {
            // Before adding the observer, nothing should be recorded.
            assertEquals(-1, observer.lastValue);
            ois.read(); // consume one byte
            assertEquals(-1, observer.lastValue);
            assertEquals(0, observer.getFinishedCount());
            assertEquals(0, observer.getClosedCount());

            ois.add(observer);

            // From now on, every single-byte read should be observed.
            for (int i = 1; i < data.length; i++) {
                final int value = ois.read();
                assertEquals((byte) value, data[i], "read value should match underlying data");
                assertEquals(value, observer.lastValue, "observer should be notified with read value");
                assertEquals(0, observer.getFinishedCount());
                assertEquals(0, observer.getClosedCount());
            }

            // EOF should trigger finished, and close should then trigger closed.
            assertEquals(-1, ois.read());
            assertEquals(1, observer.getFinishedCount(), "finished should be signaled once at EOF");
            assertEquals(0, observer.getClosedCount());
            ois.close();
            assertEquals(1, observer.getClosedCount(), "closed should be signaled once on close");
        }
    }

    /**
     * Observer#data(int) should be called for single-byte reads when the observer is provided in the constructor.
     */
    @Test
    void observerNotified_singleByteReads_whenProvidedInConstructor() throws Exception {
        final byte[] data = randomData();
        final RecordingObserver observer = new RecordingObserver();

        try (ObservableInputStream ois = observableFrom(new ByteArrayInputStream(data), observer)) {
            assertEquals(-1, observer.lastValue);
            ois.read();
            assertNotEquals(-1, observer.lastValue, "observer should be notified on first read");
            assertEquals(0, observer.getFinishedCount());
            assertEquals(0, observer.getClosedCount());

            for (int i = 1; i < data.length; i++) {
                final int value = ois.read();
                assertEquals((byte) value, data[i], "read value should match underlying data");
                assertEquals(value, observer.lastValue, "observer should be notified with read value");
                assertEquals(0, observer.getFinishedCount());
                assertEquals(0, observer.getClosedCount());
            }

            assertEquals(-1, ois.read());
            assertEquals(1, observer.getFinishedCount(), "finished should be signaled once at EOF");
            assertEquals(0, observer.getClosedCount());
            ois.close();
            assertEquals(1, observer.getClosedCount(), "closed should be signaled once on close");
        }
    }

    /**
     * Observer#data(byte[],o,l) should be called with the actual buffer, offset, and length for bulk reads.
     */
    @Test
    void observerNotified_bufferReads_bulk() throws Exception {
        final byte[] data = randomData();
        final RecordingObserver observer = new RecordingObserver();
        final byte[] readBuffer = new byte[READ_BUFFER_SIZE];

        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObservableInputStream ois = observableFrom(bais, observer)) {

            int result;
            while ((result = ois.read(readBuffer)) != -1) {
                assertEquals(readBuffer, observer.lastBuffer, "observer should receive the same buffer instance");
                assertEquals(0, observer.lastOffset, "offset should be 0 for bulk reads");
                assertEquals(result, observer.lastLength, "length should match bytes read");
            }
            ois.close();
        }
    }

    /**
     * Observer#data(byte[],o,l) should be called with the actual buffer, offset, and length for partial reads.
     */
    @Test
    void observerNotified_bufferReads_partial() throws Exception {
        final byte[] data = randomData();
        final RecordingObserver observer = new RecordingObserver();
        final byte[] readBuffer = new byte[READ_BUFFER_SIZE];

        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObservableInputStream ois = observableFrom(bais, observer)) {

            int result;
            while ((result = ois.read(readBuffer, 1, PARTIAL_READ_LEN)) != -1) {
                assertEquals(readBuffer, observer.lastBuffer, "observer should receive the same buffer instance");
                assertEquals(1, observer.lastOffset, "offset should be the requested offset");
                assertEquals(result, observer.lastLength, "length should match bytes read");
            }
            ois.close();
        }
    }

    /**
     * getObservers() should return an empty list when no observers were registered.
     */
    @Test
    void getObservers_returnsEmptyList_whenNoneRegistered() throws IOException {
        try (ObservableInputStream ois = new ObservableInputStream(new NullInputStream())) {
            assertTrue(ois.getObservers().isEmpty());
        }
    }

    /**
     * getObservers() should contain the observer provided in the constructor.
     */
    @Test
    void getObservers_containsSingleObserver_whenOneRegistered() throws IOException {
        final RecordingObserver observer = new RecordingObserver();
        try (ObservableInputStream ois = new ObservableInputStream(new NullInputStream(), observer)) {
            assertEquals(observer, ois.getObservers().get(0));
        }
    }

    /**
     * getObservers() should preserve registration order.
     */
    @Test
    void getObservers_preservesOrder() throws IOException {
        final RecordingObserver o0 = new RecordingObserver();
        final RecordingObserver o1 = new RecordingObserver();
        try (ObservableInputStream ois = new ObservableInputStream(new NullInputStream(), o0, o1)) {
            assertEquals(o0, ois.getObservers().get(0));
            assertEquals(o1, ois.getObservers().get(1));
        }
    }

    /**
     * Copies the entire input with the given buffer size and verifies callback counts and totals.
     */
    private void assertNotificationCallbacksForBufferSize(final int bufferSize) throws IOException {
        final byte[] buffer = IOUtils.byteArray(); // size == IOUtils.DEFAULT_BUFFER_SIZE
        final ByteCountingObserver byteCounter = new ByteCountingObserver();
        final CountingObserver counter = new CountingObserver();

        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(buffer), byteCounter, counter)) {
            final long copied = IOUtils.copy(ois, NullOutputStream.INSTANCE, bufferSize);
            assertEquals(RANDOM_DATA_SIZE, copied, "all bytes should be copied");
        }

        assertEquals(RANDOM_DATA_SIZE, byteCounter.getTotal(), "observer should count all bytes");
        assertEquals(1, counter.getClosedCount(), "closed should be signaled once");
        assertEquals(1, counter.getFinishedCount(), "finished should be signaled once");
        assertEquals(0, counter.getErrorCount(), "no errors expected");
        assertEquals(0, counter.getDataCount(), "no single-byte reads expected");
        assertEquals(buffer.length / bufferSize, counter.getDataBufferCount(),
                "number of buffer reads should match copy iterations");
    }

    @Test
    void notificationCallbacks_withBufferSize1() throws Exception {
        assertNotificationCallbacksForBufferSize(1);
    }

    @Test
    void notificationCallbacks_withBufferSize2() throws Exception {
        assertNotificationCallbacksForBufferSize(2);
    }

    @Test
    void notificationCallbacks_withDefaultBufferSize() throws Exception {
        assertNotificationCallbacksForBufferSize(IOUtils.DEFAULT_BUFFER_SIZE);
    }

    /**
     * For ByteArrayInputStream, read() after close still works.
     */
    @Test
    void readAfterClose_stillWorks_forByteArrayInputStream() throws Exception {
        try (InputStream in = observableFromRandomBytes()) {
            in.close();
            assertNotEquals(IOUtils.EOF, in.read());
        }
    }

    /**
     * For ChannelInputStream (opened from a real file), read() after close throws.
     */
    @SuppressWarnings("resource")
    @Test
    void readAfterClose_throws_forChannelInputStream() throws Exception {
        try (InputStream in = observableFrom(Files.newInputStream(Paths.get(RESOURCE_MORE_THAN_16K)))) {
            in.close();
            assertThrows(IOException.class, in::read, "ChannelInputStream should throw when closed");
        }
    }
}