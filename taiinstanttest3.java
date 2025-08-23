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
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("TaiInstant")
class TaiInstantTest {

    private static final TaiInstant INSTANT_ZERO = TaiInstant.ofTaiSeconds(0, 0);

    //-----------------------------------------------------------------------
    // Test factory method: parse()
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("provideInvalidStringsForParsing")
    @DisplayName("parse() throws exception for invalid formats")
    void parse_invalidFormat_throwsException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    static Stream<String> provideInvalidStringsForParsing() {
        return Stream.of(
            "A.123456789s(TAI)",   // Non-numeric seconds
            "123.12345678As(TAI)",  // Non-numeric nanos
            "123.123456789",        // Missing suffix
            "123.123456789s",       // Missing (TAI)
            "+123.123456789s(TAI)", // Explicit plus sign not allowed
            "-123.123s(TAI)"        // Nanos must be 9 digits
        );
    }

    //-----------------------------------------------------------------------
    // Test factory method: ofTaiSeconds()
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("ofTaiSeconds() normalizes correctly for positive nano adjustment")
    void ofTaiSeconds_withPositiveNanoAdjustment_normalizesByCarryingToSeconds() {
        // Nano adjustment within the second
        TaiInstant actual1 = TaiInstant.ofTaiSeconds(10, 500);
        assertEquals(10, actual1.getTaiSeconds());
        assertEquals(500, actual1.getNano());

        // Nano adjustment causing carry-over to seconds
        TaiInstant actual2 = TaiInstant.ofTaiSeconds(10, 1_000_000_500);
        assertEquals(11, actual2.getTaiSeconds());
        assertEquals(500, actual2.getNano());
    }

    @Test
    @DisplayName("ofTaiSeconds() normalizes correctly for negative nano adjustment")
    void ofTaiSeconds_withNegativeNanoAdjustment_normalizesByBorrowingFromSeconds() {
        // Nano adjustment causing borrow from seconds
        TaiInstant actual = TaiInstant.ofTaiSeconds(10, -500);
        assertEquals(9, actual.getTaiSeconds());
        assertEquals(999_999_500, actual.getNano());
    }

    //-----------------------------------------------------------------------
    // Test wither methods: withTaiSeconds(), withNano()
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("provideSecondsForWithTaiSeconds")
    @DisplayName("withTaiSeconds() sets seconds and preserves nanos")
    void withTaiSeconds_setsSecondsAndPreservesNanos(long initialSeconds, int initialNanos, long newSeconds) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withTaiSeconds(newSeconds);

        assertEquals(newSeconds, result.getTaiSeconds());
        assertEquals(initialNanos, result.getNano());
    }

    static Stream<Arguments> provideSecondsForWithTaiSeconds() {
        return Stream.of(
            Arguments.of(0L, 12345, 1L),
            Arguments.of(7L, 12345, 2L),
            Arguments.of(-99L, 12345, -3L)
        );
    }

    @Test
    @DisplayName("withNano() sets nanos and preserves seconds")
    void withNano_validNano_setsNanosAndPreservesSeconds() {
        TaiInstant initial = TaiInstant.ofTaiSeconds(10, 12345);
        TaiInstant result = initial.withNano(987);

        assertEquals(10, result.getTaiSeconds());
        assertEquals(987, result.getNano());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_000_000})
    @DisplayName("withNano() throws exception for out-of-range values")
    void withNano_invalidNano_throwsException(int invalidNano) {
        assertThrows(IllegalArgumentException.class, () -> INSTANT_ZERO.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // Test arithmetic method: plus()
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("plus() a zero duration returns an equal instant")
    void plus_zeroDuration_returnsEqualInstant() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
        assertEquals(start, start.plus(Duration.ZERO));
    }

    @Test
    @DisplayName("plus() a positive duration without nano carry")
    void plus_positiveDuration_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 100);
        Duration toAdd = Duration.ofSeconds(5, 200);
        TaiInstant result = start.plus(toAdd);

        assertEquals(15, result.getTaiSeconds());
        assertEquals(300, result.getNano());
    }

    @Test
    @DisplayName("plus() a positive duration with nano carry")
    void plus_positiveDuration_withNanoCarry_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(-4, 666_666_667);
        Duration toAdd = Duration.ofSeconds(3, 333_333_333);
        TaiInstant result = start.plus(toAdd);

        assertEquals(0, result.getTaiSeconds());
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("plus() a negative duration without nano borrow")
    void plus_negativeDuration_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
        Duration toAdd = Duration.ofSeconds(-5, -200);
        TaiInstant result = start.plus(toAdd);

        assertEquals(5, result.getTaiSeconds());
        assertEquals(300, result.getNano());
    }

    @Test
    @DisplayName("plus() a negative duration with nano borrow")
    void plus_negativeDuration_withNanoBorrow_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 100);
        Duration toAdd = Duration.ofSeconds(-5, -200);
        TaiInstant result = start.plus(toAdd);

        assertEquals(4, result.getTaiSeconds());
        assertEquals(999_999_900, result.getNano());
    }

    @Test
    @DisplayName("plus() throws ArithmeticException on overflow")
    void plus_durationCausingOverflow_throwsException() {
        TaiInstant max = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> max.plus(Duration.ofSeconds(1)));
    }

    //-----------------------------------------------------------------------
    // Test arithmetic method: minus()
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("minus() a zero duration returns an equal instant")
    void minus_zeroDuration_returnsEqualInstant() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
        assertEquals(start, start.minus(Duration.ZERO));
    }

    @Test
    @DisplayName("minus() a positive duration without nano borrow")
    void minus_positiveDuration_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
        Duration toSubtract = Duration.ofSeconds(5, 200);
        TaiInstant result = start.minus(toSubtract);

        assertEquals(5, result.getTaiSeconds());
        assertEquals(300, result.getNano());
    }

    @Test
    @DisplayName("minus() a positive duration with nano borrow")
    void minus_positiveDuration_withNanoBorrow_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(-3, 0);
        Duration toSubtract = Duration.ofNanos(1);
        TaiInstant result = start.minus(toSubtract);

        assertEquals(-4, result.getTaiSeconds());
        assertEquals(999_999_999, result.getNano());
    }

    @Test
    @DisplayName("minus() a negative duration without nano carry")
    void minus_negativeDuration_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 100);
        Duration toSubtract = Duration.ofSeconds(-5, -200);
        TaiInstant result = start.minus(toSubtract);

        assertEquals(15, result.getTaiSeconds());
        assertEquals(300, result.getNano());
    }

    @Test
    @DisplayName("minus() a negative duration with nano carry")
    void minus_negativeDuration_withNanoCarry_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 900_000_000);
        Duration toSubtract = Duration.ofSeconds(-5, -200_000_000);
        TaiInstant result = start.minus(toSubtract);

        assertEquals(16, result.getTaiSeconds());
        assertEquals(100_000_000, result.getNano());
    }

    @Test
    @DisplayName("minus() throws ArithmeticException on underflow")
    void minus_durationCausingUnderflow_throwsException() {
        TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> min.minus(Duration.ofSeconds(1)));
    }

    //-----------------------------------------------------------------------
    // Test comparison methods
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("comparison methods (compareTo, isAfter, isBefore, equals) work correctly")
    void comparisonMethods_workCorrectly() {
        TaiInstant t1 = TaiInstant.ofTaiSeconds(-1, 0);
        TaiInstant t2 = TaiInstant.ofTaiSeconds(0, 0);
        TaiInstant t3 = TaiInstant.ofTaiSeconds(0, 1);
        TaiInstant t4 = TaiInstant.ofTaiSeconds(0, 999_999_999);
        TaiInstant t5 = TaiInstant.ofTaiSeconds(1, 0);

        assertComparisonContracts(t1, t2, t3, t4, t5);
    }

    /**
     * Asserts that comparison contracts are met for a sorted array of instants.
     */
    private void assertComparisonContracts(TaiInstant... instants) {
        for (int i = 0; i < instants.length; i++) {
            TaiInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                TaiInstant b = instants[j];
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
}