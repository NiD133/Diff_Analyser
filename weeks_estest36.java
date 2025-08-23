package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that the {@code Weeks.weeks(int)} factory method correctly creates an instance
     * when provided with the maximum possible integer value.
     */
    @Test
    public void testWeeksFactoryWithMaxValue() {
        // Arrange
        final int expectedWeeks = Integer.MAX_VALUE;

        // Act
        final Weeks weeks = Weeks.weeks(expectedWeeks);

        // Assert
        assertEquals("The number of weeks should match the maximum integer value provided.",
                expectedWeeks, weeks.getWeeks());
    }
}