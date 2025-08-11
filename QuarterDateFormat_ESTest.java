package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.io.StringWriter;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.evosuite.runtime.mock.java.util.MockDate;

/**
 * Unit tests for the QuarterDateFormat class.
 */
public class QuarterDateFormatTest {

    @Test(timeout = 4000)
    public void testDifferentQuarterSymbolsNotEqual() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        QuarterDateFormat regularFormat = new QuarterDateFormat(defaultTimeZone);
        QuarterDateFormat romanFormat = new QuarterDateFormat(defaultTimeZone, QuarterDateFormat.ROMAN_QUARTERS, true);
        
        boolean areEqual = romanFormat.equals(regularFormat);
        
        assertFalse("QuarterDateFormat instances with different quarter symbols should not be equal", areEqual);
    }

    @Test(timeout = 4000)
    public void testFormatWithMockDate() {
        SimpleTimeZone simpleTimeZone = new SimpleTimeZone(712, "");
        String[] customQuarters = new String[1];
        QuarterDateFormat format = new QuarterDateFormat(simpleTimeZone, customQuarters);
        MockDate mockDate = new MockDate(-1, 3, -1, -1, 1943, -2263);
        Format.Field mockField = mock(Format.Field.class);
        FieldPosition fieldPosition = new FieldPosition(mockField, 712);
        StringBuffer buffer = new StringBuffer("{F@~<xQ@d0");
        
        format.format((Date) mockDate, buffer, fieldPosition);
        
        assertEquals("Formatted date length should be 19", 19, buffer.length());
    }

    @Test(timeout = 4000)
    public void testFormatWithRomanQuarters() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        QuarterDateFormat romanFormat = new QuarterDateFormat(defaultTimeZone, QuarterDateFormat.ROMAN_QUARTERS, true);
        MockDate mockDate = new MockDate(-2355, 0, 2123, 584, 0, -797);
        StringWriter writer = new StringWriter();
        StringBuffer buffer = writer.getBuffer();
        
        romanFormat.format((Date) mockDate, buffer, null);
        
        assertEquals("Formatted date should be 'IV 451'", "IV 451", buffer.toString());
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testFormatWithNullBuffer() {
        SimpleTimeZone simpleTimeZone = new SimpleTimeZone(4, "");
        QuarterDateFormat format = new QuarterDateFormat(simpleTimeZone);
        MockDate mockDate = new MockDate(1L);
        FieldPosition fieldPosition = new FieldPosition(80);
        
        format.format((Date) mockDate, null, fieldPosition);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testEqualsWithNullNumberFormat() {
        QuarterDateFormat format1 = new QuarterDateFormat();
        format1.setNumberFormat(null);
        QuarterDateFormat format2 = new QuarterDateFormat();
        
        format1.equals(format2);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testConstructorWithNullTimeZone() {
        String[] customQuarters = new String[6];
        new QuarterDateFormat(null, customQuarters, true);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testConstructorWithNullTimeZoneAndQuarters() {
        String[] customQuarters = new String[1];
        new QuarterDateFormat(null, customQuarters);
    }

    @Test(timeout = 4000)
    public void testEqualQuarterDateFormatInstances() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        QuarterDateFormat format1 = new QuarterDateFormat(defaultTimeZone);
        QuarterDateFormat format2 = new QuarterDateFormat(defaultTimeZone);
        
        boolean areEqual = format1.equals(format2);
        
        assertTrue("QuarterDateFormat instances with the same configuration should be equal", areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObjectType() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        QuarterDateFormat format = new QuarterDateFormat(defaultTimeZone);
        Object otherObject = new Object();
        
        boolean areEqual = format.equals(otherObject);
        
        assertFalse("QuarterDateFormat should not be equal to an object of a different type", areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameInstance() {
        QuarterDateFormat format = new QuarterDateFormat();
        
        boolean areEqual = format.equals(format);
        
        assertTrue("QuarterDateFormat should be equal to itself", areEqual);
    }

    @Test(timeout = 4000)
    public void testDifferentQuarterSymbolsNotEqualReversed() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        QuarterDateFormat regularFormat = new QuarterDateFormat(defaultTimeZone);
        QuarterDateFormat romanFormat = new QuarterDateFormat(defaultTimeZone, QuarterDateFormat.ROMAN_QUARTERS, true);
        
        boolean areEqual = regularFormat.equals(romanFormat);
        
        assertFalse("QuarterDateFormat instances with different quarter symbols should not be equal", areEqual);
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void testFormatWithEmptyQuarterSymbols() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        String[] emptyQuarters = new String[0];
        QuarterDateFormat format = new QuarterDateFormat(defaultTimeZone, emptyQuarters, true);
        MockDate mockDate = new MockDate();
        FieldPosition fieldPosition = new FieldPosition(-1375);
        
        format.format((Date) mockDate, null, fieldPosition);
    }

    @Test(timeout = 4000)
    public void testFormatWithDefaultQuarters() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        QuarterDateFormat format = new QuarterDateFormat(defaultTimeZone);
        MockDate mockDate = new MockDate(-2355, 0, 2123, 584, 0, -797);
        StringWriter writer = new StringWriter();
        StringBuffer buffer = writer.getBuffer();
        
        format.format((Date) mockDate, buffer, null);
        
        assertEquals("Formatted date should be '451 4'", "451 4", writer.toString());
    }

    @Test(timeout = 4000)
    public void testParseNotImplemented() {
        QuarterDateFormat format = new QuarterDateFormat();
        Date parsedDate = format.parse(" ", null);
        
        assertNull("Parse method is not implemented and should return null", parsedDate);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testConstructorWithNullTimeZoneOnly() {
        new QuarterDateFormat(null);
    }
}