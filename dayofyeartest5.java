package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * This test suite contains tests for the DayOfYear class.
 */
public class DayOfYearTest {

    /**
     * Tests that comparing an earlier DayOfYear to a later one results in a negative value.
     * This value should be the difference between the two day-of-year values.
     */
    @Test
    public void compareTo_withLaterDay_shouldReturnNegativeDifference() {
        // Arrange: Set up the test data and conditions.
        // Create an instance for the first day of the year.
        DayOfYear firstDay = DayOfYear.of(1);

        // Create a fixed clock set to February 14th, 2014. This date is the 45th day of that year.
        // Using a fixed clock makes the test deterministic and independent of the actual system time.
        LocalDate laterDate = LocalDate.of(2014, 2, 14); // The 45th day of the year
        Clock fixedClock = Clock.fixed(laterDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        // Create a DayOfYear instance from the fixed clock.
        DayOfYear fortyFifthDay = DayOfYear.now(fixedClock);

        // Act: Execute the method under test.
        int comparisonResult = firstDay.compareTo(fortyFifthDay);

        // Assert: Verify the outcome.
        // The result should be the value of the first day (1) minus the value of the second day (45).
        int expectedDifference = 1 - 45;
        assertEquals(expectedDifference, comparisonResult);
    }
}