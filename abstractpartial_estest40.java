package org.joda.time.base;

import org.joda.time.MonthDay;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the isBefore() method in {@link AbstractPartial}.
 */
public class AbstractPartialTest {

    /**
     * Tests that isBefore() returns true when the partial instant is
     * chronologically earlier than the one it is compared to.
     */
    @Test
    public void isBefore_shouldReturnTrue_whenComparingToLaterPartial() {
        // Arrange: Create two MonthDay instances, where the first is one day before the second.
        // Using specific dates makes the test deterministic and independent of the system clock.
        MonthDay partial = new MonthDay(2, 14); // February 14th
        MonthDay laterPartial = new MonthDay(2, 15); // February 15th

        // Act: Check if the first partial is before the second.
        boolean isBefore = partial.isBefore(laterPartial);

        // Assert: The result should be true.
        assertTrue("A partial should be considered 'before' a chronologically later one.", isBefore);
    }
}