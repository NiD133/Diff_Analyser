package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedOutputStream;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import org.apache.commons.io.HexDump;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockFileWriter;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class HexDump_ESTest extends HexDump_ESTest_scaffolding {

    private static final long NEGATIVE_OFFSET = -1131L;
    private static final int BYTE_ARRAY_LENGTH_3 = 3;
    private static final int BYTE_ARRAY_LENGTH_7 = 7;
    private static final int BYTE_ARRAY_LENGTH_8 = 8;
    private static final int BYTE_ARRAY_LENGTH_5 = 5;
    private static final int BYTE_ARRAY_LENGTH_10 = 10;
    private static final int BYTE_ARRAY_LENGTH_12 = 12;
    private static final int BYTE_ARRAY_LENGTH_42 = 42;
    private static final int BYTE_ARRAY_LENGTH_20 = 20;
    private static final int BYTE_ARRAY_LENGTH_37 = 37;
    private static final int BYTE_ARRAY_LENGTH_2 = 2;
    private static final int BYTE_ARRAY_LENGTH_25 = 25;
    private static final int BYTE_ARRAY_LENGTH_9 = 9;

    @Test(timeout = 4000)
    public void testDumpToMockPrintStream() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_3];
        MockFile mockFile = new MockFile("org.apache.commons.io.filefilter.EmptyFileFilter");
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream(mockFile);
        MockPrintStream mockPrintStream = new MockPrintStream(mockFileOutputStream);
        HexDump.dump(byteArray, NEGATIVE_OFFSET, (OutputStream) mockPrintStream, 0);
        assertEquals(61L, mockFile.length());
    }

    @Test(timeout = 4000)
    public void testDumpToAppendableWithNegativeByte() throws Throwable {
        MockPrintStream mockPrintStream = new MockPrintStream("\n");
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_7];
        byteArray[0] = (byte) (-1);
        HexDump.dump(byteArray, (Appendable) mockPrintStream);
        assertEquals(BYTE_ARRAY_LENGTH_7, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testDumpToOutputStreamWriter() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_8];
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream("\n", false);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mockFileOutputStream);
        HexDump.dump(byteArray, -1L, (Appendable) outputStreamWriter, 6, 0);
        assertArrayEquals(new byte[BYTE_ARRAY_LENGTH_8], byteArray);
    }

    @Test(timeout = 4000)
    public void testDumpToReadOnlyCharBuffer() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_5];
        CharBuffer charBuffer = CharBuffer.wrap("\n");
        try {
            HexDump.dump(byteArray, (Appendable) charBuffer);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpToOverflowingCharBuffer() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_10];
        char[] charArray = new char[2];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        try {
            HexDump.dump(byteArray, (Appendable) charBuffer);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpToReadOnlyCharBufferWithOffset() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_12];
        CharBuffer charBuffer = CharBuffer.wrap("\n");
        try {
            HexDump.dump(byteArray, 5L, (Appendable) charBuffer, 5, 6);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpToOverflowingCharBufferWithOffset() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_5];
        CharBuffer charBuffer = CharBuffer.allocate(3);
        try {
            HexDump.dump(byteArray, 17L, (Appendable) charBuffer, 1, 2);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpToClosedMockFileWriter() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_42];
        MockFileWriter mockFileWriter = new MockFileWriter("\n");
        mockFileWriter.close();
        try {
            HexDump.dump(byteArray, 0L, (Appendable) mockFileWriter, 1, 1);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDumpNullByteArrayToMockPrintStream() throws Throwable {
        MockPrintStream mockPrintStream = new MockPrintStream("\n");
        try {
            HexDump.dump(null, -2841L, (OutputStream) mockPrintStream, 198);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.HexDump", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpWithInvalidIndex() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_20];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(146);
        try {
            HexDump.dump(byteArray, -1587L, (OutputStream) byteArrayOutputStream, 146);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.HexDump", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpToUnconnectedPipedOutputStream() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_37];
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        try {
            HexDump.dump(byteArray, 804L, (OutputStream) pipedOutputStream, 16);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedOutputStream", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpToMockPrintStreamWithOffset() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_8];
        byteArray[4] = (byte) 127;
        File file = MockFile.createTempFile("x>m*Oh>@'Wm7jA", "x>m*Oh>@'Wm7jA");
        MockPrintStream mockPrintStream = new MockPrintStream(file);
        HexDump.dump(byteArray, 59L, (Appendable) mockPrintStream, 3, 3);
        assertEquals(61L, file.length());
    }

    @Test(timeout = 4000)
    public void testDumpToMockFileWriterWithOffset() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_37];
        byteArray[0] = (byte) 110;
        MockFileWriter mockFileWriter = new MockFileWriter("\n");
        HexDump.dump(byteArray, 110L, (Appendable) mockFileWriter, 0, 1);
        assertEquals(BYTE_ARRAY_LENGTH_37, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testDumpToMockFileWriterWithOffsetAndLength() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_37];
        MockFileWriter mockFileWriter = new MockFileWriter("\n");
        HexDump.dump(byteArray, 1L, (Appendable) mockFileWriter, 1, 32);
        assertEquals(BYTE_ARRAY_LENGTH_37, byteArray.length);
    }

    @Test(timeout = 4000)
    public void testDumpWithInvalidIndexAndLength() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_2];
        MockPrintStream mockPrintStream = new MockPrintStream("\n");
        try {
            HexDump.dump(byteArray, 136L, (Appendable) mockPrintStream, 136, 136);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.HexDump", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpToUnconnectedPipedWriter() throws Throwable {
        PipedWriter pipedWriter = new PipedWriter();
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_3];
        byteArray[2] = (byte) 127;
        try {
            HexDump.dump(byteArray, (Appendable) pipedWriter);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedWriter", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpWithInvalidRange() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_25];
        MockPrintStream mockPrintStream = new MockPrintStream("\n");
        try {
            HexDump.dump(byteArray, 1506L, (Appendable) mockPrintStream, 13, 13);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.HexDump", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpWithNegativeLength() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_5];
        MockPrintStream mockPrintStream = new MockPrintStream("\n");
        try {
            HexDump.dump(byteArray, -545L, (Appendable) mockPrintStream, 0, -545);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.HexDump", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpEmptyArrayToUnconnectedPipedWriter() throws Throwable {
        PipedWriter pipedWriter = new PipedWriter();
        byte[] byteArray = new byte[0];
        try {
            HexDump.dump(byteArray, (Appendable) pipedWriter);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.HexDump", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpNullByteArrayToStringWriter() throws Throwable {
        StringWriter stringWriter = new StringWriter(2);
        try {
            HexDump.dump(null, 3744L, (Appendable) stringWriter, 2, 2);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDumpWithNegativeIndexAndLength() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_8];
        MockPrintStream mockPrintStream = new MockPrintStream("\n");
        try {
            HexDump.dump(byteArray, -706L, (Appendable) mockPrintStream, -706, -706);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.HexDump", e);
        }
    }

    @Test(timeout = 4000)
    public void testHexDumpConstructor() throws Throwable {
        new HexDump();
    }

    @Test(timeout = 4000)
    public void testDumpToNullOutputStream() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_9];
        try {
            HexDump.dump(byteArray, 2185L, (OutputStream) null, 2185);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testDumpToNullAppendable() throws Throwable {
        byte[] byteArray = new byte[BYTE_ARRAY_LENGTH_20];
        try {
            HexDump.dump(byteArray, (Appendable) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }
}