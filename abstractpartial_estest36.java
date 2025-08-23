package org.joda.time.base;

import org.joda.time.MonthDay;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link AbstractPartial} class.
 */
public class AbstractPartialTest {

    /**
     * Verifies that the isBefore() method throws an IllegalArgumentException
     * when a null partial is passed as an argument, as per the method's contract.
     */
    @Test
    public void isBefore_shouldThrowIllegalArgumentException_whenComparingWithNull() {
        // Arrange: Create an instance of a concrete AbstractPartial implementation.
        MonthDay monthDay = new MonthDay();

        // Act & Assert: Attempt the comparison and verify the exception.
        try {
            monthDay.isBefore(null);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is clear and correct.
            assertEquals("Partial cannot be null", e.getMessage());
        }
    }
}