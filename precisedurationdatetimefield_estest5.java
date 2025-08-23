package org.joda.time.field;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * This test class focuses on the behavior of fields that are implemented using
 * PreciseDurationDateTimeField, such as the 'dayOfYear' field in LocalDate.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * Tests that setting a value on a field managed by PreciseDurationDateTimeField,
     * via a higher-level class like LocalDate, respects the immutability of the parent object.
     * It verifies that a new instance is returned with the updated value, and the original
     * instance is not modified.
     */
    @Test
    public void setOnDateTimeFieldShouldReturnNewInstanceAndNotModifyOriginal() {
        // Arrange: Create a fixed, deterministic date.
        // The 'dayOfYear' field in LocalDate is a PreciseDurationDateTimeField.
        LocalDate originalDate = new LocalDate(2023, 1, 15);
        int originalDayOfYear = originalDate.getDayOfYear(); // is 15
        int newDayOfYear = 365;

        // Act: Call a method that internally uses the 'set' operation of the dayOfYear field.
        LocalDate newDate = originalDate.withDayOfYear(newDayOfYear);

        // Assert:
        // 1. A new object instance was created.
        assertNotSame("A new LocalDate instance should be returned", newDate, originalDate);

        // 2. The new object has the correctly updated value.
        assertEquals("The new date should have the correct day of year", newDayOfYear, newDate.getDayOfYear());

        // 3. The original object remains unchanged.
        assertEquals("The original date should not be modified", originalDayOfYear, originalDate.getDayOfYear());
    }
}