package org.joda.time.base;

import org.joda.time.DateTimeFieldType;
import org.joda.time.MonthDay;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the {@link AbstractPartial} class.
 * This specific test was improved for understandability.
 */
public class AbstractPartial_ESTestTest55 extends AbstractPartial_ESTest_scaffolding {

    /**
     * Verifies that calling get() with a DateTimeFieldType that is not supported
     * by the partial implementation (e.g., MonthDay) throws an IllegalArgumentException.
     */
    @Test
    public void get_whenFieldTypeIsUnsupported_throwsIllegalArgumentException() {
        // Arrange: Create a MonthDay partial, which only supports month and day fields.
        // Then, define a field type (clockhourOfDay) that it does not support.
        MonthDay monthDay = MonthDay.now();
        DateTimeFieldType unsupportedFieldType = DateTimeFieldType.clockhourOfDay();

        // Act & Assert: Attempt to get the value for the unsupported field.
        try {
            monthDay.get(unsupportedFieldType);
            fail("Expected an IllegalArgumentException because the field type is not supported.");
        } catch (IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("Field 'clockhourOfDay' is not supported", e.getMessage());
        }
    }
}