package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Verifies that the getYears() method returns the same integer value
     * that was used to create the Years instance via the factory method.
     */
    @Test
    public void getYears_shouldReturnTheValueUsedInFactory() {
        // Arrange: Create a Years instance representing zero years.
        final int expectedValue = 0;
        Years years = Years.years(expectedValue);

        // Act: Call the method under test.
        int actualValue = years.getYears();

        // Assert: Verify that the returned value matches the initial value.
        assertEquals(expectedValue, actualValue);
    }
}