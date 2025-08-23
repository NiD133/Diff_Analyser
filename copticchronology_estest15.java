package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link CopticChronology} class.
 */
public class CopticChronologyTest {

    /**
     * The maximum year supported by CopticChronology, as defined in the source.
     * This is used to test behavior at the upper bounds of the chronology.
     */
    private static final int COPTIC_MAX_YEAR = 292272708;

    @Test
    public void isLeapDay_forNonLeapDayAtMaxSupportedYear_returnsFalse() {
        // Arrange: Create a CopticChronology instance and a date near the maximum
        // supported value. This date, December 26th of the max year, is not a leap day.
        // Using an explicit date makes the test's intent clear, avoiding magic numbers.
        CopticChronology copticChronology = CopticChronology.getInstance(DateTimeZone.UTC);
        DateTime dateAtMaxYear = new DateTime(COPTIC_MAX_YEAR, 12, 26, 0, 0, copticChronology);
        long instant = dateAtMaxYear.getMillis();

        // Act: Check if this instant is considered a leap day.
        boolean isLeapDay = copticChronology.isLeapDay(instant);

        // Assert: The result should be false, as it's a regular day.
        assertFalse("A regular day at the maximum supported year should not be a leap day.", isLeapDay);
    }
}