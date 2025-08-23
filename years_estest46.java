package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void yearsFactory_createsInstanceWithCorrectValue() {
        // Arrange: Define the number of years to test.
        final int expectedYears = 1;

        // Act: Create a Years instance using the factory method.
        Years years = Years.years(expectedYears);

        // Assert: Verify that the created instance holds the correct value.
        assertEquals(expectedYears, years.getYears());
    }
}