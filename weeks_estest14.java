package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void toStandardDays_convertsThreeWeeksToTwentyOneDays() {
        // Arrange
        Weeks threeWeeks = Weeks.THREE;
        int expectedDays = 21; // 3 weeks * 7 days/week

        // Act
        Days actualDays = threeWeeks.toStandardDays();

        // Assert
        assertEquals(expectedDays, actualDays.getDays());
    }
}