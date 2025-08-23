package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link AbstractPartial} class, focusing on the compareTo method.
 */
public class AbstractPartialTest {

    /**
     * Tests that comparing a ReadablePartial instance to itself returns 0,
     * which signifies equality, as per the Comparable contract.
     */
    @Test
    public void compareTo_shouldReturnZero_whenComparingAnInstanceToItself() {
        // Arrange: Create a ReadablePartial instance. YearMonth is a concrete implementation.
        YearMonth yearMonth = new YearMonth(2023, 4); // April 2023

        // Act: Compare the instance to itself.
        int comparisonResult = yearMonth.compareTo(yearMonth);

        // Assert: The result should be 0, indicating the instances are equal.
        assertEquals(0, comparisonResult);
    }
}