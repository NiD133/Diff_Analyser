package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This class contains tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that isGreaterThan() returns false when comparing a zero-week instance to null.
     * <p>
     * According to the Javadoc, a null comparison value is treated as a zero-value period.
     * Therefore, this test effectively checks if zero is greater than zero.
     */
    @Test
    public void isGreaterThan_whenComparingZeroWeeksToNull_shouldReturnFalse() {
        // Arrange: Create a Weeks object with a value of zero.
        // The standardWeeksIn() factory method returns zero for a null input.
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        assertEquals("Precondition: standardWeeksIn(null) should create a Weeks object of 0.", 0, zeroWeeks.getWeeks());

        // Act: Compare the zero-week object to null.
        boolean result = zeroWeeks.isGreaterThan(null);

        // Assert: The result should be false, as 0 is not greater than 0.
        assertFalse("A zero-week period should not be greater than null (which is treated as zero).", result);
    }
}