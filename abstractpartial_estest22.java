package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the compareTo method in the AbstractPartial class,
 * using YearMonth as a concrete implementation.
 */
public class AbstractPartialTest {

    @Test
    public void compareToShouldReturnPositiveForLaterPartial() {
        // Arrange
        // The original test used YearMonth.now(), which is non-deterministic.
        // We use a fixed date to make the test stable and easier to understand.
        // Let's assume the original test's "now" was February 2014.
        YearMonth earlierYearMonth = new YearMonth(2014, 2);

        // The original test calculated a future date by subtracting a negative number,
        // which is confusing. Using plusYears is much clearer.
        // earlierYearMonth.minusYears(-2306) is equivalent to plusYears(2306).
        YearMonth laterYearMonth = earlierYearMonth.plusYears(2306);

        // Act
        // Compare the later partial (future) to the earlier one (past).
        // The compareTo method is defined on the ReadablePartial interface.
        int comparisonResult = laterYearMonth.compareTo(earlierYearMonth);

        // Assert
        // A positive value (1) indicates that laterYearMonth is after earlierYearMonth.
        assertEquals("The year of the later partial should be correctly calculated", 4320, laterYearMonth.getYear());
        assertEquals("Comparing a later partial to an earlier one should return 1", 1, comparisonResult);
    }
}