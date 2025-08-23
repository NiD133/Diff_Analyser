package org.joda.time.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the safe subtraction methods in {@link FieldUtils}.
 * This focuses on the {@code safeSubtract(long, long)} method.
 */
@DisplayName("FieldUtils.safeSubtract(long, long)")
class FieldUtilsTest {

    @Nested
    @DisplayName("when the subtraction is valid")
    class ValidSubtractions {

        @ParameterizedTest(name = "{0} - {1} = {2}")
        @MethodSource("org.joda.time.field.FieldUtilsTest#validSubtractionScenarios")
        @DisplayName("should return the correct result")
        void shouldReturnCorrectResult(long minuend, long subtrahend, long expectedResult) {
            // Act
            long actualResult = FieldUtils.safeSubtract(minuend, subtrahend);
            // Assert
            assertEquals(expectedResult, actualResult);
        }
    }

    @Nested
    @DisplayName("when the subtraction results in overflow or underflow")
    class InvalidSubtractions {

        @ParameterizedTest(name = "{0} - {1} should throw ArithmeticException")
        @MethodSource("org.joda.time.field.FieldUtilsTest#exceptionScenarios")
        @DisplayName("should throw ArithmeticException")
        void shouldThrowException(long minuend, long subtrahend) {
            // Act & Assert
            assertThrows(ArithmeticException.class, () -> {
                FieldUtils.safeSubtract(minuend, subtrahend);
            });
        }
    }

    /**
     * Provides test cases for valid subtractions, including standard and boundary values.
     */
    static Stream<Arguments> validSubtractionScenarios() {
        return Stream.of(
            Arguments.of(0L, 0L, 0L),
            Arguments.of(2L, 3L, -1L),
            Arguments.of(2L, -3L, 5L),
            Arguments.of(-2L, 3L, -5L),
            Arguments.of(-2L, -3L, 1L),
            // Boundary cases that do not overflow/underflow
            Arguments.of(Long.MAX_VALUE, 1L, Long.MAX_VALUE - 1),
            Arguments.of(Long.MIN_VALUE, -1L, Long.MIN_VALUE + 1),
            Arguments.of(Long.MAX_VALUE, Long.MAX_VALUE, 0L),
            Arguments.of(Long.MIN_VALUE, Long.MIN_VALUE, 0L)
        );
    }

    /**
     * Provides test cases that are expected to cause an ArithmeticException.
     */
    static Stream<Arguments> exceptionScenarios() {
        return Stream.of(
            // Underflow scenarios (subtracting a positive value from MIN_VALUE)
            Arguments.of(Long.MIN_VALUE, 1L),
            Arguments.of(Long.MIN_VALUE, 100L),
            Arguments.of(Long.MIN_VALUE, Long.MAX_VALUE),
            // Overflow scenarios (subtracting a negative value from MAX_VALUE)
            Arguments.of(Long.MAX_VALUE, -1L),
            Arguments.of(Long.MAX_VALUE, -100L),
            Arguments.of(Long.MAX_VALUE, Long.MIN_VALUE)
        );
    }
}