package com.fasterxml.jackson.core.io;

import java.util.Random;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test suite for NumberOutput class, which provides efficient number-to-string conversion methods.
 * Tests cover integer printing, long printing, and optimized division operations.
 */
class NumberOutputTest {
    
    // Test constants
    private static final long RANDOM_SEED = 12345L;
    private static final int INT_RANDOM_ITERATIONS = 251_000;
    private static final int LONG_RANDOM_ITERATIONS = 678_000;
    private static final int DIV_BY_1000_MAX_SMALL = 999_999;
    private static final int DIV_BY_1000_SAMPLING_STEP = 7;
    
    // Buffer sizes for number conversion
    private static final int INT_BUFFER_SIZE = 12;  // Enough for Integer.MIN_VALUE
    private static final int LONG_BUFFER_SIZE = 22; // Enough for Long.MIN_VALUE

    @Test
    void shouldConvertIntegersToStringCorrectly() throws Exception {
        // Test edge cases and boundary values
        verifyIntegerConversion(0);
        verifyIntegerConversion(-3);
        verifyIntegerConversion(1234);
        verifyIntegerConversion(-1234);
        verifyIntegerConversion(56789);
        verifyIntegerConversion(-56789);
        verifyIntegerConversion(999999);
        verifyIntegerConversion(-999999);
        verifyIntegerConversion(1000000);
        verifyIntegerConversion(-1000000);
        verifyIntegerConversion(10000001);
        verifyIntegerConversion(-10000001);
        verifyIntegerConversion(-100000012);
        verifyIntegerConversion(100000012);
        verifyIntegerConversion(1999888777);
        verifyIntegerConversion(-1999888777);
        verifyIntegerConversion(Integer.MAX_VALUE);
        verifyIntegerConversion(Integer.MIN_VALUE);

        // Test with random values to ensure robustness
        Random random = new Random(RANDOM_SEED);
        for (int i = 0; i < INT_RANDOM_ITERATIONS; i++) {
            verifyIntegerConversion(random.nextInt());
        }
    }

    @Test
    void shouldConvertLongsToStringCorrectly() throws Exception {
        // Test edge cases and boundary values
        verifyLongConversion(0L, "zero");
        verifyLongConversion(1L, "positive one");
        verifyLongConversion(-1L, "negative one");
        verifyLongConversion(Long.MAX_VALUE, "Long.MAX_VALUE");
        verifyLongConversion(Long.MIN_VALUE, "Long.MIN_VALUE");
        verifyLongConversion(Long.MAX_VALUE - 1L, "Long.MAX_VALUE - 1");
        verifyLongConversion(Long.MIN_VALUE + 1L, "Long.MIN_VALUE + 1");

        // Test with random values across the full long range
        Random random = new Random(RANDOM_SEED);
        for (int i = 0; i < LONG_RANDOM_ITERATIONS; i++) {
            long randomLong = generateRandomLong(random);
            verifyLongConversion(randomLong, "random iteration " + i);
        }
    }

    @Test
    void shouldDivideSmallNumbersBy1000Correctly() {
        // Test all numbers from 0 to 999,999 to ensure divBy1000 optimization works correctly
        for (int number = 0; number <= DIV_BY_1000_MAX_SMALL; number++) {
            verifyDivisionBy1000(number);
        }
    }

    @Test
    void shouldDivideSampledLargeNumbersBy1000Correctly() {
        // Test sampled large numbers to verify divBy1000 works across larger range
        // Use step size to avoid testing every single number (performance consideration)
        for (int number = 1_000_000; number > 0; number += DIV_BY_1000_SAMPLING_STEP) {
            verifyDivisionBy1000(number);
        }
    }

    @Test
    @Disabled("Manual test only - tests full integer range, takes very long to run")
    void shouldDivideFullRangeBy1000Correctly() {
        // Comprehensive test for the full integer range
        // Disabled by default as it's very time-consuming and should only be run manually
        for (int number = 0; number >= 0; number++) { // Loop until overflow
            verifyDivisionBy1000(number);
        }
    }

    // Helper methods for better test organization and readability

    /**
     * Verifies that NumberOutput correctly converts an integer to its string representation
     * using both outputInt() and toString() methods.
     */
    private void verifyIntegerConversion(int value) {
        String expectedString = String.valueOf(value);
        
        // Test outputInt method
        String actualFromOutputInt = convertIntToStringUsingBuffer(value);
        if (!expectedString.equals(actualFromOutputInt)) {
            assertEquals(expectedString, actualFromOutputInt, 
                createIntConversionErrorMessage(expectedString, actualFromOutputInt));
        }
        
        // Test toString method
        String actualFromToString = NumberOutput.toString(value);
        if (!expectedString.equals(actualFromToString)) {
            assertEquals(expectedString, actualFromToString, 
                createIntConversionErrorMessage(expectedString, actualFromToString));
        }
    }

    /**
     * Verifies that NumberOutput correctly converts a long to its string representation
     * using both outputLong() and toString() methods.
     */
    private void verifyLongConversion(long value, String testCaseDescription) {
        String expectedString = String.valueOf(value);
        
        // Test outputLong method
        String actualFromOutputLong = convertLongToStringUsingBuffer(value);
        if (!expectedString.equals(actualFromOutputLong)) {
            assertEquals(expectedString, actualFromOutputLong, 
                createLongConversionErrorMessage(expectedString, actualFromOutputLong, testCaseDescription));
        }
        
        // Test toString method
        String actualFromToString = NumberOutput.toString(value);
        if (!expectedString.equals(actualFromToString)) {
            assertEquals(expectedString, actualFromToString, 
                createLongConversionErrorMessage(expectedString, actualFromToString, testCaseDescription));
        }
    }

    /**
     * Verifies that the optimized divBy1000 method produces the same result as standard division.
     */
    private void verifyDivisionBy1000(int number) {
        int expectedResult = number / 1000;
        int actualResult = NumberOutput.divBy1000(number);
        
        if (expectedResult != actualResult) {
            fail(String.format("divBy1000(%d) should return %d but returned %d", 
                number, expectedResult, actualResult));
        }
    }

    /**
     * Converts an integer to string using NumberOutput.outputInt() and a character buffer.
     */
    private String convertIntToStringUsingBuffer(int value) {
        char[] buffer = new char[INT_BUFFER_SIZE];
        int endOffset = NumberOutput.outputInt(value, buffer, 0);
        return new String(buffer, 0, endOffset);
    }

    /**
     * Converts a long to string using NumberOutput.outputLong() and a character buffer.
     */
    private String convertLongToStringUsingBuffer(long value) {
        char[] buffer = new char[LONG_BUFFER_SIZE];
        int endOffset = NumberOutput.outputLong(value, buffer, 0);
        return new String(buffer, 0, endOffset);
    }

    /**
     * Generates a random long value using two random integers to cover the full long range.
     */
    private long generateRandomLong(Random random) {
        return ((long) random.nextInt() << 32) | random.nextInt();
    }

    /**
     * Creates a detailed error message for integer conversion failures.
     */
    private String createIntConversionErrorMessage(String expected, String actual) {
        return String.format("Integer conversion failed - expected: '%s' (length: %d), actual: '%s' (length: %d)", 
            expected, expected.length(), actual, actual.length());
    }

    /**
     * Creates a detailed error message for long conversion failures.
     */
    private String createLongConversionErrorMessage(String expected, String actual, String testCase) {
        return String.format("Long conversion failed for %s - expected: '%s' (length: %d), actual: '%s' (length: %d)", 
            testCase, expected, expected.length(), actual, actual.length());
    }
}