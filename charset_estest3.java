package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSet}.
 */
public class CharSetTest {

    /**
     * Tests that {@link CharSet#getInstance(String...)} correctly creates a set
     * from a single string defining a character range like "a-z".
     */
    @Test
    public void testGetInstanceWithCharacterRange() {
        // Arrange: Define the input for the character set, representing all lowercase letters.
        final String[] setDefinition = {"a-z"};

        // Act: Create the CharSet instance.
        final CharSet lowerCaseLetters = CharSet.getInstance(setDefinition);

        // Assert: Verify that the created set behaves as expected.
        assertNotNull("The created CharSet should not be null.", lowerCaseLetters);

        // Verify that characters within the range are included.
        assertTrue("'a' should be in the set 'a-z'.", lowerCaseLetters.contains('a'));
        assertTrue("'m' should be in the set 'a-z'.", lowerCaseLetters.contains('m'));
        assertTrue("'z' should be in the set 'a-z'.", lowerCaseLetters.contains('z'));

        // Verify that characters outside the range are excluded.
        assertFalse("'A' (uppercase) should not be in the set 'a-z'.", lowerCaseLetters.contains('A'));
        assertFalse("'5' (a digit) should not be in the set 'a-z'.", lowerCaseLetters.contains('5'));
        assertFalse("'$' (a symbol) should not be in the set 'a-z'.", lowerCaseLetters.contains('$'));
    }
}