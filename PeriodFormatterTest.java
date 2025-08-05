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

import java.io.CharArrayWriter;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * This class is a Junit unit test for Period Formating.
 *
 * @author Stephen Colebourne
 */
public class TestPeriodFormatter extends TestCase {

    // Test time setup (not directly used in period formatting tests)
    private static final long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    private static final long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;

    // Standard period components for test data
    private static final String STANDARD_PERIOD_STRING = "P1Y2M3W4DT5H6M7.008S";
    private static final Period TEST_PERIOD = new Period(1, 2, 3, 4, 5, 6, 7, 8);
    private static final MutablePeriod TEST_MUTABLE_PERIOD = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;
    private PeriodFormatter formatter;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestPeriodFormatter.class);
    }

    public TestPeriodFormatter(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        // Fixed time setup (doesn't affect period formatting but preserves original behavior)
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/London"));
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
        
        formatter = ISOPeriodFormat.standard();
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        formatter = null;
    }

    //-----------------------------------------------------------------------
    // Test Period Printing
    //-----------------------------------------------------------------------

    /**
     * Tests basic printing of a period in standard ISO format.
     */
    public void testPrintStandardFormat() {
        assertEquals(STANDARD_PERIOD_STRING, formatter.print(TEST_PERIOD));
    }

    /**
     * Tests printing to a StringBuffer.
     */
    public void testPrintToStringBuffer() {
        StringBuffer buf = new StringBuffer();
        formatter.printTo(buf, TEST_PERIOD);
        assertEquals(STANDARD_PERIOD_STRING, buf.toString());
    }

    /**
     * Tests printing to a StringBuffer with null period (expects exception).
     */
    public void testPrintToStringBufferWithNullPeriod() {
        try {
            formatter.printTo(new StringBuffer(), null);
            fail("Expected IllegalArgumentException when printing null period");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    /**
     * Tests printing to a Writer.
     */
    public void testPrintToWriter() throws Exception {
        CharArrayWriter out = new CharArrayWriter();
        formatter.printTo(out, TEST_PERIOD);
        assertEquals(STANDARD_PERIOD_STRING, out.toString());
    }

    /**
     * Tests printing to a Writer with null period (expects exception).
     */
    public void testPrintToWriterWithNullPeriod() {
        try {
            formatter.printTo(new CharArrayWriter(), null);
            fail("Expected IllegalArgumentException when printing null period");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    //-----------------------------------------------------------------------
    // Test Configuration Methods
    //-----------------------------------------------------------------------

    /**
     * Tests setting/getting locale.
     */
    public void testWithGetLocale() {
        PeriodFormatter frenchFormatter = formatter.withLocale(Locale.FRENCH);
        assertEquals(Locale.FRENCH, frenchFormatter.getLocale());
        assertSame(frenchFormatter, frenchFormatter.withLocale(Locale.FRENCH));
        
        PeriodFormatter nullLocaleFormatter = formatter.withLocale(null);
        assertNull(nullLocaleFormatter.getLocale());
        assertSame(nullLocaleFormatter, nullLocaleFormatter.withLocale(null));
    }

    /**
     * Tests setting/getting parse type.
     */
    public void testWithGetParseType() {
        PeriodType dayTimeType = PeriodType.dayTime();
        PeriodFormatter dayTimeFormatter = formatter.withParseType(dayTimeType);
        assertEquals(dayTimeType, dayTimeFormatter.getParseType());
        assertSame(dayTimeFormatter, dayTimeFormatter.withParseType(dayTimeType));
        
        PeriodFormatter nullParseTypeFormatter = formatter.withParseType(null);
        assertNull(nullParseTypeFormatter.getParseType());
        assertSame(nullParseTypeFormatter, nullParseTypeFormatter.withParseType(null));
    }

    /**
     * Tests printer/parser access methods.
     */
    public void testPrinterParserAccessors() {
        PeriodFormatter printerParserFormatter = new PeriodFormatter(
            formatter.getPrinter(), formatter.getParser());
        
        assertEquals(formatter.getPrinter(), printerParserFormatter.getPrinter());
        assertEquals(formatter.getParser(), printerParserFormatter.getParser());
        assertTrue(printerParserFormatter.isPrinter());
        assertTrue(printerParserFormatter.isParser());
        
        assertEquals(STANDARD_PERIOD_STRING, printerParserFormatter.print(TEST_PERIOD));
        assertEquals(TEST_PERIOD, printerParserFormatter.parsePeriod(STANDARD_PERIOD_STRING));
    }

    /**
     * Tests formatter with only printer capability.
     */
    public void testPrinterOnlyFormatter() {
        PeriodFormatter printerOnlyFormatter = new PeriodFormatter(formatter.getPrinter(), null);
        
        assertEquals(formatter.getPrinter(), printerOnlyFormatter.getPrinter());
        assertNull(printerOnlyFormatter.getParser());
        assertTrue(printerOnlyFormatter.isPrinter());
        assertFalse(printerOnlyFormatter.isParser());
        
        assertEquals(STANDARD_PERIOD_STRING, printerOnlyFormatter.print(TEST_PERIOD));
        
        try {
            printerOnlyFormatter.parsePeriod(STANDARD_PERIOD_STRING);
            fail("Expected UnsupportedOperationException when parsing with printer-only formatter");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
    }

    /**
     * Tests formatter with only parser capability.
     */
    public void testParserOnlyFormatter() {
        PeriodFormatter parserOnlyFormatter = new PeriodFormatter(null, formatter.getParser());
        
        assertNull(parserOnlyFormatter.getPrinter());
        assertEquals(formatter.getParser(), parserOnlyFormatter.getParser());
        assertFalse(parserOnlyFormatter.isPrinter());
        assertTrue(parserOnlyFormatter.isParser());
        
        try {
            parserOnlyFormatter.print(TEST_PERIOD);
            fail("Expected UnsupportedOperationException when printing with parser-only formatter");
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
        
        assertEquals(TEST_PERIOD, parserOnlyFormatter.parsePeriod(STANDARD_PERIOD_STRING));
    }

    //-----------------------------------------------------------------------
    // Test Parsing Functionality
    //-----------------------------------------------------------------------

    /**
     * Tests basic period parsing.
     */
    public void testParsePeriod() {
        assertEquals(TEST_PERIOD, formatter.parsePeriod(STANDARD_PERIOD_STRING));
    }

    /**
     * Tests parsing invalid text (expects exception).
     */
    public void testParseInvalidText() {
        try {
            formatter.parsePeriod("ABC");
            fail("Expected IllegalArgumentException when parsing invalid text");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    /**
     * Tests parsing with specific period type.
     */
    public void testParseWithPeriodType() {
        PeriodType dayTimeType = PeriodType.dayTime();
        Period expectedDayTime = new Period(0, 0, 0, 4, 5, 6, 7, 8, dayTimeType);
        
        PeriodFormatter dayTimeFormatter = formatter.withParseType(dayTimeType);
        assertEquals(expectedDayTime, dayTimeFormatter.parsePeriod("P4DT5H6M7.008S"));
        
        try {
            dayTimeFormatter.parsePeriod("P3W4DT5H6M7.008S");
            fail("Expected IllegalArgumentException when parsing weeks with day-time type");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    /**
     * Tests parsing to MutablePeriod.
     */
    public void testParseMutablePeriod() {
        assertEquals(TEST_MUTABLE_PERIOD, formatter.parseMutablePeriod(STANDARD_PERIOD_STRING));
    }

    /**
     * Tests parsing invalid text to MutablePeriod (expects exception).
     */
    public void testParseMutablePeriodInvalidText() {
        try {
            formatter.parseMutablePeriod("ABC");
            fail("Expected IllegalArgumentException when parsing invalid text");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    /**
     * Tests parsing into existing MutablePeriod.
     */
    public void testParseIntoMutablePeriod() {
        MutablePeriod result = new MutablePeriod();
        int parsePosition = formatter.parseInto(result, STANDARD_PERIOD_STRING, 0);
        
        assertEquals(20, parsePosition);  // Entire string processed
        assertEquals(TEST_MUTABLE_PERIOD, result);
    }

    /**
     * Tests parsing into null MutablePeriod (expects exception).
     */
    public void testParseIntoNullPeriod() {
        try {
            formatter.parseInto(null, STANDARD_PERIOD_STRING, 0);
            fail("Expected IllegalArgumentException when parsing into null period");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    /**
     * Tests parsing invalid text into existing MutablePeriod.
     */
    public void testParseIntoInvalidText() {
        MutablePeriod result = new MutablePeriod();
        int parsePosition = formatter.parseInto(result, "ABC", 0);
        
        assertTrue("Expected negative position for parse failure", parsePosition < 0);
        assertEquals("Result period should remain unchanged", 
                     new MutablePeriod(), result);
    }
}