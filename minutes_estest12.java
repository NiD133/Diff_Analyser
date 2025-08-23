package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void toStandardDuration_forMinValue_convertsToDurationWithSameNumberOfMinutes() {
        // Arrange: Create a Minutes instance representing the minimum possible value.
        // Minutes.MIN_VALUE is backed by Integer.MIN_VALUE.
        Minutes minValueMinutes = Minutes.MIN_VALUE;
        long expectedMinutes = Integer.MIN_VALUE;

        // Act: Convert the Minutes object to a standard Duration.
        Duration resultingDuration = minValueMinutes.toStandardDuration();

        // Assert: Verify that the resulting Duration holds the same number of minutes.
        // Duration.getStandardMinutes() returns a long, so we compare against a long.
        assertEquals(
            "The duration should correctly represent the minimum number of minutes.",
            expectedMinutes,
            resultingDuration.getStandardMinutes()
        );
    }
}