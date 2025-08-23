package org.threeten.extra.scale;

import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.JulianFields;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    @Test
    public void parse_validIsoString_createsCorrectInstant() {
        // Arrange
        String isoString = "1958-01-01T00:53:27.000001Z";

        // For clarity, calculate the expected internal state from the input string's components.
        // The date component "1958-01-01" corresponds to a specific Modified Julian Day.
        long expectedMjd = LocalDate.of(1958, 1, 1).getLong(JulianFields.MODIFIED_JULIAN_DAY);

        // The time component "00:53:27.000001" corresponds to a specific nanosecond of the day.
        // (53 minutes * 60 seconds/minute + 27 seconds) * 1,000,000,000 nanos/second + 1,000 nanoseconds
        long expectedNanoOfDay = (53L * 60L + 27L) * 1_000_000_000L + 1_000L;

        // Act
        UtcInstant parsedInstant = UtcInstant.parse(isoString);

        // Assert
        assertEquals("Modified Julian Day should match the parsed date",
                expectedMjd, parsedInstant.getModifiedJulianDay());
        assertEquals("Nano of day should match the parsed time",
                expectedNanoOfDay, parsedInstant.getNanoOfDay());

        // Also, verify that the string representation matches the original input (round-trip check)
        assertEquals("toString() should return the original parsed string",
                isoString, parsedInstant.toString());
    }
}