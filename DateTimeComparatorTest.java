package org.joda.time;

import org.joda.time.chrono.ISOChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for DateTimeComparator focusing on:
 * - factory methods and singletons
 * - equality and hashCode
 * - serialization behavior (including singleton preservation)
 * - comparisons across different input types (DateTime, ReadableInstant, Date, Long, Calendar)
 * - comparisons constrained by different field limits
 * - sorting lists using specific comparators
 * - behavior with null inputs and invalid types
 *
 * Tests use UTC for determinism and fix the system clock where relevant.
 */
public class TestDateTimeComparator {

    // Use UTC consistently for better determinism
    private static final DateTimeZone UTC = DateTimeZone.UTC;

    // Fixed "now" used where tests rely on the current time
    private static final long FIXED_NOW_MILLIS = new DateTime("2001-01-01T00:00:00", UTC).getMillis();

    // Commonly used comparators (keep as fields for readability)
    private final Comparator<Object> cmpAll = DateTimeComparator.getInstance();
    private final Comparator<Object> cmpMillis = DateTimeComparator.getInstance(null, DateTimeFieldType.secondOfMinute());
    private final Comparator<Object> cmpSecond = DateTimeComparator.getInstance(DateTimeFieldType.secondOfMinute(), DateTimeFieldType.minuteOfHour());
    private final Comparator<Object> cmpMinute = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfHour(), DateTimeFieldType.hourOfDay());
    private final Comparator<Object> cmpHour = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
    private final Comparator<Object> cmpDayOfWeek = DateTimeComparator.getInstance(DateTimeFieldType.dayOfWeek(), DateTimeFieldType.weekOfWeekyear());
    private final Comparator<Object> cmpDayOfMonth = DateTimeComparator.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.monthOfYear());
    private final Comparator<Object> cmpDayOfYear = DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), DateTimeFieldType.year());
    private final Comparator<Object> cmpWeekOfWeekyear = DateTimeComparator.getInstance(DateTimeFieldType.weekOfWeekyear(), DateTimeFieldType.weekyear());
    private final Comparator<Object> cmpWeekyear = DateTimeComparator.getInstance(DateTimeFieldType.weekyear());
    private final Comparator<Object> cmpMonth = DateTimeComparator.getInstance(DateTimeFieldType.monthOfYear(), DateTimeFieldType.year());
    private final Comparator<Object> cmpYear = DateTimeComparator.getInstance(DateTimeFieldType.year());
    private final Comparator<Object> cmpDate = DateTimeComparator.getDateOnlyInstance();
    private final Comparator<Object> cmpTime = DateTimeComparator.getTimeOnlyInstance();

    @Before
    public void freezeClock() {
        // Freeze "now" for tests that depend on null -> now
        DateTimeUtils.setCurrentMillisFixed(FIXED_NOW_MILLIS);
    }

    @After
    public void unfreezeClock() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    // ---------------------------------------------------------------------
    // Class structure

    @Test
    public void classModifiers_and_constructorVisibility() {
        assertTrue(Modifier.isPublic(DateTimeComparator.class.getModifiers()));
        assertFalse(Modifier.isFinal(DateTimeComparator.class.getModifiers()));
        assertEquals(1, DateTimeComparator.class.getDeclaredConstructors().length);
        assertTrue(Modifier.isProtected(DateTimeComparator.class.getDeclaredConstructors()[0].getModifiers()));
    }

    // ---------------------------------------------------------------------
    // Factory methods

    @Test
    public void getInstance_default_allFields() {
        DateTimeComparator c = DateTimeComparator.getInstance();
        assertNull(c.getLowerLimit());
        assertNull(c.getUpperLimit());
        assertEquals("DateTimeComparator[]", c.toString());
    }

    @Test
    public void getDateOnlyInstance_singleton_and_description() {
        DateTimeComparator c = DateTimeComparator.getDateOnlyInstance();
        assertEquals(DateTimeFieldType.dayOfYear(), c.getLowerLimit());
        assertNull(c.getUpperLimit());
        assertEquals("DateTimeComparator[dayOfYear-]", c.toString());

        assertSame(DateTimeComparator.getDateOnlyInstance(), DateTimeComparator.getDateOnlyInstance());
    }

    @Test
    public void getTimeOnlyInstance_singleton_and_description() {
        DateTimeComparator c = DateTimeComparator.getTimeOnlyInstance();
        assertNull(c.getLowerLimit());
        assertEquals(DateTimeFieldType.dayOfYear(), c.getUpperLimit());
        assertEquals("DateTimeComparator[-dayOfYear]", c.toString());

        assertSame(DateTimeComparator.getTimeOnlyInstance(), DateTimeComparator.getTimeOnlyInstance());
    }

    @Test
    public void getInstance_withLowerLimit() {
        DateTimeComparator c = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getLowerLimit());
        assertNull(c.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay-]", c.toString());

        assertSame(DateTimeComparator.getInstance(), DateTimeComparator.getInstance(null));
    }

    @Test
    public void getInstance_withLowerAndUpperLimits_and_commonShortcuts() {
        DateTimeComparator c = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getLowerLimit());
        assertEquals(DateTimeFieldType.dayOfYear(), c.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay-dayOfYear]", c.toString());

        c = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.hourOfDay());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getLowerLimit());
        assertEquals(DateTimeFieldType.hourOfDay(), c.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay]", c.toString());

        assertSame(DateTimeComparator.getInstance(), DateTimeComparator.getInstance(null, null));
        assertSame(DateTimeComparator.getDateOnlyInstance(), DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), null));
        assertSame(DateTimeComparator.getTimeOnlyInstance(), DateTimeComparator.getInstance(null, DateTimeFieldType.dayOfYear()));
    }

    @Test
    public void nullInputs_compareToNow_isConsistent_evenAcrossManyCalls() {
        // Guard against race conditions (issue #404)
        for (int i = 0; i < 10_000; i++) {
            assertEquals(0, DateTimeComparator.getInstance().compare(null, null));
        }
    }

    // ---------------------------------------------------------------------
    // Equality / hashCode

    @Test
    public void equals_and_hashCode() {
        DateTimeComparator c1 = DateTimeComparator.getInstance();
        assertTrue(c1.equals(c1));
        assertFalse(c1.equals(null));
        assertEquals(c1.hashCode(), c1.hashCode());

        DateTimeComparator c2 = DateTimeComparator.getTimeOnlyInstance();
        assertTrue(c2.equals(c2));
        assertFalse(c2.equals(c1));
        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(null));
        assertNotEquals(c1.hashCode(), c2.hashCode());

        DateTimeComparator c3 = DateTimeComparator.getTimeOnlyInstance();
        assertTrue(c3.equals(c3));
        assertFalse(c3.equals(c1));
        assertTrue(c3.equals(c2));
        assertFalse(c1.equals(c3));
        assertTrue(c2.equals(c3));
        assertNotEquals(c1.hashCode(), c3.hashCode());
        assertEquals(c2.hashCode(), c3.hashCode());

        DateTimeComparator c4 = DateTimeComparator.getDateOnlyInstance();
        assertNotEquals(c4.hashCode(), c3.hashCode());
    }

    // ---------------------------------------------------------------------
    // Serialization

    @Test
    public void serialization_roundTrip_withLimits_preservesEquality() throws Exception {
        DateTimeComparator original = DateTimeComparator.getInstance(DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());

        byte[] bytes = serialize(original);
        DateTimeComparator result = deserialize(bytes);

        assertEquals(original, result);
    }

    @Test
    public void serialization_ofDefaultInstance_preservesSingleton() throws Exception {
        DateTimeComparator original = DateTimeComparator.getInstance();

        byte[] bytes = serialize(original);
        DateTimeComparator result = deserialize(bytes);

        assertSame(original, result);
    }

    // ---------------------------------------------------------------------
    // Equal comparisons across various input types

    @Test
    public void equalComparisons_DateTime() {
        DateTime a = dt("2020-06-01T12:34:56.789");
        DateTime b = new DateTime(a.getMillis(), UTC);

        assertAllComparatorsReturnZero(a, b);
    }

    @Test
    public void equalComparisons_ReadableInstant() {
        ReadableInstant a = dt("2020-06-01T12:34:56.789");
        ReadableInstant b = new DateTime(a.getMillis(), UTC);

        assertAllComparatorsReturnZero(a, b);
    }

    @Test
    public void equalComparisons_Date() {
        Date a = dt("2020-06-01T12:34:56.789").toDate();
        Date b = new Date(a.getTime());

        assertAllComparatorsReturnZero(a, b);
    }

    @Test
    public void equalComparisons_LongMillis() {
        Long a = dt("2020-06-01T12:34:56.789").getMillis();
        Long b = Long.valueOf(a);

        assertAllComparatorsReturnZero(a, b);
    }

    @Test
    public void equalComparisons_Calendar() {
        Calendar a = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        a.setTime(dt("2020-06-01T12:34:56.789").toDate());
        Calendar b = (Calendar) a.clone();

        assertAllComparatorsReturnZero(a, b);
    }

    // ---------------------------------------------------------------------
    // Inequality comparisons per field limit

    @Test
    public void compareBy_millisOfSecond() {
        DateTime a = dt("2020-06-01T12:34:56.000");
        DateTime b = dt("2020-06-01T12:34:56.001");

        assertEquals(-1, cmpMillis.compare(a, b));
        assertEquals(1, cmpMillis.compare(b, a));
    }

    @Test
    public void compareBy_secondOfMinute() {
        DateTime a = dt("1969-12-31T23:59:58");
        DateTime b = dt("1969-12-31T23:59:59");
        assertEquals(-1, cmpSecond.compare(a, b));
        assertEquals(1, cmpSecond.compare(b, a));

        a = dt("1970-01-01T00:00:00");
        b = dt("1970-01-01T00:00:01");
        assertEquals(-1, cmpSecond.compare(a, b));
        assertEquals(1, cmpSecond.compare(b, a));
    }

    @Test
    public void compareBy_minuteOfHour() {
        DateTime a = dt("1969-12-31T23:58:00");
        DateTime b = dt("1969-12-31T23:59:00");
        assertEquals(-1, cmpMinute.compare(a, b));
        assertEquals(1, cmpMinute.compare(b, a));

        a = dt("1970-01-01T00:00:00");
        b = dt("1970-01-01T00:01:00");
        assertEquals(-1, cmpMinute.compare(a, b));
        assertEquals(1, cmpMinute.compare(b, a));
    }

    @Test
    public void compareBy_hourOfDay() {
        DateTime a = dt("1969-12-31T22:00:00");
        DateTime b = dt("1969-12-31T23:00:00");
        assertEquals(-1, cmpHour.compare(a, b));
        assertEquals(1, cmpHour.compare(b, a));

        a = dt("1970-01-01T00:00:00");
        b = dt("1970-01-01T01:00:00");
        assertEquals(-1, cmpHour.compare(a, b));
        assertEquals(1, cmpHour.compare(b, a));

        a = dt("1969-12-31T23:59:59");
        b = dt("1970-01-01T00:00:00");
        assertEquals(1, cmpHour.compare(a, b));
        assertEquals(-1, cmpHour.compare(b, a));
    }

    @Test
    public void compareBy_dayOfWeek() {
        DateTime a = dt("2002-04-12T00:00:00"); // Friday
        DateTime b = dt("2002-04-13T00:00:00"); // Saturday
        assertEquals(-1, cmpDayOfWeek.compare(a, b));
        assertEquals(1, cmpDayOfWeek.compare(b, a));
    }

    @Test
    public void compareBy_dayOfMonth() {
        DateTime a = dt("2002-04-12T00:00:00");
        DateTime b = dt("2002-04-13T00:00:00");
        assertEquals(-1, cmpDayOfMonth.compare(a, b));
        assertEquals(1, cmpDayOfMonth.compare(b, a));

        a = dt("2000-12-01T00:00:00");
        b = dt("1814-04-30T00:00:00");
        assertEquals(-1, cmpDayOfMonth.compare(a, b));
        assertEquals(1, cmpDayOfMonth.compare(b, a));
    }

    @Test
    public void compareBy_dayOfYear() {
        DateTime a = dt("2002-04-12T00:00:00");
        DateTime b = dt("2002-04-13T00:00:00");
        assertEquals(-1, cmpDayOfYear.compare(a, b));
        assertEquals(1, cmpDayOfYear.compare(b, a));

        a = dt("2000-02-29T00:00:00");
        b = dt("1814-11-30T00:00:00");
        assertEquals(-1, cmpDayOfYear.compare(a, b));
        assertEquals(1, cmpDayOfYear.compare(b, a));
    }

    @Test
    public void compareBy_weekOfWeekyear() {
        // First week contains Jan 4
        DateTime a = dt("2000-01-04T00:00:00");
        DateTime b = dt("2000-01-11T00:00:00");
        assertEquals(-1, cmpWeekOfWeekyear.compare(a, b));
        assertEquals(1, cmpWeekOfWeekyear.compare(b, a));

        a = dt("2000-01-04T00:00:00");
        b = dt("1999-12-31T00:00:00");
        assertEquals(-1, cmpWeekOfWeekyear.compare(a, b));
        assertEquals(1, cmpWeekOfWeekyear.compare(b, a));
    }

    @Test
    public void compareBy_weekyear() {
        DateTime end1998 = dt("1998-12-31T23:59:59");
        DateTime start1999 = dt("1999-01-01T00:00:00");
        assertEquals(0, cmpWeekyear.compare(end1998, start1999));

        DateTime jan4_1999 = dt("1999-01-04T00:00:00");
        assertEquals(-1, cmpWeekyear.compare(end1998, jan4_1999));
        assertEquals(1, cmpWeekyear.compare(jan4_1999, end1998));
    }

    @Test
    public void compareBy_monthOfYear() {
        DateTime a = dt("2002-04-30T00:00:00");
        DateTime b = dt("2002-05-01T00:00:00");
        assertEquals(-1, cmpMonth.compare(a, b));
        assertEquals(1, cmpMonth.compare(b, a));

        a = dt("1900-01-01T00:00:00");
        b = dt("1899-12-31T00:00:00");
        assertEquals(-1, cmpMonth.compare(a, b));
        assertEquals(1, cmpMonth.compare(b, a));
    }

    @Test
    public void compareBy_year() {
        DateTime a = dt("2000-01-01T00:00:00");
        DateTime b = dt("2001-01-01T00:00:00");
        assertEquals(-1, cmpYear.compare(a, b));
        assertEquals(1, cmpYear.compare(b, a));

        a = dt("1968-12-31T23:59:59");
        b = dt("1970-01-01T00:00:00");
        assertEquals(-1, cmpYear.compare(a, b));
        assertEquals(1, cmpYear.compare(b, a));

        a = dt("1969-12-31T23:59:59");
        b = dt("1970-01-01T00:00:00");
        assertEquals(-1, cmpYear.compare(a, b));
        assertEquals(1, cmpYear.compare(b, a));
    }

    // ---------------------------------------------------------------------
    // Sorting tests (Each dataset is crafted so that sorting by the given comparator
    // also results in ascending millis order; we verify the result by millis)

    @Test
    public void sort_defaultComparator_fullOrder() {
        List<DateTime> list = loadAsDateTimes(
            "1999-02-01T00:00:00",
            "1998-01-20T00:00:00"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort(list); // DateTime implements Comparable by millis
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_byMillisOfSecond() {
        // All in the same second; millis differ
        List<DateTime> list = new ArrayList<>();
        long base = new DateTime("2020-01-01T00:00:10.000", UTC).getMillis();
        list.add(new DateTime(base + 999, UTC));
        list.add(new DateTime(base + 222, UTC));
        list.add(new DateTime(base + 456, UTC));
        list.add(new DateTime(base + 888, UTC));
        list.add(new DateTime(base + 123, UTC));
        list.add(new DateTime(base + 0, UTC));

        assertFalse(isSortedByMillis(list));
        // raw types to match Comparator<Object> signature
        Collections.sort((List) list, cmpMillis);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_bySecondOfMinute() {
        List<DateTime> list = loadAsDateTimes(
            "1999-02-01T00:00:10",
            "1999-02-01T00:00:30",
            "1999-02-01T00:00:25",
            "1999-02-01T00:00:18",
            "1999-02-01T00:00:01",
            "1999-02-01T00:00:59",
            "1999-02-01T00:00:22"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpSecond);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_byMinuteOfHour() {
        List<DateTime> list = loadAsDateTimes(
            "1999-02-01T00:10:00",
            "1999-02-01T00:30:00",
            "1999-02-01T00:25:00",
            "1999-02-01T00:18:00",
            "1999-02-01T00:01:00",
            "1999-02-01T00:59:00",
            "1999-02-01T00:22:00"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpMinute);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_byHourOfDay() {
        List<DateTime> list = loadAsDateTimes(
            "1999-02-01T10:00:00",
            "1999-02-01T23:00:00",
            "1999-02-01T01:00:00",
            "1999-02-01T15:00:00",
            "1999-02-01T05:00:00",
            "1999-02-01T20:00:00",
            "1999-02-01T17:00:00"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpHour);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_byDayOfWeek() {
        List<DateTime> list = loadAsDateTimes(
            // base week of 2002-04-15 (Monday)
            "2002-04-21T10:00:00",
            "2002-04-16T10:00:00",
            "2002-04-15T10:00:00",
            "2002-04-17T10:00:00",
            "2002-04-19T10:00:00",
            "2002-04-18T10:00:00",
            "2002-04-20T10:00:00"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpDayOfWeek);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_byDayOfMonth() {
        List<DateTime> list = loadAsDateTimes(
            "2002-04-20T10:00:00",
            "2002-04-16T10:00:00",
            "2002-04-15T10:00:00",
            "2002-04-17T10:00:00",
            "2002-04-19T10:00:00",
            "2002-04-18T10:00:00",
            "2002-04-14T10:00:00"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpDayOfMonth);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_byDayOfYear() {
        List<DateTime> list = loadAsDateTimes(
            "2002-04-20T10:00:00",
            "2002-01-16T10:00:00",
            "2002-12-31T10:00:00",
            "2002-09-14T10:00:00",
            "2002-09-19T10:00:00",
            "2002-02-14T10:00:00",
            "2002-10-30T10:00:00"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpDayOfYear);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_byWeekOfWeekyear() {
        List<DateTime> list = loadAsDateTimes(
            "2002-04-01T10:00:00",
            "2002-01-01T10:00:00",
            "2002-12-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-02-01T10:00:00",
            "2002-10-01T10:00:00"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpWeekOfWeekyear);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_byWeekyear() {
        List<DateTime> list = loadAsDateTimes(
            "2010-04-01T10:00:00",
            "2002-01-01T10:00:00"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpWeekyear);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_byMonthOfYear() {
        List<DateTime> list = loadAsDateTimes(
            "2002-04-01T10:00:00",
            "2002-01-01T10:00:00",
            "2002-12-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-09-01T10:00:00",
            "2002-02-01T10:00:00",
            "2002-10-01T10:00:00"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpMonth);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_byYear() {
        List<DateTime> list = loadAsDateTimes(
            "1999-02-01T00:00:00",
            "1998-02-01T00:00:00",
            "2525-02-01T00:00:00",
            "1776-02-01T00:00:00",
            "1863-02-01T00:00:00",
            "1066-02-01T00:00:00",
            "2100-02-01T00:00:00"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpYear);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_dateOnly() {
        List<DateTime> list = loadAsDateTimes(
            "1999-02-01T00:00:00",
            "1998-10-03T00:00:00",
            "2525-05-20T00:00:00",
            "1776-12-25T00:00:00",
            "1863-01-31T00:00:00",
            "1066-09-22T00:00:00",
            "2100-07-04T00:00:00"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpDate);
        assertTrue(isSortedByMillis(list));
    }

    @Test
    public void sort_timeOnly() {
        List<DateTime> list = loadAsDateTimes(
            "1999-02-01T01:02:05",
            "1999-02-01T22:22:22",
            "1999-02-01T05:30:45",
            "1999-02-01T09:17:59",
            "1999-02-01T09:17:58",
            "1999-02-01T15:30:00",
            "1999-02-01T17:00:44"
        );

        assertFalse(isSortedByMillis(list));
        Collections.sort((List) list, cmpTime);
        assertTrue(isSortedByMillis(list));
    }

    // ---------------------------------------------------------------------
    // Nulls and invalid types

    @Test
    public void nullMeansNow_inComparisons() {
        // FIXED_NOW_MILLIS = 2001-01-01
        DateTime earlier = dt("2000-01-01T00:00:00");

        assertTrue(cmpYear.compare(null, earlier) > 0);  // now (2001) > 2000
        assertTrue(cmpYear.compare(earlier, null) < 0);  // 2000 < now (2001)
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidObjectType_throws() {
        cmpYear.compare("Not a date", dt("2000-01-01T00:00:00"));
    }

    // ---------------------------------------------------------------------
    // Helpers

    private static DateTime dt(String isoUtc) {
        return new DateTime(isoUtc, UTC);
    }

    private static byte[] serialize(DateTimeComparator comparator) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(comparator);
            oos.flush();
            return baos.toByteArray();
        }
    }

    private static DateTimeComparator deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (DateTimeComparator) ois.readObject();
        }
    }

    private void assertAllComparatorsReturnZero(Object a, Object b) {
        assertEquals(0, cmpMillis.compare(a, b));
        assertEquals(0, cmpSecond.compare(a, b));
        assertEquals(0, cmpMinute.compare(a, b));
        assertEquals(0, cmpHour.compare(a, b));
        assertEquals(0, cmpDayOfWeek.compare(a, b));
        assertEquals(0, cmpDayOfMonth.compare(a, b));
        assertEquals(0, cmpDayOfYear.compare(a, b));
        assertEquals(0, cmpWeekOfWeekyear.compare(a, b));
        assertEquals(0, cmpWeekyear.compare(a, b));
        assertEquals(0, cmpMonth.compare(a, b));
        assertEquals(0, cmpYear.compare(a, b));
        assertEquals(0, cmpDate.compare(a, b));
        assertEquals(0, cmpTime.compare(a, b));
    }

    private static List<DateTime> loadAsDateTimes(String... isoUtc) {
        List<DateTime> list = new ArrayList<>(isoUtc.length);
        for (String s : isoUtc) {
            list.add(dt(s));
        }
        return list;
    }

    private static boolean isSortedByMillis(List<DateTime> list) {
        if (list.isEmpty()) return true;
        long prev = list.get(0).getMillis();
        for (int i = 1; i < list.size(); i++) {
            long curr = list.get(i).getMillis();
            if (prev > curr) return false;
            prev = curr;
        }
        return true;
    }
}