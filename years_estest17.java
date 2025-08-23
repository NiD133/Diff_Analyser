package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void getYears_shouldReturnTheValuePassedToFactory() {
        // Arrange
        final int expectedYears = -690;
        Years years = Years.years(expectedYears);

        // Act
        int actualYears = years.getYears();

        // Assert
        assertEquals("The number of years should match the value used for creation.",
                expectedYears, actualYears);
    }
}