package org.joda.time.field;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that FieldUtils.equals() returns true when comparing an object to itself,
     * confirming the reflexive property of equality.
     */
    @Test
    public void testEqualsReturnsTrueForSameObjectInstance() {
        // Arrange
        Object testObject = new Object();

        // Act & Assert
        assertTrue("An object should be considered equal to itself.", FieldUtils.equals(testObject, testObject));
    }
}