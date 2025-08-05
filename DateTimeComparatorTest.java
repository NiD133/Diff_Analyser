/*
 *  Copyright 2001-2005 Stephen Colebourne
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
package org.joda.time;

import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;

/**
 * This class is a Junit unit test for the
 * org.joda.time.DateTimeComparator class.
 *
 * @author Guy Allard
 * @author Stephen Colebourne
 */
public class TestDateTimeComparator {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    //-----------------------------------------------------------------------
    // Test class structure
    //-----------------------------------------------------------------------
    @Test
    public void testClassIsPublicAndNotFinal() {
        assertTrue(Modifier.isPublic(DateTimeComparator.class.getModifiers()));
        assertFalse(Modifier.isFinal(DateTimeComparator.class.getModifiers()));
    }

    @Test
    public void testConstructorIsProtected() {
        assertEquals(1, DateTimeComparator.class.getDeclaredConstructors().length);
        assertTrue(Modifier.isProtected(DateTimeComparator.class.getDeclaredConstructors()[0].getModifiers()));
    }

    //-----------------------------------------------------------------------
    // Test factory methods
    //-----------------------------------------------------------------------
    @Test
    public void testGetInstance_noArgs_returnsFullComparator() {
        DateTimeComparator comparator = DateTimeComparator.getInstance();
        assertNull(comparator.getLowerLimit());
        assertNull(comparator.getUpperLimit());
        assertEquals("DateTimeComparator[]", comparator.toString());
    }

    @Test
    public void testGetInstance_withSameUpperAndLowerLimits_returnsSpecificComparator() {
        DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.hourOfDay());
        assertEquals(DateTimeFieldType.hourOfDay(), comparator.getLowerLimit());
        assertEquals(DateTimeFieldType.hourOfDay(), comparator.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay]", comparator.toString());
    }

    @Test
    public void testGetDateOnlyInstance_returnsComparatorWithDayOfYearLowerLimit() {
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
        assertEquals(DateTimeFieldType.dayOfYear(), comparator.getLowerLimit());
        assertNull(comparator.getUpperLimit());
        assertEquals("DateTimeComparator[dayOfYear-]", comparator.toString());
        assertSame(DateTimeComparator.getDateOnlyInstance(), DateTimeComparator.getDateOnlyInstance());
    }

    @Test
    public void testGetTimeOnlyInstance_returnsComparatorWithDayOfYearUpperLimit() {
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        assertNull(comparator.getLowerLimit());
        assertEquals(DateTimeFieldType.dayOfYear(), comparator.getUpperLimit());
        assertEquals("DateTimeComparator[-dayOfYear]", comparator.toString());
        assertSame(DateTimeComparator.getTimeOnlyInstance(), DateTimeComparator.getTimeOnlyInstance());
    }

    //-----------------------------------------------------------------------
    // Test comparison logic
    //-----------------------------------------------------------------------
    @Test
    public void testCompare_withEqualInstants_returnsZero() {
        DateTime dt1 = new DateTime("2025-08-04T10:20:30.500Z", UTC);
        DateTime dt2 = new DateTime("2025-08-04T10:20:30.500Z", UTC);

        Comparator<Object> yearComparator = DateTimeComparator.getInstance(DateTimeFieldType.year());
        assertEquals(0, yearComparator.compare(dt1, dt2));

        Comparator<Object> dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();
        assertEquals(0, dateOnlyComparator.compare(dt1, dt2));
    }

    @Test
    public void testCompare_withDifferentSupportedTypes_returnsZeroForSameInstant() {
        long now = System.currentTimeMillis();
        DateTime dateTime = new DateTime(now, UTC);
        Date date = new Date(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        Long longInstant = now;

        Comparator<Object> fullComparator = DateTimeComparator.getInstance();

        assertEquals(0, fullComparator.compare(dateTime, date));
        assertEquals(0, fullComparator.compare(dateTime, calendar));
        assertEquals(0, fullComparator.compare(dateTime, longInstant));
    }

    @Test
    public void testCompare_byYear_returnsNegativeIfFirstIsEarlier() {
        DateTime dt1 = new DateTime("2024-01-01T00:00:00Z", UTC);
        DateTime dt2 = new DateTime("2025-01-01T00:00:00Z", UTC);
        Comparator<Object> yearComparator = DateTimeComparator.getInstance(DateTimeFieldType.year());

        assertTrue(yearComparator.compare(dt1, dt2) < 0);
    }

    @Test
    public void testCompare_byYear_returnsPositiveIfFirstIsLater() {
        DateTime dt1 = new DateTime("2025-01-01T00:00:00Z", UTC);
        DateTime dt2 = new DateTime("2024-01-01T00:00:00Z", UTC);
        Comparator<Object> yearComparator = DateTimeComparator.getInstance(DateTimeFieldType.year());

        assertTrue(yearComparator.compare(dt1, dt2) > 0);
    }

    @Test
    public void testCompare_byMonth_ignoresYear() {
        DateTime dt1 = new DateTime("2024-03-01T00:00:00Z", UTC);
        DateTime dt2 = new DateTime("2025-04-01T00:00:00Z", UTC);
        Comparator<Object> monthComparator = DateTimeComparator.getInstance(DateTimeFieldType.monthOfYear(), DateTimeFieldType.year());

        assertTrue(monthComparator.compare(dt1, dt2) < 0);
    }

    @Test
    public void testCompare_byDayOfMonth_ignoresMonthAndYear() {
        DateTime dt1 = new DateTime("2025-08-10T00:00:00Z", UTC);
        DateTime dt2 = new DateTime("2025-09-11T00:00:00Z", UTC);
        Comparator<Object> dayOfMonthComparator = DateTimeComparator.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.monthOfYear());

        assertTrue(dayOfMonthComparator.compare(dt1, dt2) < 0);
    }

    @Test
    public void testCompare_dateOnly_ignoresTime() {
        DateTime dt1 = new DateTime("2025-08-04T10:00:00Z", UTC);
        DateTime dt2 = new DateTime("2025-08-04T11:00:00Z", UTC);
        Comparator<Object> dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();

        assertEquals(0, dateOnlyComparator.compare(dt1, dt2));
    }

    @Test
    public void testCompare_timeOnly_ignoresDate() {
        DateTime dt1 = new DateTime("2024-08-04T10:00:00Z", UTC);
        DateTime dt2 = new DateTime("2025-09-05T10:00:00Z", UTC);
        Comparator<Object> timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();

        assertEquals(0, timeOnlyComparator.compare(dt1, dt2));
    }

    //-----------------------------------------------------------------------
    // Test edge cases and exceptions
    //-----------------------------------------------------------------------
    @Test
    public void testCompare_withNull_treatsNullAsCurrentTime() {
        DateTime now = new DateTime();
        DateTime fiveSecondsAgo = now.minusSeconds(5);
        Comparator<Object> fullComparator = DateTimeComparator.getInstance();

        // Null is considered "now", so it should be after fiveSecondsAgo
        assertTrue(fullComparator.compare(null, fiveSecondsAgo) > 0);
        assertTrue(fullComparator.compare(fiveSecondsAgo, null) < 0);
        assertEquals(0, fullComparator.compare(null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompare_withUnsupportedType_throwsIllegalArgumentException() {
        Comparator<Object> comparator = DateTimeComparator.getInstance();
        comparator.compare("Unsupported String", new DateTime());
    }

    //-----------------------------------------------------------------------
    // Test list sorting
    //-----------------------------------------------------------------------
    @Test
    public void testSort_withDefaultComparator() {
        DateTime dt1 = new DateTime("2025-08-04T10:20:30Z", UTC);
        DateTime dt2 = new DateTime("2024-01-01T00:00:00Z", UTC);
        DateTime dt3 = new DateTime("2025-08-04T10:20:29Z", UTC);
        List<DateTime> dateTimes = new ArrayList<>(Arrays.asList(dt1, dt2, dt3));

        Collections.sort(dateTimes, DateTimeComparator.getInstance());

        assertEquals(Arrays.asList(dt2, dt3, dt1), dateTimes);
    }

    @Test
    public void testSort_byTimeOnly() {
        DateTime dt1 = new DateTime("2025-01-01T10:20:30Z", UTC); // 10:20:30
        DateTime dt2 = new DateTime("2024-01-01T23:00:00Z", UTC); // 23:00:00
        DateTime dt3 = new DateTime("2026-01-01T09:00:00Z", UTC); // 09:00:00
        List<DateTime> dateTimes = new ArrayList<>(Arrays.asList(dt1, dt2, dt3));

        Collections.sort(dateTimes, DateTimeComparator.getTimeOnlyInstance());

        assertEquals(Arrays.asList(dt3, dt1, dt2), dateTimes);
    }

    @Test
    public void testSort_byDateOnly() {
        DateTime dt1 = new DateTime("2025-08-04T10:00:00Z", UTC); // Aug 4
        DateTime dt2 = new DateTime("2025-02-01T11:00:00Z", UTC); // Feb 1
        DateTime dt3 = new DateTime("2025-08-04T09:00:00Z", UTC); // Aug 4
        List<DateTime> dateTimes = new ArrayList<>(Arrays.asList(dt1, dt2, dt3));

        Collections.sort(dateTimes, DateTimeComparator.getDateOnlyInstance());

        // Note: The order of dt1 and dt3 is stable (not guaranteed, but typical)
        assertEquals(Arrays.asList(dt2, dt1, dt3), dateTimes);
    }

    //-----------------------------------------------------------------------
    // Test equals, hashCode, and serialization
    //-----------------------------------------------------------------------
    @Test
    public void testEqualsAndHashCode() {
        DateTimeComparator c1 = DateTimeComparator.getInstance();
        DateTimeComparator c2 = DateTimeComparator.getInstance();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());

        DateTimeComparator c3 = DateTimeComparator.getTimeOnlyInstance();
        assertNotEquals(c1, c3);
        assertNotEquals(c1.hashCode(), c3.hashCode());

        DateTimeComparator c4 = DateTimeComparator.getTimeOnlyInstance();
        assertEquals(c3, c4);
        assertEquals(c3.hashCode(), c4.hashCode());
    }

    @Test
    public void testSerialization_withLimits() throws Exception {
        DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(comparator);
        oos.close();
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DateTimeComparator result = (DateTimeComparator) ois.readObject();
        ois.close();

        assertEquals(comparator, result);
    }

    @Test
    public void testSerialization_ofSingleton() throws Exception {
        DateTimeComparator original = DateTimeComparator.getInstance();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DateTimeComparator result = (DateTimeComparator) ois.readObject();
        ois.close();

        assertSame(original, result);
    }
}