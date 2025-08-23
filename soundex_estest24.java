package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// The original class name and inheritance are kept to show a direct comparison.
// In a real-world scenario, the class might be renamed to SoundexTest.
public class Soundex_ESTestTest24 extends Soundex_ESTest_scaffolding {

    /**
     * Tests that the encode(Object) method throws an EncoderException when the
     * input object is not of type String, as per the method's contract.
     */
    @Test
    public void encodeShouldThrowExceptionWhenInputIsNotAString() {
        // Arrange
        final Soundex soundex = new Soundex();
        final Object nonStringObject = new Object();
        final String expectedErrorMessage = "Parameter supplied to Soundex encode is not of type java.lang.String";

        // Act & Assert
        try {
            soundex.encode(nonStringObject);
            fail("Expected an EncoderException to be thrown for a non-String parameter.");
        } catch (final EncoderException e) {
            // Verify that the exception has the expected message
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}