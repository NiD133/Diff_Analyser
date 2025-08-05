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
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link FileTimes}.
 */
class FileTimesTest {

    // Test data providers

    /**
     * Provides test cases for NTFS time conversions with boundary values.
     * Each argument contains:
     *   instant: ISO timestamp string
     *   ntfsTime: Expected NTFS time value in 100-nanosecond units
     */
    public static Stream<Arguments> fileTimeNanoUnitsToNtfsProvider() {
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
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),
            Arguments.of("1600-12-31T23:59:59.9990000Z", -FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1600-12-31T23:59:59.9990001Z", -FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1600-12-31T23:59:59.9989999Z", -FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),
            Arguments.of("1970-01-01T00:00:00.0000000Z", -FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", -FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1970-01-01T00:00:00.0010000Z", -FileTimes.UNIX_TO_NTFS_OFFSET + FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1969-12-31T23:59:59.9999999Z", -FileTimes.UNIX_TO_NTFS_OFFSET - 1),
            Arguments.of("1969-12-31T23:59:59.9990000Z", -FileTimes.UNIX_TO_NTFS_OFFSET - FileTimes.HUNDRED_NANOS_PER_MILLISECOND));
        // @formatter:on
    }

    /**
     * Provides test cases for basic NTFS time conversions.
     * Each argument contains:
     *   instant: ISO timestamp string
     *   fileTime: Expected FileTime representation
     */
    public static Stream<Arguments> fileTimeToNtfsProvider() {
        // @formatter:off
        return Stream.of(
            Arguments.of("1970-01-01T00:00:00Z", FileTime.from(Instant.EPOCH)),
            Arguments.of("1969-12-31T23:59:00Z", FileTime.from(Instant.EPOCH.minusSeconds(60))),
            Arguments.of("1970-01-01T00:01:00Z", FileTime.from(Instant.EPOCH.plusSeconds(60))));
        // @formatter:on
    }

    /**
     * Provides test cases for Unix time validation.
     * Each argument contains:
     *   instant: ISO timestamp string
     *   isValidUnixTime: Whether the time fits in standard Unix time range
     */
    public static Stream<Arguments> isUnixFileTimeProvider() {
        // @formatter:off
        return Stream.of(
            Arguments.of("2022-12-27T12:45:22Z", true),
            Arguments.of("2038-01-19T03:14:07Z", true),
            Arguments.of("1901-12-13T23:14:08Z", true),
            Arguments.of("1901-12-13T03:14:08Z", false),
            Arguments.of("2038-01-19T03:14:08Z", false),
            Arguments.of("2099-06-30T12:31:42Z", false));
        // @formatter:on
    }

    // Conversion tests: Date ↔ FileTime

    /**
     * Tests {@link FileTimes#toFileTime(Date)} conversion.
     */
    @ParameterizedTest
    @MethodSource("fileTimeNanoUnitsToNtfsProvider")
    void convertDateToFileTime_ValidDate_ReturnsCorrectFileTime(String instant, long ignored) {
        final Instant parsedInstant = Instant.parse(instant);
        final FileTime parsedFileTime = FileTime.from(parsedInstant);
        final Date parsedDate = Date.from(parsedInstant);
        assertEquals(parsedFileTime.toMillis(), FileTimes.toFileTime(parsedDate).toMillis());
    }

    /**
     * Tests {@link FileTimes#toDate(FileTime)} conversion.
     */
    @ParameterizedTest
    @MethodSource("fileTimeNanoUnitsToNtfsProvider")
    void convertFileTimeToDate_ValidFileTime_ReturnsCorrectDate(String instant, long ignored) {
        final Instant parsedInstant = Instant.parse(instant);
        final FileTime parsedFileTime = FileTime.from(parsedInstant);
        final Date parsedDate = Date.from(parsedInstant);
        assertEquals(parsedDate, FileTimes.toDate(parsedFileTime));
    }

    /**
     * Tests {@link FileTimes#toFileTime(Date)} with null input.
     */
    @Test
    void convertDateToFileTime_NullInput_ReturnsNull() {
        assertNull(FileTimes.toFileTime(null));
    }

    /**
     * Tests {@link FileTimes#toDate(FileTime)} with null input.
     */
    @Test
    void convertFileTimeToDate_NullInput_ReturnsNull() {
        assertNull(FileTimes.toDate(null));
    }

    // Conversion tests: NTFS time ↔ Date/FileTime

    /**
     * Tests {@link FileTimes#toNtfsTime(Date)} conversion.
     */
    @ParameterizedTest
    @MethodSource("fileTimeNanoUnitsToNtfsProvider")
    void convertDateToNtfsTime_ValidDate_ReturnsCorrectNtfsTime(String instantStr, long expectedNtfsTime) {
        final long expectedNtfsMillis = Math.floorDiv(expectedNtfsTime, FileTimes.HUNDRED_NANOS_PER_MILLISECOND) 
            * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;
        final Instant instant = Instant.parse(instantStr);
        final Date parsedDate = Date.from(instant);
        final long actualNtfsTime = FileTimes.toNtfsTime(parsedDate);
        
        if (actualNtfsTime == Long.MIN_VALUE || actualNtfsTime == Long.MAX_VALUE) {
            // Handle edge cases where conversion would overflow
        } else {
            assertEquals(expectedNtfsMillis, actualNtfsTime);
            assertEquals(expectedNtfsMillis, FileTimes.toNtfsTime(parsedDate.getTime()));
            assertEquals(expectedNtfsMillis, FileTimes.toNtfsTime(
                FileTimes.ntfsTimeToInstant(expectedNtfsTime).toEpochMilli()));
        }
        assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(
            FileTimes.ntfsTimeToInstant(expectedNtfsTime)));
    }

    /**
     * Tests {@link FileTimes#toNtfsTime(FileTime)} conversion.
     */
    @ParameterizedTest
    @MethodSource("fileTimeNanoUnitsToNtfsProvider")
    void convertFileTimeToNtfsTime_ValidFileTime_ReturnsCorrectValue(String instant, long expectedNtfsTime) {
        final FileTime parsed = FileTime.from(Instant.parse(instant));
        assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(parsed));
    }

    /**
     * Tests {@link FileTimes#ntfsTimeToFileTime(long)} conversion.
     */
    @ParameterizedTest
    @MethodSource("fileTimeNanoUnitsToNtfsProvider")
    void convertNtfsTimeToFileTime_ValidValue_ReturnsCorrectFileTime(String instantStr, long ntfsTime) {
        final Instant expectedInstant = Instant.parse(instantStr);
        final FileTime expectedFileTime = FileTime.from(expectedInstant);
        assertEquals(expectedInstant, FileTimes.ntfsTimeToInstant(ntfsTime));
        assertEquals(expectedFileTime, FileTimes.ntfsTimeToFileTime(ntfsTime));
    }

    /**
     * Tests {@link FileTimes#ntfsTimeToDate(long)} conversion.
     */
    @ParameterizedTest
    @MethodSource("fileTimeNanoUnitsToNtfsProvider")
    void convertNtfsTimeToDate_ValidValue_ReturnsCorrectDate(String instant, long ntfsTime) {
        assertEquals(Instant.parse(instant).toEpochMilli(), 
            FileTimes.ntfsTimeToDate(ntfsTime).toInstant().toEpochMilli());
    }

    /**
     * Tests round-trip conversion: FileTime → NTFS → FileTime.
     */
    @ParameterizedTest
    @MethodSource("fileTimeToNtfsProvider")
    void fileTimeToNtfsTimeAndBack_RoundTrip_ReturnsOriginalValue(String instantStr, FileTime fileTime) {
        final Instant instant = Instant.parse(instantStr);
        final FileTime parsed = FileTime.from(instant);
        assertEquals(instant, parsed.toInstant());
        assertEquals(fileTime, FileTimes.ntfsTimeToFileTime(FileTimes.toNtfsTime(parsed)));
    }

    // Unix time tests

    /**
     * Tests {@link FileTimes#fromUnixTime(long)} conversion.
     */
    @ParameterizedTest
    @MethodSource("fileTimeNanoUnitsToNtfsProvider")
    void convertUnixTimeToFileTime_ValidValue_ReturnsCorrectFileTime(String instant, long ntfsTime) {
        final long epochSecond = Instant.parse(instant).getEpochSecond();
        assertEquals(epochSecond, FileTimes.fromUnixTime(epochSecond).to(TimeUnit.SECONDS));
    }

    /**
     * Tests {@link FileTimes#isUnixTime(FileTime)} validation.
     */
    @ParameterizedTest
    @MethodSource("isUnixFileTimeProvider")
    void validateUnixTime_WithFileTime_ReturnsCorrectValidation(String instant, boolean isValidUnixTime) {
        assertEquals(isValidUnixTime, FileTimes.isUnixTime(FileTime.from(Instant.parse(instant))));
    }

    /**
     * Tests {@link FileTimes#isUnixTime(long)} validation.
     */
    @ParameterizedTest
    @MethodSource("isUnixFileTimeProvider")
    void validateUnixTime_WithEpochSeconds_ReturnsCorrectValidation(String instant, boolean isValidUnixTime) {
        assertEquals(isValidUnixTime, FileTimes.isUnixTime(Instant.parse(instant).getEpochSecond()));
    }

    /**
     * Tests {@link FileTimes#isUnixTime(FileTime)} with null input.
     */
    @Test
    void validateUnixTime_NullFileTime_ReturnsTrue() {
        assertTrue(FileTimes.isUnixTime(null));
    }

    /**
     * Tests {@link FileTimes#toUnixTime(FileTime)} conversion.
     */
    @ParameterizedTest
    @MethodSource("isUnixFileTimeProvider")
    void convertFileTimeToUnixTime_ValidValue_ReturnsCorrectValue(String instant, boolean ignored) {
        final FileTime fileTime = FileTime.from(Instant.parse(instant));
        assertEquals(FileTimes.isUnixTime(fileTime), 
            FileTimes.isUnixTime(FileTimes.toUnixTime(fileTime)));
    }

    // Epoch constant test

    /**
     * Tests {@link FileTimes#EPOCH} constant value.
     */
    @Test
    void epochConstant_ShouldRepresentUnixEpoch() {
        assertEquals(0, FileTimes.EPOCH.toMillis());
    }

    // Arithmetic operation tests

    /**
     * Tests {@link FileTimes#minusMillis(FileTime, long)}.
     */
    @Test
    void subtractMillisFromFileTime_ReturnsCorrectResult() {
        final int millis = 2;
        assertEquals(Instant.EPOCH.minusMillis(millis), 
            FileTimes.minusMillis(FileTimes.EPOCH, millis).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusMillis(FileTimes.EPOCH, 0).toInstant());
    }

    /**
     * Tests {@link FileTimes#minusNanos(FileTime, long)}.
     */
    @Test
    void subtractNanosFromFileTime_ReturnsCorrectResult() {
        final int nanos = 2;
        assertEquals(Instant.EPOCH.minusNanos(nanos), 
            FileTimes.minusNanos(FileTimes.EPOCH, nanos).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusNanos(FileTimes.EPOCH, 0).toInstant());
    }

    /**
     * Tests {@link FileTimes#minusSeconds(FileTime, long)}.
     */
    @Test
    void subtractSecondsFromFileTime_ReturnsCorrectResult() {
        final int seconds = 2;
        assertEquals(Instant.EPOCH.minusSeconds(seconds), 
            FileTimes.minusSeconds(FileTimes.EPOCH, seconds).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.minusSeconds(FileTimes.EPOCH, 0).toInstant());
    }

    /**
     * Tests {@link FileTimes#plusMillis(FileTime, long)}.
     */
    @Test
    void addMillisToFileTime_ReturnsCorrectResult() {
        final int millis = 2;
        assertEquals(Instant.EPOCH.plusMillis(millis), 
            FileTimes.plusMillis(FileTimes.EPOCH, millis).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.plusMillis(FileTimes.EPOCH, 0).toInstant());
    }

    /**
     * Tests {@link FileTimes#plusNanos(FileTime, long)}.
     */
    @Test
    void addNanosToFileTime_ReturnsCorrectResult() {
        final int nanos = 2;
        assertEquals(Instant.EPOCH.plusNanos(nanos), 
            FileTimes.plusNanos(FileTimes.EPOCH, nanos).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.plusNanos(FileTimes.EPOCH, 0).toInstant());
    }

    /**
     * Tests {@link FileTimes#plusSeconds(FileTime, long)}.
     */
    @Test
    void addSecondsToFileTime_ReturnsCorrectResult() {
        final int seconds = 2;
        assertEquals(Instant.EPOCH.plusSeconds(seconds), 
            FileTimes.plusSeconds(FileTimes.EPOCH, seconds).toInstant());
        assertEquals(Instant.EPOCH, FileTimes.plusSeconds(FileTimes.EPOCH, 0).toInstant());
    }

    // Boundary value tests

    /**
     * Tests {@link FileTimes#toNtfsTime(long)} with maximum Java time value.
     */
    @Test
    void convertMaxJavaTimeToNtfsTime_HandlesBoundary() {
        final long javaTime = Long.MAX_VALUE;
        final Instant instant = Instant.ofEpochMilli(javaTime);
        assertEquals(javaTime, instant.toEpochMilli());
        final long ntfsTime = FileTimes.toNtfsTime(javaTime);
        final Instant convertedInstant = FileTimes.ntfsTimeToInstant(ntfsTime);
        if (ntfsTime == Long.MAX_VALUE) {
            // Handles overflow by returning max value
        } else {
            assertEquals(javaTime, convertedInstant.toEpochMilli());
        }
    }

    /**
     * Tests NTFS conversion with boundary values from parameterized source.
     */
    @ParameterizedTest
    @MethodSource("fileTimeNanoUnitsToNtfsProvider")
    void convertNtfsTimeWithBoundaryValues_HandlesEdgeCases(String instantStr, long ntfsTime) {
        final Instant instant = Instant.ofEpochMilli(ntfsTime); // Note: ntfsTime != epoch millis!
        final long javaTime = instant.toEpochMilli();
        final long convertedNtfsTime = FileTimes.toNtfsTime(javaTime);
        final Instant convertedInstant = FileTimes.ntfsTimeToInstant(convertedNtfsTime);
        
        if (convertedNtfsTime == Long.MIN_VALUE || convertedNtfsTime == Long.MAX_VALUE) {
            // Handles overflow by returning min/max values
        } else {
            assertEquals(javaTime, convertedInstant.toEpochMilli());
        }
    }
}