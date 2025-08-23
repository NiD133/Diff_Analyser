package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test case verifies the behavior of the CharSet.getInstance(String...)
 * factory method when provided with null input.
 */
public class CharSet_ESTestTest15 extends CharSet_ESTest_scaffolding {

    /**
     * Tests that a CharSet created from an array of null strings is empty
     * and therefore does not contain any characters.
     */
    @Test
    public void testGetInstanceWithNullArrayCreatesEmptySet() {
        // Arrange: Create a CharSet from an array containing only null strings.
        // According to the documentation, this should result in an empty set.
        String[] inputWithNulls = new String[9];
        CharSet emptySet = CharSet.getInstance(inputWithNulls);

        // Act & Assert: Verify that an arbitrary character is not present in the set.
        assertFalse("A CharSet created from null strings should be empty and not contain 'T'.", emptySet.contains('T'));
    }
}