package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that getModifiedJulianDay() returns the correct value for the Java epoch.
     * The Java epoch (1970-01-01T00:00:00Z) corresponds to Modified Julian Day 40587.
     */
    @Test
    public void getModifiedJulianDay_forJavaEpoch_isCorrect() {
        // Arrange
        // The Java epoch is 1970-01-01T00:00:00Z.
        Instant javaEpoch = Instant.EPOCH;
        UtcInstant utcInstant = UtcInstant.of(javaEpoch);

        // The Modified Julian Day epoch starts on 1858-11-17.
        // The number of days between 1858-11-17 and 1970-01-01 is 40587.
        long expectedModifiedJulianDay = 40587L;

        // Act
        long actualModifiedJulianDay = utcInstant.getModifiedJulianDay();

        // Assert
        assertEquals("Modified Julian Day should match the expected value for the Java epoch",
                expectedModifiedJulianDay, actualModifiedJulianDay);
        assertEquals("Nano of day should be zero at the start of the epoch",
                0L, utcInstant.getNanoOfDay());
    }
}