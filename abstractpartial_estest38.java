package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the comparison logic in {@link AbstractPartial}.
 * This class focuses on verifying the behavior of methods like isAfter().
 */
public class AbstractPartial_ESTestTest38 {

    /**
     * Tests that isAfter() throws an IllegalArgumentException when its argument is null.
     * The method contract explicitly forbids comparison with a null partial.
     */
    @Test
    public void isAfter_shouldThrowIllegalArgumentException_whenComparingWithNull() {
        // Arrange: Create an instance of a class that extends AbstractPartial.
        YearMonth yearMonth = YearMonth.now();
        String expectedErrorMessage = "Partial cannot be null";

        // Act & Assert: Attempting to call isAfter(null) should throw an exception.
        try {
            yearMonth.isAfter(null);
            fail("Expected an IllegalArgumentException to be thrown, but no exception was caught.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}