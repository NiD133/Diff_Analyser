package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link Weeks#isGreaterThan(Weeks)} method.
 */
public class WeeksTest {

    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingAnInstanceToItself() {
        // Arrange: Create a Weeks instance with a clear, representative value.
        final int numberOfWeeks = 52;
        Weeks weeks = Weeks.weeks(numberOfWeeks);

        // Act: Compare the Weeks instance to itself.
        boolean isGreater = weeks.isGreaterThan(weeks);

        // Assert: Verify that an instance is not considered greater than itself.
        assertFalse("A Weeks instance should not be greater than itself.", isGreater);
        
        // Additionally, verify that the comparison did not alter the object's state (immutability).
        assertEquals("The number of weeks should remain unchanged after the comparison.",
                     numberOfWeeks, weeks.getWeeks());
    }
}