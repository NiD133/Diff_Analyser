package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that containsAny() returns false when the string to be searched
     * contains no characters from the provided set.
     * This test also ensures that null elements within the set array are handled gracefully.
     */
    @Test
    public void containsAnyShouldReturnFalseForNonMatchingSetContainingNulls() {
        // Arrange: The string to search does not contain any characters from the valid set entries.
        final String stringToSearch = "A-Za-z";
        // The set array contains a non-matching set and a null, which should be ignored.
        final String[] searchSet = {"Xii8KJ", null};

        // Act: Check if the string contains any character from the set.
        final boolean result = CharSetUtils.containsAny(stringToSearch, searchSet);

        // Assert: The result should be false as there are no matching characters.
        assertFalse("Expected false because no characters match, and nulls in the set should be ignored.", result);
    }
}