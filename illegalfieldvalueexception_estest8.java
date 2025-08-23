package org.joda.time;

import org.junit.Test;

/**
 * Unit tests for the {@link IllegalFieldValueException} class.
 */
public class IllegalFieldValueExceptionTest {

    /**
     * Tests that the constructor accepting a DurationFieldType throws a NullPointerException
     * if the field type is null, as it is a required parameter.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_withDurationFieldType_shouldThrowNPEForNullFieldType() {
        // The constructor attempts to access the name of the fieldType,
        // which will cause a NullPointerException if the fieldType is null.
        new IllegalFieldValueException((DurationFieldType) null, 10, 0, 23);
    }
}