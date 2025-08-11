/*
 * Copyright 2001-2014 Stephen Colebourne
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Understandable and maintainable tests for {@link GregorianChronology}.
 */
public class GregorianChronologyTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone UTC = DateTimeZone.UTC;

    @Test
    public void isLeapYear_shouldReturnTrue_forLeapYears() {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        
        // A year divisible by 4 but not 100 is a leap year.
        assertTrue("Year 2024 should be a leap year", chronology.isLeapYear(2024));
        
        // A year divisible by 400 is a leap year.
        assertTrue("Year 2000 should be a leap year", chronology.isLeapYear(2000));
        
        // Year 0 is a leap year in the proleptic Gregorian calendar (divisible by 400).
        assertTrue("Year 0 should be a leap year", chronology.isLeapYear(0));
    }

    @Test
    public void isLeapYear_shouldReturnFalse_forCommonYears() {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        
        // A year not divisible by 4 is a common year.
        assertFalse("Year 2025 should not be a leap year", chronology.isLeapYear(2025));
        
        // A year divisible by 100 but not by 400 is a common year.
        assertFalse("Year 1900 should not be a leap year", chronology.isLeapYear(1900));
        assertFalse("Year -900 should not be a leap year", chronology.isLeapYear(-900));
    }

    @Test
    public void calculateFirstDayOfYearMillis_shouldReturnCorrectValueForVariousYears() {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();

        // For the epoch year 1970, the first day is at 0 millis UTC.
        assertEquals(0L, chronology.calculateFirstDayOfYearMillis(1970));

        // For a future year.
        assertEquals(32503680000000L, chronology.calculateFirstDayOfYearMillis(3000));

        // For a year in the distant past.
        assertEquals(-61536067200000L, chronology.calculateFirstDayOfYearMillis(20));

        // For year 0.
        assertEquals(-62167219200000L, chronology.calculateFirstDayOfYearMillis(0));

        // For the minimum supported year.
        assertEquals(-9223372017043200000L, chronology.calculateFirstDayOfYearMillis(-292275054));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInstance_shouldThrowException_whenMinDaysInFirstWeekIsTooLarge() {
        // The valid range for minDaysInFirstWeek is 1-7.
        GregorianChronology.getInstance(UTC, 8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInstance_shouldThrowException_whenMinDaysInFirstWeekIsTooSmall() {
        // The valid range for minDaysInFirstWeek is 1-7.
        GregorianChronology.getInstance(UTC, 0);
    }

    @Test
    public void withUTC_shouldReturnNewUTCInstance_whenChronologyIsNotUTC() {
        GregorianChronology parisChronology = GregorianChronology.getInstance(PARIS);
        Chronology utcChronology = parisChronology.withUTC();

        assertNotSame("A new instance should be returned for a different zone", parisChronology, utcChronology);
        assertEquals("The new instance should have UTC zone", UTC, utcChronology.getZone());
    }

    @Test
    public void withUTC_shouldReturnSameInstance_whenChronologyIsAlreadyUTC() {
        GregorianChronology utcChronology = GregorianChronology.getInstanceUTC();
        Chronology result = utcChronology.withUTC();
        
        assertSame("withUTC() on a UTC chronology should return the same instance", utcChronology, result);
    }

    @Test
    public void withZone_shouldReturnCorrectChronologyInstance() {
        GregorianChronology initialChronology = GregorianChronology.getInstanceUTC();

        // When switching to a different zone, a new instance is returned.
        Chronology parisChronology = initialChronology.withZone(PARIS);
        assertNotSame(initialChronology, parisChronology);
        assertEquals(PARIS, parisChronology.getZone());

        // When switching to the same zone, the same instance is returned.
        Chronology sameChronology = initialChronology.withZone(UTC);
        assertSame(initialChronology, sameChronology);

        // When switching to a null zone, an instance with the default system zone is returned.
        Chronology defaultZoneChronology = initialChronology.withZone(null);
        assertEquals(DateTimeZone.getDefault(), defaultZoneChronology.getZone());
    }

    @Test
    public void constantGetters_shouldReturnExpectedValues() {
        GregorianChronology chronology = GregorianChronology.getInstance();

        assertEquals("Max year", 292278993, chronology.getMaxYear());
        assertEquals("Min year", -292275054, chronology.getMinYear());

        // Average milliseconds per year is based on 365.2425 days.
        assertEquals(31556952000L, chronology.getAverageMillisPerYear());
        assertEquals(15778476000L, chronology.getAverageMillisPerYearDividedByTwo());

        // Average milliseconds per month is (average year / 12).
        assertEquals(2629746000L, chronology.getAverageMillisPerMonth());

        // A constant related to the epoch calculation.
        assertEquals(31083597720000L, chronology.getApproxMillisAtEpochDividedByTwo());
    }

    @Test
    public void assemble_shouldNotThrowException_whenGivenValidFields() {
        GregorianChronology chronology = GregorianChronology.getInstance();
        AssembledChronology.Fields fields = new AssembledChronology.Fields();

        // This method populates the fields object; we just check it runs without error.
        try {
            chronology.assemble(fields);
        } catch (Exception e) {
            fail("assemble() should not have thrown an exception: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void assemble_shouldThrowNullPointerException_whenGivenNullFields() {
        GregorianChronology chronology = GregorianChronology.getInstance();
        chronology.assemble(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getDaysInYearMonth_shouldThrowException_forInvalidMonth() {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        // Month is 1-based, so an invalid month like -2764 will cause an exception
        // when used to access an internal array.
        chronology.getDaysInYearMonth(2024, -2764);
    }

    @Test
    public void chronologyIsRespected_whenConvertingLocalDateTimeToDateTime() {
        // 1. Create a LocalDateTime at a specific instant using the UTC chronology.
        // 199L represents the instant 1970-01-01T00:00:00.199Z.
        LocalDateTime localDateTime = new LocalDateTime(199L, GregorianChronology.getInstanceUTC());

        // 2. Define a different time zone with a non-zero offset.
        DateTimeZone zoneWithOffset = DateTimeZone.forOffsetMillis(-3631);

        // 3. Convert the LocalDateTime to a DateTime in the new time zone.
        // The local time "1970-01-01T00:00:00.199" is now interpreted in the new zone's
        // wall time. The resulting instant in milliseconds from epoch (UTC) is:
        // Millis of local time - zone offset = 199L - (-3631L) = 3830L.
        DateTime dateTime = localDateTime.toDateTime(zoneWithOffset);

        // 4. Verify the resulting instant is correct.
        assertEquals(3830L, dateTime.getMillis());
    }
}