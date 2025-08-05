package com.fasterxml.jackson.core.io;

import java.util.Random;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberOutputTest {
    // Predefined edge cases for integer printing tests
    private static final int[] INT_EDGE_CASES = {
        0, -3, 1234, -1234, 56789, -56789,
        999999, -999999, 1000000, -1000000,
        10000001, -10000001, -100000012, 100000012,
        1999888777, -1999888777, Integer.MAX_VALUE, Integer.MIN_VALUE
    };

    // Predefined edge cases for long printing tests
    private static final long[] LONG_EDGE_CASES = {
        0L, 1L, -1L, Long.MAX_VALUE, Long.MIN_VALUE,
        Long.MAX_VALUE - 1L, Long.MIN_VALUE + 1L
    };

    @Test
    void intPrinting() throws Exception {
        // Test predefined edge cases
        for (int value : INT_EDGE_CASES) {
            assertIntPrint(value);
        }

        // Test random integers to ensure broad coverage
        Random rnd = new Random(12345L);
        for (int i = 0; i < 251000; i++) {
            assertIntPrint(rnd.nextInt());
        }
    }

    @Test
    void longPrinting() throws Exception {
        // Test predefined edge cases
        for (long value : LONG_EDGE_CASES) {
            assertLongPrint(value, -1); // -1 indicates edge case
        }

        // Test random longs for comprehensive coverage
        Random rnd = new Random(12345L);
        for (int i = 0; i < 678000; i++) {
            long value = ((long) rnd.nextInt() << 32) | rnd.nextInt();
            assertLongPrint(value, i);
        }
    }

    @Test
    void divBy1000WithSmallNumbers() {
        // Verify division for all numbers in [0, 999_999]
        for (int number = 0; number <= 999_999; number++) {
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);
            assertEquals(expected, actual, () -> 
                "Failed for " + number + ": expected " + expected + ", got " + actual
            );
        }
    }

    @Test
    void divBy1000WithSampledLargeNumbers() {
        // Verify division for sampled large numbers
        for (int number = 1_000_000; number > 0; number += 7) {
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);
            assertEquals(expected, actual, () -> 
                "Failed for " + number + ": expected " + expected + ", got " + actual
            );
        }
    }

    @Test
    @Disabled("For manual testing only - verifies full integer range")
    void divBy1000FullRange() {
        // Full range verification (disabled in CI)
        for (int number = 0; number >= 0; number++) {
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);
            assertEquals(expected, actual, () -> 
                "Failed for " + number + ": expected " + expected + ", got " + actual
            );
        }
    }

    /*
    /**********************************************************
    /* Helper assertion methods
    /**********************************************************
     */

    private void assertIntPrint(int value) {
        String expectedString = String.valueOf(value);
        
        // Verify outputInt() method
        String actualOutputInt = printIntToString(value);
        assertEquals(expectedString, actualOutputInt,
            "outputInt() conversion mismatch for: " + value
        );
        
        // Verify toString() method
        String actualToString = NumberOutput.toString(value);
        assertEquals(expectedString, actualToString,
            "toString() conversion mismatch for: " + value
        );
    }

    private void assertLongPrint(long value, int index) {
        String expectedString = String.valueOf(value);
        
        // Verify outputLong() method
        String actualOutputLong = printLongToString(value);
        assertEquals(expectedString, actualOutputLong, () ->
            "outputLong() conversion mismatch at index " + index + " for: " + value
        );
        
        // Verify toString() method
        String actualToString = NumberOutput.toString(value);
        assertEquals(expectedString, actualToString, () ->
            "toString() conversion mismatch at index " + index + " for: " + value
        );
    }

    /*
    /**********************************************************
    /* Internal conversion helpers
    /**********************************************************
     */

    private String printIntToString(int value) {
        char[] buffer = new char[12];
        int len = NumberOutput.outputInt(value, buffer, 0);
        return new String(buffer, 0, len);
    }

    private String printLongToString(long value) {
        char[] buffer = new char[22];
        int len = NumberOutput.outputLong(value, buffer, 0);
        return new String(buffer, 0, len);
    }
}