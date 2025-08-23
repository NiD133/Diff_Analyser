package org.joda.time.base;

import org.joda.time.LocalTime;
import org.joda.time.ReadablePartial;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Unit tests for the abstract comparison logic in {@link AbstractPartial}.
 */
public class AbstractPartialComparisonTest {

    @Test
    public void isEqual_shouldThrowClassCastException_whenComparingPartialsWithMismatchedFieldTypes() {
        // Arrange: Create two ReadablePartial instances with completely different field types.
        // A YearMonth contains {year, monthOfYear}, while a LocalTime contains {hour, minute, ...}.
        // The isEqual method requires that the partials have the same set of field types.
        ReadablePartial yearMonth = new YearMonth(2023, 6);
        ReadablePartial localTime = new LocalTime(10, 20, 30);

        // Act & Assert
        try {
            yearMonth.isEqual(localTime);
            fail("Expected a ClassCastException because the partials have different field types.");
        } catch (ClassCastException expected) {
            // This is the expected behavior. The test passes.
            // The contract of isEqual states that a ClassCastException is thrown
            // when the field types of the two partials do not match.
        }
    }
}