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
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class WindowsLineEndingInputStream_ESTest extends WindowsLineEndingInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testReadWithPushbackStream() throws Throwable {
        byte[] inputBytes = new byte[6];
        inputBytes[5] = (byte) 24;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream);
        DataInputStream dataInputStream = new DataInputStream(pushbackInputStream);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(dataInputStream, true);

        int bytesRead = windowsStream.read(inputBytes);

        assertEquals("Expected no bytes available in the input stream", 0, byteArrayInputStream.available());
        assertEquals("Expected to read 6 bytes", 6, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadSingleByte() throws Throwable {
        byte[] inputBytes = new byte[6];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(byteArrayInputStream, false);

        int byteRead = windowsStream.read();

        assertEquals("Expected 5 bytes to be available after reading one byte", 5, byteArrayInputStream.available());
        assertEquals("Expected to read byte value 0", 0, byteRead);
    }

    @Test(timeout = 4000)
    public void testReadFromNullStream() throws Throwable {
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(null, true);

        try {
            windowsStream.read();
            fail("Expected NullPointerException due to null input stream");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidByteArrayRange() throws Throwable {
        byte[] inputBytes = new byte[2];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes, -1782, 13);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(byteArrayInputStream, false);

        try {
            windowsStream.read();
            fail("Expected ArrayIndexOutOfBoundsException due to invalid byte array range");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testReadFromMockFileInputStream() throws Throwable {
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileInputStream mockFileInputStream = new MockFileInputStream(fileDescriptor);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(mockFileInputStream, false);

        try {
            windowsStream.read();
            fail("Expected IOException due to mock file input stream");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCloseNullStream() throws Throwable {
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(null, false);

        try {
            windowsStream.close();
            fail("Expected NullPointerException due to null input stream");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testReadWithCarriageReturn() throws Throwable {
        byte[] inputBytes = new byte[6];
        inputBytes[0] = (byte) 10;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream);
        DataInputStream dataInputStream = new DataInputStream(pushbackInputStream);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(dataInputStream, true);

        int bytesRead = windowsStream.read(inputBytes);

        assertArrayEquals("Expected CRLF line endings", new byte[]{13, 10, 13, 10, 13, 10}, inputBytes);
        assertEquals("Expected to read 6 bytes", 6, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadEmptyByteArray() throws Throwable {
        byte[] inputBytes = new byte[0];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes, -3187, -3187);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(byteArrayInputStream, false);

        int byteRead = windowsStream.read();

        assertEquals("Expected end of stream (-1)", -1, byteRead);
    }

    @Test(timeout = 4000)
    public void testReadWithLineFeedAtEos() throws Throwable {
        byte[] inputBytes = new byte[0];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes, 2384, 0);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(byteArrayInputStream, true);

        int firstByte = windowsStream.read();
        assertEquals("Expected CR (13) at end of stream", 13, firstByte);

        int secondByte = windowsStream.read();
        assertEquals("Expected LF (10) at end of stream", 10, secondByte);

        int endOfStream = windowsStream.read();
        assertEquals("Expected end of stream (-1)", -1, endOfStream);
    }

    @Test(timeout = 4000)
    public void testUnsupportedMarkOperation() throws Throwable {
        byte[] inputBytes = new byte[6];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream);
        DataInputStream dataInputStream = new DataInputStream(pushbackInputStream);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(dataInputStream, true);

        try {
            windowsStream.mark(0);
            fail("Expected UnsupportedOperationException due to unsupported mark operation");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCloseStream() throws Throwable {
        byte[] inputBytes = new byte[0];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes, 2384, 0);
        WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(byteArrayInputStream, true);

        // Closing the stream should not throw an exception
        windowsStream.close();
    }
}