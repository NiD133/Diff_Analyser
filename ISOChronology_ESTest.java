package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link ISOChronology}.
 */
public class ISOChronologyTest {

    private static final DateTimeZone ZONE_UTC = DateTimeZone.UTC;
    private static final DateTimeZone ZONE_PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone ZONE_TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // --- getInstance() tests ---

    @Test
    public void getInstance_noArgs_returnsInstanceWithDefaultZone() {
        // Arrange
        DateTimeZone defaultZone = DateTimeZone.getDefault();

        // Act
        ISOChronology chronology = ISOChronology.getInstance();

        // Assert
        assertNotNull(chronology);
        assertEquals(defaultZone, chronology.getZone());
    }

    @Test
    public void getInstance_withUTCZone_returnsCachedUTCInstance() {
        // Act
        ISOChronology chronology = ISOChronology.getInstance(ZONE_UTC);

        // Assert
        assertNotNull(chronology);
        assertSame("The UTC instance should be a cached singleton",
                ISOChronology.getInstanceUTC(), chronology);
    }

    @Test
    public void getInstance_withNonNullZone_returnsInstanceWithThatZone() {
        // Act
        ISOChronology chronology = ISOChronology.getInstance(ZONE_PARIS);

        // Assert
        assertNotNull(chronology);
        assertEquals(ZONE_PARIS, chronology.getZone());
    }

    @Test
    public void getInstance_withNullZone_returnsInstanceWithDefaultZone() {
        // Act
        ISOChronology chronology = ISOChronology.getInstance(null);

        // Assert
        assertNotNull(chronology);
        assertSame("getInstance(null) should be equivalent to getInstance()",
                ISOChronology.getInstance(), chronology);
    }

    // --- withZone() tests ---

    @Test
    public void withZone_givenDifferentZone_returnsNewInstance() {
        // Arrange
        ISOChronology initialChronology = ISOChronology.getInstance(ZONE_PARIS);

        // Act
        Chronology newChronology = initialChronology.withZone(ZONE_TOKYO);

        // Assert
        assertNotSame(initialChronology, newChronology);
        assertEquals(ZONE_TOKYO, newChronology.getZone());
    }

    @Test
    public void withZone_givenSameZone_returnsSameInstance() {
        // Arrange
        ISOChronology initialChronology = ISOChronology.getInstance(ZONE_PARIS);

        // Act
        Chronology newChronology = initialChronology.withZone(ZONE_PARIS);

        // Assert
        assertSame("Should return the same instance if the zone is unchanged",
                initialChronology, newChronology);
    }

    @Test
    public void withZone_givenNullZone_returnsDefaultZoneInstance() {
        // Arrange
        ISOChronology initialChronology = ISOChronology.getInstance(ZONE_PARIS);
        ISOChronology defaultChronology = ISOChronology.getInstance();

        // Act
        Chronology result = initialChronology.withZone(null);

        // Assert
        assertSame("withZone(null) should return the instance for the default zone",
                defaultChronology, result);
    }

    // --- withUTC() tests ---

    @Test
    public void withUTC_onNonUTCInstance_returnsUTCInstance() {
        // Arrange
        ISOChronology parisChronology = ISOChronology.getInstance(ZONE_PARIS);

        // Act
        Chronology result = parisChronology.withUTC();

        // Assert
        assertSame("withUTC() should return the canonical UTC instance",
                ISOChronology.getInstanceUTC(), result);
    }

    @Test
    public void withUTC_onUTCInstance_returnsSameInstance() {
        // Arrange
        ISOChronology utcChronology = ISOChronology.getInstanceUTC();

        // Act
        Chronology result = utcChronology.withUTC();

        // Assert
        assertSame("withUTC() on a UTC instance should return itself",
                utcChronology, result);
    }

    // --- equals() and hashCode() tests ---

    @Test
    public void equals_isSymmetricAndReflexiveForSameZone() {
        // Arrange
        ISOChronology chrono1 = ISOChronology.getInstance(ZONE_PARIS);
        ISOChronology chrono2 = ISOChronology.getInstance(ZONE_PARIS);

        // Assert
        assertEquals("An instance should be equal to itself (reflexive)", chrono1, chrono1);
        assertEquals("Two instances for the same zone should be equal (symmetric)", chrono1, chrono2);
        assertEquals("Two instances for the same zone should be equal (symmetric)", chrono2, chrono1);
    }

    @Test
    public void equals_returnsFalseForDifferentZones() {
        // Arrange
        ISOChronology chronoParis = ISOChronology.getInstance(ZONE_PARIS);
        ISOChronology chronoUtc = ISOChronology.getInstanceUTC();

        // Assert
        assertNotEquals("Instances with different zones should not be equal", chronoParis, chronoUtc);
    }

    @Test
    public void equals_returnsFalseForDifferentType() {
        // Arrange
        ISOChronology chronology = ISOChronology.getInstanceUTC();
        Object other = new Object();

        // Assert
        assertNotEquals("Should not be equal to an object of a different type", chronology, other);
    }

    @Test
    public void hashCode_isConsistentForEqualObjects() {
        // Arrange
        ISOChronology chrono1 = ISOChronology.getInstance(ZONE_PARIS);
        ISOChronology chrono2 = ISOChronology.getInstance(ZONE_PARIS);

        // Assert
        assertEquals("Equal objects must have equal hash codes", chrono1.hashCode(), chrono2.hashCode());
    }

    // --- toString() tests ---

    @Test
    public void toString_forUTCInstance_isCorrect() {
        // Arrange
        ISOChronology chronology = ISOChronology.getInstanceUTC();

        // Assert
        assertEquals("ISOChronology[UTC]", chronology.toString());
    }

    @Test
    public void toString_forNonUTCInstance_isCorrect() {
        // Arrange
        ISOChronology chronology = ISOChronology.getInstance(ZONE_PARIS);

        // Assert
        assertEquals("ISOChronology[Europe/Paris]", chronology.toString());
    }
}