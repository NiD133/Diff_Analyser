package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.time.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the UtcInstant class.
 */
public class TestUtcInstant {

    // Constants for Modified Julian Days (MJD) and nanoseconds
    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682;
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;
    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    // Test if UtcInstant implements Serializable and Comparable interfaces
    @Test
    public void test_interfaces() {
        assertTrue(Serializable.class.isAssignableFrom(UtcInstant.class));
        assertTrue(Comparable.class.isAssignableFrom(UtcInstant.class));
    }

    // Test serialization and deserialization of UtcInstant
    @Test
    public void test_serialization() throws Exception {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(2, 3);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(original);
        }
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()))) {
            UtcInstant deserialized = (UtcInstant) objectInputStream.readObject();
            assertEquals(original, deserialized);
        }
    }

    // Test factory method for creating UtcInstant with Modified Julian Day and nanoseconds
    @Test
    public void test_factory_ofModifiedJulianDay() {
        for (long day = -2; day <= 2; day++) {
            for (int nanos = 0; nanos < 10; nanos++) {
                UtcInstant instant = UtcInstant.ofModifiedJulianDay(day, nanos);
                assertEquals(day, instant.getModifiedJulianDay());
                assertEquals(nanos, instant.getNanoOfDay());
                assertFalse(instant.isLeapSecond());
            }
        }
    }

    // Test UtcInstant creation at the end of a normal day
    @Test
    public void test_factory_endNormalDay() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1);
        assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
        assertEquals(NANOS_PER_DAY - 1, instant.getNanoOfDay());
        assertFalse(instant.isLeapSecond());
        assertEquals("1972-12-31T23:59:59.999999999Z", instant.toString());
    }

    // Test UtcInstant creation at the start of a leap second
    @Test
    public void test_factory_startLeapSecond() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY);
        assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
        assertEquals(NANOS_PER_DAY, instant.getNanoOfDay());
        assertTrue(instant.isLeapSecond());
        assertEquals("1972-12-31T23:59:60Z", instant.toString());
    }

    // Test UtcInstant creation at the end of a leap second
    @Test
    public void test_factory_endLeapSecond() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1);
        assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
        assertEquals(NANOS_PER_LEAP_DAY - 1, instant.getNanoOfDay());
        assertTrue(instant.isLeapSecond());
        assertEquals("1972-12-31T23:59:60.999999999Z", instant.toString());
    }

    // Test UtcInstant creation with negative nanoseconds
    @Test
    public void test_factory_negativeNanos() {
        assertThrows(DateTimeException.class, () -> UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, -1));
    }

    // Test UtcInstant creation with too large nanoseconds on a non-leap day
    @Test
    public void test_factory_nanosTooBig_nonLeapDay() {
        assertThrows(DateTimeException.class, () -> UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, NANOS_PER_DAY));
    }

    // Test UtcInstant creation with too large nanoseconds on a leap day
    @Test
    public void test_factory_nanosTooBig_leapDay() {
        assertThrows(DateTimeException.class, () -> UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY));
    }

    // Test UtcInstant creation from an Instant
    @Test
    public void test_factory_fromInstant() {
        UtcInstant instant = UtcInstant.of(Instant.ofEpochSecond(0, 2));  // 1970-01-01
        assertEquals(40587, instant.getModifiedJulianDay());
        assertEquals(2, instant.getNanoOfDay());
    }

    // Test UtcInstant creation from a null Instant
    @Test
    public void test_factory_fromNullInstant() {
        assertThrows(NullPointerException.class, () -> UtcInstant.of((Instant) null));
    }

    // Test UtcInstant creation from a TaiInstant
    @Test
    public void test_factory_fromTaiInstant() {
        for (int i = -1000; i < 1000; i++) {
            for (int j = 0; j < 10; j++) {
                UtcInstant expected = UtcInstant.ofModifiedJulianDay(36204 + i, j * NANOS_PER_SEC + 2L);
                TaiInstant tai = TaiInstant.ofTaiSeconds(i * SECS_PER_DAY + j + 10, 2);
                assertEquals(expected, UtcInstant.of(tai));
            }
        }
    }

    // Test UtcInstant creation from a null TaiInstant
    @Test
    public void test_factory_fromNullTaiInstant() {
        assertThrows(NullPointerException.class, () -> UtcInstant.of((TaiInstant) null));
    }

    // Test UtcInstant parsing from a valid string
    @Test
    public void test_factory_parseValidString() {
        assertEquals(UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC), UtcInstant.parse("1972-12-31T23:59:59Z"));
        assertEquals(UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY), UtcInstant.parse("1972-12-31T23:59:60Z"));
    }

    // Data provider for invalid parse strings
    public static Object[][] data_invalidParseStrings() {
        return new Object[][] {
            {""},
            {"A"},
            {"2012-13-01T00:00:00Z"},  // Invalid month
        };
    }

    // Test UtcInstant parsing from invalid strings
    @ParameterizedTest
    @MethodSource("data_invalidParseStrings")
    public void test_factory_parseInvalidString(String str) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(str));
    }

    // Test UtcInstant parsing from an invalid leap second string
    @Test
    public void test_factory_parseInvalidLeapSecond() {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse("1972-11-11T23:59:60Z")); // Invalid leap second
    }

    // Test UtcInstant parsing from a null string
    @Test
    public void test_factory_parseNullString() {
        assertThrows(NullPointerException.class, () -> UtcInstant.parse((String) null));
    }

    // Data provider for withModifiedJulianDay tests
    public static Object[][] data_withModifiedJulianDay() {
        return new Object[][] {
            {0L, 12345L, 1L, 1L, 12345L},
            {0L, 12345L, -1L, -1L, 12345L},
            {7L, 12345L, 2L, 2L, 12345L},
            {7L, 12345L, -2L, -2L, 12345L},
            {-99L, 12345L, 3L, 3L, 12345L},
            {-99L, 12345L, -3L, -3L, 12345L},
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30, null, null},
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY},
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01, null, null},
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY},
        };
    }

    // Test withModifiedJulianDay method
    @ParameterizedTest
    @MethodSource("data_withModifiedJulianDay")
    public void test_withModifiedJulianDay(long mjd, long nanos, long newMjd, Long expectedMjd, Long expectedNanos) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        if (expectedMjd != null) {
            UtcInstant modifiedInstant = instant.withModifiedJulianDay(newMjd);
            assertEquals(expectedMjd.longValue(), modifiedInstant.getModifiedJulianDay());
            assertEquals(expectedNanos.longValue(), modifiedInstant.getNanoOfDay());
        } else {
            assertThrows(DateTimeException.class, () -> instant.withModifiedJulianDay(newMjd));
        }
    }

    // Data provider for withNanoOfDay tests
    public static Object[][] data_withNanoOfDay() {
        return new Object[][] {
            {0L, 12345L, 1L, 0L, 1L},
            {0L, 12345L, -1L, null, null},
            {7L, 12345L, 2L, 7L, 2L},
            {-99L, 12345L, 3L, -99L, 3L},
            {MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1},
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1},
            {MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1973_01_01, NANOS_PER_DAY - 1},
            {MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY, null, null},
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY},
            {MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY, null, null},
            {MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, null, null},
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1},
            {MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, null, null},
            {MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null},
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null},
            {MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null},
        };
    }

    // Test withNanoOfDay method
    @ParameterizedTest
    @MethodSource("data_withNanoOfDay")
    public void test_withNanoOfDay(long mjd, long nanos, long newNanoOfDay, Long expectedMjd, Long expectedNanos) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        if (expectedMjd != null) {
            UtcInstant modifiedInstant = instant.withNanoOfDay(newNanoOfDay);
            assertEquals(expectedMjd.longValue(), modifiedInstant.getModifiedJulianDay());
            assertEquals(expectedNanos.longValue(), modifiedInstant.getNanoOfDay());
        } else {
            assertThrows(DateTimeException.class, () -> instant.withNanoOfDay(newNanoOfDay));
        }
    }

    // Data provider for plus method tests
    public static Object[][] data_plus() {
        return new Object[][] {
            {0, 0,  -2 * SECS_PER_DAY, 5, -2, 5},
            {0, 0,  -1 * SECS_PER_DAY, 1, -1, 1},
            {0, 0,  -1 * SECS_PER_DAY, 0, -1, 0},
            {0, 0,  0,        -2, -1,  NANOS_PER_DAY - 2},
            {0, 0,  0,        -1, -1,  NANOS_PER_DAY - 1},
            {0, 0,  0,         0,  0,  0},
            {0, 0,  0,         1,  0,  1},
            {0, 0,  0,         2,  0,  2},
            {0, 0,  1,         0,  0,  1 * NANOS_PER_SEC},
            {0, 0,  2,         0,  0,  2 * NANOS_PER_SEC},
            {0, 0,  3, 333333333,  0,  3 * NANOS_PER_SEC + 333333333},
            {0, 0,  1 * SECS_PER_DAY, 0,  1, 0},
            {0, 0,  1 * SECS_PER_DAY, 1,  1, 1},
            {0, 0,  2 * SECS_PER_DAY, 5,  2, 5},

            {1, 0,  -2 * SECS_PER_DAY, 5, -1, 5},
            {1, 0,  -1 * SECS_PER_DAY, 1, 0, 1},
            {1, 0,  -1 * SECS_PER_DAY, 0, 0, 0},
            {1, 0,  0,        -2,  0,  NANOS_PER_DAY - 2},
            {1, 0,  0,        -1,  0,  NANOS_PER_DAY - 1},
            {1, 0,  0,         0,  1,  0},
            {1, 0,  0,         1,  1,  1},
            {1, 0,  0,         2,  1,  2},
            {1, 0,  1,         0,  1,  1 * NANOS_PER_SEC},
            {1, 0,  2,         0,  1,  2 * NANOS_PER_SEC},
            {1, 0,  3, 333333333,  1,  3 * NANOS_PER_SEC + 333333333},
            {1, 0,  1 * SECS_PER_DAY, 0,  2, 0},
            {1, 0,  1 * SECS_PER_DAY, 1,  2, 1},
            {1, 0,  2 * SECS_PER_DAY, 5,  3, 5},
        };
    }

    // Test plus method
    @ParameterizedTest
    @MethodSource("data_plus")
    public void test_plus(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanos).plus(Duration.ofSeconds(plusSeconds, plusNanos));
        assertEquals(expectedMjd, instant.getModifiedJulianDay());
        assertEquals(expectedNanos, instant.getNanoOfDay());
    }

    // Test plus method overflow
    @Test
    public void test_plus_overflowTooBig() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, NANOS_PER_DAY - 1);
        assertThrows(ArithmeticException.class, () -> instant.plus(Duration.ofNanos(1)));
    }

    // Test plus method underflow
    @Test
    public void test_plus_overflowTooSmall() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> instant.plus(Duration.ofNanos(-1)));
    }

    // Data provider for minus method tests
    public static Object[][] data_minus() {
        return new Object[][] {
            {0, 0,  2 * SECS_PER_DAY, -5, -2, 5},
            {0, 0,  1 * SECS_PER_DAY, -1, -1, 1},
            {0, 0,  1 * SECS_PER_DAY, 0, -1, 0},
            {0, 0,  0,          2, -1,  NANOS_PER_DAY - 2},
            {0, 0,  0,          1, -1,  NANOS_PER_DAY - 1},
            {0, 0,  0,          0,  0,  0},
            {0, 0,  0,         -1,  0,  1},
            {0, 0,  0,         -2,  0,  2},
            {0, 0,  -1,         0,  0,  1 * NANOS_PER_SEC},
            {0, 0,  -2,         0,  0,  2 * NANOS_PER_SEC},
            {0, 0,  -3, -333333333,  0,  3 * NANOS_PER_SEC + 333333333},
            {0, 0,  -1 * SECS_PER_DAY, 0,  1, 0},
            {0, 0,  -1 * SECS_PER_DAY, -1,  1, 1},
            {0, 0,  -2 * SECS_PER_DAY, -5,  2, 5},

            {1, 0,  2 * SECS_PER_DAY, -5, -1, 5},
            {1, 0,  1 * SECS_PER_DAY, -1, 0, 1},
            {1, 0,  1 * SECS_PER_DAY, 0, 0, 0},
            {1, 0,  0,          2,  0,  NANOS_PER_DAY - 2},
            {1, 0,  0,          1,  0,  NANOS_PER_DAY - 1},
            {1, 0,  0,          0,  1,  0},
            {1, 0,  0,         -1,  1,  1},
            {1, 0,  0,         -2,  1,  2},
            {1, 0,  -1,         0,  1,  1 * NANOS_PER_SEC},
            {1, 0,  -2,         0,  1,  2 * NANOS_PER_SEC},
            {1, 0,  -3, -333333333,  1,  3 * NANOS_PER_SEC + 333333333},
            {1, 0,  -1 * SECS_PER_DAY, 0,  2, 0},
            {1, 0,  -1 * SECS_PER_DAY, -1,  2, 1},
            {1, 0,  -2 * SECS_PER_DAY, -5,  3, 5},
        };
    }

    // Test minus method
    @ParameterizedTest
    @MethodSource("data_minus")
    public void test_minus(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanos).minus(Duration.ofSeconds(minusSeconds, minusNanos));
        assertEquals(expectedMjd, instant.getModifiedJulianDay());
        assertEquals(expectedNanos, instant.getNanoOfDay());
    }

    // Test minus method underflow
    @Test
    public void test_minus_overflowTooSmall() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> instant.minus(Duration.ofNanos(1)));
    }

    // Test minus method overflow
    @Test
    public void test_minus_overflowTooBig() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, NANOS_PER_DAY - 1);
        assertThrows(ArithmeticException.class, () -> instant.minus(Duration.ofNanos(-1)));
    }

    // Test durationUntil method for one day without leap second
    @Test
    public void test_durationUntil_oneDayNoLeap() {
        UtcInstant start = UtcInstant.ofModifiedJulianDay(MJD_1972_12_30, 0);
        UtcInstant end = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
        Duration duration = start.durationUntil(end);
        assertEquals(86400, duration.getSeconds());
        assertEquals(0, duration.getNano());
    }

    // Test durationUntil method for one day with leap second
    @Test
    public void test_durationUntil_oneDayLeap() {
        UtcInstant start = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
        UtcInstant end = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 0);
        Duration duration = start.durationUntil(end);
        assertEquals(86401, duration.getSeconds());
        assertEquals(0, duration.getNano());
    }

    // Test durationUntil method for one day with negative leap second
    @Test
    public void test_durationUntil_oneDayLeapNegative() {
        UtcInstant start = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 0);
        UtcInstant end = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
        Duration duration = start.durationUntil(end);
        assertEquals(-86401, duration.getSeconds());
        assertEquals(0, duration.getNano());
    }

    // Test conversion to TaiInstant
    @Test
    public void test_toTaiInstant() {
        for (int i = -1000; i < 1000; i++) {
            for (int j = 0; j < 10; j++) {
                UtcInstant utc = UtcInstant.ofModifiedJulianDay(36204 + i, j * NANOS_PER_SEC + 2L);
                TaiInstant tai = utc.toTaiInstant();
                assertEquals(i * SECS_PER_DAY + j + 10, tai.getTaiSeconds());
                assertEquals(2, tai.getNano());
            }
        }
    }

    // Test conversion to TaiInstant with max invalid value
    @Test
    public void test_toTaiInstant_maxInvalid() {
        UtcInstant utc = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> utc.toTaiInstant());
    }

    // Test conversion to Instant
    @Test
    public void test_toInstant() {
        for (int i = -1000; i < 1000; i++) {
            for (int j = 0; j < 10; j++) {
                Instant expected = Instant.ofEpochSecond(315532800 + i * SECS_PER_DAY + j).plusNanos(2);
                UtcInstant utc = UtcInstant.ofModifiedJulianDay(44239 + i, j * NANOS_PER_SEC + 2);
                assertEquals(expected, utc.toInstant());
            }
        }
    }

    // Test comparisons between UtcInstant instances
    @Test
    public void test_comparisons() {
        UtcInstant[] instants = {
            UtcInstant.ofModifiedJulianDay(-2L, 0),
            UtcInstant.ofModifiedJulianDay(-2L, NANOS_PER_DAY - 2),
            UtcInstant.ofModifiedJulianDay(-2L, NANOS_PER_DAY - 1),
            UtcInstant.ofModifiedJulianDay(-1L, 0),
            UtcInstant.ofModifiedJulianDay(-1L, 1),
            UtcInstant.ofModifiedJulianDay(-1L, NANOS_PER_DAY - 2),
            UtcInstant.ofModifiedJulianDay(-1L, NANOS_PER_DAY - 1),
            UtcInstant.ofModifiedJulianDay(0L, 0),
            UtcInstant.ofModifiedJulianDay(0L, 1),
            UtcInstant.ofModifiedJulianDay(0L, 2),
            UtcInstant.ofModifiedJulianDay(0L, NANOS_PER_DAY - 1),
            UtcInstant.ofModifiedJulianDay(1L, 0),
            UtcInstant.ofModifiedJulianDay(2L, 0)
        };
        doTest_comparisons_UtcInstant(instants);
    }

    // Helper method for testing comparisons
    private void doTest_comparisons_UtcInstant(UtcInstant... instants) {
        for (int i = 0; i < instants.length; i++) {
            UtcInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                UtcInstant b = instants[j];
                if (i < j) {
                    assertTrue(a.compareTo(b) < 0);
                    assertFalse(a.equals(b));
                    assertTrue(a.isBefore(b));
                    assertFalse(a.isAfter(b));
                } else if (i > j) {
                    assertTrue(a.compareTo(b) > 0);
                    assertFalse(a.equals(b));
                    assertFalse(a.isBefore(b));
                    assertTrue(a.isAfter(b));
                } else {
                    assertEquals(0, a.compareTo(b));
                    assertTrue(a.equals(b));
                    assertFalse(a.isBefore(b));
                    assertFalse(a.isAfter(b));
                }
            }
        }
    }

    // Test comparison to null
    @Test
    public void test_compareTo_ObjectNull() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(0L, 0);
        assertThrows(NullPointerException.class, () -> instant.compareTo(null));
    }

    // Test comparison to non-UtcInstant object
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void test_compareToNonUtcInstant() {
        Comparable comparable = UtcInstant.ofModifiedJulianDay(0L, 2);
        assertThrows(ClassCastException.class, () -> comparable.compareTo(new Object()));
    }

    // Test equals and hashCode methods
    @Test
    public void test_equals_and_hashCode() {
        new EqualsTester()
            .addEqualityGroup(UtcInstant.ofModifiedJulianDay(5L, 20), UtcInstant.ofModifiedJulianDay(5L, 20))
            .addEqualityGroup(UtcInstant.ofModifiedJulianDay(5L, 30), UtcInstant.ofModifiedJulianDay(5L, 30))
            .addEqualityGroup(UtcInstant.ofModifiedJulianDay(6L, 20), UtcInstant.ofModifiedJulianDay(6L, 20))
            .testEquals();
    }

    // Data provider for toString method tests
    public static Object[][] data_toString() {
        return new Object[][] {
            {40587, 0, "1970-01-01T00:00:00Z"},
            {40588, 1, "1970-01-02T00:00:00.000000001Z"},
            {40588, 999, "1970-01-02T00:00:00.000000999Z"},
            {40588, 1000, "1970-01-02T00:00:00.000001Z"},
            {40588, 999000, "1970-01-02T00:00:00.000999Z"},
            {40588, 1000000, "1970-01-02T00:00:00.001Z"},
            {40618, 999999999, "1970-02-01T00:00:00.999999999Z"},
            {40619, 1000000000, "1970-02-02T00:00:01Z"},
            {40620, 60L * 1000000000L, "1970-02-03T00:01:00Z"},
            {40621, 60L * 60L * 1000000000L, "1970-02-04T01:00:00Z"},
            {MJD_1972_12_31_LEAP, 24L * 60L * 60L * 1000000000L - 1000000000L, "1972-12-31T23:59:59Z"},
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"},
            {MJD_1973_01_01, 0, "1973-01-01T00:00:00Z"},
        };
    }

    // Test toString method
    @ParameterizedTest
    @MethodSource("data_toString")
    public void test_toString(long mjd, long nod, String expected) {
        assertEquals(expected, UtcInstant.ofModifiedJulianDay(mjd, nod).toString());
    }

    // Test parsing from toString output
    @ParameterizedTest
    @MethodSource("data_toString")
    public void test_toString_parse(long mjd, long nod, String str) {
        assertEquals(UtcInstant.ofModifiedJulianDay(mjd, nod), UtcInstant.parse(str));
    }
}