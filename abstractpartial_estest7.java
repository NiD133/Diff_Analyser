package org.joda.time.base;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.MonthDay;
import org.joda.time.chrono.CopticChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains an improved test for the toDateTime(ReadableInstant) method
 * in the {@link AbstractPartial} class.
 */
public class AbstractPartial_ESTestTest7 {

    /**
     * Tests that toDateTime(ReadableInstant) correctly combines the partial's fields
     * with a base instant, preserving the base instant's chronology and its other fields.
     */
    @Test
    public void toDateTime_shouldCombinePartialWithBaseInstantUsingBaseChronology() {
        // Arrange
        // A partial date (June 21st) in the default ISO chronology.
        MonthDay partialDate = new MonthDay(6, 21);

        // A full base instant in a different chronology (Coptic) and a specific time.
        // Coptic date: Year 1700, Month 5, Day 10, at 12:30:40.
        Chronology copticChronology = CopticChronology.getInstanceUTC();
        DateTime baseInstant = new DateTime(1700, 5, 10, 12, 30, 40, 0, copticChronology);

        // Act
        // Combine the partial (month and day) with the base instant.
        DateTime result = partialDate.toDateTime(baseInstant);

        // Assert
        // The resulting DateTime should have the month and day from the partial,
        // but the year, time, and chronology from the base instant.

        // 1. Chronology should be taken from the base instant.
        assertEquals("Chronology should be preserved from the base instant",
                copticChronology, result.getChronology());

        // 2. Fields from the partial (MonthDay) should be applied.
        assertEquals("Month should be taken from the partial", 6, result.getMonthOfYear());
        assertEquals("Day should be taken from the partial", 21, result.getDayOfMonth());

        // 3. Fields not in the partial should be preserved from the base instant.
        assertEquals("Year should be preserved from the base instant", 1700, result.getYear());
        assertEquals("Hour should be preserved from the base instant", 12, result.getHourOfDay());
        assertEquals("Minute should be preserved from the base instant", 30, result.getMinuteOfHour());
        assertEquals("Second should be preserved from the base instant", 40, result.getSecondOfMinute());
    }
}