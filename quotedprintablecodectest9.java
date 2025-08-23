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
 * Tests the encode(Object) method for the QuotedPrintableCodec class.
 */
@DisplayName("QuotedPrintableCodec.encode(Object)")
class QuotedPrintableCodecEncodeObjectTest {

    private QuotedPrintableCodec codec;

    @BeforeEach
    void setUp() {
        // Using the default constructor (UTF-8, non-strict)
        codec = new QuotedPrintableCodec();
    }

    @Test
    void shouldEncodeStringObject() throws EncoderException {
        // Arrange
        final String input = "1+1 = 2";
        final String expected = "1+1 =3D 2";

        // Act
        final Object result = codec.encode((Object) input);

        // Assert
        assertEquals(expected, result, "Encoding a String object should produce a quoted-printable string.");
    }

    @Test
    void shouldEncodeByteArrayObject() throws EncoderException {
        // Arrange
        final byte[] inputBytes = "1+1 = 2".getBytes(StandardCharsets.UTF_8);
        final byte[] expectedBytes = "1+1 =3D 2".getBytes(StandardCharsets.UTF_8);

        // Act
        final Object result = codec.encode((Object) inputBytes);

        // Assert
        assertArrayEquals(expectedBytes, (byte[]) result, "Encoding a byte[] object should produce a quoted-printable byte array.");
    }

    @Test
    void shouldReturnNullWhenEncodingNullObject() throws EncoderException {
        // Arrange
        // The object to encode is null

        // Act
        final Object result = codec.encode(null);

        // Assert
        assertNull(result, "Encoding a null object should return null.");
    }

    @Test
    void shouldThrowExceptionWhenEncodingUnsupportedType() {
        // Arrange
        final Double unsupportedInput = 3.0d;

        // Act & Assert
        assertThrows(EncoderException.class, () -> {
            codec.encode(unsupportedInput);
        }, "Encoding an unsupported type (e.g., Double) should throw an EncoderException.");
    }
}