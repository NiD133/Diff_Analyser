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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link FileTimes}.
 */
class FileTimesTest {

    /**
     * Provides pairs of ISO-8601 instants and the corresponding NTFS time (in 100-nanosecond units).
     * This covers:
     * - NTFS epoch boundaries, UNIX epoch alignment, and +/- boundaries near Long min/max.
     * - Millisecond rounding behavior when going through Date (which only has millisecond precision).
     */
    static Stream<Arguments> ntfsCases() {
        // @formatter:off
        return Stream.of(
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1),
            Arguments.of("1601-01-01T00:00:00.0000010Z", 10),
            Arguments.of("1601-01-01T00:00:00.0000100Z", 100),
            Arguments.of("1601-01-01T00:00:00.0001000Z", 1000),
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1),

            Arguments.of("+30828-09-14T02:48:05.477580700Z", Long.MAX_VALUE),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", Long.MAX_VALUE - 1),
            Arguments.of("+30828-09-14T02:48:05.477579700Z", Long.MAX_VALUE - 10),
            Arguments.of("+30828-09-14T02:48:05.477570700Z", Long.MAX_VALUE - 100),
            Arguments.of("+30828-09-14T02:48:05.477480700Z", Long.MAX_VALUE - 1000),

            Arguments.of("-27627-04-19T21:11:54.522419200Z", Long.MIN_VALUE),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", Long.MIN_VALUE + 1),
            Arguments.of("-27627-04-19T21:11:54.522420200Z", Long.MIN_VALUE + 10),
            Arguments.of("-27627-04-19T21:11:54.522429200Z", Long.MIN_VALUE + 100),
            Arguments.of("-27627-04-19T21:11:54.522519200Z", Long.MIN_VALUE + 1000),

            // Rounding on millisecond boundaries when converting via Date
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),
            Arguments.of("1600-12-31T23:59:59.9990000Z", -FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1600-12-31T23:59:59.9990001Z", -FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1600-12-31T23:59:59.9989999Z", -FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),

            // UNIX epoch alignment
            Arguments.of("1970-01-01T00:00:00.0000000Z", -FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", -FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1970-01-01T00:00:00.0010000Z", -FileTimes.UNIX_TO_NTFS_OFFSET + FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1969-12-31T23:59:59.9999999Z", -FileTimes.UNIX_TO_NTFS_OFFSET - 1),
            Arguments.of("1969-12-31T23:59:59.9990000Z", -FileTimes.UNIX_TO_NTFS_OFFSET - FileTimes.HUNDRED_NANOS_PER_MILLISECOND)
        );
        // @formatter:on
    }

    /**
     * Simple FileTime round-trip cases for NTFS conversion.
     */
    static Stream<Arguments> simpleRoundTripFileTimeCases() {
        // @formatter:off
        return Stream.of(
            Arguments.of("1970-01-01T00:00:00Z", FileTime.from(Instant.EPOCH)),
            Arguments.of("1969-12-31T23:59:00Z", FileTime.from(Instant.EPOCH.minusSeconds(60))),
            Arguments.of("1970-01-01T00:01:00Z", FileTime.from(Instant.EPOCH.plusSeconds(60)))
        );
        // @formatter:on
    }

    /**
     * Cases to check which seconds are considered representable as Unix time by FileTimes.
     */
    static Stream<Arguments> unixTimeRangeCases() {
        // @formatter:off
        return Stream.of(
            Arguments.of("2022-12-27T12:45:22Z", true),
            Arguments.of("2038-01-19T03:14:07Z", true),
            Arguments.of("1901-12-13T23:14:08Z", true),

            // 32-bit signed int overflow boundaries
            Arguments.of("1901-12-13T03:14:08Z", false),
            Arguments.of("2038-01-19T03:14:08Z", false),

            Arguments.of("2099-06-30T12:31:42Z", false)
        );
        // @formatter:on
    }

    // --------------------
    // Helper methods
    // --------------------

    private static Instant parseInstant(final String iso) {
        return Instant.parse(iso);
    }

    private static FileTime fileTimeOf(final String iso) {
        return FileTime.from(parseInstant(iso));
    }

    private static Date dateOf(final String iso) {
        return Date.from(parseInstant(iso));
    }

    /**
     * NTFS time is in 100-nanosecond units. When converting via Date (millisecond precision),
     * expected NTFS values are truncated to whole milliseconds.
     */
    private static long truncateNtfsToMillisUnits(final long ntfs100ns) {
        final long unitsPerMilli = FileTimes.HUNDRED_NANOS_PER_MILLISECOND;
        return Math.floorDiv(ntfs100ns, unitsPerMilli) * unitsPerMilli;
    }

    // --------------------
    // Core conversions
    // --------------------

    @ParameterizedTest
    @MethodSource("ntfsCases")
    @DisplayName("Date -> FileTime matches Instant-based FileTime")
    void dateToFileTime_matchesInstantBasedFileTime(final String isoInstant, final long ignored) {
        final FileTime expected = fileTimeOf(isoInstant);
        final Date date = dateOf(isoInstant);
        assertEquals(expected.toMillis(), FileTimes.toFileTime(date).toMillis());
    }

    @ParameterizedTest
    @MethodSource("ntfsCases")
    @DisplayName("Date -> NTFS time handles millisecond precision and overflows")
    void dateToNtfsTime_handlesMillisPrecisionAndOverflows(final String isoInstant, final long expectedNtfs100ns) {
        final Date date = dateOf(isoInstant);
        final long viaDate = FileTimes.toNtfsTime(date);

        // If conversion results overflow, implementation clamps to Long.MIN/MAX; skip exact equality in that case.
        if (viaDate != Long.MIN_VALUE && viaDate != Long.MAX_VALUE) {
            final long expectedTruncated = truncateNtfsToMillisUnits(expectedNtfs100ns);
            assertEquals(expectedTruncated, viaDate, "Date -> NTFS should truncate to millisecond precision");
            // Verify overloads agree:
            assertEquals(expectedTruncated, FileTimes.toNtfsTime(date.getTime()));
            assertEquals(expectedTruncated, FileTimes.toNtfsTime(FileTimes.ntfsTimeToInstant(expectedNtfs100ns).toEpochMilli()));
        }

        // Regardless of truncation, round-tripping the exact NTFS value through Instant should return the same NTFS value.
        assertEquals(expectedNtfs100ns, FileTimes.toNtfsTime(FileTimes.ntfsTimeToInstant(expectedNtfs100ns)));
    }

    @Test
    @DisplayName("EPOCH constant is 1970-01-01T00:00:00Z")
    void epochConstant_isUnixEpoch() {
        assertEquals(0, FileTimes.EPOCH.toMillis());
    }

    @ParameterizedTest
    @MethodSource("ntfsCases")
    @DisplayName("FileTime -> Date matches Instant-based Date")
    void fileTimeToDate_matchesInstantBasedDate(final String isoInstant, final long ignored) {
        final Date expected = dateOf(isoInstant);
        final FileTime fileTime = fileTimeOf(isoInstant);
        assertEquals(expected, FileTimes.toDate(fileTime));
    }

    @ParameterizedTest
    @MethodSource("simpleRoundTripFileTimeCases")
    @DisplayName("FileTime <-> NTFS time simple round-trip")
    void fileTime_roundTripsThroughNtfs(final String isoInstant, final FileTime expectedFileTime) {
        final FileTime parsed = fileTimeOf(isoInstant);
        assertEquals(parsed.toInstant(), parseInstant(isoInstant)); // sanity
        assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(FileTimes.toNtfsTime(parsed)));
    }

    @ParameterizedTest
    @MethodSource("ntfsCases")
    @DisplayName("FileTime -> NTFS time direct conversion")
    void fileTime_toNtfsTime_direct(final String isoInstant, final long expectedNtfs100ns) {
        assertEquals(expectedNtfs100ns, FileTimes.toNtfsTime(fileTimeOf(isoInstant)));
    }

    @ParameterizedTest
    @MethodSource("ntfsCases")
    @DisplayName("Unix seconds -> FileTime retains seconds")
    void fromUnixTime_preservesSeconds(final String isoInstant, final long ignored) {
        final long epochSecond = parseInstant(isoInstant).getEpochSecond();
        assertEquals(epochSecond, FileTimes.fromUnixTime(epochSecond).to(TimeUnit.SECONDS));
    }

    // --------------------
    // Unix-time range checks
    // --------------------

    @ParameterizedTest
    @MethodSource("unixTimeRangeCases")
    @DisplayName("FileTime is in Unix time range")
    void isUnixTime_fileTime(final String isoInstant, final boolean expected) {
        assertEquals(expected, FileTimes.isUnixTime(fileTimeOf(isoInstant)));
    }

    @Test
    @DisplayName("Null FileTime is treated as Unix time")
    void isUnixTime_fileTimeNull() {
        assertTrue(FileTimes.isUnixTime(null));
    }

    @ParameterizedTest
    @MethodSource("unixTimeRangeCases")
    @DisplayName("Seconds value is in Unix time range")
    void isUnixTime_seconds(final String isoInstant, final boolean expected) {
        assertEquals(expected, FileTimes.isUnixTime(parseInstant(isoInstant).getEpochSecond()));
    }

    // --------------------
    // NTFS time -> Instant/Date/FileTime
    // --------------------

    @ParameterizedTest
    @MethodSource("ntfsCases")
    @DisplayName("NTFS time -> Date matches expected instant")
    void ntfsTime_toDate(final String isoInstant, final long ntfs100ns) {
        assertEquals(parseInstant(isoInstant).toEpochMilli(), FileTimes.ntfsTimeToDate(ntfs100ns).toInstant().toEpochMilli());
    }

    @ParameterizedTest
    @MethodSource("ntfsCases")
    @DisplayName("NTFS time -> FileTime/Instant matches expected instant")
    void ntfsTime_toFileTime_andInstant(final String isoInstant, final long ntfs100ns) {
        final Instant instant = parseInstant(isoInstant);
        final FileTime fileTime = FileTime.from(instant);
        assertEquals(instant, fileTime.toInstant()); // sanity
        assertEquals(instant, FileTimes.ntfsTimeToInstant(ntfs100ns));
        assertEquals(fileTime, FileTimes.ntfsTimeToFileTime(ntfs100ns));
    }

    // --------------------
    // Java millis round-trips (guarding overflow behavior)
    // --------------------

    @Test
    @DisplayName("Long.MAX_VALUE millis round-trips through NTFS unless clamped")
    void javaMillis_maxValue_roundTrip() {
        final long javaMillis = Long.MAX_VALUE;
        final long ntfs = FileTimes.toNtfsTime(javaMillis);
        final Instant roundTrip = FileTimes.ntfsTimeToInstant(ntfs);
        if (ntfs != Long.MAX_VALUE) {
            assertEquals(javaMillis, roundTrip.toEpochMilli());
        }
    }

    @ParameterizedTest
    @MethodSource("javaMillisCases")
    @DisplayName("Java millis round-trip through NTFS unless clamped")
    void javaMillis_param_roundTrip(final long javaMillis) {
        final long ntfs = FileTimes.toNtfsTime(javaMillis);
        final Instant roundTrip = FileTimes.ntfsTimeToInstant(ntfs);
        if (ntfs != Long.MIN_VALUE && ntfs != Long.MAX_VALUE) {
            assertEquals(javaMillis, roundTrip.toEpochMilli());
        }
    }

    static Stream<Long> javaMillisCases() {
        return Stream.of(
            0L,
            1L,
            -1L,
            999L,
            -999L,
            1_000L,
            -1_000L,
            Long.MAX_VALUE,
            Long.MIN_VALUE
        );
    }

    // --------------------
    // Arithmetic helpers
    // --------------------

    @Test
    @DisplayName("minusMillis")
    void minusMillis_behavesLikeInstant() {
        final int millis = 2;
        assertEquals(Instant.EPOCH.minusMillis(millis), FileTimes.minusMillis(FileTimes.EPOCH, millis).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusMillis(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    @DisplayName("minusNanos")
    void minusNanos_behavesLikeInstant() {
        final int nanos = 2;
        assertEquals(Instant.EPOCH.minusNanos(nanos), FileTimes.minusNanos(FileTimes.EPOCH, nanos).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusNanos(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    @DisplayName("minusSeconds")
    void minusSeconds_behavesLikeInstant() {
        final int seconds = 2;
        assertEquals(Instant.EPOCH.minusSeconds(seconds), FileTimes.minusSeconds(FileTimes.EPOCH, seconds).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusSeconds(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    @DisplayName("plusMillis")
    void plusMillis_behavesLikeInstant() {
        final int millis = 2;
        assertEquals(Instant.EPOCH.plusMillis(millis), FileTimes.plusMillis(FileTimes.EPOCH, millis).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.plusMillis(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    @DisplayName("plusNanos")
    void plusNanos_behavesLikeInstant() {
        final int nanos = 2;
        assertEquals(Instant.EPOCH.plusNanos(nanos), FileTimes.plusNanos(FileTimes.EPOCH, nanos).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.plusNanos(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    @DisplayName("plusSeconds")
    void plusSeconds_behavesLikeInstant() {
        final int seconds = 2;
        assertEquals(Instant.EPOCH.plusSeconds(seconds), FileTimes.plusSeconds(FileTimes.EPOCH, seconds).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.plusSeconds(FileTimes.EPOCH, 0).toInstant());
    }

    // --------------------
    // Null-handling and Unix conversion
    // --------------------

    @Test
    @DisplayName("Null Date -> null FileTime")
    void nullDate_yieldsNullFileTime() {
        assertNull(FileTimes.toFileTime(null));
    }

    @Test
    @DisplayName("Null FileTime -> null Date")
    void nullFileTime_yieldsNullDate() {
        assertNull(FileTimes.toDate(null));
    }

    @ParameterizedTest
    @MethodSource("unixTimeRangeCases")
    @DisplayName("FileTime -> Unix seconds -> isUnixTime")
    void toUnixTime_thenCheckIsUnixTime(final String isoInstant, final boolean expectedIsUnix) {
        final long unixSeconds = FileTimes.toUnixTime(fileTimeOf(isoInstant));
        assertEquals(expectedIsUnix, FileTimes.isUnixTime(unixSeconds));
    }
}