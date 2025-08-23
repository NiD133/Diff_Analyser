package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
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
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class CalendarConverter_ESTest extends CalendarConverter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testGetChronologyWithTimeZone() throws Throwable {
        // Test getting chronology with a specific time zone
        CalendarConverter calendarConverter = new CalendarConverter();
        ZoneOffset zoneOffset = ZoneOffset.MIN;
        TimeZone timeZone = TimeZone.getTimeZone((ZoneId) zoneOffset);
        MockGregorianCalendar mockCalendar = new MockGregorianCalendar(timeZone);
        Chronology chronology = calendarConverter.getChronology(mockCalendar, (Chronology) null);
        assertNotNull(chronology);
    }

    @Test(timeout = 4000)
    public void testGetInstantMillisWithMockCalendar() throws Throwable {
        // Test getting instant milliseconds from a mock calendar
        CalendarConverter calendarConverter = new CalendarConverter();
        MockGregorianCalendar mockCalendar = new MockGregorianCalendar(741, 206, 741, 741, 741, 206);
        long millis = calendarConverter.getInstantMillis(mockCalendar, (Chronology) null);
        assertEquals(0L, millis);
    }

    @Test(timeout = 4000)
    public void testGetInstantMillisWithDefaultMockCalendar() throws Throwable {
        // Test getting instant milliseconds with default mock calendar settings
        MockGregorianCalendar mockCalendar = new MockGregorianCalendar();
        CalendarConverter calendarConverter = CalendarConverter.INSTANCE;
        long millis = calendarConverter.getInstantMillis(mockCalendar, (Chronology) null);
        assertEquals(1392409281320L, millis);
    }

    @Test(timeout = 4000)
    public void testGetInstantMillisWithSystemTime() throws Throwable {
        // Test getting instant milliseconds with system time set to a specific value
        System.setCurrentTimeMillis(-1870L);
        CalendarConverter calendarConverter = CalendarConverter.INSTANCE;
        TimeZone timeZone = TimeZone.getDefault();
        MockGregorianCalendar mockCalendar = new MockGregorianCalendar(timeZone);
        long millis = calendarConverter.getInstantMillis(mockCalendar, (Chronology) null);
        assertEquals(-1870L, millis);
    }

    @Test(timeout = 4000)
    public void testGetInstantMillisWithNullObject() throws Throwable {
        // Test that a NullPointerException is thrown when passing a null object
        CalendarConverter calendarConverter = CalendarConverter.INSTANCE;
        try {
            calendarConverter.getInstantMillis(null, (Chronology) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.CalendarConverter", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetChronologyWithNullObject() throws Throwable {
        // Test that a NullPointerException is thrown when passing a null object to getChronology
        CalendarConverter calendarConverter = CalendarConverter.INSTANCE;
        try {
            calendarConverter.getChronology(null, (DateTimeZone) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.convert.CalendarConverter", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetChronologyWithInvalidObject() throws Throwable {
        // Test that a ClassCastException is thrown when passing an invalid object type
        CalendarConverter calendarConverter = new CalendarConverter();
        try {
            calendarConverter.getChronology(calendarConverter, (Chronology) null);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.joda.time.convert.CalendarConverter", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetChronologyWithMockCalendar() throws Throwable {
        // Test getting chronology with a mock calendar
        CalendarConverter calendarConverter = CalendarConverter.INSTANCE;
        MockGregorianCalendar mockCalendar = new MockGregorianCalendar(-3785, -2040, 0, -1127, -2040);
        Chronology chronology = calendarConverter.getChronology(mockCalendar, (DateTimeZone) null);
        assertNotNull(chronology);
    }

    @Test(timeout = 4000)
    public void testGetChronologyWithGregorianCalendar() throws Throwable {
        // Test getting chronology with a GregorianCalendar
        CalendarConverter calendarConverter = new CalendarConverter();
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(1963);
        MockGregorianCalendar mockCalendar = new MockGregorianCalendar(0, 1963, 0, -247581896, -21, 3826);
        ZonedDateTime zonedDateTime = mockCalendar.toZonedDateTime();
        GregorianCalendar gregorianCalendar = MockGregorianCalendar.from(zonedDateTime);
        Chronology chronology = calendarConverter.getChronology(gregorianCalendar, dateTimeZone);
        assertNotNull(chronology);
    }

    @Test(timeout = 4000)
    public void testGetChronologyWithGregorianChange() throws Throwable {
        // Test getting chronology with a Gregorian change date
        CalendarConverter calendarConverter = new CalendarConverter();
        MockDate mockDate = new MockDate(9223372036854775785L);
        MockGregorianCalendar mockCalendar = new MockGregorianCalendar();
        mockCalendar.setGregorianChange(mockDate);
        Chronology chronology = calendarConverter.getChronology(mockCalendar, (DateTimeZone) null);
        assertNotNull(chronology);
    }

    @Test(timeout = 4000)
    public void testGetChronologyWithZonedDateTime() throws Throwable {
        // Test getting chronology with a ZonedDateTime
        CalendarConverter calendarConverter = new CalendarConverter();
        ZonedDateTime zonedDateTime = MockZonedDateTime.now();
        GregorianCalendar gregorianCalendar = MockGregorianCalendar.from(zonedDateTime);
        Chronology chronology = calendarConverter.getChronology(gregorianCalendar, (Chronology) null);
        assertNotNull(chronology);
    }

    @Test(timeout = 4000)
    public void testGetChronologyWithSelfReference() throws Throwable {
        // Test getting chronology with self-reference
        CalendarConverter calendarConverter = new CalendarConverter();
        Chronology chronology = calendarConverter.getChronology(calendarConverter, (DateTimeZone) null);
        assertNotNull(chronology);
    }

    @Test(timeout = 4000)
    public void testGetSupportedType() throws Throwable {
        // Test getting the supported type of the converter
        CalendarConverter calendarConverter = new CalendarConverter();
        Class<?> supportedType = calendarConverter.getSupportedType();
        assertFalse(supportedType.isSynthetic());
    }

    @Test(timeout = 4000)
    public void testGetInstantMillisWithInvalidObject() throws Throwable {
        // Test that a ClassCastException is thrown when passing an invalid object type to getInstantMillis
        CalendarConverter calendarConverter = new CalendarConverter();
        Object invalidObject = new Object();
        try {
            calendarConverter.getInstantMillis(invalidObject, (Chronology) null);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.joda.time.convert.CalendarConverter", e);
        }
    }
}