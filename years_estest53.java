package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite contains tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Verifies that a `Years` object, created using the `years()` factory method,
     * is initialized with the correct state. It checks both the number of years
     * and the period type.
     */
    @Test
    public void yearsFactory_shouldCreateObjectWithCorrectState() {
        // Arrange: Define the expected number of years.
        final int expectedYears = 0;

        // Act: Create a Years instance using the factory method.
        Years years = Years.years(expectedYears);

        // Assert: Verify that the object's state is correct.
        // 1. The number of years should match the value provided to the factory.
        assertEquals(expectedYears, years.getYears());
        
        // 2. The period type should be correctly set to PeriodType.years().
        assertEquals(PeriodType.years(), years.getPeriodType());
    }
}