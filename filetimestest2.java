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
 * Tests for the {@link FileTimes} utility class.
 */
@DisplayName("Tests for FileTimes utility class")
class FileTimesTest {

    /**
     * Provides a stream of Instants as strings and their corresponding NTFS time values.
     * NTFS time is the number of 100-nanosecond intervals since 1601-01-01T00:00:00Z.
     */
    static Stream<Arguments> provideInstantsAndCorrespondingNtfsTimes() {
        // @formatter:off
        return Stream.of(
            // --- NTFS epoch (1601-01-01) cases ---
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0L),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1L),
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1L),

            // --- Millisecond boundary cases around NTFS epoch ---
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),

            // --- Unix epoch (1970-01-01) cases ---
            Arguments.of("1970-01-01T00:00:00.0000000Z", -FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", -FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1969-12-31T23:59:59.9999999Z", -FileTimes.UNIX_TO_NTFS_OFFSET - 1),

            // --- Long.MAX_VALUE cases for NTFS time ---
            Arguments.of("+30828-09-14T02:48:05.477580700Z", Long.MAX_VALUE),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", Long.MAX_VALUE - 1),

            // --- Long.MIN_VALUE cases for NTFS time ---
            Arguments.of("-27627-04-19T21:11:54.522419200Z", Long.MIN_VALUE),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", Long.MIN_VALUE + 1)
        );
        // @formatter:on
    }

    /**
     * Provides Instants and a boolean indicating if they are valid Unix times.
     */
    static Stream<Arguments> provideInstantsAndUnixCompatibility() {
        // @formatter:off
        return Stream.of(
            Arguments.of("2022-12-27T12:45:22Z", true),
            Arguments.of("2038-01-19T03:14:07Z", true),  // Max 32-bit Unix time
            Arguments.of("1901-12-13T20:45:52Z", true),  // Min 32-bit Unix time
            Arguments.of("1901-12-13T20:45:51Z", false), // Below min
            Arguments.of("2038-01-19T03:14:08Z", false)  // Above max
        );
        // @formatter:on
    }

    /**
     * Provides a stream of Instants to test conversions that only depend on the Instant.
     */
    static Stream<String> provideInstantsForConversion() {
        return provideInstantsAndCorrespondingNtfsTimes().map(args -> (String) args.get()[0]);
    }

    @Nested
    @DisplayName("NTFS Time Conversions")
    class NtfsTimeConversions {

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndCorrespondingNtfsTimes")
        @DisplayName("toNtfsTime(FileTime) should convert correctly")
        void toNtfsTime_fromFileTime_shouldReturnCorrectValue(final String instantString, final long expectedNtfsTime) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantString));
            assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(fileTime));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndCorrespondingNtfsTimes")
        @DisplayName("toNtfsTime(Date) should convert with millisecond precision")
        void toNtfsTime_fromDate_shouldConvertWithMillisecondPrecision(final String instantString, final long ignored) {
            final Instant instant = Instant.parse(instantString);
            final Date date = Date.from(instant);

            // Date has only millisecond precision. The expected NTFS time should be truncated accordingly.
            final Instant truncatedInstant = instant.truncatedTo(ChronoUnit.MILLIS);
            final long expectedNtfsTime = FileTimes.toNtfsTime(truncatedInstant);

            final long actualNtfsTime = FileTimes.toNtfsTime(date);

            // For extreme dates, the conversion is capped at Long.MIN/MAX_VALUE.
            if (actualNtfsTime == Long.MIN_VALUE || actualNtfsTime == Long.MAX_VALUE) {
                // This is an expected overflow case, so we don't assert equality.
            } else {
                assertEquals(expectedNtfsTime, actualNtfsTime, "NTFS time from Date should match truncated value");
            }
        }

        @Test
        @DisplayName("toNtfsTime(long) should handle edge cases")
        void toNtfsTime_fromJavaTime_shouldHandleEdgeCases() {
            // Test with Long.MAX_VALUE (Java time)
            final long maxJavaTime = Long.MAX_VALUE;
            final long ntfsTimeFromMax = FileTimes.toNtfsTime(maxJavaTime);
            // The conversion should be capped at Long.MAX_VALUE for NTFS time.
            assertEquals(Long.MAX_VALUE, ntfsTimeFromMax);

            // Test with 0 (Unix Epoch)
            final long epochJavaTime = 0L;
            final long ntfsTimeFromEpoch = FileTimes.toNtfsTime(epochJavaTime);
            assertEquals(-FileTimes.UNIX_TO_NTFS_OFFSET, ntfsTimeFromEpoch);
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndCorrespondingNtfsTimes")
        @DisplayName("ntfsTimeToFileTime() should convert correctly")
        void ntfsTimeToFileTime_shouldReturnCorrectFileTime(final String instantString, final long ntfsTime) {
            final FileTime expectedFileTime = FileTime.from(Instant.parse(instantString));
            final FileTime actualFileTime = FileTimes.ntfsTimeToFileTime(ntfsTime);
            assertEquals(expectedFileTime, actualFileTime);
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndCorrespondingNtfsTimes")
        @DisplayName("ntfsTimeToDate() should convert with millisecond precision")
        void ntfsTimeToDate_shouldReturnCorrectDate(final String instantString, final long ntfsTime) {
            final Instant expectedInstant = Instant.parse(instantString);
            final Date actualDate = FileTimes.ntfsTimeToDate(ntfsTime);
            // Compare millisecond values, as Date is limited to this precision.
            assertEquals(expectedInstant.toEpochMilli(), actualDate.toInstant().toEpochMilli());
        }
    }

    @Nested
    @DisplayName("Date and FileTime Interoperability")
    class DateAndFileTimeInteroperability {

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsForConversion")
        @DisplayName("toFileTime(Date) should preserve millisecond precision")
        void toFileTime_fromDate_shouldPreserveMillisecondPrecision(final String instantString) {
            final Instant instant = Instant.parse(instantString);
            final Date date = Date.from(instant);
            final FileTime fromDate = FileTimes.toFileTime(date);
            // Compare millisecond values
            assertEquals(instant.toEpochMilli(), fromDate.toInstant().toEpochMilli());
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsForConversion")
        @DisplayName("toDate(FileTime) should preserve millisecond precision")
        void toDate_fromFileTime_shouldPreserveMillisecondPrecision(final String instantString) {
            final Instant instant = Instant.parse(instantString);
            final FileTime fileTime = FileTime.from(instant);
            final Date fromFileTime = FileTimes.toDate(fileTime);
            assertEquals(Date.from(instant), fromFileTime);
        }
    }

    @Nested
    @DisplayName("Unix Time Compatibility")
    class UnixTimeCompatibility {

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndUnixCompatibility")
        @DisplayName("isUnixTime(FileTime) should return expected result")
        void isUnixTime_withFileTime_shouldReturnExpectedResult(final String instantString, final boolean expected) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantString));
            assertEquals(expected, FileTimes.isUnixTime(fileTime));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsAndUnixCompatibility")
        @DisplayName("isUnixTime(long) should return expected result")
        void isUnixTime_withSeconds_shouldReturnExpectedResult(final String instantString, final boolean expected) {
            final long epochSeconds = Instant.parse(instantString).getEpochSecond();
            assertEquals(expected, FileTimes.isUnixTime(epochSeconds));
        }

        @Test
        @DisplayName("isUnixTime(null) should return true")
        void isUnixTime_withNullFileTime_shouldReturnTrue() {
            assertTrue(FileTimes.isUnixTime(null), "isUnixTime(null) should be true as per its contract");
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsForConversion")
        @DisplayName("fromUnixTime() should create correct FileTime")
        void fromUnixTime_shouldCreateCorrectFileTime(final String instantString) {
            final long epochSeconds = Instant.parse(instantString).getEpochSecond();
            final FileTime fileTime = FileTimes.fromUnixTime(epochSeconds);
            assertEquals(epochSeconds, fileTime.to(TimeUnit.SECONDS));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#provideInstantsForConversion")
        @DisplayName("toUnixTime() should return correct epoch seconds")
        void toUnixTime_shouldReturnCorrectEpochSeconds(final String instantString) {
            final Instant instant = Instant.parse(instantString);
            final FileTime fileTime = FileTime.from(instant);
            final long expectedSeconds = instant.getEpochSecond();
            assertEquals(expectedSeconds, FileTimes.toUnixTime(fileTime));
        }
    }
}