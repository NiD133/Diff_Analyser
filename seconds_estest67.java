package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class, focusing on period conversions.
 */
public class SecondsTest {

    /**
     * Tests that converting a Seconds value that is less than a full week
     * into standard weeks results in zero weeks.
     */
    @Test
    public void toStandardWeeks_whenSecondsIsLessThanAWeek_returnsZeroWeeks() {
        // Arrange: A standard week contains 604,800 seconds. We use a value of two seconds,
        // which is clearly less than a full week.
        Seconds twoSeconds = Seconds.TWO;
        Weeks expectedWeeks = Weeks.ZERO;

        // Act: Convert the Seconds object to standard weeks.
        Weeks actualWeeks = twoSeconds.toStandardWeeks();

        // Assert: The result should be equivalent to Weeks.ZERO.
        assertEquals(expectedWeeks, actualWeeks);
    }
}