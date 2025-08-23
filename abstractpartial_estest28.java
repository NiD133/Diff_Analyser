package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals; // Only assertEquals is used for this test
import org.joda.time.MonthDay;
import org.joda.time.Weeks;

/**
 * Tests for duration operations on the MonthDay class, which extends AbstractPartial.
 * This test specifically verifies the behavior of subtracting week-based durations.
 */
// Renamed the class to reflect the specific Joda-Time class being tested (MonthDay)
// and the type of operations (Duration Operations).
// The "_ESTest_scaffolding" part is an EvoSuite artifact; in a real project,
// its contents would ideally be integrated or the inheritance removed if not needed.
public class MonthDayDurationOperationsTest extends AbstractPartial_ESTest_scaffolding {

    /**
     * Verifies that subtracting a 'Weeks' duration from a 'MonthDay' object
     * does not change the MonthDay's month or day fields.
     * This is expected behavior because MonthDay represents only the month and day,
     * independent of the year, and thus cannot meaningfully apply week-based
     * duration changes without a year context.
     */
    @Test(timeout = 4000) // Keep the timeout as it's a valid test attribute.
    // Renamed the method to clearly describe the specific behavior being tested.
    public void testMonthDay_minusWeeks_hasNoEffect() {
        // Arrange: Set up the initial state for the test.
        // Create a MonthDay object representing the current month and day.
        MonthDay initialMonthDay = new MonthDay();
        // Define a duration of two weeks.
        Weeks weeksToSubtract = Weeks.TWO;

        // Act: Perform the operation being tested.
        // Subtract the weeks duration from the MonthDay.
        MonthDay monthDayAfterSubtraction = initialMonthDay.minus(weeksToSubtract);

        // Assert: Verify the outcome.
        // Confirm that the MonthDay object remains unchanged after subtracting weeks.
        // This assertion includes a descriptive message for better failure diagnosis.
        assertEquals("Subtracting weeks from a MonthDay should not change its month or day fields " +
                     "as MonthDay lacks year context to resolve such durations.",
                     initialMonthDay,
                     monthDayAfterSubtraction);
    }
}