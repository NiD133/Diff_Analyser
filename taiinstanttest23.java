package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("TaiInstant Arithmetic and Comparison Tests")
class TaiInstantArithmeticAndComparisonTest {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_invalidParseStrings() {
        return Stream.of(
            Arguments.of("A.123456789s(TAI)"),      // Non-numeric seconds
            Arguments.of("123.12345678As(TAI)"),     // Non-numeric nanos
            Arguments.of("123.123456789"),           // Missing suffix
            Arguments.of("123.123456789s"),          // Missing (TAI)
            Arguments.of("+123.123456789s(TAI)"),    // Explicit plus sign not allowed
            Arguments.of("-123.123s(TAI)")           // Nanos must be 9 digits
        );
    }

    @ParameterizedTest
    @MethodSource("provider_invalidParseStrings")
    @DisplayName("parse() should throw exception for invalid formats")
    void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_withTaiSeconds() {
        return Stream.of(
            // initialSeconds, initialNanos, newSeconds, expectedSeconds, expectedNanos
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            Arguments.of(7L, 12345L, 2L, 2L, 12345L),
            Arguments.of(7L, 12345L, -2L, -2L, 12345L),
            Arguments.of(-99L, 12345L, 3L, 3L, 12345L),
            Arguments.of(-99L, 12345L, -3L, -3L, 12345L)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withTaiSeconds")
    @DisplayName("withTaiSeconds() should update seconds and preserve nanoseconds")
    void withTaiSeconds_shouldSetSecondsAndKeepNanos(long initialSeconds, long initialNanos, long newSeconds, long expectedSeconds, long expectedNanos) {
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initialInstant.withTaiSeconds(newSeconds);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_withNanoValid() {
        return Stream.of(
            // initialSeconds, initialNanos, newNanos, expectedSeconds, expectedNanos
            Arguments.of(0L, 12345L, 1, 0L, 1L),
            Arguments.of(7L, 12345L, 2, 7L, 2L),
            Arguments.of(-99L, 12345L, 3, -99L, 3L),
            Arguments.of(-99L, 12345L, 999_999_999, -99L, 999_999_999L)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withNanoValid")
    @DisplayName("withNano() should update nanoseconds for valid input")
    void withNano_shouldSetNanosForValidInput(long initialSeconds, long initialNanos, int newNanos, long expectedSeconds, long expectedNanos) {
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initialInstant.withNano(newNanos);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    static Stream<Arguments> provider_withNanoInvalid() {
        return Stream.of(
            // initialSeconds, initialNanos, invalidNanoValue
            Arguments.of(-99L, 12345L, -1),              // Below valid range
            Arguments.of(-99L, 12345L, 1_000_000_000)    // Above valid range
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withNanoInvalid")
    @DisplayName("withNano() should throw exception for out-of-range nanoseconds")
    void withNano_shouldThrowExceptionForInvalidInput(long initialSeconds, long initialNanos, int invalidNano) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------

    @ParameterizedTest
    // @formatter:off
    @CsvSource({
        // initialSeconds, initialNanos, secondsToAdd, nanosToAdd, expectedSeconds, expectedNanos
        " -9223372036854775808, 0,  9223372036854775807, 0, -1, 0",
        " -4, 666666667, -4, 666666667, -7, 333333334",
        " -4, 666666667, -3, 0, -7, 666666667",
        " -4, 666666667, -2, 0, -6, 666666667",
        " -4, 666666667, -1, 0, -5, 666666667",
        " -4, 666666667, -1, 333333334, -4, 1",
        " -4, 666666667, -1, 666666667, -4, 333333334",
        " -4, 666666667, -1, 999999999, -4, 666666666",
        " -4, 666666667, 0, 0, -4, 666666667",
        " -4, 666666667, 0, 1, -4, 666666668",
        " -4, 666666667, 0, 333333333, -3, 0",
        " -4, 666666667, 0, 666666666, -3, 333333333",
        " -4, 666666667, 1, 0, -3, 666666667",
        " -4, 666666667, 2, 0, -2, 666666667",
        " -4, 666666667, 3, 0, -1, 666666667",
        " -4, 666666667, 3, 333333333, 0, 0",
        " -3, 0, -4, 666666667, -7, 666666667",
        " -3, 0, -3, 0, -6, 0",
        " -3, 0, -2, 0, -5, 0",
        " -3, 0, -1, 0, -4, 0",
        " -3, 0, -1, 333333334, -4, 333333334",
        " -3, 0, -1, 666666667, -4, 666666667",
        " -3, 0, -1, 999999999, -4, 999999999",
        " -3, 0, 0, 0, -3, 0",
        " -3, 0, 0, 1, -3, 1",
        " -3, 0, 0, 333333333, -3, 333333333",
        " -3, 0, 0, 666666666, -3, 666666666",
        " -3, 0, 1, 0, -2, 0",
        " -3, 0, 2, 0, -1, 0",
        " -3, 0, 3, 0, 0, 0",
        " -3, 0, 3, 333333333, 0, 333333333",
        " 0, 0, 0, 0, 0, 0",
        " 0, 0, 1, 1, 1, 1",
        " 0, 333333333, 0, 666666666, 0, 999999999",
        " 0, 333333333, 0, 666666667, 1, 0", // Rollover case
        " 3, 333333333, 3, 333333333, 6, 666666666",
        " 9223372036854775807, 0, -9223372036854775808, 0, -1, 0"
    })
    // @formatter:on
    @DisplayName("plus() should correctly add a duration")
    void plus_shouldCorrectlyAddDuration(long initialSeconds, int initialNanos, long secondsToAdd, int nanosToAdd, long expectedSeconds, int expectedNanos) {
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration durationToAdd = Duration.ofSeconds(secondsToAdd, nanosToAdd);
        
        TaiInstant result = initialInstant.plus(durationToAdd);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------

    @ParameterizedTest
    // @formatter:off
    @CsvSource({
        // initialSeconds, initialNanos, secondsToSubtract, nanosToSubtract, expectedSeconds, expectedNanos
        " -9223372036854775808, 0, -9223372036854775807, 0, -1, 0",
        " -4, 666666667, -4, 666666667, 0, 0",
        " -4, 666666667, -3, 0, -1, 666666667",
        " -4, 666666667, -2, 0, -2, 666666667",
        " -4, 666666667, -1, 0, -3, 666666667",
        " -4, 666666667, -1, 333333334, -3, 333333333",
        " -4, 666666667, -1, 666666667, -3, 0",
        " -4, 666666667, -1, 999999999, -4, 666666668",
        " -4, 666666667, 0, 0, -4, 666666667",
        " -4, 666666667, 0, 1, -4, 666666666",
        " -4, 666666667, 0, 333333333, -4, 333333334",
        " -4, 666666667, 0, 666666666, -4, 1",
        " -4, 666666667, 1, 0, -5, 666666667",
        " -4, 666666667, 2, 0, -6, 666666667",
        " -4, 666666667, 3, 0, -7, 666666667",
        " -4, 666666667, 3, 333333333, -7, 333333334",
        " -3, 0, -4, 666666667, 0, 333333333",
        " -3, 0, -3, 0, 0, 0",
        " -3, 0, -2, 0, -1, 0",
        " -3, 0, -1, 0, -2, 0",
        " -3, 0, -1, 333333334, -3, 666666666",
        " -3, 0, -1, 666666667, -3, 333333333",
        " -3, 0, -1, 999999999, -3, 1",
        " -3, 0, 0, 0, -3, 0",
        " -3, 0, 0, 1, -4, 999999999", // Borrow case
        " -3, 0, 0, 333333333, -4, 666666667",
        " -3, 0, 0, 666666666, -4, 333333334",
        " -3, 0, 1, 0, -4, 0",
        " -3, 0, 2, 0, -5, 0",
        " -3, 0, 3, 0, -6, 0",
        " -3, 0, 3, 333333333, -7, 666666667",
        " 0, 0, 0, 0, 0, 0",
        " 1, 1, 1, 1, 0, 0",
        " 3, 333333333, 3, 333333333, 0, 0",
        " 9223372036854775807, 9223372036854775807, 0, 0"
    })
    // @formatter:on
    @DisplayName("minus() should correctly subtract a duration")
    void minus_shouldCorrectlySubtractDuration(long initialSeconds, int initialNanos, long secondsToSubtract, int nanosToSubtract, long expectedSeconds, int expectedNanos) {
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration durationToSubtract = Duration.ofSeconds(secondsToSubtract, nanosToSubtract);

        TaiInstant result = initialInstant.minus(durationToSubtract);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // Comparison
    //-----------------------------------------------------------------------

    /**
     * Helper method to verify the comparison contracts (compareTo, isBefore, isAfter, equals)
     * for a set of instants.
     *
     * @param sortedInstants a varargs array of TaiInstant, which must be pre-sorted from smallest to largest.
     */
    void assertTaiInstantComparisonContracts(TaiInstant... sortedInstants) {
        for (int i = 0; i < sortedInstants.length; i++) {
            TaiInstant a = sortedInstants[i];
            for (int j = 0; j < sortedInstants.length; j++) {
                TaiInstant b = sortedInstants[j];
                if (i < j) {
                    assertTrue(a.compareTo(b) < 0, a + " should be less than " + b);
                    assertFalse(a.equals(b));
                    assertTrue(a.isBefore(b));
                    assertFalse(a.isAfter(b));
                } else if (i > j) {
                    assertTrue(a.compareTo(b) > 0, a + " should be greater than " + b);
                    assertFalse(a.equals(b));
                    assertFalse(a.isBefore(b));
                    assertTrue(a.isAfter(b));
                } else {
                    assertEquals(0, a.compareTo(b), a + " should be equal to " + b);
                    assertTrue(a.equals(b));
                    assertFalse(a.isBefore(b));
                    assertFalse(a.isAfter(b));
                }
            }
        }
    }

    @Test
    @DisplayName("compareTo() should throw ClassCastException when comparing to a different type")
    @SuppressWarnings({"unchecked", "rawtypes"})
    void compareTo_withDifferentType_throwsClassCastException() {
        Comparable taiInstant = TaiInstant.ofTaiSeconds(0L, 2);
        assertThrows(ClassCastException.class, () -> taiInstant.compareTo(new Object()));
    }
}