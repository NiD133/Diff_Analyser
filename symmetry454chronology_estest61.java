package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Symmetry454Chronology}.
 * This focuses on validating the creation of dates with invalid parameters.
 */
public class Symmetry454Chronology_ImprovedTest {

    /**
     * Tests that creating a date with a day-of-month value that is invalid for
     * the given month throws a DateTimeException.
     *
     * <p>In the Symmetry454 calendar, January is a "short" month with 28 days.
     * This test attempts to create a date for January 35th, which should fail.
     */
    @Test
    public void date_throwsException_whenDayOfMonthIsInvalidForShortMonth() {
        // Arrange: Set up the chronology and invalid date components.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        int year = 1;
        int month = 1; // January, a short month with 28 days
        int invalidDayOfMonth = 35;

        // Act & Assert: Attempt to create the date and verify the correct exception.
        try {
            chronology.date(year, month, invalidDayOfMonth);
            fail("Expected a DateTimeException to be thrown for an invalid day of month.");
        } catch (DateTimeException e) {
            // Verify that the exception message is as expected, confirming
            // that the correct validation was triggered.
            assertEquals("Invalid date: 1/1/35", e.getMessage());
        }
    }
}