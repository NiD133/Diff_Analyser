package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the number-to-string conversion utility class {@link NumberOutput}.
 * This suite focuses on testing specific utility methods within the class.
 */
public class NumberOutputTest {

    /**
     * Tests the {@link NumberOutput#divBy1000(int)} method with a wide range of
     * positive integer inputs to ensure its correctness against standard integer division.
     */
    @Test
    void divBy1000_withSampledPositiveIntegers_returnsCorrectQuotient() {
        // This loop samples a wide range of positive integers.
        // It starts at 1,000,000 and increments by a prime number (7)
        // until integer overflow occurs, at which point `number` becomes negative
        // and the loop terminates. This effectively tests values up to Integer.MAX_VALUE.
        for (int number = 1_000_000; number > 0; number += 7) {
            int expectedQuotient = number / 1000;
            int actualQuotient = NumberOutput.divBy1000(number);

            assertEquals(expectedQuotient, actualQuotient,
                    () -> "Division by 1000 failed for number: " + number);
        }
    }
}