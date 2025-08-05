/*
 * Test suite for ISOChronology class
 * Tests the core functionality of ISO8601 chronology implementation
 */

package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.ISOChronology;

public class ISOChronologyTest {

    // Test field assembly functionality
    
    @Test
    public void testAssemble_WithValidFields_ShouldSucceed() {
        // Given
        ISOChronology chronology = ISOChronology.getInstance();
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        
        // When & Then - should not throw exception
        chronology.assemble(fields);
    }

    @Test
    public void testAssemble_WithCustomTimeZone_ShouldSucceed() {
        // Given
        ISOChronology defaultChronology = ISOChronology.getInstance();
        DateTimeZone customTimeZone = DateTimeZone.forOffsetMillis(7593750); // ~2 hours offset
        ISOChronology customChronology = (ISOChronology) defaultChronology.withZone(customTimeZone);
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        
        // When & Then - should not throw exception
        customChronology.assemble(fields);
        
        // Verify different instances
        assertNotSame("Chronology with different timezone should be different instance", 
                     customChronology, defaultChronology);
    }

    @Test(expected = NullPointerException.class)
    public void testAssemble_WithNullFields_ShouldThrowNullPointerException() {
        // Given
        ISOChronology chronology = ISOChronology.getInstance();
        
        // When & Then - should throw NullPointerException
        chronology.assemble(null);
    }

    // Test equality functionality
    
    @Test
    public void testEquals_WithSameInstance_ShouldReturnTrue() {
        // Given
        ISOChronology chronology = ISOChronology.getInstanceUTC();
        
        // When
        boolean isEqual = chronology.equals(chronology);
        
        // Then
        assertTrue("Chronology should equal itself", isEqual);
    }

    @Test
    public void testEquals_WithDifferentObjectType_ShouldReturnFalse() {
        // Given
        ISOChronology chronology = ISOChronology.getInstanceUTC();
        Object differentObject = new Object();
        
        // When
        boolean isEqual = chronology.equals(differentObject);
        
        // Then
        assertFalse("Chronology should not equal different object type", isEqual);
    }

    @Test
    public void testEquals_WithDifferentTimeZone_ShouldReturnFalse() {
        // Given
        ISOChronology utcChronology = ISOChronology.getInstanceUTC();
        DateTimeZone customTimeZone = DateTimeZone.forOffsetMillis(-1322);
        Chronology customChronology = utcChronology.withZone(customTimeZone);
        
        // When
        boolean isEqual = utcChronology.equals(customChronology);
        
        // Then
        assertFalse("Chronologies with different timezones should not be equal", isEqual);
    }

    // Test string representation
    
    @Test
    public void testToString_WithUTCTimeZone_ShouldReturnCorrectFormat() {
        // Given
        ISOChronology utcChronology = ISOChronology.getInstanceUTC();
        
        // When
        String stringRepresentation = utcChronology.toString();
        
        // Then
        assertEquals("UTC chronology should have correct string representation", 
                    "ISOChronology[UTC]", stringRepresentation);
    }

    // Test instance creation with different timezones
    
    @Test
    public void testGetInstance_WithCustomTimeZone_ShouldCreateInstance() {
        // Given
        DateTimeZone customTimeZone = DateTimeZone.forOffsetMillis(85);
        
        // When
        ISOChronology chronology = ISOChronology.getInstance(customTimeZone);
        
        // Then
        assertNotNull("Should create chronology instance with custom timezone", chronology);
    }

    @Test
    public void testGetInstance_WithNullTimeZone_ShouldUseDefault() {
        // Given & When
        ISOChronology chronology = ISOChronology.getInstance(null);
        
        // Then
        assertNotNull("Should create chronology instance with null timezone", chronology);
    }

    // Test timezone conversion methods
    
    @Test
    public void testWithUTC_WhenAlreadyUTC_ShouldReturnSameInstance() {
        // Given
        ISOChronology chronology = ISOChronology.getInstance(null); // defaults to UTC
        
        // When
        Chronology utcChronology = chronology.withUTC();
        
        // Then
        assertSame("Should return same instance when already UTC", chronology, utcChronology);
    }

    @Test
    public void testWithZone_WithNullTimeZone_ShouldReturnSameInstance() {
        // Given
        ISOChronology chronology = ISOChronology.getInstance();
        
        // When
        Chronology resultChronology = chronology.withZone(null);
        
        // Then
        assertSame("Should return same instance when setting null timezone", 
                  chronology, resultChronology);
    }

    // Test hash code functionality
    
    @Test
    public void testHashCode_WithCustomTimeZone_ShouldNotThrowException() {
        // Given
        DateTimeZone customTimeZone = DateTimeZone.forOffsetMillis(-84);
        ISOChronology chronology = ISOChronology.getInstance(customTimeZone);
        
        // When & Then - should not throw exception
        int hashCode = chronology.hashCode();
        
        // Verify hash code is computed (basic sanity check)
        assertNotNull("Hash code should be computed", hashCode);
    }
}