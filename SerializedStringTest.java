package com.fasterxml.jackson.core.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link SerializedString} to verify JSON string serialization,
 * quoting/escaping, and UTF-8 encoding functionality.
 */
class SerializedStringTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    // Test data: original string with quotes and backslashes
    private static final String ORIGINAL_STRING = "\"quo\\ted\"";
    
    // Expected result after JSON escaping: quotes and backslashes are escaped
    private static final String JSON_ESCAPED_STRING = "\\\"quo\\\\ted\\\"";

    @Test
    void shouldSerializeAndAppendStringWithQuotingAndEscaping() throws IOException {
        // Given: A SerializedString with quotes and backslashes
        SerializableString serializedString = new SerializedString(ORIGINAL_STRING);
        
        // Then: Basic properties should be correct
        assertEquals(ORIGINAL_STRING, serializedString.getValue());
        assertEquals(JSON_ESCAPED_STRING, new String(serializedString.asQuotedChars()));

        // When: Writing quoted UTF-8 to output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int quotedBytesWritten = serializedString.writeQuotedUTF8(outputStream);
        
        // Then: Should write escaped string with correct length
        assertEquals(JSON_ESCAPED_STRING.length(), quotedBytesWritten);
        assertEquals(JSON_ESCAPED_STRING, outputStream.toString("UTF-8"));

        // When: Writing unquoted UTF-8 to output stream
        outputStream.reset();
        int unquotedBytesWritten = serializedString.writeUnquotedUTF8(outputStream);
        
        // Then: Should write original string without escaping
        assertEquals(ORIGINAL_STRING.length(), unquotedBytesWritten);
        assertEquals(ORIGINAL_STRING, outputStream.toString("UTF-8"));

        // When: Appending quoted UTF-8 to byte buffer at offset
        byte[] buffer = new byte[100];
        int quotedBytesAppended = serializedString.appendQuotedUTF8(buffer, 3);
        
        // Then: Should append escaped string at correct offset
        assertEquals(JSON_ESCAPED_STRING.length(), quotedBytesAppended);
        assertEquals(JSON_ESCAPED_STRING, new String(buffer, 3, JSON_ESCAPED_STRING.length()));

        // When: Appending unquoted UTF-8 to byte buffer at offset
        Arrays.fill(buffer, (byte) 0); // Clear buffer
        int unquotedBytesAppended = serializedString.appendUnquotedUTF8(buffer, 5);
        
        // Then: Should append original string at correct offset
        assertEquals(ORIGINAL_STRING.length(), unquotedBytesAppended);
        assertEquals(ORIGINAL_STRING, new String(buffer, 5, ORIGINAL_STRING.length()));
    }

    @Test
    void shouldReturnNegativeOneWhenBufferIsTooSmall() throws IOException {
        // Given: A string and buffers that are too small to hold the content
        final String testString = "Bit longer text";
        SerializableString serializedString = new SerializedString(testString);

        final byte[] tooSmallByteBuffer = new byte[testString.length() - 2];
        final char[] tooSmallCharBuffer = new char[testString.length() - 2];
        final ByteBuffer tooSmallByteBuffer2 = ByteBuffer.allocate(testString.length() - 2);

        // When/Then: All append operations should fail and return -1
        assertEquals(-1, serializedString.appendQuotedUTF8(tooSmallByteBuffer, 0),
                "Should return -1 when byte buffer is too small for quoted UTF-8");
        assertEquals(-1, serializedString.appendQuoted(tooSmallCharBuffer, 0),
                "Should return -1 when char buffer is too small for quoted content");
        assertEquals(-1, serializedString.putQuotedUTF8(tooSmallByteBuffer2),
                "Should return -1 when ByteBuffer is too small for quoted UTF-8");

        // Reset ByteBuffer for unquoted operations
        tooSmallByteBuffer2.rewind();
        
        assertEquals(-1, serializedString.appendUnquotedUTF8(tooSmallByteBuffer, 0),
                "Should return -1 when byte buffer is too small for unquoted UTF-8");
        assertEquals(-1, serializedString.appendUnquoted(tooSmallCharBuffer, 0),
                "Should return -1 when char buffer is too small for unquoted content");
        assertEquals(-1, serializedString.putUnquotedUTF8(tooSmallByteBuffer2),
                "Should return -1 when ByteBuffer is too small for unquoted UTF-8");
    }

    @Test
    void shouldDoubleEscapeAlreadyEscapedString() throws IOException {
        // Given: A string that is already JSON-escaped
        SerializedString alreadyEscapedString = new SerializedString(JSON_ESCAPED_STRING);
        
        // Then: The value should be preserved as-is
        assertEquals(JSON_ESCAPED_STRING, alreadyEscapedString.getValue());
        
        // When: Applying JSON escaping again
        final byte[] buffer = new byte[100];
        final int bytesWritten = alreadyEscapedString.appendQuotedUTF8(buffer, 3);
        String doubleEscapedResult = new String(buffer, 3, bytesWritten);
        
        // Then: Should double-escape the already escaped string
        assertEquals("\\\\\\\"quo\\\\\\\\ted\\\\\\\"", doubleEscapedResult,
                "Already escaped string should be escaped again (double-escaping)");
    }

    @Test
    void shouldMaintainFunctionalityAfterJdkSerialization() throws IOException {
        // Given: A SerializedString that has been serialized and deserialized via JDK
        final byte[] serializedBytes = jdkSerialize(new SerializedString(JSON_ESCAPED_STRING));
        SerializedString deserializedString = jdkDeserialize(serializedBytes);
        
        // Then: Should preserve the original value
        assertEquals(JSON_ESCAPED_STRING, deserializedString.getValue(),
                "Value should be preserved after JDK serialization/deserialization");
        
        // And: Should maintain full functionality
        final byte[] buffer = new byte[100];
        final int bytesWritten = deserializedString.appendQuotedUTF8(buffer, 3);
        String result = new String(buffer, 3, bytesWritten);
        
        assertEquals("\\\\\\\"quo\\\\\\\\ted\\\\\\\"", result,
                "Functionality should be preserved after JDK serialization/deserialization");
    }
}