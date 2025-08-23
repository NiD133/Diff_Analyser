package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DateTimeException;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test class for modification, conversion, and serialization of {@link UtcInstant}.
 */
public class UtcInstantModificationAndConversionTest {

    // A Modified Julian Day that falls on a leap day (1972-12-31)
    private static final long MJD_1972_12_31_LEAP = 41682;
    // A Modified Julian Day for a non-leap day (1972-12-30)
    private static final long MJD_1972_12_30 = 41681;
    // A Modified Julian Day for the day after the 1972 leap day (1973-01-01)
    private static final long MJD_1973_01_01 = 41683;
    // A Modified Julian Day that falls on another leap day (1973-12-31)
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    private static Stream<Arguments> provideInvalidStringsForParsing() {
        return Stream.of(
            Arguments.of(""), // Empty string
            Arguments.of("A"), // Not a valid format
            Arguments.of("2012-13-01T00:00:00Z") // Invalid month
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidStringsForParsing")
    public void parse_withInvalidString_throwsDateTimeException(String invalidString) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------
    private static Stream<Arguments> provideDataForWithModifiedJulianDay() {
        return Stream.of(
            // mjd, nanos, newMjd, expectedMjd, expectedNanos
            // --- Basic cases ---
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),      // Positive MJD change
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),     // Negative MJD change
            Arguments.of(7L, 12345L, 2L, 2L, 12345L),      // Another positive change
            Arguments.of(7L, 12345L, -2L, -2L, 12345L),     // Another negative change

            // --- Leap second handling (success) ---
            // Set MJD to the same value on a leap day
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            // Move from one leap day to another, where nanoOfDay is valid for both
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY),

            // --- Leap second handling (failure) ---
            // Fail: move from a leap day to a normal day, as nanoOfDay becomes too large
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30, null, null),
            // Fail: move from a leap day to a normal day, as nanoOfDay becomes too large
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDataForWithModifiedJulianDay")
    public void withModifiedJulianDay_handlesValidAndInvalidInputs(long mjd, long nanos, long newMjd, Long expectedMjd, Long expectedNanos) {
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, nanos);

        if (expectedMjd != null) {
            // Test case for a valid modification
            UtcInstant result = initialInstant.withModifiedJulianDay(newMjd);
            assertEquals(expectedMjd.longValue(), result.getModifiedJulianDay());
            assertEquals(expectedNanos.longValue(), result.getNanoOfDay());
        } else {
            // Test case where modification is expected to fail
            assertThrows(DateTimeException.class, () -> initialInstant.withModifiedJulianDay(newMjd));
        }
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------
    private static Stream<Arguments> provideDataForWithNanoOfDay() {
        return Stream.of(
            // mjd, nanos, newNanoOfDay, expectedMjd, expectedNanos
            // --- Basic cases ---
            Arguments.of(0L, 12345L, 1L, 0L, 1L), // Simple change
            Arguments.of(7L, 12345L, 2L, 7L, 2L), // Another simple change

            // --- Boundary checks on a normal day ---
            Arguments.of(MJD_1972_12_30, 1L, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1), // Max valid nanos
            Arguments.of(MJD_1972_12_30, 1L, NANOS_PER_DAY, null, null), // Fail: nanos out of bounds
            Arguments.of(MJD_1972_12_30, 1L, -1L, null, null), // Fail: negative nanos

            // --- Boundary checks on a leap day ---
            Arguments.of(MJD_1972_12_31_LEAP, 1L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY), // Nanos for 23:59:60
            Arguments.of(MJD_1972_12_31_LEAP, 1L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1), // Max valid nanos
            Arguments.of(MJD_1972_12_31_LEAP, 1L, NANOS_PER_LEAP_DAY, null, null) // Fail: nanos out of bounds
        );
    }

    @ParameterizedTest
    @MethodSource("provideDataForWithNanoOfDay")
    public void withNanoOfDay_handlesValidAndInvalidInputs(long mjd, long nanos, long newNanoOfDay, Long expectedMjd, Long expectedNanos) {
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, nanos);

        if (expectedMjd != null) {
            // Test case for a valid modification
            UtcInstant result = initialInstant.withNanoOfDay(newNanoOfDay);
            assertEquals(expectedMjd.longValue(), result.getModifiedJulianDay());
            assertEquals(expectedNanos.longValue(), result.getNanoOfDay());
        } else {
            // Test case where modification is expected to fail
            assertThrows(DateTimeException.class, () -> initialInstant.withNanoOfDay(newNanoOfDay));
        }
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    private static Stream<Arguments> provideDataForPlus() {
        // mjd, nanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
        return Stream.of(
            // --- Basic additions ---
            Arguments.of(0L, 0L, 0L, 0, 0L, 0L), // Add zero
            Arguments.of(0L, 0L, 0L, 1, 0L, 1L), // Add one nanosecond
            Arguments.of(0L, 0L, 1L, 0, 0L, NANOS_PER_SEC), // Add one second
            Arguments.of(0L, 0L, 2L, 5, 0L, 2 * NANOS_PER_SEC + 5), // Add seconds and nanos

            // --- Day boundary crossing ---
            Arguments.of(0L, 0L, 0L, -1, -1L, NANOS_PER_DAY - 1), // Subtract 1ns, rolls back one day
            Arguments.of(0L, 0L, SECS_PER_DAY, 0, 1L, 0L), // Add exactly one day in seconds
            Arguments.of(1L, 0L, -SECS_PER_DAY, 0, 0L, 0L), // Subtract exactly one day in seconds
            Arguments.of(1L, 0L, -SECS_PER_DAY, 1, 0L, 1L) // Subtract a day, add 1ns
        );
    }

    @ParameterizedTest
    @MethodSource("provideDataForPlus")
    public void plus_addsDurationCorrectly(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration durationToAdd = Duration.ofSeconds(plusSeconds, plusNanos);

        UtcInstant result = initialInstant.plus(durationToAdd);

        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    private static Stream<Arguments> provideDataForMinus() {
        // mjd, nanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
        return Stream.of(
            // --- Basic subtractions ---
            Arguments.of(0L, 0L, 0L, 0, 0L, 0L), // Subtract zero
            Arguments.of(0L, 1L, 0L, 1, 0L, 0L), // Subtract one nanosecond
            Arguments.of(0L, NANOS_PER_SEC, 1L, 0, 0L, 0L), // Subtract one second
            Arguments.of(1L, 2 * NANOS_PER_SEC + 5, 2L, 5, 1L, 0L), // Subtract seconds and nanos

            // --- Day boundary crossing ---
            Arguments.of(0L, 0L, 0L, 1, -1L, NANOS_PER_DAY - 1), // Subtract 1ns, rolls back one day
            Arguments.of(0L, 0L, 0L, -1, 0L, 1L), // Subtract -1ns (add 1ns)
            Arguments.of(1L, 0L, SECS_PER_DAY, 0, 0L, 0L), // Subtract exactly one day in seconds
            Arguments.of(0L, 0L, -SECS_PER_DAY, 0, 1L, 0L) // Subtract a negative day (add a day)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDataForMinus")
    public void minus_subtractsDurationCorrectly(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration durationToSubtract = Duration.ofSeconds(minusSeconds, minusNanos);

        UtcInstant result = initialInstant.minus(durationToSubtract);

        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // toString() and parse()
    //-----------------------------------------------------------------------
    private static Stream<Arguments> provideDataForToStringAndParse() {
        return Stream.of(
            // mjd, nanoOfDay, expectedString
            Arguments.of(40587L, 0L, "1970-01-01T00:00:00Z"), // Unix epoch start
            Arguments.of(40588L, 1L, "1970-01-02T00:00:00.000000001Z"), // With single nanosecond
            Arguments.of(40588L, 999L, "1970-01-02T00:00:00.000000999Z"), // With 3-digit nanos
            Arguments.of(40588L, 1000L, "1970-01-02T00:00:00.000001Z"), // With 6-digit nanos (no trailing zeros)
            Arguments.of(40588L, 999000L, "1970-01-02T00:00:00.000999Z"), // With 6-digit nanos
            Arguments.of(40588L, 1000000L, "1970-01-02T00:00:00.001Z"), // With 3-digit nanos (milliseconds)
            Arguments.of(40618L, 999999999L, "1970-02-01T00:00:00.999999999Z"), // Max nanos
            Arguments.of(40619L, NANOS_PER_SEC, "1970-02-02T00:00:01Z"), // Rollover to next second
            Arguments.of(40620L, 60 * NANOS_PER_SEC, "1970-02-03T00:01:00Z"), // Rollover to next minute
            Arguments.of(40621L, 60 * 60 * NANOS_PER_SEC, "1970-02-04T01:00:00Z"), // Rollover to next hour
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"), // Second before leap second
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"), // Leap second representation
            Arguments.of(MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z") // Day after leap second
        );
    }

    @ParameterizedTest
    @MethodSource("provideDataForToStringAndParse")
    public void toString_returnsCorrectIsoFormat(long mjd, long nod, String expected) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);
        assertEquals(expected, instant.toString());
    }

    @ParameterizedTest
    @MethodSource("provideDataForToStringAndParse")
    public void parse_recreatesInstantFromToStringOutput(long mjd, long nod, String str) {
        UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nod);
        assertEquals(expectedInstant, UtcInstant.parse(str));
    }

    //-----------------------------------------------------------------------
    // Serialization
    //-----------------------------------------------------------------------
    @Test
    public void serialization_producesEqualObjectOnDeserialization() throws Exception {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(2, 3);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
        
        Object deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            deserialized = ois.readObject();
        }
        
        assertEquals(original, deserialized);
    }
}