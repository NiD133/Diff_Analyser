package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the utility methods in the {@link NumberOutput} class.
 * This focuses on the {@code divBy1000} method.
 */
public class NumberOutputTest {

    /**
     * Tests that the optimized `divBy1000` method correctly calculates the quotient
     * for a negative input, matching the behavior of standard integer division.
     */
    @Test
    public void divBy1000_withNegativeInput_returnsCorrectQuotient() {
        // Arrange
        int inputNumber = -1652;
        // The method under test is an optimization, so its result must match
        // standard integer division, which truncates toward zero.
        int expectedQuotient = -1652 / 1000; // This evaluates to -1

        // Act
        int actualQuotient = NumberOutput.divBy1000(inputNumber);

        // Assert
        assertEquals("The optimized division result should match standard integer division",
                expectedQuotient, actualQuotient);
    }
}