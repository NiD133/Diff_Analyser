package com.fasterxml.jackson.core.io;

import java.util.Random;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberOutputTest {

    // Randomization setup shared across tests
    private static final long RANDOM_SEED = 12345L;
    private static final int INT_RANDOM_SAMPLES = 251_000;
    private static final int LONG_RANDOM_SAMPLES = 678_000;

    // Buffer sizes large enough for any int/long textual representation
    private static final int INT_BUFFER_SIZE = 12;
    private static final int LONG_BUFFER_SIZE = 22;

    @Test
    @DisplayName("outputInt/toString produce the same result as String.valueOf for a variety of ints")
    void shouldFormatIntCorrectly() {
        // Hand-picked edge/typical values
        assertIntFormattingConsistency(0);
        assertIntFormattingConsistency(-3);
        assertIntFormattingConsistency(1234);
        assertIntFormattingConsistency(-1234);
        assertIntFormattingConsistency(56789);
        assertIntFormattingConsistency(-56789);
        assertIntFormattingConsistency(999_999);
        assertIntFormattingConsistency(-999_999);
        assertIntFormattingConsistency(1_000_000);
        assertIntFormattingConsistency(-1_000_000);
        assertIntFormattingConsistency(10_000_001);
        assertIntFormattingConsistency(-10_000_001);
        assertIntFormattingConsistency(100_000_012);
        assertIntFormattingConsistency(-100_000_012);
        assertIntFormattingConsistency(1_999_888_777);
        assertIntFormattingConsistency(-1_999_888_777);
        assertIntFormattingConsistency(Integer.MAX_VALUE);
        assertIntFormattingConsistency(Integer.MIN_VALUE);

        // Random sampling
        Random rnd = new Random(RANDOM_SEED);
        for (int i = 0; i < INT_RANDOM_SAMPLES; ++i) {
            assertIntFormattingConsistency(rnd.nextInt());
        }
    }

    @Test
    @DisplayName("outputLong/toString produce the same result as String.valueOf for a variety of longs")
    void shouldFormatLongCorrectly() {
        // Edge cases
        assertLongFormattingConsistency(0L, 0);
        assertLongFormattingConsistency(1L, 0);
        assertLongFormattingConsistency(-1L, 0);
        assertLongFormattingConsistency(Long.MAX_VALUE, 0);
        assertLongFormattingConsistency(Long.MIN_VALUE, 0);
        assertLongFormattingConsistency(Long.MAX_VALUE - 1L, 0);
        assertLongFormattingConsistency(Long.MIN_VALUE + 1L, 0);

        // Random sampling over a larger space
        Random rnd = new Random(RANDOM_SEED);
        for (int i = 0; i < LONG_RANDOM_SAMPLES; ++i) {
            long value = ((long) rnd.nextInt() << 32) | rnd.nextInt();
            assertLongFormattingConsistency(value, i);
        }
    }

    // --- Tests for divBy1000 --------------------------------------------------

    @Test
    @DisplayName("divBy1000 matches integer division for small numbers [0, 999_999]")
    void divBy1000Small() {
        for (int number = 0; number <= 999_999; ++number) {
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);
            assertEquals(expected, actual, () -> "With " + number + " expected " + expected + ", got " + actual);
        }
    }

    @Test
    @DisplayName("divBy1000 matches integer division for a sampled range from 1_000_000 to Integer.MAX_VALUE, step 7")
    void divBy1000Sampled() {
        // Use long for loop variable to avoid relying on int overflow to terminate the loop
        for (long n = 1_000_000L; n <= Integer.MAX_VALUE; n += 7L) {
            int number = (int) n;
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);
            assertEquals(expected, actual, () -> "With " + number + " expected " + expected + ", got " + actual);
        }
    }

    // For manual full-range verification; disabled for CI to keep suite fast and stable.
    @Test
    @Disabled("Manual test for exhaustive verification; not intended for CI")
    @DisplayName("divBy1000 matches integer division for the full non-negative int range")
    void divBy1000FullRange() {
        for (int number = 0; number >= 0; ++number) {
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);
            assertEquals(expected, actual, () -> "With " + number + " expected " + expected + ", got " + actual);
        }
    }

    // --- Assertion helpers ----------------------------------------------------

    private void assertIntFormattingConsistency(int value) {
        String expected = String.valueOf(value);

        String viaBuffer = printToString(value);
        assertEquals(expected, viaBuffer,
                () -> "Mismatch for outputInt(value=" + value + "): expected '" + expected + "' (len " + expected.length()
                        + "), got '" + viaBuffer + "' (len " + viaBuffer.length() + ")");

        String viaConvenience = NumberOutput.toString(value);
        assertEquals(expected, viaConvenience,
                () -> "Mismatch for toString(int) with value=" + value + ": expected '" + expected + "', got '" + viaConvenience + "'");
    }

    private void assertLongFormattingConsistency(long value, int index) {
        String expected = String.valueOf(value);

        String viaBuffer = printToString(value);
        assertEquals(expected, viaBuffer,
                () -> "Mismatch for outputLong(value=" + value + ", index " + index + "): expected '"
                        + expected + "' (len " + expected.length()
                        + "), got '" + viaBuffer + "' (len " + viaBuffer.length() + ")");

        String viaConvenience = NumberOutput.toString(value);
        assertEquals(expected, viaConvenience,
                () -> "Mismatch for toString(long) with value=" + value + " (index " + index + "): expected '"
                        + expected + "', got '" + viaConvenience + "'");
    }

    // --- Low-level formatting helpers ----------------------------------------

    private String printToString(int value) {
        char[] buffer = new char[INT_BUFFER_SIZE];
        int end = NumberOutput.outputInt(value, buffer, 0);
        return new String(buffer, 0, end);
        }

    private String printToString(long value) {
        char[] buffer = new char[LONG_BUFFER_SIZE];
        int end = NumberOutput.outputLong(value, buffer, 0);
        return new String(buffer, 0, end);
    }
}