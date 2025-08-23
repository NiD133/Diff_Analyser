package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the encode(Object) method in {@link QuotedPrintableCodec}.
 */
class QuotedPrintableCodecTest {

    @Test
    void encodeWithUnsupportedObjectTypeShouldThrowEncoderException() {
        // Arrange: Create a codec instance and an object of an unsupported type.
        // The encode(Object) method should fail for any object that isn't a String or byte array.
        // We use the codec instance itself as an example of an unsupported type.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final Object unsupportedObject = codec;

        // Act & Assert: Verify that calling encode with the unsupported object throws an EncoderException.
        final EncoderException thrown = assertThrows(EncoderException.class, () -> {
            codec.encode(unsupportedObject);
        });

        // Further Assert: Verify that the exception message is correct.
        final String expectedMessage = "Objects of type org.apache.commons.codec.net.QuotedPrintableCodec" +
                " cannot be quoted-printable encoded";
        assertEquals(expectedMessage, thrown.getMessage());
    }
}