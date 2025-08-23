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
 *
 * <p>This class is organized using {@link Nested} test classes to group tests
 * by the functionality they cover, such as NTFS time conversion, Date conversion,
 * and Unix time checks.</p>
 */
@DisplayName("Tests for FileTimes utility class")
class FileTimesTest {

    /**
     * Provides test cases for conversions between {@link Instant} or {@link FileTime}
     * and the 64-bit NTFS time format.
     *
     * @return a stream of arguments with an ISO instant string and its corresponding NTFS time.
     */
    static Stream<Arguments> ntfsConversionCases() {
        // @formatter:off
        return Stream.of(
            // --- NTFS epoch (1601-01-01T00:00:00Z) cases ---
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0L),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1L),
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1L),

            // --- Unix epoch (1970-01-01T00:00:00Z) cases ---
            Arguments.of("1970-01-01T00:00:00.0000000Z", FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1969-12-31T23:59:59.9999999Z", FileTimes.UNIX_TO_NTFS_OFFSET - 1),

            // --- Millisecond-related cases ---
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),

            // --- Long.MAX_VALUE cases ---
            Arguments.of("+30828-09-14T02:48:05.477580700Z", Long.MAX_VALUE),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", Long.MAX_VALUE - 1),
            Arguments.of("+30828-09-14T02:48:05.477570700Z", Long.MAX_VALUE - 100),

            // --- Long.MIN_VALUE cases ---
            Arguments.of("-27627-04-19T21:11:54.522419200Z", Long.MIN_VALUE),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", Long.MIN_VALUE + 1),
            Arguments.of("-27627-04-19T21:11:54.522429200Z", Long.MIN_VALUE + 100)
        );
        // @formatter:on
    }

    /**
     * Provides test cases for checking if a time falls within the standard 32-bit Unix time range.
     *
     * @return a stream of arguments with an ISO instant string and a boolean indicating if it's a valid Unix time.
     */
    static Stream<Arguments> unixTimeCheckCases() {
        // @formatter:off
        return Stream.of(
            // --- Times within the 32-bit Unix time range ---
            Arguments.of("2022-12-27T12:45:22Z", true),
            Arguments.of("1970-01-01T00:00:00Z", true),
            // Max 32-bit signed int (2147483647)
            Arguments.of("2038-01-19T03:14:07Z", true),
            // Min 32-bit signed int (-2147483648)
            Arguments.of("1901-12-13T20:45:52Z", true),

            // --- Times outside the 32-bit Unix time range ---
            // One second after max
            Arguments.of("2038-01-19T03:14:08Z", false),
            // One second before min
            Arguments.of("1901-12-13T20:45:51Z", false),
            Arguments.of("2099-06-30T12:31:42Z", false)
        );
        // @formatter:on
    }

    @Nested
    @DisplayName("NTFS Time Conversion")
    class NtfsTimeConversionTests {

        @DisplayName("Should convert FileTime to NTFS time")
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsConversionCases")
        void toNtfsTimeFromFileTime(final String instantStr, final long expectedNtfsTime) {
            // Arrange
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));

            // Act
            final long actualNtfsTime = FileTimes.toNtfsTime(fileTime);

            // Assert
            assertEquals(expectedNtfsTime, actualNtfsTime);
        }

        @DisplayName("Should convert NTFS time to FileTime")
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsConversionCases")
        void ntfsTimeToFileTime(final String instantStr, final long ntfsTime) {
            // Arrange
            final FileTime expectedFileTime = FileTime.from(Instant.parse(instantStr));

            // Act
            final FileTime actualFileTime = FileTimes.ntfsTimeToFileTime(ntfsTime);

            // Assert
            assertEquals(expectedFileTime, actualFileTime);
        }

        @DisplayName("Should perform a round-trip conversion from Java time (ms) to NTFS time and back")
        @Test
        void javaTimeToNtfsTimeRoundTrip() {
            // Arrange
            final long javaTimeMillis = System.currentTimeMillis();
            final Instant originalInstant = Instant.ofEpochMilli(javaTimeMillis);

            // Act
            final long ntfsTime = FileTimes.toNtfsTime(javaTimeMillis);
            final Instant roundTripInstant = FileTimes.ntfsTimeToInstant(ntfsTime);

            // Assert
            assertEquals(originalInstant, roundTripInstant);
        }
    }

    @Nested
    @DisplayName("Date Conversion")
    class DateConversionTests {

        @DisplayName("Should perform a round-trip conversion between Date and FileTime")
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsConversionCases")
        void dateAndFileTimeRoundTrip(final String instantStr) {
            // Arrange
            final Instant instant = Instant.parse(instantStr);
            final Date originalDate = Date.from(instant);
            final FileTime originalFileTime = FileTime.from(instant);

            // Act & Assert
            assertEquals(originalDate, FileTimes.toDate(originalFileTime), "FileTime to Date conversion failed");
            assertEquals(originalFileTime.toMillis(), FileTimes.toFileTime(originalDate).toMillis(), "Date to FileTime conversion failed (ms precision)");
        }



        @DisplayName("Should convert Date to NTFS time, respecting millisecond precision")
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsConversionCases")
        void toNtfsTimeFromDate(final String instantStr, final long fullPrecisionNtfsTime) {
            // Arrange
            final Date date = Date.from(Instant.parse(instantStr));
            // The expected NTFS time, but truncated to millisecond precision, as that's all a Date holds.
            final long expectedNtfsTimeAtMsPrecision =
                Math.floorDiv(fullPrecisionNtfsTime, FileTimes.HUNDRED_NANOS_PER_MILLISECOND) * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;

            // Act
            final long actualNtfsTime = FileTimes.toNtfsTime(date);

            // Assert
            // For extreme values, the conversion can saturate at Long.MIN/MAX_VALUE.
            if (actualNtfsTime == Long.MIN_VALUE || actualNtfsTime == Long.MAX_VALUE) {
                // This is expected for out-of-range dates, so we accept this saturation.
            } else {
                assertEquals(expectedNtfsTimeAtMsPrecision, actualNtfsTime);
            }
        }

        @DisplayName("Should convert NTFS time to Date, respecting millisecond precision")
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsConversionCases")
        void ntfsTimeToDate(final String instantStr, final long ntfsTime) {
            // Arrange
            final Instant instant = Instant.parse(instantStr);
            final long expectedMillis = instant.toEpochMilli();

            // Act
            final Date actualDate = FileTimes.ntfsTimeToDate(ntfsTime);

            // Assert
            assertEquals(expectedMillis, actualDate.getTime());
        }
    }

    @Nested
    @DisplayName("Unix Time Checks")
    class UnixTimeTests {

        @DisplayName("Should correctly identify if a FileTime is within the Unix time range")
        @ParameterizedTest(name = "{0} is Unix time: {1}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#unixTimeCheckCases")
        void isUnixTimeForFileTime(final String instantStr, final boolean expected) {
            // Arrange
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));

            // Act & Assert
            assertEquals(expected, FileTimes.isUnixTime(fileTime));
        }

        @DisplayName("Should correctly identify if seconds are within the Unix time range")
        @ParameterizedTest(name = "{0} is Unix time: {1}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#unixTimeCheckCases")
        void isUnixTimeForSeconds(final String instantStr, final boolean expected) {
            // Arrange
            final long seconds = Instant.parse(instantStr).getEpochSecond();

            // Act & Assert
            assertEquals(expected, FileTimes.isUnixTime(seconds));
        }

        @DisplayName("Should perform a round-trip conversion from FileTime to Unix time (seconds)")
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#unixTimeCheckCases")
        void toUnixTimeRoundTrip(final String instantStr) {
            // Arrange
            final FileTime originalFileTime = FileTime.from(Instant.parse(instantStr));
            final long expectedSeconds = originalFileTime.to(TimeUnit.SECONDS);

            // Act
            final long unixTime = FileTimes.toUnixTime(originalFileTime);
            final FileTime roundTripFileTime = FileTimes.fromUnixTime(unixTime);

            // Assert
            assertEquals(expectedSeconds, roundTripFileTime.to(TimeUnit.SECONDS));
        }
    }

    @Nested
    @DisplayName("Null Input Handling")
    class NullInputTests {

        @Test
        @DisplayName("toFileTime(null) should return null")
        void toFileTimeWithNullDateReturnsNull() {
            assertNull(FileTimes.toFileTime(null));
        }

        @Test
        @DisplayName("toDate(null) should return null")
        void toDateWithNullFileTimeReturnsNull() {
            assertNull(FileTimes.toDate(null));
        }

        @Test
        @DisplayName("isUnixTime(null) should return true")
        void isUnixTimeWithNullFileTimeReturnsTrue() {
            assertTrue(FileTimes.isUnixTime(null));
        }
    }
}