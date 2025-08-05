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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.test.CustomIOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.function.Executable;

/**
 * Tests for {@link ObservableInputStream}.
 */
class ObservableInputStreamTest {

    private static final int TEST_DATA_SIZE = 1024;
    private static byte[] TEST_DATA;

    @BeforeAll
    static void setupClass() {
        TEST_DATA = new byte[TEST_DATA_SIZE];
        new Random().nextBytes(TEST_DATA);
    }

    //-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~
    // Test-specific Observer implementations
    //-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~

    /** An observer that counts invocations of its methods. */
    private static class MethodCountObserver extends Observer {
        private long closedCount, dataBufferCount, dataCount, errorCount, finishedCount;

        @Override public void closed() { closedCount++; }
        @Override public void data(final byte[] b, final int o, final int l) { dataBufferCount++; }
        @Override public void data(final int value) { dataCount++; }
        @Override public void error(final IOException e) { errorCount++; }
        @Override public void finished() { finishedCount++; }

        public long getClosedCount() { return closedCount; }
        public long getDataBufferCount() { return dataBufferCount; }
        public long getDataCount() { return dataCount; }
        public long getErrorCount() { return errorCount; }
        public long getFinishedCount() { return finishedCount; }
    }

    /** An observer that captures the data passed to it for verification. */
    private static final class DataViewObserver extends MethodCountObserver {
        private byte[] capturedBuffer;
        private int capturedOffset = -1;
        private int capturedLength = -1;
        private int lastByte = -1;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) {
            super.data(buffer, offset, length);
            this.capturedBuffer = buffer;
            this.capturedOffset = offset;
            this.capturedLength = length;
        }

        @Override
        public void data(final int value) {
            super.data(value);
            this.lastByte = value;
        }
    }

    /** An observer that calculates the total number of bytes observed. */
    private static final class LengthObserver extends Observer {
        private long total;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) { this.total += length; }
        @Override
        public void data(final int value) { total++; }
        public long getTotal() { return total; }
    }

    //-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~
    // Test Methods
    //-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~

    @Test
    @DisplayName("Observer should be notified for single-byte reads when added via constructor")
    void shouldNotifyObserverForSingleByteReads_whenAddedViaConstructor() throws IOException {
        // Arrange
        final DataViewObserver observer = new DataViewObserver();
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(TEST_DATA), observer)) {
            assertEquals(-1, observer.lastByte, "Observer should not have received data before read");

            // Act & Assert
            for (int i = 0; i < TEST_DATA.length; i++) {
                final int result = ois.read();
                assertEquals(TEST_DATA[i], (byte) result, "Read byte should match test data");
                assertEquals(result, observer.lastByte, "Observer should be notified with the same byte");
            }

            // Act & Assert on EOF
            assertEquals(-1, ois.read(), "Stream should be at EOF");
            assertEquals(1, observer.getFinishedCount(), "finished() should be called once at EOF");
            assertEquals(0, observer.getClosedCount(), "closed() should not be called yet");
        }

        // Assert after close
        assertEquals(1, observer.getFinishedCount(), "finished() count should remain 1 after close");
        assertEquals(1, observer.getClosedCount(), "closed() should be called once after close");
    }

    @Test
    @DisplayName("Observer should be notified for single-byte reads after being added dynamically")
    void shouldNotifyObserverForSingleByteReads_whenAddedDynamically() throws IOException {
        // Arrange
        final DataViewObserver observer = new DataViewObserver();
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(TEST_DATA))) {
            // Read one byte before adding the observer
            final int firstByte = ois.read();
            assertEquals(TEST_DATA[0], (byte) firstByte);
            assertEquals(-1, observer.lastByte, "Observer should not be notified before being added");

            // Act: Add the observer
            ois.add(observer);

            // Assert: Read remaining bytes and verify notifications
            for (int i = 1; i < TEST_DATA.length; i++) {
                final int result = ois.read();
                assertEquals(TEST_DATA[i], (byte) result);
                assertEquals(result, observer.lastByte, "Observer should be notified for subsequent reads");
            }
        }
    }

    @Test
    @DisplayName("Observer should be notified correctly for a full buffer read")
    void shouldNotifyObserverForFullBufferRead() throws IOException {
        // Arrange
        final DataViewObserver observer = new DataViewObserver();
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(TEST_DATA))) {
            ois.add(observer);
            final byte[] readBuffer = new byte[128];

            // Act
            final int bytesRead = ois.read(readBuffer);

            // Assert
            assertEquals(readBuffer.length, bytesRead);
            assertEquals(readBuffer, observer.capturedBuffer, "Observer should receive the same buffer instance");
            assertEquals(0, observer.capturedOffset, "Offset should be 0 for read(byte[])");
            assertEquals(readBuffer.length, observer.capturedLength, "Length should match bytes read");
        }
    }

    @Test
    @DisplayName("Observer should be notified correctly for a partial buffer read")
    void shouldNotifyObserverForPartialBufferRead() throws IOException {
        // Arrange
        final DataViewObserver observer = new DataViewObserver();
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(TEST_DATA))) {
            ois.add(observer);
            final byte[] readBuffer = new byte[128];
            final int offset = 10;
            final int length = 50;

            // Act
            final int bytesRead = ois.read(readBuffer, offset, length);

            // Assert
            assertEquals(length, bytesRead);
            assertEquals(readBuffer, observer.capturedBuffer, "Observer should receive the same buffer instance");
            assertEquals(offset, observer.capturedOffset, "Observer should receive the correct offset");
            assertEquals(length, observer.capturedLength, "Observer should receive the correct length");
        }
    }

    @ParameterizedTest(name = "For copy buffer size = {0}")
    @ValueSource(ints = {1, 128, 1024, 4096})
    @DisplayName("Should invoke correct callbacks when stream is fully consumed")
    void shouldInvokeCorrectCallbacksWhenStreamIsFullyConsumed(final int copyBufferSize) throws IOException {
        // Arrange
        final LengthObserver lengthObserver = new LengthObserver();
        final MethodCountObserver methodCountObserver = new MethodCountObserver();
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(TEST_DATA), lengthObserver, methodCountObserver)) {
            // Act
            final long bytesCopied = IOUtils.copy(ois, NullOutputStream.INSTANCE, copyBufferSize);

            // Assert
            assertEquals(TEST_DATA_SIZE, bytesCopied);
        }

        // Assert after close
        assertEquals(TEST_DATA_SIZE, lengthObserver.getTotal(), "LengthObserver should count all bytes");
        assertEquals(1, methodCountObserver.getClosedCount(), "closed() should be called once");
        assertEquals(1, methodCountObserver.getFinishedCount(), "finished() should be called once");
        assertEquals(0, methodCountObserver.getErrorCount(), "error() should not be called");
        
        if (copyBufferSize == 1) {
            assertEquals(TEST_DATA_SIZE, methodCountObserver.getDataCount(), "data(int) should be called for each byte");
            assertEquals(0, methodCountObserver.getDataBufferCount(), "data(byte[]) should not be called");
        } else {
            assertEquals(0, methodCountObserver.getDataCount(), "data(int) should not be called");
            final long expectedBufferCalls = (long) Math.ceil((double) TEST_DATA_SIZE / copyBufferSize);
            assertEquals(expectedBufferCalls, methodCountObserver.getDataBufferCount(), "data(byte[]) call count should match buffer size");
        }
    }

    static Stream<Executable> brokenStreamReadOperations() {
        final ObservableInputStream ois = new ObservableInputStream(BrokenInputStream.INSTANCE);
        return Stream.of(
            ois::read,
            () -> ois.read(new byte[1]),
            () -> ois.read(new byte[2], 0, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("brokenStreamReadOperations")
    @DisplayName("Read operations on a broken stream should throw IOException")
    void readOnBrokenStreamShouldThrowException(final Executable readOperation) {
        // Act & Assert
        assertThrows(IOException.class, readOperation);
    }

    @Test
    @DisplayName("getObservers should return an empty list for a new stream")
    void getObservers_shouldReturnEmptyListForNewStream() throws IOException {
        // Arrange
        try (ObservableInputStream ois = new ObservableInputStream(new NullInputStream())) {
            // Act & Assert
            assertTrue(ois.getObservers().isEmpty());
        }
    }

    @Test
    @DisplayName("getObservers should return observers provided in constructor")
    void getObservers_shouldReturnObserversFromConstructor() throws IOException {
        // Arrange
        final Observer observer1 = new DataViewObserver();
        final Observer observer2 = new DataViewObserver();
        try (ObservableInputStream ois = new ObservableInputStream(new NullInputStream(), observer1, observer2)) {
            // Act
            final var observers = ois.getObservers();
            // Assert
            assertEquals(2, observers.size());
            assertEquals(observer1, observers.get(0));
            assertEquals(observer2, observers.get(1));
        }
    }

    @Test
    @DisplayName("Builder's afterRead consumer should be invoked on read")
    void builderAfterReadConsumer_shouldBeInvoked() throws Exception {
        // Arrange
        final AtomicBoolean wasCalled = new AtomicBoolean(false);
        try (InputStream ois = new ObservableInputStream.Builder()
                .setCharSequence("Hi")
                .setAfterRead(i -> wasCalled.set(true))
                .get()) {
            // Act
            IOUtils.consume(ois);
        }
        // Assert
        assertTrue(wasCalled.get(), "The afterRead consumer should have been called");
    }

    @Test
    @DisplayName("Builder's afterRead consumer should propagate exceptions")
    void builderAfterReadConsumer_shouldPropagateException() {
        // Arrange
        final String exceptionMessage = "test exception";
        final InputStream ois = new ObservableInputStream.Builder()
                .setCharSequence("Hi")
                .setAfterRead(i -> { throw new CustomIOException(exceptionMessage); })
                .get();

        // Act & Assert
        final var thrown = assertThrows(CustomIOException.class, () -> IOUtils.consume(ois));
        assertEquals(exceptionMessage, thrown.getMessage());
    }
    
    @Test
    @DisplayName("available() should return a positive value on an open stream")
    void available_shouldReturnPositiveValueOnOpenStream() throws Exception {
        // Arrange
        try (InputStream in = new ObservableInputStream(new ByteArrayInputStream(TEST_DATA))) {
            // Assert
            assertTrue(in.available() > 0, "available() should be > 0 before reading");
            // Act
            in.read();
            // Assert
            assertTrue(in.available() > 0, "available() should be > 0 after a partial read");
        }
    }

    @Test
    @DisplayName("available() should return zero after the stream is closed")
    void available_shouldReturnZeroAfterClose() throws Exception {
        // Arrange
        final InputStream shadow;
        try (InputStream in = new ObservableInputStream(new ByteArrayInputStream(TEST_DATA))) {
            assertTrue(in.available() > 0);
            shadow = in;
        }
        // Act & Assert
        assertEquals(0, shadow.available(), "available() should be 0 after close");
    }

    @Test
    @DisplayName("read() after close() on a ByteArrayInputStream should not throw an exception")
    void readAfterClose_onByteArrayStream_shouldSucceed() throws Exception {
        // Arrange
        try (InputStream in = new ObservableInputStream(new ByteArrayInputStream(TEST_DATA))) {
            in.close();
            // Act & Assert
            // ByteArrayInputStream#read does not throw an exception after close
            assertNotEquals(IOUtils.EOF, in.read());
        }
    }

    @Test
    @DisplayName("read() after close() on a file-based ChannelInputStream should throw an exception")
    void readAfterClose_onChannelStream_shouldFail() throws Exception {
        // Arrange
        final String resource = "src/test/resources/org/apache/commons/io/abitmorethan16k.txt";
        try (InputStream in = new ObservableInputStream(Files.newInputStream(Paths.get(resource)))) {
            assertNotNull(in, "Could not find test resource: " + resource);
            in.close();
            // Act & Assert
            // ChannelInputStream#read throws an exception after close
            assertThrows(IOException.class, in::read);
        }
    }
}