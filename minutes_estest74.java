package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void toStandardWeeks_fromOneMinute_returnsZeroWeeks() {
        // Arrange
        Minutes oneMinute = Minutes.ONE;
        
        // Act
        // Convert the one-minute duration to standard weeks.
        Weeks resultInWeeks = oneMinute.toStandardWeeks();
        
        // Assert
        // One minute is less than the number of minutes in a standard week (10,080),
        // so the conversion should result in zero whole weeks.
        assertEquals(Weeks.ZERO, resultInWeeks);
    }
}