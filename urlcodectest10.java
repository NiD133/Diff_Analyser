package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@code encode(Object)} method of {@link URLCodec}.
 */
class URLCodecEncodeObjectTest {

    private URLCodec urlCodec;

    @BeforeEach
    void setUp() {
        // The URLCodec is stateless, but we initialize it before each test
        // to ensure test isolation.
        this.urlCodec = new URLCodec();
    }

    @Test
    @DisplayName("Encoding a String object should produce a URL-encoded string")
    void encodeObjectShouldCorrectlyEncodeString() throws Exception {
        // Arrange
        final String plainText = "Hello there!";
        final String expectedEncodedText = "Hello+there%21";

        // Act
        final Object result = urlCodec.encode(plainText);

        // Assert
        assertEquals(expectedEncodedText, result, "The encoded string should match the expected URL-encoded format.");
    }

    @Test
    @DisplayName("Encoding a byte array object should produce a URL-encoded byte array")
    void encodeObjectShouldCorrectlyEncodeByteArray() throws Exception {
        // Arrange
        final byte[] plainBytes = "Hello there!".getBytes(StandardCharsets.UTF_8);
        final byte[] expectedEncodedBytes = "Hello+there%21".getBytes(StandardCharsets.US_ASCII);

        // Act
        final Object result = urlCodec.encode(plainBytes);

        // Assert
        assertArrayEquals(expectedEncodedBytes, (byte[]) result, "The encoded byte array should match the expected URL-encoded format.");
    }

    @Test
    @DisplayName("Encoding a null object should return null")
    void encodeObjectShouldReturnNullForNullInput() throws Exception {
        // Arrange
        final Object input = null;

        // Act
        final Object result = urlCodec.encode(input);

        // Assert
        assertNull(result, "Encoding a null object should result in null.");
    }

    @Test
    @DisplayName("Encoding an unsupported object type should throw EncoderException")
    void encodeObjectShouldThrowExceptionForUnsupportedType() {
        // Arrange
        final Double unsupportedObject = 3.0d;

        // Act & Assert
        assertThrows(EncoderException.class, () -> {
            urlCodec.encode(unsupportedObject);
        }, "An EncoderException should be thrown for unsupported types like Double.");
    }
}