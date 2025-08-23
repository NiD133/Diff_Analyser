package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that FieldUtils.equals returns false when comparing a null object
     * with a non-null object.
     */
    @Test
    public void testEquals_withFirstArgumentNull_shouldReturnFalse() {
        // Arrange
        Object nonNullObject = new Object();

        // Act
        boolean result = FieldUtils.equals(null, nonNullObject);

        // Assert
        assertFalse("Comparing a null with a non-null object should result in false.", result);
    }
}