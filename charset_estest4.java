package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CharSet}.
 */
public class CharSetTest {

    /**
     * Tests that the CharSet constructor correctly handles an input array containing
     * only null elements, resulting in an empty CharSet.
     */
    @Test
    public void testConstructorWithNullArrayElementsCreatesEmptySet() {
        // Arrange: Create an array of strings containing only nulls.
        // This simulates a scenario where the input definitions are invalid or missing.
        String[] setDefinitions = new String[]{null, null, null};

        // Act: Create a CharSet from the array of nulls.
        CharSet charSet = new CharSet(setDefinitions);

        // Assert: The resulting CharSet should contain no character ranges.
        CharRange[] charRanges = charSet.getCharRanges();
        assertEquals("A CharSet created from an array of nulls should be empty.", 0, charRanges.length);
    }
}