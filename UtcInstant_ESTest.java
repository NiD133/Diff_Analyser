package org.threeten.extra.scale;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A more understandable test suite for the {@link UtcInstant} class.
 */
public class UtcInstantUnderstandabilityTest {

    private static final UtcInstant MJD_EPOCH = UtcInstant.ofModifiedJulianDay(0, 0); // 1858-11-17T00:00:00Z
    private static final UtcInstant INSTANT_1970_EPOCH = UtcInstant.of(Instant.EPOCH);
    private static final long MJD_OF_1970_EPOCH = 40587L;
    private static final long NANOS_PER_SECOND = 1_000_000_000L;
    private static final long NANOS_PER_DAY = 86400L * NANOS_PER_SECOND;

    // --- Factory Methods ---

    @Test
    public void ofModifiedJulianDay_withValidValues_createsInstance() {
        // Arrange
        long mjd = 50000;
        long nanoOfDay = 12345L;

        // Act
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);

        // Assert
        assertNotNull(instant);
        assertEquals(mjd, instant.getModifiedJulianDay());
        assertEquals(nanoOfDay, instant.getNanoOfDay());
    }

    @Test
    public void ofModifiedJulianDay_withNegativeNanoOfDay_throwsException() {
        try {
            UtcInstant.ofModifiedJulianDay(123L, -1L);
            fail("Expected DateTimeException was not thrown.");
        } catch (DateTimeException e) {
            assertTrue(e.getMessage().contains("Nanosecond-of-day must be between 0 and"));
        }
    }

    @Test
    public void ofInstant_fromJavaEpoch_createsCorrectUtcInstant() {
        // Act
        UtcInstant utcInstant = UtcInstant.of(Instant.EPOCH);

        // Assert
        assertEquals(MJD_OF_1970_EPOCH, utcInstant.getModifiedJulianDay());
        assertEquals(0, utcInstant.getNanoOfDay());
    }

    @Test
    public void ofInstant_withNull_throwsException() {
        try {
            UtcInstant.of((Instant) null);
            fail("Expected NullPointerException was not thrown.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void ofTaiInstant_withNull_throwsException() {
        try {
            UtcInstant.of((TaiInstant) null);
            fail("Expected NullPointerException was not thrown.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void parse_validIsoString_createsCorrectInstance() {
        // Arrange
        String text = "1970-01-01T00:00:00Z";

        // Act
        UtcInstant parsed = UtcInstant.parse(text);

        // Assert
        assertEquals(INSTANT_1970_EPOCH, parsed);
    }

    @Test
    public void parse_invalidString_throwsException() {
        try {
            UtcInstant.parse("not a date");
            fail("Expected DateTimeParseException was not thrown.");
        } catch (DateTimeParseException e) {
            // Expected
        }
    }

    // --- Getters and With-ers ---

    @Test
    public void withModifiedJulianDay_changesDay() {
        // Arrange
        long newMjd = MJD_EPOCH.getModifiedJulianDay() + 100;

        // Act
        UtcInstant result = MJD_EPOCH.withModifiedJulianDay(newMjd);

        // Assert
        assertEquals(newMjd, result.getModifiedJulianDay());
        assertEquals(MJD_EPOCH.getNanoOfDay(), result.getNanoOfDay());
    }

    @Test
    public void withNanoOfDay_changesNanos() {
        // Arrange
        long newNanoOfDay = MJD_EPOCH.getNanoOfDay() + 100;

        // Act
        UtcInstant result = MJD_EPOCH.withNanoOfDay(newNanoOfDay);

        // Assert
        assertEquals(MJD_EPOCH.getModifiedJulianDay(), result.getModifiedJulianDay());
        assertEquals(newNanoOfDay, result.getNanoOfDay());
    }

    // --- Comparison Methods ---

    @Test
    public void isAfter_returnsTrueForLaterInstant() {
        UtcInstant base = UtcInstant.ofModifiedJulianDay(100, 100);
        UtcInstant later = UtcInstant.ofModifiedJulianDay(100, 101);
        assertTrue(later.isAfter(base));
    }

    @Test
    public void isAfter_returnsFalseForEarlierInstant() {
        UtcInstant base = UtcInstant.ofModifiedJulianDay(100, 100);
        UtcInstant earlier = UtcInstant.ofModifiedJulianDay(99, 100);
        assertFalse(earlier.isAfter(base));
    }

    @Test
    public void isBefore_returnsTrueForEarlierInstant() {
        UtcInstant base = UtcInstant.ofModifiedJulianDay(100, 100);
        UtcInstant earlier = UtcInstant.ofModifiedJulianDay(100, 99);
        assertTrue(earlier.isBefore(base));
    }

    @Test
    public void isBefore_returnsFalseForLaterInstant() {
        UtcInstant base = UtcInstant.ofModifiedJulianDay(100, 100);
        UtcInstant later = UtcInstant.ofModifiedJulianDay(101, 100);
        assertFalse(later.isBefore(base));
    }

    @Test
    public void compareTo_worksCorrectly() {
        UtcInstant base = UtcInstant.ofModifiedJulianDay(100, 100);
        UtcInstant same = UtcInstant.ofModifiedJulianDay(100, 100);
        UtcInstant later = UtcInstant.ofModifiedJulianDay(100, 101);
        UtcInstant earlier = UtcInstant.ofModifiedJulianDay(100, 99);

        assertEquals(0, base.compareTo(same));
        assertTrue(base.compareTo(earlier) > 0);
        assertTrue(base.compareTo(later) < 0);
    }

    // --- Arithmetic Methods ---

    @Test
    public void plus_addsDurationCorrectly() {
        // Arrange
        UtcInstant start = UtcInstant.ofModifiedJulianDay(50000, 1000);
        Duration duration = Duration.ofSeconds(10);

        // Act
        UtcInstant result = start.plus(duration);

        // Assert
        assertEquals(50000, result.getModifiedJulianDay());
        assertEquals(1000 + 10 * NANOS_PER_SECOND, result.getNanoOfDay());
    }

    @Test
    public void plus_handlesDayRollover() {
        // Arrange
        UtcInstant start = UtcInstant.ofModifiedJulianDay(50000, NANOS_PER_DAY - 1);
        Duration duration = Duration.ofNanos(2);

        // Act
        UtcInstant result = start.plus(duration);

        // Assert
        assertEquals(50001, result.getModifiedJulianDay());
        assertEquals(1, result.getNanoOfDay());
    }

    @Test
    public void minus_subtractsDurationCorrectly() {
        // Arrange
        UtcInstant start = UtcInstant.ofModifiedJulianDay(50000, 10 * NANOS_PER_SECOND);
        Duration duration = Duration.ofSeconds(5);

        // Act
        UtcInstant result = start.minus(duration);

        // Assert
        assertEquals(50000, result.getModifiedJulianDay());
        assertEquals(5 * NANOS_PER_SECOND, result.getNanoOfDay());
    }

    @Test
    public void minus_handlesDayRollover() {
        // Arrange
        UtcInstant start = UtcInstant.ofModifiedJulianDay(50000, 1);
        Duration duration = Duration.ofNanos(2);

        // Act
        UtcInstant result = start.minus(duration);

        // Assert
        assertEquals(49999, result.getModifiedJulianDay());
        assertEquals(NANOS_PER_DAY - 1, result.getNanoOfDay());
    }

    @Test
    public void durationUntil_calculatesPositiveDuration() {
        // Arrange
        UtcInstant start = UtcInstant.ofModifiedJulianDay(50000, 1000);
        UtcInstant end = UtcInstant.ofModifiedJulianDay(50000, 2000);
        Duration expected = Duration.ofNanos(1000);

        // Act
        Duration actual = start.durationUntil(end);

        // Assert
        assertEquals(expected, actual);
    }

    // --- Conversions and Other Methods ---

    @Test
    public void toInstant_and_of_roundtrip() {
        // Arrange
        Instant originalInstant = Instant.parse("2007-12-03T10:15:30.00Z");

        // Act
        UtcInstant utcInstant = UtcInstant.of(originalInstant);
        Instant finalInstant = utcInstant.toInstant();

        // Assert
        assertEquals(originalInstant, finalInstant);
    }

    @Test
    public void toTaiInstant_and_of_roundtrip() {
        // Arrange
        // TAI epoch is 1958-01-01T00:00:00.000 TAI
        TaiInstant originalTai = TaiInstant.ofTaiSeconds(0, 0);

        // Act
        UtcInstant utcInstant = UtcInstant.of(originalTai);
        TaiInstant finalTai = utcInstant.toTaiInstant();

        // Assert
        assertEquals(originalTai, finalTai);
    }

    @Test
    public void isLeapSecond_returnsFalseForNormalSecond() {
        // A known non-leap second time
        UtcInstant normalInstant = UtcInstant.parse("2015-06-30T23:59:59Z");
        assertFalse(normalInstant.isLeapSecond());
    }

    @Test
    public void isLeapSecond_returnsTrueForLeapSecond() {
        // A known leap second occurred on 1998-12-31. MJD is 51178.
        // The day had 86401 seconds. The leap second is the last one.
        long mjdLeapDay = 51178;
        long nanoOfLeapSecond = NANOS_PER_DAY; // Start of the 61st second

        UtcInstant leapSecondInstant = UtcInstant.ofModifiedJulianDay(mjdLeapDay, nanoOfLeapSecond);
        assertTrue(leapSecondInstant.isLeapSecond());
    }

    @Test
    public void toString_returnsCorrectIsoFormat() {
        // Arrange
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(0, 12345L);

        // Act
        String text = instant.toString();

        // Assert
        assertEquals("1858-11-17T00:00:00.000012345Z", text);
    }

    // --- equals() and hashCode() ---

    @Test
    public void equals_isReflexive() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(1, 1);
        assertEquals(instant, instant);
    }

    @Test
    public void equals_isSymmetric() {
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(1, 1);
        UtcInstant instant2 = UtcInstant.ofModifiedJulianDay(1, 1);
        assertEquals(instant1, instant2);
        assertEquals(instant2, instant1);
    }

    @Test
    public void equals_returnsFalseForDifferentMjd() {
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(1, 1);
        UtcInstant instant2 = UtcInstant.ofModifiedJulianDay(2, 1);
        assertNotEquals(instant1, instant2);
    }

    @Test
    public void equals_returnsFalseForDifferentNanoOfDay() {
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(1, 1);
        UtcInstant instant2 = UtcInstant.ofModifiedJulianDay(1, 2);
        assertNotEquals(instant1, instant2);
    }

    @Test
    public void equals_returnsFalseForNull() {
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(1, 1);
        assertNotEquals(null, instant1);
    }

    @Test
    public void equals_returnsFalseForDifferentClass() {
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(1, 1);
        assertNotEquals("a string", instant1);
    }

    @Test
    public void hashCode_isConsistentForEqualObjects() {
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(1, 1);
        UtcInstant instant2 = UtcInstant.ofModifiedJulianDay(1, 1);
        assertEquals(instant1.hashCode(), instant2.hashCode());
    }
}