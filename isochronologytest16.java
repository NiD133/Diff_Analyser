package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Partial;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests additions to a {@link Partial} object, specifically focusing on how the
 * dayOfYear field wraps across year boundaries, including leap years, using the ISOChronology.
 */
@RunWith(JUnit4.class)
public class PartialDayOfYearAdditionTest {

    // By explicitly using the UTC chronology, we make the test self-contained, deterministic,
    // and easier to understand, removing the need for complex setUp/tearDown methods.
    private static final Chronology UTC_CHRONOLOGY = ISOChronology.getInstanceUTC();

    /**
     * This test verifies that adding a large number of days to a Partial,
     * which consists of a year and a dayOfYear, correctly wraps across multiple
     * years, including a leap year.
     */
    @Test
    public void addDaysToPartialWithDayOfYear_shouldWrapAcrossMultipleYears() {
        // GIVEN: A partial date representing the last day of a leap year (2000-12-31).
        // A Partial is an immutable partial date-time, here composed of (year, dayOfYear).
        DateTimeFieldType[] types = {DateTimeFieldType.year(), DateTimeFieldType.dayOfYear()};
        int[] startValues = {2000, 366}; // Year 2000 was a leap year.
        Partial startPartial = new Partial(types, startValues, UTC_CHRONOLOGY);

        // AND: A target partial date representing the last day of a future leap year (2004-12-31).
        int[] endValues = {2004, 366}; // Year 2004 was a leap year.
        Partial expectedEndPartial = new Partial(types, endValues, UTC_CHRONOLOGY);

        // AND: The total number of days spanning the years 2001, 2002, 2003, and 2004.
        final int daysIn2001 = 365;
        final int daysIn2002 = 365;
        final int daysIn2003 = 365;
        final int daysIn2004 = 366; // Leap year
        final int totalDaysToAdd = daysIn2001 + daysIn2002 + daysIn2003 + daysIn2004;

        // WHEN: We add the total number of days to the start partial.
        Partial actualEndPartial = startPartial.withFieldAdded(DurationFieldType.days(), totalDaysToAdd);

        // THEN: The result should match the target partial date.
        assertEquals(expectedEndPartial, actualEndPartial);

        // --- Verification of the inverse operation ---

        // WHEN: We subtract the same number of days from the end partial.
        Partial actualStartPartial = expectedEndPartial.withFieldAdded(DurationFieldType.days(), -totalDaysToAdd);

        // THEN: The result should match the original start partial.
        assertEquals(startPartial, actualStartPartial);
    }
}