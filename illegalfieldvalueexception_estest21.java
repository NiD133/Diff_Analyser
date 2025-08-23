package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

// The base class is retained as it was in the original test.
public class IllegalFieldValueException_ESTestTest21 extends IllegalFieldValueException_ESTest_scaffolding {

    /**
     * Tests the constructor that accepts a DurationFieldType and an illegal String value.
     * This test verifies that the exception is initialized with the correct state,
     * including the field type, the illegal value, the field name, and the generated message.
     */
    @Test
    public void testConstructorWithDurationFieldTypeAndStringValue() {
        // Arrange
        DurationFieldType fieldType = DurationFieldType.eras();
        String illegalValue = "";
        String expectedMessage = "Value \"\" for eras is not supported";

        // Act
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldType, illegalValue);

        // Assert
        // Verify that the exception's primary properties are set correctly.
        assertEquals("The exception message should be correctly formatted.",
                expectedMessage, exception.getMessage());
        assertSame("The stored duration field type should be the one provided in the constructor.",
                fieldType, exception.getDurationFieldType());
        assertEquals("The stored illegal string value should be the one provided in the constructor.",
                illegalValue, exception.getIllegalStringValue());
        assertEquals("The field name should match the name of the provided duration field type.",
                "eras", exception.getFieldName());

        // Verify that properties related to other constructors are null, as expected.
        assertNull("The date time field type should be null for this constructor.",
                exception.getDateTimeFieldType());
        assertNull("The illegal number value should be null when a string value is provided.",
                exception.getIllegalNumberValue());
        assertNull("The lower bound should be null for this constructor.",
                exception.getLowerBound());
        assertNull("The upper bound should be null for this constructor.",
                exception.getUpperBound());
    }
}