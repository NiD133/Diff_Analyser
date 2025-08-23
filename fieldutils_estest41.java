package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that FieldUtils.equals() returns false when comparing two non-null objects
     * of different types.
     */
    @Test
    public void equals_withDifferentObjectTypes_shouldReturnFalse() {
        // Arrange: Create two objects of different, non-null types.
        Object genericObject = new Object();
        Object integerObject = Integer.valueOf(0);

        // Act: Call the equals method to compare the two objects.
        boolean result = FieldUtils.equals(genericObject, integerObject);

        // Assert: The result should be false, as the objects are of different types.
        assertFalse("FieldUtils.equals should return false for objects of different types", result);
    }
}