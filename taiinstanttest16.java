package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.testing.EqualsTester;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link TaiInstant}.
 */
@DisplayName("TaiInstant")
public class TaiInstantTest {

    private static final TaiInstant TAI_ZERO = TaiInstant.ofTaiSeconds(0, 0);

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @DisplayName("parse() should throw exception for invalid formats")
    @ParameterizedTest(name = "Parsing \"{0}\"")
    @MethodSource("invalidParseStrings")
    public void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    static Stream<String> invalidParseStrings() {
        return Stream.of(
                "A.123456789s(TAI)",      // Non-numeric seconds
                "123.12345678As(TAI)",    // Non-numeric nanos
                "123.123456789",          // Missing suffix
                "123.123456789s",         // Missing (TAI)
                "+123.123456789s(TAI)",   // Explicit plus sign not allowed
                "-123.123s(TAI)"          // Nanos must be 9 digits
        );
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @DisplayName("withTaiSeconds() should update seconds and preserve nanoseconds")
    @ParameterizedTest(name = "of({0}, {1}).withTaiSeconds({2})")
    @MethodSource
    public void withTaiSeconds_updatesSecondsAndPreservesNanos(long initialSeconds, long initialNanos, long newSeconds) {
        TaiInstant start = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = start.withTaiSeconds(newSeconds);

        assertEquals(newSeconds, result.getTaiSeconds());
        assertEquals(initialNanos, result.getNano());
    }

    static Stream<Arguments> withTaiSeconds_updatesSecondsAndPreservesNanos() {
        return Stream.of(
                Arguments.of(0L, 12345L, 1L),
                Arguments.of(0L, 12345L, -1L),
                Arguments.of(7L, 12345L, 2L),
                Arguments.of(-99L, 12345L, 3L)
        );
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("withNano() should update nanoseconds")
    public void withNano_updatesNanos() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 50);
        TaiInstant result = start.withNano(123);
        
        TaiInstant expected = TaiInstant.ofTaiSeconds(10, 123);
        assertEquals(expected, result);
    }

    @DisplayName("withNano() should throw exception for out-of-range values")
    @ParameterizedTest(name = "Nano value: {0}")
    @ValueSource(ints = {-1, 1_000_000_000})
    public void withNano_withInvalidValue_throwsIllegalArgumentException(int invalidNano) {
        assertThrows(IllegalArgumentException.class, () -> TAI_ZERO.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    @DisplayName("plus() should correctly add durations")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    public void plus_addsDurationCorrectly(String testName, TaiInstant start, Duration toAdd, TaiInstant expected) {
        TaiInstant result = start.plus(toAdd);
        assertEquals(expected, result);
    }

    static Stream<Arguments> plus_addsDurationCorrectly() {
        long ONE_THIRD_NANO = 333_333_333L;
        long TWO_THIRDS_NANO = 666_666_667L;

        return Stream.of(
                Arguments.of(
                        "Simple addition",
                        TaiInstant.ofTaiSeconds(5, 100),
                        Duration.ofSeconds(3, 200),
                        TaiInstant.ofTaiSeconds(8, 300)
                ),
                Arguments.of(
                        "Addition with nano carry-over",
                        TaiInstant.ofTaiSeconds(5, 800_000_000),
                        Duration.ofSeconds(3, 300_000_000),
                        TaiInstant.ofTaiSeconds(9, 100_000_000)
                ),
                Arguments.of(
                        "Addition with negative duration and nano borrow",
                        TaiInstant.ofTaiSeconds(5, 200_000_000),
                        Duration.ofSeconds(-3, -400_000_000),
                        TaiInstant.ofTaiSeconds(1, 800_000_000)
                ),
                Arguments.of(
                        "Addition involving fractional seconds",
                        TaiInstant.ofTaiSeconds(-4, TWO_THIRDS_NANO),
                        Duration.ofSeconds(3, ONE_THIRD_NANO),
                        TaiInstant.ofTaiSeconds(0, 0)
                ),
                Arguments.of(
                        "Boundary: MIN_VALUE + MAX_VALUE",
                        TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0),
                        Duration.ofSeconds(Long.MAX_VALUE, 0),
                        TaiInstant.ofTaiSeconds(-1, 0)
                )
        );
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    @DisplayName("minus() should correctly subtract durations")
    @ParameterizedTest(name = "{0}")
    @MethodSource
    public void minus_subtractsDurationCorrectly(String testName, TaiInstant start, Duration toSubtract, TaiInstant expected) {
        TaiInstant result = start.minus(toSubtract);
        assertEquals(expected, result);
    }

    static Stream<Arguments> minus_subtractsDurationCorrectly() {
        long ONE_THIRD_NANO = 333_333_333L;
        long TWO_THIRDS_NANO = 666_666_667L;

        return Stream.of(
                Arguments.of(
                        "Simple subtraction",
                        TaiInstant.ofTaiSeconds(8, 300),
                        Duration.ofSeconds(3, 200),
                        TaiInstant.ofTaiSeconds(5, 100)
                ),
                Arguments.of(
                        "Subtraction with nano borrow",
                        TaiInstant.ofTaiSeconds(9, 100_000_000),
                        Duration.ofSeconds(3, 300_000_000),
                        TaiInstant.ofTaiSeconds(5, 800_000_000)
                ),
                Arguments.of(
                        "Subtraction of negative duration (addition)",
                        TaiInstant.ofTaiSeconds(2, 300_000_000),
                        Duration.ofSeconds(-3, -200_000_000),
                        TaiInstant.ofTaiSeconds(5, 500_000_000)
                ),
                Arguments.of(
                        "Subtraction involving fractional seconds",
                        TaiInstant.ofTaiSeconds(0, 0),
                        Duration.ofSeconds(3, ONE_THIRD_NANO),
                        TaiInstant.ofTaiSeconds(-4, TWO_THIRDS_NANO)
                ),
                Arguments.of(
                        "Boundary: MAX_VALUE - MAX_VALUE",
                        TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0),
                        Duration.ofSeconds(Long.MAX_VALUE, 0),
                        TaiInstant.ofTaiSeconds(0, 0)
                )
        );
    }

    //-----------------------------------------------------------------------
    // durationUntil()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("durationUntil() should calculate positive duration correctly")
    public void durationUntil_forwardsInTime_returnsPositiveDuration() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 0);
        TaiInstant end = TaiInstant.ofTaiSeconds(25, 500_000_000);
        
        Duration duration = start.durationUntil(end);
        
        assertEquals(Duration.ofSeconds(15, 500_000_000), duration);
    }

    @Test
    @DisplayName("durationUntil() should calculate negative duration correctly")
    public void durationUntil_backwardsInTime_returnsNegativeDuration() {
        TaiInstant start = TaiInstant.ofTaiSeconds(25, 500_000_000);
        TaiInstant end = TaiInstant.ofTaiSeconds(10, 0);
        
        Duration duration = start.durationUntil(end);
        
        assertEquals(Duration.ofSeconds(-15, -500_000_000), duration);
    }

    //-----------------------------------------------------------------------
    // Comparisons
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("Comparison methods (compareTo, isAfter, isBefore) should be consistent")
    public void comparisonMethods_areConsistent() {
        TaiInstant[] instants = new TaiInstant[] {
            TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0),
            TaiInstant.ofTaiSeconds(-1, 0),
            TaiInstant.ofTaiSeconds(-1, 999_999_999),
            TaiInstant.ofTaiSeconds(0, 0),
            TaiInstant.ofTaiSeconds(0, 1),
            TaiInstant.ofTaiSeconds(1, 0),
            TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 999_999_999)
        };
        assertConsistentComparisons(instants);
    }

    /**
     * Asserts that comparison methods are consistent for a given sorted array of distinct instants.
     */
    private void assertConsistentComparisons(TaiInstant... instants) {
        for (int i = 0; i < instants.length; i++) {
            TaiInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                TaiInstant b = instants[j];
                if (i < j) {
                    assertTrue(a.compareTo(b) < 0, a + " should be less than " + b);
                    assertFalse(a.equals(b));
                    assertTrue(a.isBefore(b), a + " should be before " + b);
                    assertFalse(a.isAfter(b), a + " should not be after " + b);
                } else if (i > j) {
                    assertTrue(a.compareTo(b) > 0, a + " should be greater than " + b);
                    assertFalse(a.equals(b));
                    assertFalse(a.isBefore(b), a + " should not be before " + b);
                    assertTrue(a.isAfter(b), a + " should be after " + b);
                } else { // i == j
                    assertEquals(0, a.compareTo(b), a + " should be equal to " + b);
                    assertTrue(a.equals(b));
                    assertFalse(a.isBefore(b), a + " should not be before " + b);
                    assertFalse(a.isAfter(b), a + " should not be after " + b);
                }
            }
        }
    }

    //-----------------------------------------------------------------------
    // equals() and hashCode()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("equals() and hashCode() should adhere to contract")
    public void equals_and_hashCode_shouldAdhereToContract() {
        new EqualsTester()
            .addEqualityGroup(
                TaiInstant.ofTaiSeconds(1, 0),
                TaiInstant.ofTaiSeconds(1, 0))
            .addEqualityGroup(
                TaiInstant.ofTaiSeconds(2, 0))
            .addEqualityGroup(
                TaiInstant.ofTaiSeconds(1, 1))
            .testEquals();
    }
}