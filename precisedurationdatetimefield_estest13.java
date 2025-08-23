package org.joda.time.field;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link PreciseDateTimeField}.
 * This test focuses on the leniency behavior of the field.
 */
public class PreciseDateTimeFieldTest {

    /**
     * Tests that a PreciseDateTimeField is not lenient by default.
     * The leniency of a field is an intrinsic property and should not be affected
     * by the chronology from which its dependent duration fields are derived.
     */
    @Test
    public void isLenient_shouldReturnFalseByDefault() {
        // Arrange
        // 1. Get the necessary duration fields from a standard chronology.
        //    The specific chronology used (e.g., Gregorian, Julian) is not important
        //    for this test, as the field's leniency is independent of it.
        Chronology chronology = GregorianChronology.getInstanceUTC();
        DurationField minutesField = chronology.minutes();
        DurationField halfdaysField = chronology.halfdays();
        DateTimeFieldType fieldType = DateTimeFieldType.minuteOfHalfday();

        // 2. Create the field under test. PreciseDateTimeField is a concrete implementation
        //    of the abstract PreciseDurationDateTimeField.
        PreciseDateTimeField minuteOfHalfdayField = new PreciseDateTimeField(fieldType, minutesField, halfdaysField);

        // Act
        // Call the method under test.
        boolean isLenient = minuteOfHalfdayField.isLenient();

        // Assert
        // Verify that the field is not lenient.
        assertFalse("A PreciseDateTimeField should not be lenient by default", isLenient);
    }
}