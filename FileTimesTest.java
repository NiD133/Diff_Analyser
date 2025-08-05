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
 * Tests {@link FileTimes} utility class for file time conversions and operations.
 * 
 * This test suite covers:
 * - Conversions between Date, FileTime, and NTFS time formats
 * - Unix time validation and conversion
 * - Time arithmetic operations (plus/minus seconds, milliseconds, nanoseconds)
 * - Edge cases and boundary conditions
 */
class FileTimesTest {

    // Test data providers

    /**
     * Provides test data for NTFS time conversion tests.
     * Each argument contains an ISO instant string and its corresponding NTFS time value.
     * NTFS time represents 100-nanosecond intervals since January 1, 1601 UTC.
     */
    public static Stream<Arguments> ntfsTimeConversionTestData() {
        return Stream.of(
            // NTFS epoch (1601-01-01) and basic increments
            Arguments.of("1601-01-01T00:00:00.0000000Z", 0L),
            Arguments.of("1601-01-01T00:00:00.0000001Z", 1L),
            Arguments.of("1601-01-01T00:00:00.0000010Z", 10L),
            Arguments.of("1601-01-01T00:00:00.0000100Z", 100L),
            Arguments.of("1601-01-01T00:00:00.0001000Z", 1000L),
            
            // Before NTFS epoch
            Arguments.of("1600-12-31T23:59:59.9999999Z", -1L),
            
            // Boundary values
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
            
            // Millisecond precision tests
            Arguments.of("1601-01-01T00:00:00.0010000Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1601-01-01T00:00:00.0010001Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1601-01-01T00:00:00.0009999Z", FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),
            Arguments.of("1600-12-31T23:59:59.9990000Z", -FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1600-12-31T23:59:59.9990001Z", -FileTimes.HUNDRED_NANOS_PER_MILLISECOND + 1),
            Arguments.of("1600-12-31T23:59:59.9989999Z", -FileTimes.HUNDRED_NANOS_PER_MILLISECOND - 1),
            
            // Unix epoch (1970-01-01) relative to NTFS time
            Arguments.of("1970-01-01T00:00:00.0000000Z", -FileTimes.UNIX_TO_NTFS_OFFSET),
            Arguments.of("1970-01-01T00:00:00.0000001Z", -FileTimes.UNIX_TO_NTFS_OFFSET + 1),
            Arguments.of("1970-01-01T00:00:00.0010000Z", -FileTimes.UNIX_TO_NTFS_OFFSET + FileTimes.HUNDRED_NANOS_PER_MILLISECOND),
            Arguments.of("1969-12-31T23:59:59.9999999Z", -FileTimes.UNIX_TO_NTFS_OFFSET - 1),
            Arguments.of("1969-12-31T23:59:59.9990000Z", -FileTimes.UNIX_TO_NTFS_OFFSET - FileTimes.HUNDRED_NANOS_PER_MILLISECOND)
        );
    }

    /**
     * Provides test data for FileTime to NTFS conversion tests with common timestamps.
     */
    public static Stream<Arguments> fileTimeToNtfsTestData() {
        return Stream.of(
            Arguments.of("1970-01-01T00:00:00Z", FileTime.from(Instant.EPOCH)),
            Arguments.of("1969-12-31T23:59:00Z", FileTime.from(Instant.EPOCH.minusSeconds(60))),
            Arguments.of("1970-01-01T00:01:00Z", FileTime.from(Instant.EPOCH.plusSeconds(60)))
        );
    }

    /**
     * Provides test data for Unix time validation tests.
     * Tests whether timestamps fall within the valid Unix time range (1901-2038).
     */
    public static Stream<Arguments> unixTimeValidationTestData() {
        return Stream.of(
            // Valid Unix times
            Arguments.of("2022-12-27T12:45:22Z", true),
            Arguments.of("2038-01-19T03:14:07Z", true),  // Last valid second
            Arguments.of("1901-12-13T23:14:08Z", true),  // First valid second after 1901
            
            // Invalid Unix times (outside 32-bit signed integer range)
            Arguments.of("1901-12-13T03:14:08Z", false), // Before Unix time range
            Arguments.of("2038-01-19T03:14:08Z", false), // After Unix time range
            Arguments.of("2099-06-30T12:31:42Z", false)  // Far future
        );
    }

    // Date conversion tests

    @ParameterizedTest
    @MethodSource("ntfsTimeConversionTestData")
    void shouldConvertDateToFileTimeCorrectly(final String instantString, final long ignoredNtfsTime) {
        // Given: A date created from an instant
        final Instant instant = Instant.parse(instantString);
        final Date date = Date.from(instant);
        final FileTime expectedFileTime = FileTime.from(instant);
        
        // When: Converting the date to FileTime
        final FileTime actualFileTime = FileTimes.toFileTime(date);
        
        // Then: The conversion should preserve millisecond precision
        assertEquals(expectedFileTime.toMillis(), actualFileTime.toMillis());
    }

    @ParameterizedTest
    @MethodSource("ntfsTimeConversionTestData")
    void shouldConvertDateToNtfsTimeCorrectly(final String instantString, final long expectedNtfsTime) {
        // Given: A date and its expected NTFS time
        final Instant instant = Instant.parse(instantString);
        final Date date = Date.from(instant);
        
        // Calculate expected NTFS time rounded to millisecond precision
        final long expectedNtfsMillis = Math.floorDiv(expectedNtfsTime, FileTimes.HUNDRED_NANOS_PER_MILLISECOND) 
                                      * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;
        
        // When: Converting the date to NTFS time
        final long actualNtfsTime = FileTimes.toNtfsTime(date);
        
        // Then: Handle overflow cases or verify correct conversion
        if (actualNtfsTime == Long.MIN_VALUE || actualNtfsTime == Long.MAX_VALUE) {
            // toNtfsTime returns boundary values instead of overflowing - this is expected behavior
        } else {
            assertEquals(expectedNtfsMillis, actualNtfsTime);
            assertEquals(expectedNtfsMillis, FileTimes.toNtfsTime(date.getTime()));
            assertEquals(expectedNtfsMillis, FileTimes.toNtfsTime(FileTimes.ntfsTimeToInstant(expectedNtfsTime).toEpochMilli()));
        }
        
        // Verify round-trip conversion for full precision
        assertEquals(expectedNtfsTime, FileTimes.toNtfsTime(FileTimes.ntfsTimeToInstant(expectedNtfsTime)));
    }

    @ParameterizedTest
    @MethodSource("ntfsTimeConversionTestData")
    void shouldConvertFileTimeToDateCorrectly(final String instantString, final long ignoredNtfsTime) {
        // Given: A FileTime and corresponding Date
        final Instant instant = Instant.parse(instantString);
        final FileTime fileTime = FileTime.from(instant);
        final Date expectedDate = Date.from(instant);
        
        // When: Converting FileTime to Date
        final Date actualDate = FileTimes.toDate(fileTime);
        
        // Then: The dates should be equal
        assertEquals(expectedDate, actualDate);
    }

    // FileTime conversion tests

    @ParameterizedTest
    @MethodSource("fileTimeToNtfsTestData")
    void shouldConvertFileTimeToNtfsTimeAndBack(final String instantString, final FileTime fileTime) {
        // Given: An instant and corresponding FileTime
        final Instant instant = Instant.parse(instantString);
        final FileTime parsedFileTime = FileTime.from(instant);
        
        // When: Converting to NTFS time and back
        final long ntfsTime = FileTimes.toNtfsTime(parsedFileTime);
        final FileTime roundTripFileTime = FileTimes.ntfsTimeToFileTime(ntfsTime);
        
        // Then: The round-trip conversion should preserve the original value
        assertEquals(instant, parsedFileTime.toInstant());
        assertEquals(fileTime, roundTripFileTime);
    }

    @ParameterizedTest
    @MethodSource("ntfsTimeConversionTestData")
    void shouldConvertFileTimeToNtfsTimeCorrectly(final String instantString, final long expectedNtfsTime) {
        // Given: A FileTime created from an instant
        final FileTime fileTime = FileTime.from(Instant.parse(instantString));
        
        // When: Converting to NTFS time
        final long actualNtfsTime = FileTimes.toNtfsTime(fileTime);
        
        // Then: The NTFS time should match expected value
        assertEquals(expectedNtfsTime, actualNtfsTime);
    }

    // NTFS time conversion tests

    @ParameterizedTest
    @MethodSource("ntfsTimeConversionTestData")
    void shouldConvertNtfsTimeToDateCorrectly(final String instantString, final long ntfsTime) {
        // Given: An NTFS time value
        final long expectedMillis = Instant.parse(instantString).toEpochMilli();
        
        // When: Converting NTFS time to Date
        final Date actualDate = FileTimes.ntfsTimeToDate(ntfsTime);
        
        // Then: The date should have the correct millisecond value
        assertEquals(expectedMillis, actualDate.toInstant().toEpochMilli());
    }

    @ParameterizedTest
    @MethodSource("ntfsTimeConversionTestData")
    void shouldConvertNtfsTimeToFileTimeCorrectly(final String instantString, final long ntfsTime) {
        // Given: An NTFS time and expected instant
        final Instant expectedInstant = Instant.parse(instantString);
        final FileTime expectedFileTime = FileTime.from(expectedInstant);
        
        // When: Converting NTFS time to FileTime and Instant
        final Instant actualInstant = FileTimes.ntfsTimeToInstant(ntfsTime);
        final FileTime actualFileTime = FileTimes.ntfsTimeToFileTime(ntfsTime);
        
        // Then: Both conversions should match expected values
        assertEquals(expectedInstant, expectedFileTime.toInstant()); // sanity check
        assertEquals(expectedInstant, actualInstant);
        assertEquals(expectedFileTime, actualFileTime);
    }

    // Unix time tests

    @ParameterizedTest
    @MethodSource("ntfsTimeConversionTestData")
    void shouldCreateFileTimeFromUnixTimeCorrectly(final String instantString, final long ignoredNtfsTime) {
        // Given: Unix timestamp (seconds since epoch)
        final long unixSeconds = Instant.parse(instantString).getEpochSecond();
        
        // When: Creating FileTime from Unix time
        final FileTime fileTime = FileTimes.fromUnixTime(unixSeconds);
        
        // Then: The FileTime should represent the same number of seconds
        assertEquals(unixSeconds, fileTime.to(TimeUnit.SECONDS));
    }

    @ParameterizedTest
    @MethodSource("unixTimeValidationTestData")
    void shouldValidateUnixTimeRangeForFileTime(final String instantString, final boolean expectedIsValid) {
        // Given: A FileTime created from an instant
        final FileTime fileTime = FileTime.from(Instant.parse(instantString));
        
        // When: Checking if it's within Unix time range
        final boolean actualIsValid = FileTimes.isUnixTime(fileTime);
        
        // Then: The validation should match expected result
        assertEquals(expectedIsValid, actualIsValid);
    }

    @Test
    void shouldReturnTrueForNullFileTimeInUnixValidation() {
        // When: Checking if null FileTime is valid Unix time
        final boolean result = FileTimes.isUnixTime((FileTime) null);
        
        // Then: Should return true (null is considered valid)
        assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("unixTimeValidationTestData")
    void shouldValidateUnixTimeRangeForLongValue(final String instantString, final boolean expectedIsValid) {
        // Given: Unix timestamp in seconds
        final long unixSeconds = Instant.parse(instantString).getEpochSecond();
        
        // When: Checking if the timestamp is within Unix time range
        final boolean actualIsValid = FileTimes.isUnixTime(unixSeconds);
        
        // Then: The validation should match expected result
        assertEquals(expectedIsValid, actualIsValid);
    }

    @ParameterizedTest
    @MethodSource("unixTimeValidationTestData")
    void shouldConvertToUnixTimeAndValidateConsistently(final String instantString, final boolean expectedIsValid) {
        // Given: A FileTime
        final FileTime fileTime = FileTime.from(Instant.parse(instantString));
        
        // When: Converting to Unix time and validating
        final long unixTime = FileTimes.toUnixTime(fileTime);
        final boolean actualIsValid = FileTimes.isUnixTime(unixTime);
        
        // Then: The validation should be consistent
        assertEquals(expectedIsValid, actualIsValid);
    }

    // Time arithmetic tests

    @Test
    void shouldAddMillisecondsToFileTime() {
        // Given: A base FileTime and milliseconds to add
        final int millisToAdd = 2;
        final Instant expectedInstant = Instant.EPOCH.plusMillis(millisToAdd);
        
        // When: Adding milliseconds
        final FileTime result = FileTimes.plusMillis(FileTimes.EPOCH, millisToAdd);
        
        // Then: The result should match expected instant
        assertEquals(expectedInstant, result.toInstant());
        
        // And: Adding zero should return the same time
        assertEquals(Instant.EPOCH, FileTimes.plusMillis(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    void shouldSubtractMillisecondsFromFileTime() {
        // Given: A base FileTime and milliseconds to subtract
        final int millisToSubtract = 2;
        final Instant expectedInstant = Instant.EPOCH.minusMillis(millisToSubtract);
        
        // When: Subtracting milliseconds
        final FileTime result = FileTimes.minusMillis(FileTimes.EPOCH, millisToSubtract);
        
        // Then: The result should match expected instant
        assertEquals(expectedInstant, result.toInstant());
        
        // And: Subtracting zero should return the same time
        assertEquals(Instant.EPOCH, FileTimes.minusMillis(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    void shouldAddNanosecondsToFileTime() {
        // Given: A base FileTime and nanoseconds to add
        final int nanosToAdd = 2;
        final Instant expectedInstant = Instant.EPOCH.plusNanos(nanosToAdd);
        
        // When: Adding nanoseconds
        final FileTime result = FileTimes.plusNanos(FileTimes.EPOCH, nanosToAdd);
        
        // Then: The result should match expected instant
        assertEquals(expectedInstant, result.toInstant());
        
        // And: Adding zero should return the same time
        assertEquals(Instant.EPOCH, FileTimes.plusNanos(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    void shouldSubtractNanosecondsFromFileTime() {
        // Given: A base FileTime and nanoseconds to subtract
        final int nanosToSubtract = 2;
        final Instant expectedInstant = Instant.EPOCH.minusNanos(nanosToSubtract);
        
        // When: Subtracting nanoseconds
        final FileTime result = FileTimes.minusNanos(FileTimes.EPOCH, nanosToSubtract);
        
        // Then: The result should match expected instant
        assertEquals(expectedInstant, result.toInstant());
        
        // And: Subtracting zero should return the same time
        assertEquals(Instant.EPOCH, FileTimes.minusNanos(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    void shouldAddSecondsToFileTime() {
        // Given: A base FileTime and seconds to add
        final int secondsToAdd = 2;
        final Instant expectedInstant = Instant.EPOCH.plusSeconds(secondsToAdd);
        
        // When: Adding seconds
        final FileTime result = FileTimes.plusSeconds(FileTimes.EPOCH, secondsToAdd);
        
        // Then: The result should match expected instant
        assertEquals(expectedInstant, result.toInstant());
        
        // And: Adding zero should return the same time
        assertEquals(Instant.EPOCH, FileTimes.plusSeconds(FileTimes.EPOCH, 0).toInstant());
    }

    @Test
    void shouldSubtractSecondsFromFileTime() {
        // Given: A base FileTime and seconds to subtract
        final int secondsToSubtract = 2;
        final Instant expectedInstant = Instant.EPOCH.minusSeconds(secondsToSubtract);
        
        // When: Subtracting seconds
        final FileTime result = FileTimes.minusSeconds(FileTimes.EPOCH, secondsToSubtract);
        
        // Then: The result should match expected instant
        assertEquals(expectedInstant, result.toInstant());
        
        // And: Subtracting zero should return the same time
        assertEquals(Instant.EPOCH, FileTimes.minusSeconds(FileTimes.EPOCH, 0).toInstant());
    }

    // Null handling tests

    @Test
    void shouldReturnNullWhenConvertingNullDateToFileTime() {
        // When: Converting null Date to FileTime
        final FileTime result = FileTimes.toFileTime(null);
        
        // Then: Should return null
        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenConvertingNullFileTimeToDate() {
        // When: Converting null FileTime to Date
        final Date result = FileTimes.toDate(null);
        
        // Then: Should return null
        assertNull(result);
    }

    // Constants and edge cases

    @Test
    void shouldHaveEpochConstantWithZeroMillis() {
        // When: Checking the EPOCH constant
        final long epochMillis = FileTimes.EPOCH.toMillis();
        
        // Then: Should be zero (Unix epoch)
        assertEquals(0, epochMillis);
    }

    @Test
    void shouldHandleMaxJavaTimeConversion() {
        // Given: Maximum Java time value
        final long maxJavaTime = Long.MAX_VALUE;
        final Instant instant = Instant.ofEpochMilli(maxJavaTime);
        
        // When: Converting to NTFS time and back
        final long ntfsTime = FileTimes.toNtfsTime(maxJavaTime);
        final Instant convertedInstant = FileTimes.ntfsTimeToInstant(ntfsTime);
        
        // Then: Should handle overflow gracefully
        assertEquals(maxJavaTime, instant.toEpochMilli()); // sanity check
        if (ntfsTime == Long.MAX_VALUE) {
            // toNtfsTime returns max long instead of overflowing - this is expected
        } else {
            assertEquals(maxJavaTime, convertedInstant.toEpochMilli());
        }
    }

    @ParameterizedTest
    @MethodSource("ntfsTimeConversionTestData")
    void shouldHandleJavaTimeConversionWithParameterizedData(final String instantString, final long javaTime) {
        // Given: A Java time value (reusing NTFS test data)
        final Instant instant = Instant.ofEpochMilli(javaTime);
        
        // When: Converting to NTFS time and back
        final long ntfsTime = FileTimes.toNtfsTime(javaTime);
        final Instant convertedInstant = FileTimes.ntfsTimeToInstant(ntfsTime);
        
        // Then: Should handle conversion correctly or gracefully overflow
        assertEquals(javaTime, instant.toEpochMilli()); // sanity check
        if (ntfsTime == Long.MIN_VALUE || ntfsTime == Long.MAX_VALUE) {
            // toNtfsTime returns boundary values instead of overflowing - this is expected
        } else {
            assertEquals(javaTime, convertedInstant.toEpochMilli());
        }
    }
}