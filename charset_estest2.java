package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link CharSet} class.
 */
public class CharSetTest {

    /**
     * Tests that CharSet.getInstance() correctly handles an array of strings
     * that contains null elements. The method should ignore the nulls and
     * create a CharSet based on the valid definition strings provided.
     */
    @Test
    public void testGetInstanceWithNullsInInputArrayShouldCreateCorrectSet() {
        // Arrange: Define an array with a valid CharSet definition string ("^n%R")
        // surrounded by nulls. This simulates a scenario where the input array
        // might contain empty or invalid entries.
        final String[] setDefinitions = {null, "^n%R", null};

        // Act: Create a CharSet instance from the mixed-content array.
        final CharSet charSet = CharSet.getInstance(setDefinitions);

        // Assert: Verify that the CharSet was created and correctly represents
        // the rules from the valid definition string.
        assertNotNull("The factory method should return a non-null CharSet instance.", charSet);

        // The definition "^n%R" translates to:
        // - All characters EXCEPT 'n' (from "^n")
        // - The character '%' (from "%")
        // - The character 'R' (from "R")
        assertFalse("Should not contain 'n' because it was negated.", charSet.contains('n'));
        assertTrue("Should contain '%' because it was explicitly included.", charSet.contains('%'));
        assertTrue("Should contain 'R' because it was explicitly included.", charSet.contains('R'));
        assertTrue("Should contain 'a' as it is not part of the negated set.", charSet.contains('a'));
    }
}