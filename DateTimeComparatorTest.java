package org.joda.time;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.chrono.ISOChronology;

/**
 * JUnit test suite for the org.joda.time.DateTimeComparator class.
 * Tests various functionalities and edge cases of the DateTimeComparator.
 */
public class TestDateTimeComparator extends TestCase {

    private static final Chronology ISO = ISOChronology.getInstance();

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestDateTimeComparator.class);
    }

    public TestDateTimeComparator(String name) {
        super(name);
    }

    private DateTime aDateTime;
    private DateTime bDateTime;
    private Comparator<DateTime> cMillis;
    private Comparator<DateTime> cSecond;
    private Comparator<DateTime> cMinute;
    private Comparator<DateTime> cHour;
    private Comparator<DateTime> cDayOfWeek;
    private Comparator<DateTime> cDayOfMonth;
    private Comparator<DateTime> cDayOfYear;
    private Comparator<DateTime> cWeekOfWeekyear;
    private Comparator<DateTime> cWeekyear;
    private Comparator<DateTime> cMonth;
    private Comparator<DateTime> cYear;
    private Comparator<DateTime> cDate;
    private Comparator<DateTime> cTime;

    @Override
    public void setUp() {
        cMillis = DateTimeComparator.getInstance(null, DateTimeFieldType.secondOfMinute());
        cSecond = DateTimeComparator.getInstance(DateTimeFieldType.secondOfMinute(), DateTimeFieldType.minuteOfHour());
        cMinute = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfHour(), DateTimeFieldType.hourOfDay());
        cHour = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        cDayOfWeek = DateTimeComparator.getInstance(DateTimeFieldType.dayOfWeek(), DateTimeFieldType.weekOfWeekyear());
        cDayOfMonth = DateTimeComparator.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.monthOfYear());
        cDayOfYear = DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), DateTimeFieldType.year());
        cWeekOfWeekyear = DateTimeComparator.getInstance(DateTimeFieldType.weekOfWeekyear(), DateTimeFieldType.weekyear());
        cWeekyear = DateTimeComparator.getInstance(DateTimeFieldType.weekyear());
        cMonth = DateTimeComparator.getInstance(DateTimeFieldType.monthOfYear(), DateTimeFieldType.year());
        cYear = DateTimeComparator.getInstance(DateTimeFieldType.year());
        cDate = DateTimeComparator.getDateOnlyInstance();
        cTime = DateTimeComparator.getTimeOnlyInstance();
    }

    @Override
    protected void tearDown() {
        aDateTime = null;
        bDateTime = null;
        cMillis = null;
        cSecond = null;
        cMinute = null;
        cHour = null;
        cDayOfWeek = null;
        cDayOfMonth = null;
        cDayOfYear = null;
        cWeekOfWeekyear = null;
        cWeekyear = null;
        cMonth = null;
        cYear = null;
        cDate = null;
        cTime = null;
    }

    public void testClassModifiers() {
        assertTrue(Modifier.isPublic(DateTimeComparator.class.getModifiers()));
        assertFalse(Modifier.isFinal(DateTimeComparator.class.getModifiers()));
        assertEquals(1, DateTimeComparator.class.getDeclaredConstructors().length);
        assertTrue(Modifier.isProtected(DateTimeComparator.class.getDeclaredConstructors()[0].getModifiers()));
    }

    public void testStaticGetInstance() {
        DateTimeComparator comparator = DateTimeComparator.getInstance();
        assertNull(comparator.getLowerLimit());
        assertNull(comparator.getUpperLimit());
        assertEquals("DateTimeComparator[]", comparator.toString());
    }

    public void testStaticGetDateOnlyInstance() {
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
        assertEquals(DateTimeFieldType.dayOfYear(), comparator.getLowerLimit());
        assertNull(comparator.getUpperLimit());
        assertEquals("DateTimeComparator[dayOfYear-]", comparator.toString());
        assertSame(DateTimeComparator.getDateOnlyInstance(), DateTimeComparator.getDateOnlyInstance());
    }

    public void testStaticGetTimeOnlyInstance() {
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        assertNull(comparator.getLowerLimit());
        assertEquals(DateTimeFieldType.dayOfYear(), comparator.getUpperLimit());
        assertEquals("DateTimeComparator[-dayOfYear]", comparator.toString());
        assertSame(DateTimeComparator.getTimeOnlyInstance(), DateTimeComparator.getTimeOnlyInstance());
    }

    public void testStaticGetInstanceLower() {
        DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay());
        assertEquals(DateTimeFieldType.hourOfDay(), comparator.getLowerLimit());
        assertNull(comparator.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay-]", comparator.toString());

        comparator = DateTimeComparator.getInstance(null);
        assertSame(DateTimeComparator.getInstance(), comparator);
    }

    public void testStaticGetInstanceLowerUpper() {
        DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        assertEquals(DateTimeFieldType.hourOfDay(), comparator.getLowerLimit());
        assertEquals(DateTimeFieldType.dayOfYear(), comparator.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay-dayOfYear]", comparator.toString());

        comparator = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.hourOfDay());
        assertEquals(DateTimeFieldType.hourOfDay(), comparator.getLowerLimit());
        assertEquals(DateTimeFieldType.hourOfDay(), comparator.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay]", comparator.toString());

        comparator = DateTimeComparator.getInstance(null, null);
        assertSame(DateTimeComparator.getInstance(), comparator);

        comparator = DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), null);
        assertSame(DateTimeComparator.getDateOnlyInstance(), comparator);

        comparator = DateTimeComparator.getInstance(null, DateTimeFieldType.dayOfYear());
        assertSame(DateTimeComparator.getTimeOnlyInstance(), comparator);
    }

    public void testNullNowCheckedOnce() {
        for (int i = 0; i < 10000; i++) {
            assertEquals(0, DateTimeComparator.getInstance().compare(null, null));
        }
    }

    public void testEqualsHashCode() {
        DateTimeComparator comparator1 = DateTimeComparator.getInstance();
        assertTrue(comparator1.equals(comparator1));
        assertFalse(comparator1.equals(null));
        assertEquals(comparator1.hashCode(), comparator1.hashCode());

        DateTimeComparator comparator2 = DateTimeComparator.getTimeOnlyInstance();
        assertTrue(comparator2.equals(comparator2));
        assertFalse(comparator2.equals(comparator1));
        assertFalse(comparator1.equals(comparator2));
        assertFalse(comparator2.equals(null));
        assertNotEquals(comparator1.hashCode(), comparator2.hashCode());

        DateTimeComparator comparator3 = DateTimeComparator.getTimeOnlyInstance();
        assertTrue(comparator3.equals(comparator3));
        assertFalse(comparator3.equals(comparator1));
        assertTrue(comparator3.equals(comparator2));
        assertFalse(comparator1.equals(comparator3));
        assertTrue(comparator2.equals(comparator3));
        assertNotEquals(comparator1.hashCode(), comparator3.hashCode());
        assertEquals(comparator2.hashCode(), comparator3.hashCode());

        DateTimeComparator comparator4 = DateTimeComparator.getDateOnlyInstance();
        assertNotEquals(comparator4.hashCode(), comparator3.hashCode());
    }

    public void testSerialization() throws Exception {
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

    public void testSerializationSingleton() throws Exception {
        DateTimeComparator comparator = DateTimeComparator.getInstance();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(comparator);
        oos.close();
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DateTimeComparator result = (DateTimeComparator) ois.readObject();
        ois.close();

        assertSame(comparator, result);
    }

    public void testBasicComparisons() {
        aDateTime = new DateTime(System.currentTimeMillis(), DateTimeZone.UTC);
        bDateTime = new DateTime(aDateTime.getMillis(), DateTimeZone.UTC);

        assertEquals("MILLIS", 0, cMillis.compare(aDateTime, bDateTime));
        assertEquals("SECOND", 0, cSecond.compare(aDateTime, bDateTime));
        assertEquals("MINUTE", 0, cMinute.compare(aDateTime, bDateTime));
        assertEquals("HOUR", 0, cHour.compare(aDateTime, bDateTime));
        assertEquals("DOW", 0, cDayOfWeek.compare(aDateTime, bDateTime));
        assertEquals("DOM", 0, cDayOfMonth.compare(aDateTime, bDateTime));
        assertEquals("DOY", 0, cDayOfYear.compare(aDateTime, bDateTime));
        assertEquals("WOW", 0, cWeekOfWeekyear.compare(aDateTime, bDateTime));
        assertEquals("WY", 0, cWeekyear.compare(aDateTime, bDateTime));
        assertEquals("MONTH", 0, cMonth.compare(aDateTime, bDateTime));
        assertEquals("YEAR", 0, cYear.compare(aDateTime, bDateTime));
        assertEquals("DATE", 0, cDate.compare(aDateTime, bDateTime));
        assertEquals("TIME", 0, cTime.compare(aDateTime, bDateTime));
    }

    public void testMillisComparison() {
        aDateTime = new DateTime(System.currentTimeMillis(), DateTimeZone.UTC);
        bDateTime = new DateTime(aDateTime.getMillis() + 1, DateTimeZone.UTC);
        assertEquals("MillisM1", -1, cMillis.compare(aDateTime, bDateTime));
        assertEquals("MillisP1", 1, cMillis.compare(bDateTime, aDateTime));
    }

    public void testSecondComparison() {
        aDateTime = getDateTime("1969-12-31T23:59:58");
        bDateTime = getDateTime("1969-12-31T23:50:59");
        assertEquals("SecondM1", -1, cSecond.compare(aDateTime, bDateTime));
        assertEquals("SecondP1", 1, cSecond.compare(bDateTime, aDateTime));

        aDateTime = getDateTime("1970-01-01T00:00:00");
        bDateTime = getDateTime("1970-01-01T00:00:01");
        assertEquals("SecondM1", -1, cSecond.compare(aDateTime, bDateTime));
        assertEquals("SecondP1", 1, cSecond.compare(bDateTime, aDateTime));
    }

    public void testMinuteComparison() {
        aDateTime = getDateTime("1969-12-31T23:58:00");
        bDateTime = getDateTime("1969-12-31T23:59:00");
        assertEquals("MinuteM1", -1, cMinute.compare(aDateTime, bDateTime));
        assertEquals("MinuteP1", 1, cMinute.compare(bDateTime, aDateTime));

        aDateTime = getDateTime("1970-01-01T00:00:00");
        bDateTime = getDateTime("1970-01-01T00:01:00");
        assertEquals("MinuteM1", -1, cMinute.compare(aDateTime, bDateTime));
        assertEquals("MinuteP1", 1, cMinute.compare(bDateTime, aDateTime));
    }

    public void testHourComparison() {
        aDateTime = getDateTime("1969-12-31T22:00:00");
        bDateTime = getDateTime("1969-12-31T23:00:00");
        assertEquals("HourM1", -1, cHour.compare(aDateTime, bDateTime));
        assertEquals("HourP1", 1, cHour.compare(bDateTime, aDateTime));

        aDateTime = getDateTime("1970-01-01T00:00:00");
        bDateTime = getDateTime("1970-01-01T01:00:00");
        assertEquals("HourM1", -1, cHour.compare(aDateTime, bDateTime));
        assertEquals("HourP1", 1, cHour.compare(bDateTime, aDateTime));

        aDateTime = getDateTime("1969-12-31T23:59:59");
        bDateTime = getDateTime("1970-01-01T00:00:00");
        assertEquals("HourP1", 1, cHour.compare(aDateTime, bDateTime));
        assertEquals("HourM1", -1, cHour.compare(bDateTime, aDateTime));
    }

    public void testDayOfWeekComparison() {
        aDateTime = getDateTime("2002-04-12T00:00:00");
        bDateTime = getDateTime("2002-04-13T00:00:00");
        assertEquals("DOWM1", -1, cDayOfWeek.compare(aDateTime, bDateTime));
        assertEquals("DOWP1", 1, cDayOfWeek.compare(bDateTime, aDateTime));
    }

    public void testDayOfMonthComparison() {
        aDateTime = getDateTime("2002-04-12T00:00:00");
        bDateTime = getDateTime("2002-04-13T00:00:00");
        assertEquals("DOMM1", -1, cDayOfMonth.compare(aDateTime, bDateTime));
        assertEquals("DOMP1", 1, cDayOfMonth.compare(bDateTime, aDateTime));

        aDateTime = getDateTime("2000-12-01T00:00:00");
        bDateTime = getDateTime("1814-04-30T00:00:00");
        assertEquals("DOMM1", -1, cDayOfMonth.compare(aDateTime, bDateTime));
        assertEquals("DOMP1", 1, cDayOfMonth.compare(bDateTime, aDateTime));
    }

    public void testDayOfYearComparison() {
        aDateTime = getDateTime("2002-04-12T00:00:00");
        bDateTime = getDateTime("2002-04-13T00:00:00");
        assertEquals("DOYM1", -1, cDayOfYear.compare(aDateTime, bDateTime));
        assertEquals("DOYP1", 1, cDayOfYear.compare(bDateTime, aDateTime));

        aDateTime = getDateTime("2000-02-29T00:00:00");
        bDateTime = getDateTime("1814-11-30T00:00:00");
        assertEquals("DOYM1", -1, cDayOfYear.compare(aDateTime, bDateTime));
        assertEquals("DOYP1", 1, cDayOfYear.compare(bDateTime, aDateTime));
    }

    public void testWeekOfWeekyearComparison() {
        aDateTime = getDateTime("2000-01-04T00:00:00");
        bDateTime = getDateTime("2000-01-11T00:00:00");
        assertEquals("WOWM1", -1, cWeekOfWeekyear.compare(aDateTime, bDateTime));
        assertEquals("WOWP1", 1, cWeekOfWeekyear.compare(bDateTime, aDateTime));

        aDateTime = getDateTime("2000-01-04T00:00:00");
        bDateTime = getDateTime("1999-12-31T00:00:00");
        assertEquals("WOWM1", -1, cWeekOfWeekyear.compare(aDateTime, bDateTime));
        assertEquals("WOWP1", 1, cWeekOfWeekyear.compare(bDateTime, aDateTime));
    }

    public void testWeekyearComparison() {
        aDateTime = getDateTime("1998-12-31T23:59:59");
        bDateTime = getDateTime("1999-01-01T00:00:00");
        assertEquals("YOYYZ", 0, cWeekyear.compare(aDateTime, bDateTime));

        bDateTime = getDateTime("1999-01-04T00:00:00");
        assertEquals("YOYYM1", -1, cWeekyear.compare(aDateTime, bDateTime));
        assertEquals("YOYYP1", 1, cWeekyear.compare(bDateTime, aDateTime));
    }

    public void testMonthComparison() {
        aDateTime = getDateTime("2002-04-30T00:00:00");
        bDateTime = getDateTime("2002-05-01T00:00:00");
        assertEquals("MONTHM1", -1, cMonth.compare(aDateTime, bDateTime));
        assertEquals("MONTHP1", 1, cMonth.compare(bDateTime, aDateTime));

        aDateTime = getDateTime("1900-01-01T00:00:00");
        bDateTime = getDateTime("1899-12-31T00:00:00");
        assertEquals("MONTHM1", -1, cMonth.compare(aDateTime, bDateTime));
        assertEquals("MONTHP1", 1, cMonth.compare(bDateTime, aDateTime));
    }

    public void testYearComparison() {
        aDateTime = getDateTime("2000-01-01T00:00:00");
        bDateTime = getDateTime("2001-01-01T00:00:00");
        assertEquals("YEARM1", -1, cYear.compare(aDateTime, bDateTime));
        assertEquals("YEARP1", 1, cYear.compare(bDateTime, aDateTime));

        aDateTime = getDateTime("1968-12-31T23:59:59");
        bDateTime = getDateTime("1970-01-01T00:00:00");
        assertEquals("YEARM1", -1, cYear.compare(aDateTime, bDateTime));
        assertEquals("YEARP1", 1, cYear.compare(bDateTime, aDateTime));

        aDateTime = getDateTime("1969-12-31T23:59:59");
        bDateTime = getDateTime("1970-01-01T00:00:00");
        assertEquals("YEARM1", -1, cYear.compare(aDateTime, bDateTime));
        assertEquals("YEARP1", 1, cYear.compare(bDateTime, aDateTime));
    }

    public void testListSorting() {
        String[] dateStrings = {
            "1999-02-01T00:00:00",
            "1998-01-20T00:00:00"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSorting", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingMillis() {
        List<DateTime> dateList = new ArrayList<>();
        long base = 12345L * 1000L;
        dateList.add(new DateTime(base + 999L, DateTimeZone.UTC));
        dateList.add(new DateTime(base + 222L, DateTimeZone.UTC));
        dateList.add(new DateTime(base + 456L, DateTimeZone.UTC));
        dateList.add(new DateTime(base + 888L, DateTimeZone.UTC));
        dateList.add(new DateTime(base + 123L, DateTimeZone.UTC));
        dateList.add(new DateTime(base, DateTimeZone.UTC));
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cMillis);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingMillis", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingSecond() {
        String[] dateStrings = {
            "1999-02-01T00:00:10",
            "1999-02-01T00:00:30",
            "1999-02-01T00:00:25",
            "1999-02-01T00:00:18",
            "1999-02-01T00:00:01",
            "1999-02-01T00:00:59",
            "1999-02-01T00:00:22"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cSecond);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingSecond", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingMinute() {
        String[] dateStrings = {
            "1999-02-01T00:10:00",
            "1999-02-01T00:30:00",
            "1999-02-01T00:25:00",
            "1999-02-01T00:18:00",
            "1999-02-01T00:01:00",
            "1999-02-01T00:59:00",
            "1999-02-01T00:22:00"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cMinute);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingMinute", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingHour() {
        String[] dateStrings = {
            "1999-02-01T10:00:00",
            "1999-02-01T23:00:00",
            "1999-02-01T01:00:00",
            "1999-02-01T15:00:00",
            "1999-02-01T05:00:00",
            "1999-02-01T20:00:00",
            "1999-02-01T17:00:00"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cHour);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingHour", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingDayOfWeek() {
        String[] dateStrings = {
            "2002-04-21T10:00:00",
            "2002-04-16T10:00:00",
            "2002-04-15T10:00:00",
            "2002-04-17T10:00:00",
            "2002-04-19T10:00:00",
            "2002-04-18T10:00:00",
            "2002-04-20T10:00:00"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cDayOfWeek);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingDayOfWeek", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingDayOfMonth() {
        String[] dateStrings = {
            "2002-04-20T10:00:00",
            "2002-04-16T10:00:00",
            "2002-04-15T10:00:00",
            "2002-04-17T10:00:00",
            "2002-04-19T10:00:00",
            "2002-04-18T10:00:00",
            "2002-04-14T10:00:00"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cDayOfMonth);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingDayOfMonth", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingDayOfYear() {
        String[] dateStrings = {
            "2002-04-20T10:00:00",
            "2002-01-16T10:00:00",
            "2002-12-31T10:00:00",
            "2002-09-14T10:00:00",
            "2002-09-19T10:00:00",
            "2002-02-14T10:00:00",
            "2002-10-30T10:00:00"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cDayOfYear);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingDayOfYear", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingWeekOfWeekyear() {
        String[] dateStrings = {
            "2002-04-01T10:00:00",
            "2002-01-01T10:00:00",
            "2002-12-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-02-01T10:00:00",
            "2002-10-01T10:00:00"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cWeekOfWeekyear);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingWeekOfWeekyear", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingWeekyear() {
        String[] dateStrings = {
            "2010-04-01T10:00:00",
            "2002-01-01T10:00:00"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cWeekyear);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingWeekyear", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingMonth() {
        String[] dateStrings = {
            "2002-04-01T10:00:00",
            "2002-01-01T10:00:00",
            "2002-12-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-02-01T10:00:00",
            "2002-10-01T10:00:00"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cMonth);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingMonth", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingYear() {
        String[] dateStrings = {
            "1999-02-01T00:00:00",
            "1998-02-01T00:00:00",
            "2525-02-01T00:00:00",
            "1776-02-01T00:00:00",
            "1863-02-01T00:00:00",
            "1066-02-01T00:00:00",
            "2100-02-01T00:00:00"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cYear);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingYear", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingDate() {
        String[] dateStrings = {
            "1999-02-01T00:00:00",
            "1998-10-03T00:00:00",
            "2525-05-20T00:00:00",
            "1776-12-25T00:00:00",
            "1863-01-31T00:00:00",
            "1066-09-22T00:00:00",
            "2100-07-04T00:00:00"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cDate);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingDate", !isSortedBefore, isSortedAfter);
    }

    public void testListSortingTime() {
        String[] dateStrings = {
            "1999-02-01T01:02:05",
            "1999-02-01T22:22:22",
            "1999-02-01T05:30:45",
            "1999-02-01T09:17:59",
            "1999-02-01T09:17:58",
            "1999-02-01T15:30:00",
            "1999-02-01T17:00:44"
        };
        List<DateTime> dateList = loadDateTimeList(dateStrings);
        boolean isSortedBefore = isListSorted(dateList);
        Collections.sort(dateList, cTime);
        boolean isSortedAfter = isListSorted(dateList);
        assertEquals("ListSortingTime", !isSortedBefore, isSortedAfter);
    }

    public void testNullDateTimeComparison() {
        aDateTime = getDateTime("2000-01-01T00:00:00");
        assertTrue(cYear.compare(null, aDateTime) > 0);
        assertTrue(cYear.compare(aDateTime, null) < 0);
    }

    public void testInvalidObjectComparison() {
        aDateTime = getDateTime("2000-01-01T00:00:00");
        try {
            cYear.compare("InvalidObject", aDateTime);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected exception
        }
    }

    private DateTime getDateTime(String dateTimeString) {
        return new DateTime(dateTimeString, DateTimeZone.UTC);
    }

    private List<DateTime> loadDateTimeList(String[] dateTimeStrings) {
        List<DateTime> dateTimeList = new ArrayList<>();
        for (String dateTimeString : dateTimeStrings) {
            dateTimeList.add(new DateTime(dateTimeString, DateTimeZone.UTC));
        }
        return dateTimeList;
    }

    private boolean isListSorted(List<DateTime> dateTimeList) {
        for (int i = 1; i < dateTimeList.size(); i++) {
            if (dateTimeList.get(i - 1).isAfter(dateTimeList.get(i))) {
                return false;
            }
        }
        return true;
    }
}