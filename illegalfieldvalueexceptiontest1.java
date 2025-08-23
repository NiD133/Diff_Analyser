package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.SkipDateTimeField;

public class IllegalFieldValueExceptionTestTest1 extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestIllegalFieldValueException.class);
    }

    public void testVerifyValueBounds() {
        try {
            FieldUtils.verifyValueBounds(ISOChronology.getInstance().monthOfYear(), -5, 1, 31);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.monthOfYear(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("monthOfYear", e.getFieldName());
            assertEquals(new Integer(-5), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("-5", e.getIllegalValueAsString());
            assertEquals(new Integer(1), e.getLowerBound());
            assertEquals(new Integer(31), e.getUpperBound());
        }
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.hourOfDay(), 27, 0, 23);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(DateTimeFieldType.hourOfDay(), e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("hourOfDay", e.getFieldName());
            assertEquals(new Integer(27), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("27", e.getIllegalValueAsString());
            assertEquals(new Integer(0), e.getLowerBound());
            assertEquals(new Integer(23), e.getUpperBound());
        }
        try {
            FieldUtils.verifyValueBounds("foo", 1, 2, 3);
            fail();
        } catch (IllegalFieldValueException e) {
            assertEquals(null, e.getDateTimeFieldType());
            assertEquals(null, e.getDurationFieldType());
            assertEquals("foo", e.getFieldName());
            assertEquals(new Integer(1), e.getIllegalNumberValue());
            assertEquals(null, e.getIllegalStringValue());
            assertEquals("1", e.getIllegalValueAsString());
            assertEquals(new Integer(2), e.getLowerBound());
            assertEquals(new Integer(3), e.getUpperBound());
        }
    }
}
