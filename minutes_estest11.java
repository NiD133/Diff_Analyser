package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that converting {@link Minutes#ONE} to a standard duration results in the
     * correct number of milliseconds (60,000).
     */
    @Test
    public void toStandardDuration_forOneMinute_returnsCorrectDurationInMilliseconds() {
        // Arrange
        Minutes oneMinute = Minutes.ONE;
        long expectedMilliseconds = DateTimeConstants.MILLIS_PER_MINUTE;

        // Act
        Duration duration = oneMinute.toStandardDuration();

        // Assert
        assertEquals("One minute should be equivalent to 60,000 milliseconds",
                     expectedMilliseconds, duration.getMillis());
    }
}