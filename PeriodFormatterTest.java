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

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;

/**
 * Test suite for PeriodFormatter functionality including printing and parsing of time periods.
 *
 * @author Stephen Colebourne
 */
public class TestPeriodFormatter extends TestCase {

    // Test data constants
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final DateTimeZone NEWYORK = DateTimeZone.forID("America/New_York");
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();
    private static final Chronology ISO_PARIS = ISOChronology.getInstance(PARIS);
    private static final Chronology BUDDHIST_PARIS = BuddhistChronology.getInstance(PARIS);

    // Test period: 1 year, 2 months, 3 weeks, 4 days, 5 hours, 6 minutes, 7 seconds, 8 milliseconds
    private static final Period SAMPLE_PERIOD = new Period(1, 2, 3, 4, 5, 6, 7, 8);
    private static final String EXPECTED_ISO_FORMAT = "P1Y2M3W4DT5H6M7.008S";
    
    // Fixed test time: June 9, 2002
    private static final long DAYS_TO_2002 = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                                             366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                                             365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                                             366 + 365;
    private static final long TEST_TIME_JUNE_9_2002 =
            (DAYS_TO_2002 + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

    // Test environment state
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;
    private PeriodFormatter standardFormatter = null;

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
        setupFixedTestEnvironment();
        standardFormatter = ISOPeriodFormat.standard();
    }

    @Override
    protected void tearDown() throws Exception {
        restoreOriginalEnvironment();
    }

    private void setupFixedTestEnvironment() {
        // Fix time to ensure consistent test results
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_JUNE_9_2002);
        
        // Store original environment settings
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        
        // Set test environment to London/UK
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    private void restoreOriginalEnvironment() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        
        // Clean up references
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
        standardFormatter = null;
    }

    // ========================================================================
    // Print functionality tests
    // ========================================================================

    public void testPrint_StandardPeriodFormat() {
        // Given: A period with all components set
        // When: Printing using standard ISO format
        String result = standardFormatter.print(SAMPLE_PERIOD);
        
        // Then: Should produce correct ISO period string
        assertEquals("Standard period should format to ISO string", 
                     EXPECTED_ISO_FORMAT, result);
    }

    public void testPrintTo_StringBuffer() throws Exception {
        // Given: A StringBuffer and a sample period
        StringBuffer buffer = new StringBuffer();
        
        // When: Printing period to buffer
        standardFormatter.printTo(buffer, SAMPLE_PERIOD);
        
        // Then: Buffer should contain formatted period
        assertEquals("Period should be correctly formatted in StringBuffer", 
                     EXPECTED_ISO_FORMAT, buffer.toString());
    }

    public void testPrintTo_StringBuffer_WithNullPeriod() {
        // Given: A StringBuffer and null period
        StringBuffer buffer = new StringBuffer();
        
        // When: Attempting to print null period
        // Then: Should throw IllegalArgumentException
        try {
            standardFormatter.printTo(buffer, null);
            fail("Should throw IllegalArgumentException for null period");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    public void testPrintTo_Writer() throws Exception {
        // Given: A CharArrayWriter and a sample period
        CharArrayWriter writer = new CharArrayWriter();
        
        // When: Printing period to writer
        standardFormatter.printTo(writer, SAMPLE_PERIOD);
        
        // Then: Writer should contain formatted period
        assertEquals("Period should be correctly formatted in Writer", 
                     EXPECTED_ISO_FORMAT, writer.toString());
    }

    public void testPrintTo_Writer_WithNullPeriod() {
        // Given: A CharArrayWriter and null period
        CharArrayWriter writer = new CharArrayWriter();
        
        // When: Attempting to print null period
        // Then: Should throw IllegalArgumentException
        try {
            standardFormatter.printTo(writer, null);
            fail("Should throw IllegalArgumentException for null period");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    // ========================================================================
    // Formatter configuration tests
    // ========================================================================

    public void testWithLocale_SettingAndGetting() {
        // When: Creating formatter with French locale
        PeriodFormatter frenchFormatter = standardFormatter.withLocale(Locale.FRENCH);
        
        // Then: Should return formatter with correct locale
        assertEquals("Formatter should have French locale", 
                     Locale.FRENCH, frenchFormatter.getLocale());
        
        // And: Should return same instance when setting same locale again
        assertSame("Should return same instance for identical locale", 
                   frenchFormatter, frenchFormatter.withLocale(Locale.FRENCH));
    }

    public void testWithLocale_NullLocale() {
        // When: Creating formatter with null locale
        PeriodFormatter nullLocaleFormatter = standardFormatter.withLocale(null);
        
        // Then: Should return formatter with null locale
        assertEquals("Formatter should have null locale", 
                     null, nullLocaleFormatter.getLocale());
        
        // And: Should return same instance when setting null again
        assertSame("Should return same instance for null locale", 
                   nullLocaleFormatter, nullLocaleFormatter.withLocale(null));
    }

    public void testWithParseType_SettingAndGetting() {
        // When: Creating formatter with day-time parse type
        PeriodFormatter dayTimeFormatter = standardFormatter.withParseType(PeriodType.dayTime());
        
        // Then: Should return formatter with correct parse type
        assertEquals("Formatter should have dayTime parse type", 
                     PeriodType.dayTime(), dayTimeFormatter.getParseType());
        
        // And: Should return same instance when setting same type again
        assertSame("Should return same instance for identical parse type", 
                   dayTimeFormatter, dayTimeFormatter.withParseType(PeriodType.dayTime()));
    }

    public void testWithParseType_NullType() {
        // When: Creating formatter with null parse type
        PeriodFormatter nullTypeFormatter = standardFormatter.withParseType(null);
        
        // Then: Should return formatter with null parse type
        assertEquals("Formatter should have null parse type", 
                     null, nullTypeFormatter.getParseType());
        
        // And: Should return same instance when setting null again
        assertSame("Should return same instance for null parse type", 
                   nullTypeFormatter, nullTypeFormatter.withParseType(null));
    }

    public void testPrinterParserCapabilities_BothAvailable() {
        // Given: Formatter with both printer and parser
        PeriodFormatter fullFormatter = new PeriodFormatter(
                standardFormatter.getPrinter(), 
                standardFormatter.getParser());
        
        // Then: Should report both capabilities correctly
        assertEquals("Should have same printer", 
                     standardFormatter.getPrinter(), fullFormatter.getPrinter());
        assertEquals("Should have same parser", 
                     standardFormatter.getParser(), fullFormatter.getParser());
        assertTrue("Should be able to print", fullFormatter.isPrinter());
        assertTrue("Should be able to parse", fullFormatter.isParser());
        
        // And: Should be able to perform both operations
        assertNotNull("Should be able to print period", 
                      fullFormatter.print(SAMPLE_PERIOD));
        assertNotNull("Should be able to parse period", 
                      fullFormatter.parsePeriod(EXPECTED_ISO_FORMAT));
    }

    public void testPrinterParserCapabilities_PrinterOnly() {
        // Given: Formatter with printer only
        PeriodFormatter printerOnlyFormatter = new PeriodFormatter(
                standardFormatter.getPrinter(), null);
        
        // Then: Should report capabilities correctly
        assertEquals("Should have printer", 
                     standardFormatter.getPrinter(), printerOnlyFormatter.getPrinter());
        assertEquals("Should have no parser", 
                     null, printerOnlyFormatter.getParser());
        assertTrue("Should be able to print", printerOnlyFormatter.isPrinter());
        assertFalse("Should not be able to parse", printerOnlyFormatter.isParser());
        
        // And: Should be able to print but not parse
        assertNotNull("Should be able to print period", 
                      printerOnlyFormatter.print(SAMPLE_PERIOD));
        
        try {
            printerOnlyFormatter.parsePeriod(EXPECTED_ISO_FORMAT);
            fail("Should throw UnsupportedOperationException when parsing without parser");
        } catch (UnsupportedOperationException expected) {
            // Expected behavior
        }
    }

    public void testPrinterParserCapabilities_ParserOnly() {
        // Given: Formatter with parser only
        PeriodFormatter parserOnlyFormatter = new PeriodFormatter(
                null, standardFormatter.getParser());
        
        // Then: Should report capabilities correctly
        assertEquals("Should have no printer", 
                     null, parserOnlyFormatter.getPrinter());
        assertEquals("Should have parser", 
                     standardFormatter.getParser(), parserOnlyFormatter.getParser());
        assertFalse("Should not be able to print", parserOnlyFormatter.isPrinter());
        assertTrue("Should be able to parse", parserOnlyFormatter.isParser());
        
        // And: Should be able to parse but not print
        assertNotNull("Should be able to parse period", 
                      parserOnlyFormatter.parsePeriod(EXPECTED_ISO_FORMAT));
        
        try {
            parserOnlyFormatter.print(SAMPLE_PERIOD);
            fail("Should throw UnsupportedOperationException when printing without printer");
        } catch (UnsupportedOperationException expected) {
            // Expected behavior
        }
    }

    // ========================================================================
    // Parse functionality tests
    // ========================================================================

    public void testParsePeriod_ValidInput() {
        // When: Parsing valid ISO period string
        Period parsedPeriod = standardFormatter.parsePeriod(EXPECTED_ISO_FORMAT);
        
        // Then: Should return equivalent period
        assertEquals("Parsed period should match original", 
                     SAMPLE_PERIOD, parsedPeriod);
    }

    public void testParsePeriod_InvalidInput() {
        // When: Parsing invalid period string
        // Then: Should throw IllegalArgumentException
        try {
            standardFormatter.parsePeriod("ABC");
            fail("Should throw IllegalArgumentException for invalid input");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    public void testParsePeriod_WithSpecificParseType() {
        // Given: Formatter configured for day-time periods only
        PeriodFormatter dayTimeFormatter = standardFormatter.withParseType(PeriodType.dayTime());
        Period expectedDayTimePeriod = new Period(0, 0, 0, 4, 5, 6, 7, 8, PeriodType.dayTime());
        
        // When: Parsing day-time period string
        Period parsedPeriod = dayTimeFormatter.parsePeriod("P4DT5H6M7.008S");
        
        // Then: Should return period with correct type and values
        assertEquals("Should parse day-time period correctly", 
                     expectedDayTimePeriod, parsedPeriod);
    }

    public void testParsePeriod_WithIncompatibleParseType() {
        // Given: Formatter configured for day-time periods only
        PeriodFormatter dayTimeFormatter = standardFormatter.withParseType(PeriodType.dayTime());
        
        // When: Parsing period string with incompatible components (weeks)
        // Then: Should throw IllegalArgumentException
        try {
            dayTimeFormatter.parsePeriod("P3W4DT5H6M7.008S");
            fail("Should throw IllegalArgumentException for incompatible parse type");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    public void testParseMutablePeriod_ValidInput() {
        // Given: Expected mutable period
        MutablePeriod expectedMutablePeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        
        // When: Parsing valid ISO period string to mutable period
        MutablePeriod parsedPeriod = standardFormatter.parseMutablePeriod(EXPECTED_ISO_FORMAT);
        
        // Then: Should return equivalent mutable period
        assertEquals("Parsed mutable period should match expected", 
                     expectedMutablePeriod, parsedPeriod);
    }

    public void testParseMutablePeriod_InvalidInput() {
        // When: Parsing invalid period string to mutable period
        // Then: Should throw IllegalArgumentException
        try {
            standardFormatter.parseMutablePeriod("ABC");
            fail("Should throw IllegalArgumentException for invalid input");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    public void testParseInto_ValidInput() {
        // Given: Empty mutable period and expected result
        MutablePeriod targetPeriod = new MutablePeriod();
        MutablePeriod expectedPeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        
        // When: Parsing into the mutable period
        int newPosition = standardFormatter.parseInto(targetPeriod, EXPECTED_ISO_FORMAT, 0);
        
        // Then: Should return correct position and populate period
        assertEquals("Should return position after parsed text", 
                     20, newPosition);
        assertEquals("Target period should be populated correctly", 
                     expectedPeriod, targetPeriod);
    }

    public void testParseInto_NullTarget() {
        // When: Attempting to parse into null period
        // Then: Should throw IllegalArgumentException
        try {
            standardFormatter.parseInto(null, EXPECTED_ISO_FORMAT, 0);
            fail("Should throw IllegalArgumentException for null target period");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    public void testParseInto_InvalidInput() {
        // Given: Empty mutable period and invalid input
        MutablePeriod targetPeriod = new MutablePeriod();
        
        // When: Parsing invalid input
        int result = standardFormatter.parseInto(targetPeriod, "ABC", 0);
        
        // Then: Should return bitwise complement of error position (which is ~0)
        assertEquals("Should return complement of error position", 
                     ~0, result);
    }
}