package org.joda.time.tz;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class CachedDateTimeZone_ESTest extends CachedDateTimeZone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testGetNameForUTC() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(defaultZone);
        String name = cachedZone.getName(212544000000L);
        assertEquals("Coordinated Universal Time", name);
    }

    @Test(timeout = 4000)
    public void testGetNameForWET() {
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        String name = wetZone.getName(686653601480704L);
        assertEquals("Western European Time", name);
    }

    @Test(timeout = 4000)
    public void testHashCodeForCachedZone() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(defaultZone);
        cachedZone.hashCode(); // No assertion needed, just testing method call
    }

    @Test(timeout = 4000)
    public void testPreviousTransitionForDefaultZone() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(defaultZone);
        long transition = cachedZone.previousTransition(1);
        assertEquals(1L, transition);
    }

    @Test(timeout = 4000)
    public void testPreviousTransitionForUTC() {
        DateTimeZone utcZone = DateTimeZone.UTC;
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(utcZone);
        long transition = cachedZone.previousTransition(-983L);
        assertEquals(-983L, transition);
    }

    @Test(timeout = 4000)
    public void testNextTransitionForOffsetMillis() {
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-2614);
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(offsetZone);
        long transition = cachedZone.nextTransition(1L);
        assertEquals(1L, transition);
    }

    @Test(timeout = 4000)
    public void testNextTransitionForOffsetMillisNegative() {
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-2614);
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(offsetZone);
        long transition = cachedZone.nextTransition(-2614);
        assertEquals(-2614L, transition);
    }

    @Test(timeout = 4000)
    public void testIsFixedForInstantZone() {
        Instant now = Instant.now();
        DateTimeZone zone = now.getZone();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(zone);
        assertTrue(cachedZone.isFixed());
    }

    @Test(timeout = 4000)
    public void testIsFixedForWET() {
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        assertFalse(wetZone.isFixed());
    }

    @Test(timeout = 4000)
    public void testStandardOffsetForWET() {
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        int offset = wetZone.getStandardOffset(999999992896684031L);
        assertEquals(0, offset);
    }

    @Test(timeout = 4000)
    public void testStandardOffsetForOffsetMillis() {
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-2614);
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(offsetZone);
        int offset = cachedZone.getStandardOffset(0L);
        assertEquals(-2614, offset);
    }

    @Test(timeout = 4000)
    public void testGetOffsetForUTC() {
        DateTimeZone utcZone = DateTimeZone.UTC;
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(utcZone);
        int offset = cachedZone.getOffset(0L);
        assertEquals(0, offset);
    }

    @Test(timeout = 4000)
    public void testGetOffsetForDefaultZone() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(defaultZone);
        int offset = cachedZone.getOffset(-418L);
        assertEquals(0, offset);
    }

    @Test(timeout = 4000)
    public void testGetOffsetForOffsetMillis() {
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-2614);
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(offsetZone);
        int offset = cachedZone.getOffset(86400000L);
        assertEquals(-2614, offset);
    }

    @Test(timeout = 4000)
    public void testGetNameKeyForDefaultZone() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(defaultZone);
        String nameKey = cachedZone.getNameKey(-1048L);
        assertEquals("UTC", nameKey);
    }

    @Test(timeout = 4000)
    public void testGetNameKeyForWET() {
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        String nameKey = wetZone.getNameKey(-4294967296L);
        assertEquals("CET", nameKey);
    }

    @Test(timeout = 4000)
    public void testEqualsForInstantZone() {
        Instant now = Instant.now();
        DateTimeZone zone = now.getZone();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(zone);
        assertFalse(cachedZone.equals(zone));
    }

    @Test(timeout = 4000)
    public void testPreviousTransitionException() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        try {
            defaultZone.previousTransition(-1748L);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNextTransitionException() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        try {
            defaultZone.nextTransition(-1L);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIsFixedException() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        try {
            defaultZone.isFixed();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testHashCodeException() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        try {
            defaultZone.hashCode();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testStandardOffsetException() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        try {
            defaultZone.getStandardOffset(100000000000000000L);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetOffsetException() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        try {
            defaultZone.getOffset(100000000000000000L);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetNameKeyException() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        try {
            defaultZone.getNameKey(-458L);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEqualsException() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(defaultZone);
        try {
            defaultZone.equals(cachedZone);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEqualsForCachedZone() {
        Instant now = Instant.now();
        DateTimeZone zone = now.getZone();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(zone);
        assertTrue(cachedZone.equals(cachedZone));
    }

    @Test(timeout = 4000)
    public void testSameInstanceForWET() {
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(wetZone);
        assertSame(wetZone, cachedZone);
    }

    @Test(timeout = 4000)
    public void testStandardOffsetForDefaultZone() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(defaultZone);
        int offset = cachedZone.getStandardOffset(6);
        assertEquals(0, offset);
    }

    @Test(timeout = 4000)
    public void testGetNameForWET() {
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        String name = wetZone.getName(212544000000L);
        assertEquals("Western European Time", name);
    }

    @Test(timeout = 4000)
    public void testGetUncachedZone() {
        Instant now = Instant.now();
        DateTimeZone zone = now.getZone();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(zone);
        DateTimeZone uncachedZone = cachedZone.getUncachedZone();
        assertSame(uncachedZone, zone);
    }

    @Test(timeout = 4000)
    public void testIsLocalDateTimeGap() {
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        LocalDateTime localDateTime = new LocalDateTime(9999998125080576L, (Chronology) gregorianChronology);
        assertFalse(wetZone.isLocalDateTimeGap(localDateTime));
    }

    @Test(timeout = 4000)
    public void testNextTransitionForDefaultZone() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(defaultZone);
        long transition = cachedZone.nextTransition(0L);
        assertEquals(0L, transition);
    }

    @Test(timeout = 4000)
    public void testNullZoneException() {
        try {
            CachedDateTimeZone.forZone(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}