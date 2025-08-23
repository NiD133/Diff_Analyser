package org.apache.commons.io.file.attribute;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Provides arguments for NTFS time conversion tests, mapping an Instant string to its
     * corresponding NTFS time value (100-nanosecond intervals since 1601-01-01).
     */
    public static Stream<Arguments> createInstantToNtfsTimeArguments() {
        // @formatter:off
        return Stream.of(
            // --- NTFS Epoch (1601-01-01T00:00:00Z) ---
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0L),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1L),
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1L),

            // --- Unix Epoch (1970-01-01T00:00:00Z) ---
            Arguments.of("1970-01-01T00:00:00.0000000Z", -FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", -FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1969-12-31T23:59:59.9999999Z", -FileTimes.UNIX_TO_NTFS_OFFSET - 1),

            // --- Millisecond boundaries ---
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1600-12-31T23:59:59.9990000Z", -FileTimes.HUNDRED_NANOS_PER_MILLISECOND),

            // --- Long.MAX_VALUE boundary cases ---
            Arguments.of("+30828-09-14T02:48:05.477580700Z", Long.MAX_VALUE),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", Long.MAX_VALUE - 1),
            Arguments.of("+30828-09-14T02:48:05.477570700Z", Long.MAX_VALUE - 100),

            // --- Long.MIN_VALUE boundary cases ---
            Arguments.of("-27627-04-19T21:11:54.522419200Z", Long.MIN_VALUE),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", Long.MIN_VALUE + 1),
            Arguments.of("-27627-04-19T21:11:54.522429200Z", Long.MIN_VALUE + 100)
        );
        // @formatter:on
    }

    /**
     * Provides arguments for testing conversions around the Unix epoch with full-second precision.
     */
    public static Stream<Arguments> createEpochSecondFileTimeArguments() {
        return Stream.of(
            Arguments.of("1970-01-01T00:00:00Z", FileTime.from(Instant.EPOCH)),
            Arguments.of("1969-12-31T23:59:00Z", FileTime.from(Instant.EPOCH.minusSeconds(60))),
            Arguments.of("1970-01-01T00:01:00Z", FileTime.from(Instant.EPOCH.plusSeconds(60)))
        );
    }

    /**
     * Provides arguments for testing {@link FileTimes#isUnixTime(FileTime)}.
     */
    public static Stream<Arguments> createIsUnixTimeArguments() {
        // @formatter:off
        return Stream.of(
            // Valid Unix times
            Arguments.of("2022-12-27T12:45:22Z", true),
            Arguments.of("2038-01-19T03:14:07Z", true), // Max 32-bit signed int
            Arguments.of("1901-12-13T23:14:08Z", true), // Min 32-bit signed int is 1901-12-13T20:45:52Z, this is close
            // Invalid Unix times (outside 32-bit range)
            Arguments.of("1901-12-13T03:14:08Z", false),
            Arguments.of("2038-01-19T03:14:08Z", false),
            Arguments.of("2099-06-30T12:31:42Z", false)
        );
        // @formatter:on
    }

    @ParameterizedTest
    @MethodSource("createInstantToNtfsTimeArguments")
    void toFileTime_shouldConvertDateToEquivalentFileTime(final String instantStr, final long ignoredNtfsTime) {
        final Instant instant = Instant.parse(instantStr);
        final Date date = Date.from(instant);
        final FileTime expectedFileTime = FileTime.from(instant);

        // Compare milliseconds, as Date and FileTime from Instant may differ in sub-millisecond precision.
        assertEquals(expectedFileTime.toMillis(), FileTimes.toFileTime(date).toMillis());
    }

    @ParameterizedTest
    @MethodSource("createInstantToNtfsTimeArguments")
    void toDate_shouldConvertFileTimeToEquivalentDate(final String instantStr, final long ignoredNtfsTime) {
        final Instant instant = Instant.parse(instantStr);
        final FileTime fileTime = FileTime.from(instant);
        final Date expectedDate = Date.from(instant);

        assertEquals(expectedDate, FileTimes.toDate(fileTime));
    }

    @ParameterizedTest
    @MethodSource("createInstantToNtfsTimeArguments")
    void toNtfsTime_fromFileTime_shouldMatchExpectedValue(final String instantStr, final long expectedNtfsTime) {
        final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
        assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(fileTime));
    }

    @ParameterizedTest
    @MethodSource("createInstantToNtfsTimeArguments")
    void toNtfsTime_fromDate_shouldLoseSubMillisecondPrecision(final String instantStr, final long ntfsTime) {
        final Date date = Date.from(Instant.parse(instantStr));
        final long actualNtfsTime = FileTimes.toNtfsTime(date);

        // Since Date has only millisecond precision, the resulting NTFS time should be truncated.
        final long expectedNtfsTimeTruncated = Math.floorDiv(ntfsTime, FileTimes.HUNDRED_NANOS_PER_MILLISECOND) * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;

        // For values at the edge of the Long range, the conversion saturates instead of overflowing.
        if (actualNtfsTime == Long.MIN_VALUE || actualNtfsTime == Long.MAX_VALUE) {
            // This is expected saturation behavior for out-of-range dates.
        } else {
            assertEquals(expectedNtfsTimeTruncated, actualNtfsTime);
        }
    }

    @Test
    void toNtfsTime_fromJavaMillis_shouldHandleEpoch() {
        // The number of 100-nanosecond intervals between the Windows and Unix epochs.
        final long expected = -FileTimes.UNIX_TO_NTFS_OFFSET;
        assertEquals(expected, FileTimes.toNtfsTime(0L)); // Unix epoch in millis
    }

    @Test
    void toNtfsTime_fromJavaMillis_shouldHandleMaxAndMinValues() {
        // Test that converting Long.MAX_VALUE milliseconds saturates to Long.MAX_VALUE.
        assertEquals(Long.MAX_VALUE, FileTimes.toNtfsTime(Long.MAX_VALUE));
        // Test that converting Long.MIN_VALUE milliseconds saturates to Long.MIN_VALUE.
        assertEquals(Long.MIN_VALUE, FileTimes.toNtfsTime(Long.MIN_VALUE));
    }

    @ParameterizedTest
    @MethodSource("createInstantToNtfsTimeArguments")
    void ntfsTimeToFileTime_shouldConvertNtfsToEquivalentFileTime(final String instantStr, final long ntfsTime) {
        final Instant expectedInstant = Instant.parse(instantStr);
        final FileTime expectedFileTime = FileTime.from(expectedInstant);

        assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(ntfsTime));
        assertEquals(expectedInstant, FileTimes.ntfsTimeToInstant(ntfsTime));
    }

    @ParameterizedTest
    @MethodSource("createEpochSecondFileTimeArguments")
    void ntfsTimeToFileTime_roundtrip_shouldBeLosslessForFullSeconds(final String ignoredInstantStr, final FileTime fileTime) {
        final long ntfsTime = FileTimes.toNtfsTime(fileTime);
        final FileTime roundTripFileTime = FileTimes.ntfsTimeToFileTime(ntfsTime);
        assertEquals(fileTime, roundTripFileTime);
    }

    @ParameterizedTest
    @MethodSource("createInstantToNtfsTimeArguments")
    void ntfsTimeToDate_shouldConvertNtfsToEquivalentDate(final String instantStr, final long ntfsTime) {
        // Compare epoch milliseconds because Date only has millisecond precision.
        final long expectedMillis = Instant.parse(instantStr).toEpochMilli();
        final long actualMillis = FileTimes.ntfsTimeToDate(ntfsTime).getTime();
        assertEquals(expectedMillis, actualMillis);
    }

    @ParameterizedTest
    @MethodSource("createInstantToNtfsTimeArguments")
    void fromUnixTime_shouldCreateCorrectFileTime(final String instantStr, final long ignoredNtfsTime) {
        final long epochSecond = Instant.parse(instantStr).getEpochSecond();
        final FileTime result = FileTimes.fromUnixTime(epochSecond);
        assertEquals(epochSecond, result.to(TimeUnit.SECONDS));
    }

    @ParameterizedTest
    @MethodSource("createIsUnixTimeArguments")
    void isUnixTime_forFileTime_shouldReturnExpectedResult(final String instantStr, final boolean expected) {
        final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
        assertEquals(expected, FileTimes.isUnixTime(fileTime));
    }

    @Test
    void isUnixTime_shouldReturnTrue_whenFileTimeIsNull() {
        assertTrue(FileTimes.isUnixTime(null));
    }

    @ParameterizedTest
    @MethodSource("createIsUnixTimeArguments")
    void isUnixTime_forLongSeconds_shouldReturnExpectedResult(final String instantStr, final boolean expected) {
        final long epochSecond = Instant.parse(instantStr).getEpochSecond();
        assertEquals(expected, FileTimes.isUnixTime(epochSecond));
    }

    @ParameterizedTest
    @MethodSource("createIsUnixTimeArguments")
    void toUnixTime_shouldProduceValueConsistentWithIsUnixTime(final String instantStr, final boolean isUnixTime) {
        final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
        final long unixTime = FileTimes.toUnixTime(fileTime);
        assertEquals(isUnixTime, FileTimes.isUnixTime(unixTime));
    }

    @Test
    void minusSeconds_shouldSubtractSecondsFromFileTime() {
        assertEquals(Instant.EPOCH.minusSeconds(2), FileTimes.minusSeconds(FileTimes.EPOCH, 2).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusSeconds(FileTimes.EPOCH, 0).toInstant());
    }
}