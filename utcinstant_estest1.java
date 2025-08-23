package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link UtcInstant} class, focusing on clarity and maintainability.
 */
public class UtcInstantTest {

    /**
     * Tests that the ofModifiedJulianDay factory method correctly creates an instance
     * and that the getModifiedJulianDay getter returns the same value.
     */
    @Test
    public void ofModifiedJulianDay_createsInstanceWithCorrectModifiedJulianDay() {
        // Arrange: Define a valid Modified Julian Day and a valid nano-of-day.
        // Using the MJD for a known date (2024-01-01) makes the test data meaningful.
        long expectedModifiedJulianDay = 60309L; // MJD for 2024-01-01
        long anyValidNanoOfDay = 12345L;

        // Act: Create a UtcInstant instance using the factory method.
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(expectedModifiedJulianDay, anyValidNanoOfDay);

        // Assert: Verify that the getter returns the value used during construction.
        long actualModifiedJulianDay = utcInstant.getModifiedJulianDay();
        assertEquals(expectedModifiedJulianDay, actualModifiedJulianDay);
    }
}