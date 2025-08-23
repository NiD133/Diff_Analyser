package org.threeten.extra.scale;

import org.junit.Test;
import java.time.DateTimeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link UtcInstant} class.
 */
public class UtcInstantTest {

    /**
     * Tests that {@code UtcInstant.ofModifiedJulianDay} throws a {@code DateTimeException}
     * when the nano-of-day argument is negative.
     *
     * According to the method's contract, the nano-of-day must be a positive value
     * representing the number of nanoseconds from the start of the day.
     */
    @Test
    public void ofModifiedJulianDay_whenNanoOfDayIsNegative_throwsException() {
        // Arrange: Define a valid Modified Julian Day and an invalid negative nano-of-day.
        long modifiedJulianDay = -361L;
        long invalidNanoOfDay = -361L;

        // Act & Assert: Attempt to create the UtcInstant and verify the exception.
        try {
            UtcInstant.ofModifiedJulianDay(modifiedJulianDay, invalidNanoOfDay);
            fail("Expected a DateTimeException to be thrown, but it was not.");
        } catch (DateTimeException e) {
            // Verify that the exception message is correct, as it provides crucial context.
            String expectedMessage = "Nanosecond-of-day must be between 0 and 86400000000000 on date -361";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}