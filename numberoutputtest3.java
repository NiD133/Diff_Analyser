package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the optimized integer division method {@link NumberOutput#divBy1000(int)}.
 * This class focuses on verifying the correctness of this specific optimization.
 */
class NumberOutputDivBy1000Test {

    /**
     * Exhaustively tests that {@link NumberOutput#divBy1000(int)} produces the same result
     * as standard Java integer division for all integers from 0 to 999,999.
     * This ensures the optimization is correct for a common range of small, positive values.
     */
    @Test
    void divBy1000_shouldMatchStandardDivisionForSmallPositiveIntegers() {
        for (int number = 0; number <= 999_999; ++number) {
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);

            // Using a lambda for the message improves performance, as the string is only
            // constructed if the assertion fails.
            assertEquals(expected, actual,
                    () -> String.format("divBy1000(%d) - Expected: %d, Actual: %d",
                            number, expected, actual));
        }
    }
}