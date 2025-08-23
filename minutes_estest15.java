package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A more understandable test suite for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that a sequence of arithmetic operations (division by a negative number
     * followed by addition) produces the correct, predictable result.
     */
    @Test
    public void dividedByNegativeNumberThenAddingToItselfShouldResultInDoubleTheValue() {
        // Arrange: Set up the initial state and expected outcomes.
        final int divisor = -82;
        final Minutes maxMinutes = Minutes.MAX_VALUE;

        // Calculate expected results dynamically to avoid magic numbers and make the test self-documenting.
        final int expectedDivisionResult = Integer.MAX_VALUE / divisor;
        final int expectedSumResult = expectedDivisionResult * 2;

        // Act: Perform the operations under test.
        Minutes divisionResult = maxMinutes.dividedBy(divisor);
        Minutes sumResult = divisionResult.plus(divisionResult);

        // Assert: Verify that the actual results match the expected results.
        assertEquals("The result of dividing MAX_VALUE by the divisor should be correct.",
                expectedDivisionResult, divisionResult.getMinutes());
        
        assertEquals("The sum of the division result with itself should be double the value.",
                expectedSumResult, sumResult.getMinutes());
    }
}