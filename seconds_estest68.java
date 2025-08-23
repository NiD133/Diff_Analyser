package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void getPeriodType_shouldReturnSecondsPeriodType() {
        // Arrange: Create an instance of the Seconds class.
        Seconds seconds = Seconds.ONE;
        PeriodType expectedPeriodType = PeriodType.seconds();

        // Act: Call the method under test.
        PeriodType actualPeriodType = seconds.getPeriodType();

        // Assert: Verify that the returned PeriodType is specifically for seconds.
        assertEquals("A Seconds object must have a PeriodType of 'seconds'.", expectedPeriodType, actualPeriodType);
    }
}