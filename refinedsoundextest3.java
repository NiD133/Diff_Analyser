package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link RefinedSoundex}.
 * <p>
 * This class is named to reflect the class under test, improving on the original
 * name {@code RefinedSoundexTestTest3}.
 * </p>
 */
public class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Test
    @DisplayName("getMappingCode() should return the NUL character for non-alphabetic input")
    void getMappingCodeForNonLetterReturnsNulChar() {
        // The RefinedSoundex algorithm is defined for alphabetic characters.
        // This test verifies that a non-alphabetic character ('#') is mapped
        // to the NUL character ((char) 0), which signifies 'no mapping'.
        final char code = getStringEncoder().getMappingCode('#');

        // The original test asserted `assertEquals(0, code)`. This works due to
        // implicit type promotion from char to int, but it is not immediately
        // clear that the expected value is the NUL character.
        // Comparing with `(char) 0` makes the intent explicit and more understandable.
        assertEquals((char) 0, code);
    }
}