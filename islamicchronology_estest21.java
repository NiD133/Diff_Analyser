package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link IslamicChronology} class, focusing on edge cases.
 */
public class IslamicChronologyTest {

    /**
     * This test verifies the behavior of getMonthOfYear when the provided millisecond instant
     * is far outside the calendar year given as context.
     *
     * <p>The method is expected to perform its calculation based on these mismatched inputs,
     * resulting in a large negative month value. This serves as a regression test to ensure
     * consistent behavior for this specific edge case, which might occur with unusual
     * data or during complex date manipulations.</p>
     */
    @Test
    public void getMonthOfYear_whenInstantIsFarOutsideTheGivenYear_calculatesLargeNegativeMonth() {
        // Arrange
        // The maximum supported year in IslamicChronology.
        final int maxSupportedYear = 292271022;
        // A millisecond instant very close to the standard Java epoch (1970-01-01).
        final long instantNearEpoch = 1518L;
        // The expected, large negative month value that results from the calculation.
        final int expectedMonth = -16657633;

        IslamicChronology chronology = IslamicChronology.getInstanceUTC();

        // Act
        // Calculate the month of the year for an instant in 1970, but within the context of the max year.
        int actualMonth = chronology.getMonthOfYear(instantNearEpoch, maxSupportedYear);

        // Assert
        assertEquals(expectedMonth, actualMonth);
    }
}