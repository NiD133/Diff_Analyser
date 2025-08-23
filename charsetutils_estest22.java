package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that containsAny() returns false when the set of characters to search for
     * is an array containing only null elements. This scenario results in an empty
     * character set, so no characters can be found.
     */
    @Test
    public void testContainsAnyReturnsFalseForSetContainingOnlyNulls() {
        // Arrange: A non-empty string and a character set with only null values.
        final String inputString = "h|Bv_9mUP7'&Y";
        final String[] characterSet = {null, null};

        // Act: Call the method under test.
        final boolean result = CharSetUtils.containsAny(inputString, characterSet);

        // Assert: The result should be false, as no characters are in the (empty) set.
        assertFalse("containsAny should return false for a set with only nulls", result);
    }
}