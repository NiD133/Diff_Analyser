package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that converting a Seconds value that is less than a full minute (60 seconds)
     * to standard minutes results in zero minutes, as the conversion truncates fractional parts.
     */
    @Test
    public void toStandardMinutes_shouldReturnZero_whenConvertingLessThanSixtySeconds() {
        // Arrange: A Seconds instance representing 2 seconds, which is less than a minute.
        Seconds twoSeconds = Seconds.TWO;

        // Act: Convert the Seconds object to standard minutes.
        Minutes resultingMinutes = twoSeconds.toStandardMinutes();

        // Assert: The result should be 0 minutes, as 2 seconds does not constitute a full minute.
        assertEquals(0, resultingMinutes.getMinutes());
    }
}