package org.joda.time;

import java.util.Comparator;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest extends TestCase {

    // Comparators initialized in setUp() for use across multiple tests
    private Comparator<Object> millisComparator;
    private Comparator<Object> secondComparator;
    private Comparator<Object> minuteComparator;
    private Comparator<Object> hourComparator;
    private Comparator<Object> dayOfWeekComparator;
    private Comparator<Object> dayOfMonthComparator;
    private Comparator<Object> dayOfYearComparator;
    private Comparator<Object> weekOfWeekyearComparator;
    private Comparator<Object> weekyearComparator;
    private Comparator<Object> monthComparator;
    private Comparator<Object> yearComparator;
    private Comparator<Object> dateOnlyComparator;
    private Comparator<Object> timeOnlyComparator;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(DateTimeComparatorTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        // These comparators are initialized here to be available for all tests in the suite.
        // This avoids repetitive setup in each test method.
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

    //-----------------------------------------------------------------------
    public void testGetInstance_noArgs_returnsFullComparator() {
        // The no-arg getInstance() should return the default singleton comparator
        DateTimeComparator comparator = DateTimeComparator.getInstance();

        // A "full" comparator has no lower or upper field limits.
        assertNull("Lower limit should be null for a full comparator", comparator.getLowerLimit());
        assertNull("Upper limit should be null for a full comparator", comparator.getUpperLimit());
        assertEquals("DateTimeComparator[]", comparator.toString());
    }
}