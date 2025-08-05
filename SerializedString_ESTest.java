package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

/**
 * Understandable and maintainable unit tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    // ==========================================================
    // Constructor and Basic Getters
    // ==========================================================

    @Test(expected = NullPointerException.class)
    public void constructor_withNullString_throwsNullPointerException() {
        new SerializedString(null);
    }

    @Test
    public void getValue_returnsOriginalString() {
        String original = "test-string";
        SerializedString ss = new SerializedString(original);
        assertEquals(original, ss.getValue());
    }

    @Test
    public void charLength_returnsCorrectLength() {
        assertEquals(0, new SerializedString("").charLength());
        assertEquals(4, new SerializedString("test").charLength());
    }

    @Test
    public void toString_returnsOriginalString() {
        String original = "test-string";
        SerializedString ss = new SerializedString(original);
        assertEquals(original, ss.toString());
    }

    // ==========================================================
    // Caching Behavior (as... methods)
    // ==========================================================

    @Test
    public void asQuotedUTF8_isCached() {
        SerializedString ss = new SerializedString("value");
        byte[] firstCall = ss.asQuotedUTF8();
        byte[] secondCall = ss.asQuotedUTF8();
        assertSame("asQuotedUTF8 should return a cached byte array", firstCall, secondCall);
    }

    @Test
    public void asUnquotedUTF8_isCached() {
        SerializedString ss = new SerializedString("value");
        byte[] firstCall = ss.asUnquotedUTF8();
        byte[] secondCall = ss.asUnquotedUTF8();
        assertSame("asUnquotedUTF8 should return a cached byte array", firstCall, secondCall);
    }

    @Test
    public void asQuotedChars_isCached() {
        SerializedString ss = new SerializedString("value");
        char[] firstCall = ss.asQuotedChars();
        char[] secondCall = ss.asQuotedChars();
        assertSame("asQuotedChars should return a cached char array", firstCall, secondCall);
    }

    @Test
    public void asQuotedUTF8_returnsCorrectlyQuotedBytes() throws UnsupportedEncodingException {
        // A string with a character that needs JSON escaping (the double quote).
        SerializedString ss = new SerializedString("a\"b");
        // The quoted form is "a\"b"
        assertArrayEquals("a\\\"b".getBytes("UTF-8"), ss.asQuotedUTF8());
    }

    @Test
    public void asUnquotedUTF8_returnsCorrectBytes() throws UnsupportedEncodingException {
        String original = "simple string";
        SerializedString ss = new SerializedString(original);
        assertArrayEquals(original.getBytes("UTF-8"), ss.asUnquotedUTF8());
    }

    // ==========================================================
    // Writing to OutputStream (write...UTF8 methods)
    // ==========================================================

    @Test
    public void writeUnquotedUTF8_writesRawBytes() throws IOException {
        String original = "data";
        SerializedString ss = new SerializedString(original);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesWritten = ss.writeUnquotedUTF8(baos);

        assertEquals(original.length(), bytesWritten);
        assertArrayEquals(original.getBytes("UTF-8"), baos.toByteArray());
    }

    @Test
    public void writeQuotedUTF8_writesQuotedBytes() throws IOException {
        String original = "a\"b";
        SerializedString ss = new SerializedString(original);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesWritten = ss.writeQuotedUTF8(baos);

        byte[] expectedBytes = "a\\\"b".getBytes("UTF-8");
        assertEquals(expectedBytes.length, bytesWritten);
        assertArrayEquals(expectedBytes, baos.toByteArray());
    }

    @Test
    public void writeUnquotedUTF8_forEmptyString_writesNothing() throws IOException {
        SerializedString ss = new SerializedString("");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assertEquals(0, ss.writeUnquotedUTF8(baos));
        assertEquals(0, baos.size());
    }

    @Test(expected = NullPointerException.class)
    public void writeUnquotedUTF8_withNullStream_throwsNullPointerException() throws IOException {
        new SerializedString("test").writeUnquotedUTF8(null);
    }

    @Test(expected = IOException.class)
    public void writeQuotedUTF8_toBrokenPipe_throwsIOException() throws IOException {
        // A PipedOutputStream that is not connected to a PipedInputStream will throw an IOException on write.
        PipedOutputStream brokenPipe = new PipedOutputStream();
        new SerializedString("test").writeQuotedUTF8(brokenPipe);
    }

    // ==========================================================
    // Putting into ByteBuffer (put...UTF8 methods)
    // ==========================================================

    @Test
    public void putUnquotedUTF8_intoSufficientBuffer_succeeds() {
        String original = "data";
        SerializedString ss = new SerializedString(original);
        ByteBuffer buffer = ByteBuffer.allocate(10);

        int bytesPut = ss.putUnquotedUTF8(buffer);

        assertEquals(original.length(), bytesPut);
        assertEquals(original.length(), buffer.position());
    }

    @Test
    public void putQuotedUTF8_intoSufficientBuffer_succeeds() {
        String original = "a\"b"; // quoted form "a\\\"b" is 4 bytes
        SerializedString ss = new SerializedString(original);
        ByteBuffer buffer = ByteBuffer.allocate(10);

        int bytesPut = ss.putQuotedUTF8(buffer);

        assertEquals(4, bytesPut);
        assertEquals(4, buffer.position());
    }

    @Test
    public void putUnquotedUTF8_intoInsufficientBuffer_returnsNegativeOne() {
        SerializedString ss = new SerializedString("long-string");
        ByteBuffer buffer = ByteBuffer.allocate(5);
        assertEquals(-1, ss.putUnquotedUTF8(buffer));
    }

    @Test
    public void putQuotedUTF8_intoInsufficientBuffer_returnsNegativeOne() {
        SerializedString ss = new SerializedString("a\"b"); // quoted form is 4 bytes
        ByteBuffer buffer = ByteBuffer.allocate(3);
        assertEquals(-1, ss.putQuotedUTF8(buffer));
    }

    @Test(expected = NullPointerException.class)
    public void putUnquotedUTF8_withNullBuffer_throwsNullPointerException() {
        new SerializedString("test").putUnquotedUTF8(null);
    }

    // ==========================================================
    // Appending to arrays (append... methods)
    // ==========================================================

    @Test
    public void appendUnquoted_toCharArray_appendsChars() {
        SerializedString ss = new SerializedString("World");
        char[] buffer = new char[]{'H', 'e', 'l', 'l', 'o', ',', ' ', '!', '!', '!'}; // "Hello,!!!"

        int charsAppended = ss.appendUnquoted(buffer, 7); // Append at index 7

        assertEquals(5, charsAppended);
        assertArrayEquals("Hello, World!".toCharArray(), buffer);
    }

    @Test
    public void appendQuotedUTF8_toByteArray_appendsQuotedBytes() {
        SerializedString ss = new SerializedString("a\"b"); // quoted form "a\\\"b"
        byte[] buffer = new byte[10];

        int bytesAppended = ss.appendQuotedUTF8(buffer, 0);

        assertEquals(4, bytesAppended);
        assertArrayEquals(new byte[]{'a', '\\', '"', 'b', 0, 0, 0, 0, 0, 0}, buffer);
    }

    @Test
    public void appendUnquoted_toInsufficientCharArray_returnsNegativeOne() {
        SerializedString ss = new SerializedString("long-string");
        char[] buffer = new char[5];
        assertEquals(-1, ss.appendUnquoted(buffer, 0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendUnquoted_withNegativeOffset_throwsException() {
        SerializedString ss = new SerializedString("test");
        char[] buffer = new char[10];
        ss.appendUnquoted(buffer, -1);
    }

    @Test(expected = NullPointerException.class)
    public void appendUnquoted_withNullBuffer_throwsException() {
        new SerializedString("test").appendUnquoted(null, 0);
    }

    // ==========================================================
    // equals() and hashCode()
    // ==========================================================

    @Test
    public void equals_contract() {
        SerializedString ss1 = new SerializedString("test");
        SerializedString ss2 = new SerializedString("test");
        SerializedString ss3 = new SerializedString("TEST");

        // Reflexive, Symmetric, and against different value
        assertTrue(ss1.equals(ss1));
        assertTrue(ss1.equals(ss2));
        assertTrue(ss2.equals(ss1));
        assertFalse(ss1.equals(ss3));

        // Against null and different type
        assertFalse(ss1.equals(null));
        assertFalse(ss1.equals("test"));
    }

    @Test
    public void hashCode_contract() {
        SerializedString ss1 = new SerializedString("test");
        SerializedString ss2 = new SerializedString("test");

        // Equal objects must have equal hash codes
        assertEquals(ss1.hashCode(), ss2.hashCode());
        // Hash code should be consistent with the underlying String's hash code
        assertEquals("test".hashCode(), ss1.hashCode());
    }

    // ==========================================================
    // Java Serialization
    // ==========================================================

    @Test
    public void javaSerialization_deserializedObjectIsEqual() throws IOException, ClassNotFoundException {
        SerializedString original = new SerializedString("serialized-value");

        // Serialize
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(byteStream)) {
            oos.writeObject(original);
        }
        byte[] serializedData = byteStream.toByteArray();

        // Deserialize
        ByteArrayInputStream inStream = new ByteArrayInputStream(serializedData);
        SerializedString deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(inStream)) {
            deserialized = (SerializedString) ois.readObject();
        }

        // The deserialized object should be equal to the original
        assertEquals(original, deserialized);
        assertEquals(original.getValue(), deserialized.getValue());
        assertNotSame(original, deserialized);
    }
}