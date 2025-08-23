package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class DateTimeComparator_ESTest extends DateTimeComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCompareWithInvalidFormatThrowsException() {
        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        try {
            dateTimeComparator.compare(null, "DateTimeComparator[dayOfYear-]");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.format.DateTimeParserBucket", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetLowerLimitReturnsYearOfEra() {
        DateTimeFieldType yearOfEra = DateTimeFieldType.yearOfEra();
        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance(yearOfEra);
        DateTimeFieldType lowerLimit = dateTimeComparator.getLowerLimit();
        assertNotNull(lowerLimit);
        assertEquals("yearOfEra", lowerLimit.toString());
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithDayOfYear() {
        DateTimeFieldType dayOfYear = DateTimeFieldType.dayOfYear();
        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance(dayOfYear, null);
        assertNotNull(dateTimeComparator);
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithNoLimits() {
        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance(null, null);
        assertNotNull(dateTimeComparator);
    }

    @Test(timeout = 4000)
    public void testTimeOnlyInstanceToString() {
        DateTimeComparator dateTimeComparator = DateTimeComparator.getTimeOnlyInstance();
        assertEquals("DateTimeComparator[-dayOfYear]", dateTimeComparator.toString());
    }

    @Test(timeout = 4000)
    public void testWeekyearOfCenturyToString() {
        DateTimeFieldType weekyearOfCentury = DateTimeFieldType.weekyearOfCentury();
        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance(weekyearOfCentury, weekyearOfCentury);
        assertEquals("DateTimeComparator[weekyearOfCentury]", dateTimeComparator.toString());
    }

    @Test(timeout = 4000)
    public void testDefaultInstanceToString() {
        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance();
        assertEquals("DateTimeComparator[]", dateTimeComparator.toString());
    }

    @Test(timeout = 4000)
    public void testHashCodeExecution() {
        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance();
        dateTimeComparator.hashCode();
    }

    @Test(timeout = 4000)
    public void testTimeOnlyInstanceNotEqualsDefaultInstance() {
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();
        DateTimeComparator defaultComparator = DateTimeComparator.getInstance();
        assertFalse(defaultComparator.equals(timeOnlyComparator));
    }

    @Test(timeout = 4000)
    public void testDefaultInstanceNotEqualsTimeOnlyInstance() {
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();
        DateTimeComparator defaultComparator = DateTimeComparator.getInstance();
        assertFalse(timeOnlyComparator.equals(defaultComparator));
    }

    @Test(timeout = 4000)
    public void testDefaultInstanceNotEqualsDateOnlyInstance() {
        DateTimeComparator defaultComparator = DateTimeComparator.getInstance();
        DateTimeComparator dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();
        assertFalse(defaultComparator.equals(dateOnlyComparator));
    }

    @Test(timeout = 4000)
    public void testTimeOnlyInstanceEqualsItself() {
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();
        assertTrue(timeOnlyComparator.equals(timeOnlyComparator));
    }

    @Test(timeout = 4000)
    public void testTimeOnlyInstanceNotEqualsDifferentObject() {
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();
        Object differentObject = new Object();
        assertFalse(timeOnlyComparator.equals(differentObject));
    }

    @Test(timeout = 4000)
    public void testCompareNullObjectsReturnsZero() {
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();
        int result = timeOnlyComparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithMonthOfYear() {
        DateTimeFieldType monthOfYear = DateTimeFieldType.monthOfYear();
        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance(null, monthOfYear);
        assertNotNull(dateTimeComparator);
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithSameLowerAndUpperLimit() {
        DateTimeFieldType dayOfYear = DateTimeFieldType.dayOfYear();
        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance(dayOfYear, dayOfYear);
        assertNotNull(dateTimeComparator);
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithNullLowerAndDayOfYearUpperLimit() {
        DateTimeFieldType dayOfYear = DateTimeFieldType.dayOfYear();
        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance(null, dayOfYear);
        assertNotNull(dateTimeComparator);
    }

    @Test(timeout = 4000)
    public void testTimeOnlyInstanceUpperLimit() {
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();
        DateTimeFieldType upperLimit = timeOnlyComparator.getUpperLimit();
        assertEquals("dayOfYear", upperLimit.toString());
    }

    @Test(timeout = 4000)
    public void testHashCodeExecutionWithDayOfMonth() {
        DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
        DateTimeComparator dateTimeComparator = new DateTimeComparator(dayOfMonth, dayOfMonth);
        dateTimeComparator.hashCode();
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithDayOfYearUpperLimit() {
        DateTimeFieldType dayOfYear = DateTimeFieldType.dayOfYear();
        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance(dayOfYear);
        DateTimeFieldType upperLimit = dateTimeComparator.getUpperLimit();
        assertNull(upperLimit);
    }

    @Test(timeout = 4000)
    public void testDateOnlyInstanceNotEqualsDefaultInstance() {
        DateTimeComparator dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();
        DateTimeComparator defaultComparator = DateTimeComparator.getInstance();
        assertFalse(dateOnlyComparator.equals(defaultComparator));
    }

    @Test(timeout = 4000)
    public void testDateOnlyInstanceToString() {
        DateTimeComparator dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();
        assertEquals("DateTimeComparator[dayOfYear-]", dateOnlyComparator.toString());
    }

    @Test(timeout = 4000)
    public void testTimeOnlyInstanceLowerLimitIsNull() {
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();
        DateTimeFieldType lowerLimit = timeOnlyComparator.getLowerLimit();
        assertNull(lowerLimit);
    }
}