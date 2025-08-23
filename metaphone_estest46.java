package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the encode(Object) method throws an EncoderException when the input
     * is not of type String, as specified by the Encoder interface contract.
     */
    @Test
    public void encodeShouldThrowExceptionForInvalidInputType() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        Object invalidInput = new Object(); // Use a generic Object as the invalid type.
        String expectedMessage = "Parameter supplied to Metaphone encode is not of type java.lang.String";

        // Act & Assert
        try {
            metaphone.encode(invalidInput);
            fail("Expected an EncoderException to be thrown for non-String input.");
        } catch (final EncoderException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}