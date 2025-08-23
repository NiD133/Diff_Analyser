package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void getYears_shouldReturnTheCorrectNumberOfYears() {
        // Arrange: Create a Years instance representing 20 years.
        Years twentyYears = Years.years(20);

        // Act: Retrieve the number of years from the instance.
        int actualYears = twentyYears.getYears();

        // Assert: The retrieved value should match the initial value.
        assertEquals(20, actualYears);
    }
}