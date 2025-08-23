package org.joda.time.chrono;

import org.joda.time.chrono.AssembledChronology.Fields;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the field assembly mechanism in {@link ISOChronology}.
 */
public class ISOChronologyTest {

    /**
     * Verifies that the assemble() method correctly populates a Fields object
     * with all the standard ISO date and time fields. This test ensures the
     * fundamental construction of the chronology is sound and doesn't throw an
     * exception.
     */
    @Test
    public void assemble_shouldPopulateAllDateTimeFields() {
        // Arrange: Create an ISOChronology instance and an empty Fields object.
        ISOChronology chronology = ISOChronology.getInstance();
        Fields fields = new Fields();

        // Act: Ask the chronology to assemble its constituent fields.
        chronology.assemble(fields);

        // Assert: Verify that essential date and time fields are now populated.
        assertNotNull("Year field should be populated", fields.year);
        assertNotNull("Month of year field should be populated", fields.monthOfYear);
        assertNotNull("Day of month field should be populated", fields.dayOfMonth);
        assertNotNull("Weekyear field should be populated", fields.weekyear);
        assertNotNull("Day of week field should be populated", fields.dayOfWeek);
        assertNotNull("Hour of day field should be populated", fields.hourOfDay);
        assertNotNull("Minute of hour field should be populated", fields.minuteOfHour);
        assertNotNull("Second of minute field should be populated", fields.secondOfMinute);
        assertNotNull("Millis of second field should be populated", fields.millisOfSecond);

        // Assert: Verify that essential duration fields are also populated.
        assertNotNull("Years duration field should be populated", fields.years);
        assertNotNull("Months duration field should be populated", fields.months);
        assertNotNull("Days duration field should be populated", fields.days);
        assertNotNull("Hours duration field should be populated", fields.hours);
    }
}