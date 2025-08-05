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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.ISOChronology;

/**
 * Unit tests for {@link org.joda.time.DateTimeComparator}.
 */
public class TestDateTimeComparator extends TestCase {

    private static final Chronology ISO = ISOChronology.getInstanceUTC();
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final String TEST_DATE = "2002-04-12T00:00:00";

    // Comparator instances
    private Comparator millisComparator;
    private Comparator secondComparator;
    private Comparator minuteComparator;
    private Comparator hourComparator;
    private Comparator dayOfWeekComparator;
    private Comparator dayOfMonthComparator;
    private Comparator dayOfYearComparator;
    private Comparator weekOfWeekyearComparator;
    private Comparator weekyearComparator;
    private Comparator monthComparator;
    private Comparator yearComparator;
    private Comparator dateOnlyComparator;
    private Comparator timeOnlyComparator;

    @Override
    public void setUp() {
        // Initialize all comparator instances
        millisComparator = DateTimeComparator.getInstance(null, DateTimeFieldType.secondOfMinute());
        secondComparator = DateTimeComparator.getInstance(DateTimeFieldType.secondOfMinute(), DateTimeFieldType.minuteOfHour());
        minuteComparator = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfHour(), DateTimeFieldType.hourOfDay());
        hourComparator = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        dayOfWeekComparator = DateTimeComparator.getInstance(DateTimeFieldType.dayOfWeek(), DateTimeFieldType.weekOfWeekyear());
        dayOfMonthComparator = DateTimeComparator.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.monthOfYear());
        dayOfYearComparator = DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), DateTimeFieldType.year());
        weekOfWeekyearComparator = DateTimeComparator.getInstance(DateTimeFieldType.weekOfWeekyear(), DateTimeFieldType.weekyear());
        weekyearComparator = DateTimeComparator.getInstance(DateTimeFieldType.weekyear());
        monthComparator = DateTimeComparator.getInstance(DateTimeFieldType.monthOfYear(), DateTimeFieldType.year());
        yearComparator = DateTimeComparator.getInstance(DateTimeFieldType.year());
        dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();
        timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();
    }

    // Class structure tests -------------------------------------------------
    
    public void testClassStructure() {
        Class<?> cls = DateTimeComparator.class;
        assertTrue("Class should be public", Modifier.isPublic(cls.getModifiers()));
        assertFalse("Class should not be final", Modifier.isFinal(cls.getModifiers()));
        assertEquals("Should have exactly 1 constructor", 1, cls.getDeclaredConstructors().length);
        assertTrue("Constructor should be protected", 
            Modifier.isProtected(cls.getDeclaredConstructors()[0].getModifiers()));
    }

    // Factory method tests --------------------------------------------------
    
    public void testGetInstance() {
        DateTimeComparator c = DateTimeComparator.getInstance();
        assertNull(c.getLowerLimit());
        assertNull(c.getUpperLimit());
        assertEquals("DateTimeComparator[]", c.toString());
    }

    public void testGetDateOnlyInstance() {
        DateTimeComparator c = DateTimeComparator.getDateOnlyInstance();
        assertEquals(DateTimeFieldType.dayOfYear(), c.getLowerLimit());
        assertNull(c.getUpperLimit());
        assertEquals("DateTimeComparator[dayOfYear-]", c.toString());
        assertSame(DateTimeComparator.getDateOnlyInstance(), c);
    }

    public void testGetTimeOnlyInstance() {
        DateTimeComparator c = DateTimeComparator.getTimeOnlyInstance();
        assertNull(c.getLowerLimit());
        assertEquals(DateTimeFieldType.dayOfYear(), c.getUpperLimit());
        assertEquals("DateTimeComparator[-dayOfYear]", c.toString());
        assertSame(DateTimeComparator.getTimeOnlyInstance(), c);
    }

    public void testGetInstanceWithLowerLimit() {
        DateTimeComparator c = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getLowerLimit());
        assertNull(c.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay-]", c.toString());
        
        c = DateTimeComparator.getInstance(null);
        assertSame(DateTimeComparator.getInstance(), c);
    }

    public void testGetInstanceWithBothLimits() {
        DateTimeComparator c = DateTimeComparator.getInstance(
            DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getLowerLimit());
        assertEquals(DateTimeFieldType.dayOfYear(), c.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay-dayOfYear]", c.toString());
        
        c = DateTimeComparator.getInstance(
            DateTimeFieldType.hourOfDay(), DateTimeFieldType.hourOfDay());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getLowerLimit());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay]", c.toString());
        
        c = DateTimeComparator.getInstance(null, null);
        assertSame(DateTimeComparator.getInstance(), c);
        
        c = DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), null);
        assertSame(DateTimeComparator.getDateOnlyInstance(), c);
        
        c = DateTimeComparator.getInstance(null, DateTimeFieldType.dayOfYear());
        assertSame(DateTimeComparator.getTimeOnlyInstance(), c);
    }

    // Edge case tests -------------------------------------------------------
    
    public void testNullComparison() {
        for (int i = 0; i < 100; i++) {
            assertEquals("Null comparisons should return 0", 
                0, DateTimeComparator.getInstance().compare(null, null));
        }
    }

    public void testEqualsAndHashCode() {
        DateTimeComparator base = DateTimeComparator.getInstance();
        DateTimeComparator timeOnly = DateTimeComparator.getTimeOnlyInstance();
        DateTimeComparator dateOnly = DateTimeComparator.getDateOnlyInstance();
        
        // Reflexivity
        assertEquals(base, base);
        assertEquals(timeOnly, timeOnly);
        
        // Symmetry & consistency
        assertNotEquals(base, timeOnly);
        assertNotEquals(base, dateOnly);
        assertNotEquals(timeOnly, dateOnly);
        
        // Hash code consistency
        assertEquals(base.hashCode(), base.hashCode());
        assertEquals(timeOnly.hashCode(), timeOnly.hashCode());
        assertNotEquals(base.hashCode(), timeOnly.hashCode());
    }

    // Serialization tests ---------------------------------------------------
    
    public void testSerializationWithLimits() throws Exception {
        DateTimeComparator original = DateTimeComparator.getInstance(
            DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();
        
        ObjectInputStream ois = new ObjectInputStream(
            new ByteArrayInputStream(baos.toByteArray()));
        DateTimeComparator deserialized = (DateTimeComparator) ois.readObject();
        ois.close();
        
        assertEquals(original, deserialized);
    }

    public void testSerializationWithoutLimits() throws Exception {
        DateTimeComparator original = DateTimeComparator.getInstance();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();
        
        ObjectInputStream ois = new ObjectInputStream(
            new ByteArrayInputStream(baos.toByteArray()));
        DateTimeComparator deserialized = (DateTimeComparator) ois.readObject();
        ois.close();
        
        assertSame(original, deserialized);
    }

    // Comparator functionality tests ----------------------------------------
    
    public void testAllComparatorsWithEqualDateTimeObjects() {
        DateTime now = new DateTime(UTC);
        testAllComparators(now, now);
    }

    public void testAllComparatorsWithEqualReadableInstantObjects() {
        ReadableInstant now = new DateTime(UTC);
        testAllComparators(now, now);
    }

    public void testAllComparatorsWithEqualDateObjects() {
        Date now = new Date();
        testAllComparators(now, now);
    }

    public void testAllComparatorsWithEqualLongObjects() {
        Long now = System.currentTimeMillis();
        testAllComparators(now, now);
    }

    public void testAllComparatorsWithEqualCalendarObjects() {
        Calendar now = Calendar.getInstance();
        testAllComparators(now, now);
    }

    private void testAllComparators(Object a, Object b) {
        assertComparatorEquals(0, millisComparator, a, b);
        assertComparatorEquals(0, secondComparator, a, b);
        assertComparatorEquals(0, minuteComparator, a, b);
        assertComparatorEquals(0, hourComparator, a, b);
        assertComparatorEquals(0, dayOfWeekComparator, a, b);
        assertComparatorEquals(0, dayOfMonthComparator, a, b);
        assertComparatorEquals(0, dayOfYearComparator, a, b);
        assertComparatorEquals(0, weekOfWeekyearComparator, a, b);
        assertComparatorEquals(0, weekyearComparator, a, b);
        assertComparatorEquals(0, monthComparator, a, b);
        assertComparatorEquals(0, yearComparator, a, b);
        assertComparatorEquals(0, dateOnlyComparator, a, b);
        assertComparatorEquals(0, timeOnlyComparator, a, b);
    }

    // Field-specific comparison tests ---------------------------------------
    
    public void testMillisecondComparison() {
        DateTime base = new DateTime(UTC);
        DateTime later = base.plusMillis(1);
        assertComparatorEquals(-1, millisComparator, base, later);
        assertComparatorEquals(1, millisComparator, later, base);
    }

    public void testSecondComparison() {
        DateTime early = parse("1969-12-31T23:59:58");
        DateTime late = parse("1969-12-31T23:59:59");
        assertComparatorEquals(-1, secondComparator, early, late);
        assertComparatorEquals(1, secondComparator, late, early);
    }

    public void testMinuteComparison() {
        DateTime early = parse("1970-01-01T00:00:00");
        DateTime late = parse("1970-01-01T00:01:00");
        assertComparatorEquals(-1, minuteComparator, early, late);
        assertComparatorEquals(1, minuteComparator, late, early);
    }

    public void testHourComparison() {
        DateTime early = parse("1969-12-31T22:00:00");
        DateTime late = parse("1969-12-31T23:00:00");
        assertComparatorEquals(-1, hourComparator, early, late);
        assertComparatorEquals(1, hourComparator, late, early);
    }

    public void testDayOfWeekComparison() {
        DateTime wednesday = parse("2023-05-10T00:00:00"); // Wednesday
        DateTime thursday = parse("2023-05-11T00:00:00");  // Thursday
        assertComparatorEquals(-1, dayOfWeekComparator, wednesday, thursday);
        assertComparatorEquals(1, dayOfWeekComparator, thursday, wednesday);
    }

    public void testDayOfMonthComparison() {
        DateTime first = parse("2023-05-01T00:00:00");
        DateTime second = parse("2023-05-02T00:00:00");
        assertComparatorEquals(-1, dayOfMonthComparator, first, second);
        assertComparatorEquals(1, dayOfMonthComparator, second, first);
    }

    public void testDayOfYearComparison() {
        DateTime jan1 = parse("2023-01-01T00:00:00");
        DateTime dec31 = parse("2023-12-31T00:00:00");
        assertComparatorEquals(-1, dayOfYearComparator, jan1, dec31);
        assertComparatorEquals(1, dayOfYearComparator, dec31, jan1);
    }

    public void testWeekOfWeekyearComparison() {
        DateTime week1 = parse("2023-01-02T00:00:00"); // Week 1
        DateTime week2 = parse("2023-01-09T00:00:00"); // Week 2
        assertComparatorEquals(-1, weekOfWeekyearComparator, week1, week2);
        assertComparatorEquals(1, weekOfWeekyearComparator, week2, week1);
    }

    public void testWeekyearComparison() {
        DateTime year2022 = parse("2022-12-31T23:59:59");
        DateTime year2023 = parse("2023-01-04T00:00:00");
        assertComparatorEquals(-1, weekyearComparator, year2022, year2023);
        assertComparatorEquals(1, weekyearComparator, year2023, year2022);
    }

    public void testMonthComparison() {
        DateTime jan = parse("2023-01-01T00:00:00");
        DateTime feb = parse("2023-02-01T00:00:00");
        assertComparatorEquals(-1, monthComparator, jan, feb);
        assertComparatorEquals(1, monthComparator, feb, jan);
    }

    public void testYearComparison() {
        DateTime year2000 = parse("2000-01-01T00:00:00");
        DateTime year2001 = parse("2001-01-01T00:00:00");
        assertComparatorEquals(-1, yearComparator, year2000, year2001);
        assertComparatorEquals(1, yearComparator, year2001, year2000);
    }

    // Special case tests ----------------------------------------------------
    
    public void testNullObjectComparison() {
        DateTime now = new DateTime(UTC);
        assertTrue(yearComparator.compare(null, now) > 0);
        assertTrue(yearComparator.compare(now, null) < 0);
    }

    public void testInvalidObjectComparison() {
        try {
            yearComparator.compare("Invalid", new DateTime(UTC));
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    // List sorting tests ----------------------------------------------------
    
    public void testListSortingWithNaturalOrder() {
        List<DateTime> dates = createDateList(
            "1999-02-01T00:00:00", 
            "1998-01-20T00:00:00"
        );
        assertListSorting(dates, null);
    }

    public void testListSortingWithSecondComparator() {
        List<DateTime> dates = createDateList(
            "1999-02-01T00:00:30",
            "1999-02-01T00:00:10",
            "1999-02-01T00:00:25"
        );
        assertListSorting(dates, secondComparator);
    }

    public void testListSortingWithMinuteComparator() {
        List<DateTime> dates = createDateList(
            "1999-02-01T00:30:00",
            "1999-02-01T00:10:00",
            "1999-02-01T00:25:00"
        );
        assertListSorting(dates, minuteComparator);
    }

    public void testListSortingWithHourComparator() {
        List<DateTime> dates = createDateList(
            "1999-02-01T23:00:00",
            "1999-02-01T10:00:00",
            "1999-02-01T15:00:00"
        );
        assertListSorting(dates, hourComparator);
    }

    public void testListSortingWithDayOfWeekComparator() {
        List<DateTime> dates = createDateList(
            "2023-05-12T00:00:00", // Friday
            "2023-05-08T00:00:00", // Monday
            "2023-05-10T00:00:00"  // Wednesday
        );
        assertListSorting(dates, dayOfWeekComparator);
    }

    public void testListSortingWithDayOfMonthComparator() {
        List<DateTime> dates = createDateList(
            "2023-05-15T00:00:00",
            "2023-05-01T00:00:00",
            "2023-05-10T00:00:00"
        );
        assertListSorting(dates, dayOfMonthComparator);
    }

    public void testListSortingWithDateOnlyComparator() {
        List<DateTime> dates = createDateList(
            "2023-12-31T00:00:00",
            "2023-01-01T00:00:00",
            "2023-06-15T00:00:00"
        );
        assertListSorting(dates, dateOnlyComparator);
    }

    // Helper methods --------------------------------------------------------
    
    private DateTime parse(String text) {
        return new DateTime(text, UTC);
    }
    
    private List<DateTime> createDateList(String... dateStrings) {
        List<DateTime> dates = new ArrayList<>();
        for (String dateString : dateStrings) {
            dates.add(parse(dateString));
        }
        return dates;
    }
    
    private void assertComparatorEquals(int expected, Comparator comparator, Object a, Object b) {
        assertEquals(expected, comparator.compare(a, b));
    }
    
    private void assertListSorting(List<DateTime> dates, Comparator comparator) {
        // Test natural order if no comparator provided
        Comparator<DateTime> effectiveComparator = (comparator != null) 
            ? comparator 
            : (d1, d2) -> d1.compareTo(d2);
        
        // Verify initial unsorted state
        assertFalse("List should initially be unsorted", isSorted(dates, effectiveComparator));
        
        // Sort and verify
        Collections.sort(dates, effectiveComparator);
        assertTrue("List should be sorted after sort", isSorted(dates, effectiveComparator));
    }
    
    private boolean isSorted(List<DateTime> dates, Comparator<DateTime> comparator) {
        for (int i = 0; i < dates.size() - 1; i++) {
            if (comparator.compare(dates.get(i), dates.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }
}