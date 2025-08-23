package org.apache.commons.io.file.attribute;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
 * Tests for the {@link FileTimes} utility class.
 */
@DisplayName("Tests for FileTimes utility class")
class FileTimesTest {

    // Main provider with high-precision edge cases for NTFS time conversions.
    static Stream<Arguments> provideInstantStringAndNtfsTime() {
        // @formatter:off
        return Stream.of(
            // NTFS epoch (1601-01-01T00:00:00Z) and nearby values
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0L),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1L),
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1L),

            // Long.MAX_VALUE and nearby values
            Arguments.of("+30828-09-14T02:48:05.477580700Z", Long.MAX_VALUE),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", Long.MAX_VALUE - 1),

            // Long.MIN_VALUE and nearby values
            Arguments.of("-27627-04-19T21:11:54.522419200Z", Long.MIN_VALUE),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", Long.MIN_VALUE + 1),

            // Values around one millisecond in 100-nanosecond units
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),

            // Unix epoch (1970-01-01T00:00:00Z) and nearby values
            Arguments.of("1970-01-01T00:00:00.0000000Z", -FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", -FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1969-12-31T23:59:59.9999999Z", -FileTimes.UNIX_TO_NTFS_OFFSET - 1)
        );
        // @formatter:on
    }

    // Provider for general-purpose date-time values around the Unix epoch.
    static Stream<Arguments> provideInstantStringAndFileTimeAroundUnixEpoch() {
        return Stream.of(
            Arguments.of("1970-01-01T00:00:00Z", FileTime.from(Instant.EPOCH)),
            Arguments.of("1969-12-31T23:59:00Z", FileTime.from(Instant.EPOCH.minusSeconds(60))),
            Arguments.of("1970-01-01T00:01:00Z", FileTime.from(Instant.EPOCH.plusSeconds(60)))
        );
    }

    // Provider for testing Unix time boundaries.
    static Stream<Arguments> provideInstantStringAndIsUnixTimeFlag() {
        return Stream.of(
            // Within 32-bit signed integer range for seconds
            Arguments.of("2022-12-27T12:45:22Z", true),
            Arguments.of("2038-01-19T03:14:07Z", true), // Max Unix time
            Arguments.of("1901-12-13T20:45:52Z", true), // Min Unix time
            // Outside 32-bit signed integer range for seconds
            Arguments.of("1901-12-13T20:45:51Z", false), // Below min
            Arguments.of("2038-01-19T03:14:08Z", false)  // Above max
        );
    }

    // Provider that extracts only the instant strings for tests that don't need the NTFS time value.
    private static Stream<String> provideInstantStrings() {
        return provideInstantStringAndNtfsTime().map(args -> (String) args.get()[0]);
    }

    @Nested
    @DisplayName("Conversion To NTFS Time")
    class ToNtfsTimeTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStringAndNtfsTime")
        @DisplayName("Should convert FileTime to the correct NTFS time value")
        void shouldConvertFileTimeFromInstantToNtfsTime(final String instantStr, final long expectedNtfsTime) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStringAndNtfsTime")
        @DisplayName("Should convert Date to NTFS time with millisecond precision")
        void shouldConvertDateToNtfsTimeWithMillisecondPrecision(final String instantStr, final long ntfsTime) {
            // Since java.util.Date has only millisecond precision, the expected NTFS time must be truncated.
            final long expectedNtfsTimeTruncatedToMillis =
                Math.floorDiv(ntfsTime, FileTimes.HUNDRED_NANOS_PER_MILLISECOND) * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;

            final Date date = Date.from(Instant.parse(instantStr));
            final long actualNtfsTime = FileTimes.toNtfsTime(date);

            // For MIN/MAX values, the conversion clamps instead of overflowing.
            if (actualNtfsTime == Long.MIN_VALUE || actualNtfsTime == Long.MAX_VALUE) {
                // This branch handles expected overflow cases, so no assertion is needed.
            } else {
                assertEquals(expectedNtfsTimeTruncatedToMillis, actualNtfsTime);
            }
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStringAndNtfsTime")
        @DisplayName("Should convert Java milliseconds to NTFS time with millisecond precision")
        void shouldConvertJavaMillisToNtfsTimeWithMillisecondPrecision(final String instantStr, final long ntfsTime) {
            // This test verifies the toNtfsTime(long) overload.
            final long expectedNtfsTimeTruncatedToMillis =
                Math.floorDiv(ntfsTime, FileTimes.HUNDRED_NANOS_PER_MILLISECOND) * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;

            final long javaMillis = Instant.parse(instantStr).toEpochMilli();
            final long actualNtfsTime = FileTimes.toNtfsTime(javaMillis);

            if (actualNtfsTime == Long.MIN_VALUE || actualNtfsTime == Long.MAX_VALUE) {
                // This branch handles expected overflow cases, so no assertion is needed.
            } else {
                assertEquals(expectedNtfsTimeTruncatedToMillis, actualNtfsTime);
            }
        }
    }

    @Nested
    @DisplayName("Conversion From NTFS Time")
    class FromNtfsTimeTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStringAndNtfsTime")
        @DisplayName("Should convert NTFS time to the correct FileTime")
        void shouldConvertNtfsTimeToCorrectFileTime(final String expectedInstantStr, final long ntfsTime) {
            final FileTime expectedFileTime = FileTime.from(Instant.parse(expectedInstantStr));
            assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(ntfsTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStringAndNtfsTime")
        @DisplayName("Should convert NTFS time to Date with millisecond precision")
        void shouldConvertNtfsTimeToDateWithMillisecondPrecision(final String instantStr, final long ntfsTime) {
            final long expectedMillis = Instant.parse(instantStr).toEpochMilli();
            final long actualMillis = FileTimes.ntfsTimeToDate(ntfsTime).getTime();
            assertEquals(expectedMillis, actualMillis);
        }
    }

    @Nested
    @DisplayName("Round-Trip Conversions")
    class RoundTripConversionTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStringAndFileTimeAroundUnixEpoch")
        @DisplayName("FileTime to NTFS time and back should be reversible")
        void fileTimeAndNtfsTimeConversionsShouldBeReversible(final String instantStr, final FileTime originalFileTime) {
            final long ntfsTime = FileTimes.toNtfsTime(originalFileTime);
            final FileTime roundTrippedFileTime = FileTimes.ntfsTimeToFileTime(ntfsTime);
            assertEquals(originalFileTime, roundTrippedFileTime);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStringAndNtfsTime")
        @DisplayName("Instant to NTFS time and back via ntfsTimeToInstant should be reversible")
        void instantAndNtfsTimeConversionsShouldBeReversible(final String instantStr, final long ntfsTime) {
            final Instant originalInstant = Instant.parse(instantStr);
            final long intermediateNtfsTime = FileTimes.toNtfsTime(originalInstant);
            final Instant roundTrippedInstant = FileTimes.ntfsTimeToInstant(intermediateNtfsTime);

            assertEquals(ntfsTime, intermediateNtfsTime);
            assertEquals(originalInstant, roundTrippedInstant);
        }
    }

    @Nested
    @DisplayName("Java Date Conversions")
    class DateConversionTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStrings")
        @DisplayName("Should convert Date to an equivalent FileTime")
        void shouldConvertDateToFileTime(final String instantStr) {
            final Instant instant = Instant.parse(instantStr);
            final Date date = Date.from(instant);
            final FileTime expectedFileTime = FileTime.from(instant);
            // Compare milliseconds due to precision differences between Date and FileTime
            assertEquals(expectedFileTime.toMillis(), FileTimes.toFileTime(date).toMillis());
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStrings")
        @DisplayName("Should convert FileTime to an equivalent Date")
        void shouldConvertFileTimeFromInstantToDate(final String instantStr) {
            final Instant instant = Instant.parse(instantStr);
            final FileTime fileTime = FileTime.from(instant);
            final Date expectedDate = Date.from(instant);
            assertEquals(expectedDate, FileTimes.toDate(fileTime));
        }
    }

    @Nested
    @DisplayName("Unix Time Compatibility")
    class UnixTimeCompatibilityTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStringAndIsUnixTimeFlag")
        @DisplayName("isUnixTime should correctly identify if a time is within the valid Unix range")
        void isUnixTimeShouldCorrectlyIdentifyRange(final String instantStr, final boolean isUnixTime) {
            final Instant instant = Instant.parse(instantStr);
            // Test isUnixTime(FileTime)
            assertEquals(isUnixTime, FileTimes.isUnixTime(FileTime.from(instant)));
            // Test isUnixTime(long)
            assertEquals(isUnixTime, FileTimes.isUnixTime(instant.getEpochSecond()));
        }

        @Test
        @DisplayName("isUnixTime should return true for a null FileTime")
        void isUnixTimeShouldReturnTrueForNullFileTime() {
            assertTrue(FileTimes.isUnixTime(null));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStrings")
        @DisplayName("fromUnixTime should create the correct FileTime from epoch seconds")
        void fromUnixTimeShouldCreateCorrectFileTime(final String instantStr) {
            final long epochSecond = Instant.parse(instantStr).getEpochSecond();
            assertEquals(epochSecond, FileTimes.fromUnixTime(epochSecond).to(TimeUnit.SECONDS));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantStringAndIsUnixTimeFlag")
        @DisplayName("toUnixTime should convert FileTime to the correct epoch seconds")
        void toUnixTimeShouldConvertFileTimeToEpochSeconds(final String instantStr, final boolean ignored) {
            final Instant instant = Instant.parse(instantStr);
            final long expectedSeconds = instant.getEpochSecond();
            final long actualSeconds = FileTimes.toUnixTime(FileTime.from(instant));
            assertEquals(expectedSeconds, actualSeconds);
        }
    }

    @Nested
    @DisplayName("Time Arithmetic")
    class TimeArithmeticTests {

        @Test
        @DisplayName("plusSeconds should correctly add seconds to a FileTime")
        void plusSecondsShouldCorrectlyAddSeconds() {
            assertEquals(Instant.EPOCH.plusSeconds(2), FileTimes.plusSeconds(FileTimes.EPOCH, 2).toInstant());
            assertEquals(Instant.EPOCH, FileTimes.plusSeconds(FileTimes.EPOCH, 0).toInstant());
        }
    }
}