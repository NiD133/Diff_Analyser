package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains tests for the integer division methods in {@link NumberOutput}.
 * This suite includes both fast, sample-based tests for regular execution and
 * a slow, comprehensive test for manual full-range verification.
 */
@DisplayName("NumberOutput Division Tests")
public class NumberOutputDivisionTest {

    /**
     * Tests the optimized divBy1000() method against standard integer division
     * using a set of representative values. This test runs quickly and is suitable
     * for inclusion in an automated CI/CD pipeline.
     */
    @ParameterizedTest(name = "divBy1000({0}) should be {1}")
    @CsvSource({
            "0, 0",
            "999, 0",
            "1000, 1",
            "12345, 12",
            "2147483647, 2147483" // Integer.MAX_VALUE
    })
    @DisplayName("Quick check of divBy1000() with sample values")
    void divBy1000_withSampleValues(int number, int expectedQuotient) {
        // Act
        int actualQuotient = NumberOutput.divBy1000(number);

        // Assert
        assertEquals(expectedQuotient, actualQuotient);
    }

    /**
     * A comprehensive but very slow test that verifies {@link NumberOutput#divBy1000(int)}
     * for all positive integer values.
     * <p>
     * This test is disabled by default because it can take several minutes to run.
     * It should be run manually when changes are made to the underlying division logic
     * to ensure correctness across the entire positive integer range.
     * <p>
     * To run this test, comment out or remove the {@code @Disabled} annotation.
     */
    @Test
    @Disabled("This test is too slow for regular execution. Run manually for full verification.")
    @DisplayName("Full range verification of divBy1000()")
    void divBy1000_fullRangeVerification() {
        // This loop iterates through all positive integers from 0 to Integer.MAX_VALUE.
        // It cleverly uses integer overflow to terminate: when 'number' increments
        // past MAX_VALUE, it wraps around to MIN_VALUE (a negative number),
        // causing the loop condition (number >= 0) to become false.
        for (int number = 0; number >= 0; ++number) {
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);

            // Use assertEquals with a lambda for the failure message.
            // The message is only constructed if the assertion fails, which is efficient.
            assertEquals(expected, actual,
                    () -> "divBy1000(" + number + ") failed. Expected: " + expected + ", but got: " + actual);
        }
    }
}