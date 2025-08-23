package org.apache.commons.codec.binary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains an improved version of a generated test case for the {@link Base16} class.
 */
public class Base16RefactoredTest {

    /**
     * Tests that the decode method behaves correctly when given an inconsistent
     * context, which could result from a previously interrupted streaming operation.
     *
     * This test specifically checks for a bug where having a non-zero `ibitWorkArea`
     * in the context could cause the decoder to incorrectly process an input byte,
     * even when the specified length for decoding is zero.
     */
    @Test
    public void decodeWithInconsistentContextAndZeroLengthShouldThrowException() {
        // ARRANGE
        final Base16 base16 = new Base16();
        // Input containing a byte (0) that is not a valid Base16 character.
        final byte[] invalidInputData = { 0 };

        // Simulate a corrupted or inconsistent context from a prior operation.
        // This is a white-box test to ensure the decoder handles such edge cases robustly.
        // The non-zero 'ibitWorkArea' is the key to triggering the specific behavior.
        final BaseNCodec.Context context = new BaseNCodec.Context();
        context.ibitWorkArea = 76; // A non-zero value to create the inconsistent state.

        // ACT & ASSERT
        try {
            // Call decode with a length of 0. A correct implementation should either
            // do nothing or only process the context state. This test verifies that
            // the implementation doesn't incorrectly read from the input buffer.
            base16.decode(invalidInputData, 0, 0, context);
            fail("Expected an IllegalArgumentException because the inconsistent context " +
                 "led to processing an invalid character from the input.");
        } catch (final IllegalArgumentException e) {
            // VERIFY
            // The exception message confirms that the decoder attempted to process the
            // invalid byte (0) from the input array, as expected.
            assertEquals("Invalid octet in encoded value: 0", e.getMessage());
        }
    }
}