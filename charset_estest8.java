package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link CharSet#equals(Object)}.
 */
public class CharSetTest {

    /**
     * Tests that a CharSet instance is not equal to an object of a different type.
     * This is a standard requirement of the equals() contract.
     */
    @Test
    public void testEquals_returnsFalse_whenComparedWithDifferentType() {
        // Arrange
        final CharSet charSet = CharSet.getInstance("a-z");
        final Object nonCharSetObject = new Object();

        // Act
        final boolean isEqual = charSet.equals(nonCharSetObject);

        // Assert
        assertFalse("CharSet should not be equal to an object of a different type", isEqual);
    }
}