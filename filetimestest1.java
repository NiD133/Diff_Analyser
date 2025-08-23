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
 * Tests for {@link FileTimes}.
 *
 * <p>Tests are organized into nested classes based on the functionality being tested.
 */
@DisplayName("FileTimes Utility Class")
class FileTimesTest {

    /**
     * Provides a comprehensive stream of Instants and their corresponding NTFS time values.
     * This includes epoch boundaries, min/max long values, and values around them.
     */
    static Stream<Arguments> provideInstantsAndNtfsTimes() {
        // @formatter:off
        return Stream.of(
            // NTFS epoch (1601-01-01)
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0L),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1L),
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1L),

            // Unix epoch (1970-01-01)
            Arguments.of("1970-01-01T00:00:00.0000000Z", FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1969-12-31T23:59:59.9999999Z", FileTimes.UNIX_TO_NTFS_OFFSET - 1),

            // Millisecond boundaries relative to NTFS epoch
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),

            // Long.MAX_VALUE boundaries
            Arguments.of("+30828-09-14T02:48:05.477580700Z", Long.MAX_VALUE),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", Long.MAX_VALUE - 1),
            Arguments.of("+30828-09-14T02:48:05.477570700Z", Long.MAX_VALUE - 100),

            // Long.MIN_VALUE boundaries
            Arguments.of("-27627-04-19T21:11:54.522419200Z", Long.MIN_VALUE),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", Long.MIN_VALUE + 1),
            Arguments.of("-27627-04-19T21:11:54.522519200Z", Long.MIN_VALUE + 1000)
        );
        // @formatter:on
    }

    @Nested
    @DisplayName("NTFS Time Conversions")
    class NtfsTimeConversionsTest {

        @ParameterizedTest(name = "Instant {0} -> NTFS {1}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("toNtfsTime(FileTime) should convert correctly")
        void toNtfsTime_fromFileTime_shouldConvertToCorrectValue(final String instantStr, final long expectedNtfsTime) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(fileTime));
        }

        @ParameterizedTest(name = "NTFS {1} -> Instant {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("ntfsTimeToFileTime() should convert correctly")
        void ntfsTimeToFileTime_shouldConvertToCorrectFileTime(final String expectedInstantStr, final long ntfsTime) {
            final Instant expectedInstant = Instant.parse(expectedInstantStr);
            final FileTime expectedFileTime = FileTime.from(expectedInstant);
            assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(ntfsTime));
        }

        @ParameterizedTest(name = "Date from Instant {0} -> NTFS (truncated)")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("toNtfsTime(Date) should convert correctly")
        void toNtfsTime_fromDate_shouldConvertToCorrectValue(final String instantStr, final long ntfsTimeWithNanos) {
            // Date has only millisecond precision, so the expected NTFS time must be truncated.
            final long millisTo100Nanos = FileTimes.HUNDRED_NANOS_PER_MILLISECOND;
            final long expectedNtfsTime = Math.floorDiv(ntfsTimeWithNanos, millisTo100Nanos) * millisTo100Nanos;

            final Date date = Date.from(Instant.parse(instantStr));
            final long actualNtfsTime = FileTimes.toNtfsTime(date);

            // The method saturates at Long.MIN_VALUE/MAX_VALUE instead of overflowing.
            if (actualNtfsTime == Long.MIN_VALUE || actualNtfsTime == Long.MAX_VALUE) {
                // This is expected for boundary cases, assertion is that it doesn't throw.
            } else {
                assertEquals(expectedNtfsTime, actualNtfsTime);
            }
        }

        @ParameterizedTest(name = "NTFS {1} -> Date (from Instant {0})")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("ntfsTimeToDate() should convert correctly")
        void ntfsTimeToDate_shouldConvertToCorrectDate(final String instantStr, final long ntfsTime) {
            final Date expectedDate = Date.from(Instant.parse(instantStr));
            final Date actualDate = FileTimes.ntfsTimeToDate(ntfsTime);
            assertEquals(expectedDate.getTime(), actualDate.getTime());
        }

        static Stream<Arguments> provideJavaMillisAndNtfsTimes() {
            return Stream.of(
                Arguments.of(0L, FileTimes.UNIX_TO_NTFS_OFFSET), // Unix Epoch
                Arguments.of(1000L, FileTimes.UNIX_TO_NTFS_OFFSET + 10_000_000), // 1 second after epoch
                Arguments.of(-1000L, FileTimes.UNIX_TO_NTFS_OFFSET - 10_000_000) // 1 second before epoch
            );
        }

        @ParameterizedTest
        @MethodSource("provideJavaMillisAndNtfsTimes")
        @DisplayName("toNtfsTime(long) should convert Java milliseconds correctly")
        void toNtfsTime_fromJavaMillis_shouldConvertToCorrectValue(final long javaMillis, final long expectedNtfsTime) {
            assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(javaMillis));
        }
    }

    @Nested
    @DisplayName("Java Date and FileTime Conversions")
    class DateAndFileTimeConversionTest {

        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("toDate() and toFileTime() should be reversible")
        void dateAndFileTimeConversions_shouldBeReversible(final String instantStr, final long ignoredNtfsTime) {
            final Instant instant = Instant.parse(instantStr);
            final FileTime fileTime = FileTime.from(instant);
            final Date date = Date.from(instant);

            // Since Date has millisecond precision, we compare the millis value.
            assertEquals(fileTime.toMillis(), FileTimes.toFileTime(date).toMillis());
            assertEquals(date, FileTimes.toDate(fileTime));
        }
    }

    @Nested
    @DisplayName("Unix Time Utilities")
    class UnixTimeTest {

        static Stream<Arguments> provideUnixTimeBoundaryCases() {
            // @formatter:off
            return Stream.of(
                // Valid 32-bit Unix time range
                Arguments.of("1901-12-13T20:45:52Z", true), // Minimum 32-bit signed int
                Arguments.of("2038-01-19T03:14:07Z", true), // Maximum 32-bit signed int
                Arguments.of("2022-12-27T12:45:22Z", true), // A typical date

                // Outside 32-bit Unix time range
                Arguments.of("1901-12-13T20:45:51Z", false), // Just before minimum
                Arguments.of("2038-01-19T03:14:08Z", false), // Just after maximum
                Arguments.of("2099-06-30T12:31:42Z", false)  // Far future
            );
            // @formatter:on
        }

        @ParameterizedTest(name = "{0} -> isUnixTime = {1}")
        @MethodSource("provideUnixTimeBoundaryCases")
        @DisplayName("isUnixTime(FileTime) should return correct result for boundaries")
        void isUnixTime_forFileTime_shouldReturnExpectedResult(final String instantStr, final boolean expected) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expected, FileTimes.isUnixTime(fileTime));
        }

        @ParameterizedTest(name = "EpochSeconds for {0} -> isUnixTime = {1}")
        @MethodSource("provideUnixTimeBoundaryCases")
        @DisplayName("isUnixTime(long) should return correct result for boundaries")
        void isUnixTime_forEpochSeconds_shouldReturnExpectedResult(final String instantStr, final boolean expected) {
            final long epochSecond = Instant.parse(instantStr).getEpochSecond();
            assertEquals(expected, FileTimes.isUnixTime(epochSecond));
        }

        @Test
        @DisplayName("isUnixTime(null) should return true")
        void isUnixTime_forNullFileTime_shouldReturnTrue() {
            assertTrue(FileTimes.isUnixTime(null));
        }

        @ParameterizedTest(name = "Roundtrip for {0}")
        @MethodSource("provideUnixTimeBoundaryCases")
        @DisplayName("fromUnixTime() and toUnixTime() should be reversible for valid Unix times")
        void fromAndToUnixTime_shouldBeReversible(final String instantStr, final boolean isUnixTime) {
            final FileTime originalFileTime = FileTime.from(Instant.parse(instantStr));
            if (isUnixTime) {
                final long unixTime = FileTimes.toUnixTime(originalFileTime);
                final FileTime roundTrippedFileTime = FileTimes.fromUnixTime(unixTime);
                // Compare seconds, as toUnixTime() loses nanosecond precision.
                assertEquals(originalFileTime.to(TimeUnit.SECONDS), roundTrippedFileTime.to(TimeUnit.SECONDS));
            }
        }
    }

    @Nested
    @DisplayName("Constants")
    class ConstantsTest {

        @Test
        @DisplayName("EPOCH constant should represent the Unix epoch")
        void epoch_shouldBeUnixEpoch() {
            assertEquals(0, FileTimes.EPOCH.toMillis());
            assertEquals(Instant.EPOCH, FileTimes.EPOCH.toInstant());
        }
    }
}