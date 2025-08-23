package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that isLessThan() returns false when comparing a zero-week instance to null.
     * According to the Javadoc, a null comparison is treated as comparing to zero.
     */
    @Test
    public void isLessThan_comparingZeroWeeksToNull_shouldReturnFalse() {
        // Arrange: The Javadoc for isLessThan() specifies that a null input
        // is treated as a zero-value period. We create a zero-week instance
        // to test the comparison of 0 < 0.
        Weeks zeroWeeks = Weeks.ZERO;

        // Act: Call the method under test with a null argument.
        boolean result = zeroWeeks.isLessThan(null);

        // Assert: Since 0 is not less than 0, the result should be false.
        assertFalse("Weeks.ZERO should not be less than null (which is treated as zero)", result);
    }
}