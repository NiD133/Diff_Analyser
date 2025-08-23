package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link IllegalFieldValueException}.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Tests the constructor that accepts a DateTimeFieldType and an illegal String value.
     * This test verifies that all properties of the created exception are set correctly.
     */
    @Test
    public void constructor_withDateTimeFieldTypeAndStringValue_setsAllPropertiesCorrectly() {
        // Arrange
        DateTimeFieldType fieldType = DateTimeFieldType.secondOfMinute();
        String illegalValue = "";
        String expectedMessage = "Value \"\" for secondOfMinute is not supported";

        // Act
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldType, illegalValue);

        // Assert
        // Verify the primary properties are set as expected
        assertEquals("The exception message should be correctly formatted.",
                expectedMessage, exception.getMessage());
        assertSame("The DateTimeFieldType should be the one provided in the constructor.",
                fieldType, exception.getDateTimeFieldType());
        assertEquals("The field name should be derived from the DateTimeFieldType.",
                "secondOfMinute", exception.getFieldName());
        assertSame("The illegal string value should be stored.",
                illegalValue, exception.getIllegalStringValue());
        assertEquals("The illegal value should be available as a string.",
                illegalValue, exception.getIllegalValueAsString());

        // Verify that properties related to other constructor overloads are null
        assertNull("DurationFieldType should be null for this constructor.",
                exception.getDurationFieldType());
        assertNull("IllegalNumberValue should be null when a string value is provided.",
                exception.getIllegalNumberValue());
        assertNull("LowerBound should be null as it was not provided.",
                exception.getLowerBound());
        assertNull("UpperBound should be null as it was not provided.",
                exception.getUpperBound());
    }
}