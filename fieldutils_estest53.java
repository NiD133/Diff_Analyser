package org.joda.time.field;

import org.joda.time.DateTimeField;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.chrono.IslamicChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the FieldUtils class.
 * The original class name was FieldUtils_ESTestTest53.
 */
public class FieldUtilsTest {

    /**
     * Tests that verifyValueBounds throws an IllegalFieldValueException when the
     * specified bounds are invalid (i.e., lower bound is greater than the upper bound).
     */
    @Test
    public void verifyValueBounds_shouldThrowException_whenBoundsAreInverted() {
        // Arrange: Set up a DateTimeField and define an invalid range where the
        // lower bound is greater than the upper bound.
        // The specific DateTimeField (minuteOfHour) is arbitrary; any field would work
        // as it's only used to construct the exception message.
        final DateTimeField minuteOfHourField = IslamicChronology.getInstanceUTC().minuteOfHour();

        final int valueToTest = 1;
        final int lowerBound = 1;
        final int upperBound = -1; // Create an invalid range where lower > upper

        // Act & Assert
        try {
            FieldUtils.verifyValueBounds(minuteOfHourField, valueToTest, lowerBound, upperBound);
            fail("Expected an IllegalFieldValueException to be thrown due to invalid bounds.");
        } catch (IllegalFieldValueException e) {
            // The method correctly throws an exception because the value (1) is greater
            // than the upper bound (-1), thus falling outside the invalid range [1, -1].
            final String expectedMessage = "Value 1 for minuteOfHour must be in the range [1,-1]";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}