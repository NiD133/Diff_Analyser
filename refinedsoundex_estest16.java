package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link RefinedSoundex} class, focusing on exception handling.
 */
public class RefinedSoundexTest {

    /**
     * Tests that the encode(Object) method throws an EncoderException when the input is null.
     * The RefinedSoundex encoder is designed to work only with Strings.
     */
    @Test
    public void encodeObjectWithNullShouldThrowEncoderException() {
        // Arrange: Create an instance of the encoder.
        final RefinedSoundex encoder = new RefinedSoundex();
        final String expectedMessage = "Parameter supplied to RefinedSoundex encode is not of type java.lang.String";

        // Act & Assert: Verify that encoding a null object throws the expected exception.
        try {
            encoder.encode((Object) null);
            fail("Expected an EncoderException to be thrown for a null input.");
        } catch (final EncoderException e) {
            // Verify that the exception is of the correct type and has the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}