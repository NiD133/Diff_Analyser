/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.format;

import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link PeriodFormatter}.
 */
public class TestPeriodFormatter {

    private static final Period FULL_PERIOD = new Period(1, 2, 3, 4, 5, 6, 7, 8);
    private static final String FULL_PERIOD_ISO_STRING = "P1Y2M3W4DT5H6M7.008S";

    private Locale originalLocale;
    private PeriodFormatter formatter;

    @Before
    public void setUp() {
        // Save and set a fixed locale for predictable tests
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.UK);
        // The formatter under test is the standard ISO format
        formatter = ISOPeriodFormat.standard();
    }

    @After
    public void tearDown() {
        // Restore original locale
        Locale.setDefault(originalLocale);
        originalLocale = null;
        formatter = null;
    }

    //-----------------------------------------------------------------------
    // Printing Tests
    //-----------------------------------------------------------------------

    @Test
    public void testPrint_printsStandardPeriod() {
        String result = formatter.print(FULL_PERIOD);
        assertEquals(FULL_PERIOD_ISO_STRING, result);
    }

    @Test
    public void testPrintTo_stringBuffer_appendsPeriod() {
        StringBuffer buffer = new StringBuffer("Prefix: ");
        formatter.printTo(buffer, FULL_PERIOD);
        assertEquals("Prefix: " + FULL_PERIOD_ISO_STRING, buffer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintTo_stringBuffer_throwsForNullPeriod() {
        formatter.printTo(new StringBuffer(), null);
    }

    @Test
    public void testPrintTo_writer_writesPeriod() throws IOException {
        CharArrayWriter writer = new CharArrayWriter();
        formatter.printTo(writer, FULL_PERIOD);
        assertEquals(FULL_PERIOD_ISO_STRING, writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintTo_writer_throwsForNullPeriod() throws IOException {
        formatter.printTo(new CharArrayWriter(), null);
    }

    //-----------------------------------------------------------------------
    // Parsing Tests
    //-----------------------------------------------------------------------

    @Test
    public void testParsePeriod_parsesIsoString() {
        Period result = formatter.parsePeriod(FULL_PERIOD_ISO_STRING);
        assertEquals(FULL_PERIOD, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsePeriod_throwsForInvalidString() {
        formatter.parsePeriod("INVALID-STRING");
    }

    @Test
    public void testParseMutablePeriod_parsesIsoString() {
        MutablePeriod expected = new MutablePeriod(FULL_PERIOD);
        MutablePeriod result = formatter.parseMutablePeriod(FULL_PERIOD_ISO_STRING);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutablePeriod_throwsForInvalidString() {
        formatter.parseMutablePeriod("INVALID-STRING");
    }

    @Test
    public void testParseInto_populatesMutablePeriodAndReturnsEndPosition() {
        MutablePeriod result = new MutablePeriod();
        int newPosition = formatter.parseInto(result, FULL_PERIOD_ISO_STRING, 0);

        assertEquals(FULL_PERIOD, result);
        assertEquals(FULL_PERIOD_ISO_STRING.length(), newPosition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInto_throwsForNullPeriod() {
        formatter.parseInto(null, FULL_PERIOD_ISO_STRING, 0);
    }

    @Test
    public void testParseInto_returnsNegativePositionForInvalidString() {
        MutablePeriod result = new MutablePeriod();
        int failurePosition = formatter.parseInto(result, "INVALID-STRING", 0);
        assertEquals(~0, failurePosition);
    }

    //-----------------------------------------------------------------------
    // Configuration Tests
    //-----------------------------------------------------------------------

    @Test
    public void testWithLocale_returnsNewFormatterWithLocale() {
        PeriodFormatter frenchFormatter = formatter.withLocale(Locale.FRENCH);
        assertEquals(Locale.FRENCH, frenchFormatter.getLocale());
        assertNull(formatter.getLocale()); // Original is unchanged
    }

    @Test
    public void testWithLocale_returnsSameInstanceForSameLocale() {
        PeriodFormatter frenchFormatter = formatter.withLocale(Locale.FRENCH);
        assertSame(frenchFormatter, frenchFormatter.withLocale(Locale.FRENCH));
    }

    @Test
    public void testWithParseType_returnsNewFormatterWithParseType() {
        PeriodFormatter dayTimeFormatter = formatter.withParseType(PeriodType.dayTime());
        assertEquals(PeriodType.dayTime(), dayTimeFormatter.getParseType());
        assertNull(formatter.getParseType()); // Original is unchanged
    }

    @Test
    public void testWithParseType_returnsSameInstanceForSameParseType() {
        PeriodFormatter dayTimeFormatter = formatter.withParseType(PeriodType.dayTime());
        assertSame(dayTimeFormatter, dayTimeFormatter.withParseType(PeriodType.dayTime()));
    }

    @Test
    public void testWithParseType_respectsTypeDuringParsing() {
        PeriodFormatter dayTimeFormatter = formatter.withParseType(PeriodType.dayTime());
        Period expected = new Period(0, 0, 0, 4, 5, 6, 7, 8, PeriodType.dayTime());
        Period result = dayTimeFormatter.parsePeriod("P4DT5H6M7.008S");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithParseType_throwsForFieldNotInType() {
        PeriodFormatter dayTimeFormatter = formatter.withParseType(PeriodType.dayTime());
        // This string contains weeks (W), which is not in the dayTime type.
        dayTimeFormatter.parsePeriod("P3W4DT5H6M7.008S");
    }

    //-----------------------------------------------------------------------
    // Printer/Parser State Tests
    //-----------------------------------------------------------------------

    @Test
    public void testFormatter_withPrinterAndParser() {
        PeriodFormatter f = new PeriodFormatter(formatter.getPrinter(), formatter.getParser());
        assertTrue(f.isPrinter());
        assertTrue(f.isParser());
        assertNotNull(f.print(FULL_PERIOD));
        assertNotNull(f.parsePeriod(FULL_PERIOD_ISO_STRING));
    }

    @Test
    public void testFormatter_withPrinterOnly() {
        PeriodFormatter f = new PeriodFormatter(formatter.getPrinter(), null);
        assertTrue(f.isPrinter());
        assertFalse(f.isParser());
        assertNotNull(f.print(FULL_PERIOD));
        try {
            f.parsePeriod(FULL_PERIOD_ISO_STRING);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
    }

    @Test
    public void testFormatter_withParserOnly() {
        PeriodFormatter f = new PeriodFormatter(null, formatter.getParser());
        assertFalse(f.isPrinter());
        assertTrue(f.isParser());
        assertNotNull(f.parsePeriod(FULL_PERIOD_ISO_STRING));
        try {
            f.print(FULL_PERIOD);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
    }
}