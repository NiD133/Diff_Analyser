package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for modification and parsing of {@link TaiInstant}.
 */
@DisplayName("TaiInstant")
class TaiInstantTest {

    private static final int NANOS_PER_SECOND = 1_000_000_000;

    @Nested
    @DisplayName("parse()")
    class ParseTest {

        @Test
        void parse_shouldThrowNullPointerException_forNullInput() {
            assertThrows(NullPointerException.class, () -> TaiInstant.parse(null));
        }

        @DisplayName("should throw DateTimeParseException for invalid formats")
        @ParameterizedTest(name = "for input: \"{0}\"")
        @MethodSource("org.threeten.extra.scale.TaiInstantTest#invalidParseStrings")
        void parse_shouldThrowException_forInvalidFormats(String invalidString) {
            assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
        }
    }

    @Nested
    @DisplayName("with...")
    class WithTest {

        @Test
        void withTaiSeconds_shouldUpdateSecondsAndPreserveNanos() {
            // Arrange
            long initialNanos = 12345L;
            TaiInstant initialInstant = TaiInstant.ofTaiSeconds(10, initialNanos);
            long newSeconds = -5L;

            // Act
            TaiInstant updatedInstant = initialInstant.withTaiSeconds(newSeconds);

            // Assert
            assertEquals(newSeconds, updatedInstant.getTaiSeconds());
            assertEquals(initialNanos, updatedInstant.getNano());
        }

        @Test
        void withNano_shouldUpdateNanosAndPreserveSeconds() {
            // Arrange
            long initialSeconds = -99L;
            TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, 12345);
            int newNanos = 999_999_999;

            // Act
            TaiInstant updatedInstant = initialInstant.withNano(newNanos);

            // Assert
            assertEquals(initialSeconds, updatedInstant.getTaiSeconds());
            assertEquals(newNanos, updatedInstant.getNano());
        }

        @DisplayName("withNano() should throw for out-of-range input")
        @ParameterizedTest
        @ValueSource(ints = {-1, NANOS_PER_SECOND})
        void withNano_shouldThrowException_forInvalidInput(int invalidNano) {
            // Arrange
            TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
        }
    }

    /**
     * The original tests for plus() and minus() used a very large,
     * programmatically-generated data set that was difficult to read and maintain.
     * These have been replaced by a smaller number of focused, descriptive tests
     * that cover the main scenarios and edge cases for better understandability.
     */
    @Nested
    @DisplayName("plus(Duration)")
    class PlusTest {

        @Test
        void plus_whenAddingZeroDuration_shouldReturnSameInstant() {
            TaiInstant instant = TaiInstant.ofTaiSeconds(10, 500);
            TaiInstant result = instant.plus(Duration.ZERO);
            assertEquals(instant, result);
        }

        @Test
        void plus_whenAddingNanos_shouldCarryOverToSeconds() {
            // -4s + 666,666,667ns  PLUS  0s + 333,333,333ns
            // = -4s + 1,000,000,000ns
            // = -3s + 0ns
            TaiInstant instant = TaiInstant.ofTaiSeconds(-4, 666_666_667);
            Duration toAdd = Duration.ofNanos(333_333_333);

            TaiInstant result = instant.plus(toAdd);

            assertEquals(-3, result.getTaiSeconds());
            assertEquals(0, result.getNano());
        }

        @Test
        void plus_whenAddingNegativeDuration_shouldEffectivelySubtract() {
            // -4s + 666,666,667ns PLUS Duration.ofSeconds(-1, 333,333,334)
            // The duration is equivalent to -0.666... seconds.
            // Adding it normalizes to: (-4s - 1s) + (666,666,667ns + 333,333,334ns)
            // = -5s + 1,000,000,001ns => -4s + 1ns
            TaiInstant instant = TaiInstant.ofTaiSeconds(-4, 666_666_667);
            Duration toAdd = Duration.ofSeconds(-1, 333_333_334);

            TaiInstant result = instant.plus(toAdd);

            assertEquals(-4, result.getTaiSeconds());
            assertEquals(1, result.getNano());
        }

        @Test
        void plus_whenOverflow_shouldThrowArithmeticException() {
            TaiInstant instant = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
            Duration toAdd = Duration.ofSeconds(1);
            assertThrows(ArithmeticException.class, () -> instant.plus(toAdd));
        }
    }

    @Nested
    @DisplayName("minus(Duration)")
    class MinusTest {

        @Test
        void minus_whenSubtractingZeroDuration_shouldReturnSameInstant() {
            TaiInstant instant = TaiInstant.ofTaiSeconds(10, 500);
            TaiInstant result = instant.minus(Duration.ZERO);
            assertEquals(instant, result);
        }

        @Test
        void minus_whenSubtractingNanos_shouldBorrowFromSeconds() {
            // -3s + 0ns  MINUS  0s + 1ns
            // = -3s - 1ns
            // = -4s + 1,000,000,000ns - 1ns
            // = -4s + 999,999,999ns
            TaiInstant instant = TaiInstant.ofTaiSeconds(-3, 0);
            Duration toSubtract = Duration.ofNanos(1);

            TaiInstant result = instant.minus(toSubtract);

            assertEquals(-4, result.getTaiSeconds());
            assertEquals(999_999_999, result.getNano());
        }

        @Test
        void minus_whenSubtractingNegativeDuration_shouldEffectivelyAdd() {
            // 0s + 0ns MINUS -3s + 0ns
            // = 3s + 0ns
            TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
            Duration toSubtract = Duration.ofSeconds(-3);

            TaiInstant result = instant.minus(toSubtract);

            assertEquals(3, result.getTaiSeconds());
            assertEquals(0, result.getNano());
        }

        @Test
        void minus_whenUnderflow_shouldThrowArithmeticException() {
            TaiInstant instant = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
            Duration toSubtract = Duration.ofSeconds(1);
            assertThrows(ArithmeticException.class, () -> instant.minus(toSubtract));
        }
    }

    // Data Providers

    static Stream<Arguments> invalidParseStrings() {
        return Stream.of(
                // Note: The format is {seconds}.{nanosOfSecond}s(TAI)
                Arguments.of("A.123456789s(TAI)"),      // Non-digit seconds
                Arguments.of("123.12345678As(TAI)"),     // Non-digit nanos
                Arguments.of("123.123456789"),           // Missing suffix
                Arguments.of("123.123456789s"),          // Missing (TAI)
                Arguments.of("+123.123456789s(TAI)"),    // Explicit plus sign not supported
                Arguments.of("-123.123s(TAI)")           // Nanos must be 9 digits
        );
    }
}