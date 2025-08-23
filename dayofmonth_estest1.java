package org.threeten.extra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Unit tests for the DayOfMonth class, focusing on the equals() method.
 */
public class DayOfMonthTest {

    @Test
    public void equals_returnsFalse_forDifferentDayOfMonthInstances() {
        // Arrange: Create two DayOfMonth instances with different values.
        DayOfMonth day31 = DayOfMonth.of(31);
        DayOfMonth day15 = DayOfMonth.of(15);

        // Act & Assert: Verify that the two instances are not considered equal.
        // The check is performed both ways to ensure the equals() method is symmetric.
        assertNotEquals(day31, day15);
        assertNotEquals(day15, day31);
    }

    @Test
    public void equals_returnsTrue_forSameDayOfMonthInstances() {
        // Arrange: Create two DayOfMonth instances representing the same day.
        // The 'of' method is expected to return cached, identical instances.
        DayOfMonth day20_first_instance = DayOfMonth.of(20);
        DayOfMonth day20_second_instance = DayOfMonth.of(20);

        // Act & Assert: Verify that the two instances are considered equal.
        assertEquals(day20_first_instance, day20_second_instance);
    }
}