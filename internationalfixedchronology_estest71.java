package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.chrono.Era;

import static org.junit.Assert.assertEquals;

/**
 * This class contains improved tests for the {@link InternationalFixedChronology}.
 * The original test was part of an auto-generated suite and has been refactored for clarity.
 */
public class InternationalFixedChronologyImprovedTest {

    @Test
    public void dateFromEraYearMonthDay_createsDateWithCorrectEpochDay() {
        // Arrange: Define the components of the date to be created.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        Era era = InternationalFixedEra.CE;
        int yearOfEra = 7;
        int month = 7;
        int dayOfMonth = 7;
        long expectedEpochDay = -716797L; // The pre-calculated epoch day for 0007-07-07 (IFC).

        // Act: Create an InternationalFixedDate using the specified components.
        InternationalFixedDate date = chronology.date(era, yearOfEra, month, dayOfMonth);

        // Assert: Verify that the created date has the correct epoch day value.
        assertEquals(expectedEpochDay, date.toEpochDay());
    }
}