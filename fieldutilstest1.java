package org.joda.time.field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the safe integer addition in {@link FieldUtils}.
 * This class focuses on the {@code safeAdd(int, int)} method.
 */
@DisplayName("FieldUtils.safeAdd(int, int)")
class FieldUtilsTest {

    @Test
    @DisplayName("should return the correct sum for typical values")
    void safeAdd_shouldReturnCorrectSum_forTypicalValues() {
        assertAll(
            () -> assertEquals(0, FieldUtils.safeAdd(0, 0), "Adding zero to zero"),
            () -> assertEquals(5, FieldUtils.safeAdd(2, 3), "Adding two positive numbers"),
            () -> assertEquals(-1, FieldUtils.safeAdd(2, -3), "Adding a positive and a larger negative number"),
            () -> assertEquals(1, FieldUtils.safeAdd(-2, 3), "Adding a negative and a larger positive number"),
            () -> assertEquals(-5, FieldUtils.safeAdd(-2, -3), "Adding two negative numbers")
        );
    }

    @Test
    @DisplayName("should correctly handle additions at integer boundaries without overflowing")
    void safeAdd_shouldHandleBoundaryValues_withoutOverflow() {
        assertAll(
            () -> assertEquals(Integer.MAX_VALUE - 1, FieldUtils.safeAdd(Integer.MAX_VALUE, -1)),
            () -> assertEquals(Integer.MIN_VALUE + 1, FieldUtils.safeAdd(Integer.MIN_VALUE, 1)),
            () -> assertEquals(-1, FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MIN_VALUE))
        );
    }

    @ParameterizedTest
    @MethodSource("positiveOverflowInputs")
    @DisplayName("should throw ArithmeticException on positive overflow")
    void safeAdd_shouldThrowException_onPositiveOverflow(int a, int b, String description) {
        ArithmeticException exception = assertThrows(ArithmeticException.class,
            () -> FieldUtils.safeAdd(a, b),
            "Expected an ArithmeticException for: " + description
        );
        assertEquals("Integer.MAX_VALUE exceeded", exception.getMessage());
    }

    private static Stream<Arguments> positiveOverflowInputs() {
        return Stream.of(
            Arguments.of(Integer.MAX_VALUE, 1, "MAX_VALUE + 1"),
            Arguments.of(Integer.MAX_VALUE, 100, "MAX_VALUE + 100"),
            Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE, "MAX_VALUE + MAX_VALUE")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeOverflowInputs")
    @DisplayName("should throw ArithmeticException on negative overflow (underflow)")
    void safeAdd_shouldThrowException_onNegativeOverflow(int a, int b, String description) {
        ArithmeticException exception = assertThrows(ArithmeticException.class,
            () -> FieldUtils.safeAdd(a, b),
            "Expected an ArithmeticException for: " + description
        );
        assertEquals("Integer.MIN_VALUE exceeded", exception.getMessage());
    }

    private static Stream<Arguments> negativeOverflowInputs() {
        return Stream.of(
            Arguments.of(Integer.MIN_VALUE, -1, "MIN_VALUE - 1"),
            Arguments.of(Integer.MIN_VALUE, -100, "MIN_VALUE - 100"),
            Arguments.of(Integer.MIN_VALUE, Integer.MIN_VALUE, "MIN_VALUE + MIN_VALUE")
        );
    }
}