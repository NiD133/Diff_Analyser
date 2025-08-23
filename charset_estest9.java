package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSet}.
 */
public class CharSetTest {

    /**
     * Tests that CharSet.contains() returns true for a character that falls
     * within a defined character range (e.g., "A-Z").
     */
    @Test
    public void testContainsReturnsTrueForCharacterWithinRange() {
        // Arrange: Create a CharSet representing all uppercase ASCII letters.
        // The "A-Z" syntax defines a range from 'A' to 'Z'.
        CharSet uppercaseLetters = CharSet.getInstance("A-Z");

        // Act & Assert: Verify that a character within that range is correctly identified.
        // The character 'T' is expected to be in the set.
        assertTrue(
            "contains('T') should return true for a CharSet defined with the range 'A-Z'",
            uppercaseLetters.contains('T')
        );
    }
}