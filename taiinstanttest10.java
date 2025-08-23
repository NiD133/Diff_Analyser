package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link TaiInstant}.
 */
class TaiInstantTest {

    private static final TaiInstant TAI_1_SEC_10_NANOS = TaiInstant.ofTaiSeconds(1, 10);

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------

    @DisplayName("parse() should create an instant for valid string formats")
    @ParameterizedTest(name = "Parsing ''{0}''")
    @CsvSource({
            "0.123456789s(TAI), 0, 123456789",
            "-1.987654321s(TAI), -1, 987654321",
            "12345.000000000s(TAI), 12345, 0",
            "-98765.000000001s(TAI), -98765, 1"
    })
    void parse_withValidFormat_returnsCorrectInstant(String text, long expectedSeconds, int expectedNanos) {
        TaiInstant test = TaiInstant.parse(text);
        assertEquals(expectedSeconds, test.getTaiSeconds());
        assertEquals(expectedNanos, test.getNano());
    }

    static Stream<Arguments> invalidParseStrings() {
        return Stream.of(
                Arguments.of("A.123456789s(TAI)", "Non-numeric seconds"),
                Arguments.of("123.12345678As(TAI)", "Non-numeric nanos"),
                Arguments.of("123.123456789", "Missing suffix 's(TAI)'"),
                Arguments.of("123.123456789s", "Missing '(TAI)'"),
                Arguments.of("+123.123456789s(TAI)", "Leading plus sign not allowed"),
                Arguments.of("-123.123s(TAI)", "Nanos part must be 9 digits")
        );
    }

    @DisplayName("parse() should throw an exception for invalid string formats")
    @ParameterizedTest(name = "Invalid format: {1}")
    @MethodSource("invalidParseStrings")
    void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString, String description) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("withTaiSeconds() should set seconds and preserve nanos")
    void withTaiSeconds_setsSecondsAndPreservesNanos() {
        TaiInstant base = TaiInstant.ofTaiSeconds(10, 123);
        TaiInstant result = base.withTaiSeconds(5);
        assertEquals(5, result.getTaiSeconds());
        assertEquals(123, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("withNano() should set nanos and preserve seconds for valid input")
    void withNano_setsNanoAndPreservesSeconds() {
        TaiInstant base = TaiInstant.ofTaiSeconds(10, 123);
        TaiInstant result = base.withNano(456);
        assertEquals(10, result.getTaiSeconds());
        assertEquals(456, result.getNano());
    }

    @DisplayName("withNano() should throw an exception for out-of-range input")
    @ParameterizedTest(name = "Nano value: {0}")
    @ValueSource(ints = {-1, 1_000_000_000})
    void withNano_givenInvalidNano_throwsIllegalArgumentException(int invalidNano) {
        assertThrows(IllegalArgumentException.class, () -> TAI_1_SEC_10_NANOS.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------

    @DisplayName("plus() should add durations correctly")
    @ParameterizedTest(name = "{0}s + {1}ns, add {2}s + {3}ns -> {4}s + {5}ns")
    @CsvSource({
            // Positive duration, no nano carry
            "1, 100, 2, 200, 3, 300",
            // Positive duration, with nano carry
            "1, 800_000_000, 0, 300_000_000, 2, 100_000_000",
            // Negative duration, no nano borrow
            "5, 500_000_000, -2, -100_000_000, 3, 400_000_000",
            // Negative duration, with nano borrow
            "5, 100_000_000, -2, -300_000_000, 2, 800_000_000",
    })
    void plus_addsDurationCorrectly(long s1, int n1, long s2, int n2, long expS, int expN) {
        TaiInstant base = TaiInstant.ofTaiSeconds(s1, n1);
        Duration toAdd = Duration.ofSeconds(s2, n2);
        TaiInstant result = base.plus(toAdd);
        assertEquals(TaiInstant.ofTaiSeconds(expS, expN), result);
    }

    @Test
    @DisplayName("plus() a zero duration should return an equal instant")
    void plus_zeroDuration_returnsSameInstant() {
        assertEquals(TAI_1_SEC_10_NANOS, TAI_1_SEC_10_NANOS.plus(Duration.ZERO));
    }

    @Test
    @DisplayName("plus() should throw an exception on overflow")
    void plus_durationCausingOverflow_throwsArithmeticException() {
        TaiInstant max = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> max.plus(Duration.ofSeconds(1)));

        TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> min.plus(Duration.ofSeconds(-1)));
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------

    @DisplayName("minus() should subtract durations correctly")
    @ParameterizedTest(name = "{0}s + {1}ns, minus {2}s + {3}ns -> {4}s + {5}ns")
    @CsvSource({
            // Positive duration, no nano borrow
            "5, 500_000_000, 2, 100_000_000, 3, 400_000_000",
            // Positive duration, with nano borrow
            "5, 100_000_000, 2, 300_000_000, 2, 800_000_000",
            // Negative duration (addition), no nano carry
            "3, 300, -2, -200, 5, 500",
            // Negative duration (addition), with nano carry
            "1, 800_000_000, -1, -300_000_000, 3, 100_000_000",
    })
    void minus_subtractsDurationCorrectly(long s1, int n1, long s2, int n2, long expS, int expN) {
        TaiInstant base = TaiInstant.ofTaiSeconds(s1, n1);
        Duration toSubtract = Duration.ofSeconds(s2, n2);
        TaiInstant result = base.minus(toSubtract);
        assertEquals(TaiInstant.ofTaiSeconds(expS, expN), result);
    }

    @Test
    @DisplayName("minus() a zero duration should return an equal instant")
    void minus_zeroDuration_returnsSameInstant() {
        assertEquals(TAI_1_SEC_10_NANOS, TAI_1_SEC_10_NANOS.minus(Duration.ZERO));
    }

    @Test
    @DisplayName("minus() should throw an exception on overflow")
    void minus_durationCausingOverflow_throwsArithmeticException() {
        TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> min.minus(Duration.ofSeconds(1)));

        TaiInstant max = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> max.minus(Duration.ofSeconds(-1)));
    }

    //-----------------------------------------------------------------------
    // comparisons
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("comparison methods should work correctly")
    void comparison_worksCorrectly() {
        TaiInstant lesser = TaiInstant.ofTaiSeconds(10, 100);
        TaiInstant middle = TaiInstant.ofTaiSeconds(10, 200);
        TaiInstant greater = TaiInstant.ofTaiSeconds(11, 0);

        // isBefore
        assertTrue(lesser.isBefore(middle));
        assertTrue(middle.isBefore(greater));
        assertFalse(middle.isBefore(lesser));
        assertFalse(middle.isBefore(middle));

        // isAfter
        assertTrue(middle.isAfter(lesser));
        assertTrue(greater.isAfter(middle));
        assertFalse(lesser.isAfter(middle));
        assertFalse(middle.isAfter(middle));

        // compareTo
        assertTrue(lesser.compareTo(middle) < 0);
        assertTrue(greater.compareTo(middle) > 0);
        assertEquals(0, middle.compareTo(middle));
    }
}