package org.apache.commons.codec.language;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Soundex} class, focusing on exception handling with custom mappings.
 */
public class SoundexTest {

    /**
     * Tests that the encode() method throws an IllegalArgumentException when the input string
     * contains a character that is not covered by a custom mapping. This scenario typically
     * occurs when the provided mapping string is shorter than the standard 26 characters
     * required for the English alphabet.
     */
    @Test
    void encodeThrowsExceptionForCharacterOutsideCustomMappingRange() {
        // Arrange: Create a Soundex instance with a custom mapping that only covers 'A' through 'D'.
        final Soundex soundexWithShortMapping = new Soundex("ABCD");
        final String inputWithUnmappedChar = "Google"; // 'G' is the first character outside the A-D range.

        // Act & Assert: Verify that encoding a string with an unmapped character throws an exception.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> soundexWithShortMapping.encode(inputWithUnmappedChar),
            "Expected encode() to throw for an unmappable character."
        );

        // Verify the exception message correctly identifies the unmapped character.
        assertEquals("The character is not mapped: G", thrown.getMessage());
    }
}