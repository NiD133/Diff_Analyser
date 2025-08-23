package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that CharSetUtils.count() returns 0 when the character set array
     * contains only null elements. An array of nulls is treated as an empty set,
     * so no characters should be counted.
     */
    @Test
    public void testCountWithSetContainingOnlyNullsReturnsZero() {
        // Arrange
        final String inputString = "hello";
        final String[] characterSetWithNulls = {null, null};

        // Act
        final int count = CharSetUtils.count(inputString, characterSetWithNulls);

        // Assert
        assertEquals("The count should be 0 for a set containing only nulls.", 0, count);
    }
}