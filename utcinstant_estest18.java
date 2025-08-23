package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * An improved version of an automatically generated test for {@link UtcInstant}.
 */
public class UtcInstant_ESTestTest18 extends UtcInstant_ESTest_scaffolding {

    /**
     * Tests that parsing a valid ISO 8601 string correctly calculates the
     * Modified Julian Day (MJD) and the nano-of-day.
     * <p>
     * This test uses a date before the modern UTC epoch (1972) to ensure
     * that proleptic (historical) calculations are handled correctly.
     */
    @Test
    public void parse_validIsoString_calculatesCorrectMjdAndNanoOfDay() {
        // Arrange
        String instantText = "1859-09-09T00:00:00Z";
        
        // The Modified Julian Day epoch is 1858-11-17.
        // The date 1859-09-09 is 296 days after this epoch.
        long expectedMjd = 296L;
        long expectedNanoOfDay = 0L;

        // Act
        UtcInstant utcInstant = UtcInstant.parse(instantText);

        // Assert
        assertEquals("Nano-of-day should be zero for a time of midnight.",
                expectedNanoOfDay, utcInstant.getNanoOfDay());
        assertEquals("Modified Julian Day should be calculated correctly from its epoch.",
                expectedMjd, utcInstant.getModifiedJulianDay());
    }
}