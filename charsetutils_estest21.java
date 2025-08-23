package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that containsAny() returns true when the search set contains the input string itself,
     * even if the set also contains null elements.
     */
    @Test
    public void testContainsAnyReturnsTrueWhenSetIncludesTheSearchStringAndNull() {
        // Arrange
        final String inputString = "h|Bv_9mUP7'&Y";
        // The set of characters to search for includes the input string and a null.
        // This tests that the method correctly processes the valid string and ignores the null.
        final String[] searchSet = {inputString, null};

        // Act
        final boolean found = CharSetUtils.containsAny(inputString, searchSet);

        // Assert
        assertTrue("Expected to find a match when the search set contains the input string.", found);
    }
}