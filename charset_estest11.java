package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CharSet}.
 */
public class CharSetTest {

    /**
     * Tests that {@link CharSet#getInstance(String...)} correctly creates a set
     * from a string containing multiple, non-contiguous characters.
     */
    @Test
    public void testGetInstanceWithMultipleIndividualCharacters() {
        // Arrange: Define a set of individual characters.
        // The CharSet syntax treats each character in this string as a member of the set.
        final String setDefinition = "]ZOr9";

        // Act: Create the CharSet instance.
        final CharSet charSet = CharSet.getInstance(setDefinition);

        // Assert: Verify the contents of the created set.
        assertNotNull("The created CharSet should not be null.", charSet);

        // The set should contain all characters from the definition string.
        assertTrue("Should contain ']'", charSet.contains(']'));
        assertTrue("Should contain 'Z'", charSet.contains('Z'));
        assertTrue("Should contain 'O'", charSet.contains('O'));
        assertTrue("Should contain 'r'", charSet.contains('r'));
        assertTrue("Should contain '9'", charSet.contains('9'));

        // The set should not contain characters that were not in the definition.
        assertFalse("Should not contain 'a'", charSet.contains('a'));
        assertFalse("Should not contain ' '", charSet.contains(' '));
    }
}