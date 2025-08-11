package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;

/**
 * Test suite for SerializedString class functionality including:
 * - String serialization and encoding (UTF-8, quoted/unquoted)
 * - Buffer operations (append, write, put)
 * - Edge cases and error conditions
 */
public class SerializedStringTest {

    // Test Data Constants
    private static final String EMPTY_STRING = "";
    private static final String SIMPLE_STRING = "Q";
    private static final String COMPLEX_STRING = "8+19.:23Vy=-x";
    private static final String STRING_WITH_QUOTES = ";\"mRHC$u";
    private static final String MULTI_CHAR_STRING = "TP1aO6Zd";

    // ========== Constructor Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void constructor_WithNullString_ShouldThrowException() {
        new SerializedString(null);
    }

    @Test
    public void constructor_WithValidString_ShouldCreateInstance() {
        SerializedString serializedString = new SerializedString(SIMPLE_STRING);
        assertEquals(SIMPLE_STRING, serializedString.getValue());
    }

    // ========== Basic Property Tests ==========
    
    @Test
    public void getValue_ShouldReturnOriginalString() {
        SerializedString serializedString = new SerializedString(COMPLEX_STRING);
        assertEquals(COMPLEX_STRING, serializedString.getValue());
    }

    @Test
    public void charLength_WithEmptyString_ShouldReturnZero() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        assertEquals(0, serializedString.charLength());
    }

    @Test
    public void charLength_WithNonEmptyString_ShouldReturnCorrectLength() {
        SerializedString serializedString = new SerializedString(SIMPLE_STRING);
        assertEquals(1, serializedString.charLength());
    }

    @Test
    public void toString_ShouldReturnOriginalString() {
        SerializedString serializedString = new SerializedString(STRING_WITH_QUOTES);
        assertEquals(STRING_WITH_QUOTES, serializedString.toString());
    }

    // ========== Encoding Tests ==========
    
    @Test
    public void asQuotedChars_WithEmptyString_ShouldReturnEmptyArray() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        char[] result = serializedString.asQuotedChars();
        assertEquals(0, result.length);
    }

    @Test
    public void asQuotedChars_ShouldCacheResult() {
        SerializedString serializedString = new SerializedString(MULTI_CHAR_STRING);
        char[] first = serializedString.asQuotedChars();
        char[] second = serializedString.asQuotedChars();
        assertSame("Should return cached instance", first, second);
    }

    @Test
    public void asUnquotedUTF8_ShouldCacheResult() {
        SerializedString serializedString = new SerializedString(COMPLEX_STRING);
        byte[] first = serializedString.asUnquotedUTF8();
        byte[] second = serializedString.asUnquotedUTF8();
        assertSame("Should return cached instance", first, second);
    }

    @Test
    public void asQuotedUTF8_ShouldCacheResult() {
        SerializedString serializedString = new SerializedString("Test String");
        byte[] first = serializedString.asQuotedUTF8();
        byte[] second = serializedString.asQuotedUTF8();
        assertSame("Should return cached instance", first, second);
    }

    // ========== Append Operations Tests ==========
    
    @Test
    public void appendUnquoted_WithEmptyString_ShouldReturnZero() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        char[] buffer = new char[7];
        int bytesAppended = serializedString.appendUnquoted(buffer, 1);
        assertEquals(0, bytesAppended);
    }

    @Test
    public void appendUnquoted_WithValidString_ShouldAppendToBuffer() {
        SerializedString serializedString = new SerializedString(SIMPLE_STRING);
        char[] buffer = new char[4];
        int bytesAppended = serializedString.appendUnquoted(buffer, 1);
        
        assertEquals(1, bytesAppended);
        assertArrayEquals(new char[] {'\u0000', 'Q', '\u0000', '\u0000'}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void appendUnquoted_WithNullBuffer_ShouldThrowException() {
        SerializedString serializedString = new SerializedString(SIMPLE_STRING);
        serializedString.appendUnquoted(null, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendUnquoted_WithNegativeOffset_ShouldThrowException() {
        SerializedString serializedString = new SerializedString(SIMPLE_STRING);
        char[] buffer = new char[0];
        serializedString.appendUnquoted(buffer, -1);
    }

    @Test
    public void appendUnquoted_WithInsufficientSpace_ShouldReturnNegativeOne() {
        SerializedString serializedString = new SerializedString("Long string");
        char[] buffer = new char[2];
        int result = serializedString.appendUnquoted(buffer, -1);
        assertEquals(-1, result);
    }

    @Test
    public void appendUnquotedUTF8_WithEmptyString_ShouldReturnZero() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        byte[] buffer = new byte[2];
        int bytesAppended = serializedString.appendUnquotedUTF8(buffer, 0);
        assertEquals(0, bytesAppended);
    }

    @Test
    public void appendUnquotedUTF8_WithValidString_ShouldAppendBytes() {
        SerializedString serializedString = new SerializedString(MULTI_CHAR_STRING);
        byte[] buffer = serializedString.asUnquotedUTF8();
        int bytesAppended = serializedString.appendUnquotedUTF8(buffer, 0);
        assertEquals(8, bytesAppended);
    }

    @Test(expected = NullPointerException.class)
    public void appendUnquotedUTF8_WithNullBuffer_ShouldThrowException() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        serializedString.appendUnquotedUTF8(null, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendUnquotedUTF8_WithNegativeOffset_ShouldThrowException() {
        SerializedString serializedString = new SerializedString(COMPLEX_STRING);
        byte[] buffer = new byte[0];
        serializedString.appendUnquotedUTF8(buffer, -1910);
    }

    @Test
    public void appendQuoted_WithEmptyString_ShouldReturnZero() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        char[] buffer = new char[5];
        int result = serializedString.appendQuoted(buffer, 1);
        assertEquals(0, result);
    }

    @Test
    public void appendQuoted_WithPresetQuotedChars_ShouldAppendAll() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        char[] quotedChars = new char[4];
        serializedString._quotedChars = quotedChars;
        
        char[] buffer = new char[4];
        int result = serializedString.appendQuoted(buffer, 0);
        assertEquals(4, result);
    }

    @Test(expected = NullPointerException.class)
    public void appendQuoted_WithNullBuffer_ShouldThrowException() {
        SerializedString serializedString = new SerializedString(SIMPLE_STRING);
        serializedString.appendQuoted(null, 0);
    }

    @Test
    public void appendQuotedUTF8_WithEmptyString_ShouldReturnZero() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        byte[] buffer = new byte[9];
        int result = serializedString.appendQuotedUTF8(buffer, 1);
        assertEquals(0, result);
    }

    @Test
    public void appendQuotedUTF8_WithValidString_ShouldAppendBytes() {
        SerializedString serializedString = new SerializedString(", copied ");
        serializedString.asQuotedUTF8(); // Initialize quoted UTF8 cache
        
        byte[] buffer = new byte[20];
        int bytesAppended = serializedString.appendQuotedUTF8(buffer, 0);
        assertEquals(10, bytesAppended);
    }

    @Test(expected = NullPointerException.class)
    public void appendQuotedUTF8_WithNullBuffer_ShouldThrowException() {
        SerializedString serializedString = new SerializedString("Test");
        serializedString.appendQuotedUTF8(null, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendQuotedUTF8_WithNegativeOffset_ShouldThrowException() {
        SerializedString serializedString = new SerializedString(COMPLEX_STRING);
        byte[] buffer = serializedString.asUnquotedUTF8();
        serializedString.appendQuotedUTF8(buffer, -1);
    }

    // ========== Write Operations Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void writeUnquotedUTF8_WithNullOutputStream_ShouldThrowException() throws IOException {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        serializedString.writeUnquotedUTF8(null);
    }

    @Test
    public void writeUnquotedUTF8_WithValidOutputStream_ShouldWriteBytes() throws IOException {
        SerializedString serializedString = new SerializedString("TestString");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        int bytesWritten = serializedString.writeUnquotedUTF8(outputStream);
        assertEquals(10, bytesWritten);
        assertTrue(outputStream.size() > 0);
    }

    @Test(expected = IOException.class)
    public void writeUnquotedUTF8_WithClosedPipe_ShouldThrowIOException() throws IOException {
        SerializedString serializedString = new SerializedString("Test");
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        serializedString.writeUnquotedUTF8(pipedOutputStream);
    }

    @Test(expected = NullPointerException.class)
    public void writeQuotedUTF8_WithNullOutputStream_ShouldThrowException() throws IOException {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        serializedString.writeQuotedUTF8(null);
    }

    @Test
    public void writeQuotedUTF8_WithEmptyString_ShouldReturnZero() throws IOException {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        int bytesWritten = serializedString.writeQuotedUTF8(outputStream);
        assertEquals(0, bytesWritten);
    }

    @Test
    public void writeQuotedUTF8_WithValidString_ShouldWriteBytes() throws IOException {
        SerializedString serializedString = new SerializedString(SIMPLE_STRING);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        int bytesWritten = serializedString.writeQuotedUTF8(outputStream);
        assertEquals(1, bytesWritten);
    }

    @Test(expected = IOException.class)
    public void writeQuotedUTF8_WithClosedPipe_ShouldThrowIOException() throws IOException {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        serializedString.writeQuotedUTF8(pipedOutputStream);
    }

    // ========== ByteBuffer Operations Tests ==========
    
    @Test
    public void putUnquotedUTF8_WithEmptyString_ShouldReturnZero() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        ByteBuffer buffer = ByteBuffer.allocateDirect(0);
        int result = serializedString.putUnquotedUTF8(buffer);
        assertEquals(0, result);
    }

    @Test
    public void putUnquotedUTF8_WithValidString_ShouldPutBytes() {
        SerializedString serializedString = new SerializedString(" ");
        ByteBuffer buffer = ByteBuffer.allocateDirect(3669);
        
        int bytesPut = serializedString.putUnquotedUTF8(buffer);
        assertEquals(1, bytesPut);
        assertEquals(3668, buffer.remaining());
    }

    @Test
    public void putUnquotedUTF8_WithInsufficientSpace_ShouldReturnNegativeOne() {
        SerializedString serializedString = new SerializedString("Long string");
        ByteBuffer buffer = ByteBuffer.allocate(0);
        int result = serializedString.putUnquotedUTF8(buffer);
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void putUnquotedUTF8_WithNullBuffer_ShouldThrowException() {
        SerializedString serializedString = new SerializedString("Test");
        serializedString.putUnquotedUTF8(null);
    }

    @Test
    public void putQuotedUTF8_WithEmptyString_ShouldReturnZero() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        ByteBuffer buffer = ByteBuffer.allocateDirect(0);
        int result = serializedString.putQuotedUTF8(buffer);
        assertEquals(0, result);
    }

    @Test
    public void putQuotedUTF8_WithValidString_ShouldPutBytes() {
        SerializedString serializedString = new SerializedString("TestString123");
        ByteBuffer buffer = ByteBuffer.allocateDirect(3615);
        
        int bytesPut = serializedString.putQuotedUTF8(buffer);
        assertEquals(13, bytesPut);
        assertEquals(3602, buffer.remaining());
    }

    @Test
    public void putQuotedUTF8_WithInsufficientSpace_ShouldReturnNegativeOne() {
        SerializedString serializedString = new SerializedString("Long string");
        ByteBuffer buffer = ByteBuffer.allocateDirect(7);
        int result = serializedString.putQuotedUTF8(buffer);
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void putQuotedUTF8_WithNullBuffer_ShouldThrowException() {
        SerializedString serializedString = new SerializedString(SIMPLE_STRING);
        serializedString.asQuotedUTF8(); // Initialize cache
        serializedString.putQuotedUTF8(null);
    }

    // ========== Equality and Hash Tests ==========
    
    @Test
    public void equals_WithSameContent_ShouldReturnTrue() {
        SerializedString first = new SerializedString(EMPTY_STRING);
        SerializedString second = new SerializedString(EMPTY_STRING);
        assertTrue(first.equals(second));
    }

    @Test
    public void equals_WithSameInstance_ShouldReturnTrue() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        assertTrue(serializedString.equals(serializedString));
    }

    @Test
    public void equals_WithNull_ShouldReturnFalse() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        assertFalse(serializedString.equals(null));
    }

    @Test
    public void equals_WithDifferentType_ShouldReturnFalse() {
        SerializedString serializedString = new SerializedString(COMPLEX_STRING);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(0);
        assertFalse(serializedString.equals(byteBuffer));
    }

    @Test
    public void hashCode_ShouldNotThrowException() {
        SerializedString serializedString = new SerializedString("TestHash");
        // Should not throw any exception
        serializedString.hashCode();
    }

    // ========== Serialization Tests ==========
    
    @Test
    public void readResolve_WithJdkSerializeValue_ShouldCreateNewInstance() {
        SerializedString serializedString = new SerializedString("Original");
        serializedString._jdkSerializeValue = EMPTY_STRING;
        
        Object resolved = serializedString.readResolve();
        assertEquals(EMPTY_STRING, resolved.toString());
    }

    @Test(expected = NullPointerException.class)
    public void readResolve_WithNullJdkSerializeValue_ShouldThrowException() {
        SerializedString serializedString = new SerializedString("Test");
        // _jdkSerializeValue remains null
        serializedString.readResolve();
    }

    // ========== Edge Cases and Error Conditions ==========
    
    @Test
    public void appendUnquotedUTF8_WithEmptyStringAndInvalidOffset_ShouldReturnNegativeOne() {
        SerializedString serializedString = new SerializedString(EMPTY_STRING);
        byte[] buffer = new byte[0];
        int result = serializedString.appendUnquotedUTF8(buffer, 1);
        assertEquals(-1, result);
    }

    @Test
    public void appendQuotedUTF8_WithInsufficientBufferSpace_ShouldReturnNegativeOne() {
        SerializedString serializedString = new SerializedString("Long string content");
        byte[] buffer = new byte[14];
        int result = serializedString.appendQuotedUTF8(buffer, 77);
        assertEquals(-1, result);
    }

    @Test
    public void appendQuoted_WithInsufficientBufferSpace_ShouldReturnNegativeOne() {
        SerializedString serializedString = new SerializedString("Long string content");
        char[] quotedChars = serializedString.asQuotedChars();
        int result = serializedString.appendQuoted(quotedChars, 188);
        assertEquals(-1, result);
    }
}