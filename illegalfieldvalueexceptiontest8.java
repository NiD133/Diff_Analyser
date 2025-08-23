package org.joda.time;

import junit.framework.TestCase;

/**
 * Unit tests for the constructors of {@link IllegalFieldValueException}.
 * <p>
 * This class focuses on constructors that are not commonly called by other parts of the Joda-Time library,
 * ensuring they correctly initialize the exception's state.
 */
public class TestIllegalFieldValueException extends TestCase {

    /**
     * Tests the constructor that accepts a DurationFieldType and numeric bounds.
     */
    public void testConstructorWithDurationFieldTypeAndBounds() {
        // Arrange
        final DurationFieldType fieldType = DurationFieldType.days();
        final Number illegalValue = Integer.valueOf(1);
        final Number lowerBound = Integer.valueOf(2);
        final Number upperBound = Integer.valueOf(3);

        // Act
        IllegalFieldValueException exception = new IllegalFieldValueException(
                fieldType, illegalValue, lowerBound, upperBound);

        // Assert
        assertNull("DateTimeFieldType should be null", exception.getDateTimeFieldType());
        assertEquals("DurationFieldType should be set correctly", fieldType, exception.getDurationFieldType());
        assertEquals("Field name should be derived from the field type", "days", exception.getFieldName());
        assertEquals("Illegal number value should be set correctly", illegalValue, exception.getIllegalNumberValue());
        assertNull("Illegal string value should be null", exception.getIllegalStringValue());
        assertEquals("Illegal value as string should be correct", "1", exception.getIllegalValueAsString());
        assertEquals("Lower bound should be set correctly", lowerBound, exception.getLowerBound());
        assertEquals("Upper bound should be set correctly", upperBound, exception.getUpperBound());
    }

    /**
     * Tests the constructor that accepts a DurationFieldType and a string value.
     */
    public void testConstructorWithDurationFieldTypeAndStringValue() {
        // Arrange
        final DurationFieldType fieldType = DurationFieldType.months();
        final String illegalValue = "five";

        // Act
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldType, illegalValue);

        // Assert
        assertNull("DateTimeFieldType should be null", exception.getDateTimeFieldType());
        assertEquals("DurationFieldType should be set correctly", fieldType, exception.getDurationFieldType());
        assertEquals("Field name should be derived from the field type", "months", exception.getFieldName());
        assertNull("Illegal number value should be null", exception.getIllegalNumberValue());
        assertEquals("Illegal string value should be set correctly", illegalValue, exception.getIllegalStringValue());
        assertEquals("Illegal value as string should be correct", "five", exception.getIllegalValueAsString());
        assertNull("Lower bound should be null", exception.getLowerBound());
        assertNull("Upper bound should be null", exception.getUpperBound());
    }

    /**
     * Tests the constructor that accepts a field name and a string value.
     */
    public void testConstructorWithStringFieldNameAndStringValue() {
        // Arrange
        final String fieldName = "months";
        final String illegalValue = "five";

        // Act
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldName, illegalValue);

        // Assert
        assertNull("DateTimeFieldType should be null", exception.getDateTimeFieldType());
        assertNull("DurationFieldType should be null", exception.getDurationFieldType());
        assertEquals("Field name should be set correctly", fieldName, exception.getFieldName());
        assertNull("Illegal number value should be null", exception.getIllegalNumberValue());
        assertEquals("Illegal string value should be set correctly", illegalValue, exception.getIllegalStringValue());
        assertEquals("Illegal value as string should be correct", "five", exception.getIllegalValueAsString());
        assertNull("Lower bound should be null", exception.getLowerBound());
        assertNull("Upper bound should be null", exception.getUpperBound());
    }
}