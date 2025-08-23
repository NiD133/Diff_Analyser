package org.apache.commons.codec.binary;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test suite for the Base16 class, focusing on exception handling for invalid input.
 */
// The original test class name and inheritance are preserved to maintain context.
public class Base16_ESTestTest23 extends Base16_ESTest_scaffolding {

    /**
     * Tests that decoding a string containing a character outside the Base16 alphabet
     * throws an IllegalArgumentException.
     */
    @Test
    public void decodeWithInvalidCharacterThrowsIllegalArgumentException() {
        // Arrange: Set up the test case
        final Base16 base16 = new Base16();
        // The string "BDT" is invalid because the character 'T' is not part of the
        // Base16 alphabet (0-9, A-F).
        final String invalidInput = "BDT";
        final char invalidChar = 'T';

        // Act & Assert: Execute the code under test and verify the outcome
        try {
            base16.decode(invalidInput);
            fail("Expected an IllegalArgumentException to be thrown due to an invalid character.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception was thrown for the correct reason by checking its message.
            final String expectedMessageContent = "Invalid octet in encoded value: " + (int) invalidChar;
            assertTrue(
                "The exception message should indicate which character is invalid.",
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}