package com.fasterxml.jackson.core.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SerializedString} to verify JSON escaping,
 * UTF-8 serialization, and edge-case handling.
 */
class SerializedStringTest extends com.fasterxml.jackson.core.JUnit5TestBase {
    // Input string containing quotes and backslashes: "quo\ted"
    private static final String INPUT_STRING = "\"quo\\ted\"";
    // Expected JSON-escaped version: \"quo\\ted\"
    private static final String ESCAPED_STRING = "\\\"quo\\\\ted\\\"";

    @Test
    void constructorShouldStoreOriginalValue() {
        SerializableString sstr = new SerializedString(INPUT_STRING);
        assertEquals(INPUT_STRING, sstr.getValue());
    }

    @Test
    void asQuotedCharsShouldReturnEscapedCharacters() {
        SerializableString sstr = new SerializedString(INPUT_STRING);
        assertArrayEquals(ESCAPED_STRING.toCharArray(), sstr.asQuotedChars());
    }

    @Test
    void writeQuotedUTF8ShouldEscapeToOutputStream() throws IOException {
        SerializableString sstr = new SerializedString(INPUT_STRING);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int written = sstr.writeQuotedUTF8(out);
        assertEquals(ESCAPED_STRING.length(), written);
        assertEquals(ESCAPED_STRING, out.toString("UTF-8"));
    }

    @Test
    void writeUnquotedUTF8ShouldWriteRawToOutputStream() throws IOException {
        SerializableString sstr = new SerializedString(INPUT_STRING);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int written = sstr.writeUnquotedUTF8(out);
        assertEquals(INPUT_STRING.length(), written);
        assertEquals(INPUT_STRING, out.toString("UTF-8"));
    }

    @Test
    void appendQuotedUTF8ShouldEscapeToByteArray() {
        SerializableString sstr = new SerializedString(INPUT_STRING);
        byte[] buffer = new byte[100];
        Arrays.fill(buffer, (byte) 0);
        int written = sstr.appendQuotedUTF8(buffer, 3);
        assertEquals(ESCAPED_STRING.length(), written);
        assertEquals(ESCAPED_STRING, new String(buffer, 3, written));
    }

    @Test
    void appendUnquotedUTF8ShouldWriteRawToByteArray() {
        SerializableString sstr = new SerializedString(INPUT_STRING);
        byte[] buffer = new byte[100];
        Arrays.fill(buffer, (byte) 0);
        int written = sstr.appendUnquotedUTF8(buffer, 5);
        assertEquals(INPUT_STRING.length(), written);
        assertEquals(INPUT_STRING, new String(buffer, 5, written));
    }

    @Test
    void appendOperationsShouldFailWhenBufferTooSmall() {
        final String text = "Bit longer text";
        SerializableString sstr = new SerializedString(text);
        
        // Create buffers too small to hold the escaped/unquoted text
        byte[] smallByteBuffer = new byte[text.length() - 2];
        char[] smallCharBuffer = new char[text.length() - 2];
        ByteBuffer smallByteBuf = ByteBuffer.allocate(text.length() - 2);

        // Verify quoted appends fail
        assertEquals(-1, sstr.appendQuotedUTF8(smallByteBuffer, 0));
        assertEquals(-1, sstr.appendQuoted(smallCharBuffer, 0));
        assertEquals(-1, sstr.putQuotedUTF8(smallByteBuf));

        // Reset and verify unquoted appends fail
        smallByteBuf.rewind();
        assertEquals(-1, sstr.appendUnquotedUTF8(smallByteBuffer, 0));
        assertEquals(-1, sstr.appendUnquoted(smallCharBuffer, 0));
        assertEquals(-1, sstr.putUnquotedUTF8(smallByteBuf));
    }

    @Test
    void appendingAlreadyEscapedStringShouldDoubleEscape() {
        // Create with pre-escaped content (which contains backslashes)
        SerializedString sstr = new SerializedString(ESCAPED_STRING);
        byte[] buffer = new byte[100];
        
        // Append should escape existing escapes (output becomes \\\"quo\\\\ted\\\")
        int len = sstr.appendQuotedUTF8(buffer, 3);
        String result = new String(buffer, 3, len);
        
        // Verify double escaping: each backslash becomes two backslashes
        assertEquals("\\\\\\\"quo\\\\\\\\ted\\\\\\\"", result);
    }

    @Test
    void shouldMaintainFunctionalityAfterSerializationRoundtrip() throws Exception {
        SerializedString original = new SerializedString(ESCAPED_STRING);
        
        // Serialize and deserialize
        byte[] serialized = jdkSerialize(original);
        SerializedString deserialized = jdkDeserialize(serialized);
        
        // Verify functionality after deserialization
        assertEquals(ESCAPED_STRING, deserialized.getValue());
        
        byte[] buffer = new byte[100];
        int len = deserialized.appendQuotedUTF8(buffer, 3);
        String result = new String(buffer, 3, len);
        assertEquals("\\\\\\\"quo\\\\\\\\ted\\\\\\\"", result);
    }
}