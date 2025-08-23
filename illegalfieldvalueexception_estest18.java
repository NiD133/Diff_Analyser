package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the constructors of IllegalFieldValueException.
 * Note: The original class name "IllegalFieldValueException_ESTestTest18" is preserved
 * as requested, but a more descriptive name like "IllegalFieldValueExceptionConstructorTest"
 * would be preferable in a real-world scenario.
 */
public class IllegalFieldValueException_ESTestTest18 {

    /**
     * Tests that the constructor taking a String field name correctly initializes the
     * exception's state, particularly when some arguments are null or represent edge cases
     * like an empty field name.
     */
    @Test
    public void testConstructorWithStringFieldNameHandlesNullsAndEmptyName() {
        // Arrange: Define arguments for the exception constructor. This setup mirrors the
        // original test's use of nulls and an empty string to test constructor robustness.
        String fieldName = "";
        Number illegalValue = null;
        Number lowerBound = 0.0f;
        Number upperBound = null;

        // Act: Create an instance of the exception. This step implicitly verifies that
        // the constructor executes without throwing an unexpected error (e.g., NullPointerException).
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldName, illegalValue, lowerBound, upperBound);

        // Assert: Verify that the internal state of the exception object is set correctly
        // based on the arguments passed to the constructor.
        assertEquals("Field name should be set correctly.", fieldName, exception.getFieldName());
        assertNull("The illegal number value should be null as passed to the constructor.", exception.getIllegalNumberValue());
        assertEquals("The lower bound should be set correctly.", lowerBound, exception.getLowerBound());
        assertNull("The upper bound should be null as passed to the constructor.", exception.getUpperBound());

        // This specific constructor is not expected to populate the following properties.
        assertNull("The illegal string value should not be set by this constructor.", exception.getIllegalStringValue());
        assertNull("The DateTimeFieldType should not be set by this constructor.", exception.getDateTimeFieldType());
        assertNull("The DurationFieldType should not be set by this constructor.", exception.getDurationFieldType());
    }
}