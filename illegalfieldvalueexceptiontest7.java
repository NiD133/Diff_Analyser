package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.SkipDateTimeField;

public class IllegalFieldValueExceptionTestTest7 extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestIllegalFieldValueException.class);
    }

    @SuppressWarnings("deprecation")
    public void testReadablePartialValidate() {
        try {
            new YearMonthDay(1970, -5, 1);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.monthOfYear(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("monthOfYear", e.getFieldName());
            assertEquals(new Integer(-5), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("-5", e.getIllegalValueAsString());
            assertEquals(new Integer(1), e.getLowerBound());
            assertEquals(null, e.getUpperBound());
        }
        try {
            new YearMonthDay(1970, 500, 1);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.monthOfYear(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("monthOfYear", e.getFieldName());
            assertEquals(new Integer(500), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("500", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(new Integer(12), e.getUpperBound());
        }
        try {
            new YearMonthDay(1970, 2, 30);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.dayOfMonth(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("dayOfMonth", e.getFieldName());
            assertEquals(new Integer(30), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("30", e.getIllegalValueAsString());
            assertEquals(null, e.getLowerBound());
            assertEquals(new Integer(28), e.getUpperBound());
        }
    }
}
