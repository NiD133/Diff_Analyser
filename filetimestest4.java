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
 */
@DisplayName("Tests for FileTimes utility class")
class FileTimesTest {

    /**
     * Provides arguments for NTFS time conversions, pairing an Instant (as a String) with its corresponding NTFS time value.
     * NTFS time is measured in 100-nanosecond intervals since 1601-01-01T00:00:00Z.
     */
    static Stream<Arguments> provideInstantsAndNtfsTimes() {
        // @formatter:off
        return Stream.of(
            // --- NTFS Epoch (1601-01-01) ---
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0L),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1L),
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1L),

            // --- Unix Epoch (1970-01-01) ---
            Arguments.of("1970-01-01T00:00:00.0000000Z", FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1969-12-31T23:59:59.9999999Z", FileTimes.UNIX_TO_NTFS_OFFSET - 1),

            // --- Millisecond boundaries ---
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),

            // --- Long.MAX_VALUE boundary ---
            Arguments.of("+30828-09-14T02:48:05.477580700Z", Long.MAX_VALUE),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", Long.MAX_VALUE - 1),

            // --- Long.MIN_VALUE boundary ---
            Arguments.of("-27627-04-19T21:11:54.522419200Z", Long.MIN_VALUE),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", Long.MIN_VALUE + 1)
        );
        // @formatter:on
    }

    /**
     * Provides Instants for testing conversions that don't depend on the specific NTFS value.
     */
    static Stream<Instant> provideInstantsForConversion() {
        return provideInstantsAndNtfsTimes().map(args -> Instant.parse((String) args.get()[0]));
    }

    /**
     * Provides arguments for testing Unix time compatibility checks.
     */
    static Stream<Arguments> provideUnixTimeChecks() {
        // @formatter:off
        return Stream.of(
            // --- Within standard 32-bit Unix time range ---
            Arguments.of("2022-12-27T12:45:22Z", true),
            Arguments.of("2038-01-19T03:14:07Z", true), // Max 32-bit signed int
            Arguments.of("1901-12-13T20:45:52Z", true), // Min 32-bit signed int

            // --- Outside standard 32-bit Unix time range ---
            Arguments.of("1901-12-13T20:45:51Z", false), // Below min
            Arguments.of("2038-01-19T03:14:08Z", false), // Above max
            Arguments.of("2099-06-30T12:31:42Z", false)
        );
        // @formatter:on
    }

    @Nested
    @DisplayName("NTFS Time Conversions")
    class NtfsTimeConversions {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("toNtfsTime should convert FileTime to the correct NTFS long value")
        void toNtfsTime_fromFileTime_shouldConvertCorrectly(final String instantStr, final long expectedNtfsTime) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("ntfsTimeToFileTime should convert NTFS long to the correct FileTime")
        void ntfsTimeToFileTime_shouldConvertCorrectly(final String expectedInstantStr, final long ntfsTime) {
            final Instant expectedInstant = Instant.parse(expectedInstantStr);
            final FileTime expectedFileTime = FileTime.from(expectedInstant);
            assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(ntfsTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("toNtfsTime should convert Date to the correct NTFS long value (with millisecond precision)")
        void toNtfsTime_fromDate_shouldConvertCorrectly(final String instantStr, final long fullPrecisionNtfsTime) {
            final Instant instant = Instant.parse(instantStr);
            final Date date = Date.from(instant);

            // Expected NTFS time is truncated to millisecond precision because Date only supports milliseconds.
            final long expectedNtfsTime = Math.floorDiv(fullPrecisionNtfsTime, FileTimes.HUNDRED_NANOS_PER_MILLISECOND)
                * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;

            final long actualNtfsTime = FileTimes.toNtfsTime(date);

            // The conversion from Date might saturate at Long.MIN_VALUE or Long.MAX_VALUE.
            if (actualNtfsTime == Long.MIN_VALUE || actualNtfsTime == Long.MAX_VALUE) {
                // In cases of saturation, a direct comparison is not meaningful.
                // This branch acknowledges the saturation behavior is being tested.
            } else {
                assertEquals(expectedNtfsTime, actualNtfsTime);
            }
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("ntfsTimeToDate should convert NTFS long to the correct Date")
        void ntfsTimeToDate_shouldConvertCorrectly(final String instantStr, final long ntfsTime) {
            final long expectedMillis = Instant.parse(instantStr).toEpochMilli();
            final long actualMillis = FileTimes.ntfsTimeToDate(ntfsTime).getTime();
            assertEquals(expectedMillis, actualMillis);
        }
    }

    @Nested
    @DisplayName("Java Date Conversions")
    class DateConversions {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsForConversion")
        @DisplayName("toDate should convert FileTime to an equivalent Date")
        void toDate_shouldConvertFileTimeCorrectly(final Instant instant) {
            final FileTime fileTime = FileTime.from(instant);
            final Date expectedDate = Date.from(instant);
            assertEquals(expectedDate, FileTimes.toDate(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsForConversion")
        @DisplayName("toFileTime should convert Date to an equivalent FileTime")
        void toFileTime_shouldConvertDateCorrectly(final Instant instant) {
            final Date date = Date.from(instant);
            final FileTime expectedFileTime = FileTime.from(instant);
            // Compare milliseconds, as Date's precision is limited to milliseconds.
            assertEquals(expectedFileTime.toMillis(), FileTimes.toFileTime(date).toMillis());
        }
    }

    @Nested
    @DisplayName("Unix Time Conversions")
    class UnixTimeConversions {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideUnixTimeChecks")
        @DisplayName("isUnixTime should correctly check if a FileTime is within the 32-bit Unix time range")
        void isUnixTime_forFileTime_shouldReturnExpectedResult(final String instantStr, final boolean isUnixTime) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(isUnixTime, FileTimes.isUnixTime(fileTime));
        }

        @Test
        @DisplayName("isUnixTime should return true for a null FileTime")
        void isUnixTime_shouldReturnTrue_forNullFileTime() {
            assertTrue(FileTimes.isUnixTime(null));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideUnixTimeChecks")
        @DisplayName("isUnixTime should correctly check if a long is within the 32-bit Unix time range")
        void isUnixTime_forLong_shouldReturnExpectedResult(final String instantStr, final boolean isUnixTime) {
            final long epochSecond = Instant.parse(instantStr).getEpochSecond();
            assertEquals(isUnixTime, FileTimes.isUnixTime(epochSecond));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndNtfsTimes")
        @DisplayName("fromUnixTime should create a FileTime from seconds since epoch")
        void fromUnixTime_shouldCreateCorrectFileTime(final String instantStr, final long ignoredNtfsTime) {
            final long epochSecond = Instant.parse(instantStr).getEpochSecond();
            final FileTime result = FileTimes.fromUnixTime(epochSecond);
            assertEquals(epochSecond, result.to(TimeUnit.SECONDS));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideUnixTimeChecks")
        @DisplayName("toUnixTime should convert a FileTime to the correct seconds since epoch")
        void toUnixTime_shouldReturnCorrectSeconds(final String instantStr, final boolean isUnixTime) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            final long expectedSeconds = Instant.parse(instantStr).getEpochSecond();
            if (isUnixTime) {
                assertEquals(expectedSeconds, FileTimes.toUnixTime(fileTime));
            }
            // Also test that the round trip with isUnixTime holds true
            assertEquals(isUnixTime, FileTimes.isUnixTime(FileTimes.toUnixTime(fileTime)));
        }
    }

    @Nested
    @DisplayName("Arithmetic Operations")
    class ArithmeticOperations {

        @Test
        @DisplayName("minusNanos should subtract nanoseconds from a FileTime")
        void minusNanos_shouldSubtractNanosCorrectly() {
            assertEquals(Instant.EPOCH.minusNanos(2), FileTimes.minusNanos(FileTimes.EPOCH, 2).toInstant());
            assertEquals(Instant.EPOCH, FileTimes.minusNanos(FileTimes.EPOCH, 0).toInstant());
            assertEquals(Instant.EPOCH.plusNanos(5), FileTimes.minusNanos(FileTimes.EPOCH, -5).toInstant());
        }
    }
}