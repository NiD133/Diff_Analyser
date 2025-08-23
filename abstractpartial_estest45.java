package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for the equals method in {@link AbstractPartial}.
 * This test uses {@link YearMonth} as a concrete implementation of AbstractPartial.
 */
// Note: The original test class name "AbstractPartial_ESTestTest45" was renamed for clarity.
public class AbstractPartialTest {

    @Test
    public void equals_shouldReturnFalse_whenComparingPartialsWithDifferentValues() {
        // Arrange: Create two YearMonth instances with different year values.
        // Using a fixed date ensures the test is deterministic and not dependent on when it is run.
        YearMonth yearMonth2023 = new YearMonth(2023, 2); // February 2023
        YearMonth yearMonth2024 = yearMonth2023.plusYears(1); // February 2024

        // Sanity check to ensure the test setup is correct.
        assertEquals(2024, yearMonth2024.getYear());
        assertEquals(2, yearMonth2024.getMonthOfYear());

        // Act & Assert: The equals method, inherited from AbstractPartial, should
        // return false when comparing two partials with different values.
        // We use assertNotEquals for a clear and concise comparison.
        assertNotEquals(yearMonth2023, yearMonth2024);
    }
}