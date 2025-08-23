package org.joda.time.base;

import org.joda.time.MonthDay;
import org.joda.time.Partial;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the comparison logic in {@link AbstractPartial}.
 */
public class AbstractPartialTest {

    @Test
    public void compareTo_shouldReturnZero_whenPartialsAreEquivalent() {
        // Arrange: Create two different but equivalent ReadablePartial instances.
        // Both represent the same partial date: February 14th.
        MonthDay originalMonthDay = new MonthDay(2, 14);
        Partial partialFromMonthDay = new Partial(originalMonthDay);

        // Act: Compare the Partial to the MonthDay it was created from.
        // This tests the AbstractPartial's ability to compare itself against
        // another ReadablePartial implementation.
        int comparisonResult = partialFromMonthDay.compareTo(originalMonthDay);

        // Assert: The result should be 0, indicating that the partials are equal.
        assertEquals("Comparing two equivalent partials should result in 0.", 0, comparisonResult);
    }
}