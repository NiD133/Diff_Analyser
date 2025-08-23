package org.joda.time;

import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

/**
 * Tests that various DateTimeComparators correctly identify equal instants.
 */
public class DateTimeComparatorEqualityTest {

    // Comparator for milliseconds, ignoring any larger fields.
    private Comparator<Object> millisComparator;
    // Comparator for seconds, ignoring milliseconds.
    private Comparator<Object> secondComparator;
    // Comparator for minutes, ignoring seconds and smaller fields.
    private Comparator<Object> minuteComparator;
    // Comparator for hours, ignoring minutes and smaller fields.
    private Comparator<Object> hourComparator;
    // Comparator for the day of the week.
    private Comparator<Object> dayOfWeekComparator;
    // Comparator for the day of the month.
    private Comparator<Object> dayOfMonthComparator;
    // Comparator for the day of the year.
    private Comparator<Object> dayOfYearComparator;
    // Comparator for the week of the week-year.
    private Comparator<Object> weekOfWeekyearComparator;
    // Comparator for the week-year.
    private Comparator<Object> weekyearComparator;
    // Comparator for the month of the year.
    private Comparator<Object> monthComparator;
    // Comparator for the year.
    private Comparator<Object> yearComparator;
    // Comparator for the date part only (year, month, day).
    private Comparator<Object> dateOnlyComparator;
    // Comparator for the time part only (hour, minute, second, millis).
    private Comparator<Object> timeOnlyComparator;

    @Before
    public void setUp() {
        // Initialize all comparators to be used in the test.
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

    @Test
    public void comparatorsShouldReturnZeroForEqualInstants() {
        // Create two identical ReadableInstant objects to compare.
        long now = System.currentTimeMillis();
        ReadableInstant instant1 = new DateTime(now, DateTimeZone.UTC);
        ReadableInstant instant2 = new DateTime(now, DateTimeZone.UTC);

        // Assert that all comparators return 0, indicating equality.
        assertEquals("Millis comparator should be 0 for equal instants", 0, millisComparator.compare(instant1, instant2));
        assertEquals("Second comparator should be 0 for equal instants", 0, secondComparator.compare(instant1, instant2));
        assertEquals("Minute comparator should be 0 for equal instants", 0, minuteComparator.compare(instant1, instant2));
        assertEquals("Hour comparator should be 0 for equal instants", 0, hourComparator.compare(instant1, instant2));
        assertEquals("Day of week comparator should be 0 for equal instants", 0, dayOfWeekComparator.compare(instant1, instant2));
        assertEquals("Day of month comparator should be 0 for equal instants", 0, dayOfMonthComparator.compare(instant1, instant2));
        assertEquals("Day of year comparator should be 0 for equal instants", 0, dayOfYearComparator.compare(instant1, instant2));
        assertEquals("Week of week-year comparator should be 0 for equal instants", 0, weekOfWeekyearComparator.compare(instant1, instant2));
        assertEquals("Week-year comparator should be 0 for equal instants", 0, weekyearComparator.compare(instant1, instant2));
        assertEquals("Month comparator should be 0 for equal instants", 0, monthComparator.compare(instant1, instant2));
        assertEquals("Year comparator should be 0 for equal instants", 0, yearComparator.compare(instant1, instant2));
        assertEquals("Date-only comparator should be 0 for equal instants", 0, dateOnlyComparator.compare(instant1, instant2));
        assertEquals("Time-only comparator should be 0 for equal instants", 0, timeOnlyComparator.compare(instant1, instant2));
    }
}