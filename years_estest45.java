package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that the static factory method `years(int)` creates an instance
     * with the correct number of years, which is retrievable via `getYears()`.
     */
    @Test
    public void yearsFactory_shouldCreateInstanceWithCorrectValue() {
        // Arrange: Define the expected number of years.
        final int expectedYears = 2;

        // Act: Create a Years instance using the static factory method.
        Years twoYears = Years.years(expectedYears);

        // Assert: Verify that the getter returns the expected value.
        assertEquals(expectedYears, twoYears.getYears());
    }
}