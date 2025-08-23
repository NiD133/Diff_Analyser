package org.joda.time.chrono;

import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Unit tests for {@link CopticChronology}.
 */
public class CopticChronologyTest {

    @Test
    public void isLeap_forDateOnCopticLeapDay_returnsTrueForAllProperties() {
        // The Coptic calendar has 12 months of 30 days, plus a 13th "intercalary"
        // month of 5 days in a common year and 6 days in a leap year.
        // The 6th day of the 13th month is the leap day and only exists in a leap year.
        // Coptic year 3 is a leap year.
        final Chronology copticChronology = CopticChronology.getInstance();
        final DateTime copticLeapDate = new DateTime(3, 13, 6, 0, 0, copticChronology);

        // Assert that the year property correctly identifies a leap year
        assertTrue("Year 3 should be a leap year", copticLeapDate.year().isLeap());

        // Assert that the month property identifies the 13th month as a leap month
        assertTrue("Month 13 in a leap year should be a leap month", copticLeapDate.monthOfYear().isLeap());

        // Assert that the day of month property identifies the 6th day as a leap day
        assertTrue("Day 6 of month 13 should be a leap day", copticLeapDate.dayOfMonth().isLeap());

        // Assert that the day of year property identifies the last day as a leap day
        assertTrue("The 366th day of the year should be a leap day", copticLeapDate.dayOfYear().isLeap());
    }
}