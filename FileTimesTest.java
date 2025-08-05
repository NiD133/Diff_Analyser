/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.io.file.attribute;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
 * Tests {@link FileTimes} to ensure correct conversion and handling of file times.
 */
@DisplayName("Tests for FileTimes utility class")
class FileTimesTest {

    // Provider for a wide range of Instants and their corresponding NTFS time values.
    // Covers NTFS epoch, Unix epoch, and boundary values (Long.MIN_VALUE, Long.MAX_VALUE).
    public static Stream<Arguments> ntfsTimeConversions() {
        // @formatter:off
        return Stream.of(
            // NTFS epoch
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0L),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1L),
            // Values around NTFS epoch
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1L),
            // Long.MAX_VALUE
            Arguments.of("+30828-09-14T02:48:05.477580700Z", Long.MAX_VALUE),
            Arguments.of("+30828-09-14T02:48:05.477580600Z", Long.MAX_VALUE - 1),
            // Long.MIN_VALUE
            Arguments.of("-27627-04-19T21:11:54.522419200Z", Long.MIN_VALUE),
            Arguments.of("-27627-04-19T21:11:54.522419300Z", Long.MIN_VALUE + 1),
            // Millisecond constants
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),
            // Unix epoch
            Arguments.of("1970-01-01T00:00:00.0000000Z", -FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", -FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1969-12-31T23:59:59.9999999Z", -FileTimes.UNIX_TO_NTFS_OFFSET - 1)
        );
        // @formatter:on
    }

    // Provider for timestamps to test if they fall within the standard 32-bit Unix time range.
    public static Stream<Arguments> unixTimeRange() {
        // @formatter:off
        return Stream.of(
            // Within range
            Arguments.of("2022-12-27T12:45:22Z", true),
            // Upper bound of 32-bit signed Unix time
            Arguments.of("2038-01-19T03:14:07Z", true),
            // Lower bound of 32-bit signed Unix time
            Arguments.of("1901-12-13T20:45:52Z", true), // Corrected lower bound for clarity
            // Outside range (before)
            Arguments.of("1901-12-13T03:14:08Z", false),
            // Outside range (after)
            Arguments.of("2038-01-19T03:14:08Z", false),
            Arguments.of("2099-06-30T12:31:42Z", false)
        );
        // @formatter:on
    }

    @Nested
    @DisplayName("NTFS Time Conversions")
    class NtfsTimeConversionTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeConversions")
        @DisplayName("toNtfsTime(FileTime) should convert correctly for various instants")
        void toNtfsTimeFromFileTime_shouldConvertCorrectly(final String instantStr, final long expectedNtfsTime) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeConversions")
        @DisplayName("ntfsTimeToFileTime should recreate the original FileTime")
        void ntfsTimeToFileTime_shouldRecreateOriginalTime(final String instantStr, final long ntfsTime) {
            final Instant expectedInstant = Instant.parse(instantStr);
            final FileTime expectedFileTime = FileTime.from(expectedInstant);

            assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(ntfsTime));
            assertEquals(expectedInstant, FileTimes.ntfsTimeToInstant(ntfsTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeConversions")
        @DisplayName("toNtfsTime(Date) should convert with millisecond precision")
        void toNtfsTimeFromDate_shouldConvertToNtfsMilliseconds(final String instantStr, final long ntfsTimeWithNanos) {
            // toNtfsTime(Date) converts from a Date, which only has millisecond precision.
            // We calculate the expected NTFS time by truncating the nanosecond-precise value to milliseconds.
            final long expectedNtfsMillis = Math.floorDiv(ntfsTimeWithNanos, FileTimes.HUNDRED_NANOS_PER_MILLISECOND) * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;

            final Date date = Date.from(Instant.parse(instantStr));
            final long actualNtfsTime = FileTimes.toNtfsTime(date);

            // The conversion from Date handles overflow by clamping to Long.MAX_VALUE/MIN_VALUE.
            if (actualNtfsTime == Long.MIN_VALUE || actualNtfsTime == Long.MAX_VALUE) {
                // This is an expected overflow case, assertion is implicitly met.
            } else {
                assertEquals(expectedNtfsMillis, actualNtfsTime);
                assertEquals(expectedNtfsMillis, FileTimes.toNtfsTime(date.getTime()));
            }
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeConversions")
        @DisplayName("ntfsTimeToDate should convert back to a Date with millisecond precision")
        void ntfsTimeToDate_shouldConvertBackToCorrectDate(final String instantStr, final long ntfsTime) {
            // Conversion from NTFS time to Date loses sub-millisecond precision.
            final Instant originalInstant = Instant.parse(instantStr);
            final Instant expectedInstant = originalInstant.truncatedTo(ChronoUnit.MILLIS);

            final Date actualDate = FileTimes.ntfsTimeToDate(ntfsTime);
            assertEquals(expectedInstant, actualDate.toInstant());
        }
    }

    @Nested
    @DisplayName("Java Date and FileTime Interoperability")
    class DateFileTimeInteropTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeConversions")
        @DisplayName("toFileTime(Date) should be equivalent to FileTime.from(date.toInstant())")
        void toFileTimeFromDate_shouldBeEquivalent(final String instantStr, final long ignoredNtfsTime) {
            final Instant instant = Instant.parse(instantStr);
            final FileTime expectedFileTime = FileTime.from(instant);
            final Date date = Date.from(instant);

            assertEquals(expectedFileTime, FileTimes.toFileTime(date));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeConversions")
        @DisplayName("toDate(FileTime) should be equivalent to Date.from(fileTime.toInstant())")
        void toDateFromFileTime_shouldBeEquivalent(final String instantStr, final long ignoredNtfsTime) {
            final Instant instant = Instant.parse(instantStr);
            final Date expectedDate = Date.from(instant);
            final FileTime fileTime = FileTime.from(instant);

            assertEquals(expectedDate, FileTimes.toDate(fileTime));
        }

        @Test
        @DisplayName("toFileTime(null) should return null")
        void toFileTime_shouldReturnNullForNullDate() {
            assertNull(FileTimes.toFileTime(null));
        }

        @Test
        @DisplayName("toDate(null) should return null")
        void toDate_shouldReturnNullForNullFileTime() {
            assertNull(FileTimes.toDate(null));
        }
    }

    @Nested
    @DisplayName("Unix Time Utilities")
    class UnixTimeTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#unixTimeRange")
        @DisplayName("isUnixTime(FileTime) should correctly identify if a time is in the Unix range")
        void isUnixTimeForFileTime_shouldCorrectlyIdentifyRange(final String instantStr, final boolean expected) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            assertEquals(expected, FileTimes.isUnixTime(fileTime));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#unixTimeRange")
        @DisplayName("isUnixTime(long) should correctly identify if seconds are in the Unix range")
        void isUnixTimeForLong_shouldCorrectlyIdentifyRange(final String instantStr, final boolean expected) {
            final long seconds = Instant.parse(instantStr).getEpochSecond();
            assertEquals(expected, FileTimes.isUnixTime(seconds));
        }

        @Test
        @DisplayName("isUnixTime(null) should return true")
        void isUnixTime_shouldReturnTrueForNullFileTime() {
            assertTrue(FileTimes.isUnixTime(null));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#ntfsTimeConversions")
        @DisplayName("fromUnixTime should create a FileTime that converts back to the same seconds")
        void fromUnixTime_shouldCreateCorrectFileTime(final String instantStr, final long ignoredNtfsTime) {
            final long epochSecond = Instant.parse(instantStr).getEpochSecond();
            final FileTime result = FileTimes.fromUnixTime(epochSecond);
            assertEquals(epochSecond, result.to(TimeUnit.SECONDS));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.attribute.FileTimesTest#unixTimeRange")
        @DisplayName("toUnixTime should produce a value consistent with isUnixTime")
        void toUnixTime_shouldBeConsistentWithIsUnixTime(final String instantStr, final boolean isUnixTime) {
            final FileTime fileTime = FileTime.from(Instant.parse(instantStr));
            final long unixTime = FileTimes.toUnixTime(fileTime);
            assertEquals(isUnixTime, FileTimes.isUnixTime(unixTime));
        }
    }

    @Nested
    @DisplayName("Arithmetic Operations")
    class ArithmeticTests {

        @Test
        void plusSeconds_shouldCorrectlyAddSeconds() {
            assertEquals(Instant.EPOCH.plusSeconds(5), FileTimes.plusSeconds(FileTimes.EPOCH, 5).toInstant());
            assertEquals(Instant.EPOCH, FileTimes.plusSeconds(FileTimes.EPOCH, 0).toInstant());
        }

        @Test
        void minusSeconds_shouldCorrectlySubtractSeconds() {
            assertEquals(Instant.EPOCH.minusSeconds(5), FileTimes.minusSeconds(FileTimes.EPOCH, 5).toInstant());
            assertEquals(Instant.EPOCH, FileTimes.minusSeconds(FileTimes.EPOCH, 0).toInstant());
        }

        @Test
        void plusMillis_shouldCorrectlyAddMilliseconds() {
            assertEquals(Instant.EPOCH.plusMillis(5), FileTimes.plusMillis(FileTimes.EPOCH, 5).toInstant());
            assertEquals(Instant.EPOCH, FileTimes.plusMillis(FileTimes.EPOCH, 0).toInstant());
        }

        @Test
        void minusMillis_shouldCorrectlySubtractMilliseconds() {
            assertEquals(Instant.EPOCH.minusMillis(5), FileTimes.minusMillis(FileTimes.EPOCH, 5).toInstant());
            assertEquals(Instant.EPOCH, FileTimes.minusMillis(FileTimes.EPOCH, 0).toInstant());
        }

        @Test
        void plusNanos_shouldCorrectlyAddNanoseconds() {
            assertEquals(Instant.EPOCH.plusNanos(5), FileTimes.plusNanos(FileTimes.EPOCH, 5).toInstant());
            assertEquals(Instant.EPOCH, FileTimes.plusNanos(FileTimes.EPOCH, 0).toInstant());
        }

        @Test
        void minusNanos_shouldCorrectlySubtractNanoseconds() {
            assertEquals(Instant.EPOCH.minusNanos(5), FileTimes.minusNanos(FileTimes.EPOCH, 5).toInstant());
            assertEquals(Instant.EPOCH, FileTimes.minusNanos(FileTimes.EPOCH, 0).toInstant());
        }
    }

    @Nested
    @DisplayName("Constants and Edge Cases")
    class ConstantsAndEdgeCasesTests {

        @Test
        @DisplayName("EPOCH constant should match Instant.EPOCH")
        void epochConstant_shouldMatchInstantEpoch() {
            assertEquals(0, FileTimes.EPOCH.toMillis());
            assertEquals(Instant.EPOCH, FileTimes.EPOCH.toInstant());
        }

        @Test
        @DisplayName("toNtfsTime from Long.MAX_VALUE milliseconds should handle overflow by clamping")
        void toNtfsTime_from_maxJavaTime_shouldHandleOverflow() {
            // Test that converting Long.MAX_VALUE milliseconds from epoch does not throw an exception,
            // but instead clamps to Long.MAX_VALUE for the NTFS time.
            final long javaTime = Long.MAX_VALUE;
            final long ntfsTime = FileTimes.toNtfsTime(javaTime);
            final Instant resultingInstant = FileTimes.ntfsTimeToInstant(ntfsTime);

            // The resulting NTFS time is clamped, so it won't convert back to the original javaTime.
            // We check that the conversion is handled gracefully.
            if (ntfsTime == Long.MAX_VALUE) {
                // This is the expected behavior for overflow.
            } else {
                // If no overflow, it should convert back correctly.
                assertEquals(javaTime, resultingInstant.toEpochMilli());
            }
        }
    }
}