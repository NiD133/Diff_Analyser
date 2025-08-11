package com.fasterxml.jackson.core.io;

import java.util.Random;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class NumberOutputTest {

    /**
     * Test the conversion of integer values to their string representation.
     */
    @Test
    void testIntegerToStringConversion() throws Exception {
        int[] testValues = {
            0, -3, 1234, -1234, 56789, -56789, 999999, -999999,
            1000000, -1000000, 10000001, -10000001, -100000012, 100000012,
            1999888777, -1999888777, Integer.MAX_VALUE, Integer.MIN_VALUE
        };

        for (int value : testValues) {
            assertIntegerToStringConversion(value);
        }

        Random random = new Random(12345L);
        for (int i = 0; i < 251000; ++i) {
            assertIntegerToStringConversion(random.nextInt());
        }
    }

    /**
     * Test the conversion of long values to their string representation.
     */
    @Test
    void testLongToStringConversion() throws Exception {
        long[] edgeCases = {
            0L, 1L, -1L, Long.MAX_VALUE, Long.MIN_VALUE,
            Long.MAX_VALUE - 1L, Long.MIN_VALUE + 1L
        };

        for (long value : edgeCases) {
            assertLongToStringConversion(value, 0);
        }

        Random random = new Random(12345L);
        for (int i = 0; i < 678000; ++i) {
            long randomLong = ((long) random.nextInt() << 32) | random.nextInt();
            assertLongToStringConversion(randomLong, i);
        }
    }

    /**
     * Test the division of small integers by 1000.
     */
    @Test
    void testDivisionBy1000ForSmallNumbers() {
        for (int number = 0; number <= 999_999; ++number) {
            assertDivisionBy1000(number);
        }
    }

    /**
     * Test the division of sampled integers by 1000.
     */
    @Test
    void testDivisionBy1000ForSampledNumbers() {
        for (int number = 1_000_000; number > 0; number += 7) {
            assertDivisionBy1000(number);
        }
    }

    /**
     * Test the division of all integers by 1000.
     * This test is disabled by default due to its extensive range.
     */
    @Test
    @Disabled("Disabled for CI. Run manually for full range verification.")
    void testDivisionBy1000ForFullRange() {
        for (int number = 0; number >= 0; ++number) {
            assertDivisionBy1000(number);
        }
    }

    // Helper methods

    private void assertIntegerToStringConversion(int value) {
        String expected = Integer.toString(value);
        String actual = convertIntToString(value);

        assertEquals(expected, actual, 
            String.format("Mismatch for value %d: expected '%s', got '%s'", value, expected, actual));

        String alternative = NumberOutput.toString(value);
        assertEquals(expected, alternative, 
            String.format("Mismatch for value %d: expected '%s', got '%s'", value, expected, alternative));
    }

    private void assertLongToStringConversion(long value, int index) {
        String expected = Long.toString(value);
        String actual = convertLongToString(value);

        assertEquals(expected, actual, 
            String.format("Mismatch for value %d at index %d: expected '%s', got '%s'", value, index, expected, actual));

        String alternative = NumberOutput.toString(value);
        assertEquals(expected, alternative, 
            String.format("Mismatch for value %d at index %d: expected '%s', got '%s'", value, index, expected, alternative));
    }

    private void assertDivisionBy1000(int number) {
        int expected = number / 1000;
        int actual = NumberOutput.divBy1000(number);

        if (expected != actual) {
            fail(String.format("For number %d: expected %d, got %d", number, expected, actual));
        }
    }

    private String convertIntToString(int value) {
        char[] buffer = new char[12];
        int offset = NumberOutput.outputInt(value, buffer, 0);
        return new String(buffer, 0, offset);
    }

    private String convertLongToString(long value) {
        char[] buffer = new char[22];
        int offset = NumberOutput.outputLong(value, buffer, 0);
        return new String(buffer, 0, offset);
    }
}