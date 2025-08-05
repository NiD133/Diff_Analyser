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

import org.apache.commons.io.IOExceptionList;
import org.junit.Test;
import org.mockito.InOrder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Understandable and maintainable tests for {@link ObservableInputStream}.
 * This class replaces an auto-generated test suite, focusing on clarity,
 * descriptive naming, and standard testing practices.
 */
public class ObservableInputStreamTest {

    private static final byte[] TEST_DATA = "Hello World".getBytes();

    // A concrete observer implementation for simple spying.
    private static class TestObserver extends ObservableInputStream.Observer {
        @Override
        public void data(int b) throws IOException {}
        @Override
        public void data(byte[] b, int off, int len) throws IOException {}
        @Override
        public void finished() throws IOException {}
        @Override
        public void closed() throws IOException {}
        @Override
        public void error(IOException exception) throws IOException {
            super.error(exception);
        }
    }

    // =================================================================
    // Read Method Tests
    // =================================================================

    @Test
    public void read_shouldReturnNextByteAndNotifyObserver() throws IOException {
        // Arrange
        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        final InputStream source = new ByteArrayInputStream(TEST_DATA);
        final ObservableInputStream ois = new ObservableInputStream(source, observer);

        // Act
        final int byte1 = ois.read();
        final int byte2 = ois.read();

        // Assert
        assertEquals('H', byte1);
        assertEquals('e', byte2);

        final InOrder inOrder = inOrder(observer);
        inOrder.verify(observer).data('H');
        inOrder.verify(observer).data('e');
    }

    @Test
    public void read_whenStreamIsEmpty_shouldReturnEOFAndNotifyFinished() throws IOException {
        // Arrange
        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        final InputStream source = new ByteArrayInputStream(new byte[0]);
        final ObservableInputStream ois = new ObservableInputStream(source, observer);

        // Act
        final int result = ois.read();

        // Assert
        assertEquals(-1, result);
        verify(observer).finished();
        verify(observer, never()).data(org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    public void readByteArray_shouldFillBufferAndNotifyObserver() throws IOException {
        // Arrange
        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        final InputStream source = new ByteArrayInputStream(TEST_DATA);
        final ObservableInputStream ois = new ObservableInputStream(source, observer);
        final byte[] buffer = new byte[5];

        // Act
        final int bytesRead = ois.read(buffer);

        // Assert
        assertEquals(5, bytesRead);
        assertArrayEquals("Hello".getBytes(), buffer);
        verify(observer).data(buffer, 0, 5);
    }

    @Test
    public void readByteArray_whenStreamIsEmpty_shouldReturnEOFAndNotifyFinished() throws IOException {
        // Arrange
        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        final InputStream source = new ByteArrayInputStream(new byte[0]);
        final ObservableInputStream ois = new ObservableInputStream(source, observer);
        final byte[] buffer = new byte[10];

        // Act
        final int bytesRead = ois.read(buffer);

        // Assert
        assertEquals(-1, bytesRead);
        verify(observer).finished();
        verify(observer, never()).data(org.mockito.ArgumentMatchers.any(byte[].class), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    public void read_whenLengthIsZero_shouldReturnZeroAndNotNotify() throws IOException {
        // Arrange
        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        final InputStream source = new ByteArrayInputStream(TEST_DATA);
        final ObservableInputStream ois = new ObservableInputStream(source, observer);

        // Act
        final int bytesRead = ois.read(new byte[5], 0, 0);

        // Assert
        assertEquals(0, bytesRead);
        verify(observer, never()).data(org.mockito.ArgumentMatchers.any(byte[].class), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt());
    }

    // =================================================================
    // Close and Consume Method Tests
    // =================================================================

    @Test
    public void close_shouldCloseUnderlyingStreamAndNotifyObservers() throws IOException {
        // Arrange
        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        final InputStream source = spy(new ByteArrayInputStream(TEST_DATA));
        final ObservableInputStream ois = new ObservableInputStream(source, observer);

        // Act
        ois.close();

        // Assert
        verify(source).close();
        verify(observer).closed();
    }

    @Test
    public void consume_shouldReadAllBytesAndNotifyFinished() throws IOException {
        // Arrange
        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        final InputStream source = new ByteArrayInputStream(TEST_DATA);
        final ObservableInputStream ois = new ObservableInputStream(source, observer);

        // Act
        ois.consume();

        // Assert that all data was passed to the observer, and then finished was called.
        verify(observer).data(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.eq(TEST_DATA.length));
        verify(observer).finished();

        // The stream should now be at the end
        assertEquals(-1, ois.read());
    }

    // =================================================================
    // Observer Management Tests
    // =================================================================

    @Test
    public void addAndRemoveObserver_shouldModifyObserverList() {
        // Arrange
        final ObservableInputStream.Observer observer = new TestObserver();
        final ObservableInputStream ois = new ObservableInputStream(null);

        // Assert initial state
        assertTrue(ois.getObservers().isEmpty());

        // Act: Add
        ois.add(observer);

        // Assert after add
        assertEquals(1, ois.getObservers().size());
        assertTrue(ois.getObservers().contains(observer));

        // Act: Remove
        ois.remove(observer);

        // Assert after remove
        assertTrue(ois.getObservers().isEmpty());
    }

    @Test
    public void removeAllObservers_shouldClearAllRegisteredObservers() {
        // Arrange
        final ObservableInputStream ois = new ObservableInputStream(null);
        ois.add(new TestObserver());
        ois.add(new TestObserver());
        assertEquals(2, ois.getObservers().size());

        // Act
        ois.removeAllObservers();

        // Assert
        assertTrue(ois.getObservers().isEmpty());
    }


    @Test(expected = UnsupportedOperationException.class)
    public void removeAllObservers_whenConstructedWithArray_shouldThrowException() {
        // Arrange
        // This constructor uses Arrays.asList(), which returns a fixed-size list.
        final ObservableInputStream ois = new ObservableInputStream(null, new TestObserver());

        // Act
        ois.removeAllObservers(); // Should throw UnsupportedOperationException
    }

    @Test
    public void getObservers_shouldReturnCopyOfObserverList() {
        // Arrange
        final ObservableInputStream ois = new ObservableInputStream(null);
        final ObservableInputStream.Observer observer = new TestObserver();
        ois.add(observer);

        // Act
        final List<ObservableInputStream.Observer> observers = ois.getObservers();
        observers.clear(); // Modify the returned list

        // Assert
        // The original list inside the stream should be unaffected.
        assertFalse("getObservers() should return a copy", ois.getObservers().isEmpty());
        assertEquals(1, ois.getObservers().size());
    }

    // =================================================================
    // Exception Handling Tests
    // =================================================================

    @Test
    public void read_whenUnderlyingStreamThrowsIOException_shouldNotifyErrorAndPropagate() throws IOException {
        // Arrange
        final IOException streamException = new IOException("Stream error");
        final InputStream source = mock(InputStream.class);
        doThrow(streamException).when(source).read();

        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        final ObservableInputStream ois = new ObservableInputStream(source, observer);

        // Act & Assert
        final IOException thrown = assertThrows(IOException.class, ois::read);

        // The original exception should be propagated.
        assertEquals(streamException, thrown);

        // The observer should have been notified of the error.
        verify(observer).error(streamException);
    }

    @Test
    public void read_whenObserverThrowsIOException_shouldBeWrappedInIOExceptionList() throws IOException {
        // Arrange
        final IOException observerException = new IOException("Observer error");
        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        doThrow(observerException).when(observer).data(org.mockito.ArgumentMatchers.anyInt());

        final InputStream source = new ByteArrayInputStream(TEST_DATA);
        final ObservableInputStream ois = new ObservableInputStream(source, observer);

        // Act & Assert
        try {
            ois.read();
            fail("Expected an IOExceptionList to be thrown");
        } catch (final IOExceptionList e) {
            // The list should contain the exception from the observer.
            assertEquals(1, e.getCauseList().size());
            assertEquals(observerException, e.getCause(0));
        }
    }

    @Test
    public void close_whenObserverThrowsIOException_shouldBeWrappedInIOExceptionList() throws IOException {
        // Arrange
        final IOException observerException = new IOException("Observer close error");
        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        doThrow(observerException).when(observer).closed();

        final InputStream source = new ByteArrayInputStream(TEST_DATA);
        final ObservableInputStream ois = new ObservableInputStream(source, observer);

        // Act & Assert
        try {
            ois.close();
            fail("Expected an IOExceptionList to be thrown");
        } catch (final IOExceptionList e) {
            assertEquals(1, e.getCauseList().size());
            assertEquals(observerException, e.getCause(0));
        }
    }

    @Test(expected = NullPointerException.class)
    public void add_whenObserverIsNull_shouldThrowNullPointerException() {
        // Arrange
        final ObservableInputStream ois = new ObservableInputStream(null);
        // Act
        ois.add(null);
    }

    @Test(expected = NullPointerException.class)
    public void read_whenObserverIsNull_shouldThrowNullPointerException() throws IOException {
        // Arrange
        final ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(TEST_DATA), (ObservableInputStream.Observer) null);
        // Act
        ois.read();
    }

    // =================================================================
    // Builder Tests
    // =================================================================

    @Test
    public void builder_shouldBuildStreamWithObservers() throws IOException {
        // Arrange
        final ObservableInputStream.Observer observer = mock(ObservableInputStream.Observer.class);
        final ObservableInputStream.Builder builder = new ObservableInputStream.Builder()
                .setInputStream(new ByteArrayInputStream(TEST_DATA))
                .setObservers(Collections.singletonList(observer));

        // Act
        final ObservableInputStream ois = builder.get();
        assertNotNull(ois);
        ois.read();

        // Assert
        verify(observer).data('H');
    }

    @Test(expected = IllegalStateException.class)
    public void builder_whenNoOriginSet_shouldThrowIllegalStateException() throws IOException {
        // Arrange
        final ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        // Act
        builder.get();
    }
}