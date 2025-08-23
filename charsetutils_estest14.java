package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that CharSetUtils.keep() returns an empty string when the provided
     * set of characters to keep is an array containing only null elements.
     * According to the documentation, a null or empty set should result in
     * an empty string being returned.
     */
    @Test
    public void keepShouldReturnEmptyStringWhenSetContainsOnlyNulls() {
        // Arrange
        final String originalString = "any-string-to-filter";
        // A set containing only nulls should be treated as an empty set of characters.
        final String[] setWithOnlyNulls = new String[] { null, null };

        // Act
        final String result = CharSetUtils.keep(originalString, setWithOnlyNulls);

        // Assert
        assertEquals("Keeping characters from a null set should result in an empty string.", "", result);
    }
}