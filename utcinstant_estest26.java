package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that getModifiedJulianDay() returns the same value that was used
     * to create the UtcInstant.
     */
    @Test
    public void getModifiedJulianDay_returnsValueUsedInCreation() {
        // Arrange: Create a UtcInstant at the start of Modified Julian Day 0.
        long expectedMjd = 0L;
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(expectedMjd, 0L);

        // Act: Retrieve the Modified Julian Day from the created instant.
        long actualMjd = instant.getModifiedJulianDay();

        // Assert: The retrieved value should match the expected value.
        assertEquals(expectedMjd, actualMjd);
    }
}