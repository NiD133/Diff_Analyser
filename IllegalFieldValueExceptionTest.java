package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.SkipDateTimeField;

/**
 * Tests IllegalFieldValueException by triggering it from other methods.
 */
public class TestIllegalFieldValueException extends TestCase {

    // Constants for common values used in tests
    private static final int INVALID_MONTH = -5;
    private static final int VALID_MONTH_LOWER_BOUND = 1;
    private static final int VALID_MONTH_UPPER_BOUND = 31;

    private static final int INVALID_HOUR = 27;
    private static final int VALID_HOUR_LOWER_BOUND = 0;
    private static final int VALID_HOUR_UPPER_BOUND = 23;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestIllegalFieldValueException.class);
    }

    public TestIllegalFieldValueException(String name) {
        super(name);
    }

    public void testVerifyValueBounds() {
        testInvalidMonthOfYear();
        testInvalidHourOfDay();
        testInvalidCustomField();
    }

    private void testInvalidMonthOfYear() {
        try {
            FieldUtils.verifyValueBounds(ISOChronology.getInstance().monthOfYear(), INVALID_MONTH, VALID_MONTH_LOWER_BOUND, VALID_MONTH_UPPER_BOUND);
            fail("Expected IllegalFieldValueException for invalid month");
        } catch (IllegalFieldValueException e) {
            assertIllegalFieldValueException(e, DateTimeFieldType.monthOfYear(), INVALID_MONTH, VALID_MONTH_LOWER_BOUND, VALID_MONTH_UPPER_BOUND);
        }
    }

    private void testInvalidHourOfDay() {
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.hourOfDay(), INVALID_HOUR, VALID_HOUR_LOWER_BOUND, VALID_HOUR_UPPER_BOUND);
            fail("Expected IllegalFieldValueException for invalid hour");
        } catch (IllegalFieldValueException e) {
            assertIllegalFieldValueException(e, DateTimeFieldType.hourOfDay(), INVALID_HOUR, VALID_HOUR_LOWER_BOUND, VALID_HOUR_UPPER_BOUND);
        }
    }

    private void testInvalidCustomField() {
        try {
            FieldUtils.verifyValueBounds("foo", 1, 2, 3);
            fail("Expected IllegalFieldValueException for custom field");
        } catch (IllegalFieldValueException e) {
            assertIllegalFieldValueException(e, null, 1, 2, 3);
        }
    }

    public void testSkipDateTimeField() {
        DateTimeField field = new SkipDateTimeField(ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC().year(), 1970);
        try {
            field.set(0, 1970);
            fail("Expected IllegalFieldValueException for year 1970");
        } catch (IllegalFieldValueException e) {
            assertIllegalFieldValueException(e, DateTimeFieldType.year(), 1970, null, null);
        }
    }

    public void testSetText() {
        testSetTextWithNullValue();
        testSetTextWithInvalidStringValue();
    }

    private void testSetTextWithNullValue() {
        try {
            ISOChronology.getInstanceUTC().year().set(0, null, java.util.Locale.US);
            fail("Expected IllegalFieldValueException for null year value");
        } catch (IllegalFieldValueException e) {
            assertIllegalFieldValueException(e, DateTimeFieldType.year(), null, null, null);
        }
    }

    private void testSetTextWithInvalidStringValue() {
        try {
            ISOChronology.getInstanceUTC().year().set(0, "nineteen seventy", java.util.Locale.US);
            fail("Expected IllegalFieldValueException for invalid string year value");
        } catch (IllegalFieldValueException e) {
            assertIllegalFieldValueException(e, DateTimeFieldType.year(), "nineteen seventy", null, null);
        }
    }

    public void testZoneTransition() {
        DateTime dt = new DateTime(2005, 4, 3, 1, 0, 0, 0, DateTimeZone.forID("America/Los_Angeles"));
        try {
            dt.hourOfDay().setCopy(2);
            fail("Expected IllegalFieldValueException for hour transition");
        } catch (IllegalFieldValueException e) {
            assertIllegalFieldValueException(e, DateTimeFieldType.hourOfDay(), 2, null, null);
        }
    }

    public void testJulianYearZero() {
        DateTime dt = new DateTime(JulianChronology.getInstanceUTC());
        try {
            dt.year().setCopy(0);
            fail("Expected IllegalFieldValueException for Julian year zero");
        } catch (IllegalFieldValueException e) {
            assertIllegalFieldValueException(e, DateTimeFieldType.year(), 0, null, null);
        }
    }

    public void testGJCutover() {
        testGJCutoverInvalidDay("1582-10-04", 5);
        testGJCutoverInvalidDay("1582-10-15", 14);
    }

    private void testGJCutoverInvalidDay(String date, int invalidDay) {
        DateTime dt = new DateTime(date, GJChronology.getInstanceUTC());
        try {
            dt.dayOfMonth().setCopy(invalidDay);
            fail("Expected IllegalFieldValueException for invalid GJ cutover day");
        } catch (IllegalFieldValueException e) {
            assertIllegalFieldValueException(e, DateTimeFieldType.dayOfMonth(), invalidDay, null, null);
        }
    }

    @SuppressWarnings("deprecation")
    public void testReadablePartialValidate() {
        testInvalidYearMonthDayMonth(-5, 1);
        testInvalidYearMonthDayMonth(500, 1);
        testInvalidYearMonthDayDay(2, 30);
    }

    private void testInvalidYearMonthDayMonth(int month, int day) {
        try {
            new YearMonthDay(1970, month, day);
            fail("Expected IllegalFieldValueException for invalid month");
        } catch (IllegalFieldValueException e) {
            assertIllegalFieldValueException(e, DateTimeFieldType.monthOfYear(), month, 1, 12);
        }
    }

    private void testInvalidYearMonthDayDay(int month, int day) {
        try {
            new YearMonthDay(1970, month, day);
            fail("Expected IllegalFieldValueException for invalid day");
        } catch (IllegalFieldValueException e) {
            assertIllegalFieldValueException(e, DateTimeFieldType.dayOfMonth(), day, null, 28);
        }
    }

    public void testOtherConstructors() {
        testConstructorWithDurationFieldType();
        testConstructorWithStringValue();
    }

    private void testConstructorWithDurationFieldType() {
        IllegalFieldValueException e = new IllegalFieldValueException(DurationFieldType.days(), 1, 2, 3);
        assertIllegalFieldValueException(e, null, 1, 2, 3);
    }

    private void testConstructorWithStringValue() {
        IllegalFieldValueException e = new IllegalFieldValueException(DurationFieldType.months(), "five");
        assertIllegalFieldValueException(e, null, "five", null, null);
    }

    private void assertIllegalFieldValueException(IllegalFieldValueException e, DateTimeFieldType expectedFieldType, Object expectedValue, Object expectedLowerBound, Object expectedUpperBound) {
        assertEquals(expectedFieldType, e.getDateTimeFieldType());
        assertEquals(expectedValue, e.getIllegalNumberValue() != null ? e.getIllegalNumberValue() : e.getIllegalStringValue());
        assertEquals(expectedLowerBound, e.getLowerBound());
        assertEquals(expectedUpperBound, e.getUpperBound());
    }
}