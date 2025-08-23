package org.joda.time.base;

import org.joda.time.MonthDay;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link AbstractPartial} class, focusing on comparison methods.
 */
public class AbstractPartial_ESTestTest50 extends AbstractPartial_ESTest_scaffolding {

    /**
     * Tests that isBefore() returns false when the partial instance is chronologically
     * after the partial it is being compared to.
     */
    @Test
    public void isBefore_shouldReturnFalse_whenPartialIsAfterComparisonPartial() {
        // Arrange: Create two specific, non-consecutive MonthDay instances.
        // Using explicit dates makes the test deterministic and easy to understand.
        MonthDay march15 = new MonthDay(3, 15);
        MonthDay february14 = new MonthDay(2, 14);

        // Act: Check if March 15th is before February 14th.
        boolean isBefore = march15.isBefore(february14);

        // Assert: The result should be false, as March 15th is not before February 14th.
        assertFalse("A later date (Mar 15) should not be considered 'before' an earlier date (Feb 14).", isBefore);
    }
}