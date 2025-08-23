package org.joda.time.field;

import org.joda.time.DateTimeField;
import org.junit.Test;

/**
 * Unit tests for {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that verifyValueBounds throws a NullPointerException if the DateTimeField argument is null.
     * The method should fail fast before it even checks the value against the bounds.
     */
    @Test(expected = NullPointerException.class)
    public void verifyValueBounds_shouldThrowNullPointerException_whenFieldIsNull() {
        // Arrange: The first argument (DateTimeField) is null.
        // The other arguments' values are irrelevant for this test.
        DateTimeField nullField = null;
        int value = 10;
        int lowerBound = 0;
        int upperBound = 20;

        // Act & Assert: Call the method and expect a NullPointerException.
        FieldUtils.verifyValueBounds(nullField, value, lowerBound, upperBound);
    }
}