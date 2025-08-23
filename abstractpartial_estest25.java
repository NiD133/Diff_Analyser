package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A test suite for the comparison logic in the {@link AbstractPartial} class.
 * This test uses {@link YearMonth} as a concrete implementation for testing.
 */
public class AbstractPartialCompareToTest {

    /**
     * Tests that compareTo() returns a negative value when the instance
     * is chronologically earlier than the partial it is compared to.
     */
    @Test
    public void compareTo_whenThisPartialIsEarlier_shouldReturnNegative() {
        // Arrange
        YearMonth laterYearMonth = new YearMonth(2005, 6); // June 2005
        YearMonth earlierYearMonth = new YearMonth(2004, 6); // June 2004

        // Act
        int result = earlierYearMonth.compareTo(laterYearMonth);

        // Assert
        // The result must be -1, indicating that 2004-06 is "less than" 2005-06.
        assertEquals(-1, result);
    }
}