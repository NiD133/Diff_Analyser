package com.fasterxml.jackson.core.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for verifying the functionality of the {@link SerializedString} class,
 * which implements the {@link SerializableString} interface.
 */
class SerializedStringTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    private static final String QUOTED_STRING = "\\\"quo\\\\ted\\\"";

    @Test
    void testAppendingQuotedAndUnquotedUTF8() throws IOException {
        final String inputString = "\"quo\\ted\"";
        SerializableString serializedString = new SerializedString(inputString);

        // Verify initial value and quoted characters
        assertEquals(inputString, serializedString.getValue());
        assertEquals(QUOTED_STRING, new String(serializedString.asQuotedChars()));

        // Test writing quoted UTF-8
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertEquals(QUOTED_STRING.length(), serializedString.writeQuotedUTF8(outputStream));
        assertEquals(QUOTED_STRING, outputStream.toString("UTF-8"));

        // Test writing unquoted UTF-8
        outputStream.reset();
        assertEquals(inputString.length(), serializedString.writeUnquotedUTF8(outputStream));
        assertEquals(inputString, outputStream.toString("UTF-8"));

        // Test appending quoted UTF-8 to a buffer
        byte[] buffer = new byte[100];
        assertEquals(QUOTED_STRING.length(), serializedString.appendQuotedUTF8(buffer, 3));
        assertEquals(QUOTED_STRING, new String(buffer, 3, QUOTED_STRING.length()));

        // Test appending unquoted UTF-8 to a buffer
        Arrays.fill(buffer, (byte) 0);
        assertEquals(inputString.length(), serializedString.appendUnquotedUTF8(buffer, 5));
        assertEquals(inputString, new String(buffer, 5, inputString.length()));
    }

    @Test
    void testFailedAccessDueToInsufficientBufferSize() throws IOException {
        final String inputString = "Bit longer text";
        SerializableString serializedString = new SerializedString(inputString);

        // Buffers smaller than the input string length
        final byte[] byteBuffer = new byte[inputString.length() - 2];
        final char[] charBuffer = new char[inputString.length() - 2];
        final ByteBuffer byteBufferObj = ByteBuffer.allocate(inputString.length() - 2);

        // Verify that appending to insufficient buffers returns -1
        assertEquals(-1, serializedString.appendQuotedUTF8(byteBuffer, 0));
        assertEquals(-1, serializedString.appendQuoted(charBuffer, 0));
        assertEquals(-1, serializedString.putQuotedUTF8(byteBufferObj));

        byteBufferObj.rewind();
        assertEquals(-1, serializedString.appendUnquotedUTF8(byteBuffer, 0));
        assertEquals(-1, serializedString.appendUnquoted(charBuffer, 0));
        assertEquals(-1, serializedString.putUnquotedUTF8(byteBufferObj));
    }

    @Test
    void testAppendQuotedUTF8ToBuffer() throws IOException {
        SerializedString serializedString = new SerializedString(QUOTED_STRING);
        assertEquals(QUOTED_STRING, serializedString.getValue());

        final byte[] buffer = new byte[100];
        final int length = serializedString.appendQuotedUTF8(buffer, 3);
        assertEquals("\\\\\\\"quo\\\\\\\\ted\\\\\\\"", new String(buffer, 3, length));
    }

    @Test
    void testJdkSerialization() throws IOException {
        // Serialize and deserialize the SerializedString object
        final byte[] serializedBytes = jdkSerialize(new SerializedString(QUOTED_STRING));
        SerializedString deserializedString = jdkDeserialize(serializedBytes);

        // Verify the deserialized value
        assertEquals(QUOTED_STRING, deserializedString.getValue());

        // Test appending quoted UTF-8 to a buffer
        final byte[] buffer = new byte[100];
        final int length = deserializedString.appendQuotedUTF8(buffer, 3);
        assertEquals("\\\\\\\"quo\\\\\\\\ted\\\\\\\"", new String(buffer, 3, length));
    }
}