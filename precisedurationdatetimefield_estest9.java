package org.joda.time.field;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PreciseDurationDateTimeField_ESTestTest9 extends PreciseDurationDateTimeField_ESTest_scaffolding {

    /**
     * Tests that roundFloor() returns the same instant when the given instant
     * is already aligned with the field's unit (e.g., on an exact minute).
     */
    @Test
    public void roundFloor_whenInstantIsOnUnitBoundary_returnsSameInstant() {
        // Arrange
        // We need a chronology to obtain duration fields. UTC is used for consistency.
        Chronology chronology = GregorianChronology.getInstanceUTC();
        DurationField minutesField = chronology.minutes(); // The unit of the field under test.
        DurationField daysField = chronology.days();       // A range field, required by the constructor but not used by roundFloor.

        // The System Under Test (SUT) is a field with a unit duration of minutes.
        // We use a concrete implementation, PreciseDateTimeField, for this test,
        // as PreciseDurationDateTimeField is abstract.
        PreciseDurationDateTimeField field = new PreciseDateTimeField(
                DateTimeFieldType.dayOfWeek(), minutesField, daysField);

        // An instant that is already rounded to the nearest minute (the unit of our field).
        // This corresponds to 2022-06-15 10:30:00.000 UTC.
        long instantOnMinuteBoundary = 1655289000000L;

        // Act
        long roundedInstant = field.roundFloor(instantOnMinuteBoundary);

        // Assert
        assertEquals("Rounding an instant that is already on a minute boundary should not change it.",
                instantOnMinuteBoundary, roundedInstant);
        
        // Also, verify a basic property of the constructed field.
        assertFalse("The field should not be lenient by default.", field.isLenient());
    }
}