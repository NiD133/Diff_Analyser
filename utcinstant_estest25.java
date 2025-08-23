package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    @Test
    public void ofModifiedJulianDay_gettersReturnCorrectValues() {
        // Arrange: Define the input values for creating a UtcInstant.
        long expectedModifiedJulianDay = 54L;
        long expectedNanoOfDay = 54L;

        // Act: Create the UtcInstant and retrieve its properties.
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(expectedModifiedJulianDay, expectedNanoOfDay);
        long actualModifiedJulianDay = utcInstant.getModifiedJulianDay();
        long actualNanoOfDay = utcInstant.getNanoOfDay();

        // Assert: Verify that the retrieved properties match the initial values.
        assertEquals("Modified Julian Day should match the value provided at creation",
                expectedModifiedJulianDay, actualModifiedJulianDay);
        assertEquals("Nano of day should match the value provided at creation",
                expectedNanoOfDay, actualNanoOfDay);
    }
}