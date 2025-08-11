package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.SerializedString;
import java.io.*;
import java.nio.ByteBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class SerializedStringTest extends SerializedString_ESTest_scaffolding {

    // Test for ArrayIndexOutOfBoundsException when appending unquoted characters
    @Test(timeout = 4000)
    public void testAppendUnquotedCharArrayIndexOutOfBounds() {
        SerializedString serializedString = new SerializedString("Q");
        char[] emptyCharArray = new char[0];
        try {
            serializedString.appendUnquoted(emptyCharArray, -1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    // Test for ArrayIndexOutOfBoundsException when appending quoted UTF8 bytes
    @Test(timeout = 4000)
    public void testAppendQuotedUTF8ArrayIndexOutOfBounds() {
        SerializedString serializedString = new SerializedString("8+19.:23Vy=-x");
        byte[] utf8Bytes = serializedString.asUnquotedUTF8();
        try {
            serializedString.appendQuotedUTF8(utf8Bytes, -1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    // Test writing unquoted UTF8 to a mock file output stream
    @Test(timeout = 4000)
    public void testWriteUnquotedUTF8ToMockFile() throws IOException {
        SerializedString serializedString = new SerializedString("E]`R4#OI%");
        MockFile mockFile = new MockFile("E]`R4#OI%", "E]`R4#OI%");
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream(mockFile);
        int bytesWritten = serializedString.writeUnquotedUTF8(mockFileOutputStream);
        assertEquals(9, bytesWritten);
        assertEquals(9L, mockFile.length());
    }

    // Test writing quoted UTF8 to a buffered output stream
    @Test(timeout = 4000)
    public void testWriteQuotedUTF8ToBufferedOutputStream() throws IOException {
        SerializedString serializedString = new SerializedString("");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(14);
        MockPrintStream mockPrintStream = new MockPrintStream(byteArrayOutputStream, false);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(mockPrintStream, 65);
        int bytesWritten = serializedString.writeQuotedUTF8(bufferedOutputStream);
        assertEquals(0, bytesWritten);
    }

    // Test writing quoted UTF8 to a mock print stream
    @Test(timeout = 4000)
    public void testWriteQuotedUTF8ToMockPrintStream() throws IOException {
        SerializedString serializedString = new SerializedString("0");
        MockPrintStream mockPrintStream = new MockPrintStream("Sc2,\"g4ysda!5!{]");
        int bytesWritten = serializedString.writeQuotedUTF8(mockPrintStream);
        assertEquals(1, bytesWritten);
    }

    // Test toString method
    @Test(timeout = 4000)
    public void testToString() {
        SerializedString serializedString = new SerializedString(";\"mRHC$u");
        String stringValue = serializedString.toString();
        assertEquals(";\"mRHC$u", stringValue);
    }

    // Test readResolve method with empty serialization value
    @Test(timeout = 4000)
    public void testReadResolveWithEmptySerializationValue() {
        SerializedString serializedString = new SerializedString("'n~Q4n");
        serializedString._jdkSerializeValue = "";
        Object resolvedObject = serializedString.readResolve();
        assertEquals("", resolvedObject.toString());
    }

    // Test putting unquoted UTF8 into a ByteBuffer
    @Test(timeout = 4000)
    public void testPutUnquotedUTF8IntoByteBuffer() {
        SerializedString serializedString = new SerializedString("");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(0);
        int bytesWritten = serializedString.putUnquotedUTF8(byteBuffer);
        assertEquals(0, bytesWritten);
    }

    // Test putting quoted UTF8 into a ByteBuffer
    @Test(timeout = 4000)
    public void testPutQuotedUTF8IntoByteBuffer() {
        SerializedString serializedString = new SerializedString("");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(0);
        int bytesWritten = serializedString.putQuotedUTF8(byteBuffer);
        assertEquals(0, bytesWritten);
    }

    // Test getValue method
    @Test(timeout = 4000)
    public void testGetValue() {
        SerializedString serializedString = new SerializedString("");
        String value = serializedString.getValue();
        assertEquals("", value);
    }

    // Test charLength method
    @Test(timeout = 4000)
    public void testCharLength() {
        SerializedString serializedString = new SerializedString("");
        int length = serializedString.charLength();
        assertEquals(0, length);
    }

    // Test asQuotedChars method
    @Test(timeout = 4000)
    public void testAsQuotedChars() {
        SerializedString serializedString = new SerializedString("");
        char[] quotedChars = serializedString.asQuotedChars();
        assertEquals(0, quotedChars.length);
    }

    // Test appending unquoted UTF8 bytes
    @Test(timeout = 4000)
    public void testAppendUnquotedUTF8Bytes() {
        SerializedString serializedString = new SerializedString("");
        byte[] byteArray = new byte[2];
        int bytesAppended = serializedString.appendUnquotedUTF8(byteArray, (byte) 0);
        assertEquals(0, bytesAppended);
    }

    // Test appending unquoted characters
    @Test(timeout = 4000)
    public void testAppendUnquotedChars() {
        SerializedString serializedString = new SerializedString("");
        char[] charArray = new char[7];
        int charsAppended = serializedString.appendUnquoted(charArray, 1);
        assertEquals(0, charsAppended);
    }

    // Test appending unquoted characters with non-empty string
    @Test(timeout = 4000)
    public void testAppendUnquotedCharsWithNonEmptyString() {
        SerializedString serializedString = new SerializedString("j");
        char[] charArray = new char[4];
        int charsAppended = serializedString.appendUnquoted(charArray, 1);
        assertEquals(1, charsAppended);
        assertArrayEquals(new char[] {'\u0000', 'j', '\u0000', '\u0000'}, charArray);
    }

    // Test appending quoted UTF8 bytes
    @Test(timeout = 4000)
    public void testAppendQuotedUTF8Bytes() {
        SerializedString serializedString = new SerializedString("");
        byte[] byteArray = new byte[9];
        int bytesAppended = serializedString.appendQuotedUTF8(byteArray, 1);
        assertEquals(0, bytesAppended);
    }

    // Test appending quoted characters
    @Test(timeout = 4000)
    public void testAppendQuotedChars() {
        SerializedString serializedString = new SerializedString("");
        char[] charArray = new char[5];
        int charsAppended = serializedString.appendQuoted(charArray, 1);
        assertEquals(0, charsAppended);
    }

    // Test appending quoted characters with pre-set quoted chars
    @Test(timeout = 4000)
    public void testAppendQuotedCharsWithPreSetQuotedChars() {
        SerializedString serializedString = new SerializedString("");
        char[] charArray = new char[4];
        serializedString._quotedChars = charArray;
        int charsAppended = serializedString.appendQuoted(charArray, 0);
        assertEquals(4, charsAppended);
    }

    // Test writing unquoted UTF8 to a null OutputStream
    @Test(timeout = 4000)
    public void testWriteUnquotedUTF8ToNullOutputStream() {
        SerializedString serializedString = new SerializedString("");
        try {
            serializedString.writeUnquotedUTF8((OutputStream) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test writing quoted UTF8 to a disconnected PipedOutputStream
    @Test(timeout = 4000)
    public void testWriteQuotedUTF8ToDisconnectedPipedOutputStream() {
        SerializedString serializedString = new SerializedString("");
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        try {
            serializedString.writeQuotedUTF8(pipedOutputStream);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception: Pipe not connected
        }
    }

    // Test putting unquoted UTF8 into a null ByteBuffer
    @Test(timeout = 4000)
    public void testPutUnquotedUTF8IntoNullByteBuffer() {
        SerializedString serializedString = new SerializedString("X fh]-BVE2`Wh");
        try {
            serializedString.putUnquotedUTF8((ByteBuffer) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test appending unquoted UTF8 with null byte array
    @Test(timeout = 4000)
    public void testAppendUnquotedUTF8WithNullByteArray() {
        SerializedString serializedString = new SerializedString("");
        try {
            serializedString.appendUnquotedUTF8((byte[]) null, -443);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test appending unquoted UTF8 with empty byte array
    @Test(timeout = 4000)
    public void testAppendUnquotedUTF8WithEmptyByteArray() {
        SerializedString serializedString = new SerializedString("R^$t>9-hl+");
        byte[] emptyByteArray = new byte[0];
        try {
            serializedString.appendUnquotedUTF8(emptyByteArray, -1910);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    // Test appending unquoted chars with null char array
    @Test(timeout = 4000)
    public void testAppendUnquotedCharsWithNullCharArray() {
        SerializedString serializedString = new SerializedString("wSQ ");
        try {
            serializedString.appendUnquoted((char[]) null, -1666);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test appending quoted UTF8 with null byte array
    @Test(timeout = 4000)
    public void testAppendQuotedUTF8WithNullByteArray() {
        SerializedString serializedString = new SerializedString("Rklr,bQx~YNd\"");
        try {
            serializedString.appendQuotedUTF8((byte[]) null, 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test creating SerializedString with null string
    @Test(timeout = 4000)
    public void testSerializedStringWithNullString() {
        try {
            SerializedString serializedString = new SerializedString((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception: Null String illegal for SerializedString
        }
    }

    // Test equals method with another SerializedString with the same value
    @Test(timeout = 4000)
    public void testEqualsWithSameValue() {
        SerializedString serializedString1 = new SerializedString("");
        SerializedString serializedString2 = new SerializedString("");
        assertTrue(serializedString1.equals(serializedString2));
    }

    // Test equals method with null
    @Test(timeout = 4000)
    public void testEqualsWithNull() {
        SerializedString serializedString = new SerializedString("");
        assertFalse(serializedString.equals(null));
    }

    // Test equals method with itself
    @Test(timeout = 4000)
    public void testEqualsWithItself() {
        SerializedString serializedString = new SerializedString("");
        assertTrue(serializedString.equals(serializedString));
    }

    // Test equals method with a different type
    @Test(timeout = 4000)
    public void testEqualsWithDifferentType() {
        SerializedString serializedString = new SerializedString("8+19.:23Vy=-x");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(0);
        assertFalse(serializedString.equals(byteBuffer));
    }

    // Test putting unquoted UTF8 into a large ByteBuffer
    @Test(timeout = 4000)
    public void testPutUnquotedUTF8IntoLargeByteBuffer() {
        SerializedString serializedString = new SerializedString(" ");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3669);
        int bytesWritten = serializedString.putUnquotedUTF8(byteBuffer);
        assertEquals(3668, byteBuffer.remaining());
        assertEquals(1, bytesWritten);
    }

    // Test putting unquoted UTF8 into an empty ByteBuffer
    @Test(timeout = 4000)
    public void testPutUnquotedUTF8IntoEmptyByteBuffer() {
        SerializedString serializedString = new SerializedString("L,3EO8LyQHon");
        ByteBuffer byteBuffer = ByteBuffer.allocate(0);
        int bytesWritten = serializedString.putUnquotedUTF8(byteBuffer);
        assertEquals(-1, bytesWritten);
    }

    // Test putting unquoted UTF8 into an empty ByteBuffer after converting to UTF8
    @Test(timeout = 4000)
    public void testPutUnquotedUTF8IntoEmptyByteBufferAfterConversion() {
        SerializedString serializedString = new SerializedString("8+19.:23Vy=-x");
        serializedString.asUnquotedUTF8();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(0);
        int bytesWritten = serializedString.putUnquotedUTF8(byteBuffer);
        assertEquals(-1, bytesWritten);
    }

    // Test putting quoted UTF8 into a small ByteBuffer
    @Test(timeout = 4000)
    public void testPutQuotedUTF8IntoSmallByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(7);
        SerializedString serializedString = new SerializedString("8g9F:23V=x");
        int bytesWritten = serializedString.putQuotedUTF8(byteBuffer);
        assertEquals(-1, bytesWritten);
    }

    // Test putting quoted UTF8 into a large ByteBuffer
    @Test(timeout = 4000)
    public void testPutQuotedUTF8IntoLargeByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3615);
        SerializedString serializedString = new SerializedString("8+19F:23Vy=-x");
        int bytesWritten = serializedString.putQuotedUTF8(byteBuffer);
        assertEquals(3602, byteBuffer.remaining());
        assertEquals(13, bytesWritten);
    }

    // Test putting quoted UTF8 into a null ByteBuffer
    @Test(timeout = 4000)
    public void testPutQuotedUTF8IntoNullByteBuffer() {
        SerializedString serializedString = new SerializedString("Q");
        serializedString.asQuotedUTF8();
        try {
            serializedString.putQuotedUTF8((ByteBuffer) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test writing unquoted UTF8 to a disconnected PipedOutputStream
    @Test(timeout = 4000)
    public void testWriteUnquotedUTF8ToDisconnectedPipedOutputStream() {
        SerializedString serializedString = new SerializedString("2#>7299cK");
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        try {
            serializedString.writeUnquotedUTF8(pipedOutputStream);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception: Pipe not connected
        }
    }

    // Test appending unquoted UTF8 bytes and writing to a mock print stream
    @Test(timeout = 4000)
    public void testAppendUnquotedUTF8BytesAndWriteToMockPrintStream() throws IOException {
        SerializedString serializedString = new SerializedString("");
        byte[] emptyByteArray = new byte[0];
        MockPrintStream mockPrintStream = new MockPrintStream("I?4|?BKE|");
        int bytesAppended = serializedString.appendUnquotedUTF8(emptyByteArray, 1);
        assertEquals(-1, bytesAppended);

        int bytesWritten = serializedString.writeUnquotedUTF8(mockPrintStream);
        assertEquals(0, bytesWritten);
    }

    // Test writing quoted UTF8 to a null OutputStream
    @Test(timeout = 4000)
    public void testWriteQuotedUTF8ToNullOutputStream() {
        SerializedString serializedString = new SerializedString("");
        try {
            serializedString.writeQuotedUTF8((OutputStream) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test writing quoted UTF8 to a null OutputStream after conversion
    @Test(timeout = 4000)
    public void testWriteQuotedUTF8ToNullOutputStreamAfterConversion() {
        SerializedString serializedString = new SerializedString("");
        serializedString.asQuotedUTF8();
        try {
            serializedString.writeQuotedUTF8((OutputStream) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test appending unquoted UTF8 bytes
    @Test(timeout = 4000)
    public void testAppendUnquotedUTF8BytesWithNonEmptyString() {
        SerializedString serializedString = new SerializedString("TP1aO6Zd");
        byte[] utf8Bytes = serializedString.asUnquotedUTF8();
        int bytesAppended = serializedString.appendUnquotedUTF8(utf8Bytes, 0);
        assertEquals(8, bytesAppended);
    }

    // Test appending unquoted UTF8 bytes with empty string
    @Test(timeout = 4000)
    public void testAppendUnquotedUTF8BytesWithEmptyString() {
        SerializedString serializedString = new SerializedString("");
        byte[] utf8Bytes = serializedString.asUnquotedUTF8();
        assertNotNull(utf8Bytes);

        int bytesAppended = serializedString.appendUnquotedUTF8(utf8Bytes, 1);
        assertEquals(-1, bytesAppended);
    }

    // Test appending unquoted characters with negative offset
    @Test(timeout = 4000)
    public void testAppendUnquotedCharsWithNegativeOffset() {
        SerializedString serializedString = new SerializedString(", coipied ");
        char[] charArray = new char[2];
        int charsAppended = serializedString.appendUnquoted(charArray, -1);
        assertEquals(-1, charsAppended);
    }

    // Test appending unquoted characters with invalid offset
    @Test(timeout = 4000)
    public void testAppendUnquotedCharsWithInvalidOffset() {
        SerializedString serializedString = new SerializedString("Broken surrogate pair: first char 0x");
        char[] quotedChars = serializedString.asQuotedChars();
        try {
            serializedString.appendUnquoted(quotedChars, -182);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    // Test appending quoted UTF8 bytes with invalid offset
    @Test(timeout = 4000)
    public void testAppendQuotedUTF8BytesWithInvalidOffset() {
        SerializedString serializedString = new SerializedString("M:oX:\"bsoHuP");
        byte[] byteArray = new byte[14];
        int bytesAppended = serializedString.appendQuotedUTF8(byteArray, (byte) 77);
        assertEquals(-1, bytesAppended);
    }

    // Test appending quoted UTF8 bytes with valid offset
    @Test(timeout = 4000)
    public void testAppendQuotedUTF8BytesWithValidOffset() {
        SerializedString serializedString = new SerializedString(", coipied ");
        byte[] quotedUTF8Bytes = serializedString.asQuotedUTF8();
        assertNotNull(quotedUTF8Bytes);

        byte[] byteArray = new byte[20];
        int bytesAppended = serializedString.appendQuotedUTF8(byteArray, 0);
        assertEquals(10, bytesAppended);
    }

    // Test appending quoted characters with invalid offset
    @Test(timeout = 4000)
    public void testAppendQuotedCharsWithInvalidOffset() {
        SerializedString serializedString = new SerializedString("Q");
        char[] quotedChars = serializedString.asQuotedChars();
        try {
            serializedString.appendQuoted(quotedChars, -932);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    // Test appending quoted characters with null char array
    @Test(timeout = 4000)
    public void testAppendQuotedCharsWithNullCharArray() {
        SerializedString serializedString = new SerializedString("Q");
        try {
            serializedString.appendQuoted((char[]) null, -932);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test appending quoted characters with valid offset
    @Test(timeout = 4000)
    public void testAppendQuotedCharsWithValidOffset() {
        SerializedString serializedString = new SerializedString("n5($,aeq=SuL");
        char[] quotedChars = serializedString.asQuotedChars();
        assertNotNull(quotedChars);

        int charsAppended = serializedString.appendQuoted(quotedChars, 188);
        assertEquals(-1, charsAppended);
    }

    // Test asUnquotedUTF8 method
    @Test(timeout = 4000)
    public void testAsUnquotedUTF8() {
        SerializedString serializedString = new SerializedString("R^$t>9-hl+");
        byte[] utf8Bytes1 = serializedString.asUnquotedUTF8();
        byte[] utf8Bytes2 = serializedString.asUnquotedUTF8();
        assertSame(utf8Bytes2, utf8Bytes1);
        assertNotNull(utf8Bytes2);
    }

    // Test asQuotedUTF8 method
    @Test(timeout = 4000)
    public void testAsQuotedUTF8() {
        SerializedString serializedString = new SerializedString("Null String illegal for SerializedString");
        byte[] quotedUTF8Bytes1 = serializedString.asQuotedUTF8();
        byte[] quotedUTF8Bytes2 = serializedString.asQuotedUTF8();
        assertSame(quotedUTF8Bytes2, quotedUTF8Bytes1);
        assertNotNull(quotedUTF8Bytes2);
    }

    // Test asQuotedChars method
    @Test(timeout = 4000)
    public void testAsQuotedChars() {
        SerializedString serializedString = new SerializedString("2]sk2");
        char[] quotedChars1 = serializedString.asQuotedChars();
        char[] quotedChars2 = serializedString.asQuotedChars();
        assertSame(quotedChars2, quotedChars1);
    }

    // Test readResolve method with non-empty serialization value
    @Test(timeout = 4000)
    public void testReadResolveWithNonEmptySerializationValue() {
        SerializedString serializedString = new SerializedString("vH47?#$,~&t");
        try {
            serializedString.readResolve();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception: Null String illegal for SerializedString
        }
    }

    // Test hashCode method
    @Test(timeout = 4000)
    public void testHashCode() {
        SerializedString serializedString = new SerializedString(">l2[BA");
        serializedString.hashCode();
    }

    // Test toString method with empty string
    @Test(timeout = 4000)
    public void testToStringWithEmptyString() {
        SerializedString serializedString = new SerializedString("");
        String stringValue = serializedString.toString();
        assertEquals("", stringValue);
    }

    // Test charLength method with non-empty string
    @Test(timeout = 4000)
    public void testCharLengthWithNonEmptyString() {
        SerializedString serializedString = new SerializedString("j");
        int length = serializedString.charLength();
        assertEquals(1, length);
    }

    // Test getValue method with non-empty string
    @Test(timeout = 4000)
    public void testGetValueWithNonEmptyString() {
        SerializedString serializedString = new SerializedString("TP1aO6Zd");
        String value = serializedString.getValue();
        assertEquals("TP1aO6Zd", value);
    }
}