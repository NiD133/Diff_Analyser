package org.apache.commons.io.file.attribute;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link FileTimes}.
 */
class FileTimesTest {

    /**
     * Provides a comprehensive stream of Instants and their equivalent NTFS time values.
     * NTFS time is the number of 100-nanosecond intervals since 1601-01-01T00:00:00Z.
     *
     * @return A stream of Arguments, each containing an Instant and its corresponding long NTFS time.
     */
    static Stream<Arguments> provideInstantsAndNtfsTimes() {
        // @formatter:off
        return Stream.of(
            // NTFS epoch
            Arguments.of(Instant.parse("1601-01-01T00:00:00.0000000Z"), 0L),
            Arguments.of(Instant.parse("1601-01-01T00:00:00.0000001Z"), 1L),
            Arguments.of(Instant.parse("1600-12-31T23:59:59.9999999Z"), -1L),

            // Unix epoch
            Arguments.of(Instant.parse("1970-01-01T00:00:00.0000000Z"), -FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of(Instant.parse("1970-01-01T00:00:00.0000001Z"), -FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of(Instant.parse("1969-12-31T23:59:59.9999999Z"), -FileTimes.UNIX_TO_NTFS_OFFSET - 1),

            // Values around one millisecond
            Arguments.of(Instant.parse("1601-01-01T00:00:00.0010000Z"), FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of(Instant.parse("1601-01-01T00:00:00.0010001Z"), FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of(Instant.parse("1601-01-01T00:00:00.0009999Z"), FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),

            // Boundary values for Long
            Arguments.of(Instant.parse("+30828-09-14T02:48:05.477580700Z"), Long.MAX_VALUE),
            Arguments.of(Instant.parse("+30828-09-14T02:48:05.477580600Z"), Long.MAX_VALUE - 1),
            Arguments.of(Instant.parse("-27627-04-19T21:11:54.522419200Z"), Long.MIN_VALUE),
            Arguments.of(Instant.parse("-27627-04-19T21:11:54.522419300Z"), Long.MIN_VALUE + 1)
        );
        // @formatter:on
    }

    /**
     * Provides Instants and a boolean indicating if they are valid Unix times.
     * A Unix time is a 32-bit signed integer representing seconds since the epoch.
     *
     * @return A stream of Arguments, each with an Instant and a boolean.
     */
    static Stream<Arguments> provideInstantsAndUnixTimeCompatibility() {
        // @formatter:off
        return Stream.of(
            // Valid Unix time range
            Arguments.of(Instant.parse("2022-12-27T12:45:22Z"), true),
            Arguments.of(Instant.parse("2038-01-19T03:14:07Z"), true), // Max Unix time
            Arguments.of(Instant.parse("1901-12-13T20:45:52Z"), true), // Min Unix time

            // Outside valid Unix time range
            Arguments.of(Instant.parse("2038-01-19T03:14:08Z"), false), // Max + 1 second
            Arguments.of(Instant.parse("1901-12-13T20:45:51Z"), false), // Min - 1 second
            Arguments.of(Instant.parse("2099-06-30T12:31:42Z"), false)
        );
        // @formatter:on
    }

    /**
     * Provides a stream of Instants for conversion tests.
     */
    static Stream<Instant> provideInstants() {
        return provideInstantsAndNtfsTimes().map(args -> (Instant) args.get()[0]);
    }

    @Nested
    @DisplayName("NTFS Time Conversion Tests")
    class NtfsConversionTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("toNtfsTime should convert FileTime to the correct NTFS long value")
        void toNtfsTime_shouldConvertFromFileTime(final Instant instant, final long expectedNtfsTime) {
            final FileTime fileTime = FileTime.from(instant);
            assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("ntfsTimeToFileTime should convert NTFS long value to the correct FileTime")
        void ntfsTimeToFileTime_shouldConvertFromLong(final Instant expectedInstant, final long ntfsTime) {
            final FileTime expectedFileTime = FileTime.from(expectedInstant);
            assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(ntfsTime));
            assertEquals(expectedInstant, FileTimes.ntfsTimeToInstant(ntfsTime));
        }
    }

    @Nested
    @DisplayName("java.util.Date Conversion Tests")
    class DateConversionTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstants")
        @DisplayName("toDate should convert FileTime to an equivalent Date")
        void toDate_shouldConvertFromFileTime(final Instant instant) {
            final FileTime fileTime = FileTime.from(instant);
            final Date expectedDate = Date.from(instant);
            assertEquals(expectedDate, FileTimes.toDate(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstants")
        @DisplayName("toFileTime should convert Date to an equivalent FileTime")
        void toFileTime_shouldConvertFromDate(final Instant instant) {
            final Date date = Date.from(instant);
            final FileTime expectedFileTime = FileTime.from(instant);
            // Compare milliseconds, as Date and FileTime from Instant can have precision differences
            assertEquals(expectedFileTime.toMillis(), FileTimes.toFileTime(date).toMillis());
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("toNtfsTime should convert Date with millisecond precision")
        void toNtfsTime_shouldConvertFromDate(final Instant instant, final long ntfsTime) {
            // Since java.util.Date has only millisecond precision, the expected NTFS time
            // must be truncated to the nearest millisecond for a valid comparison.
            final long expectedNtfsMillis = Math.floorDiv(ntfsTime, FileTimes.HUNDRED_NANOS_PER_MILLISECOND) * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;

            final Date date = Date.from(instant);
            final long actualNtfsTime = FileTimes.toNtfsTime(date);

            // The conversion from Date can result in saturation at Long.MIN_VALUE/MAX_VALUE.
            if (actualNtfsTime == Long.MIN_VALUE || actualNtfsTime == Long.MAX_VALUE) {
                // This is an expected outcome for boundary values, so no assertion is needed.
            } else {
                assertEquals(expectedNtfsMillis, actualNtfsTime);
            }
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("ntfsTimeToDate should convert NTFS time to a Date with millisecond precision")
        void ntfsTimeToDate_shouldConvertFromNtfsTime(final Instant instant, final long ntfsTime) {
            final long expectedMillis = instant.toEpochMilli();
            final long actualMillis = FileTimes.ntfsTimeToDate(ntfsTime).getTime();
            assertEquals(expectedMillis, actualMillis);
        }
    }

    @Nested
    @DisplayName("Unix Time Compatibility Tests")
    class UnixTimeCompatibilityTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndUnixTimeCompatibility")
        @DisplayName("isUnixTime should correctly check FileTime compatibility")
        void isUnixTime_shouldCheckFileTime(final Instant instant, final boolean expected) {
            final FileTime fileTime = FileTime.from(instant);
            assertEquals(expected, FileTimes.isUnixTime(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndUnixTimeCompatibility")
        @DisplayName("isUnixTime should correctly check seconds-since-epoch compatibility")
        void isUnixTime_shouldCheckSeconds(final Instant instant, final boolean expected) {
            final long seconds = instant.getEpochSecond();
            assertEquals(expected, FileTimes.isUnixTime(seconds));
        }

        @Test
        @DisplayName("isUnixTime should return true for a null FileTime")
        void isUnixTime_shouldReturnTrueForNullFileTime() {
            assertTrue(FileTimes.isUnixTime(null));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstants")
        @DisplayName("fromUnixTime should create a FileTime with the correct number of seconds")
        void fromUnixTime_shouldCreateCorrectFileTime(final Instant instant) {
            final long epochSecond = instant.getEpochSecond();
            assertEquals(epochSecond, FileTimes.fromUnixTime(epochSecond).to(TimeUnit.SECONDS));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndUnixTimeCompatibility")
        @DisplayName("toUnixTime should produce a value consistent with isUnixTime")
        void toUnixTime_shouldBeConsistentWithIsUnixTimeCheck(final Instant instant, final boolean isUnixTime) {
            final FileTime fileTime = FileTime.from(instant);
            final long unixTime = FileTimes.toUnixTime(fileTime);
            assertEquals(isUnixTime, FileTimes.isUnixTime(unixTime));
        }
    }

    @Nested
    @DisplayName("Null Input Handling Tests")
    class NullInputTests {

        @Test
        @DisplayName("toDate should return null when FileTime is null")
        void toDate_shouldReturnNullForNullFileTime() {
            assertNull(FileTimes.toDate(null));
        }

        @Test
        @DisplayName("toFileTime should return null when Date is null")
        void toFileTime_shouldReturnNullForNullDate() {
            assertNull(FileTimes.toFileTime(null));
        }
    }
}