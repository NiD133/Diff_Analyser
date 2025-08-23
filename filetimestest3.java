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
@DisplayName("FileTimes Utility Class")
class FileTimesTest {

    // ---------------------------------------------------------------------------------
    // Data Providers
    // ---------------------------------------------------------------------------------

    /**
     * Provides a comprehensive set of Instants and their equivalent NTFS time values,
     * including epoch boundaries, min/max values, and high-precision times.
     */
    static Stream<Arguments> provideInstantAndNtfsTimeEquivalents() {
        // @formatter:off
        return Stream.of(
            // NTFS epoch and nearby values
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0L),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1L),
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1L),
            // Unix epoch and nearby values
            Arguments.of("1970-01-01T00:00:00.0000000Z", -FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", -FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1969-12-31T23:59:59.9999999Z", -FileTimes.UNIX_TO_NTFS_OFFSET - 1),
            // Millisecond-related values
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),
            // Long.MAX_VALUE and nearby values
            Arguments.of("+30828-09-14T02:48:05.477580700Z", Long.MAX_VALUE),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", Long.MAX_VALUE - 1),
            // Long.MIN_VALUE and nearby values
            Arguments.of("-27627-04-19T21:11:54.522419200Z", Long.MIN_VALUE),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", Long.MIN_VALUE + 1)
        );
        // @formatter:on
    }

    /**
     * Provides Instants and a boolean indicating if they fall within the standard 32-bit Unix time range.
     */
    static Stream<Arguments> provideTimesForUnixRangeCheck() {
        // @formatter:off
        return Stream.of(
            Arguments.of("2022-12-27T12:45:22Z", true),      // Typical value
            Arguments.of("2038-01-19T03:14:07Z", true),      // Max Unix time (32-bit signed)
            Arguments.of("1901-12-13T20:45:52Z", true),      // Min Unix time (32-bit signed)
            Arguments.of("1901-12-13T20:45:51Z", false),     // Just below min
            Arguments.of("2038-01-19T03:14:08Z", false),     // Just above max
            Arguments.of("2099-06-30T12:31:42Z", false)       // Well above max
        );
        // @formatter:on
    }

    // ---------------------------------------------------------------------------------
    // Test Nests
    // ---------------------------------------------------------------------------------

    @Nested
    @DisplayName("NTFS Time Conversions")
    class NtfsTimeConversionTests {

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantAndNtfsTimeEquivalents")
        @DisplayName("should convert from FileTime to NTFS time")
        void toNtfsTime_fromFileTime_shouldReturnCorrectValue(final String instantStr, final long expectedNtfsTime) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(fileTime));
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantAndNtfsTimeEquivalents")
        @DisplayName("should convert from NTFS time to FileTime")
        void ntfsTimeToFileTime_fromNtfsTime_shouldReturnCorrectValue(final String instantStr, final long ntfsTime) {
            final FileTime expectedFileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(ntfsTime));
        }

        @ParameterizedTest(name = "[{index}] For NTFS time {1}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantAndNtfsTimeEquivalents")
        @DisplayName("should handle precision loss in NTFS -> Java time (ms) -> NTFS round trip")
        void ntfsTime_toJavaTime_andBack_shouldBeConsistentAfterTruncation(final String instantStr, final long originalNtfsTime) {
            // Arrange: Convert NTFS time to Java time (milliseconds since epoch), which involves precision loss.
            final long javaTimeMillis = FileTimes.ntfsTimeToInstant(originalNtfsTime).toEpochMilli();

            // Act: Convert the Java time back to NTFS time.
            final long roundTrippedNtfsTime = FileTimes.toNtfsTime(javaTimeMillis);

            // Assert: The result should be the original NTFS time, but truncated to millisecond precision.
            final long expectedNtfsTimeTruncated =
                Math.floorDiv(originalNtfsTime, FileTimes.HUNDRED_NANOS_PER_MILLISECOND) * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;

            // The SUT clamps on overflow, so we only check for non-clamped values.
            if (originalNtfsTime != Long.MIN_VALUE && originalNtfsTime != Long.MAX_VALUE) {
                assertEquals(expectedNtfsTimeTruncated, roundTrippedNtfsTime);
            } else {
                // If the original was a boundary value, the round-tripped value might also be a boundary.
                assertTrue(roundTrippedNtfsTime == Long.MIN_VALUE || roundTrippedNtfsTime == Long.MAX_VALUE);
            }
        }
    }

    @Nested
    @DisplayName("java.util.Date Conversions")
    class DateConversionTests {

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantAndNtfsTimeEquivalents")
        @DisplayName("should convert from Date to FileTime, truncating sub-millisecond precision")
        void toFileTime_from_Date_shouldTruncateSubMilliseconds(final String instantStr) {
            final Instant sourceInstant = Instant.parse(instantStr);
            final Date date = Date.from(sourceInstant); // This truncates to milliseconds

            final Instant expectedInstant = sourceInstant.truncatedTo(ChronoUnit.MILLIS);
            final FileTime expectedFileTime = FileTime.from(expectedInstant);

            assertEquals(expectedFileTime, FileTimes.toFileTime(date));
        }



        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantAndNtfsTimeEquivalents")
        @DisplayName("should convert from FileTime to Date, truncating sub-millisecond precision")
        void toDate_fromFileTime_shouldTruncateSubMilliseconds(final String instantStr) {
            final Instant sourceInstant = Instant.parse(instantStr);
            final FileTime fileTime = FileTime.from(sourceInstant);

            final Instant expectedInstant = sourceInstant.truncatedTo(ChronoUnit.MILLIS);
            final Date expectedDate = Date.from(expectedInstant);

            assertEquals(expectedDate, FileTimes.toDate(fileTime));
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantAndNtfsTimeEquivalents")
        @DisplayName("should convert from Date to NTFS time using millisecond precision")
        void toNtfsTime_from_Date_shouldUseMillisecondPrecision(final String instantStr) {
            // Arrange: Create a Date from an Instant, which truncates to millisecond precision.
            final Instant sourceInstant = Instant.parse(instantStr);
            final Date dateWithMillisecondPrecision = Date.from(sourceInstant);

            // Calculate the expected NTFS time based on the truncated (millisecond-precision) instant.
            final Instant truncatedInstant = sourceInstant.truncatedTo(ChronoUnit.MILLIS);
            final long expectedNtfsTime = FileTimes.toNtfsTime(FileTime.from(truncatedInstant));

            // Act: Convert the Date to NTFS time.
            final long actualNtfsTime = FileTimes.toNtfsTime(dateWithMillisecondPrecision);

            // Assert
            assertEquals(expectedNtfsTime, actualNtfsTime);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantAndNtfsTimeEquivalents")
        @DisplayName("should convert from NTFS time to Date, truncating sub-millisecond precision")
        void ntfsTimeToDate_fromNtfsTime_shouldTruncateSubMilliseconds(final String instantStr, final long ntfsTime) {
            final Instant sourceInstant = Instant.parse(instantStr);
            final Instant expectedInstant = sourceInstant.truncatedTo(ChronoUnit.MILLIS);
            final Date expectedDate = Date.from(expectedInstant);

            assertEquals(expectedDate, FileTimes.ntfsTimeToDate(ntfsTime));
        }
    }

    @Nested
    @DisplayName("Unix Time Compatibility")
    class UnixTimeCompatibilityTests {

        @ParameterizedTest(name = "[{index}] {0} -> isUnixTime: {1}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideTimesForUnixRangeCheck")
        @DisplayName("isUnixTime(FileTime) should correctly identify if a time is in the Unix range")
        void isUnixTime_forFileTime_shouldCorrectlyIdentifyRange(final String instantStr, final boolean expected) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expected, FileTimes.isUnixTime(fileTime));
        }

        @Test
        @DisplayName("isUnixTime(FileTime) should return true for a null input")
        void isUnixTime_shouldReturnTrue_forNullFileTime() {
            assertTrue(FileTimes.isUnixTime(null));
        }

        @ParameterizedTest(name = "[{index}] {0} -> isUnixTime: {1}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideTimesForUnixRangeCheck")
        @DisplayName("isUnixTime(long) should correctly identify if seconds are in the Unix range")
        void isUnixTime_forEpochSeconds_shouldCorrectlyIdentifyRange(final String instantStr, final boolean expected) {
            final long epochSeconds = Instant.parse(instantStr).getEpochSecond();
            assertEquals(expected, FileTimes.isUnixTime(epochSeconds));
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideTimesForUnixRangeCheck")
        @DisplayName("toUnixTime should return the correct epoch seconds from a FileTime")
        void toUnixTime_shouldReturnCorrectEpochSeconds(final String instantStr, final boolean ignored) {
            final Instant instant = Instant.parse(instantStr);
            final FileTime fileTime = FileTime.from(instant);
            assertEquals(instant.getEpochSecond(), FileTimes.toUnixTime(fileTime));
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideTimesForUnixRangeCheck")
        @DisplayName("fromUnixTime should return the correct FileTime from epoch seconds")
        void fromUnixTime_shouldReturnCorrectFileTime(final String instantStr, final boolean ignored) {
            final long epochSecond = Instant.parse(instantStr).getEpochSecond();
            final FileTime expectedFileTime = FileTime.from(epochSecond, TimeUnit.SECONDS);
            assertEquals(expectedFileTime, FileTimes.fromUnixTime(epochSecond));
        }
    }

    @Test
    @DisplayName("minusMillis should subtract milliseconds correctly")
    void minusMillis_shouldSubtractCorrectly() {
        assertEquals(Instant.EPOCH.minusMillis(2), FileTimes.minusMillis(FileTimes.EPOCH, 2).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusMillis(FileTimes.EPOCH, 0).toInstant());
    }
}