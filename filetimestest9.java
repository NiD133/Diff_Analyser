package org.apache.commons.io.file.attribute;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
@DisplayName("Tests for FileTimes utility class")
class FileTimesTest {

    /**
     * Provides arguments for NTFS time conversions, including boundary and epoch values.
     *
     * @return a stream of arguments: [ISO instant string, corresponding NTFS time (long)].
     */
    static Stream<Arguments> ntfsTimeAndInstantProvider() {
        // @formatter:off
        return Stream.of(
            // NTFS Epoch (1601-01-01)
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0L),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1L),
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1L),

            // Unix Epoch (1970-01-01)
            Arguments.of("1970-01-01T00:00:00.0000000Z", -FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", -FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1969-12-31T23:59:59.9999999Z", -FileTimes.UNIX_TO_NTFS_OFFSET - 1),

            // Millisecond boundaries relative to NTFS epoch
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),

            // Long min/max boundaries
            Arguments.of("+30828-09-14T02:48:05.477580700Z", Long.MAX_VALUE),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", Long.MAX_VALUE - 1),
            Arguments.of("-27627-04-19T21:11:54.522419200Z", Long.MIN_VALUE),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", Long.MIN_VALUE + 1)
        );
        // @formatter:on
    }

    /**
     * Provides arguments for testing Unix time validity.
     *
     * @return a stream of arguments: [ISO instant string, is valid Unix time (boolean)].
     */
    static Stream<Arguments> isUnixTimeProvider() {
        // @formatter:off
        return Stream.of(
            Arguments.of("2022-12-27T12:45:22Z", true),
            // Boundary values for 32-bit signed integer (Unix time range)
            Arguments.of("2038-01-19T03:14:07Z", true),  // Max Unix time
            Arguments.of("1901-12-13T20:45:52Z", true),  // Min Unix time
            // Out of bounds values
            Arguments.of("2038-01-19T03:14:08Z", false), // Max Unix time + 1 second
            Arguments.of("1901-12-13T20:45:51Z", false), // Min Unix time - 1 second
            Arguments.of("2099-06-30T12:31:42Z", false)
        );
        // @formatter:on
    }

    @Nested
    @DisplayName("NTFS Time Conversions")
    class NtfsConversionTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeAndInstantProvider")
        @DisplayName("toNtfsTime(FileTime) should convert correctly")
        void toNtfsTime_fromFileTime_shouldReturnCorrectValue(final String instantStr, final long expectedNtfsTime) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeAndInstantProvider")
        @DisplayName("ntfsTimeToFileTime(long) should convert correctly")
        void ntfsTimeToFileTime_shouldReturnCorrectFileTime(final String instantStr, final long ntfsTime) {
            final FileTime expectedFileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(ntfsTime));
        }

        @Test
        @DisplayName("toNtfsTime(long) should handle boundary Java millisecond values by saturating")
        void toNtfsTime_fromJavaMillis_shouldSaturateAtBoundaries() {
            assertEquals(Long.MAX_VALUE, FileTimes.toNtfsTime(Long.MAX_VALUE));
            assertEquals(Long.MIN_VALUE, FileTimes.toNtfsTime(Long.MIN_VALUE));
        }
    }

    @Nested
    @DisplayName("Unix Time Conversions")
    class UnixTimeTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeAndInstantProvider")
        @DisplayName("fromUnixTime(long) should create correct FileTime")
        void fromUnixTime_shouldCreateCorrectFileTime(final String instantStr, final long ignoredNtfsTime) {
            final Instant instant = Instant.parse(instantStr);
            final long epochSecond = instant.getEpochSecond();
            final FileTime resultFileTime = FileTimes.fromUnixTime(epochSecond);
            assertEquals(epochSecond, resultFileTime.to(TimeUnit.SECONDS));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeAndInstantProvider")
        @DisplayName("toUnixTime(FileTime) should return correct epoch seconds")
        void toUnixTime_shouldReturnCorrectEpochSeconds(final String instantStr, final long ignoredNtfsTime) {
            final Instant instant = Instant.parse(instantStr);
            final FileTime fileTime = FileTime.from(instant);
            assertEquals(instant.getEpochSecond(), FileTimes.toUnixTime(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#isUnixTimeProvider")
        @DisplayName("isUnixTime(FileTime) should return expected result")
        void isUnixTime_forFileTime_shouldReturnExpectedResult(final String instantStr, final boolean expected) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expected, FileTimes.isUnixTime(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#isUnixTimeProvider")
        @DisplayName("isUnixTime(long) should return expected result")
        void isUnixTime_forEpochSeconds_shouldReturnExpectedResult(final String instantStr, final boolean expected) {
            final long epochSeconds = Instant.parse(instantStr).getEpochSecond();
            assertEquals(expected, FileTimes.isUnixTime(epochSeconds));
        }

        @Test
        @DisplayName("isUnixTime(null) should return true")
        void isUnixTime_forNullFileTime_shouldReturnTrue() {
            assertTrue(FileTimes.isUnixTime(null));
        }
    }

    @Nested
    @DisplayName("Java Date Conversions")
    class DateConversionTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeAndInstantProvider")
        @DisplayName("toDate(FileTime) should convert correctly")
        void toDate_fromFileTime_shouldReturnCorrectDate(final String instantStr, final long ignoredNtfsTime) {
            final Instant instant = Instant.parse(instantStr);
            final FileTime fileTime = FileTime.from(instant);
            final Date expectedDate = Date.from(instant);
            assertEquals(expectedDate, FileTimes.toDate(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeAndInstantProvider")
        @DisplayName("toFileTime(Date) should convert correctly, truncating to milliseconds")
        void toFileTime_fromDate_shouldReturnCorrectFileTime(final String instantStr, final long ignoredNtfsTime) {
            final Instant instant = Instant.parse(instantStr);
            final Date date = Date.from(instant); // Date truncates to milliseconds
            final Instant instantTruncatedToMillis = instant.truncatedTo(ChronoUnit.MILLIS);
            final FileTime expectedFileTime = FileTime.from(instantTruncatedToMillis);

            assertEquals(expectedFileTime, FileTimes.toFileTime(date));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeAndInstantProvider")
        @DisplayName("ntfsTimeToDate(long) should convert correctly")
        void ntfsTimeToDate_shouldReturnCorrectDate(final String instantStr, final long ntfsTime) {
            final Date expectedDate = Date.from(Instant.parse(instantStr));
            final Date actualDate = FileTimes.ntfsTimeToDate(ntfsTime);
            assertEquals(expectedDate.getTime(), actualDate.getTime());
        }
    }

    @Nested
    @DisplayName("Arithmetic Operations")
    class ArithmeticTests {

        @Test
        @DisplayName("plusNanos should correctly add nanoseconds")
        void plusNanos_shouldCorrectlyAddNanos() {
            assertEquals(Instant.EPOCH.plusNanos(2), FileTimes.plusNanos(FileTimes.EPOCH, 2).toInstant());
            assertEquals(Instant.EPOCH, FileTimes.plusNanos(FileTimes.EPOCH, 0).toInstant());
            assertEquals(Instant.EPOCH.plusNanos(-2), FileTimes.plusNanos(FileTimes.EPOCH, -2).toInstant());
        }
    }
}