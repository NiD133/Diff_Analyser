package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the comparison methods in {@link AbstractPartial}.
 * This suite uses {@link YearMonth} as a concrete implementation of AbstractPartial
 * to verify the behavior of methods like isEqual().
 */
public class AbstractPartialComparisonTest {

    @Test
    public void isEqual_shouldReturnFalse_whenPartialsAreDifferent() {
        // Arrange
        // Use a fixed date to ensure the test is deterministic.
        YearMonth baseYearMonth = new YearMonth(2023, 2); // February 2023
        YearMonth differentYearMonth = baseYearMonth.plusMonths(1); // March 2023

        // Act
        // The isEqual method is inherited from the AbstractPartial class.
        boolean areEqual = baseYearMonth.isEqual(differentYearMonth);

        // Assert
        assertFalse("isEqual() should return false for two different partials.", areEqual);
    }

    @Test
    public void isEqual_shouldReturnTrue_whenPartialsRepresentTheSameValue() {
        // Arrange
        // Create two separate but identical YearMonth instances.
        YearMonth yearMonthOne = new YearMonth(2023, 2); // February 2023
        YearMonth yearMonthTwo = new YearMonth(2023, 2); // February 2023

        // Act
        boolean areEqual = yearMonthOne.isEqual(yearMonthTwo);

        // Assert
        assertTrue("isEqual() should return true for two identical partials.", areEqual);
    }
}