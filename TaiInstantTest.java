package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Test suite for the TaiInstant class.
 */
public class TestTaiInstant {

    // Test if TaiInstant implements Serializable and Comparable interfaces
    @Test
    public void test_interfaces() {
        assertTrue(Serializable.class.isAssignableFrom(TaiInstant.class));
        assertTrue(Comparable.class.isAssignableFrom(TaiInstant.class));
    }

    // Test serialization and deserialization of TaiInstant
    @Test
    public void test_serialization() throws Exception {
        TaiInstant original = TaiInstant.ofTaiSeconds(2, 3);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            TaiInstant deserialized = (TaiInstant) ois.readObject();
            assertEquals(original, deserialized);
        }
    }

    // Test factory method ofTaiSeconds with various inputs
    @Test
    public void factory_ofTaiSecondslong_long() {
        for (long seconds = -2; seconds <= 2; seconds++) {
            for (int nanos = 0; nanos < 10; nanos++) {
                TaiInstant instant = TaiInstant.ofTaiSeconds(seconds, nanos);
                assertEquals(seconds, instant.getTaiSeconds());
                assertEquals(nanos, instant.getNano());
            }
            for (int nanos = -10; nanos < 0; nanos++) {
                TaiInstant instant = TaiInstant.ofTaiSeconds(seconds, nanos);
                assertEquals(seconds - 1, instant.getTaiSeconds());
                assertEquals(nanos + 1_000_000_000, instant.getNano());
            }
            for (int nanos = 999_999_990; nanos < 1_000_000_000; nanos++) {
                TaiInstant instant = TaiInstant.ofTaiSeconds(seconds, nanos);
                assertEquals(seconds, instant.getTaiSeconds());
                assertEquals(nanos, instant.getNano());
            }
        }
    }

    @Test
    public void factory_ofTaiSeconds_long_long_nanosNegativeAdjusted() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(2L, -1);
        assertEquals(1, instant.getTaiSeconds());
        assertEquals(999_999_999, instant.getNano());
    }

    @Test
    public void factory_ofTaiSeconds_long_long_tooBig() {
        assertThrows(ArithmeticException.class, () -> TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 1_000_000_000));
    }

    // Test factory method of(Instant)
    @Test
    public void factory_of_Instant() {
        TaiInstant instant = TaiInstant.of(Instant.ofEpochSecond(0, 2));
        long expectedSeconds = ((40587L - 36204) * 24 * 60 * 60 + 10);
        assertEquals(expectedSeconds, instant.getTaiSeconds());
        assertEquals(2, instant.getNano());
    }

    @Test
    public void factory_of_Instant_null() {
        assertThrows(NullPointerException.class, () -> TaiInstant.of((Instant) null));
    }

    // Test factory method of(UtcInstant)
    @Test
    public void factory_of_UtcInstant() {
        for (int days = -1000; days < 1000; days++) {
            for (int seconds = 0; seconds < 10; seconds++) {
                TaiInstant instant = TaiInstant.of(UtcInstant.ofModifiedJulianDay(36204 + days, seconds * 1_000_000_000L + 2L));
                assertEquals(days * 24 * 60 * 60 + seconds + 10, instant.getTaiSeconds());
                assertEquals(2, instant.getNano());
            }
        }
    }

    @Test
    public void factory_of_UtcInstant_null() {
        assertThrows(NullPointerException.class, () -> TaiInstant.of((UtcInstant) null));
    }

    // Test parsing from CharSequence
    @Test
    public void factory_parse_CharSequence() {
        for (int seconds = -1000; seconds < 1000; seconds++) {
            for (int nanos = 900_000_000; nanos < 990_000_000; nanos += 10_000_000) {
                String str = seconds + "." + nanos + "s(TAI)";
                TaiInstant instant = TaiInstant.parse(str);
                assertEquals(seconds, instant.getTaiSeconds());
                assertEquals(nanos, instant.getNano());
            }
        }
    }

    public static Object[][] data_badParse() {
        return new Object[][] {
            {"A.123456789s(TAI)"},
            {"123.12345678As(TAI)"},
            {"123.123456789"},
            {"123.123456789s"},
            {"+123.123456789s(TAI)"},
            {"-123.123s(TAI)"},
        };
    }

    @ParameterizedTest
    @MethodSource("data_badParse")
    public void factory_parse_CharSequence_invalid(String str) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(str));
    }

    @Test
    public void factory_parse_CharSequence_null() {
        assertThrows(NullPointerException.class, () -> TaiInstant.parse((String) null));
    }

    // Test withTAISeconds() method
    public static Object[][] data_withTAISeconds() {
        return new Object[][] {
            {0L, 12345L, 1L, 1L, 12345L},
            {0L, 12345L, -1L, -1L, 12345L},
            {7L, 12345L, 2L, 2L, 12345L},
            {7L, 12345L, -2L, -2L, 12345L},
            {-99L, 12345L, 3L, 3L, 12345L},
            {-99L, 12345L, -3L, -3L, 12345L},
        };
    }

    @ParameterizedTest
    @MethodSource("data_withTAISeconds")
    public void test_withTAISeconds(long tai, long nanos, long newTai, Long expectedTai, Long expectedNanos) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(tai, nanos).withTaiSeconds(newTai);
        assertEquals(expectedTai.longValue(), instant.getTaiSeconds());
        assertEquals(expectedNanos.longValue(), instant.getNano());
    }

    // Test withNano() method
    public static Object[][] data_withNano() {
        return new Object[][] {
            {0L, 12345L, 1, 0L, 1L},
            {7L, 12345L, 2, 7L, 2L},
            {-99L, 12345L, 3, -99L, 3L},
            {-99L, 12345L, 999999999, -99L, 999999999L},
            {-99L, 12345L, -1, null, null},
            {-99L, 12345L, 1000000000, null, null},
        };
    }

    @ParameterizedTest
    @MethodSource("data_withNano")
    public void test_withNano(long tai, long nanos, int newNano, Long expectedTai, Long expectedNanos) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(tai, nanos);
        if (expectedTai != null) {
            TaiInstant withNano = instant.withNano(newNano);
            assertEquals(expectedTai.longValue(), withNano.getTaiSeconds());
            assertEquals(expectedNanos.longValue(), withNano.getNano());
        } else {
            assertThrows(IllegalArgumentException.class, () -> instant.withNano(newNano));
        }
    }

    // Test plus(Duration) method
    public static Object[][] data_plus() {
        return new Object[][] {
            {Long.MIN_VALUE, 0, Long.MAX_VALUE, 0, -1, 0},
            {-4, 666666667, -4, 666666667, -7, 333333334},
            {-4, 666666667, -3, 0, -7, 666666667},
            {-4, 666666667, -2, 0, -6, 666666667},
            {-4, 666666667, -1, 0, -5, 666666667},
            {-4, 666666667, -1, 333333334, -4, 1},
            {-4, 666666667, -1, 666666667, -4, 333333334},
            {-4, 666666667, -1, 999999999, -4, 666666666},
            {-4, 666666667, 0, 0, -4, 666666667},
            {-4, 666666667, 0, 1, -4, 666666668},
            {-4, 666666667, 0, 333333333, -3, 0},
            {-4, 666666667, 0, 666666666, -3, 333333333},
            {-4, 666666667, 1, 0, -3, 666666667},
            {-4, 666666667, 2, 0, -2, 666666667},
            {-4, 666666667, 3, 0, -1, 666666667},
            {-4, 666666667, 3, 333333333, 0, 0},
            {-3, 0, -4, 666666667, -7, 666666667},
            {-3, 0, -3, 0, -6, 0},
            {-3, 0, -2, 0, -5, 0},
            {-3, 0, -1, 0, -4, 0},
            {-3, 0, -1, 333333334, -4, 333333334},
            {-3, 0, -1, 666666667, -4, 666666667},
            {-3, 0, -1, 999999999, -4, 999999999},
            {-3, 0, 0, 0, -3, 0},
            {-3, 0, 0, 1, -3, 1},
            {-3, 0, 0, 333333333, -3, 333333333},
            {-3, 0, 0, 666666666, -3, 666666666},
            {-3, 0, 1, 0, -2, 0},
            {-3, 0, 2, 0, -1, 0},
            {-3, 0, 3, 0, 0, 0},
            {-3, 0, 3, 333333333, 0, 333333333},
            {-2, 0, -4, 666666667, -6, 666666667},
            {-2, 0, -3, 0, -5, 0},
            {-2, 0, -2, 0, -4, 0},
            {-2, 0, -1, 0, -3, 0},
            {-2, 0, -1, 333333334, -3, 333333334},
            {-2, 0, -1, 666666667, -3, 666666667},
            {-2, 0, -1, 999999999, -3, 999999999},
            {-2, 0, 0, 0, -2, 0},
            {-2, 0, 0, 1, -2, 1},
            {-2, 0, 0, 333333333, -2, 333333333},
            {-2, 0, 0, 666666666, -2, 666666666},
            {-2, 0, 1, 0, -1, 0},
            {-2, 0, 2, 0, 0, 0},
            {-2, 0, 3, 0, 1, 0},
            {-2, 0, 3, 333333333, 1, 333333333},
            {-1, 0, -4, 666666667, -5, 666666667},
            {-1, 0, -3, 0, -4, 0},
            {-1, 0, -2, 0, -3, 0},
            {-1, 0, -1, 0, -2, 0},
            {-1, 0, -1, 333333334, -2, 333333334},
            {-1, 0, -1, 666666667, -2, 666666667},
            {-1, 0, -1, 999999999, -2, 999999999},
            {-1, 0, 0, 0, -1, 0},
            {-1, 0, 0, 1, -1, 1},
            {-1, 0, 0, 333333333, -1, 333333333},
            {-1, 0, 0, 666666666, -1, 666666666},
            {-1, 0, 1, 0, 0, 0},
            {-1, 0, 2, 0, 1, 0},
            {-1, 0, 3, 0, 2, 0},
            {-1, 0, 3, 333333333, 2, 333333333},
            {-1, 666666667, -4, 666666667, -4, 333333334},
            {-1, 666666667, -3, 0, -4, 666666667},
            {-1, 666666667, -2, 0, -3, 666666667},
            {-1, 666666667, -1, 0, -2, 666666667},
            {-1, 666666667, -1, 333333334, -1, 1},
            {-1, 666666667, -1, 666666667, -1, 333333334},
            {-1, 666666667, -1, 999999999, -1, 666666666},
            {-1, 666666667, 0, 0, -1, 666666667},
            {-1, 666666667, 0, 1, -1, 666666668},
            {-1, 666666667, 0, 333333333, 0, 0},
            {-1, 666666667, 0, 666666666, 0, 333333333},
            {-1, 666666667, 1, 0, 0, 666666667},
            {-1, 666666667, 2, 0, 1, 666666667},
            {-1, 666666667, 3, 0, 2, 666666667},
            {-1, 666666667, 3, 333333333, 3, 0},
            {0, 0, -4, 666666667, -4, 666666667},
            {0, 0, -3, 0, -3, 0},
            {0, 0, -2, 0, -2, 0},
            {0, 0, -1, 0, -1, 0},
            {0, 0, -1, 333333334, -1, 333333334},
            {0, 0, -1, 666666667, -1, 666666667},
            {0, 0, -1, 999999999, -1, 999999999},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 1},
            {0, 0, 0, 333333333, 0, 333333333},
            {0, 0, 0, 666666666, 0, 666666666},
            {0, 0, 1, 0, 1, 0},
            {0, 0, 2, 0, 2, 0},
            {0, 0, 3, 0, 3, 0},
            {0, 0, 3, 333333333, 3, 333333333},
            {0, 333333333, -4, 666666667, -3, 0},
            {0, 333333333, -3, 0, -3, 333333333},
            {0, 333333333, -2, 0, -2, 333333333},
            {0, 333333333, -1, 0, -1, 333333333},
            {0, 333333333, -1, 333333334, -1, 666666667},
            {0, 333333333, -1, 666666667, 0, 0},
            {0, 333333333, -1, 999999999, 0, 333333332},
            {0, 333333333, 0, 0, 0, 333333333},
            {0, 333333333, 0, 1, 0, 333333334},
            {0, 333333333, 0, 333333333, 0, 666666666},
            {0, 333333333, 0, 666666666, 0, 999999999},
            {0, 333333333, 1, 0, 1, 333333333},
            {0, 333333333, 2, 0, 2, 333333333},
            {0, 333333333, 3, 0, 3, 333333333},
            {0, 333333333, 3, 333333333, 3, 666666666},
            {1, 0, -4, 666666667, -3, 666666667},
            {1, 0, -3, 0, -2, 0},
            {1, 0, -2, 0, -1, 0},
            {1, 0, -1, 0, 0, 0},
            {1, 0, -1, 333333334, 0, 333333334},
            {1, 0, -1, 666666667, 0, 666666667},
            {1, 0, -1, 999999999, 0, 999999999},
            {1, 0, 0, 0, 1, 0},
            {1, 0, 0, 1, 1, 1},
            {1, 0, 0, 333333333, 1, 333333333},
            {1, 0, 0, 666666666, 1, 666666666},
            {1, 0, 1, 0, 2, 0},
            {1, 0, 2, 0, 3, 0},
            {1, 0, 3, 0, 4, 0},
            {1, 0, 3, 333333333, 4, 333333333},
            {2, 0, -4, 666666667, -2, 666666667},
            {2, 0, -3, 0, -1, 0},
            {2, 0, -2, 0, 0, 0},
            {2, 0, -1, 0, 1, 0},
            {2, 0, -1, 333333334, 1, 333333334},
            {2, 0, -1, 666666667, 1, 666666667},
            {2, 0, -1, 999999999, 1, 999999999},
            {2, 0, 0, 0, 2, 0},
            {2, 0, 0, 1, 2, 1},
            {2, 0, 0, 333333333, 2, 333333333},
            {2, 0, 0, 666666666, 2, 666666666},
            {2, 0, 1, 0, 3, 0},
            {2, 0, 2, 0, 4, 0},
            {2, 0, 3, 0, 5, 0},
            {2, 0, 3, 333333333, 5, 333333333},
            {3, 0, -4, 666666667, -1, 666666667},
            {3, 0, -3, 0, 0, 0},
            {3, 0, -2, 0, 1, 0},
            {3, 0, -1, 0, 2, 0},
            {3, 0, -1, 333333334, 2, 333333334},
            {3, 0, -1, 666666667, 2, 666666667},
            {3, 0, -1, 999999999, 2, 999999999},
            {3, 0, 0, 0, 3, 0},
            {3, 0, 0, 1, 3, 1},
            {3, 0, 0, 333333333, 3, 333333333},
            {3, 0, 0, 666666666, 3, 666666666},
            {3, 0, 1, 0, 4, 0},
            {3, 0, 2, 0, 5, 0},
            {3, 0, 3, 0, 6, 0},
            {3, 0, 3, 333333333, 6, 333333333},
            {3, 333333333, -4, 666666667, 0, 0},
            {3, 333333333, -3, 0, 0, 333333333},
            {3, 333333333, -2, 0, 1, 333333333},
            {3, 333333333, -1, 0, 2, 333333333},
            {3, 333333333, -1, 333333334, 2, 666666667},
            {3, 333333333, -1, 666666667, 3, 0},
            {3, 333333333, -1, 999999999, 3, 333333332},
            {3, 333333333, 0, 0, 3, 333333333},
            {3, 333333333, 0, 1, 3, 333333334},
            {3, 333333333, 0, 333333333, 3, 666666666},
            {3, 333333333, 0, 666666666, 3, 999999999},
            {3, 333333333, 1, 0, 4, 333333333},
            {3, 333333333, 2, 0, 5, 333333333},
            {3, 333333333, 3, 0, 6, 333333333},
            {3, 333333333, 3, 333333333, 6, 666666666},
            {Long.MAX_VALUE, 0, Long.MIN_VALUE, 0, -1, 0},
        };
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    public void test_plus(long seconds, int nanos, long plusSeconds, int plusNanos, long expectedSeconds, int expectedNanoOfSecond) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(seconds, nanos).plus(Duration.ofSeconds(plusSeconds, plusNanos));
        assertEquals(expectedSeconds, instant.getTaiSeconds());
        assertEquals(expectedNanoOfSecond, instant.getNano());
    }

    @Test
    public void test_plus_overflowTooBig() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 999_999_999);
        assertThrows(ArithmeticException.class, () -> instant.plus(Duration.ofSeconds(0, 1)));
    }

    @Test
    public void test_plus_overflowTooSmall() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> instant.plus(Duration.ofSeconds(-1, 999_999_999)));
    }

    // Test minus(Duration) method
    public static Object[][] data_minus() {
        return new Object[][] {
            {Long.MIN_VALUE, 0, Long.MIN_VALUE + 1, 0, -1, 0},
            {-4, 666666667, -4, 666666667, 0, 0},
            {-4, 666666667, -3, 0, -1, 666666667},
            {-4, 666666667, -2, 0, -2, 666666667},
            {-4, 666666667, -1, 0, -3, 666666667},
            {-4, 666666667, -1, 333333334, -3, 333333333},
            {-4, 666666667, -1, 666666667, -3, 0},
            {-4, 666666667, -1, 999999999, -4, 666666668},
            {-4, 666666667, 0, 0, -4, 666666667},
            {-4, 666666667, 0, 1, -4, 666666666},
            {-4, 666666667, 0, 333333333, -4, 333333334},
            {-4, 666666667, 0, 666666666, -4, 1},
            {-4, 666666667, 1, 0, -5, 666666667},
            {-4, 666666667, 2, 0, -6, 666666667},
            {-4, 666666667, 3, 0, -7, 666666667},
            {-4, 666666667, 3, 333333333, -7, 333333334},
            {-3, 0, -4, 666666667, 0, 333333333},
            {-3, 0, -3, 0, 0, 0},
            {-3, 0, -2, 0, -1, 0},
            {-3, 0, -1, 0, -2, 0},
            {-3, 0, -1, 333333334, -3, 666666666},
            {-3, 0, -1, 666666667, -3, 333333333},
            {-3, 0, -1, 999999999, -3, 1},
            {-3, 0, 0, 0, -3, 0},
            {-3, 0, 0, 1, -4, 999999999},
            {-3, 0, 0, 333333333, -4, 666666667},
            {-3, 0, 0, 666666666, -4, 333333334},
            {-3, 0, 1, 0, -4, 0},
            {-3, 0, 2, 0, -5, 0},
            {-3, 0, 3, 0, -6, 0},
            {-3, 0, 3, 333333333, -7, 666666667},
            {-2, 0, -4, 666666667, 1, 333333333},
            {-2, 0, -3, 0, 1, 0},
            {-2, 0, -2, 0, 0, 0},
            {-2, 0, -1, 0, -1, 0},
            {-2, 0, -1, 333333334, -2, 666666666},
            {-2, 0, -1, 666666667, -2, 333333333},
            {-2, 0, -1, 999999999, -2, 1},
            {-2, 0, 0, 0, -2, 0},
            {-2, 0, 0, 1, -3, 999999999},
            {-2, 0, 0, 333333333, -3, 666666667},
            {-2, 0, 0, 666666666, -3, 333333334},
            {-2, 0, 1, 0, -3, 0},
            {-2, 0, 2, 0, -4, 0},
            {-2, 0, 3, 0, -5, 0},
            {-2, 0, 3, 333333333, -6, 666666667},
            {-1, 0, -4, 666666667, 2, 333333333},
            {-1, 0, -3, 0, 2, 0},
            {-1, 0, -2, 0, 1, 0},
            {-1, 0, -1, 0, 0, 0},
            {-1, 0, -1, 333333334, -1, 666666666},
            {-1, 0, -1, 666666667, -1, 333333333},
            {-1, 0, -1, 999999999, -1, 1},
            {-1, 0, 0, 0, -1, 0},
            {-1, 0, 0, 1, -2, 999999999},
            {-1, 0, 0, 333333333, -2, 666666667},
            {-1, 0, 0, 666666666, -2, 333333334},
            {-1, 0, 1, 0, -2, 0},
            {-1, 0, 2, 0, -3, 0},
            {-1, 0, 3, 0, -4, 0},
            {-1, 0, 3, 333333333, -5, 666666667},
            {-1, 666666667, -4, 666666667, 3, 0},
            {-1, 666666667, -3, 0, 2, 666666667},
            {-1, 666666667, -2, 0, 1, 666666667},
            {-1, 666666667, -1, 0, 0, 666666667},
            {-1, 666666667, -1, 333333334, 0, 333333333},
            {-1, 666666667, -1, 666666667, 0, 0},
            {-1, 666666667, -1, 999999999, -1, 666666668},
            {-1, 666666667, 0, 0, -1, 666666667},
            {-1, 666666667, 0, 1, -1, 666666666},
            {-1, 666666667, 0, 333333333, -1, 333333334},
            {-1, 666666667, 0, 666666666, -1, 1},
            {-1, 666666667, 1, 0, -2, 666666667},
            {-1, 666666667, 2, 0, -3, 666666667},
            {-1, 666666667, 3, 0, -4, 666666667},
            {-1, 666666667, 3, 333333333, -4, 333333334},
            {0, 0, -4, 666666667, 3, 333333333},
            {0, 0, -3, 0, 3, 0},
            {0, 0, -2, 0, 2, 0},
            {0, 0, -1, 0, 1, 0},
            {0, 0, -1, 333333334, 0, 666666666},
            {0, 0, -1, 666666667, 0, 333333333},
            {0, 0, -1, 999999999, 0, 1},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, -1, 999999999},
            {0, 0, 0, 333333333, -1, 666666667},
            {0, 0, 0, 666666666, -1, 333333334},
            {0, 0, 1, 0, -1, 0},
            {0, 0, 2, 0, -2, 0},
            {0, 0, 3, 0, -3, 0},
            {0, 0, 3, 333333333, -4, 666666667},
            {0, 333333333, -4, 666666667, 3, 666666666},
            {0, 333333333, -3, 0, 3, 333333333},
            {0, 333333333, -2, 0, 2, 333333333},
            {0, 333333333, -1, 0, 1, 333333333},
            {0, 333333333, -1, 333333334, 0, 999999999},
            {0, 333333333, -1, 666666667, 0, 666666666},
            {0, 333333333, -1, 999999999, 0, 333333334},
            {0, 333333333, 0, 0, 0, 333333333},
            {0, 333333333, 0, 1, 0, 333333332},
            {0, 333333333, 0, 333333333, 0, 0},
            {0, 333333333, 0, 666666666, -1, 666666667},
            {0, 333333333, 1, 0, -1, 333333333},
            {0, 333333333, 2, 0, -2, 333333333},
            {0, 333333333, 3, 0, -3, 333333333},
            {0, 333333333, 3, 333333333, -3, 0},
            {1, 0, -4, 666666667, 4, 333333333},
            {1, 0, -3, 0, 4, 0},
            {1, 0, -2, 0, 3, 0},
            {1, 0, -1, 0, 2, 0},
            {1, 0, -1, 333333334, 1, 666666666},
            {1, 0, -1, 666666667, 1, 333333333},
            {1, 0, -1, 999999999, 1, 1},
            {1, 0, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 999999999},
            {1, 0, 0, 333333333, 0, 666666667},
            {1, 0, 0, 666666666, 0, 333333334},
            {1, 0, 1, 0, 0, 0},
            {1, 0, 2, 0, -1, 0},
            {1, 0, 3, 0, -2, 0},
            {1, 0, 3, 333333333, -3, 666666667},
            {2, 0, -4, 666666667, 5, 333333333},
            {2, 0, -3, 0, 5, 0},
            {2, 0, -2, 0, 4, 0},
            {2, 0, -1, 0, 3, 0},
            {2, 0, -1, 333333334, 2, 666666666},
            {2, 0, -1, 666666667, 2, 333333333},
            {2, 0, -1, 999999999, 2, 1},
            {2, 0, 0, 0, 2, 0},
            {2, 0, 0, 1, 1, 999999999},
            {2, 0, 0, 333333333, 1, 666666667},
            {2, 0, 0, 666666666, 1, 333333334},
            {2, 0, 1, 0, 1, 0},
            {2, 0, 2, 0, 0, 0},
            {2, 0, 3, 0, -1, 0},
            {2, 0, 3, 333333333, -2, 666666667},
            {3, 0, -4, 666666667, 6, 333333333},
            {3, 0, -3, 0, 6, 0},
            {3, 0, -2, 0, 5, 0},
            {3, 0, -1, 0, 4, 0},
            {3, 0, -1, 333333334, 3, 666666666},
            {3, 0, -1, 666666667, 3, 333333333},
            {3, 0, -1, 999999999, 3, 1},
            {3, 0, 0, 0, 3, 0},
            {3, 0, 0, 1, 2, 999999999},
            {3, 0, 0, 333333333, 2, 666666667},
            {3, 0, 0, 666666666, 2, 333333334},
            {3, 0, 1, 0, 2, 0},
            {3, 0, 2, 0, 1, 0},
            {3, 0, 3, 0, 0, 0},
            {3, 0, 3, 333333333, -1, 666666667},
            {3, 333333333, -4, 666666667, 6, 666666666},
            {3, 333333333, -3, 0, 6, 333333333},
            {3, 333333333, -2, 0, 5, 333333333},
            {3, 333333333, -1, 0, 4, 333333333},
            {3, 333333333, -1, 333333334, 3, 999999999},
            {3, 333333333, -1, 666666667, 3, 666666666},
            {3, 333333333, -1, 999999999, 3, 333333334},
            {3, 333333333, 0, 0, 3, 333333333},
            {3, 333333333, 0, 1, 3, 333333332},
            {3, 333333333, 0, 333333333, 3, 0},
            {3, 333333333, 0, 666666666, 2, 666666667},
            {3, 333333333, 1, 0, 2, 333333333},
            {3, 333333333, 2, 0, 1, 333333333},
            {3, 333333333, 3, 0, 0, 333333333},
            {3, 333333333, 3, 333333333, 0, 0},
            {Long.MAX_VALUE, 0, Long.MAX_VALUE, 0, 0, 0},
        };
    }

    @ParameterizedTest
    @MethodSource("data_minus")
    public void test_minus(long seconds, int nanos, long minusSeconds, int minusNanos, long expectedSeconds, int expectedNanoOfSecond) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(seconds, nanos).minus(Duration.ofSeconds(minusSeconds, minusNanos));
        assertEquals(expectedSeconds, instant.getTaiSeconds());
        assertEquals(expectedNanoOfSecond, instant.getNano());
    }

    @Test
    public void test_minus_overflowTooSmall() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> instant.minus(Duration.ofSeconds(0, 1)));
    }

    @Test
    public void test_minus_overflowTooBig() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 999_999_999);
        assertThrows(ArithmeticException.class, () -> instant.minus(Duration.ofSeconds(-1, 999_999_999)));
    }

    // Test durationUntil() method
    @Test
    public void test_durationUntil_fifteenSeconds() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 0);
        TaiInstant end = TaiInstant.ofTaiSeconds(25, 0);
        Duration duration = start.durationUntil(end);
        assertEquals(15, duration.getSeconds());
        assertEquals(0, duration.getNano());
    }

    @Test
    public void test_durationUntil_twoNanos() {
        TaiInstant start = TaiInstant.ofTaiSeconds(4, 5);
        TaiInstant end = TaiInstant.ofTaiSeconds(4, 7);
        Duration duration = start.durationUntil(end);
        assertEquals(0, duration.getSeconds());
        assertEquals(2, duration.getNano());
    }

    @Test
    public void test_durationUntil_twoNanosNegative() {
        TaiInstant start = TaiInstant.ofTaiSeconds(4, 9);
        TaiInstant end = TaiInstant.ofTaiSeconds(4, 7);
        Duration duration = start.durationUntil(end);
        assertEquals(-1, duration.getSeconds());
        assertEquals(999_999_998, duration.getNano());
    }

    // Test toUtcInstant() method
    @Test
    public void test_toUtcInstant() {
        for (int days = -1000; days < 1000; days++) {
            for (int seconds = 0; seconds < 10; seconds++) {
                UtcInstant expected = UtcInstant.ofModifiedJulianDay(36204 + days, seconds * 1_000_000_000L + 2L);
                TaiInstant instant = TaiInstant.ofTaiSeconds(days * 24 * 60 * 60 + seconds + 10, 2);
                assertEquals(expected, instant.toUtcInstant());
            }
        }
    }

    // Test toInstant() method
    @Test
    public void test_toInstant() {
        for (int days = -1000; days < 1000; days++) {
            for (int seconds = 0; seconds < 10; seconds++) {
                Instant expected = Instant.ofEpochSecond(-378691200L + days * 24 * 60 * 60 + seconds).plusNanos(2);
                TaiInstant instant = TaiInstant.ofTaiSeconds(days * 24 * 60 * 60 + seconds + 10, 2);
                assertEquals(expected, instant.toInstant());
            }
        }
    }

    // Test compareTo() method
    @Test
    public void test_comparisons() {
        doTest_comparisons_TaiInstant(
            TaiInstant.ofTaiSeconds(-2L, 0),
            TaiInstant.ofTaiSeconds(-2L, 999999998),
            TaiInstant.ofTaiSeconds(-2L, 999999999),
            TaiInstant.ofTaiSeconds(-1L, 0),
            TaiInstant.ofTaiSeconds(-1L, 1),
            TaiInstant.ofTaiSeconds(-1L, 999999998),
            TaiInstant.ofTaiSeconds(-1L, 999999999),
            TaiInstant.ofTaiSeconds(0L, 0),
            TaiInstant.ofTaiSeconds(0L, 1),
            TaiInstant.ofTaiSeconds(0L, 2),
            TaiInstant.ofTaiSeconds(0L, 999999999),
            TaiInstant.ofTaiSeconds(1L, 0),
            TaiInstant.ofTaiSeconds(2L, 0)
        );
    }

    void doTest_comparisons_TaiInstant(TaiInstant... instants) {
        for (int i = 0; i < instants.length; i++) {
            TaiInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                TaiInstant b = instants[j];
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

    @Test
    public void test_compareTo_ObjectNull() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 0);
        assertThrows(NullPointerException.class, () -> instant.compareTo(null));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void test_compareToNonTaiInstant() {
        Comparable comparable = TaiInstant.ofTaiSeconds(0L, 2);
        assertThrows(ClassCastException.class, () -> comparable.compareTo(new Object()));
    }

    // Test equals() and hashCode() methods
    @Test
    public void test_equals_and_hashCode() {
        new EqualsTester()
            .addEqualityGroup(TaiInstant.ofTaiSeconds(5L, 20), TaiInstant.ofTaiSeconds(5L, 20))
            .addEqualityGroup(TaiInstant.ofTaiSeconds(5L, 30), TaiInstant.ofTaiSeconds(5L, 30))
            .addEqualityGroup(TaiInstant.ofTaiSeconds(6L, 20), TaiInstant.ofTaiSeconds(6L, 20))
            .testEquals();
    }

    // Test toString() method
    @Test
    public void test_toString_standard() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(123L, 123456789);
        assertEquals("123.123456789s(TAI)", instant.toString());
    }

    @Test
    public void test_toString_negative() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(-123L, 123456789);
        assertEquals("-123.123456789s(TAI)", instant.toString());
    }

    @Test
    public void test_toString_zeroDecimal() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 567);
        assertEquals("0.000000567s(TAI)", instant.toString());
    }
}