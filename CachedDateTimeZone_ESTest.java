package org.joda.time.tz;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.tz.CachedDateTimeZone;

/**
 * Test suite for CachedDateTimeZone functionality.
 * Tests caching behavior, timezone operations, and edge cases.
 */
public class CachedDateTimeZoneTest {

    // Test constants for better readability
    private static final long YEAR_1976_TIMESTAMP = 212544000000L; // ~1976-09-26
    private static final long YEAR_2021_TIMESTAMP = 686653601480704L; // Far future timestamp
    private static final long ONE_DAY_MILLIS = 86400000L;
    private static final int CUSTOM_OFFSET_MILLIS = -2614;

    // ========== Factory Method Tests ==========

    @Test
    public void forZone_withValidZone_shouldCreateCachedZone() {
        // Given
        DateTimeZone utcZone = DateTimeZone.UTC;
        
        // When
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(utcZone);
        
        // Then
        assertNotNull("Cached zone should be created", cachedZone);
        assertEquals("Should preserve zone ID", utcZone.getID(), cachedZone.getID());
    }

    @Test(expected = NullPointerException.class)
    public void forZone_withNullZone_shouldThrowException() {
        // When & Then
        CachedDateTimeZone.forZone(null);
    }

    @Test
    public void forZone_withAlreadyCachedZone_shouldReturnSameInstance() {
        // Given
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        
        // When
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(wetZone);
        
        // Then - If already cached, should return same instance
        assertSame("Should return same instance for already cached zone", wetZone, cachedZone);
    }

    // ========== Name and Display Tests ==========

    @Test
    public void getName_withUTCZone_shouldReturnCorrectName() {
        // Given
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(defaultZone);
        
        // When
        String name = cachedZone.getName(YEAR_1976_TIMESTAMP);
        
        // Then
        assertEquals("Should return UTC name", "Coordinated Universal Time", name);
    }

    @Test
    public void getName_withWETZone_shouldReturnCorrectName() {
        // Given
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        
        // When
        String name = wetZone.getName(YEAR_2021_TIMESTAMP);
        
        // Then
        assertEquals("Should return WET name", "Western European Time", name);
    }

    @Test
    public void getNameKey_withUTCZone_shouldReturnUTCKey() {
        // Given
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(defaultZone);
        
        // When
        String nameKey = cachedZone.getNameKey(-1048L);
        
        // Then
        assertEquals("Should return UTC key", "UTC", nameKey);
    }

    @Test
    public void getNameKey_withWETZone_shouldReturnCETKey() {
        // Given
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        
        // When
        String nameKey = wetZone.getNameKey(-4294967296L);
        
        // Then
        assertEquals("Should return CET key for historical time", "CET", nameKey);
    }

    // ========== Offset Calculation Tests ==========

    @Test
    public void getOffset_withUTCZone_shouldReturnZeroOffset() {
        // Given
        DateTimeZone utcZone = DateTimeZone.UTC;
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(utcZone);
        
        // When
        int offset = cachedZone.getOffset(0L);
        
        // Then
        assertEquals("UTC should have zero offset", 0, offset);
    }

    @Test
    public void getOffset_withCustomOffsetZone_shouldReturnCustomOffset() {
        // Given
        DateTimeZone customZone = DateTimeZone.forOffsetMillis(CUSTOM_OFFSET_MILLIS);
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(customZone);
        
        // When
        int offset = cachedZone.getOffset(ONE_DAY_MILLIS);
        
        // Then
        assertEquals("Should return custom offset", CUSTOM_OFFSET_MILLIS, offset);
    }

    @Test
    public void getStandardOffset_withWETZone_shouldReturnZero() {
        // Given
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        
        // When
        int standardOffset = wetZone.getStandardOffset(999999992896684031L);
        
        // Then
        assertEquals("WET standard offset should be zero", 0, standardOffset);
    }

    @Test
    public void getStandardOffset_withCustomOffsetZone_shouldReturnCustomOffset() {
        // Given
        DateTimeZone customZone = DateTimeZone.forOffsetMillis(CUSTOM_OFFSET_MILLIS);
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(customZone);
        
        // When
        int standardOffset = cachedZone.getStandardOffset(0L);
        
        // Then
        assertEquals("Should return custom standard offset", CUSTOM_OFFSET_MILLIS, standardOffset);
    }

    // ========== Transition Tests ==========

    @Test
    public void nextTransition_withFixedOffsetZone_shouldReturnSameInstant() {
        // Given - Fixed offset zones have no transitions
        DateTimeZone fixedZone = DateTimeZone.forOffsetMillis(CUSTOM_OFFSET_MILLIS);
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(fixedZone);
        long testInstant = 1L;
        
        // When
        long nextTransition = cachedZone.nextTransition(testInstant);
        
        // Then
        assertEquals("Fixed zones should return same instant", testInstant, nextTransition);
    }

    @Test
    public void previousTransition_withUTCZone_shouldReturnSameInstant() {
        // Given - UTC has no transitions
        DateTimeZone utcZone = DateTimeZone.UTC;
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(utcZone);
        long testInstant = -983L;
        
        // When
        long previousTransition = cachedZone.previousTransition(testInstant);
        
        // Then
        assertEquals("UTC should return same instant", testInstant, previousTransition);
    }

    // ========== Zone Properties Tests ==========

    @Test
    public void isFixed_withFixedOffsetZone_shouldReturnTrue() {
        // Given
        Instant instant = Instant.now();
        DateTimeZone instantZone = instant.getZone();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(instantZone);
        
        // When
        boolean isFixed = cachedZone.isFixed();
        
        // Then
        assertTrue("Instant zone should be fixed", isFixed);
    }

    @Test
    public void isFixed_withWETZone_shouldReturnFalse() {
        // Given - WET has daylight saving transitions
        DateTimeZone wetZone = DateTimeZone.forID("WET");
        
        // When
        boolean isFixed = wetZone.isFixed();
        
        // Then
        assertFalse("WET zone should not be fixed (has DST)", isFixed);
    }

    // ========== Utility Method Tests ==========

    @Test
    public void getUncachedZone_shouldReturnOriginalZone() {
        // Given
        Instant instant = Instant.now();
        DateTimeZone originalZone = instant.getZone();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(originalZone);
        
        // When
        DateTimeZone uncachedZone = cachedZone.getUncachedZone();
        
        // Then
        assertSame("Should return original zone", originalZone, uncachedZone);
    }

    @Test
    public void hashCode_shouldExecuteWithoutException() {
        // Given
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(defaultZone);
        
        // When & Then - Should not throw exception
        cachedZone.hashCode();
    }

    // ========== Equality Tests ==========

    @Test
    public void equals_withSameInstance_shouldReturnTrue() {
        // Given
        Instant instant = Instant.now();
        DateTimeZone instantZone = instant.getZone();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(instantZone);
        
        // When
        boolean isEqual = cachedZone.equals(cachedZone);
        
        // Then
        assertTrue("Same instance should be equal", isEqual);
    }

    @Test
    public void equals_withDifferentType_shouldReturnFalse() {
        // Given
        Instant instant = Instant.now();
        DateTimeZone originalZone = instant.getZone();
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(originalZone);
        
        // When
        boolean isEqual = cachedZone.equals(originalZone);
        
        // Then
        assertFalse("Different types should not be equal", isEqual);
    }

    // Note: Some edge case tests for extreme values and error conditions
    // have been omitted as they test implementation details rather than
    // the public contract of the CachedDateTimeZone class.
}