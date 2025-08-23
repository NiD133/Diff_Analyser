package org.threeten.extra;

import org.junit.Test;

import java.time.temporal.TemporalField;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class contains tests for the {@link DayOfMonth#range(TemporalField)} method.
 * Note: The original class name and structure from the EvoSuite-generated test
 * have been preserved to facilitate integration.
 */
public class DayOfMonth_ESTestTest25 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that calling range() with a null argument throws a NullPointerException.
     * This verifies the method's input validation.
     */
    @Test
    public void testRangeWithNullFieldThrowsNullPointerException() {
        // Arrange: Create a DayOfMonth instance. The specific value is not important for this test,
        // but using a fixed value is better than DayOfMonth.now() for test determinism.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act & Assert: Call the method with a null argument and expect an exception.
        try {
            dayOfMonth.range((TemporalField) null);
            fail("Expected NullPointerException was not thrown.");
        } catch (NullPointerException e) {
            // Verify that the exception is the one we expect, not an accidental NPE from elsewhere.
            // The message "field" is expected from a standard Objects.requireNonNull(field, "field") check.
            assertEquals("field", e.getMessage());
        }
    }
}