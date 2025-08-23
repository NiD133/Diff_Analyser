package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that negating a Weeks object representing zero weeks
     * results in a Weeks object that is also zero.
     */
    @Test
    public void negated_onZero_returnsZero() {
        // Arrange: Create a Weeks object representing zero.
        Weeks zeroWeeks = Weeks.ZERO;

        // Act: Negate the zero-week object.
        Weeks result = zeroWeeks.negated();

        // Assert: The result should still be equivalent to zero weeks.
        assertEquals(Weeks.ZERO, result);
    }
}