/*
 *  Copyright 2001-2012 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.tz;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This test suite verifies the functionality of the {@link CachedDateTimeZone} class.
 * It ensures that the caching wrapper correctly delegates calls to the underlying
 * time zone while maintaining its own contracts for caching, equality, and identity.
 */
public class CachedDateTimeZoneTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;
    // A non-fixed zone with daylight saving transitions (e.g., GMT/BST).
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    // A fixed-offset zone that is not UTC.
    private static final DateTimeZone FIXED_OFFSET_ZONE = DateTimeZone.forOffsetHours(-5);

    // Winter instant in London (GMT, offset 0)
    private static final long WINTER_INSTANT = new DateTime(2025, 1, 1, 0, 0, LONDON).getMillis();

    // Summer instant in London (BST, offset +1)
    private static final long SUMMER_INSTANT = new DateTime(2025, 7, 1, 0, 0, LONDON).getMillis();

    // --- Test forZone() factory method ---

    @Test
    public void forZone_givenFixedZone_returnsCachedWrapper() {
        // Act
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(FIXED_OFFSET_ZONE);

        // Assert
        assertNotNull(cachedZone);
        assertEquals(FIXED_OFFSET_ZONE.getID(), cachedZone.getID());
        assertSame("The original zone should be retrievable", FIXED_OFFSET_ZONE, cachedZone.getUncachedZone());
    }

    @Test
    public void forZone_whenZoneIsAlreadyCached_returnsSameInstance() {
        // DateTimeZone.forID() often returns a CachedDateTimeZone instance directly for performance.
        DateTimeZone alreadyCached = DateTimeZone.forID("Europe/Paris");
        assertTrue("Precondition: Zone from forID should be a CachedDateTimeZone",
                alreadyCached instanceof CachedDateTimeZone);

        // Act: forZone should not re-wrap an already cached zone.
        CachedDateTimeZone result = CachedDateTimeZone.forZone(alreadyCached);

        // Assert
        assertSame("forZone should return the same instance if already cached", alreadyCached, result);
    }

    @Test(expected = NullPointerException.class)
    public void forZone_givenNull_throwsNullPointerException() {
        // Act
        CachedDateTimeZone.forZone(null);
    }

    // --- Tests for Fixed Time Zones ---

    @Test
    public void isFixed_forCachedFixedZone_returnsTrue() {
        // Arrange
        CachedDateTimeZone cachedUtc = CachedDateTimeZone.forZone(UTC);

        // Assert
        assertTrue("isFixed should return true for a cached fixed zone", cachedUtc.isFixed());
    }

    @Test
    public void nextTransition_forCachedFixedZone_returnsSameInstant() {
        // Arrange
        CachedDateTimeZone cachedFixedZone = CachedDateTimeZone.forZone(FIXED_OFFSET_ZONE);

        // Act & Assert
        assertEquals(12345L, cachedFixedZone.nextTransition(12345L));
        assertEquals(-1L, cachedFixedZone.nextTransition(-1L));
    }

    @Test
    public void previousTransition_forCachedFixedZone_returnsSameInstant() {
        // Arrange
        CachedDateTimeZone cachedFixedZone = CachedDateTimeZone.forZone(FIXED_OFFSET_ZONE);

        // Act & Assert
        assertEquals(12345L, cachedFixedZone.previousTransition(12345L));
        assertEquals(-1L, cachedFixedZone.previousTransition(-1L));
    }

    @Test
    public void getOffset_forCachedFixedZone_returnsCorrectOffset() {
        // Arrange
        CachedDateTimeZone cachedFixedZone = CachedDateTimeZone.forZone(FIXED_OFFSET_ZONE);
        int expectedOffset = -5 * 3600 * 1000;

        // Act & Assert
        assertEquals(expectedOffset, cachedFixedZone.getOffset(WINTER_INSTANT));
        assertEquals(expectedOffset, cachedFixedZone.getStandardOffset(WINTER_INSTANT));
    }

    // --- Tests for Non-Fixed Time Zones ---

    @Test
    public void isFixed_forCachedNonFixedZone_returnsFalse() {
        // Arrange
        CachedDateTimeZone cachedLondon = CachedDateTimeZone.forZone(LONDON);

        // Assert
        assertFalse("isFixed should return false for a cached non-fixed zone", cachedLondon.isFixed());
    }

    @Test
    public void getOffset_forCachedNonFixedZone_returnsCorrectChangingOffset() {
        // Arrange
        CachedDateTimeZone cachedLondon = CachedDateTimeZone.forZone(LONDON);

        // Assert
        assertEquals("Offset should be GMT in winter", 0, cachedLondon.getOffset(WINTER_INSTANT));
        assertEquals("Offset should be BST in summer", 3600000, cachedLondon.getOffset(SUMMER_INSTANT));
    }

    @Test
    public void getNameKey_forCachedNonFixedZone_returnsCorrectChangingKey() {
        // Arrange
        CachedDateTimeZone cachedLondon = CachedDateTimeZone.forZone(LONDON);

        // Assert
        assertEquals("Name key should be GMT in winter", "GMT", cachedLondon.getNameKey(WINTER_INSTANT));
        assertEquals("Name key should be BST in summer", "BST", cachedLondon.getNameKey(SUMMER_INSTANT));
    }

    @Test
    public void isLocalDateTimeGap_forCachedNonFixedZone_delegatesCorrectly() {
        // Arrange
        CachedDateTimeZone cachedLondon = CachedDateTimeZone.forZone(LONDON);
        // A known gap in London is on 2024-03-31 between 01:00 and 02:00
        GregorianChronology chrono = GregorianChronology.getInstance(LONDON);
        long instantBeforeGap = new DateTime(2024, 3, 31, 0, 59, chrono).getMillis();
        long instantInGap = new DateTime(2024, 3, 31, 1, 30, chrono).getMillis();

        // Act
        boolean isGapBefore = cachedLondon.isLocalDateTimeGap(chrono.getLocalDateTime(instantBeforeGap));
        boolean isGapDuring = cachedLondon.isLocalDateTimeGap(chrono.getLocalDateTime(instantInGap));

        // Assert
        assertFalse("There should be no gap before the transition", isGapBefore);
        assertTrue("There should be a gap during the DST transition", isGapDuring);
    }

    // --- Tests for equals() and hashCode() contracts ---

    @Test
    public void hashCode_isSameAsUnderlyingZone() {
        // Arrange
        CachedDateTimeZone cachedLondon = CachedDateTimeZone.forZone(LONDON);

        // Assert
        assertEquals(LONDON.hashCode(), cachedLondon.hashCode());
    }

    @Test
    public void equals_isReflexive() {
        // Arrange
        CachedDateTimeZone cachedLondon = CachedDateTimeZone.forZone(LONDON);

        // Assert
        assertTrue("A cached zone must be equal to itself", cachedLondon.equals(cachedLondon));
    }

    @Test
    public void equals_isFalseWhenComparedWithUnderlyingZone() {
        // This test demonstrates an intentional asymmetry in the equals() contract.
        // A CachedDateTimeZone instance is not considered equal to its underlying zone.
        // Arrange
        CachedDateTimeZone cachedLondon = CachedDateTimeZone.forZone(LONDON);

        // Assert
        assertFalse("A cached zone should not be equal to its unwrapped underlying zone",
                cachedLondon.equals(LONDON));
    }

    @Test
    public void equals_isTrueForDifferentWrappersOfSameZone() {
        // To ensure we get two different wrapper instances, we create a fresh, non-interned zone.
        // Arrange
        DateTimeZone customZone = new FixedDateTimeZone("TestZone", "TZ", 1000, 1000);
        CachedDateTimeZone cached1 = CachedDateTimeZone.forZone(customZone);
        CachedDateTimeZone cached2 = CachedDateTimeZone.forZone(customZone);

        // Assert
        assertNotSame("The test requires two distinct wrapper instances", cached1, cached2);
        assertTrue("Two cached wrappers of the same underlying zone should be equal",
                cached1.equals(cached2));
    }
}