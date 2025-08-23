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
 * Tests for modification and arithmetic operations on {@link TaiInstant}.
 */
@DisplayName("TaiInstant Modification and Arithmetic")
class TaiInstantModificationAndArithmeticTest {

    private static final int HALF_A_SECOND_NANOS = 500_000_000;
    private static final int MAX_NANOS = 999_999_999;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    static Stream<String> invalidParseStrings() {
        return Stream.of(
            "A.123456789s(TAI)",      // non-numeric seconds
            "123.12345678As(TAI)",     // non-numeric nanos
            "123.123456789",           // missing suffix
            "123.123456789s",          // missing (TAI)
            "+123.123456789s(TAI)",    // leading plus not allowed
            "-123.123s(TAI)"           // nanos must be 9 digits
        );
    }

    @ParameterizedTest
    @MethodSource("invalidParseStrings")
    @DisplayName("parse() with invalid format throws exception")
    void parse_withInvalidFormat_throwsException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    static Stream<Arguments> dataForWithTaiSeconds() {
        return Stream.of(
            Arguments.of(0L, 12345L, 1L),
            Arguments.of(0L, 12345L, -1L),
            Arguments.of(7L, 12345L, 2L),
            Arguments.of(-99L, 12345L, 3L)
        );
    }

    @ParameterizedTest
    @MethodSource("dataForWithTaiSeconds")
    @DisplayName("withTaiSeconds() replaces seconds and preserves nanos")
    void withTaiSeconds_replacesSecondsAndPreservesNanos(long initialSeconds, long initialNanos, long newSeconds) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withTaiSeconds(newSeconds);

        assertEquals(newSeconds, result.getTaiSeconds());
        assertEquals(initialNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("withNano()")
    class WithNanoTests {

        static Stream<Arguments> dataForWithNano() {
            return Stream.of(
                Arguments.of(0L, 12345L, 1),
                Arguments.of(7L, 12345L, 2),
                Arguments.of(-99L, 12345L, 3),
                Arguments.of(-99L, 12345L, MAX_NANOS)
            );
        }

        @ParameterizedTest
        @MethodSource("dataForWithNano")
        @DisplayName("replaces nanos and preserves seconds for valid input")
        void withNano_replacesNanosAndPreservesSeconds(long initialSeconds, long initialNanos, int newNanos) {
            TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
            TaiInstant result = initial.withNano(newNanos);

            assertEquals(initialSeconds, result.getTaiSeconds());
            assertEquals(newNanos, result.getNano());
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 1_000_000_000})
        @DisplayName("throws exception for out-of-range input")
        void withNano_withOutOfRangeValue_throwsException(int invalidNano) {
            TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
            assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
        }
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("plus(Duration)")
    class PlusTests {

        @Test
        @DisplayName("adding zero duration returns an equal instant")
        void plus_zeroDuration_returnsEqualInstant() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 100);
            TaiInstant result = initial.plus(Duration.ZERO);
            assertEquals(initial, result);
        }

        @Test
        @DisplayName("adds nanos without carrying over to seconds")
        void plus_nanosWithoutCarry() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(5, HALF_A_SECOND_NANOS);
            TaiInstant result = initial.plus(Duration.ofNanos(100));
            
            assertEquals(5, result.getTaiSeconds());
            assertEquals(HALF_A_SECOND_NANOS + 100, result.getNano());
        }

        @Test
        @DisplayName("adds nanos with a carry-over to seconds")
        void plus_nanosWithCarry() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(5, MAX_NANOS - 100);
            TaiInstant result = initial.plus(Duration.ofNanos(200));

            assertEquals(6, result.getTaiSeconds());
            assertEquals(99, result.getNano()); // (MAX_NANOS - 100 + 200) - 1_000_000_000
        }

        @Test
        @DisplayName("adds seconds and nanos")
        void plus_secondsAndNanos() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 100);
            TaiInstant result = initial.plus(Duration.ofSeconds(3, 200));

            assertEquals(13, result.getTaiSeconds());
            assertEquals(300, result.getNano());
        }
        
        @Test
        @DisplayName("adds a negative duration, borrowing from seconds")
        void plus_negativeDurationWithBorrow() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 100);
            TaiInstant result = initial.plus(Duration.ofSeconds(-3, -200));

            assertEquals(6, result.getTaiSeconds());
            assertEquals(MAX_NANOS - 99, result.getNano()); // 100 - 200 requires borrowing 1 second
        }

        @Test
        @DisplayName("adding to MAX_VALUE throws ArithmeticException")
        void plus_toMaxValue_throwsException() {
            TaiInstant maxInstant = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, MAX_NANOS);
            Duration smallPositive = Duration.ofNanos(1);
            assertThrows(ArithmeticException.class, () -> maxInstant.plus(smallPositive));
        }

        @Test
        @DisplayName("adding a negative duration to MIN_VALUE throws ArithmeticException")
        void plus_toMinValueWithNegative_throwsException() {
            TaiInstant minInstant = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
            Duration smallNegative = Duration.ofNanos(-1);
            assertThrows(ArithmeticException.class, () -> minInstant.plus(smallNegative));
        }
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("minus(Duration)")
    class MinusTests {

        @Test
        @DisplayName("subtracting zero duration returns an equal instant")
        void minus_zeroDuration_returnsEqualInstant() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 100);
            TaiInstant result = initial.minus(Duration.ZERO);
            assertEquals(initial, result);
        }

        @Test
        @DisplayName("subtracts nanos without borrowing from seconds")
        void minus_nanosWithoutBorrow() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(5, HALF_A_SECOND_NANOS);
            TaiInstant result = initial.minus(Duration.ofNanos(100));

            assertEquals(5, result.getTaiSeconds());
            assertEquals(HALF_A_SECOND_NANOS - 100, result.getNano());
        }

        @Test
        @DisplayName("subtracts nanos with a borrow from seconds")
        void minus_nanosWithBorrow() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(5, 100);
            TaiInstant result = initial.minus(Duration.ofNanos(200));

            assertEquals(4, result.getTaiSeconds());
            assertEquals(MAX_NANOS - 99, result.getNano()); // 100 - 200 requires borrowing 1 second
        }

        @Test
        @DisplayName("subtracts seconds and nanos")
        void minus_secondsAndNanos() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 200);
            TaiInstant result = initial.minus(Duration.ofSeconds(3, 100));

            assertEquals(7, result.getTaiSeconds());
            assertEquals(100, result.getNano());
        }

        @Test
        @DisplayName("subtracts a negative duration, carrying over to seconds")
        void minus_negativeDurationWithCarry() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, MAX_NANOS - 100);
            TaiInstant result = initial.minus(Duration.ofSeconds(-3, -200));

            assertEquals(14, result.getTaiSeconds());
            assertEquals(99, result.getNano());
        }

        @Test
        @DisplayName("subtracting from MIN_VALUE throws ArithmeticException")
        void minus_fromMinValue_throwsException() {
            TaiInstant minInstant = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
            Duration smallPositive = Duration.ofNanos(1);
            assertThrows(ArithmeticException.class, () -> minInstant.minus(smallPositive));
        }

        @Test
        @DisplayName("subtracting a negative duration from MAX_VALUE throws ArithmeticException")
        void minus_negativeFromMaxValue_throwsException() {
            TaiInstant maxInstant = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, MAX_NANOS);
            Duration smallNegative = Duration.ofNanos(-1);
            assertThrows(ArithmeticException.class, () -> maxInstant.minus(smallNegative));
        }
    }
}