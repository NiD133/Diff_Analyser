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
 * Unit tests for the DateTimeComparator class.
 *
 * @author Guy Allard
 */
public class TestDateTimeComparator extends TestCase {

    private static final Chronology ISO = ISOChronology.getInstance();
    
    // Test data constants
    private static final String SAMPLE_DATE_1969 = "1969-12-31T23:59:58";
    private static final String SAMPLE_DATE_1970 = "1970-01-01T00:00:00";
    private static final String SAMPLE_DATE_2002_APR_12 = "2002-04-12T00:00:00";
    private static final String SAMPLE_DATE_2002_APR_13 = "2002-04-13T00:00:00";
    
    // Comparator instances for different field types
    private DateTimeComparator millisComparator;
    private DateTimeComparator secondComparator;
    private DateTimeComparator minuteComparator;
    private DateTimeComparator hourComparator;
    private DateTimeComparator dayOfWeekComparator;
    private DateTimeComparator dayOfMonthComparator;
    private DateTimeComparator dayOfYearComparator;
    private DateTimeComparator weekOfWeekyearComparator;
    private DateTimeComparator weekyearComparator;
    private DateTimeComparator monthComparator;
    private DateTimeComparator yearComparator;
    private DateTimeComparator dateOnlyComparator;
    private DateTimeComparator timeOnlyComparator;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDateTimeComparator.class);
    }

    public TestDateTimeComparator(String name) {
        super(name);
    }

    @Override
    public void setUp() {
        // Initialize comparators for different field types
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

    @Override
    protected void tearDown() {
        // Clear all comparator references
        millisComparator = null;
        secondComparator = null;
        minuteComparator = null;
        hourComparator = null;
        dayOfWeekComparator = null;
        dayOfMonthComparator = null;
        dayOfYearComparator = null;
        weekOfWeekyearComparator = null;
        weekyearComparator = null;
        monthComparator = null;
        yearComparator = null;
        dateOnlyComparator = null;
        timeOnlyComparator = null;
    }

    //-----------------------------------------------------------------------
    // Class structure tests
    //-----------------------------------------------------------------------
    
    public void testClassStructure() {
        assertTrue("DateTimeComparator should be public", 
                  Modifier.isPublic(DateTimeComparator.class.getModifiers()));
        assertFalse("DateTimeComparator should not be final", 
                   Modifier.isFinal(DateTimeComparator.class.getModifiers()));
        assertEquals("DateTimeComparator should have exactly one constructor", 
                    1, DateTimeComparator.class.getDeclaredConstructors().length);
        assertTrue("Constructor should be protected", 
                  Modifier.isProtected(DateTimeComparator.class.getDeclaredConstructors()[0].getModifiers()));
    }
    
    //-----------------------------------------------------------------------
    // Static factory method tests
    //-----------------------------------------------------------------------
    
    public void testGetInstance_NoParameters() {
        DateTimeComparator comparator = DateTimeComparator.getInstance();
        
        assertNull("Default instance should have no lower limit", comparator.getLowerLimit());
        assertNull("Default instance should have no upper limit", comparator.getUpperLimit());
        assertEquals("Default instance toString", "DateTimeComparator[]", comparator.toString());
    }
    
    public void testGetDateOnlyInstance() {
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
        
        assertEquals("Date-only instance should have dayOfYear lower limit", 
                    DateTimeFieldType.dayOfYear(), comparator.getLowerLimit());
        assertNull("Date-only instance should have no upper limit", comparator.getUpperLimit());
        assertEquals("Date-only instance toString", "DateTimeComparator[dayOfYear-]", comparator.toString());
        
        assertSame("getDateOnlyInstance should return singleton", 
                  DateTimeComparator.getDateOnlyInstance(), DateTimeComparator.getDateOnlyInstance());
    }
    
    public void testGetTimeOnlyInstance() {
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        
        assertNull("Time-only instance should have no lower limit", comparator.getLowerLimit());
        assertEquals("Time-only instance should have dayOfYear upper limit", 
                    DateTimeFieldType.dayOfYear(), comparator.getUpperLimit());
        assertEquals("Time-only instance toString", "DateTimeComparator[-dayOfYear]", comparator.toString());
        
        assertSame("getTimeOnlyInstance should return singleton", 
                  DateTimeComparator.getTimeOnlyInstance(), DateTimeComparator.getTimeOnlyInstance());
    }
    
    public void testGetInstance_WithLowerLimit() {
        DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay());
        
        assertEquals("Should have specified lower limit", 
                    DateTimeFieldType.hourOfDay(), comparator.getLowerLimit());
        assertNull("Should have no upper limit", comparator.getUpperLimit());
        assertEquals("ToString should show lower limit", 
                    "DateTimeComparator[hourOfDay-]", comparator.toString());
        
        DateTimeComparator nullComparator = DateTimeComparator.getInstance(null);
        assertSame("getInstance(null) should return default instance", 
                  DateTimeComparator.getInstance(), nullComparator);
    }

    public void testGetInstance_WithBothLimits() {
        DateTimeComparator comparator = DateTimeComparator.getInstance(
            DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        
        assertEquals("Should have specified lower limit", 
                    DateTimeFieldType.hourOfDay(), comparator.getLowerLimit());
        assertEquals("Should have specified upper limit", 
                    DateTimeFieldType.dayOfYear(), comparator.getUpperLimit());
        assertEquals("ToString should show both limits", 
                    "DateTimeComparator[hourOfDay-dayOfYear]", comparator.toString());
        
        // Test same field for both limits
        DateTimeComparator sameFieldComparator = DateTimeComparator.getInstance(
            DateTimeFieldType.hourOfDay(), DateTimeFieldType.hourOfDay());
        assertEquals("Should show single field when limits are same", 
                    "DateTimeComparator[hourOfDay]", sameFieldComparator.toString());
        
        // Test special cases that return singletons
        assertSame("getInstance(null, null) should return default instance", 
                  DateTimeComparator.getInstance(), 
                  DateTimeComparator.getInstance(null, null));
        
        assertSame("getInstance(dayOfYear, null) should return date-only instance", 
                  DateTimeComparator.getDateOnlyInstance(), 
                  DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), null));
        
        assertSame("getInstance(null, dayOfYear) should return time-only instance", 
                  DateTimeComparator.getTimeOnlyInstance(), 
                  DateTimeComparator.getInstance(null, DateTimeFieldType.dayOfYear()));
    }
    
    public void testNullComparison_RaceConditionCheck() {
        // Test for race condition against system clock (issue #404)
        for (int i = 0; i < 10000; i++) {
            int result = DateTimeComparator.getInstance().compare(null, null);
            assertEquals("Comparing (null, null) should always return 0", 0, result);
        }
    }
    
    //-----------------------------------------------------------------------
    // Equals and hashCode tests
    //-----------------------------------------------------------------------
    
    public void testEqualsAndHashCode() {
        DateTimeComparator defaultComparator = DateTimeComparator.getInstance();
        DateTimeComparator timeOnlyComparator1 = DateTimeComparator.getTimeOnlyInstance();
        DateTimeComparator timeOnlyComparator2 = DateTimeComparator.getTimeOnlyInstance();
        DateTimeComparator dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();
        
        // Self-equality
        assertTrue("Comparator should equal itself", defaultComparator.equals(defaultComparator));
        assertEquals("Hash codes should be equal for same instance", 
                    defaultComparator.hashCode(), defaultComparator.hashCode());
        
        // Null comparison
        assertFalse("Comparator should not equal null", defaultComparator.equals(null));
        assertFalse("Comparator should not equal null", timeOnlyComparator1.equals(null));
        
        // Different types
        assertFalse("Default and time-only should not be equal", 
                   defaultComparator.equals(timeOnlyComparator1));
        assertFalse("Time-only and default should not be equal", 
                   timeOnlyComparator1.equals(defaultComparator));
        assertFalse("Hash codes should differ for different types", 
                   defaultComparator.hashCode() == timeOnlyComparator1.hashCode());
        
        // Same types
        assertTrue("Same type instances should be equal", 
                  timeOnlyComparator1.equals(timeOnlyComparator2));
        assertTrue("Same type instances should be equal (symmetric)", 
                  timeOnlyComparator2.equals(timeOnlyComparator1));
        assertEquals("Hash codes should be equal for same type", 
                    timeOnlyComparator1.hashCode(), timeOnlyComparator2.hashCode());
        
        // Different hash codes for different types
        assertFalse("Date-only and time-only should have different hash codes", 
                   dateOnlyComparator.hashCode() == timeOnlyComparator1.hashCode());
    }
    
    //-----------------------------------------------------------------------
    // Serialization tests
    //-----------------------------------------------------------------------
    
    public void testSerialization_CustomComparator() throws Exception {
        DateTimeComparator original = DateTimeComparator.getInstance(
            DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        
        // Force field initialization
        ISO.dayOfYear().toString();
        
        DateTimeComparator deserialized = serializeAndDeserialize(original);
        
        assertEquals("Deserialized comparator should equal original", original, deserialized);
    }

    public void testSerialization_DefaultComparator() throws Exception {
        DateTimeComparator original = DateTimeComparator.getInstance();
        DateTimeComparator deserialized = serializeAndDeserialize(original);
        
        assertSame("Deserialized default comparator should be same singleton", original, deserialized);
    }

    //-----------------------------------------------------------------------
    // Basic comparison tests with different object types
    //-----------------------------------------------------------------------
    
    public void testComparison_WithDateTime() {
        long currentTime = System.currentTimeMillis();
        DateTime dateTime1 = new DateTime(currentTime, DateTimeZone.UTC);
        DateTime dateTime2 = new DateTime(currentTime, DateTimeZone.UTC);
        
        assertEquals("Same milliseconds should be equal", dateTime1.getMillis(), dateTime2.getMillis());
        
        assertAllComparatorsReturnZero(dateTime1, dateTime2);
    }

    public void testComparison_WithReadableInstant() {
        long currentTime = System.currentTimeMillis();
        ReadableInstant instant1 = new DateTime(currentTime, DateTimeZone.UTC);
        ReadableInstant instant2 = new DateTime(currentTime, DateTimeZone.UTC);
        
        assertEquals("Same milliseconds should be equal", instant1.getMillis(), instant2.getMillis());
        
        assertAllComparatorsReturnZero(instant1, instant2);
    }

    public void testComparison_WithDate() {
        long currentTime = System.currentTimeMillis();
        Date date1 = new Date(currentTime);
        Date date2 = new Date(currentTime);
        
        assertAllComparatorsReturnZero(date1, date2);
    }

    public void testComparison_WithLong() {
        Long time1 = System.currentTimeMillis();
        Long time2 = time1;
        
        assertAllComparatorsReturnZero(time1, time2);
    }

    public void testComparison_WithCalendar() {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = calendar1; // Same reference
        
        assertAllComparatorsReturnZero(calendar1, calendar2);
    }

    //-----------------------------------------------------------------------
    // Field-specific comparison tests
    //-----------------------------------------------------------------------
    
    public void testMillisecondComparison() {
        long baseTime = System.currentTimeMillis();
        DateTime earlier = new DateTime(baseTime, DateTimeZone.UTC);
        DateTime later = new DateTime(baseTime + 1, DateTimeZone.UTC);
        
        assertEquals("Earlier should be less than later", -1, millisComparator.compare(earlier, later));
        assertEquals("Later should be greater than earlier", 1, millisComparator.compare(later, earlier));
    }

    public void testSecondComparison() {
        DateTime time58Seconds = createDateTime("1969-12-31T23:59:58");
        DateTime time59Seconds = createDateTime("1969-12-31T23:50:59");
        
        assertEquals("58 seconds should be less than 59 seconds", 
                    -1, secondComparator.compare(time58Seconds, time59Seconds));
        assertEquals("59 seconds should be greater than 58 seconds", 
                    1, secondComparator.compare(time59Seconds, time58Seconds));
        
        DateTime zeroSeconds = createDateTime("1970-01-01T00:00:00");
        DateTime oneSecond = createDateTime("1970-01-01T00:00:01");
        
        assertEquals("0 seconds should be less than 1 second", 
                    -1, secondComparator.compare(zeroSeconds, oneSecond));
        assertEquals("1 second should be greater than 0 seconds", 
                    1, secondComparator.compare(oneSecond, zeroSeconds));
    }

    public void testMinuteComparison() {
        DateTime minute58 = createDateTime("1969-12-31T23:58:00");
        DateTime minute59 = createDateTime("1969-12-31T23:59:00");
        
        assertEquals("Minute 58 should be less than minute 59", 
                    -1, minuteComparator.compare(minute58, minute59));
        assertEquals("Minute 59 should be greater than minute 58", 
                    1, minuteComparator.compare(minute59, minute58));
    }

    public void testHourComparison() {
        DateTime hour22 = createDateTime("1969-12-31T22:00:00");
        DateTime hour23 = createDateTime("1969-12-31T23:00:00");
        
        assertEquals("Hour 22 should be less than hour 23", 
                    -1, hourComparator.compare(hour22, hour23));
        assertEquals("Hour 23 should be greater than hour 22", 
                    1, hourComparator.compare(hour23, hour22));
        
        // Test day boundary crossing
        DateTime lastHourPrevDay = createDateTime("1969-12-31T23:59:59");
        DateTime firstHourNextDay = createDateTime("1970-01-01T00:00:00");
        
        assertEquals("Last hour of previous day should be greater than first hour of next day", 
                    1, hourComparator.compare(lastHourPrevDay, firstHourNextDay));
    }

    public void testDayOfWeekComparison() {
        // 2002-04-12 = Friday, 2002-04-13 = Saturday
        DateTime friday = createDateTime("2002-04-12T00:00:00");
        DateTime saturday = createDateTime("2002-04-13T00:00:00");
        
        assertEquals("Friday should be less than Saturday", 
                    -1, dayOfWeekComparator.compare(friday, saturday));
        assertEquals("Saturday should be greater than Friday", 
                    1, dayOfWeekComparator.compare(saturday, friday));
    }

    public void testYearComparison() {
        DateTime year2000 = createDateTime("2000-01-01T00:00:00");
        DateTime year2001 = createDateTime("2001-01-01T00:00:00");
        
        assertEquals("Year 2000 should be less than 2001", 
                    -1, yearComparator.compare(year2000, year2001));
        assertEquals("Year 2001 should be greater than 2000", 
                    1, yearComparator.compare(year2001, year2000));
    }

    //-----------------------------------------------------------------------
    // List sorting tests
    //-----------------------------------------------------------------------
    
    public void testListSorting_BasicComparator() {
        String[] dateStrings = {
            "1999-02-01T00:00:00",
            "1998-01-20T00:00:00"
        };
        
        List<DateTime> dateList = createDateTimeList(dateStrings);
        boolean wasInitiallySorted = isListSorted(dateList);
        
        Collections.sort(dateList);
        boolean isSortedAfter = isListSorted(dateList);
        
        assertNotSame("List should change sort order", wasInitiallySorted, isSortedAfter);
        assertTrue("List should be sorted after Collections.sort()", isSortedAfter);
    }

    public void testListSorting_MillisComparator() {
        List<DateTime> dateList = new ArrayList<>();
        long baseTime = 12345L * 1000L;
        
        // Add dates with different millisecond values
        dateList.add(new DateTime(baseTime + 999L, DateTimeZone.UTC));
        dateList.add(new DateTime(baseTime + 222L, DateTimeZone.UTC));
        dateList.add(new DateTime(baseTime + 456L, DateTimeZone.UTC));
        dateList.add(new DateTime(baseTime + 888L, DateTimeZone.UTC));
        dateList.add(new DateTime(baseTime + 123L, DateTimeZone.UTC));
        dateList.add(new DateTime(baseTime + 0L, DateTimeZone.UTC));
        
        boolean wasInitiallySorted = isListSorted(dateList);
        Collections.sort(dateList, millisComparator);
        boolean isSortedAfter = isListSorted(dateList);
        
        assertNotSame("Millisecond sorting should change order", wasInitiallySorted, isSortedAfter);
        assertTrue("List should be sorted by milliseconds", isSortedAfter);
    }

    public void testListSorting_YearComparator() {
        String[] dateStrings = {
            "1999-02-01T00:00:00",
            "1998-02-01T00:00:00",
            "2525-02-01T00:00:00",
            "1776-02-01T00:00:00",
            "1863-02-01T00:00:00",
            "1066-02-01T00:00:00",
            "2100-02-01T00:00:00"
        };
        
        List<DateTime> dateList = createDateTimeList(dateStrings);
        boolean wasInitiallySorted = isListSorted(dateList);
        
        Collections.sort(dateList, yearComparator);
        boolean isSortedAfter = isListSorted(dateList);
        
        assertNotSame("Year sorting should change order", wasInitiallySorted, isSortedAfter);
        assertTrue("List should be sorted by year", isSortedAfter);
    }

    //-----------------------------------------------------------------------
    // Edge case and error condition tests
    //-----------------------------------------------------------------------
    
    public void testNullComparison() {
        // null means "now", so it should be greater than past dates
        DateTime pastDate = createDateTime("2000-01-01T00:00:00");
        
        assertTrue("null (now) should be greater than past date", 
                  yearComparator.compare(null, pastDate) > 0);
        assertTrue("Past date should be less than null (now)", 
                  yearComparator.compare(pastDate, null) < 0);
    }

    public void testInvalidObjectType() {
        DateTime validDate = createDateTime("2000-01-01T00:00:00");
        
        try {
            yearComparator.compare("InvalidObject", validDate);
            fail("Should throw IllegalArgumentException for invalid object type");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    //-----------------------------------------------------------------------
    // Helper methods
    //-----------------------------------------------------------------------
    
    private DateTime createDateTime(String dateString) {
        try {
            return new DateTime(dateString, DateTimeZone.UTC);
        } catch (IllegalArgumentException e) {
            fail("Failed to create DateTime from: " + dateString);
            return null;
        }
    }

    private List<DateTime> createDateTimeList(String[] dateStrings) {
        List<DateTime> dateList = new ArrayList<>();
        for (String dateString : dateStrings) {
            dateList.add(createDateTime(dateString));
        }
        return dateList;
    }

    private boolean isListSorted(List<DateTime> dateList) {
        for (int i = 1; i < dateList.size(); i++) {
            DateTime previous = dateList.get(i - 1);
            DateTime current = dateList.get(i);
            if (previous.getMillis() > current.getMillis()) {
                return false;
            }
        }
        return true;
    }

    private void assertAllComparatorsReturnZero(Object obj1, Object obj2) {
        assertEquals("Millis comparator should return 0", 0, millisComparator.compare(obj1, obj2));
        assertEquals("Second comparator should return 0", 0, secondComparator.compare(obj1, obj2));
        assertEquals("Minute comparator should return 0", 0, minuteComparator.compare(obj1, obj2));
        assertEquals("Hour comparator should return 0", 0, hourComparator.compare(obj1, obj2));
        assertEquals("Day of week comparator should return 0", 0, dayOfWeekComparator.compare(obj1, obj2));
        assertEquals("Day of month comparator should return 0", 0, dayOfMonthComparator.compare(obj1, obj2));
        assertEquals("Day of year comparator should return 0", 0, dayOfYearComparator.compare(obj1, obj2));
        assertEquals("Week of weekyear comparator should return 0", 0, weekOfWeekyearComparator.compare(obj1, obj2));
        assertEquals("Weekyear comparator should return 0", 0, weekyearComparator.compare(obj1, obj2));
        assertEquals("Month comparator should return 0", 0, monthComparator.compare(obj1, obj2));
        assertEquals("Year comparator should return 0", 0, yearComparator.compare(obj1, obj2));
        assertEquals("Date-only comparator should return 0", 0, dateOnlyComparator.compare(obj1, obj2));
        assertEquals("Time-only comparator should return 0", 0, timeOnlyComparator.compare(obj1, obj2));
    }

    private DateTimeComparator serializeAndDeserialize(DateTimeComparator original) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (DateTimeComparator) ois.readObject();
        }
    }
}