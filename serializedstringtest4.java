package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the JDK serialization and deserialization of {@link SerializedString}.
 */
public class SerializedStringTestTest4 extends com.fasterxml.jackson.core.JUnit5TestBase {

    // The original string value contains characters that require JSON escaping:
    // a quote (\") and a backslash (\\).
    // The actual string value is: "quo\ted"
    private static final String ORIGINAL_VALUE = "\\\"quo\\\\ted\\\"";

    /**
    * This test verifies that a {@link SerializedString} instance, after being
    * serialized and deserialized using standard Java serialization,
    * retains its original value and behaves correctly.
    */
    @Test
    void shouldPreserveValueAndQuotingBehaviorAfterJdkSerialization() throws IOException {
        // Arrange
        final SerializedString originalString = new SerializedString(ORIGINAL_VALUE);
        final int bufferOffset = 3;
        final byte[] outputBuffer = new byte[100];

        // The expected JSON-escaped representation of ORIGINAL_VALUE ("quo\ted").
        // Escaping rules transform it to: \\"quo\\ted\\"
        final String expectedQuotedValue = "\\\\\\\"quo\\\\\\\\ted\\\\\\\"";

        // Act
        // 1. Serialize the object using standard Java serialization.
        final byte[] serializedBytes = jdkSerialize(originalString);

        // 2. Deserialize it back into a new object.
        final SerializedString deserializedString = jdkDeserialize(serializedBytes);

        // 3. Use the deserialized object to perform a quoting operation.
        final int bytesWritten = deserializedString.appendQuotedUTF8(outputBuffer, bufferOffset);
        final String actualQuotedValue = new String(outputBuffer, bufferOffset, bytesWritten, StandardCharsets.UTF_8);

        // Assert
        // First, verify that the deserialized object's value matches the original.
        assertEquals(ORIGINAL_VALUE, deserializedString.getValue(),
                "Deserialized object should have the same value as the original");

        // Second, verify that the deserialized object still performs quoting correctly.
        assertEquals(expectedQuotedValue, actualQuotedValue,
                "Deserialized object should produce the correct quoted UTF-8 representation");
    }
}