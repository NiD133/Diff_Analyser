package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that the factory method {@link Years#years(int)} creates an instance
     * holding the specified number of years.
     */
    @Test
    public void shouldCreateYearsInstanceWithCorrectValue() {
        // Define the number of years to test with
        final int expectedYears = 3;

        // Create a Years instance using the factory method
        Years years = Years.years(expectedYears);

        // Verify that the created instance holds the correct value
        assertEquals(expectedYears, years.getYears());
    }
}