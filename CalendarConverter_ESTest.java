/*
 *  Copyright 2001-2009 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link CalendarConverter}.
 */
public class CalendarConverterTest {

    private static final CalendarConverter CONVERTER = CalendarConverter.INSTANCE;

    // --- Test getInstantMillis ---

    @Test
    public void getInstantMillisShouldReturnCalendarTimeInMillis() {
        // Arrange
        Calendar calendar = new GregorianCalendar();
        long expectedMillis = 123456789000L;
        calendar.setTimeInMillis(expectedMillis);

        // Act
        long actualMillis = CONVERTER.getInstantMillis(calendar, null);

        // Assert
        assertEquals(expectedMillis, actualMillis);
    }

    @Test(expected = NullPointerException.class)
    public void getInstantMillisWithNullObjectShouldThrowNullPointerException() {
        // Act
        CONVERTER.getInstantMillis(null, null);
    }

    @Test(expected = ClassCastException.class)
    public void getInstantMillisWithInvalidTypeShouldThrowClassCastException() {
        // Act
        CONVERTER.getInstantMillis(new Object(), null);
    }

    // --- Test getChronology ---

    @Test
    public void getChronologyFromGregorianCalendarShouldReturnGJChronologyWithSameZone() {
        // Arrange
        TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
        Calendar calendar = new GregorianCalendar(gmtTimeZone);

        // Act
        Chronology chronology = CONVERTER.getChronology(calendar, (Chronology) null);

        // Assert
        assertThat("Chronology should be GJChronology for a GregorianCalendar",
                chronology, instanceOf(GJChronology.class));
        assertEquals("Chronology time zone should match the calendar's time zone",
                DateTimeZone.forTimeZone(gmtTimeZone), chronology.getZone());
    }

    @Test
    public void getChronologyWithProvidedChronologyShouldReturnThatChronology() {
        // Arrange
        Calendar calendar = new GregorianCalendar();
        Chronology expectedChronology = ISOChronology.getInstance(DateTimeZone.forID("Asia/Tokyo"));

        // Act
        Chronology actualChronology = CONVERTER.getChronology(calendar, expectedChronology);

        // Assert
        assertSame("Should return the exact chronology instance that was passed in",
                expectedChronology, actualChronology);
    }

    @Test
    public void getChronologyWithProvidedZoneShouldOverrideCalendarsZone() {
        // Arrange
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        DateTimeZone parisZone = DateTimeZone.forID("Europe/Paris");

        // Act
        Chronology chronology = CONVERTER.getChronology(calendar, parisZone);

        // Assert
        assertEquals("Chronology time zone should be the one provided, not the calendar's",
                parisZone, chronology.getZone());
    }

    @Test
    public void getChronologyWithNullZoneShouldUseCalendarsZone() {
        // Arrange
        TimeZone berlinTimeZone = TimeZone.getTimeZone("Europe/Berlin");
        Calendar calendar = new GregorianCalendar(berlinTimeZone);

        // Act
        Chronology chronology = CONVERTER.getChronology(calendar, (DateTimeZone) null);

        // Assert
        assertEquals("Chronology time zone should default to the calendar's zone",
                DateTimeZone.forTimeZone(berlinTimeZone), chronology.getZone());
    }
    
    @Test
    public void getChronologyFromGregorianCalendarWithChangeoverDateShouldReturnCorrectGJChronology() {
        // Arrange
        GregorianCalendar calendar = new GregorianCalendar();
        // Set a non-default gregorian changeover date to the epoch
        calendar.setGregorianChange(new Date(0L));

        // Act
        Chronology chronology = CONVERTER.getChronology(calendar, (DateTimeZone) null);

        // Assert
        // The converter should detect the custom changeover and return a GJChronology
        assertThat(chronology, instanceOf(GJChronology.class));

        // Verify that the cutover instant matches the one set on the calendar
        GJChronology gjChronology = (GJChronology) chronology;
        assertEquals(0L, gjChronology.getGregorianCutover().getMillis());
    }

    @Test(expected = NullPointerException.class)
    public void getChronologyWithNullObjectAndChronologyShouldThrowNullPointerException() {
        // Act
        CONVERTER.getChronology(null, ISOChronology.getInstanceUTC());
    }

    @Test(expected = NullPointerException.class)
    public void getChronologyWithNullObjectAndZoneShouldThrowNullPointerException() {
        // Act
        CONVERTER.getChronology(null, DateTimeZone.UTC);
    }

    @Test(expected = ClassCastException.class)
    public void getChronologyWithInvalidTypeShouldThrowClassCastException() {
        // Act
        CONVERTER.getChronology(new Object(), (Chronology) null);
    }

    // --- Test getSupportedType ---

    @Test
    public void getSupportedTypeShouldReturnCalendarClass() {
        // Act & Assert
        assertEquals(Calendar.class, CONVERTER.getSupportedType());
    }
}