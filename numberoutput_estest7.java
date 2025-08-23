package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    /**
     * Tests that {@link NumberOutput#toString(long)} correctly converts the long value
     * that corresponds to {@link Integer#MIN_VALUE}.
     *
     * This is an important edge case, as the implementation may have an optimized
     * code path for long values that fit within the integer range.
     */
    @Test
    public void shouldConvertIntegerMinValueAsLongToString() {
        // Arrange: Define the input and the expected outcome clearly.
        // Using the Integer.MIN_VALUE constant makes the test's intent explicit.
        long intMinValueAsLong = Integer.MIN_VALUE;
        String expectedResult = String.valueOf(Integer.MIN_VALUE);

        // Act: Call the method under test.
        String actualResult = NumberOutput.toString(intMinValueAsLong);

        // Assert: Verify that the actual result matches the expected result.
        assertEquals(expectedResult, actualResult);
    }
}