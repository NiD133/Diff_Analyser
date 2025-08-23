package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that FieldUtils.equals() returns false when comparing a non-null object to null.
     * The comparison should be symmetrical, so this also implicitly covers comparing null to a non-null object.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparingNonNullObjectToNull() {
        // Arrange
        Object nonNullObject = -1; // Autoboxed to Integer

        // Act
        boolean result = FieldUtils.equals(nonNullObject, null);

        // Assert
        assertFalse("FieldUtils.equals(nonNull, null) should return false.", result);
    }
}