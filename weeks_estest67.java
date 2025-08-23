package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that getPeriodType() correctly returns the PeriodType for 'weeks'.
     */
    @Test
    public void getPeriodType_shouldReturnWeeksPeriodType() {
        // Arrange
        Weeks twoWeeks = Weeks.TWO;
        PeriodType expectedPeriodType = PeriodType.weeks();

        // Act
        PeriodType actualPeriodType = twoWeeks.getPeriodType();

        // Assert
        assertEquals("The period type for Weeks should be 'weeks'", expectedPeriodType, actualPeriodType);
    }
}