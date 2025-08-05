package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.LongStream;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on the correctness
 * of integer and long to string conversion methods.
 */
class NumberOutputTest {

    //<editor-fold desc="Tests for int to String conversion">

    private static IntStream intBoundaryValues() {
        return IntStream.of(
                0, -3, 1234, -1234, 56789, -56789,
                999999, -999999, 1000000, -1000000,
                10000001, -10000001, 100000012, -100000012,
                1999888777, -1999888777,
                Integer.MAX_VALUE, Integer.MIN_VALUE
        );
    }

    @ParameterizedTest
    @MethodSource("intBoundaryValues")
    @DisplayName("outputInt() should correctly format boundary and special integer values")
    void outputIntShouldCorrectlyFormatBoundaryValues(int value) {
        assertIntConversion(value);
    }

    @Test
    @DisplayName("outputInt() should correctly format a large set of random integer values")
    void outputIntShouldCorrectlyFormatRandomValues() {
        Random rnd = new Random(12345L);
        for (int i = 0; i < 251000; ++i) {
            assertIntConversion(rnd.nextInt());
        }
    }

    //</editor-fold>

    //<editor-fold desc="Tests for long to String conversion">

    private static LongStream longBoundaryValues() {
        return LongStream.of(
                0L, 1L, -1L,
                Long.MAX_VALUE, Long.MIN_VALUE,
                Long.MAX_VALUE - 1L, Long.MIN_VALUE + 1L
        );
    }

    @ParameterizedTest
    @MethodSource("longBoundaryValues")
    @DisplayName("outputLong() should correctly format boundary and special long values")
    void outputLongShouldCorrectlyFormatBoundaryValues(long value) {
        assertLongConversion(value);
    }

    @Test
    @DisplayName("outputLong() should correctly format a large set of random long values")
    void outputLongShouldCorrectlyFormatRandomValues() {
        Random rnd = new Random(12345L);
        // Bigger value space, need more iterations for long
        for (int i = 0; i < 678000; ++i) {
            long l = ((long) rnd.nextInt() << 32) | rnd.nextInt();
            assertLongConversion(l);
        }
    }

    //</editor-fold>

    //<editor-fold desc="Tests for divBy1000">

    @Test
    @DisplayName("divBy1000() should be correct for small positive integers (0 to 999,999)")
    void divBy1000ShouldBeCorrectForSmallPositiveIntegers() {
        for (int number = 0; number <= 999_999; ++number) {
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);
            assertEquals(expected, actual, () -> "Failed for number: " + number);
        }
    }

    @Test
    @DisplayName("divBy1000() should be correct for a sample of large positive integers")
    void divBy1000ShouldBeCorrectForSampledLargePositiveIntegers() {
        // Check a sampling of numbers up to Integer.MAX_VALUE
        for (int number = 1_000_000; number > 0; number += 7) {
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);
            assertEquals(expected, actual, () -> "Failed for number: " + number);
        }
    }

    @Test
    @Disabled("Manual, long-running test for full positive integer range verification")
    @DisplayName("divBy1000() should be correct for the full positive integer range (manual test)")
    void divBy1000ShouldBeCorrectForFullPositiveIntegerRange() {
        // This loop iterates through all positive integers by relying on integer overflow.
        // When `number` increments past Integer.MAX_VALUE, it wraps around to a negative value,
        // causing the loop condition `number >= 0` to fail.
        for (int number = 0; number >= 0; ++number) {
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);
            assertEquals(expected, actual, () -> "Failed for number: " + number);
        }
    }

    //</editor-fold>

    //<editor-fold desc="Assertion Helpers">

    /**
     * Asserts that both {@link NumberOutput#outputInt} and {@link NumberOutput#toString(int)}
     * produce the correct string representation for a given integer value.
     */
    private void assertIntConversion(int value) {
        String expected = String.valueOf(value);

        // Test NumberOutput.outputInt(int, char[], int)
        String actualFromOutputInt = convertUsingOutputInt(value);
        assertEquals(expected, actualFromOutputInt,
                () -> "outputInt(int) failed for value: " + value);

        // Test NumberOutput.toString(int)
        String actualFromToString = NumberOutput.toString(value);
        assertEquals(expected, actualFromToString,
                () -> "toString(int) failed for value: " + value);
    }

    /**
     * Asserts that both {@link NumberOutput#outputLong} and {@link NumberOutput#toString(long)}
     * produce the correct string representation for a given long value.
     */
    private void assertLongConversion(long value) {
        String expected = String.valueOf(value);

        // Test NumberOutput.outputLong(long, char[], int)
        String actualFromOutputLong = convertUsingOutputLong(value);
        assertEquals(expected, actualFromOutputLong,
                () -> "outputLong(long) failed for value: " + value);

        // Test NumberOutput.toString(long)
        String actualFromToString = NumberOutput.toString(value);
        assertEquals(expected, actualFromToString,
                () -> "toString(long) failed for value: " + value);
    }

    private String convertUsingOutputInt(int value) {
        char[] buffer = new char[12]; // Max int length is 11 chars ("-2147483648")
        int offset = NumberOutput.outputInt(value, buffer, 0);
        return new String(buffer, 0, offset);
    }

    private String convertUsingOutputLong(long value) {
        char[] buffer = new char[22]; // Max long length is 20 chars ("-9223372036854775808")
        int offset = NumberOutput.outputLong(value, buffer, 0);
        return new String(buffer, 0, offset);
    }

    //</editor-fold>
}