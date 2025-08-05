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
 * Tests {@link ObservableInputStream}.
 */
class ObservableInputStreamTest {

    /**
     * Observer that captures data read events and counts method invocations.
     */
    private static final class DataCapturingObserver extends MethodCountingObserver {
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
     * Observer that counts the total number of bytes read.
     */
    private static final class ByteCountingObserver extends Observer {
        private long totalBytes;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            this.totalBytes += length;
        }

        @Override
        public void data(final int value) throws IOException {
            totalBytes++;
        }

        public long getTotalBytes() {
            return totalBytes;
        }
    }

    /**
     * Base observer that counts invocations of each callback method.
     */
    private static class MethodCountingObserver extends Observer {
        private long closedCount;
        private long dataBufferCount;
        private long dataByteCount;
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
            dataByteCount++;
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

    private ObservableInputStream createBrokenInputStream() {
        return new ObservableInputStream(BrokenInputStream.INSTANCE);
    }

    private InputStream createTestInputStream() {
        final byte[] buffer = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        return new ByteArrayInputStream(buffer);
    }

    private ObservableInputStream createObservableInputStream(InputStream source) {
        return new ObservableInputStream(source);
    }

    /**
     * Tests that the afterRead consumer is called after each read operation
     * and handles exceptions properly.
     */
    @Test
    void testAfterReadConsumer_IsInvokedAndHandlesExceptions() throws Exception {
        final AtomicBoolean consumerCalled = new AtomicBoolean();
        
        // Test successful invocation
        try (InputStream bounded = new ObservableInputStream.Builder()
                .setCharSequence("Hi")
                .setAfterRead(i -> consumerCalled.set(true))
                .get()) {
            IOUtils.consume(bounded);
        }
        assertTrue(consumerCalled.get());
        
        // Test exception propagation
        final String message = "test exception message";
        try (InputStream bounded = new ObservableInputStream.Builder()
                .setCharSequence("Hi")
                .setAfterRead(i -> {
                    throw new CustomIOException(message);
                })
                .get()) {
            assertEquals(message, 
                assertThrowsExactly(CustomIOException.class, () -> IOUtils.consume(bounded)).getMessage());
        }
    }

    /**
     * Tests available() returns 0 after stream is closed.
     */
    @SuppressWarnings("resource")
    @Test
    void testAvailable_ReturnsZeroAfterClose() throws Exception {
        final InputStream shadow;
        try (InputStream in = createObservableInputStream(createTestInputStream())) {
            assertTrue(in.available() > 0);
            shadow = in;
        }
        assertEquals(0, shadow.available());
    }

    /**
     * Tests available() returns positive value when stream has data.
     */
    @Test
    void testAvailable_ReturnsPositiveWhenStreamHasData() throws Exception {
        try (InputStream in = createObservableInputStream(createTestInputStream())) {
            assertTrue(in.available() > 0);
            assertNotEquals(IOUtils.EOF, in.read());
            assertTrue(in.available() > 0);
        }
    }

    /**
     * Tests read() propagates IOException from underlying broken stream.
     */
    @Test
    void testRead_WithBrokenInputStream_ThrowsIOException() throws IOException {
        try (ObservableInputStream ois = createBrokenInputStream()) {
            assertThrows(IOException.class, ois::read);
        }
    }

    /**
     * Tests read(byte[]) propagates IOException from underlying broken stream.
     */
    @Test
    void testReadByteArray_WithBrokenInputStream_ThrowsIOException() throws IOException {
        try (ObservableInputStream ois = createBrokenInputStream()) {
            assertThrows(IOException.class, () -> ois.read(new byte[1]));
        }
    }

    /**
     * Tests read(byte[], int, int) propagates IOException from underlying broken stream.
     */
    @Test
    void testReadByteArraySubsection_WithBrokenInputStream_ThrowsIOException() throws IOException {
        try (ObservableInputStream ois = createBrokenInputStream()) {
            assertThrows(IOException.class, () -> ois.read(new byte[2], 0, 1));
        }
    }

    /**
     * Tests data(int) callback is properly invoked when observer is added after initialization.
     */
    @Test
    void testDataByteCallback_WhenObserverAddedAfterCreation_IsInvoked() throws Exception {
        final byte[] buffer = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        final DataCapturingObserver observer = new DataCapturingObserver();
        
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(buffer))) {
            assertEquals(-1, observer.lastValue);
            
            // Read before adding observer - shouldn't notify
            ois.read();
            assertEquals(-1, observer.lastValue);
            assertEquals(0, observer.getFinishedCount());
            assertEquals(0, observer.getClosedCount());
            
            // Add observer and read remaining bytes
            ois.add(observer);
            for (int i = 1; i < buffer.length; i++) {
                final int result = ois.read();
                assertEquals((byte) result, buffer[i]);
                assertEquals(result, observer.lastValue);
                assertEquals(0, observer.getFinishedCount());
                assertEquals(0, observer.getClosedCount());
            }
            
            // Test EOF
            final int result = ois.read();
            assertEquals(-1, result);
            assertEquals(1, observer.getFinishedCount());
            assertEquals(0, observer.getClosedCount());
            
            // Test close notification
            ois.close();
            assertEquals(1, observer.getFinishedCount());
            assertEquals(1, observer.getClosedCount());
        }
    }

    /**
     * Tests data(int) callback is properly invoked when observer is passed to constructor.
     */
    @Test
    void testDataByteCallback_WhenObserverPassedToConstructor_IsInvoked() throws Exception {
        final byte[] buffer = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        final DataCapturingObserver observer = new DataCapturingObserver();
        
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(buffer), observer)) {
            assertEquals(-1, observer.lastValue);
            
            // First read should notify observer
            ois.read();
            assertNotEquals(-1, observer.lastValue);
            assertEquals(0, observer.getFinishedCount());
            assertEquals(0, observer.getClosedCount());
            
            // Read remaining bytes
            for (int i = 1; i < buffer.length; i++) {
                final int result = ois.read();
                assertEquals((byte) result, buffer[i]);
                assertEquals(result, observer.lastValue);
                assertEquals(0, observer.getFinishedCount());
                assertEquals(0, observer.getClosedCount());
            }
            
            // Test EOF
            final int result = ois.read();
            assertEquals(-1, result);
            assertEquals(1, observer.getFinishedCount());
            assertEquals(0, observer.getClosedCount());
            
            // Test close notification
            ois.close();
            assertEquals(1, observer.getFinishedCount());
            assertEquals(1, observer.getClosedCount());
        }
    }

    /**
     * Tests data(byte[], int, int) callback is properly invoked for different read operations.
     */
    @Test
    void testDataBytesCallback_WithVariousReadOperations_IsInvokedCorrectly() throws Exception {
        final byte[] buffer = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                ObservableInputStream ois = createObservableInputStream(bais)) {
            final DataCapturingObserver observer = new DataCapturingObserver();
            final byte[] readBuffer = new byte[23];
            
            // Read without observer - shouldn't notify
            ois.read(readBuffer);
            assertNull(observer.buffer);
            
            // Add observer and perform mixed read operations
            ois.add(observer);
            while (bais.available() > 0) {
                if (bais.available() >= 2048) {
                    final int result = ois.read(readBuffer);
                    if (result == -1) {
                        break;
                    }
                    assertEquals(readBuffer, observer.buffer);
                    assertEquals(0, observer.offset);
                    assertEquals(readBuffer.length, observer.length);
                } else {
                    final int res = Math.min(11, bais.available());
                    final int result = ois.read(readBuffer, 1, 11);
                    if (result == -1) {
                        break;
                    }
                    assertEquals(readBuffer, observer.buffer);
                    assertEquals(1, observer.offset);
                    assertEquals(res, observer.length);
                }
            }
            ois.close();
        }
    }

    /**
     * Tests getObservers() returns empty list when no observers are registered.
     */
    @Test
    void testGetObservers_WithNoObservers_ReturnsEmptyList() throws IOException {
        try (ObservableInputStream ois = new ObservableInputStream(new NullInputStream())) {
            assertTrue(ois.getObservers().isEmpty());
        }
    }

    /**
     * Tests getObservers() returns single observer when one is registered at construction.
     */
    @Test
    void testGetObservers_WithSingleObserver_ReturnsObserver() throws IOException {
        final DataCapturingObserver observer = new DataCapturingObserver();
        try (ObservableInputStream ois = new ObservableInputStream(new NullInputStream(), observer)) {
            assertEquals(observer, ois.getObservers().get(0));
        }
    }

    /**
     * Tests getObservers() maintains observer registration order.
     */
    @Test
    void testGetObservers_WithMultipleObservers_MaintainsRegistrationOrder() throws IOException {
        final DataCapturingObserver observer1 = new DataCapturingObserver();
        final DataCapturingObserver observer2 = new DataCapturingObserver();
        try (ObservableInputStream ois = new ObservableInputStream(new NullInputStream(), observer1, observer2)) {
            assertEquals(observer1, ois.getObservers().get(0));
            assertEquals(observer2, ois.getObservers().get(1));
        }
    }

    /**
     * Helper method to verify observer callbacks are correctly invoked
     * for different buffer sizes.
     */
    private void verifyObserverCallbacks(final int bufferSize) throws IOException {
        final byte[] buffer = IOUtils.byteArray();
        final ByteCountingObserver byteCounter = new ByteCountingObserver();
        final MethodCountingObserver methodCounter = new MethodCountingObserver();
        
        try (ObservableInputStream ois = new ObservableInputStream(
                new ByteArrayInputStream(buffer), byteCounter, methodCounter)) {
            IOUtils.copy(ois, NullOutputStream.INSTANCE, bufferSize);
        }
        
        // Verify counts
        assertEquals(IOUtils.DEFAULT_BUFFER_SIZE, byteCounter.getTotalBytes());
        assertEquals(1, methodCounter.getClosedCount());
        assertEquals(1, methodCounter.getFinishedCount());
        assertEquals(0, methodCounter.getErrorCount());
        assertEquals(0, methodCounter.getDataByteCount());
        assertEquals(buffer.length / bufferSize, methodCounter.getDataBufferCount());
    }

    @Test
    void testObserverCallbacks_WithBufferSize1_CountsCorrectly() throws Exception {
        verifyObserverCallbacks(1);
    }

    @Test
    void testObserverCallbacks_WithBufferSize2_CountsCorrectly() throws Exception {
        verifyObserverCallbacks(2);
    }

    @Test
    void testObserverCallbacks_WithDefaultBufferSize_CountsCorrectly() throws Exception {
        verifyObserverCallbacks(IOUtils.DEFAULT_BUFFER_SIZE);
    }

    /**
     * Tests read() after close for ByteArrayInputStream (should still be readable).
     */
    @Test
    void testReadAfterClose_WithByteArrayInputStream_StillReadable() throws Exception {
        try (InputStream in = createObservableInputStream(createTestInputStream())) {
            in.close();
            assertNotEquals(IOUtils.EOF, in.read());
        }
    }

    /**
     * Tests read() after close for ChannelInputStream (should throw IOException).
     */
    @SuppressWarnings("resource")
    @Test
    void testReadAfterClose_WithChannelInputStream_ThrowsIOException() throws Exception {
        try (InputStream in = createObservableInputStream(
                Files.newInputStream(Paths.get("src/test/resources/org/apache/commons/io/abitmorethan16k.txt")))) {
            in.close();
            assertThrows(IOException.class, in::read);
        }
    }
}