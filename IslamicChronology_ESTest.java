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
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A comprehensive test suite for the IslamicChronology class, focusing on clarity and correctness.
 */
public class IslamicChronologyTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    // The default leap year pattern is 16-based.
    private static final IslamicChronology.LeapYearPatternType PATTERN_16_BASED = IslamicChronology.LEAP_YEAR_16_BASED;
    private static final IslamicChronology.LeapYearPatternType PATTERN_15_BASED = IslamicChronology.LEAP_YEAR_15_BASED;
    private static final IslamicChronology.LeapYearPatternType PATTERN_HABASH = IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB;

    private static final IslamicChronology CHRONO_UTC_DEFAULT = IslamicChronology.getInstanceUTC();

    // --- Factory and Singleton Tests ---

    @Test
    public void getInstance_shouldReturnCachedInstancesForSameZoneAndPattern() {
        IslamicChronology chrono1 = IslamicChronology.getInstance(PARIS, PATTERN_15_BASED);
        IslamicChronology chrono2 = IslamicChronology.getInstance(PARIS, PATTERN_15_BASED);
        assertSame("Instances should be cached and reused", chrono1, chrono2);
    }

    @Test
    public void getInstanceUTC_shouldReturnSingletonInstance() {
        IslamicChronology chrono = IslamicChronology.getInstanceUTC();
        assertSame(chrono, CHRONO_UTC_DEFAULT);
    }

    @Test(expected = NullPointerException.class)
    public void getInstance_shouldThrowExceptionForNullLeapYearPattern() {
        IslamicChronology.getInstance(UTC, null);
    }

    // --- withZone and withUTC Tests ---

    @Test
    public void withZone_shouldReturnNewInstanceForDifferentZone() {
        IslamicChronology chronoInParis = IslamicChronology.getInstance(PARIS);
        Chronology chronoInUTC = chronoInParis.withZone(UTC);

        assertNotSame(chronoInParis, chronoInUTC);
        assertEquals(UTC, chronoInUTC.getZone());
    }

    @Test
    public void withZone_shouldReturnSameInstanceForSameZone() {
        IslamicChronology chronoInParis = IslamicChronology.getInstance(PARIS);
        Chronology sameChrono = chronoInParis.withZone(PARIS);
        assertSame(chronoInParis, sameChrono);
    }

    @Test
    public void withZone_shouldUseDefaultZoneWhenZoneIsNull() {
        IslamicChronology chronoInParis = IslamicChronology.getInstance(PARIS);
        Chronology chronoInDefault = chronoInParis.withZone(null);
        assertEquals(DateTimeZone.getDefault(), chronoInDefault.getZone());
    }

    @Test
    public void withUTC_shouldReturnUTCInstance() {
        IslamicChronology chronoInParis = IslamicChronology.getInstance(PARIS);
        Chronology chronoInUTC = chronoInParis.withUTC();
        assertSame("withUTC() should return the singleton UTC instance", CHRONO_UTC_DEFAULT, chronoInUTC);
    }

    // --- equals() and hashCode() Tests ---

    @Test
    public void equals_shouldBeTrueForEquivalentInstances() {
        IslamicChronology chrono1 = IslamicChronology.getInstance(UTC, PATTERN_16_BASED);
        IslamicChronology chrono2 = IslamicChronology.getInstance(UTC, PATTERN_16_BASED);
        assertTrue(chrono1.equals(chrono2));
    }

    @Test
    public void equals_shouldBeFalseForDifferentZone() {
        IslamicChronology chronoUTC = IslamicChronology.getInstance(UTC);
        IslamicChronology chronoParis = IslamicChronology.getInstance(PARIS);
        assertFalse(chronoUTC.equals(chronoParis));
    }

    @Test
    public void equals_shouldBeFalseForDifferentLeapYearPattern() {
        IslamicChronology chrono16Based = IslamicChronology.getInstance(UTC, PATTERN_16_BASED);
        IslamicChronology chrono15Based = IslamicChronology.getInstance(UTC, PATTERN_15_BASED);
        assertFalse(chrono16Based.equals(chrono15Based));
    }

    @Test
    public void equals_shouldBeFalseForDifferentClass() {
        assertFalse(CHRONO_UTC_DEFAULT.equals(new Object()));
    }

    @Test
    public void hashCode_shouldBeSameForEquivalentInstances() {
        IslamicChronology chrono1 = IslamicChronology.getInstance(UTC, PATTERN_16_BASED);
        IslamicChronology chrono2 = IslamicChronology.getInstance(UTC, PATTERN_16_BASED);
        assertEquals(chrono1.hashCode(), chrono2.hashCode());
    }

    // --- Leap Year and Day Calculation Tests ---

    @Test
    public void isLeapYear_shouldBeFalseForCommonYear() {
        // Year 1 is not a leap year in the default (16-based) pattern.
        assertFalse(CHRONO_UTC_DEFAULT.isLeapYear(1));
    }

    @Test
    public void isLeapYear_shouldBeTrueForLeapYear() {
        // Year 2 is a leap year in the default (16-based) pattern.
        assertTrue(CHRONO_UTC_DEFAULT.isLeapYear(2));
    }

    @Test
    public void isLeapYear_shouldBeCorrectForHabashAlHasibPattern() {
        IslamicChronology chrono = IslamicChronology.getInstance(UTC, PATTERN_HABASH);
        // Year 30 is a leap year in the Habash al-Hasib pattern, but not in others.
        assertTrue(chrono.isLeapYear(30));
        assertFalse(chrono.isLeapYear(29));
    }

    @Test
    public void getDaysInYear_shouldBe354_forCommonYear() {
        assertEquals(354, CHRONO_UTC_DEFAULT.getDaysInYear(1)); // Common year
    }

    @Test
    public void getDaysInYear_shouldBe355_forLeapYear() {
        assertEquals(355, CHRONO_UTC_DEFAULT.getDaysInYear(2)); // Leap year
    }

    @Test
    public void getDaysInMonthMax_shouldReturnCorrectValuesForMonths() {
        assertEquals(30, CHRONO_UTC_DEFAULT.getDaysInMonthMax(1));  // Odd months have 30 days
        assertEquals(29, CHRONO_UTC_DEFAULT.getDaysInMonthMax(2));  // Even months (not 12) have 29
        assertEquals(30, CHRONO_UTC_DEFAULT.getDaysInMonthMax(12)); // Month 12 can have 30 days
    }

    @Test
    public void getDaysInYearMonth_shouldReturn29_forMonth12_inCommonYear() {
        assertEquals(29, CHRONO_UTC_DEFAULT.getDaysInYearMonth(1, 12)); // Year 1 is common
    }

    @Test
    public void getDaysInYearMonth_shouldReturn30_forMonth12_inLeapYear() {
        assertEquals(30, CHRONO_UTC_DEFAULT.getDaysInYearMonth(2, 12)); // Year 2 is leap
    }

    // --- Field Calculation Tests ---

    @Test
    public void setYear_shouldSetYearCorrectly() {
        // Arrange: Start with 1390-05-10 AH (which is 1970-08-12 CE)
        long instant = 19252800000L;
        assertEquals(1390, CHRONO_UTC_DEFAULT.getYear(instant));

        // Act: Set the year to 1400 AH
        long newInstant = CHRONO_UTC_DEFAULT.setYear(instant, 1400);

        // Assert: Verify the new date is 1400-05-10 AH
        assertEquals(1400, CHRONO_UTC_DEFAULT.getYear(newInstant));
        assertEquals(5, CHRONO_UTC_DEFAULT.getMonthOfYear(newInstant));
        assertEquals(10, CHRONO_UTC_DEFAULT.getDayOfMonth(newInstant));
    }

    @Test
    public void calculateFirstDayOfYearMillis_shouldHandleMinAndMaxYears() {
        // Test minimum supported year
        assertEquals(-8948534433609600000L, CHRONO_UTC_DEFAULT.calculateFirstDayOfYearMillis(-292269337));
        // Test maximum supported year
        assertEquals(8948501182656000000L, CHRONO_UTC_DEFAULT.calculateFirstDayOfYearMillis(292271022));
    }

    @Test(expected = ArithmeticException.class)
    public void calculateFirstDayOfYearMillis_shouldThrowExceptionForTooLargeYear() {
        // The maximum supported year is 292271022.
        CHRONO_UTC_DEFAULT.calculateFirstDayOfYearMillis(292271023);
    }

    @Test(expected = ArithmeticException.class)
    public void calculateFirstDayOfYearMillis_shouldThrowExceptionForTooSmallYear() {
        // The minimum supported year is -292269337.
        CHRONO_UTC_DEFAULT.calculateFirstDayOfYearMillis(-292269338);
    }

    // --- Constant Value Tests ---

    @Test
    public void getMinMaxYears_shouldReturnCorrectConstants() {
        assertEquals(1, CHRONO_UTC_DEFAULT.getMinYear());
        assertEquals(292271022, CHRONO_UTC_DEFAULT.getMaxYear());
    }

    @Test
    public void getDaysInYearMax_shouldReturn355() {
        assertEquals(355, CHRONO_UTC_DEFAULT.getDaysInYearMax());
    }

    @Test
    public void getAverageMillis_shouldReturnCorrectValues() {
        assertEquals(30617280288L, CHRONO_UTC_DEFAULT.getAverageMillisPerYear());
        assertEquals(15308640144L, CHRONO_UTC_DEFAULT.getAverageMillisPerYearDividedByTwo());
        assertEquals(2551440384L, CHRO_UTC_DEFAULT.getAverageMillisPerMonth());
    }

    // --- LeapYearPatternType Tests ---

    @Test
    public void leapYearPatternType_equals_and_hashCode_shouldBehaveCorrectly() {
        // Test equality
        assertTrue(PATTERN_15_BASED.equals(PATTERN_15_BASED));
        assertFalse(PATTERN_15_BASED.equals(PATTERN_16_BASED));
        assertFalse(PATTERN_15_BASED.equals(null));
        assertFalse(PATTERN_15_BASED.equals(new Object()));

        // Test hash code consistency
        assertEquals(PATTERN_15_BASED.hashCode(), PATTERN_15_BASED.hashCode());
        assertNotEquals(PATTERN_15_BASED.hashCode(), PATTERN_16_BASED.hashCode());
    }

    @Test
    public void chronology_shouldRespectCustomLeapYearPattern() {
        // Arrange: A custom pattern where only year 9 of the 30-year cycle is a leap year.
        // The pattern is a bitmask; for year 9, we need to set the 8th bit (9-1).
        // 1 << 8 = 256. The index (100) is arbitrary for this test.
        IslamicChronology.LeapYearPatternType customPattern = new IslamicChronology.LeapYearPatternType(100, 256);
        IslamicChronology chrono = new IslamicChronology(null, null, customPattern);

        // Act & Assert:
        // Year 1389 is in the 9th year of its cycle (1389 % 30 = 9).
        // With our custom pattern, it should be a leap year.
        assertTrue(chrono.isLeapYear(1389));
        assertEquals(355, chrono.getDaysInYear(1389));

        // Year 1390 is in the 10th year of its cycle, so it should be a common year.
        assertFalse(chrono.isLeapYear(1390));
        assertEquals(354, chrono.getDaysInYear(1390));
    }
}