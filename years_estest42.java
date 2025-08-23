package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void isLessThan_shouldReturnTrue_whenComparingToLargerYears() {
        // Arrange: Set up two Years instances, one smaller than the other.
        final Years smallerYears = Years.ZERO; // 0 years
        final Years largerYears = Years.TWO.plus(2); // 2 + 2 = 4 years

        // Act: Perform the comparison.
        final boolean result = smallerYears.isLessThan(largerYears);

        // Assert: Verify the outcome.
        // First, a sanity check to ensure our 'largerYears' variable is what we expect.
        assertEquals("The value of largerYears should be 4", 4, largerYears.getYears());
        
        // Now, verify the main logic: 0 is indeed less than 4.
        assertTrue("isLessThan should return true when comparing a smaller value to a larger one", result);
    }
}