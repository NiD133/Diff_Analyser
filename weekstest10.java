package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void getPeriodType_shouldReturnWeeksPeriodType() {
        // Arrange
        Weeks weeks = Weeks.weeks(20);
        PeriodType expectedType = PeriodType.weeks();

        // Act
        PeriodType actualType = weeks.getPeriodType();

        // Assert
        assertEquals(expectedType, actualType);
    }
}