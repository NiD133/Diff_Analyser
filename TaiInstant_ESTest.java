package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.UtcInstant;

/**
 * Test suite for TaiInstant class functionality.
 * Tests cover creation, manipulation, conversion, and comparison operations.
 */
public class TaiInstant_ESTest {

    // Test constants for better readability
    private static final long SAMPLE_TAI_SECONDS = 1000L;
    private static final int SAMPLE_NANOS = 1000;
    private static final long ZERO_SECONDS = 0L;
    private static final int ZERO_NANOS = 0;
    private static final int MAX_NANOS = 999_999_999;

    // ========== Creation and Factory Method Tests ==========

    @Test
    public void testCreateTaiInstantFromInstant_ShouldSucceed() {
        // Given: A mock instant representing current time
        Instant mockInstant = MockInstant.now();
        
        // When: Creating TaiInstant from the instant
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        
        // Then: TaiInstant should be created successfully
        assertNotNull(taiInstant);
        assertEquals(320000000, taiInstant.getNano());
        assertEquals(1771100516L, taiInstant.getTaiSeconds());
    }

    @Test
    public void testCreateTaiInstantFromUtcInstant_ShouldSucceed() {
        // Given: A UTC instant at modified Julian day 0
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        
        // When: Creating TaiInstant from UTC instant
        TaiInstant taiInstant = TaiInstant.of(utcInstant);
        
        // Then: TaiInstant should be created with correct values
        assertEquals(ZERO_NANOS, taiInstant.getNano());
        assertNotNull(taiInstant);
    }

    @Test
    public void testCreateTaiInstantWithTaiSeconds_ShouldSucceed() {
        // When: Creating TaiInstant with specific TAI seconds and nanos
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(SAMPLE_TAI_SECONDS, SAMPLE_NANOS);
        
        // Then: Values should be set correctly
        assertEquals(SAMPLE_TAI_SECONDS, taiInstant.getTaiSeconds());
        assertEquals(SAMPLE_NANOS, taiInstant.getNano());
    }

    // ========== Validation and Error Handling Tests ==========

    @Test(expected = NullPointerException.class)
    public void testCreateFromNullInstant_ShouldThrowException() {
        // When: Attempting to create TaiInstant from null instant
        // Then: Should throw NullPointerException
        TaiInstant.of((Instant) null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromNullUtcInstant_ShouldThrowException() {
        // When: Attempting to create TaiInstant from null UTC instant
        // Then: Should throw NullPointerException
        TaiInstant.of((UtcInstant) null);
    }

    @Test(expected = ArithmeticException.class)
    public void testCreateWithOverflowValues_ShouldThrowException() {
        // When: Attempting to create with values that cause overflow
        // Then: Should throw ArithmeticException
        TaiInstant.ofTaiSeconds(Long.MAX_VALUE, Long.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidNanos_ShouldThrowException() {
        // Given: A valid TaiInstant
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(50L, 50L);
        
        // When: Attempting to set invalid nano value (>= 1 billion)
        // Then: Should throw IllegalArgumentException
        taiInstant.withNano(1_000_000_000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNegativeNanos_ShouldThrowException() {
        // Given: A valid TaiInstant
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(ZERO_SECONDS, ZERO_NANOS);
        
        // When: Attempting to set negative nano value
        // Then: Should throw IllegalArgumentException
        taiInstant.withNano(-4339);
    }

    // ========== Modification Tests ==========

    @Test
    public void testWithNano_ShouldReturnNewInstanceWithUpdatedNanos() {
        // Given: A TaiInstant with initial values
        TaiInstant original = TaiInstant.ofTaiSeconds(ZERO_SECONDS, ZERO_NANOS);
        int newNanos = 91;
        
        // When: Setting new nano value
        TaiInstant modified = original.withNano(newNanos);
        
        // Then: Should return new instance with updated nanos
        assertEquals(ZERO_SECONDS, modified.getTaiSeconds());
        assertEquals(newNanos, modified.getNano());
        assertNotSame(original, modified);
    }

    @Test
    public void testWithTaiSeconds_ShouldReturnNewInstanceWithUpdatedSeconds() {
        // Given: A TaiInstant with initial values
        Instant mockInstant = MockInstant.now();
        TaiInstant original = TaiInstant.of(mockInstant);
        long newSeconds = 1000000000L;
        
        // When: Setting new TAI seconds
        TaiInstant modified = original.withTaiSeconds(newSeconds);
        
        // Then: Should return new instance with updated seconds
        assertEquals(newSeconds, modified.getTaiSeconds());
        assertEquals(original.getNano(), modified.getNano());
    }

    // ========== Arithmetic Operations Tests ==========

    @Test
    public void testPlusDuration_ShouldAddCorrectly() {
        // Given: A TaiInstant and a duration to add
        TaiInstant original = TaiInstant.ofTaiSeconds(SAMPLE_TAI_SECONDS, SAMPLE_NANOS);
        Duration duration = Duration.ofSeconds(SAMPLE_TAI_SECONDS, SAMPLE_NANOS);
        
        // When: Adding the duration
        TaiInstant result = original.plus(duration);
        
        // Then: Should add correctly
        assertEquals(2 * SAMPLE_TAI_SECONDS, result.getTaiSeconds());
        assertEquals(2 * SAMPLE_NANOS, result.getNano());
    }

    @Test
    public void testPlusZeroDuration_ShouldReturnSameInstance() {
        // Given: A TaiInstant and zero duration
        TaiInstant original = TaiInstant.ofTaiSeconds(ZERO_SECONDS, ZERO_NANOS);
        Duration zeroDuration = Duration.ZERO;
        
        // When: Adding zero duration
        TaiInstant result = original.plus(zeroDuration);
        
        // Then: Should return the same instance
        assertSame(original, result);
    }

    @Test
    public void testMinusDuration_ShouldSubtractCorrectly() {
        // Given: A TaiInstant and a duration to subtract
        Instant mockInstant = MockInstant.ofEpochSecond(36204L, -1L);
        TaiInstant original = TaiInstant.of(mockInstant);
        Duration duration = Duration.ofMillis(-1L);
        
        // When: Subtracting the duration
        TaiInstant result = original.minus(duration);
        
        // Then: Should subtract correctly
        assertEquals(378727414L, result.getTaiSeconds());
        assertEquals(999999, result.getNano());
    }

    @Test(expected = NullPointerException.class)
    public void testPlusNullDuration_ShouldThrowException() {
        // Given: A valid TaiInstant
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(ZERO_SECONDS, ZERO_NANOS);
        
        // When: Adding null duration
        // Then: Should throw NullPointerException
        taiInstant.plus(null);
    }

    @Test(expected = ArithmeticException.class)
    public void testPlusInfiniteDuration_ShouldThrowException() {
        // Given: A TaiInstant and infinite duration
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(1000000000L, 1000000000L);
        Duration infiniteDuration = ChronoUnit.FOREVER.getDuration();
        
        // When: Adding infinite duration
        // Then: Should throw ArithmeticException due to overflow
        taiInstant.plus(infiniteDuration);
    }

    // ========== Comparison Tests ==========

    @Test
    public void testCompareTo_SameInstance_ShouldReturnZero() {
        // Given: A TaiInstant
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(ZERO_SECONDS, ZERO_NANOS);
        
        // When: Comparing to itself
        int result = taiInstant.compareTo(taiInstant);
        
        // Then: Should return 0
        assertEquals(0, result);
    }

    @Test
    public void testCompareTo_EarlierInstance_ShouldReturnNegative() {
        // Given: Two TaiInstants with different times
        Instant mockInstant = MockInstant.now();
        TaiInstant later = TaiInstant.of(mockInstant);
        TaiInstant earlier = later.withTaiSeconds(-2L);
        
        // When: Comparing earlier to later
        int result = earlier.compareTo(later);
        
        // Then: Should return negative value
        assertEquals(-1, result);
    }

    @Test
    public void testIsBefore_WithEarlierTime_ShouldReturnTrue() {
        // Given: Two TaiInstants where first is before second
        TaiInstant earlier = TaiInstant.ofTaiSeconds(37L, 37L);
        TaiInstant later = TaiInstant.ofTaiSeconds(37L, 3503L);
        
        // When: Checking if earlier is before later
        boolean result = earlier.isBefore(later);
        
        // Then: Should return true
        assertTrue(result);
    }

    @Test
    public void testIsBefore_SameTime_ShouldReturnFalse() {
        // Given: A TaiInstant
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(ZERO_SECONDS, ZERO_NANOS);
        
        // When: Checking if it's before itself
        boolean result = taiInstant.isBefore(taiInstant);
        
        // Then: Should return false
        assertFalse(result);
    }

    @Test
    public void testIsAfter_WithLaterTime_ShouldReturnTrue() {
        // Given: Two TaiInstants where first is after second
        TaiInstant earlier = TaiInstant.ofTaiSeconds(37L, 37L);
        TaiInstant later = TaiInstant.ofTaiSeconds(37L, 3503L);
        
        // When: Checking if later is after earlier
        boolean result = later.isAfter(earlier);
        
        // Then: Should return true
        assertTrue(result);
    }

    // ========== Equality Tests ==========

    @Test
    public void testEquals_SameInstance_ShouldReturnTrue() {
        // Given: A TaiInstant
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(ZERO_SECONDS, ZERO_NANOS);
        
        // When: Comparing to itself
        boolean result = taiInstant.equals(taiInstant);
        
        // Then: Should return true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentNanos_ShouldReturnFalse() {
        // Given: Two TaiInstants with same seconds but different nanos
        TaiInstant first = TaiInstant.ofTaiSeconds(37L, 37L);
        TaiInstant second = TaiInstant.ofTaiSeconds(37L, 3503L);
        
        // When: Comparing them
        boolean result = first.equals(second);
        
        // Then: Should return false
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentType_ShouldReturnFalse() {
        // Given: A TaiInstant and a different type object
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(19L, 19L);
        Object other = new Object();
        
        // When: Comparing them
        boolean result = taiInstant.equals(other);
        
        // Then: Should return false
        assertFalse(result);
    }

    // ========== Conversion Tests ==========

    @Test
    public void testToUtcInstant_ShouldConvertCorrectly() {
        // Given: A TaiInstant at epoch
        UtcInstant originalUtc = UtcInstant.ofModifiedJulianDay(0L, 0L);
        TaiInstant taiInstant = TaiInstant.of(originalUtc);
        
        // When: Converting back to UTC
        UtcInstant convertedUtc = taiInstant.toUtcInstant();
        
        // Then: Should match original
        assertEquals(0L, convertedUtc.getNanoOfDay());
    }

    @Test
    public void testToInstant_ShouldConvertCorrectly() {
        // Given: A TaiInstant at epoch
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(ZERO_SECONDS, ZERO_NANOS);
        
        // When: Converting to Instant
        Instant instant = taiInstant.toInstant();
        
        // Then: Should create valid Instant
        assertNotNull(instant);
        
        // And: Converting back should preserve values
        TaiInstant roundTrip = TaiInstant.of(instant);
        assertEquals(ZERO_NANOS, roundTrip.getNano());
    }

    // ========== Duration Calculation Tests ==========

    @Test
    public void testDurationUntil_SameInstant_ShouldReturnZero() {
        // Given: A TaiInstant
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(ZERO_SECONDS, ZERO_NANOS);
        
        // When: Calculating duration to itself
        Duration duration = taiInstant.durationUntil(taiInstant);
        
        // Then: Should return zero duration
        assertEquals(Duration.ZERO, duration);
    }

    @Test(expected = NullPointerException.class)
    public void testDurationUntilNull_ShouldThrowException() {
        // Given: A valid TaiInstant
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(2784L, 2784L);
        
        // When: Calculating duration to null
        // Then: Should throw NullPointerException
        taiInstant.durationUntil(null);
    }

    // ========== String Parsing Tests ==========

    @Test(expected = NullPointerException.class)
    public void testParseNull_ShouldThrowException() {
        // When: Parsing null string
        // Then: Should throw NullPointerException
        TaiInstant.parse(null);
    }

    @Test(expected = DateTimeParseException.class)
    public void testParseInvalidFormat_ShouldThrowException() {
        // When: Parsing string with invalid format (8 digits instead of 9)
        // Then: Should throw DateTimeParseException
        TaiInstant.parse("0.00000000s(TAI)");
    }

    // ========== String Representation Tests ==========

    @Test
    public void testToString_ShouldFormatCorrectly() {
        // Given: A TaiInstant with specific values
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(1L, 0L);
        
        // When: Converting to string
        String result = taiInstant.toString();
        
        // Then: Should format correctly
        assertEquals("1.000000000s(TAI)", result);
    }

    // ========== Hash Code Tests ==========

    @Test
    public void testHashCode_ShouldBeConsistent() {
        // Given: A TaiInstant created from current time
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        UtcInstant utcInstant = UtcInstant.of(taiInstant);
        UtcInstant modifiedUtc = utcInstant.withModifiedJulianDay(0L);
        TaiInstant modifiedTai = TaiInstant.of(modifiedUtc);
        
        // When: Getting hash code
        int hashCode = modifiedTai.hashCode();
        
        // Then: Should be consistent (same hash code on repeated calls)
        assertEquals(hashCode, modifiedTai.hashCode());
    }
}