package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void yearsFactory_shouldCreateYearsWithMinimumValue() {
        // Arrange: Define the minimum possible integer value for years.
        final int minValue = Integer.MIN_VALUE;

        // Act: Create a Years instance using the factory method.
        Years years = Years.years(minValue);

        // Assert: Verify that the created instance holds the correct minimum value.
        assertEquals(minValue, years.getYears());
    }
}