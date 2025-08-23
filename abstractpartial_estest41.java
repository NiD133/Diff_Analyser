package org.joda.time.base;

import org.joda.time.MonthDay;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the comparison logic in {@link AbstractPartial}.
 */
public class AbstractPartialTest {

    /**
     * Tests that comparison methods like isBefore() throw a ClassCastException
     * when comparing two ReadablePartial objects that have different sets of field types.
     *
     * The contract of AbstractPartial requires that for a comparison to be possible,
     * both partials must have the exact same field types in the same order.
     */
    @Test
    public void isBefore_whenComparingPartialsWithMismatchedFieldTypes_throwsClassCastException() {
        // Arrange: Create two partials with incompatible field types.
        // YearMonth has fields [year, monthOfYear].
        YearMonth yearMonth = new YearMonth(2023, 6);
        // MonthDay has fields [monthOfYear, dayOfMonth].
        MonthDay monthDay = new MonthDay(6, 15);

        // Act & Assert
        try {
            monthDay.isBefore(yearMonth);
            fail("Expected a ClassCastException because the partials have different field types.");
        } catch (ClassCastException e) {
            // Verify that the exception message clearly states the reason for the failure.
            assertEquals("ReadablePartial objects must have matching field types", e.getMessage());
        }
    }
}