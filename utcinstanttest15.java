package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import com.google.common.testing.EqualsTester;

public class UtcInstantTestTest15 {

    private static final long MJD_1972_12_30 = 41681;

    private static final long MJD_1972_12_31_LEAP = 41682;

    private static final long MJD_1973_01_01 = 41683;

    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;

    private static final long NANOS_PER_SEC = 1000000000L;

    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;

    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    public static Object[][] data_badParse() {
        return new Object[][] { { "" }, { "A" }, // bad month
        { "2012-13-01T00:00:00Z" } };
    }

    @ParameterizedTest
    @MethodSource("data_badParse")
    public void factory_parse_CharSequence_invalid(String str) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(str));
    }

    //-----------------------------------------------------------------------
    public static Object[][] data_withModifiedJulianDay() {
        return new Object[][] { { 0L, 12345L, 1L, 1L, 12345L }, { 0L, 12345L, -1L, -1L, 12345L }, { 7L, 12345L, 2L, 2L, 12345L }, { 7L, 12345L, -2L, -2L, 12345L }, { -99L, 12345L, 3L, 3L, 12345L }, { -99L, 12345L, -3L, -3L, 12345L }, { MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30, null, null }, { MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY }, { MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01, null, null }, { MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY } };
    }

    @ParameterizedTest
    @MethodSource("data_withModifiedJulianDay")
    public void test_withModifiedJulianDay(long mjd, long nanos, long newMjd, Long expectedMjd, Long expectedNanos) {
        UtcInstant i = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        if (expectedMjd != null) {
            UtcInstant withModifiedJulianDay = i.withModifiedJulianDay(newMjd);
            assertEquals(expectedMjd.longValue(), withModifiedJulianDay.getModifiedJulianDay());
            assertEquals(expectedNanos.longValue(), withModifiedJulianDay.getNanoOfDay());
        } else {
            assertThrows(DateTimeException.class, () -> i.withModifiedJulianDay(newMjd));
        }
    }

    //-----------------------------------------------------------------------
    public static Object[][] data_withNanoOfDay() {
        return new Object[][] { { 0L, 12345L, 1L, 0L, 1L }, { 0L, 12345L, -1L, null, null }, { 7L, 12345L, 2L, 7L, 2L }, { -99L, 12345L, 3L, -99L, 3L }, { MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1 }, { MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1 }, { MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1973_01_01, NANOS_PER_DAY - 1 }, { MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY, null, null }, { MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY }, { MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY, null, null }, { MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, null, null }, { MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1 }, { MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, null, null }, { MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null }, { MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null }, { MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null } };
    }

    @ParameterizedTest
    @MethodSource("data_withNanoOfDay")
    public void test_withNanoOfDay(long mjd, long nanos, long newNanoOfDay, Long expectedMjd, Long expectedNanos) {
        UtcInstant i = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        if (expectedMjd != null) {
            UtcInstant withNanoOfDay = i.withNanoOfDay(newNanoOfDay);
            assertEquals(expectedMjd.longValue(), withNanoOfDay.getModifiedJulianDay());
            assertEquals(expectedNanos.longValue(), withNanoOfDay.getNanoOfDay());
        } else {
            assertThrows(DateTimeException.class, () -> i.withNanoOfDay(newNanoOfDay));
        }
    }

    //-----------------------------------------------------------------------
    public static Object[][] data_plus() {
        return new Object[][] { { 0, 0, -2 * SECS_PER_DAY, 5, -2, 5 }, { 0, 0, -1 * SECS_PER_DAY, 1, -1, 1 }, { 0, 0, -1 * SECS_PER_DAY, 0, -1, 0 }, { 0, 0, 0, -2, -1, NANOS_PER_DAY - 2 }, { 0, 0, 0, -1, -1, NANOS_PER_DAY - 1 }, { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 1 }, { 0, 0, 0, 2, 0, 2 }, { 0, 0, 1, 0, 0, 1 * NANOS_PER_SEC }, { 0, 0, 2, 0, 0, 2 * NANOS_PER_SEC }, { 0, 0, 3, 333333333, 0, 3 * NANOS_PER_SEC + 333333333 }, { 0, 0, 1 * SECS_PER_DAY, 0, 1, 0 }, { 0, 0, 1 * SECS_PER_DAY, 1, 1, 1 }, { 0, 0, 2 * SECS_PER_DAY, 5, 2, 5 }, { 1, 0, -2 * SECS_PER_DAY, 5, -1, 5 }, { 1, 0, -1 * SECS_PER_DAY, 1, 0, 1 }, { 1, 0, -1 * SECS_PER_DAY, 0, 0, 0 }, { 1, 0, 0, -2, 0, NANOS_PER_DAY - 2 }, { 1, 0, 0, -1, 0, NANOS_PER_DAY - 1 }, { 1, 0, 0, 0, 1, 0 }, { 1, 0, 0, 1, 1, 1 }, { 1, 0, 0, 2, 1, 2 }, { 1, 0, 1, 0, 1, 1 * NANOS_PER_SEC }, { 1, 0, 2, 0, 1, 2 * NANOS_PER_SEC }, { 1, 0, 3, 333333333, 1, 3 * NANOS_PER_SEC + 333333333 }, { 1, 0, 1 * SECS_PER_DAY, 0, 2, 0 }, { 1, 0, 1 * SECS_PER_DAY, 1, 2, 1 }, { 1, 0, 2 * SECS_PER_DAY, 5, 3, 5 } };
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    public void test_plus(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant i = UtcInstant.ofModifiedJulianDay(mjd, nanos).plus(Duration.ofSeconds(plusSeconds, plusNanos));
        assertEquals(expectedMjd, i.getModifiedJulianDay());
        assertEquals(expectedNanos, i.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    public static Object[][] data_minus() {
        return new Object[][] { { 0, 0, 2 * SECS_PER_DAY, -5, -2, 5 }, { 0, 0, 1 * SECS_PER_DAY, -1, -1, 1 }, { 0, 0, 1 * SECS_PER_DAY, 0, -1, 0 }, { 0, 0, 0, 2, -1, NANOS_PER_DAY - 2 }, { 0, 0, 0, 1, -1, NANOS_PER_DAY - 1 }, { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, -1, 0, 1 }, { 0, 0, 0, -2, 0, 2 }, { 0, 0, -1, 0, 0, 1 * NANOS_PER_SEC }, { 0, 0, -2, 0, 0, 2 * NANOS_PER_SEC }, { 0, 0, -3, -333333333, 0, 3 * NANOS_PER_SEC + 333333333 }, { 0, 0, -1 * SECS_PER_DAY, 0, 1, 0 }, { 0, 0, -1 * SECS_PER_DAY, -1, 1, 1 }, { 0, 0, -2 * SECS_PER_DAY, -5, 2, 5 }, { 1, 0, 2 * SECS_PER_DAY, -5, -1, 5 }, { 1, 0, 1 * SECS_PER_DAY, -1, 0, 1 }, { 1, 0, 1 * SECS_PER_DAY, 0, 0, 0 }, { 1, 0, 0, 2, 0, NANOS_PER_DAY - 2 }, { 1, 0, 0, 1, 0, NANOS_PER_DAY - 1 }, { 1, 0, 0, 0, 1, 0 }, { 1, 0, 0, -1, 1, 1 }, { 1, 0, 0, -2, 1, 2 }, { 1, 0, -1, 0, 1, 1 * NANOS_PER_SEC }, { 1, 0, -2, 0, 1, 2 * NANOS_PER_SEC }, { 1, 0, -3, -333333333, 1, 3 * NANOS_PER_SEC + 333333333 }, { 1, 0, -1 * SECS_PER_DAY, 0, 2, 0 }, { 1, 0, -1 * SECS_PER_DAY, -1, 2, 1 }, { 1, 0, -2 * SECS_PER_DAY, -5, 3, 5 } };
    }

    @ParameterizedTest
    @MethodSource("data_minus")
    public void test_minus(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant i = UtcInstant.ofModifiedJulianDay(mjd, nanos).minus(Duration.ofSeconds(minusSeconds, minusNanos));
        assertEquals(expectedMjd, i.getModifiedJulianDay());
        assertEquals(expectedNanos, i.getNanoOfDay());
    }

    void doTest_comparisons_UtcInstant(UtcInstant... instants) {
        for (int i = 0; i < instants.length; i++) {
            UtcInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                UtcInstant b = instants[j];
                if (i < j) {
                    assertEquals(-1, a.compareTo(b));
                    assertEquals(false, a.equals(b));
                    assertTrue(a.isBefore(b));
                    assertFalse(a.isAfter(b));
                } else if (i > j) {
                    assertEquals(1, a.compareTo(b));
                    assertEquals(false, a.equals(b));
                    assertFalse(a.isBefore(b));
                    assertTrue(a.isAfter(b));
                } else {
                    assertEquals(0, a.compareTo(b));
                    assertEquals(true, a.equals(b));
                    assertFalse(a.isBefore(b));
                    assertFalse(a.isAfter(b));
                }
            }
        }
    }

    //-----------------------------------------------------------------------
    public static Object[][] data_toString() {
        return new Object[][] { { 40587, 0, "1970-01-01T00:00:00Z" }, { 40588, 1, "1970-01-02T00:00:00.000000001Z" }, { 40588, 999, "1970-01-02T00:00:00.000000999Z" }, { 40588, 1000, "1970-01-02T00:00:00.000001Z" }, { 40588, 999000, "1970-01-02T00:00:00.000999Z" }, { 40588, 1000000, "1970-01-02T00:00:00.001Z" }, { 40618, 999999999, "1970-02-01T00:00:00.999999999Z" }, { 40619, 1000000000, "1970-02-02T00:00:01Z" }, { 40620, 60L * 1000000000L, "1970-02-03T00:01:00Z" }, { 40621, 60L * 60L * 1000000000L, "1970-02-04T01:00:00Z" }, { MJD_1972_12_31_LEAP, 24L * 60L * 60L * 1000000000L - 1000000000L, "1972-12-31T23:59:59Z" }, { MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z" }, { MJD_1973_01_01, 0, "1973-01-01T00:00:00Z" } };
    }

    @ParameterizedTest
    @MethodSource("data_toString")
    public void test_toString(long mjd, long nod, String expected) {
        assertEquals(expected, UtcInstant.ofModifiedJulianDay(mjd, nod).toString());
    }

    @ParameterizedTest
    @MethodSource("data_toString")
    public void test_toString_parse(long mjd, long nod, String str) {
        assertEquals(UtcInstant.ofModifiedJulianDay(mjd, nod), UtcInstant.parse(str));
    }

    @Test
    public void factory_parse_CharSequence_invalidLeapSecond() {
        // leap second but not leap day
        assertThrows(DateTimeException.class, () -> UtcInstant.parse("1972-11-11T23:59:60Z"));
    }
}
