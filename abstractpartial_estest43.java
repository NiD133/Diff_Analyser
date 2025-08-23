package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Test suite for the comparison logic in the {@link AbstractPartial} class.
 */
public class AbstractPartialTest {

    /**
     * Verifies that isBefore() returns false when a partial instant is compared to itself.
     * A partial instant should never be considered strictly before an identical one.
     */
    @Test
    public void isBefore_shouldReturnFalse_whenComparingPartialToItself() {
        // Arrange: Create an instance of YearMonth, which is a concrete implementation
        // of AbstractPartial. The values (year 4, month 4) are from the original test.
        YearMonth yearMonth = new YearMonth(4, 4);

        // Act: Compare the YearMonth instance with itself.
        boolean isBefore = yearMonth.isBefore(yearMonth);

        // Assert: The result should be false, as an object is not before itself.
        assertFalse("A partial instance should not be considered 'before' itself.", isBefore);
    }
}