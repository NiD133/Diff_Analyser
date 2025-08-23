package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains improved tests for the DayOfYear class.
 * Note: The original class name and inheritance are preserved to match the input structure.
 * In a typical project, this would be named DayOfYearTest.
 */
public class DayOfYear_ESTestTest42 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that the hashCode() method returns the integer value of the day-of-year.
     * This aligns with the behavior of a value-based class where the hash code
     * is derived directly from its primary value.
     */
    @Test
    public void hashCode_shouldReturnTheDayValue() {
        // Arrange: Create a DayOfYear instance for a specific day.
        DayOfYear dayOfYear = DayOfYear.of(1);
        int expectedHashCode = 1;

        // Act: Calculate the hash code.
        int actualHashCode = dayOfYear.hashCode();

        // Assert: Verify that the hash code is equal to the day's integer value.
        assertEquals(expectedHashCode, actualHashCode);
    }
}