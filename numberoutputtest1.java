package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on integer-to-string conversions.
 */
class NumberOutputTest {

    /**
     * Verifies that both {@link NumberOutput#outputInt(int, char[], int)} and
     * {@link NumberOutput#toString(int)} produce the correct string representation for a given integer.
     *
     * @param value The integer value to test.
     */
    private void verifyIntegerConversion(int value) {
        String expected = String.valueOf(value);

        // 1. Test the direct-to-buffer method: outputInt()
        // Max length for an int is 11 characters (for Integer.MIN_VALUE), so 12 is a safe buffer size.
        char[] buffer = new char[12];
        int len = NumberOutput.outputInt(value, buffer, 0);
        String actualFromOutputInt = new String(buffer, 0, len);
        assertEquals(expected, actualFromOutputInt,
                () -> "Conversion via outputInt() should match for value " + value);

        // 2. Test the convenience method: toString()
        String actualFromToString = NumberOutput.toString(value);
        assertEquals(expected, actualFromToString,
                () -> "Conversion via toString() should match for value " + value);
    }

    @ParameterizedTest(name = "value = {0}")
    @ValueSource(ints = {
            0, -3, 1234, -1234, 56789, -56789,
            999999, -999999, 1000000, -1000000,
            10000001, -10000001, -100000012, 100000012,
            1999888777, -1999888777,
            Integer.MAX_VALUE, Integer.MIN_VALUE
    })
    void testIntConversionWithBoundaryAndCommonValues(int value) {
        verifyIntegerConversion(value);
    }

    @Test
    void testIntConversionWithRandomValues() {
        // Use a fixed seed for reproducible test runs
        Random random = new Random(12345L);
        // Test with a large set of random values to ensure broad coverage
        final int testCycles = 251000;
        for (int i = 0; i < testCycles; ++i) {
            verifyIntegerConversion(random.nextInt());
        }
    }
}