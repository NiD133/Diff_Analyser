package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void getPeriodType_shouldReturnYearsType() {
        // Arrange: Create an instance of Years.
        Years twentyYears = Years.years(20);
        PeriodType expectedType = PeriodType.years();

        // Act: Get the period type from the instance.
        PeriodType actualType = twentyYears.getPeriodType();

        // Assert: Verify that the period type is correct.
        assertEquals(expectedType, actualType);
    }
}