package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Years} class, focusing on its interaction with other Joda-Time types.
 */
public class YearsTest {

    /**
     * Tests that adding a {@link Years} object to a {@link LocalDate} correctly
     * advances the date by the specified number of years.
     */
    @Test
    public void testAddingYearsToLocalDate() {
        // Arrange: Define a period of 3 years and a starting date.
        final Years threeYears = Years.years(3);
        final LocalDate startDate = new LocalDate(2006, 6, 1);
        final LocalDate expectedDate = new LocalDate(2009, 6, 1);

        // Act: Add the years to the starting date.
        final LocalDate resultDate = startDate.plus(threeYears);

        // Assert: The resulting date should be exactly 3 years after the start date.
        assertEquals(expectedDate, resultDate);
    }
}