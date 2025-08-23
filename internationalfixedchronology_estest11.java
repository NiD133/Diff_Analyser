package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.LocalDate;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link InternationalFixedChronology}.
 * This class contains the improved test case.
 */
public class InternationalFixedChronology_ESTestTest11 extends InternationalFixedChronology_ESTest_scaffolding {

    /**
     * Tests that {@link InternationalFixedChronology#dateYearDay(int, int)}
     * correctly creates a date for a given day of a non-leap year.
     */
    @Test
    public void dateYearDay_forNonLeapYear_createsCorrectDate() {
        // Arrange
        final InternationalFixedChronology ifcChronology = InternationalFixedChronology.INSTANCE;
        final int year = 4314; // A non-leap year (not divisible by 4)
        final int dayOfYear = 7;

        // According to its documentation, the International Fixed Calendar is aligned with the
        // ISO calendar. The 7th day of year 4314 in IFC should correspond to Jan 7, 4314 in ISO.
        // We use LocalDate to calculate the expected epoch day to make the test self-documenting.
        final long expectedEpochDay = LocalDate.of(year, 1, dayOfYear).toEpochDay();
        final int expectedLengthOfYear = 365;

        // Act
        InternationalFixedDate ifcDate = ifcChronology.dateYearDay(year, dayOfYear);

        // Assert
        assertEquals("Epoch day should match the equivalent ISO date", expectedEpochDay, ifcDate.toEpochDay());
        assertEquals("Year 4314 should be a non-leap year with 365 days", expectedLengthOfYear, ifcDate.lengthOfYear());
    }
}