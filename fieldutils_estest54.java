package org.joda.time.field;

import org.joda.time.DateTimeField;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Verifies that verifyValueBounds() throws a NullPointerException when the
     * DateTimeField argument is null, as the method needs the field to construct
     * a meaningful error message.
     */
    @Test(expected = NullPointerException.class)
    public void verifyValueBounds_withNullDateTimeField_shouldThrowNullPointerException() {
        // The integer arguments for value, lowerBound, and upperBound are arbitrary
        // for this test, as the null check on the field should occur first.
        FieldUtils.verifyValueBounds((DateTimeField) null, 10, 0, 20);
    }
}