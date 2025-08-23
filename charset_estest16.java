package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link CharSet} class.
 */
public class CharSetTest {

    /**
     * Tests that the toString() method returns "[]" for a CharSet
     * created from an array containing only null string elements.
     *
     * This verifies that the getInstance() factory method handles null inputs
     * gracefully, resulting in an empty set.
     */
    @Test
    public void toString_shouldReturnEmptyBrackets_whenCharSetIsCreatedWithArrayOfNulls() {
        // Arrange: Create an input array containing multiple null elements.
        // The CharSet.getInstance factory method should treat these as empty definitions.
        final String[] nullStrings = new String[9];

        // Act: Create a CharSet instance using the array of nulls.
        final CharSet emptyCharSet = CharSet.getInstance(nullStrings);
        final String toStringResult = emptyCharSet.toString();

        // Assert: The string representation of the resulting CharSet should be "[]",
        // indicating an empty set.
        assertEquals("[]", toStringResult);
    }
}