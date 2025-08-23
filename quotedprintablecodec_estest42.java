package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    @Test
    public void decodeObjectWithUnsupportedTypeShouldThrowDecoderException() {
        // Arrange: Create a codec and an object of a type that cannot be decoded.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final Object unsupportedObject = new QuotedPrintableCodec(); // Any object other than String or byte[]

        // Act & Assert: Verify that attempting to decode the unsupported object throws a DecoderException.
        try {
            codec.decode(unsupportedObject);
            fail("Expected a DecoderException to be thrown for an unsupported object type.");
        } catch (final DecoderException e) {
            // Verify the exception message is correct and informative.
            final String expectedMessage = "Objects of type " +
                    unsupportedObject.getClass().getName() +
                    " cannot be quoted-printable decoded";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}