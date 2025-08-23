package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that verifyValueBounds throws a NullPointerException when passed a null
     * DateTimeFieldType and a value that is out of bounds.
     *
     * The method is designed to throw an IllegalFieldValueException for out-of-bounds
     * values. However, the construction of this exception involves calling getName()
     * on the provided DateTimeFieldType. This test confirms that if the field type is
     * null, this operation correctly results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void verifyValueBounds_withNullFieldTypeAndOutOfBoundsValue_throwsNullPointerException() {
        // Arrange: A value that is outside the specified lower and upper bounds,
        // and a null field type. This combination is necessary to trigger the
        // creation of an IllegalFieldValueException, which in turn causes the NPE.
        final DateTimeFieldType nullFieldType = null;
        final int value = 10;
        final int lowerBound = 20;
        final int upperBound = 30;

        // Act: Call the method under test.
        FieldUtils.verifyValueBounds(nullFieldType, value, lowerBound, upperBound);

        // Assert: The test expects a NullPointerException, as declared in the @Test annotation.
    }
}