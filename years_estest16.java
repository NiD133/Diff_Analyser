package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Years class.
 */
public class YearsTest {

    /**
     * Verifies that the plus(int) method correctly adds a given number of years
     * to an existing Years instance.
     */
    @Test
    public void plus_addsYearsToInstance() {
        // Arrange: Start with a Years instance representing 2 years.
        Years twoYears = Years.TWO;
        int yearsToAdd = 2;
        int expectedTotalYears = 4;

        // Act: Add 2 more years.
        Years result = twoYears.plus(yearsToAdd);

        // Assert: The new instance should represent 4 years.
        assertEquals(expectedTotalYears, result.getYears());
    }
}