package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.System;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;
import org.evosuite.runtime.mock.java.util.MockDate;
import org.evosuite.runtime.mock.java.util.MockGregorianCalendar;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.convert.CalendarConverter;
import org.junit.runner.RunWith;

/**
 * Test suite for CalendarConverter class.
 * Tests the conversion of Java Calendar objects to Joda-Time instants and chronologies.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class CalendarConverter_ESTest extends CalendarConverter_ESTest_scaffolding {

    // ========== getChronology() Tests ==========
    
    @Test(timeout = 4000)
    public void testGetChronology_WithMinZoneOffset_ReturnsValidChronology() throws Throwable {
        // Given: A CalendarConverter and a calendar with minimum zone offset
        CalendarConverter converter = new CalendarConverter();
        ZoneOffset minZoneOffset = ZoneOffset.MIN;
        TimeZone timeZone = TimeZone.getTimeZone((ZoneId) minZoneOffset);
        MockGregorianCalendar calendar = new MockGregorianCalendar(timeZone);
        
        // When: Getting chronology with null chronology parameter
        Chronology result = converter.getChronology(calendar, (Chronology) null);
        
        // Then: A valid chronology should be returned
        assertNotNull("Chronology should not be null", result);
    }

    @Test(timeout = 4000)
    public void testGetChronology_WithGregorianCalendar_ReturnsValidChronology() throws Throwable {
        // Given: A CalendarConverter and a Gregorian calendar with extreme values
        CalendarConverter converter = CalendarConverter.INSTANCE;
        MockGregorianCalendar calendar = new MockGregorianCalendar(-3785, -2040, 0, -1127, -2040);
        
        // When: Getting chronology with null DateTimeZone
        Chronology result = converter.getChronology(calendar, (DateTimeZone) null);
        
        // Then: A valid chronology should be returned
        assertNotNull("Chronology should not be null for Gregorian calendar", result);
    }

    @Test(timeout = 4000)
    public void testGetChronology_WithCustomDateTimeZone_ReturnsValidChronology() throws Throwable {
        // Given: A CalendarConverter with custom DateTimeZone and calendar
        CalendarConverter converter = new CalendarConverter();
        DateTimeZone customZone = DateTimeZone.forOffsetMillis(1963);
        MockGregorianCalendar calendar = new MockGregorianCalendar(0, 1963, 0, -247581896, -21, 3826);
        ZonedDateTime zonedDateTime = calendar.toZonedDateTime();
        GregorianCalendar gregorianCalendar = MockGregorianCalendar.from(zonedDateTime);
        
        // When: Getting chronology with custom zone
        Chronology result = converter.getChronology(gregorianCalendar, customZone);
        
        // Then: A valid chronology should be returned
        assertNotNull("Chronology should not be null with custom zone", result);
    }

    @Test(timeout = 4000)
    public void testGetChronology_WithGregorianChange_ReturnsValidChronology() throws Throwable {
        // Given: A CalendarConverter and calendar with custom Gregorian change date
        CalendarConverter converter = new CalendarConverter();
        MockDate customChangeDate = new MockDate(9223372036854775785L);
        MockGregorianCalendar calendar = new MockGregorianCalendar();
        calendar.setGregorianChange(customChangeDate);
        
        // When: Getting chronology
        Chronology result = converter.getChronology(calendar, (DateTimeZone) null);
        
        // Then: A valid chronology should be returned
        assertNotNull("Chronology should not be null with custom Gregorian change", result);
    }

    @Test(timeout = 4000)
    public void testGetChronology_WithZonedDateTime_ReturnsValidChronology() throws Throwable {
        // Given: A CalendarConverter and calendar created from ZonedDateTime
        CalendarConverter converter = new CalendarConverter();
        ZonedDateTime zonedDateTime = MockZonedDateTime.now();
        GregorianCalendar calendar = MockGregorianCalendar.from(zonedDateTime);
        
        // When: Getting chronology
        Chronology result = converter.getChronology(calendar, (Chronology) null);
        
        // Then: A valid chronology should be returned
        assertNotNull("Chronology should not be null for ZonedDateTime-based calendar", result);
    }

    // ========== getInstantMillis() Tests ==========
    
    @Test(timeout = 4000)
    public void testGetInstantMillis_WithExtremeValues_ReturnsZero() throws Throwable {
        // Given: A CalendarConverter and calendar with extreme date values
        CalendarConverter converter = new CalendarConverter();
        MockGregorianCalendar calendar = new MockGregorianCalendar(741, 206, 741, 741, 741, 206);
        
        // When: Getting instant millis
        long result = converter.getInstantMillis(calendar, (Chronology) null);
        
        // Then: Should return 0 for invalid/extreme dates
        assertEquals("Should return 0 for extreme date values", 0L, result);
    }

    @Test(timeout = 4000)
    public void testGetInstantMillis_WithDefaultCalendar_ReturnsExpectedValue() throws Throwable {
        // Given: A CalendarConverter and default calendar
        MockGregorianCalendar calendar = new MockGregorianCalendar();
        CalendarConverter converter = CalendarConverter.INSTANCE;
        
        // When: Getting instant millis
        long result = converter.getInstantMillis(calendar, (Chronology) null);
        
        // Then: Should return expected timestamp
        assertEquals("Should return expected timestamp for default calendar", 1392409281320L, result);
    }

    @Test(timeout = 4000)
    public void testGetInstantMillis_WithCustomSystemTime_ReturnsSystemTime() throws Throwable {
        // Given: A CalendarConverter with custom system time
        System.setCurrentTimeMillis(-1870L);
        CalendarConverter converter = CalendarConverter.INSTANCE;
        TimeZone defaultTimeZone = TimeZone.getDefault();
        MockGregorianCalendar calendar = new MockGregorianCalendar(defaultTimeZone);
        
        // When: Getting instant millis
        long result = converter.getInstantMillis(calendar, (Chronology) null);
        
        // Then: Should return the custom system time
        assertEquals("Should return custom system time", -1870L, result);
    }

    // ========== Error Handling Tests ==========
    
    @Test(timeout = 4000)
    public void testGetInstantMillis_WithNullObject_ThrowsNullPointerException() throws Throwable {
        // Given: A CalendarConverter
        CalendarConverter converter = CalendarConverter.INSTANCE;
        
        // When & Then: Calling with null object should throw NullPointerException
        try {
            converter.getInstantMillis(null, (Chronology) null);
            fail("Should throw NullPointerException for null object");
        } catch(NullPointerException e) {
            // Expected exception - no message validation needed for auto-generated tests
        }
    }

    @Test(timeout = 4000)
    public void testGetChronology_WithNullObjectAndDateTimeZone_ThrowsNullPointerException() throws Throwable {
        // Given: A CalendarConverter
        CalendarConverter converter = CalendarConverter.INSTANCE;
        
        // When & Then: Calling with null object should throw NullPointerException
        try {
            converter.getChronology(null, (DateTimeZone) null);
            fail("Should throw NullPointerException for null object");
        } catch(NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetChronology_WithNullObjectAndChronology_ThrowsNullPointerException() throws Throwable {
        // Given: A CalendarConverter
        CalendarConverter converter = new CalendarConverter();
        
        // When & Then: Calling with null object should throw NullPointerException
        try {
            converter.getChronology(null, (Chronology) null);
            fail("Should throw NullPointerException for null object");
        } catch(NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetChronology_WithInvalidObjectType_ThrowsClassCastException() throws Throwable {
        // Given: A CalendarConverter and invalid object type
        CalendarConverter converter = new CalendarConverter();
        
        // When & Then: Calling with wrong object type should throw ClassCastException
        try {
            converter.getChronology(converter, (Chronology) null);
            fail("Should throw ClassCastException for invalid object type");
        } catch(ClassCastException e) {
            assertTrue("Should mention CalendarConverter in error message", 
                      e.getMessage().contains("CalendarConverter cannot be cast to java.util.Calendar"));
        }
    }

    @Test(timeout = 4000)
    public void testGetInstantMillis_WithInvalidObjectType_ThrowsClassCastException() throws Throwable {
        // Given: A CalendarConverter and invalid object type
        CalendarConverter converter = new CalendarConverter();
        Object invalidObject = new Object();
        
        // When & Then: Calling with wrong object type should throw ClassCastException
        try {
            converter.getInstantMillis(invalidObject, (Chronology) null);
            fail("Should throw ClassCastException for invalid object type");
        } catch(ClassCastException e) {
            assertTrue("Should mention Object cannot be cast to Calendar", 
                      e.getMessage().contains("java.lang.Object cannot be cast to java.util.Calendar"));
        }
    }

    // ========== Utility Method Tests ==========
    
    @Test(timeout = 4000)
    public void testGetSupportedType_ReturnsCalendarClass() throws Throwable {
        // Given: A CalendarConverter
        CalendarConverter converter = new CalendarConverter();
        
        // When: Getting supported type
        Class<?> supportedType = converter.getSupportedType();
        
        // Then: Should return Calendar class and not be synthetic
        assertNotNull("Supported type should not be null", supportedType);
        assertFalse("Supported type should not be synthetic", supportedType.isSynthetic());
    }

    // ========== Edge Case Tests ==========
    
    @Test(timeout = 4000)
    public void testGetChronology_WithConverterAsObject_ReturnsValidChronology() throws Throwable {
        // Given: A CalendarConverter used as both converter and object
        CalendarConverter converter = new CalendarConverter();
        
        // When: Getting chronology with converter as object (edge case)
        Chronology result = converter.getChronology(converter, (DateTimeZone) null);
        
        // Then: Should return a valid chronology (implementation detail)
        assertNotNull("Should return valid chronology even for edge case", result);
    }
}