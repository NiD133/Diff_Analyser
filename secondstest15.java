package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    // Test case for the toStandardWeeks() method.
    @Test
    public void toStandardWeeks_forDurationOfTwoWeeks_returnsTwoWeeks() {
        // Arrange: Create a Seconds instance representing the duration of two standard weeks.
        // Using the library's constant for seconds in a week makes the setup clear and robust.
        final int twoWeeksInSeconds = 2 * DateTimeConstants.SECONDS_PER_WEEK;
        Seconds seconds = Seconds.seconds(twoWeeksInSeconds);
        Weeks expectedWeeks = Weeks.weeks(2);

        // Act: Convert the Seconds object to Weeks.
        Weeks actualWeeks = seconds.toStandardWeeks();

        // Assert: The result should be a Weeks object with the value 2.
        assertEquals(expectedWeeks, actualWeeks);
    }
}