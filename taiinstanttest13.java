package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertAll;
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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for modification, arithmetic, and parsing of {@link TaiInstant}.
 */
@DisplayName("TaiInstant Modification, Arithmetic, and Parsing")
class TaiInstantModificationAndParsingTest {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @DisplayName("parse() with invalid format throws exception")
    @ParameterizedTest
    @MethodSource("invalidParseStrings")
    void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    static Stream<Arguments> invalidParseStrings() {
        return Stream.of(
                // "A.123456789s(TAI)" -> non-digit in seconds part
                Arguments.of("A.123456789s(TAI)"),
                // "123.12345678As(TAI)" -> non-digit in nanos part
                Arguments.of("123.12345678As(TAI)"),
                // "123.123456789" -> missing suffix
                Arguments.of("123.123456789"),
                // "123.123456789s" -> missing (TAI)
                Arguments.of("123.123456789s"),
                // "+123.123456789s(TAI)" -> leading plus sign not allowed by parser
                Arguments.of("+123.123456789s(TAI)"),
                // "-123.123s(TAI)" -> nanos part must be exactly 9 digits
                Arguments.of("-123.123s(TAI)"));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @DisplayName("withTaiSeconds() should set seconds and preserve nanos")
    @ParameterizedTest
    @MethodSource("argumentsForWithTaiSeconds")
    void withTaiSeconds_shouldSetSecondsAndPreserveNanos(long initialSeconds, int initialNanos, long newSeconds) {
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant updatedInstant = initialInstant.withTaiSeconds(newSeconds);

        assertAll(
                () -> assertEquals(newSeconds, updatedInstant.getTaiSeconds(), "Seconds should be updated"),
                () -> assertEquals(initialNanos, updatedInstant.getNano(), "Nanos should be preserved"));
    }

    static Stream<Arguments> argumentsForWithTaiSeconds() {
        return Stream.of(
                Arguments.of(0L, 12345, 1L),
                Arguments.of(0L, 12345, -1L),
                Arguments.of(7L, 12345, 2L),
                Arguments.of(7L, 12345, -2L),
                Arguments.of(-99L, 12345, 3L),
                Arguments.of(-99L, 12345, -3L));
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("withNano()")
    class WithNanoTests {

        @ParameterizedTest
        @CsvSource({
                "0, 12345, 1",
                "7, 12345, 2",
                "-99, 12345, 3",
                "-99, 12345, 999999999"
        })
        void validInput_shouldSetNanosAndPreserveSeconds(long initialSeconds, int initialNanos, int newNanos) {
            TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
            TaiInstant updatedInstant = initialInstant.withNano(newNanos);

            assertAll(
                    () -> assertEquals(initialSeconds, updatedInstant.getTaiSeconds(), "Seconds should be preserved"),
                    () -> assertEquals(newNanos, updatedInstant.getNano(), "Nanos should be updated"));
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 1_000_000_000})
        void outOfRangeInput_throwsIllegalArgumentException(int invalidNano) {
            TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
            assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
        }
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("plus(Duration)")
    class PlusDurationTests {

        @Test
        void add_zeroDuration_returnsSameInstant() {
            TaiInstant instant = TaiInstant.ofTaiSeconds(10, 500);
            assertEquals(instant, instant.plus(Duration.ZERO));
        }

        @Test
        void add_positiveSecondsOnly_isCorrect() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
            TaiInstant result = start.plus(Duration.ofSeconds(5));
            assertAll(
                    () -> assertEquals(15, result.getTaiSeconds()),
                    () -> assertEquals(500, result.getNano()));
        }

        @Test
        void add_positiveNanos_noCarryOver() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
            TaiInstant result = start.plus(Duration.ofNanos(1_000));
            assertAll(
                    () -> assertEquals(10, result.getTaiSeconds()),
                    () -> assertEquals(1_500, result.getNano()));
        }

        @Test
        void add_positiveNanos_withCarryOver() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 999_999_500);
            TaiInstant result = start.plus(Duration.ofNanos(1_000));
            assertAll(
                    () -> assertEquals(11, result.getTaiSeconds()),
                    () -> assertEquals(500, result.getNano()));
        }

        @Test
        void add_negativeNanos_withBorrow() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
            TaiInstant result = start.plus(Duration.ofNanos(-1_000));
            assertAll(
                    () -> assertEquals(9, result.getTaiSeconds()),
                    () -> assertEquals(999_999_500, result.getNano()));
        }

        @Test
        void add_fromMinValue_withNegativeDuration_throwsArithmeticException() {
            TaiInstant minInstant = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
            assertThrows(ArithmeticException.class, () -> minInstant.plus(Duration.ofSeconds(-1)));
        }

        @Test
        void add_toMaxValue_withPositiveDuration_throwsArithmeticException() {
            TaiInstant maxInstant = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 999_999_999);
            assertThrows(ArithmeticException.class, () -> maxInstant.plus(Duration.ofNanos(1)));
        }
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("minus(Duration)")
    class MinusDurationTests {

        @Test
        void subtract_zeroDuration_returnsSameInstant() {
            TaiInstant instant = TaiInstant.ofTaiSeconds(10, 500);
            assertEquals(instant, instant.minus(Duration.ZERO));
        }

        @Test
        void subtract_positiveSecondsOnly_isCorrect() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
            TaiInstant result = start.minus(Duration.ofSeconds(5));
            assertAll(
                    () -> assertEquals(5, result.getTaiSeconds()),
                    () -> assertEquals(500, result.getNano()));
        }

        @Test
        void subtract_positiveNanos_noBorrow() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
            TaiInstant result = start.minus(Duration.ofNanos(100));
            assertAll(
                    () -> assertEquals(10, result.getTaiSeconds()),
                    () -> assertEquals(400, result.getNano()));
        }

        @Test
        void subtract_positiveNanos_withBorrow() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
            TaiInstant result = start.minus(Duration.ofNanos(1_000));
            assertAll(
                    () -> assertEquals(9, result.getTaiSeconds()),
                    () -> assertEquals(999_999_500, result.getNano()));
        }

        @Test
        void subtract_negativeNanos_withCarryOver() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 999_999_500);
            TaiInstant result = start.minus(Duration.ofNanos(-1_000));
            assertAll(
                    () -> assertEquals(11, result.getTaiSeconds()),
                    () -> assertEquals(500, result.getNano()));
        }

        @Test
        void subtract_fromMinValue_withPositiveDuration_throwsArithmeticException() {
            TaiInstant minInstant = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
            assertThrows(ArithmeticException.class, () -> minInstant.minus(Duration.ofNanos(1)));
        }

        @Test
        void subtract_fromMaxValue_withNegativeDuration_throwsArithmeticException() {
            TaiInstant maxInstant = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
            assertThrows(ArithmeticException.class, () -> maxInstant.minus(Duration.ofSeconds(-1)));
        }
    }
}