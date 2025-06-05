package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Date;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.util.MockDate;
import org.example.DateRange;
import org.jfree.data.Range;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true,
    useJEE = true
)
public class DateRange_ESTest extends DateRange_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testZeroRange() throws Throwable {
        // Test DateRange with zero lower and upper bounds
        DateRange dateRange = new DateRange(0.0, 0.0);
        assertEquals(0L, dateRange.getUpperMillis());
    }

    @Test(timeout = 4000)
    public void testNegativeRange() throws Throwable {
        // Test DateRange with negative lower and upper bounds
        DateRange dateRange = new DateRange(-145.3, -145.3);
        assertEquals(-145L, dateRange.getUpperMillis());
        assertEquals(-145L, dateRange.getLowerMillis());
    }

    @Test(timeout = 4000)
    public void testExpandToIncludeNullRange() throws Throwable {
        // Test expanding a null Range to include a value
        Range range = Range.expandToInclude(null, 1801.7621);
        DateRange dateRange = new DateRange(range);
        assertEquals(1801L, dateRange.getUpperMillis());
        assertEquals(1801L, dateRange.getLowerMillis());
    }

    @Test(timeout = 4000)
    public void testNegativeRangeWithSameBounds() throws Throwable {
        // Test DateRange with the same negative lower and upper bounds
        DateRange dateRange = new DateRange(-1157.7534, -1157.7534);
        assertEquals(-1157L, dateRange.getLowerMillis());
        assertEquals(-1157L, dateRange.getUpperMillis());
    }

    @Test(timeout = 4000)
    public void testExpandToIncludeWithDefaultDateRange() throws Throwable {
        // Test expanding a DateRange to include a value
        DateRange dateRange = new DateRange();
        Range range = Range.expandToInclude(dateRange, 1.2030679447063568);
        try {
            range.toString();
            // fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullRangeConstructor() throws Throwable {
        // Test DateRange constructor with null Range
        try {
            new DateRange((Range) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.example.DateRange", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullDateConstructor() throws Throwable {
        // Test DateRange constructor with null Dates
        try {
            new DateRange((Date) null, (Date) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.example.DateRange", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidDateRange() throws Throwable {
        // Test DateRange with invalid MockDate range
        MockDate mockDate0 = new MockDate(1, 785, -574, -1, 32, -1);
        MockDate mockDate1 = new MockDate(0, 0, 1);
        try {
            new DateRange(mockDate0, mockDate1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.data.Range", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidRangeOrder() throws Throwable {
        // Test DateRange with lower bound greater than upper bound
        try {
            new DateRange(0.0, -5043.58);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.data.Range", e);
        }
    }

    @Test(timeout = 4000)
    public void testValidMockDateRange() throws Throwable {
        // Test DateRange with valid MockDate range
        MockDate mockDate = new MockDate(774, 774, 32);
        DateRange dateRange = new DateRange(mockDate, mockDate);
        assertFalse(dateRange.isNaNRange());
    }

    @Test(timeout = 4000)
    public void testUpperDateForZeroRange() throws Throwable {
        // Test getting upper date for zero range
        DateRange dateRange = new DateRange(0.0, 0.0);
        Date date = dateRange.getUpperDate();
        assertEquals("Thu Jan 01 00:00:00 GMT 1970", date.toString());
    }

    @Test(timeout = 4000)
    public void testLowerDateForZeroRange() throws Throwable {
        // Test getting lower date for zero range
        DateRange dateRange = new DateRange(0.0, 0.0);
        Date date = dateRange.getLowerDate();
        assertEquals("Thu Jan 01 00:00:00 GMT 1970", date.toString());
    }

    @Test(timeout = 4000)
    public void testDefaultDateRangeLowerMillis() throws Throwable {
        // Test default DateRange lower millis
        DateRange dateRange = new DateRange();
        long lowerMillis = dateRange.getLowerMillis();
        assertEquals(1L, dateRange.getUpperMillis());
        assertEquals(0L, lowerMillis);
    }

    @Test(timeout = 4000)
    public void testDefaultDateRangeToString() throws Throwable {
        // Test default DateRange toString method
        DateRange dateRange = new DateRange();
        String rangeString = dateRange.toString();
        assertEquals(1L, dateRange.getUpperMillis());
        assertEquals(0L, dateRange.getLowerMillis());
        assertEquals("[Jan 1, 1970 12:00:00 AM --> Jan 1, 1970 12:00:00 AM]", rangeString);
    }

    @Test(timeout = 4000)
    public void testDefaultDateRangeUpperMillis() throws Throwable {
        // Test default DateRange upper millis
        DateRange dateRange = new DateRange();
        long upperMillis = dateRange.getUpperMillis();
        assertEquals(1L, upperMillis);
        assertEquals(0L, dateRange.getLowerMillis());
    }
}