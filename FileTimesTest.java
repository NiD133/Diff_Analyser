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
 * Unit tests for {@link FileTimes}.
 */
class FileTimesTest {

    private static final long MAX_LONG = Long.MAX_VALUE;
    private static final long MIN_LONG = Long.MIN_VALUE;

    private static final long HUNDRED_NANOS_PER_MILLISECOND = FileTimes.HUNDRED_NANOS_PER_MILLISECOND;
    private static final long UNIX_TO_NTFS_OFFSET = FileTimes.UNIX_TO_NTFS_OFFSET;

    // Provides test data for NTFS time conversion tests
    public static Stream<Arguments> provideNtfsTimeTestData() {
        return Stream.of(
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1),
            Arguments.of("1601-01-01T00:00:00.0000010Z", 10),
            Arguments.of("1601-01-01T00:00:00.0000100Z", 100),
            Arguments.of("1601-01-01T00:00:00.0001000Z", 1000),
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1),
            Arguments.of("+30828-09-14T02:48:05.477580700Z", MAX_LONG),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", MAX_LONG - 1),
            Arguments.of("+30828-09-14T02:48:05.477579700Z", MAX_LONG - 10),
            Arguments.of("+30828-09-14T02:48:05.477570700Z", MAX_LONG - 100),
            Arguments.of("+30828-09-14T02:48:05.477480700Z", MAX_LONG - 1000),
            Arguments.of("-27627-04-19T21:11:54.522419200Z", MIN_LONG),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", MIN_LONG + 1),
            Arguments.of("-27627-04-19T21:11:54.522420200Z", MIN_LONG + 10),
            Arguments.of("-27627-04-19T21:11:54.522429200Z", MIN_LONG + 100),
            Arguments.of("-27627-04-19T21:11:54.522519200Z", MIN_LONG + 1000),
            Arguments.of("1601-01-01T00:00:00.0010000Z", HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", HUNDRED_NANOS_PER_MILLISECOND - 1),
            Arguments.of("1600-12-31T23:59:59.9990000Z", -HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1600-12-31T23:59:59.9990001Z", -HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1600-12-31T23:59:59.9989999Z", -HUNDRED_NANOS_PER_MILLISECOND - 1),
            Arguments.of("1970-01-01T00:00:00.0000000Z", -UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", -UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1970-01-01T00:00:00.0010000Z", -UNIX_TO_NTFS_OFFSET + HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1969-12-31T23:59:59.9999999Z", -UNIX_TO_NTFS_OFFSET - 1),
            Arguments.of("1969-12-31T23:59:59.9990000Z", -UNIX_TO_NTFS_OFFSET - HUNDRED_NANOS_PER_MILLISECOND)
        );
    }

    // Provides test data for FileTime conversion tests
    public static Stream<Arguments> provideFileTimeTestData() {
        return Stream.of(
            Arguments.of("1970-01-01T00:00:00Z", FileTime.from(Instant.EPOCH)),
            Arguments.of("1969-12-31T23:59:00Z", FileTime.from(Instant.EPOCH.minusSeconds(60))),
            Arguments.of("1970-01-01T00:01:00Z", FileTime.from(Instant.EPOCH.plusSeconds(60)))
        );
    }

    // Provides test data for Unix time checks
    public static Stream<Arguments> provideUnixTimeTestData() {
        return Stream.of(
            Arguments.of("2022-12-27T12:45:22Z", true),
            Arguments.of("2038-01-19T03:14:07Z", true),
            Arguments.of("1901-12-13T23:14:08Z", true),
            Arguments.of("1901-12-13T03:14:08Z", false),
            Arguments.of("2038-01-19T03:14:08Z", false),
            Arguments.of("2099-06-30T12:31:42Z", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNtfsTimeTestData")
    void testDateToFileTimeConversion(final String instantStr, final long ignored) {
        Instant instant = Instant.parse(instantStr);
        FileTime expectedFileTime = FileTime.from(instant);
        Date date = Date.from(instant);
        assertEquals(expectedFileTime.toMillis(), FileTimes.toFileTime(date).toMillis());
    }

    @ParameterizedTest
    @MethodSource("provideNtfsTimeTestData")
    void testDateToNtfsTimeConversion(final String instantStr, final long expectedNtfsTime) {
        Instant instant = Instant.parse(instantStr);
        Date date = Date.from(instant);
        long calculatedNtfsTime = FileTimes.toNtfsTime(date);
        long expectedNtfsMillis = Math.floorDiv(expectedNtfsTime, HUNDRED_NANOS_PER_MILLISECOND) * HUNDRED_NANOS_PER_MILLISECOND;

        if (calculatedNtfsTime == MIN_LONG || calculatedNtfsTime == MAX_LONG) {
            // Handle edge cases where toNtfsTime returns min or max long instead of overflowing
        } else {
            assertEquals(expectedNtfsMillis, calculatedNtfsTime);
            assertEquals(expectedNtfsMillis, FileTimes.toNtfsTime(date.getTime()));
            assertEquals(expectedNtfsMillis, FileTimes.toNtfsTime(FileTimes.ntfsTimeToInstant(expectedNtfsTime).toEpochMilli()));
        }
        assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(FileTimes.ntfsTimeToInstant(expectedNtfsTime)));
    }

    @Test
    void testEpochFileTime() {
        assertEquals(0, FileTimes.EPOCH.toMillis());
    }

    @ParameterizedTest
    @MethodSource("provideNtfsTimeTestData")
    void testFileTimeToDateConversion(final String instantStr, final long ignored) {
        Instant instant = Instant.parse(instantStr);
        FileTime fileTime = FileTime.from(instant);
        Date expectedDate = Date.from(instant);
        assertEquals(expectedDate, FileTimes.toDate(fileTime));
    }

    @ParameterizedTest
    @MethodSource("provideFileTimeTestData")
    void testFileTimeToNtfsTimeConversion(final String instantStr, final FileTime expectedFileTime) {
        Instant instant = Instant.parse(instantStr);
        FileTime fileTime = FileTime.from(instant);
        assertEquals(instant, fileTime.toInstant());
        assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(FileTimes.toNtfsTime(fileTime)));
    }

    @ParameterizedTest
    @MethodSource("provideNtfsTimeTestData")
    void testFileTimeToNtfsTime(final String instantStr, final long expectedNtfsTime) {
        FileTime fileTime = FileTime.from(Instant.parse(instantStr));
        assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(fileTime));
    }

    @ParameterizedTest
    @MethodSource("provideNtfsTimeTestData")
    void testFromUnixTime(final String instantStr, final long expectedNtfsTime) {
        long epochSecond = Instant.parse(instantStr).getEpochSecond();
        assertEquals(epochSecond, FileTimes.fromUnixTime(epochSecond).to(TimeUnit.SECONDS));
    }

    @ParameterizedTest
    @MethodSource("provideUnixTimeTestData")
    void testIsUnixTime(final String instantStr, final boolean expectedIsUnixTime) {
        assertEquals(expectedIsUnixTime, FileTimes.isUnixTime(FileTime.from(Instant.parse(instantStr))));
    }

    @Test
    void testIsUnixTimeWithNullFileTime() {
        assertTrue(FileTimes.isUnixTime(null));
    }

    @ParameterizedTest
    @MethodSource("provideUnixTimeTestData")
    void testIsUnixTimeWithLong(final String instantStr, final boolean expectedIsUnixTime) {
        assertEquals(expectedIsUnixTime, FileTimes.isUnixTime(Instant.parse(instantStr).getEpochSecond()));
    }

    @Test
    void testMaxJavaTimeHandling() {
        long javaTime = MAX_LONG;
        Instant instant = Instant.ofEpochMilli(javaTime);
        assertEquals(javaTime, instant.toEpochMilli()); // Sanity check
        long ntfsTime = FileTimes.toNtfsTime(javaTime);
        Instant convertedInstant = FileTimes.ntfsTimeToInstant(ntfsTime);
        if (ntfsTime == MAX_LONG) {
            // Handle edge case where toNtfsTime returns max long instead of overflowing
        } else {
            assertEquals(javaTime, convertedInstant.toEpochMilli());
        }
    }

    @ParameterizedTest
    @MethodSource("provideNtfsTimeTestData")
    void testMaxJavaTimeParamHandling(final String instantStr, final long javaTime) {
        Instant instant = Instant.ofEpochMilli(javaTime);
        assertEquals(javaTime, instant.toEpochMilli()); // Sanity check
        long ntfsTime = FileTimes.toNtfsTime(javaTime);
        Instant convertedInstant = FileTimes.ntfsTimeToInstant(ntfsTime);
        if (ntfsTime == MIN_LONG || ntfsTime == MAX_LONG) {
            // Handle edge case where toNtfsTime returns min or max long instead of overflowing
        } else {
            assertEquals(javaTime, convertedInstant.toEpochMilli());
        }
    }

    @Test
    void testMinusMillis() {
        int millis = 2;
        assertEquals(Instant.EPOCH.minusMillis(millis), FileTimes.minusMillis(FileTimes.EPOCH, millis).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusMillis(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    void testMinusNanos() {
        int nanos = 2;
        assertEquals(Instant.EPOCH.minusNanos(nanos), FileTimes.minusNanos(FileTimes.EPOCH, nanos).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusNanos(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    void testMinusSeconds() {
        int seconds = 2;
        assertEquals(Instant.EPOCH.minusSeconds(seconds), FileTimes.minusSeconds(FileTimes.EPOCH, seconds).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusSeconds(FileTimes.EPOCH, 0).toInstant());
    }

    @ParameterizedTest
    @MethodSource("provideNtfsTimeTestData")
    void testNtfsTimeToDateConversion(final String instantStr, final long ntfsTime) {
        assertEquals(Instant.parse(instantStr).toEpochMilli(), FileTimes.ntfsTimeToDate(ntfsTime).toInstant().toEpochMilli());
    }

    @ParameterizedTest
    @MethodSource("provideNtfsTimeTestData")
    void testNtfsTimeToFileTimeConversion(final String instantStr, final long ntfsTime) {
        Instant instant = Instant.parse(instantStr);
        FileTime fileTime = FileTime.from(instant);
        assertEquals(instant, fileTime.toInstant()); // Sanity check
        assertEquals(instant, FileTimes.ntfsTimeToInstant(ntfsTime));
        assertEquals(fileTime, FileTimes.ntfsTimeToFileTime(ntfsTime));
    }

    @Test
    void testNullDateToFileTimeConversion() {
        assertNull(FileTimes.toFileTime(null));
    }

    @Test
    void testNullFileTimeToDateConversion() {
        assertNull(FileTimes.toDate(null));
    }

    @Test
    void testPlusMillis() {
        int millis = 2;
        assertEquals(Instant.EPOCH.plusMillis(millis), FileTimes.plusMillis(FileTimes.EPOCH, millis).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.plusMillis(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    void testPlusNanos() {
        int nanos = 2;
        assertEquals(Instant.EPOCH.plusNanos(nanos), FileTimes.plusNanos(FileTimes.EPOCH, nanos).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.plusNanos(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    void testPlusSeconds() {
        int seconds = 2;
        assertEquals(Instant.EPOCH.plusSeconds(seconds), FileTimes.plusSeconds(FileTimes.EPOCH, seconds).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.plusSeconds(FileTimes.EPOCH, 0).toInstant());
    }

    @ParameterizedTest
    @MethodSource("provideUnixTimeTestData")
    void testToUnixTimeConversion(final String instantStr, final boolean expectedIsUnixTime) {
        assertEquals(expectedIsUnixTime, FileTimes.isUnixTime(FileTimes.toUnixTime(FileTime.from(Instant.parse(instantStr)))));
    }
}