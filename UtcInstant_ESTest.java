package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.CharBuffer;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.UtcInstant;

/**
 * Test suite for UtcInstant class functionality.
 * Tests cover creation, manipulation, conversion, and comparison operations.
 */
public class UtcInstantTest {

    // Test constants for better readability
    private static final long EPOCH_MJD = 0L; // 1858-11-17
    private static final long UNIX_EPOCH_MJD = 40587L; // 1970-01-01
    private static final long NANOS_PER_SECOND = 1_000_000_000L;
    private static final long NANOS_PER_DAY = 86_400_000_000_000L;

    // ========== Creation Tests ==========

    @Test
    public void testCreateFromModifiedJulianDay_ValidValues() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        
        assertEquals(EPOCH_MJD, instant.getModifiedJulianDay());
        assertEquals(0L, instant.getNanoOfDay());
    }

    @Test
    public void testCreateFromModifiedJulianDay_WithNanoseconds() {
        long nanoOfDay = 3207000001000L; // 00:53:27.000001
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(36204L, nanoOfDay);
        
        assertEquals(36204L, instant.getModifiedJulianDay());
        assertEquals(nanoOfDay, instant.getNanoOfDay());
    }

    @Test(expected = DateTimeException.class)
    public void testCreateFromModifiedJulianDay_InvalidNanoOfDay() {
        // Negative nano-of-day should throw exception
        UtcInstant.ofModifiedJulianDay(-361L, -361L);
    }

    @Test
    public void testCreateFromInstant_UnixEpoch() {
        Instant unixEpoch = Instant.ofEpochSecond(0L);
        UtcInstant utcInstant = UtcInstant.of(unixEpoch);
        
        assertEquals(UNIX_EPOCH_MJD, utcInstant.getModifiedJulianDay());
        assertEquals(0L, utcInstant.getNanoOfDay());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromInstant_NullInput() {
        UtcInstant.of((Instant) null);
    }

    @Test
    public void testCreateFromTaiInstant() {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(3217L, 1000L);
        UtcInstant utcInstant = UtcInstant.of(taiInstant);
        
        assertNotNull(utcInstant);
        // Verify conversion maintains time relationship
        assertEquals(taiInstant, utcInstant.toTaiInstant());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromTaiInstant_NullInput() {
        UtcInstant.of((TaiInstant) null);
    }

    // ========== Parsing Tests ==========

    @Test
    public void testParse_ValidIsoString() {
        String isoString = "1958-01-01T00:53:27.000001Z";
        UtcInstant instant = UtcInstant.parse(isoString);
        
        assertEquals(36204L, instant.getModifiedJulianDay());
        assertEquals(3207000001000L, instant.getNanoOfDay());
    }

    @Test
    public void testParse_EpochString() {
        String epochString = "1858-11-17T00:00:00Z";
        UtcInstant instant = UtcInstant.parse(epochString);
        
        assertEquals(EPOCH_MJD, instant.getModifiedJulianDay());
        assertEquals(0L, instant.getNanoOfDay());
    }

    @Test(expected = DateTimeParseException.class)
    public void testParse_InvalidFormat() {
        CharBuffer invalidBuffer = CharBuffer.allocate(10);
        UtcInstant.parse(invalidBuffer);
    }

    @Test(expected = NullPointerException.class)
    public void testParse_NullInput() {
        UtcInstant.parse((CharSequence) null);
    }

    // ========== Modification Tests ==========

    @Test
    public void testWithModifiedJulianDay_SameValue() {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        UtcInstant modified = original.withModifiedJulianDay(EPOCH_MJD);
        
        assertEquals(original, modified);
        assertEquals(0L, modified.getNanoOfDay());
    }

    @Test
    public void testWithModifiedJulianDay_DifferentValue() {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        UtcInstant modified = original.withModifiedJulianDay(-2547L);
        
        assertEquals(-2547L, modified.getModifiedJulianDay());
        assertEquals(0L, modified.getNanoOfDay()); // Nano part preserved
    }

    @Test
    public void testWithNanoOfDay_SameValue() {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(UNIX_EPOCH_MJD, 0L);
        UtcInstant modified = original.withNanoOfDay(0L);
        
        assertEquals(original, modified);
    }

    @Test
    public void testWithNanoOfDay_DifferentValue() {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(-2985L, 0L);
        UtcInstant modified = original.withNanoOfDay(1000L);
        
        assertEquals(-2985L, modified.getModifiedJulianDay()); // Day preserved
        assertEquals(1000L, modified.getNanoOfDay());
    }

    @Test(expected = DateTimeException.class)
    public void testWithNanoOfDay_InvalidValue() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(86340000001821L, 86340000001821L);
        instant.withNanoOfDay(-892L); // Negative value should fail
    }

    // ========== Duration Operations Tests ==========

    @Test
    public void testPlus_ZeroDuration() {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        Duration zeroDuration = original.durationUntil(original);
        UtcInstant result = original.plus(zeroDuration);
        
        assertEquals(original, result);
    }

    @Test
    public void testPlus_PositiveDuration() {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(73281320003518L, 73281320003518L);
        Duration duration = Duration.ofMillis(73281320003518L);
        UtcInstant result = original.plus(duration);
        
        assertTrue(original.isBefore(result));
        assertEquals(23684838003518L, result.getNanoOfDay());
    }

    @Test(expected = NullPointerException.class)
    public void testPlus_NullDuration() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(86399999998838L, 86399999998838L);
        instant.plus(null);
    }

    @Test
    public void testMinus_NegativeDuration() {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(-571L, 0L);
        Duration negativeDuration = Duration.ofNanos(-1606L);
        UtcInstant result = original.plus(negativeDuration);
        
        assertEquals(-572L, result.getModifiedJulianDay());
        assertEquals(86399999998394L, result.getNanoOfDay());
    }

    @Test
    public void testMinus_PositiveDuration() {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(140L, 140L);
        Duration duration = Duration.ofMinutes(140L);
        UtcInstant result = original.minus(duration);
        
        assertEquals(139L, result.getModifiedJulianDay());
        assertEquals(78000000000140L, result.getNanoOfDay());
    }

    @Test(expected = NullPointerException.class)
    public void testMinus_NullDuration() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(73281320003506L, 73281320003506L);
        instant.minus(null);
    }

    @Test
    public void testDurationUntil_SameInstant() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        Duration duration = instant.durationUntil(instant);
        
        assertEquals(Duration.ZERO, duration);
    }

    @Test(expected = NullPointerException.class)
    public void testDurationUntil_NullInstant() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(1000000000L, 1000000000L);
        instant.durationUntil(null);
    }

    // ========== Comparison Tests ==========

    @Test
    public void testCompareTo_SameInstant() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        
        assertEquals(0, instant.compareTo(instant));
    }

    @Test
    public void testCompareTo_DifferentNanoOfDay() {
        UtcInstant earlier = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        UtcInstant later = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 1891L);
        
        assertEquals(1, later.compareTo(earlier));
        assertEquals(-1, earlier.compareTo(later));
    }

    @Test(expected = NullPointerException.class)
    public void testCompareTo_NullInstant() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(2775000000000L, 2775000000000L);
        instant.compareTo(null);
    }

    @Test
    public void testIsAfter_True() {
        UtcInstant earlier = UtcInstant.ofModifiedJulianDay(-1406L, 83843999999988L);
        UtcInstant later = UtcInstant.ofModifiedJulianDay(83843999999988L, 83843999999988L);
        
        assertTrue(later.isAfter(earlier));
    }

    @Test
    public void testIsAfter_False() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        
        assertFalse(instant.isAfter(instant));
    }

    @Test(expected = NullPointerException.class)
    public void testIsAfter_NullInstant() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(2775000000000L, 2775000000000L);
        instant.isAfter(null);
    }

    @Test
    public void testIsBefore_True() {
        UtcInstant earlier = UtcInstant.ofModifiedJulianDay(6400000041317L, 73281320003515L);
        UtcInstant later = UtcInstant.ofModifiedJulianDay(9223372036854775807L, 73281320003515L);
        
        assertTrue(earlier.isBefore(later));
    }

    @Test
    public void testIsBefore_False() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(9223372036854775807L, 73281320003515L);
        
        assertFalse(instant.isBefore(instant));
    }

    @Test(expected = NullPointerException.class)
    public void testIsBefore_NullInstant() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(2774999999977L, 2774999999977L);
        instant.isBefore(null);
    }

    // ========== Conversion Tests ==========

    @Test
    public void testToTaiInstant_RoundTrip() {
        TaiInstant originalTai = TaiInstant.ofTaiSeconds(3217L, 1000L);
        UtcInstant utc = originalTai.toUtcInstant();
        TaiInstant convertedTai = utc.toTaiInstant();
        
        assertEquals(originalTai.getTaiSeconds(), convertedTai.getTaiSeconds());
        assertEquals(originalTai.getNano(), convertedTai.getNano());
    }

    @Test
    public void testToInstant_RoundTrip() {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(-127L, 41317L);
        Instant instant = original.toInstant();
        UtcInstant converted = UtcInstant.of(instant);
        
        assertEquals(original, converted);
    }

    // ========== Equality and Hash Tests ==========

    @Test
    public void testEquals_SameInstance() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        
        assertTrue(instant.equals(instant));
    }

    @Test
    public void testEquals_EqualInstances() {
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        UtcInstant instant2 = UtcInstant.parse("1858-11-17T00:00:00Z");
        
        assertTrue(instant1.equals(instant2));
        assertTrue(instant2.equals(instant1));
    }

    @Test
    public void testEquals_DifferentNanoOfDay() {
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(301L, 301L);
        UtcInstant instant2 = UtcInstant.ofModifiedJulianDay(301L, 70L);
        
        assertFalse(instant1.equals(instant2));
        assertFalse(instant2.equals(instant1));
    }

    @Test
    public void testEquals_DifferentMjd() {
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(1285L, 2341L);
        UtcInstant instant2 = UtcInstant.ofModifiedJulianDay(0L, 0L);
        
        assertFalse(instant1.equals(instant2));
    }

    @Test
    public void testEquals_DifferentType() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        Boolean otherType = Boolean.FALSE;
        
        assertFalse(instant.equals(otherType));
    }

    @Test
    public void testHashCode_ConsistentWithEquals() {
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(73281320003518L, 73281320003518L);
        UtcInstant instant2 = UtcInstant.ofModifiedJulianDay(-2985L, 0L);
        
        // Hash codes should be consistent (same object produces same hash)
        assertEquals(instant1.hashCode(), instant1.hashCode());
        assertEquals(instant2.hashCode(), instant2.hashCode());
    }

    // ========== Leap Second Tests ==========

    @Test
    public void testIsLeapSecond_False() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(83843999999988L, 83843999999988L);
        
        assertFalse(instant.isLeapSecond());
    }

    // ========== String Representation Tests ==========

    @Test
    public void testToString_EpochInstant() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(EPOCH_MJD, 0L);
        
        assertEquals("1858-11-17T00:00:00Z", instant.toString());
    }

    // ========== Edge Case and Error Tests ==========

    @Test(expected = ArithmeticException.class)
    public void testToTaiInstant_Overflow() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(9223372036854775807L, 490L);
        instant.toTaiInstant();
    }

    @Test(expected = DateTimeException.class)
    public void testToInstant_ExceedsRange() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(73281320003633L, 73281320003633L);
        instant.toInstant();
    }

    @Test(expected = ArithmeticException.class)
    public void testToInstant_Overflow() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(9223372036854775807L, 0L);
        instant.toInstant();
    }

    @Test(expected = ArithmeticException.class)
    public void testPlus_Overflow() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(86399999999628L, 86399999999628L);
        Duration hugeDuration = Duration.ofDays(86399999999628L);
        instant.plus(hugeDuration);
    }

    @Test(expected = ArithmeticException.class)
    public void testMinus_Overflow() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(9223372036854775807L, 490L);
        Duration duration = Duration.ofNanos(490L);
        instant.minus(duration);
    }

    @Test(expected = ArithmeticException.class)
    public void testDurationUntil_Overflow() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(9223372036854775807L, 73281320003515L);
        instant.durationUntil(instant);
    }

    @Test(expected = DateTimeException.class)
    public void testToString_InvalidMjd() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(83844000000000L, 83844000000000L);
        instant.toString();
    }
}