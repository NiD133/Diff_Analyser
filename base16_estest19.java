package org.apache.commons.codec.binary;

import org.apache.commons.codec.CodecPolicy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test suite contains tests for the {@link Base16} class.
 * The original test was automatically generated and has been improved for clarity.
 */
public class Base16_ESTestTest19 { // Note: Test class name kept for consistency.

    /**
     * Tests that decoding a Base16 string with an odd number of characters
     * throws an IllegalArgumentException when using the STRICT decoding policy.
     * Base16 decoding requires character pairs to form a single byte.
     */
    @Test
    public void decodeWithStrictPolicyThrowsExceptionForOddLengthInput() {
        // Arrange
        final Base16 base16 = new Base16(false, CodecPolicy.STRICT);
        final String singleCharInput = "F";
        final String expectedErrorMessage = "Strict decoding: Last encoded character is a valid base 16 alphabet " +
                "character but not a possible encoding. Decoding requires at least two characters to create one byte.";

        // Act & Assert
        try {
            base16.decode(singleCharInput);
            fail("Expected an IllegalArgumentException to be thrown for an odd-length input in STRICT mode.");
        } catch (final IllegalArgumentException e) {
            assertEquals("The exception message should clearly explain the decoding error.",
                    expectedErrorMessage, e.getMessage());
        }
    }
}