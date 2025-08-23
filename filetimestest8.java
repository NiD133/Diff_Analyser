package org.apache.commons.io.file.attribute;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link FileTimes}.
 */
@DisplayName("FileTimes Utility Class")
class FileTimesTest {

    /**
     * Provides a comprehensive set of Instants and their equivalent NTFS time values,
     * covering various boundary conditions.
     */
    public static Stream<Arguments> provideNtfsTimeAndEquivalentInstant() {
        // @formatter:off
        return Stream.of(
            // --- NTFS epoch (1601-01-01) cases ---
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0L),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1L),
            Arguments.of("1601-01-01T00:00:00.0000010Z", 10L),
            Arguments.of("1601-01-01T00:00:00.0001000Z", 1000L),
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1L),

            // --- Long.MAX_VALUE boundary cases for NTFS time ---
            Arguments.of("+30828-09-14T02:48:05.477580700Z", Long.MAX_VALUE),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", Long.MAX_VALUE - 1),
            Arguments.of("+30828-09-14T02:48:05.477570700Z", Long.MAX_VALUE - 100),

            // --- Long.MIN_VALUE boundary cases for NTFS time ---
            Arguments.of("-27627-04-19T21:11:54.522419200Z", Long.MIN_VALUE),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", Long.MIN_VALUE + 1),
            Arguments.of("-27627-04-19T21:11:54.522429200Z", Long.MIN_VALUE + 100),

            // --- Millisecond conversion boundary cases ---
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),
            Arguments.of("1600-12-31T23:59:59.9990000Z", -FileTimes.HUNDRED_NANOS_PER_MILLISECOND),

            // --- Unix epoch (1970-01-01) cases ---
            Arguments.of("1970-01-01T00:00:00.0000000Z", -FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", -FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1969-12-31T23:59:59.9999999Z", -FileTimes.UNIX_TO_NTFS_OFFSET - 1)
        );
        // @formatter:on
    }

    /**
     * Provides Instants and a boolean indicating if they are valid Unix times.
     */
    public static Stream<Arguments> provideInstantsAndUnixTimeCompatibility() {
        // @formatter:off
        return Stream.of(
            Arguments.of("2022-12-27T12:45:22Z", true),
            Arguments.of("2038-01-19T03:14:07Z", true),  // Max 32-bit signed int
            Arguments.of("1901-12-13T23:14:08Z", true),  // Min 32-bit signed int
            Arguments.of("1901-12-13T03:14:08Z", false), // Before min
            Arguments.of("2038-01-19T03:14:08Z", false), // After max
            Arguments.of("2099-06-30T12:31:42Z", false)
        );
        // @formatter:on
    }

    @DisplayName("toFileTime(Date) should convert Date to equivalent FileTime")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideNtfsTimeAndEquivalentInstant")
    void toFileTime_shouldConvertDateToEquivalentFileTime(final String instantStr, final long ignoredNtfsTime) {
        final Instant instant = Instant.parse(instantStr);
        final Date date = Date.from(instant);
        final FileTime expectedFileTime = FileTime.from(instant);

        // Date has only millisecond precision, so we compare the converted time at that precision.
        assertEquals(expectedFileTime.toMillis(), FileTimes.toFileTime(date).toMillis());
    }

    @DisplayName("toNtfsTime(Date) should convert correctly, losing sub-millisecond precision")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideNtfsTimeAndEquivalentInstant")
    void toNtfsTime_from_Date_shouldLoseNanosecondPrecision(final String instantStr, final long ntfsTimeWithNanos) {
        final Instant instant = Instant.parse(instantStr);
        final Date date = Date.from(instant);

        // The expected NTFS time will be truncated to the nearest millisecond because Date has no nanosecond precision.
        final long expectedNtfsTime = Math.floorDiv(ntfsTimeWithNanos, FileTimes.HUNDRED_NANOS_PER_MILLISECOND) * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;
        final long actualNtfsTime = FileTimes.toNtfsTime(date);

        // The SUT clamps to Long.MIN_VALUE/MAX_VALUE on overflow, so we only check non-boundary values.
        if (actualNtfsTime != Long.MIN_VALUE && actualNtfsTime != Long.MAX_VALUE) {
            assertEquals(expectedNtfsTime, actualNtfsTime);
        }
    }

    @DisplayName("toDate(FileTime) should convert FileTime to equivalent Date")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideNtfsTimeAndEquivalentInstant")
    void toDate_shouldConvertFileTimeToEquivalentDate(final String instantStr, final long ignoredNtfsTime) {
        final Instant instant = Instant.parse(instantStr);
        final FileTime fileTime = FileTime.from(instant);
        final Date expectedDate = Date.from(instant);

        assertEquals(expectedDate, FileTimes.toDate(fileTime));
    }

    @DisplayName("toNtfsTime(FileTime) and ntfsTimeToFileTime(long) should be reversible")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideNtfsTimeAndEquivalentInstant")
    void conversionToFileTimeAndBack_shouldBeReversible(final String instantStr, final long ignoredNtfsTime) {
        final FileTime originalFileTime = FileTime.from(Instant.parse(instantStr));

        final long ntfsTime = FileTimes.toNtfsTime(originalFileTime);
        final FileTime resultFileTime = FileTimes.ntfsTimeToFileTime(ntfsTime);

        assertEquals(originalFileTime, resultFileTime);
    }

    @DisplayName("toNtfsTime(FileTime) should convert with nanosecond precision")
    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @MethodSource("provideNtfsTimeAndEquivalentInstant")
    void toNtfsTime_from_FileTime_shouldConvertWithNanosecondPrecision(final String instantStr, final long expectedNtfsTime) {
        final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
        assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(fileTime));
    }

    @DisplayName("fromUnixTime(long) should convert seconds to equivalent FileTime")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideNtfsTimeAndEquivalentInstant")
    void fromUnixTime_shouldConvertSecondsToEquivalentFileTime(final String instantStr, final long ignoredNtfsTime) {
        final long epochSecond = Instant.parse(instantStr).getEpochSecond();
        assertEquals(epochSecond, FileTimes.fromUnixTime(epochSecond).to(TimeUnit.SECONDS));
    }

    @DisplayName("isUnixTime(FileTime) should return correct flag")
    @ParameterizedTest(name = "[{index}] For {0}, isUnixTime should be {1}")
    @MethodSource("provideInstantsAndUnixTimeCompatibility")
    void isUnixTime_forFileTime_shouldReturnCorrectFlag(final String instantStr, final boolean expected) {
        final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
        assertEquals(expected, FileTimes.isUnixTime(fileTime));
    }

    @Test
    @DisplayName("isUnixTime(FileTime) should return true for a null FileTime")
    void isUnixTime_shouldReturnTrue_forNullFileTime() {
        assertTrue(FileTimes.isUnixTime(null));
    }

    @DisplayName("isUnixTime(long) should return correct flag for epoch seconds")
    @ParameterizedTest(name = "[{index}] For {0}, isUnixTime should be {1}")
    @MethodSource("provideInstantsAndUnixTimeCompatibility")
    void isUnixTime_forEpochSeconds_shouldReturnCorrectFlag(final String instantStr, final boolean expected) {
        final long epochSeconds = Instant.parse(instantStr).getEpochSecond();
        assertEquals(expected, FileTimes.isUnixTime(epochSeconds));
    }

    @DisplayName("ntfsTimeToDate(long) should convert NTFS time to equivalent Date")
    @ParameterizedTest(name = "[{index}] {1} -> {0}")
    @MethodSource("provideNtfsTimeAndEquivalentInstant")
    void ntfsTimeToDate_shouldConvertNtfsTimeToEquivalentDate(final String instantStr, final long ntfsTime) {
        final long expectedMillis = Instant.parse(instantStr).toEpochMilli();
        final long actualMillis = FileTimes.ntfsTimeToDate(ntfsTime).toInstant().toEpochMilli();
        assertEquals(expectedMillis, actualMillis);
    }

    @DisplayName("ntfsTimeToFileTime(long) should convert NTFS time to equivalent FileTime")
    @ParameterizedTest(name = "[{index}] {1} -> {0}")
    @MethodSource("provideNtfsTimeAndEquivalentInstant")
    void ntfsTimeToFileTime_shouldConvertNtfsTimeToEquivalentFileTime(final String instantStr, final long ntfsTime) {
        final Instant expectedInstant = Instant.parse(instantStr);
        final FileTime expectedFileTime = FileTime.from(expectedInstant);

        assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(ntfsTime));
        assertEquals(expectedInstant, FileTimes.ntfsTimeToInstant(ntfsTime));
    }

    @DisplayName("toUnixTime(FileTime) should convert to correct epoch seconds")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideInstantsAndUnixTimeCompatibility")
    void toUnixTime_shouldConvertFileTimeCorrectly(final String instantStr, final boolean ignoredIsUnixTime) {
        final Instant instant = Instant.parse(instantStr);
        final FileTime fileTime = FileTime.from(instant);
        final long expectedSeconds = instant.getEpochSecond();

        final long actualSeconds = FileTimes.toUnixTime(fileTime);

        assertEquals(expectedSeconds, actualSeconds);
    }

    @Test
    @DisplayName("plusMillis should add milliseconds to a FileTime")
    void plusMillis_shouldAddMilliseconds() {
        assertEquals(Instant.EPOCH.plusMillis(2), FileTimes.plusMillis(FileTimes.EPOCH, 2).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.plusMillis(FileTimes.EPOCH, 0).toInstant());
        assertEquals(Instant.EPOCH.minusMillis(2), FileTimes.plusMillis(FileTimes.EPOCH, -2).toInstant());
    }

    @Test
    @DisplayName("minusMillis should subtract milliseconds from a FileTime")
    void minusMillis_shouldSubtractMilliseconds() {
        assertEquals(Instant.EPOCH.minusMillis(2), FileTimes.minusMillis(FileTimes.EPOCH, 2).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusMillis(FileTimes.EPOCH, 0).toInstant());
        assertEquals(Instant.EPOCH.plusMillis(2), FileTimes.minusMillis(FileTimes.EPOCH, -2).toInstant());
    }
}