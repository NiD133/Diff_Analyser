package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void multipliedBy_shouldReturnCorrectProductOfWeeks() {
        // Arrange: Start with a known period of one week.
        Weeks oneWeek = Weeks.ONE;
        int scalar = 5;

        // Act: Multiply the period by the scalar.
        Weeks multipliedWeeks = oneWeek.multipliedBy(scalar);

        // Assert: The result should be a period equal to the scalar value in weeks.
        int expectedWeeks = 5;
        assertEquals(expectedWeeks, multipliedWeeks.getWeeks());
    }
}