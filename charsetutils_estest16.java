package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CharSetUtils#delete(String, String...)}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that CharSetUtils.delete() returns the original string when the set of
     * characters to delete is an array containing only null elements.
     * An array of nulls is treated as an empty set, so no characters should be deleted.
     */
    @Test
    public void deleteWithSetContainingOnlyNullsShouldReturnOriginalString() {
        // Arrange
        final String input = "A-Z";
        final String[] setToDelete = new String[8]; // Creates an array of 8 nulls

        // Act
        final String result = CharSetUtils.delete(input, setToDelete);

        // Assert
        assertEquals("The string should remain unchanged", input, result);
    }
}