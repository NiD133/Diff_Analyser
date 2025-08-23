package org.joda.time.base;

import org.joda.time.DateTimeField;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link AbstractPartial} class, tested via its concrete subclass {@link YearMonth}.
 */
public class AbstractPartialTest {

    /**
     * Verifies that getFields() on a YearMonth instance returns an array
     * containing exactly two fields, corresponding to 'year' and 'month'.
     */
    @Test
    public void getFields_onYearMonth_returnsArrayWithTwoFields() {
        // Arrange: A YearMonth is a partial date consisting of a year and a month.
        YearMonth yearMonth = new YearMonth();

        // Act: Call the getFields() method, which is implemented in the AbstractPartial hierarchy.
        DateTimeField[] fields = yearMonth.getFields();

        // Assert: We expect two fields for a YearMonth instance.
        final int expectedFieldCount = 2;
        assertEquals("A YearMonth should consist of exactly two fields (year and month).",
                expectedFieldCount, fields.length);
    }
}