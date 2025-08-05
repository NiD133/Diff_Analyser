package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.util.List;
import org.apache.commons.io.input.ObservableInputStream.Observer;

/**
 * Test suite for ObservableInputStream functionality.
 * Tests the observable pattern implementation for input streams,
 * including observer notifications and stream operations.
 */
public class ObservableInputStreamTest {

    // Test Data Setup
    private static final byte[] SAMPLE_DATA = {1, 2, 3, 4, 5};
    private static final byte[] EMPTY_DATA = new byte[0];
    
    /**
     * Simple test observer that tracks method calls for verification
     */
    private static class TestObserver extends Observer {
        public boolean dataByteCalled = false;
        public boolean dataArrayCalled = false;
        public boolean finishedCalled = false;
        public boolean closedCalled = false;
        public boolean errorCalled = false;
        public IOException lastError = null;
        
        @Override
        public void data(int value) throws IOException {
            dataByteCalled = true;
        }
        
        @Override
        public void data(byte[] buffer, int offset, int length) throws IOException {
            dataArrayCalled = true;
        }
        
        @Override
        public void finished() throws IOException {
            finishedCalled = true;
        }
        
        @Override
        public void closed() throws IOException {
            closedCalled = true;
        }
        
        @Override
        public void error(IOException exception) throws IOException {
            errorCalled = true;
            lastError = exception;
            throw exception; // Re-throw to test error handling
        }
    }

    // === Constructor Tests ===
    
    @Test
    public void testConstructorWithInputStream() {
        InputStream input = new ByteArrayInputStream(SAMPLE_DATA);
        ObservableInputStream observable = new ObservableInputStream(input);
        
        assertNotNull("Observable stream should be created", observable);
        assertTrue("Should have empty observer list initially", 
                  observable.getObservers().isEmpty());
    }
    
    @Test
    public void testConstructorWithInputStreamAndObservers() {
        InputStream input = new ByteArrayInputStream(SAMPLE_DATA);
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        
        ObservableInputStream observable = new ObservableInputStream(input, observer1, observer2);
        
        assertEquals("Should have 2 observers", 2, observable.getObservers().size());
    }
    
    @Test
    public void testBuilderWithCharSequence() throws IOException {
        CharBuffer buffer = CharBuffer.wrap("Hello World");
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(buffer);
        
        ObservableInputStream observable = builder.get();
        assertNotNull("Observable stream should be created from builder", observable);
    }

    // === Observer Management Tests ===
    
    @Test
    public void testAddObserver() {
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA));
        TestObserver observer = new TestObserver();
        
        observable.add(observer);
        
        assertEquals("Should have 1 observer after adding", 1, observable.getObservers().size());
        assertTrue("Observer list should contain added observer", 
                  observable.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        
        observable.remove(observer);
        
        assertFalse("Observer should be removed from list", 
                   observable.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveAllObservers() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer1, observer2);
        
        observable.removeAllObservers();
        
        assertTrue("All observers should be removed", observable.getObservers().isEmpty());
    }
    
    @Test
    public void testGetObserversReturnsCopy() {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        
        List<Observer> observers = observable.getObservers();
        observers.clear(); // Modify the returned list
        
        assertEquals("Original observer list should be unchanged", 
                    1, observable.getObservers().size());
    }

    // === Stream Reading Tests ===
    
    @Test
    public void testReadSingleByteNotifiesObserver() throws IOException {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        
        int value = observable.read();
        
        assertEquals("Should read first byte", 1, value);
        assertTrue("Observer should be notified of data byte", observer.dataByteCalled);
    }
    
    @Test
    public void testReadByteArrayNotifiesObserver() throws IOException {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        
        byte[] buffer = new byte[3];
        int bytesRead = observable.read(buffer);
        
        assertEquals("Should read 3 bytes", 3, bytesRead);
        assertTrue("Observer should be notified of data array", observer.dataArrayCalled);
    }
    
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        
        byte[] buffer = new byte[10];
        int bytesRead = observable.read(buffer, 2, 3);
        
        assertEquals("Should read 3 bytes", 3, bytesRead);
        assertTrue("Observer should be notified", observer.dataArrayCalled);
    }
    
    @Test
    public void testReadEmptyArrayReturnsZero() throws IOException {
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA));
        
        int result = observable.read(EMPTY_DATA);
        
        assertEquals("Reading empty array should return 0", 0, result);
    }
    
    @Test
    public void testReadAtEndOfStreamNotifiesFinished() throws IOException {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(EMPTY_DATA), observer);
        
        int result = observable.read();
        
        assertEquals("Should return EOF", -1, result);
        assertTrue("Observer should be notified of finished", observer.finishedCalled);
    }

    // === Stream Lifecycle Tests ===
    
    @Test
    public void testCloseNotifiesObserver() throws IOException {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        
        observable.close();
        
        assertTrue("Observer should be notified of close", observer.closedCalled);
    }
    
    @Test
    public void testConsumeReadsEntireStream() throws IOException {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        
        observable.consume();
        
        assertTrue("Observer should be notified of data", observer.dataArrayCalled);
        assertTrue("Observer should be notified of finish", observer.finishedCalled);
    }

    // === Observer Notification Tests ===
    
    @Test
    public void testNoteDataByteNotifiesObservers() throws IOException {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        
        observable.noteDataByte(42);
        
        assertTrue("Observer should be notified of data byte", observer.dataByteCalled);
    }
    
    @Test
    public void testNoteDataBytesNotifiesObservers() throws IOException {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        
        observable.noteDataBytes(SAMPLE_DATA, 0, SAMPLE_DATA.length);
        
        assertTrue("Observer should be notified of data bytes", observer.dataArrayCalled);
    }
    
    @Test
    public void testNoteFinishedNotifiesObservers() throws IOException {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        
        observable.noteFinished();
        
        assertTrue("Observer should be notified of finished", observer.finishedCalled);
    }
    
    @Test
    public void testNoteClosedNotifiesObservers() throws IOException {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        
        observable.noteClosed();
        
        assertTrue("Observer should be notified of closed", observer.closedCalled);
    }

    // === Error Handling Tests ===
    
    @Test
    public void testNoteErrorNotifiesObservers() {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer);
        IOException testException = new IOException("Test error");
        
        try {
            observable.noteError(testException);
            fail("Should have thrown IOException");
        } catch (IOException e) {
            assertTrue("Observer should be notified of error", observer.errorCalled);
            assertEquals("Should receive the same exception", testException, observer.lastError);
        }
    }

    // === Edge Cases and Validation Tests ===
    
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullObserverArrayThrows() {
        new ObservableInputStream(new ByteArrayInputStream(SAMPLE_DATA), (Observer[]) null);
    }
    
    @Test
    public void testMultipleObserversAllNotified() throws IOException {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA), observer1, observer2);
        
        observable.read();
        
        assertTrue("First observer should be notified", observer1.dataByteCalled);
        assertTrue("Second observer should be notified", observer2.dataByteCalled);
    }
    
    @Test
    public void testObserverCanBeAddedAfterConstruction() throws IOException {
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(SAMPLE_DATA));
        TestObserver observer = new TestObserver();
        
        observable.add(observer);
        observable.read();
        
        assertTrue("Dynamically added observer should be notified", observer.dataByteCalled);
    }

    // === Integration Tests ===
    
    @Test
    public void testCompleteReadCycleWithObserver() throws IOException {
        TestObserver observer = new TestObserver();
        ObservableInputStream observable = new ObservableInputStream(
            new ByteArrayInputStream(new byte[]{1, 2}), observer);
        
        // Read all data
        assertEquals("First read should return 1", 1, observable.read());
        assertEquals("Second read should return 2", 2, observable.read());
        assertEquals("Third read should return EOF", -1, observable.read());
        
        observable.close();
        
        assertTrue("Observer should see data notifications", observer.dataByteCalled);
        assertTrue("Observer should see finished notification", observer.finishedCalled);
        assertTrue("Observer should see closed notification", observer.closedCalled);
    }
}