package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("TaiInstant Tests")
class TaiInstantTest {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------

    static Stream<String> invalidParseStrings() {
        return Stream.of(
            "A.123456789s(TAI)",      // non-digit seconds
            "123.12345678As(TAI)",     // non-digit nanos
            "123.123456789",           // missing suffix
            "123.123456789s",          // missing (TAI)
            "+123.123456789s(TAI)",    // explicit plus sign not allowed
            "-123.123s(TAI)"           // nanos must be 9 digits
        );
    }

    @ParameterizedTest
    @MethodSource("invalidParseStrings")
    @DisplayName("parse() should throw for invalid formats")
    void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("withTaiSeconds() should update seconds and preserve nanos")
    void withTaiSeconds_returnsNewInstanceWithUpdatedSeconds() {
        TaiInstant initial = TaiInstant.ofTaiSeconds(100, 12345);
        long newSeconds = 200L;

        TaiInstant result = initial.withTaiSeconds(newSeconds);

        assertEquals(newSeconds, result.getTaiSeconds());
        assertEquals(initial.getNano(), result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @CsvSource({
        "0",
        "12345",
        "999999999"
    })
    @DisplayName("withNano() should update nanos and preserve seconds for valid input")
    void withNano_givenValidNano_returnsNewInstanceWithUpdatedNanos(int newNano) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(100, 54321);

        TaiInstant result = initial.withNano(newNano);

        assertEquals(initial.getTaiSeconds(), result.getTaiSeconds());
        assertEquals(newNano, result.getNano());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_000_000})
    @DisplayName("withNano() should throw for out-of-range nanos")
    void withNano_givenInvalidNano_throwsIllegalArgumentException(int invalidNano) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(100, 12345);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------

    @ParameterizedTest(name = "({0}s, {1}ns) + ({2}s, {3}ns) = ({4}s, {5}ns)")
    @CsvSource({
        // s,           ns,     plus_s,     plus_ns, expected_s, expected_ns
        "  0,            0,          1,           0,          1,           0", // Simple second addition
        "  1,            0,         -1,           0,          0,           0", // Subtracting seconds
        "  0,            1,          0,           1,          0,           2", // Simple nano addition
        "  0,    999999999,          0,           1,          1,           0", // Nano overflow
        "  1,            0,          0,          -1,          0,   999999999", // Nano underflow (borrow)
        " -4,    666666667,          0,   333333333,         -3,           0", // Nano overflow to zero
        "  3,    333333333,          3,   333333333,          6,   666666666"  // Addition of fractional seconds
    })
    @DisplayName("plus() should correctly add durations")
    void plus_addsDurationCorrectly(long s, int ns, long plus_s, int plus_ns, long expected_s, int expected_ns) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(s, ns);
        Duration toAdd = Duration.ofSeconds(plus_s, plus_ns);

        TaiInstant result = initial.plus(toAdd);

        assertEquals(expected_s, result.getTaiSeconds());
        assertEquals(expected_ns, result.getNano());
    }

    @Test
    @DisplayName("plus() should throw ArithmeticException on overflow")
    void plus_throwsOnOverflow() {
        TaiInstant max = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> max.plus(Duration.ofSeconds(1)));

        TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> min.plus(Duration.ofSeconds(-1)));
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------

    @ParameterizedTest(name = "({0}s, {1}ns) - ({2}s, {3}ns) = ({4}s, {5}ns)")
    @CsvSource({
        // s,           ns,    minus_s,    minus_ns, expected_s, expected_ns
        "  5,    500000000,          2,   100000000,          3,   400000000", // Simple subtraction
        "  5,    100000000,          2,   200000000,          2,   900000000", // Subtraction with nano borrow
        "  5,    100000000,         -2,  -100000000,          7,   200000000", // Subtracting negative (addition)
        "  5,    600000000,         -2,  -500000000,          8,   100000000", // Subtracting negative with nano carry
        " -1,    666666667,         -1,   666666667,          0,           0"  // Subtracting itself
    })
    @DisplayName("minus() should correctly subtract durations")
    void minus_subtractsDurationCorrectly(long s, int ns, long minus_s, int minus_ns, long expected_s, int expected_ns) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(s, ns);
        Duration toSubtract = Duration.ofSeconds(minus_s, minus_ns);

        TaiInstant result = initial.minus(toSubtract);

        assertEquals(expected_s, result.getTaiSeconds());
        assertEquals(expected_ns, result.getNano());
    }

    @Test
    @DisplayName("minus() should throw ArithmeticException on overflow")
    void minus_throwsOnOverflow() {
        TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> min.minus(Duration.ofSeconds(1)));

        TaiInstant max = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> max.minus(Duration.ofSeconds(-1)));
    }

    //-----------------------------------------------------------------------
    // toUtcInstant()
    //-----------------------------------------------------------------------

    private static final long TAI_EPOCH_MJD = 36204L; // Modified Julian Date for 1958-01-01
    private static final long SECONDS_PER_DAY = 24 * 60 * 60;
    // This test assumes a fixed 10-second difference between TAI and UTC,
    // which was the case from 1972-01-01 until the first leap second was added.
    private static final long TAI_UTC_DIFFERENCE_SECONDS = 10;

    @ParameterizedTest(name = "Days from epoch: {0}, Seconds in day: {1}")
    @CsvSource({
        // days_from_epoch, seconds_in_day
        "           0,            0",
        "         500,            5",
        "        -500,            9",
        "        1000,        86399" // Test near end of day
    })
    @DisplayName("toUtcInstant() should correctly convert TAI to UTC assuming a fixed offset")
    void toUtcInstant_convertsCorrectly(int daysFromEpoch, int secondsInDay) {
        long nanoOfSecond = 12345L;
        long nanosOfDay = secondsInDay * 1_000_000_000L + nanoOfSecond;

        // The expected UTC instant, calculated from Modified Julian Date and nanosecond-of-day.
        UtcInstant expectedUtc = UtcInstant.ofModifiedJulianDay(TAI_EPOCH_MJD + daysFromEpoch, nanosOfDay);

        // The TAI instant to be tested. Its value in seconds is based on days from epoch,
        // plus an assumed fixed offset from UTC.
        long taiSeconds = daysFromEpoch * SECONDS_PER_DAY + secondsInDay + TAI_UTC_DIFFERENCE_SECONDS;
        TaiInstant testTai = TaiInstant.ofTaiSeconds(taiSeconds, nanoOfSecond);

        assertEquals(expectedUtc, testTai.toUtcInstant());
    }
}