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

    // Test data constants
    private static final String TEST_MESSAGE = "test exception message";
    private static final String TEST_FILE_PATH = "src/test/resources/org/apache/commons/io/abitmorethan16k.txt";
    private static final String SIMPLE_TEST_STRING = "Hi";
    private static final int SMALL_BUFFER_SIZE = 23;
    private static final int TINY_BUFFER_SIZE = 11;
    private static final int BUFFER_OFFSET = 1;

    /**
     * Observer that captures detailed information about data operations.
     * Extends MethodCountObserver to also track buffer contents and values.
     */
    private static final class DataCaptureObserver extends MethodCountObserver {
        private byte[] capturedBuffer;
        private int lastByteValue = -1;
        private int capturedLength = -1;
        private int capturedOffset = -1;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            this.capturedBuffer = buffer;
            this.capturedOffset = offset;
            this.capturedLength = length;
        }

        @Override
        public void data(final int value) throws IOException {
            super.data(value);
            lastByteValue = value;
        }

        public byte[] getCapturedBuffer() { return capturedBuffer; }
        public int getLastByteValue() { return lastByteValue; }
        public int getCapturedLength() { return capturedLength; }
        public int getCapturedOffset() { return capturedOffset; }
    }

    /**
     * Observer that tracks the total number of bytes read.
     */
    private static final class ByteCountObserver extends Observer {
        private long totalBytesRead;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            this.totalBytesRead += length;
        }

        @Override
        public void data(final int value) throws IOException {
            totalBytesRead++;
        }

        public long getTotalBytesRead() {
            return totalBytesRead;
        }
    }

    /**
     * Observer that counts how many times each callback method is invoked.
     */
    private static class MethodCountObserver extends Observer {
        private long closedCallCount;
        private long dataBufferCallCount;
        private long dataByteCallCount;
        private long errorCallCount;
        private long finishedCallCount;

        @Override
        public void closed() throws IOException {
            closedCallCount++;
        }

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            dataBufferCallCount++;
        }

        @Override
        public void data(final int value) throws IOException {
            dataByteCallCount++;
        }

        @Override
        public void error(final IOException exception) throws IOException {
            errorCallCount++;
        }

        @Override
        public void finished() throws IOException {
            finishedCallCount++;
        }

        public long getClosedCallCount() { return closedCallCount; }
        public long getDataBufferCallCount() { return dataBufferCallCount; }
        public long getDataByteCallCount() { return dataByteCallCount; }
        public long getErrorCallCount() { return errorCallCount; }
        public long getFinishedCallCount() { return finishedCallCount; }
    }

    // Helper methods for creating test instances

    private ObservableInputStream createBrokenObservableInputStream() {
        return new ObservableInputStream(BrokenInputStream.INSTANCE);
    }

    private InputStream createRandomDataInputStream() {
        final byte[] randomData = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        return createObservableInputStream(new ByteArrayInputStream(randomData));
    }

    private ObservableInputStream createObservableInputStream(final InputStream sourceStream) {
        return new ObservableInputStream(sourceStream);
    }

    // Tests for Builder functionality

    @Test
    void testAfterReadConsumer_SuccessfulCallback() throws Exception {
        final AtomicBoolean callbackInvoked = new AtomicBoolean();
        
        try (InputStream observableStream = new ObservableInputStream.Builder()
                .setCharSequence(SIMPLE_TEST_STRING)
                .setAfterRead(i -> callbackInvoked.set(true))
                .get()) {
            IOUtils.consume(observableStream);
        }
        
        assertTrue(callbackInvoked.get(), "After-read callback should have been invoked");
    }

    @Test
    void testAfterReadConsumer_ThrowsException() throws Exception {
        try (InputStream observableStream = new ObservableInputStream.Builder()
                .setCharSequence(SIMPLE_TEST_STRING)
                .setAfterRead(i -> {
                    throw new CustomIOException(TEST_MESSAGE);
                })
                .get()) {
            
            CustomIOException exception = assertThrowsExactly(CustomIOException.class, 
                () -> IOUtils.consume(observableStream));
            assertEquals(TEST_MESSAGE, exception.getMessage());
        }
    }

    // Tests for available() method behavior

    @Test
    void testAvailable_AfterStreamClosed() throws Exception {
        final InputStream shadowReference;
        
        try (InputStream observableStream = createRandomDataInputStream()) {
            assertTrue(observableStream.available() > 0, "Stream should have data available when open");
            shadowReference = observableStream;
        }
        
        assertEquals(0, shadowReference.available(), "Stream should report no data available after close");
    }

    @Test
    void testAvailable_WhenStreamOpen() throws Exception {
        try (InputStream observableStream = createRandomDataInputStream()) {
            assertTrue(observableStream.available() > 0, "Stream should have data available initially");
            assertNotEquals(IOUtils.EOF, observableStream.read(), "Should be able to read data");
            assertTrue(observableStream.available() > 0, "Stream should still have data available after reading one byte");
        }
    }

    // Tests for error handling with broken streams

    @Test
    void testBrokenInputStream_SingleByteRead() throws IOException {
        try (ObservableInputStream brokenStream = createBrokenObservableInputStream()) {
            assertThrows(IOException.class, brokenStream::read, 
                "Reading from broken stream should throw IOException");
        }
    }

    @Test
    void testBrokenInputStream_BufferRead() throws IOException {
        try (ObservableInputStream brokenStream = createBrokenObservableInputStream()) {
            assertThrows(IOException.class, () -> brokenStream.read(new byte[1]), 
                "Reading buffer from broken stream should throw IOException");
        }
    }

    @Test
    void testBrokenInputStream_PartialBufferRead() throws IOException {
        try (ObservableInputStream brokenStream = createBrokenObservableInputStream()) {
            assertThrows(IOException.class, () -> brokenStream.read(new byte[2], 0, 1), 
                "Reading partial buffer from broken stream should throw IOException");
        }
    }

    // Tests for observer callback functionality - single byte reads

    @Test
    void testSingleByteReadObserver_AddedAfterCreation() throws Exception {
        final byte[] testData = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        final DataCaptureObserver observer = new DataCaptureObserver();
        
        try (ObservableInputStream observableStream = new ObservableInputStream(new ByteArrayInputStream(testData))) {
            // Verify observer not called before being added
            assertEquals(-1, observer.getLastByteValue(), "Observer should not have captured any value initially");
            observableStream.read(); // Read first byte without observer
            assertEquals(-1, observer.getLastByteValue(), "Observer should not capture data before being added");
            
            // Add observer and verify it captures subsequent reads
            observableStream.add(observer);
            verifyObserverCapturesRemainingBytes(observableStream, testData, observer, 1);
        }
    }

    @Test
    void testSingleByteReadObserver_AddedAtCreation() throws Exception {
        final byte[] testData = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        final DataCaptureObserver observer = new DataCaptureObserver();
        
        try (ObservableInputStream observableStream = new ObservableInputStream(new ByteArrayInputStream(testData), observer)) {
            assertEquals(-1, observer.getLastByteValue(), "Observer should not have captured any value initially");
            
            // Read first byte and verify observer captures it
            observableStream.read();
            assertNotEquals(-1, observer.getLastByteValue(), "Observer should capture first byte read");
            
            verifyObserverCapturesRemainingBytes(observableStream, testData, observer, 1);
        }
    }

    private void verifyObserverCapturesRemainingBytes(ObservableInputStream stream, byte[] expectedData, 
            DataCaptureObserver observer, int startIndex) throws IOException {
        
        // Read remaining bytes and verify observer captures each one
        for (int i = startIndex; i < expectedData.length; i++) {
            final int bytesRead = stream.read();
            assertEquals((byte) bytesRead, expectedData[i], "Read byte should match expected data");
            assertEquals(bytesRead, observer.getLastByteValue(), "Observer should capture the read byte");
            assertEquals(0, observer.getFinishedCallCount(), "Stream should not be finished yet");
            assertEquals(0, observer.getClosedCallCount(), "Stream should not be closed yet");
        }
        
        // Verify end-of-stream handling
        final int endResult = stream.read();
        assertEquals(-1, endResult, "Should return EOF at end of stream");
        assertEquals(1, observer.getFinishedCallCount(), "Observer should be notified when stream finishes");
        assertEquals(0, observer.getClosedCallCount(), "Stream should not be closed until explicitly closed");
        
        stream.close();
        assertEquals(1, observer.getFinishedCallCount(), "Finished count should remain 1 after close");
        assertEquals(1, observer.getClosedCallCount(), "Observer should be notified when stream is closed");
    }

    // Tests for observer callback functionality - buffer reads

    @Test
    void testBufferReadObserver_CapturesBufferDetails() throws Exception {
        final byte[] testData = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        
        try (ByteArrayInputStream sourceStream = new ByteArrayInputStream(testData);
                ObservableInputStream observableStream = createObservableInputStream(sourceStream)) {
            
            final DataCaptureObserver observer = new DataCaptureObserver();
            final byte[] readBuffer = new byte[SMALL_BUFFER_SIZE];
            
            // Verify observer doesn't capture data before being added
            assertNull(observer.getCapturedBuffer(), "Observer should not have captured buffer initially");
            observableStream.read(readBuffer);
            assertNull(observer.getCapturedBuffer(), "Observer should not capture data before being added");
            
            // Add observer and test buffer reads
            observableStream.add(observer);
            verifyBufferReadsWithObserver(observableStream, sourceStream, observer, readBuffer);
        }
    }

    private void verifyBufferReadsWithObserver(ObservableInputStream stream, ByteArrayInputStream sourceStream, 
            DataCaptureObserver observer, byte[] readBuffer) throws IOException {
        
        while (true) {
            if (sourceStream.available() >= 2048) {
                // Test full buffer reads
                final int bytesRead = stream.read(readBuffer);
                if (bytesRead == -1) {
                    stream.close();
                    break;
                }
                
                assertEquals(readBuffer, observer.getCapturedBuffer(), "Observer should capture the read buffer");
                assertEquals(0, observer.getCapturedOffset(), "Observer should capture correct offset");
                assertEquals(readBuffer.length, observer.getCapturedLength(), "Observer should capture correct length");
            } else {
                // Test partial buffer reads
                final int expectedBytes = Math.min(TINY_BUFFER_SIZE, sourceStream.available());
                final int bytesRead = stream.read(readBuffer, BUFFER_OFFSET, TINY_BUFFER_SIZE);
                if (bytesRead == -1) {
                    stream.close();
                    break;
                }
                
                assertEquals(readBuffer, observer.getCapturedBuffer(), "Observer should capture the read buffer");
                assertEquals(BUFFER_OFFSET, observer.getCapturedOffset(), "Observer should capture correct offset");
                assertEquals(expectedBytes, observer.getCapturedLength(), "Observer should capture actual bytes read");
            }
        }
    }

    // Tests for observer management

    @Test
    void testObserverManagement_NoObservers() throws IOException {
        try (ObservableInputStream stream = new ObservableInputStream(new NullInputStream())) {
            assertTrue(stream.getObservers().isEmpty(), "New stream should have no observers");
        }
    }

    @Test
    void testObserverManagement_SingleObserver() throws IOException {
        final DataCaptureObserver observer = new DataCaptureObserver();
        try (ObservableInputStream stream = new ObservableInputStream(new NullInputStream(), observer)) {
            assertEquals(observer, stream.getObservers().get(0), "Stream should contain the added observer");
        }
    }

    @Test
    void testObserverManagement_MultipleObserversInOrder() throws IOException {
        final DataCaptureObserver firstObserver = new DataCaptureObserver();
        final DataCaptureObserver secondObserver = new DataCaptureObserver();
        
        try (ObservableInputStream stream = new ObservableInputStream(new NullInputStream(), firstObserver, secondObserver)) {
            assertEquals(firstObserver, stream.getObservers().get(0), "First observer should be at index 0");
            assertEquals(secondObserver, stream.getObservers().get(1), "Second observer should be at index 1");
        }
    }

    // Tests for notification callbacks with different buffer sizes

    @Test
    void testNotificationCallbacks_SmallBufferSize() throws IOException {
        verifyNotificationCallbacksWithBufferSize(1);
    }

    @Test
    void testNotificationCallbacks_MediumBufferSize() throws IOException {
        verifyNotificationCallbacksWithBufferSize(2);
    }

    @Test
    void testNotificationCallbacks_DefaultBufferSize() throws IOException {
        verifyNotificationCallbacksWithBufferSize(IOUtils.DEFAULT_BUFFER_SIZE);
    }

    private void verifyNotificationCallbacksWithBufferSize(final int bufferSize) throws IOException {
        final byte[] testData = IOUtils.byteArray();
        final ByteCountObserver byteCounter = new ByteCountObserver();
        final MethodCountObserver methodCounter = new MethodCountObserver();
        
        try (ObservableInputStream stream = new ObservableInputStream(new ByteArrayInputStream(testData), byteCounter, methodCounter)) {
            assertEquals(IOUtils.DEFAULT_BUFFER_SIZE, IOUtils.copy(stream, NullOutputStream.INSTANCE, bufferSize),
                "Should copy all data from stream");
        }
        
        // Verify byte counting
        assertEquals(IOUtils.DEFAULT_BUFFER_SIZE, byteCounter.getTotalBytesRead(), 
            "Observer should count all bytes read");
        
        // Verify method call counts
        assertEquals(1, methodCounter.getClosedCallCount(), "Stream should be closed once");
        assertEquals(1, methodCounter.getFinishedCallCount(), "Stream should finish once");
        assertEquals(0, methodCounter.getErrorCallCount(), "No errors should occur");
        assertEquals(0, methodCounter.getDataByteCallCount(), "No single-byte reads should occur");
        assertEquals(testData.length / bufferSize, methodCounter.getDataBufferCallCount(), 
            "Buffer read count should match expected number of buffer operations");
    }

    // Tests for reading after stream closure

    @Test
    void testReadAfterClose_ByteArrayInputStream() throws Exception {
        try (InputStream stream = createRandomDataInputStream()) {
            stream.close();
            // ByteArrayInputStream allows reading after close
            assertNotEquals(IOUtils.EOF, stream.read(), 
                "ByteArrayInputStream should still allow reading after close");
        }
    }

    @SuppressWarnings("resource")
    @Test
    void testReadAfterClose_FileInputStream() throws Exception {
        try (InputStream stream = createObservableInputStream(Files.newInputStream(Paths.get(TEST_FILE_PATH)))) {
            stream.close();
            // File-based streams typically throw when reading after close
            assertThrows(IOException.class, stream::read, 
                "File-based InputStream should throw IOException when reading after close");
        }
    }
}