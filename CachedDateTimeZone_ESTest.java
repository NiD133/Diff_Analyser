package org.joda.time.tz;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, deterministic tests for CachedDateTimeZone.
 * - Avoids system defaults and current time to keep behavior stable.
 * - Uses well-known zones (UTC, WET, fixed offsets).
 * - Verifies delegation behavior to the underlying zone.
 */
public class CachedDateTimeZoneTest {

    // Helper: a couple of stable instants in UTC.
    private static final long JAN_1_2020_UTC = new DateTime(2020, 1, 1, 0, 0, DateTimeZone.UTC).getMillis();
    private static final long JUL_1_2020_UTC = new DateTime(2020, 7, 1, 0, 0, DateTimeZone.UTC).getMillis();

    @Test
    public void forZone_null_throwsNullPointerException() {
        try {
            CachedDateTimeZone.forZone(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void forZone_returnsOriginalInstanceForFixedOffsetZones() {
        DateTimeZone fixed = DateTimeZone.forOffsetHoursMinutes(2, 30);
        DateTimeZone result = CachedDateTimeZone.forZone(fixed);

        // Fixed zones should not be wrapped
        assertSame(fixed, result);
    }

    @Test
    public void forZone_wrapsRegionZones() {
        DateTimeZone region = DateTimeZone.forID("WET");
        DateTimeZone result = CachedDateTimeZone.forZone(region);

        assertNotSame("Non-fixed zones should be wrapped", region, result);
        assertTrue("Result should be a CachedDateTimeZone", result instanceof CachedDateTimeZone);
    }

    @Test
    public void forZone_givenAlreadyCached_returnsSameInstance() {
        DateTimeZone region = DateTimeZone.forID("WET");
        CachedDateTimeZone cached = CachedDateTimeZone.forZone(region);
        CachedDateTimeZone cachedAgain = CachedDateTimeZone.forZone(cached);

        assertSame("Should return the same cached instance if already cached", cached, cachedAgain);
    }

    @Test
    public void getUncachedZone_returnsOriginalZone() {
        DateTimeZone region = DateTimeZone.forID("WET");
        CachedDateTimeZone cached = CachedDateTimeZone.forZone(region);

        assertSame(region, cached.getUncachedZone());
    }

    @Test
    public void getOffset_delegatesToUnderlyingZone() {
        DateTimeZone region = DateTimeZone.forID("WET");
        CachedDateTimeZone cached = CachedDateTimeZone.forZone(region);

        assertEquals(region.getOffset(JAN_1_2020_UTC), cached.getOffset(JAN_1_2020_UTC));
        assertEquals(region.getOffset(JUL_1_2020_UTC), cached.getOffset(JUL_1_2020_UTC));
    }

    @Test
    public void getStandardOffset_delegatesToUnderlyingZone() {
        DateTimeZone region = DateTimeZone.forID("WET");
        CachedDateTimeZone cached = CachedDateTimeZone.forZone(region);

        assertEquals(region.getStandardOffset(JAN_1_2020_UTC), cached.getStandardOffset(JAN_1_2020_UTC));
        assertEquals(region.getStandardOffset(JUL_1_2020_UTC), cached.getStandardOffset(JUL_1_2020_UTC));
    }

    @Test
    public void nextTransition_delegatesToUnderlyingZone() {
        DateTimeZone region = DateTimeZone.forID("WET");
        CachedDateTimeZone cached = CachedDateTimeZone.forZone(region);

        assertEquals(region.nextTransition(JAN_1_2020_UTC), cached.nextTransition(JAN_1_2020_UTC));
        assertEquals(region.nextTransition(JUL_1_2020_UTC), cached.nextTransition(JUL_1_2020_UTC));
    }

    @Test
    public void previousTransition_delegatesToUnderlyingZone() {
        DateTimeZone region = DateTimeZone.forID("WET");
        CachedDateTimeZone cached = CachedDateTimeZone.forZone(region);

        assertEquals(region.previousTransition(JAN_1_2020_UTC), cached.previousTransition(JAN_1_2020_UTC));
        assertEquals(region.previousTransition(JUL_1_2020_UTC), cached.previousTransition(JUL_1_2020_UTC));
    }

    @Test
    public void isFixed_trueForUTC_falseForRegionZone() {
        CachedDateTimeZone utcCached = CachedDateTimeZone.forZone(DateTimeZone.UTC);
        CachedDateTimeZone wetCached = CachedDateTimeZone.forZone(DateTimeZone.forID("WET"));

        assertTrue(utcCached.isFixed());
        assertFalse(wetCached.isFixed());
    }

    @Test
    public void getName_delegatesToUnderlyingZone() {
        DateTimeZone region = DateTimeZone.forID("WET");
        CachedDateTimeZone cached = CachedDateTimeZone.forZone(region);

        assertEquals(region.getName(JAN_1_2020_UTC), cached.getName(JAN_1_2020_UTC));
        assertEquals(region.getName(JUL_1_2020_UTC), cached.getName(JUL_1_2020_UTC));
    }

    @Test
    public void getNameKey_returnsUTCForUTCZone() {
        CachedDateTimeZone utcCached = CachedDateTimeZone.forZone(DateTimeZone.UTC);

        assertEquals("UTC", utcCached.getNameKey(0L));
        assertEquals("UTC", utcCached.getNameKey(JAN_1_2020_UTC));
    }

    @Test
    public void equals_isReflexive_and_notEqualToNull() {
        CachedDateTimeZone utcCached = CachedDateTimeZone.forZone(DateTimeZone.UTC);

        assertTrue(utcCached.equals(utcCached));
        assertFalse(utcCached.equals(null));
    }

    @Test
    public void nextAndPreviousTransition_areNoOpsForFixedOffsetZones() {
        DateTimeZone fixed = DateTimeZone.forOffsetMillis(-2614);
        CachedDateTimeZone cached = CachedDateTimeZone.forZone(fixed);

        long someInstant = 123456789L;
        assertEquals(someInstant, cached.nextTransition(someInstant));
        assertEquals(someInstant, cached.previousTransition(someInstant));
    }
}