package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.testing.EqualsTester;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("UtcInstant Tests")
public class UtcInstantTest {

    // A non-leap day for general testing
    private static final long MJD_NORMAL_DAY = 41681; // 1972-12-30
    // A known leap day, which had a 61-second last minute
    private static final long MJD_LEAP_DAY = 41682; // 1972-12-31
    // A non-leap day following the leap day
    private static final long MJD_DAY_AFTER_LEAP = 41683; // 1973-01-01
    // Another leap day, for testing transitions between leap days
    private static final long MJD_ANOTHER_LEAP_DAY = MJD_LEAP_DAY + 365; // 1973-12-31

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("Factory and Parsing")
    class FactoryAndParsingTests {

        @ParameterizedTest
        @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
        @DisplayName("parse() should throw DateTimeException for invalid input strings")
        void parse_throwsException_forInvalidInput(String invalidString) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
        }

        @ParameterizedTest(name = "Parse: {2}")
        @CsvSource({
            // Inlined values for MJD and NOD as annotations don't support constants.
            // MJD_LEAP_DAY = 41682, NANOS_PER_DAY = 86400000000000
            "40587, 0,                '1970-01-01T00:00:00Z'",
            "41682, 86400000000000,   '1972-12-31T23:59:60Z'",
            "41683, 0,                '1973-01-01T00:00:00Z'"
        })
        @DisplayName("parse() should correctly interpret valid ISO-8601 strings")
        void parse_recreatesOriginalInstant(long mjd, long nod, String isoString) {
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nod);
            UtcInstant parsed = UtcInstant.parse(isoString);
            assertEquals(expected, parsed);
        }
    }

    @Nested
    @DisplayName("Modification Methods")
    class ModificationTests {

        @Test
        @DisplayName("withModifiedJulianDay() should update the day while preserving nano-of-day")
        void withModifiedJulianDay_shouldUpdateDay() {
            UtcInstant base = UtcInstant.ofModifiedJulianDay(MJD_NORMAL_DAY, 12345L);
            UtcInstant result = base.withModifiedJulianDay(MJD_DAY_AFTER_LEAP);
            assertEquals(MJD_DAY_AFTER_LEAP, result.getModifiedJulianDay());
            assertEquals(12345L, result.getNanoOfDay());
        }

        @Test
        @DisplayName("withModifiedJulianDay() should preserve a valid leap-second time when moving to another leap day")
        void withModifiedJulianDay_shouldHandleTransitionBetweenLeapDays() {
            UtcInstant baseOnLeapDay = UtcInstant.ofModifiedJulianDay(MJD_LEAP_DAY, NANOS_PER_DAY); // 23:59:60
            UtcInstant result = baseOnLeapDay.withModifiedJulianDay(MJD_ANOTHER_LEAP_DAY);
            assertEquals(MJD_ANOTHER_LEAP_DAY, result.getModifiedJulianDay());
            assertEquals(NANOS_PER_DAY, result.getNanoOfDay());
        }

        @Test
        @DisplayName("withModifiedJulianDay() should throw exception if nano-of-day is invalid for the new day")
        void withModifiedJulianDay_shouldThrowException_forInvalidNanoOfDayOnNewDate() {
            // Create an instant at the leap second (23:59:60), valid only on a leap day.
            UtcInstant baseOnLeapDay = UtcInstant.ofModifiedJulianDay(MJD_LEAP_DAY, NANOS_PER_DAY);
            // Attempt to move to a normal day, where this nano-of-day is invalid.
            assertThrows(DateTimeException.class, () -> baseOnLeapDay.withModifiedJulianDay(MJD_NORMAL_DAY));
        }

        @Test
        @DisplayName("withNanoOfDay() should update the nano-of-day")
        void withNanoOfDay_shouldUpdateNanos() {
            UtcInstant base = UtcInstant.ofModifiedJulianDay(MJD_NORMAL_DAY, 12345L);
            UtcInstant result = base.withNanoOfDay(54321L);
            assertEquals(MJD_NORMAL_DAY, result.getModifiedJulianDay());
            assertEquals(54321L, result.getNanoOfDay());
        }

        @Test
        @DisplayName("withNanoOfDay() should allow setting nano-of-day to a valid leap second")
        void withNanoOfDay_shouldAllowSettingLeapSecondTime() {
            UtcInstant base = UtcInstant.ofModifiedJulianDay(MJD_LEAP_DAY, 12345L);
            UtcInstant result = base.withNanoOfDay(NANOS_PER_DAY); // 23:59:60
            assertEquals(MJD_LEAP_DAY, result.getModifiedJulianDay());
            assertEquals(NANOS_PER_DAY, result.getNanoOfDay());
        }

        @Test
        @DisplayName("withNanoOfDay() should throw exception for a nano-of-day value that is too large")
        void withNanoOfDay_shouldThrowException_forInvalidNanosOnNormalDay() {
            UtcInstant base = UtcInstant.ofModifiedJulianDay(MJD_NORMAL_DAY, 12345L);
            // NANOS_PER_DAY is invalid for a normal day.
            assertThrows(DateTimeException.class, () -> base.withNanoOfDay(NANOS_PER_DAY));
        }

        @Test
        @DisplayName("withNanoOfDay() should throw exception for a negative nano-of-day value")
        void withNanoOfDay_shouldThrowException_forNegativeNanos() {
            UtcInstant base = UtcInstant.ofModifiedJulianDay(MJD_NORMAL_DAY, 12345L);
            assertThrows(DateTimeException.class, () -> base.withNanoOfDay(-1L));
        }
    }

    @Nested
    @DisplayName("Arithmetic Methods")
    class ArithmeticTests {

        @Test
        @DisplayName("plus() should add duration without rolling over the day")
        void plus_addsDuration_withoutDayRollover() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(10, 1000);
            UtcInstant result = start.plus(Duration.ofSeconds(10, 500));
            assertEquals(10, result.getModifiedJulianDay());
            assertEquals(10 * NANOS_PER_SEC + 1500, result.getNanoOfDay());
        }

        @Test
        @DisplayName("plus() should handle rolling over to the next day")
        void plus_addsDuration_rollingToNextDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(10, NANOS_PER_DAY - 5);
            UtcInstant result = start.plus(Duration.ofNanos(10));
            assertEquals(11, result.getModifiedJulianDay());
            assertEquals(5, result.getNanoOfDay());
        }

        @Test
        @DisplayName("plus() should handle negative duration, rolling to the previous day")
        void plus_addsNegativeDuration_rollingToPreviousDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(10, 5);
            UtcInstant result = start.plus(Duration.ofNanos(-10));
            assertEquals(9, result.getModifiedJulianDay());
            assertEquals(NANOS_PER_DAY - 5, result.getNanoOfDay());
        }

        @Test
        @DisplayName("plus() should correctly roll over a leap day")
        void plus_addsDuration_rollingOverLeapDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(MJD_LEAP_DAY, NANOS_PER_LEAP_DAY - 5);
            UtcInstant result = start.plus(Duration.ofNanos(10));
            assertEquals(MJD_DAY_AFTER_LEAP, result.getModifiedJulianDay());
            assertEquals(5, result.getNanoOfDay());
        }

        @Test
        @DisplayName("minus() should subtract duration without rolling over the day")
        void minus_subtractsDuration_withoutDayRollover() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(10, 1000);
            UtcInstant result = start.minus(Duration.ofNanos(500));
            assertEquals(10, result.getModifiedJulianDay());
            assertEquals(500, result.getNanoOfDay());
        }

        @Test
        @DisplayName("minus() should handle rolling over to the previous day")
        void minus_subtractsDuration_rollingToPreviousDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(10, 5);
            UtcInstant result = start.minus(Duration.ofNanos(10));
            assertEquals(9, result.getModifiedJulianDay());
            assertEquals(NANOS_PER_DAY - 5, result.getNanoOfDay());
        }
    }

    @Nested
    @DisplayName("Comparison and Equality")
    class ComparisonAndEqualityTests {

        private final UtcInstant T1 = UtcInstant.ofModifiedJulianDay(40000, 10);
        private final UtcInstant T2 = UtcInstant.ofModifiedJulianDay(40000, 20);
        private final UtcInstant T3 = UtcInstant.ofModifiedJulianDay(40001, 10);
        private final UtcInstant T1_equivalent = UtcInstant.ofModifiedJulianDay(40000, 10);

        @Test
        @DisplayName("compareTo() should correctly order instants")
        void compareTo_shouldOrderInstantsCorrectly() {
            assertTrue(T1.compareTo(T2) < 0, "T1 should be less than T2");
            assertTrue(T2.compareTo(T1) > 0, "T2 should be greater than T1");
            assertTrue(T1.compareTo(T3) < 0, "T1 should be less than T3");
            assertEquals(0, T1.compareTo(T1_equivalent), "T1 should be equal to its equivalent");
        }

        @Test
        @DisplayName("isBefore() and isAfter() should work correctly")
        void isBefore_and_isAfter_shouldWorkCorrectly() {
            assertTrue(T1.isBefore(T2));
            assertFalse(T2.isBefore(T1));
            assertFalse(T1.isBefore(T1_equivalent));

            assertTrue(T2.isAfter(T1));
            assertFalse(T1.isAfter(T2));
            assertFalse(T1.isAfter(T1_equivalent));
        }

        @Test
        @DisplayName("equals() and hashCode() should adhere to the contract")
        void equalsAndHashCode_shouldAdhereToContract() {
            new EqualsTester()
                .addEqualityGroup(T1, T1_equivalent)
                .addEqualityGroup(T2)
                .addEqualityGroup(T3)
                .testEquals();
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        @ParameterizedTest(name = "MJD={0}, NOD={1} -> {2}")
        @CsvSource({
            // Inlined values for MJD and NOD as annotations don't support constants.
            // MJD_LEAP_DAY = 41682, MJD_DAY_AFTER_LEAP = 41683
            // NANOS_PER_DAY = 86400000000000
            "40587, 0,                                '1970-01-01T00:00:00Z'",
            "40588, 1,                                '1970-01-02T00:00:00.000000001Z'",
            "40588, 1000000,                          '1970-01-02T00:00:00.001Z'",
            "40619, 1000000000,                      '1970-02-02T00:00:01Z'",
            "41682, 86399000000000,                  '1972-12-31T23:59:59Z'",
            "41682, 86400000000000,                  '1972-12-31T23:59:60Z'",
            "41683, 0,                                '1973-01-01T00:00:00Z'"
        })
        @DisplayName("toString() should produce correct ISO-8601 representation")
        void toString_shouldProduceCorrectIsoFormat(long mjd, long nod, String expected) {
            assertEquals(expected, UtcInstant.ofModifiedJulianDay(mjd, nod).toString());
        }
    }

    @Nested
    @DisplayName("Serialization and Interface Contracts")
    class SerializationAndInterfaceTests {

        @Test
        @DisplayName("should implement Serializable and Comparable")
        void classShouldImplementRequiredInterfaces() {
            assertTrue(Serializable.class.isAssignableFrom(UtcInstant.class));
            assertTrue(Comparable.class.isAssignableFrom(UtcInstant.class));
        }

        @Test
        @DisplayName("should be serializable and deserializable")
        void serialization_deserialization_cycleShouldResultInEqualObject() throws Exception {
            UtcInstant original = UtcInstant.ofModifiedJulianDay(MJD_LEAP_DAY, NANOS_PER_DAY);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(original);
            }

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                UtcInstant deserialized = (UtcInstant) ois.readObject();
                assertEquals(original, deserialized);
            }
        }
    }
}