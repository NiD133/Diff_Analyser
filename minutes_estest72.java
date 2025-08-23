package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void toStandardDays_forZeroMinutes_returnsZeroDays() {
        // Arrange
        Minutes zeroMinutes = Minutes.ZERO;
        Days expectedDays = Days.ZERO;

        // Act
        Days actualDays = zeroMinutes.toStandardDays();

        // Assert
        assertEquals(expectedDays, actualDays);
    }
}