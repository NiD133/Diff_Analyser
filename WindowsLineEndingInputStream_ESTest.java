package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import org.apache.commons.io.input.WindowsLineEndingInputStream;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.junit.runner.RunWith;

/**
 * Test suite for WindowsLineEndingInputStream which ensures content has Windows line endings (CRLF).
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class WindowsLineEndingInputStream_ESTest extends WindowsLineEndingInputStream_ESTest_scaffolding {

    // Test constants for better readability
    private static final byte LINE_FEED = 10;      // \n
    private static final byte CARRIAGE_RETURN = 13; // \r
    private static final int END_OF_STREAM = -1;
    private static final int BUFFER_SIZE = 6;

    @Test(timeout = 4000)
    public void testReadBufferWithEnsureLineEndingEnabled() throws Throwable {
        // Given: A byte array with non-zero byte at the end and ensureLineEnding = true
        byte[] inputData = new byte[BUFFER_SIZE];
        inputData[5] = (byte) 24; // Non-zero byte at end
        
        InputStream wrappedStream = createWrappedInputStream(inputData);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(wrappedStream, true);
        
        // When: Reading the entire buffer
        byte[] readBuffer = new byte[BUFFER_SIZE];
        int bytesRead = windowsStream.read(readBuffer);
        
        // Then: All bytes should be read successfully
        assertEquals("Should read all 6 bytes", BUFFER_SIZE, bytesRead);
        assertEquals("Underlying stream should be exhausted", 0, 
                    ((ByteArrayInputStream) ((DataInputStream) wrappedStream).in).available());
    }

    @Test(timeout = 4000)
    public void testReadSingleByteWithEnsureLineEndingDisabled() throws Throwable {
        // Given: A byte array of zeros with ensureLineEnding = false
        byte[] inputData = new byte[BUFFER_SIZE];
        ByteArrayInputStream byteStream = new ByteArrayInputStream(inputData);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(byteStream, false);
        
        // When: Reading a single byte
        int readByte = windowsStream.read();
        
        // Then: Should read the first zero byte
        assertEquals("Should read first byte (0)", 0, readByte);
        assertEquals("Should have 5 bytes remaining", 5, byteStream.available());
    }

    @Test(timeout = 4000)
    public void testReadWithNullInputStream() throws Throwable {
        // Given: A WindowsLineEndingInputStream with null input stream
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(null, true);
        
        // When/Then: Reading should throw NullPointerException
        try {
            windowsStream.read();
            fail("Expected NullPointerException when reading from null input stream");
        } catch (NullPointerException e) {
            // Expected behavior - null input stream should cause NPE
        }
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidByteArrayBounds() throws Throwable {
        // Given: A ByteArrayInputStream with invalid offset parameters
        byte[] inputData = new byte[2];
        ByteArrayInputStream invalidStream = new ByteArrayInputStream(inputData, -1782, CARRIAGE_RETURN);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(invalidStream, false);
        
        // When/Then: Reading should throw ArrayIndexOutOfBoundsException
        try {
            windowsStream.read();
            fail("Expected ArrayIndexOutOfBoundsException with invalid array bounds");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected behavior - invalid bounds should cause exception
        }
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidFileDescriptor() throws Throwable {
        // Given: A WindowsLineEndingInputStream with invalid file descriptor
        FileDescriptor invalidDescriptor = new FileDescriptor();
        MockFileInputStream mockStream = new MockFileInputStream(invalidDescriptor);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(mockStream, false);
        
        // When/Then: Reading should throw IOException
        try {
            windowsStream.read();
            fail("Expected IOException when reading from invalid file descriptor");
        } catch (IOException e) {
            // Expected behavior - invalid file descriptor should cause IOException
        }
    }

    @Test(timeout = 4000)
    public void testCloseWithNullInputStream() throws Throwable {
        // Given: A WindowsLineEndingInputStream with null input stream
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(null, false);
        
        // When/Then: Closing should throw NullPointerException
        try {
            windowsStream.close();
            fail("Expected NullPointerException when closing null input stream");
        } catch (NullPointerException e) {
            // Expected behavior - null input stream should cause NPE
        }
    }

    @Test(timeout = 4000)
    public void testLineFeedConversionToCRLF() throws Throwable {
        // Given: Input data starting with line feed character
        byte[] inputData = new byte[BUFFER_SIZE];
        inputData[0] = LINE_FEED; // \n at start
        
        InputStream wrappedStream = createWrappedInputStream(inputData);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(wrappedStream, true);
        
        // When: Reading the buffer
        byte[] readBuffer = new byte[BUFFER_SIZE];
        int bytesRead = windowsStream.read(readBuffer);
        
        // Then: Line feeds should be converted to CRLF pairs
        byte[] expectedOutput = {CARRIAGE_RETURN, LINE_FEED, CARRIAGE_RETURN, 
                               LINE_FEED, CARRIAGE_RETURN, LINE_FEED};
        assertArrayEquals("Line feeds should be converted to CRLF", expectedOutput, readBuffer);
        assertEquals("Should read all 6 bytes", BUFFER_SIZE, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadFromEmptyStreamReturnsEOF() throws Throwable {
        // Given: An empty input stream
        byte[] emptyData = new byte[0];
        ByteArrayInputStream emptyStream = new ByteArrayInputStream(emptyData, -3187, -3187);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(emptyStream, false);
        
        // When: Reading from empty stream
        int result = windowsStream.read();
        
        // Then: Should return end-of-stream indicator
        assertEquals("Empty stream should return EOF", END_OF_STREAM, result);
    }

    @Test(timeout = 4000)
    public void testEnsureLineEndingAtEndOfStream() throws Throwable {
        // Given: An empty stream with ensureLineEnding = true
        byte[] emptyData = new byte[0];
        ByteArrayInputStream emptyStream = new ByteArrayInputStream(emptyData, 2384, 0);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(emptyStream, true);
        
        // When: Reading from stream (should inject CRLF at end)
        int firstByte = windowsStream.read();
        int secondByte = windowsStream.read();
        int thirdByte = windowsStream.read();
        
        // Then: Should inject CRLF at end of stream
        assertEquals("First injected byte should be CR", CARRIAGE_RETURN, firstByte);
        assertEquals("Second injected byte should be LF", LINE_FEED, secondByte);
        assertEquals("Third read should return EOF", END_OF_STREAM, thirdByte);
    }

    @Test(timeout = 4000)
    public void testMarkOperationNotSupported() throws Throwable {
        // Given: A WindowsLineEndingInputStream
        byte[] inputData = new byte[BUFFER_SIZE];
        InputStream wrappedStream = createWrappedInputStream(inputData);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(wrappedStream, true);
        
        // When/Then: Mark operation should throw UnsupportedOperationException
        try {
            windowsStream.mark(0);
            fail("Expected UnsupportedOperationException for mark operation");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should indicate mark/reset not supported", 
                        "mark/reset not supported", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCloseOperationSucceeds() throws Throwable {
        // Given: A WindowsLineEndingInputStream with valid input
        byte[] emptyData = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(emptyData, 2384, 0);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(inputStream, true);
        
        // When: Closing the stream
        windowsStream.close();
        
        // Then: Operation should complete without exception
        // (No assertion needed - test passes if no exception is thrown)
    }

    /**
     * Helper method to create a wrapped input stream with multiple layers
     * (mimics the complex stream wrapping in original tests)
     */
    private InputStream createWrappedInputStream(byte[] data) {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        PushbackInputStream pushbackStream = new PushbackInputStream(byteStream);
        return new DataInputStream(pushbackStream);
    }
}