package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that a date within a long month of a non-leap year has the correct properties.
     *
     * <p>The International Fixed Calendar (IFC) has 13 months. In a non-leap year,
     * month 12 has 29 days, while the other 12 months have 28 days. A non-leap year
     * has a total of 365 days.
     */
    @Test
    public void dateInLongMonthOfNonLeapYear_hasCorrectProperties() {
        // Arrange:
        // The year 1971 is a non-leap year in both the ISO and IFC calendars.
        // We select an ISO date that corresponds to a date in the 12th month of the IFC.
        // IFC Month 12 in a non-leap year runs from day-of-year 309 to 337.
        // The ISO date 1971-11-17 is the 321st day of the year, which falls within this range.
        InternationalFixedChronology ifcChronology = InternationalFixedChronology.INSTANCE;
        LocalDate isoDate = LocalDate.of(1971, Month.NOVEMBER, 17);

        // Act:
        // Convert the ISO date to an InternationalFixedDate.
        InternationalFixedDate ifcDate = ifcChronology.date(isoDate);

        // Assert:
        // Verify that the date is correctly identified as being in a non-leap year.
        assertEquals("Year length for a non-leap year should be 365", 365, ifcDate.lengthOfYear());

        // Verify that the date falls within the 12th month, which has 29 days.
        assertEquals("Month of year should be 12", 12, ifcDate.getMonthValue());
        assertEquals("Length of the 12th month should be 29", 29, ifcDate.lengthOfMonth());
    }
}