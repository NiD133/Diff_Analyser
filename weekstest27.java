package org.joda.time;

import junit.framework.TestCase;

/**
 * Test cases for the Weeks class.
 */
public class WeeksTest extends TestCase {

    /**
     * Tests that adding a Weeks period to a LocalDate correctly calculates the new date.
     */
    public void testPlus_addsWeeksToLocalDate() {
        // Arrange: Set up the initial state and expected outcome.
        // Using the expressive constant provided by the Weeks class.
        final Weeks weeksToAdd = Weeks.THREE;
        final LocalDate startDate = new LocalDate(2006, 6, 1);
        
        // The expected date is 3 weeks (21 days) after the start date.
        final LocalDate expectedDate = new LocalDate(2006, 6, 22);

        // Act: Perform the action under test.
        final LocalDate actualDate = startDate.plus(weeksToAdd);

        // Assert: Verify the action produced the correct result.
        assertEquals(expectedDate, actualDate);
    }
}