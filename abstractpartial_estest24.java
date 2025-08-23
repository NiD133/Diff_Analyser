package org.joda.time.base;

import org.joda.time.MonthDay;
import org.joda.time.ReadablePartial;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link AbstractPartial#compareTo(ReadablePartial)} method.
 */
public class AbstractPartialTest {

    /**
     * Tests that compareTo() throws a ClassCastException when comparing two ReadablePartial
     * instances that have different sets of field types, as required by the method's contract.
     */
    @Test
    public void compareTo_whenPartialsHaveMismatchedFieldTypes_throwsClassCastException() {
        // Arrange: Create two ReadablePartial objects with different field types.
        // A YearMonth contains the fields [year, monthOfYear].
        // A MonthDay contains the fields [monthOfYear, dayOfMonth].
        // Although both have a size of 2, their field types at index 0 are different,
        // which should trigger the exception.
        ReadablePartial yearMonth = new YearMonth(2023, 10);
        ReadablePartial monthDay = new MonthDay(10, 27);

        // Act & Assert
        try {
            yearMonth.compareTo(monthDay);
            fail("Expected ClassCastException due to mismatched field types, but none was thrown.");
        } catch (ClassCastException e) {
            // Verify that the exception and its message are correct.
            assertEquals("ReadablePartial objects must have matching field types", e.getMessage());
        }
    }
}