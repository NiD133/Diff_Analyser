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
 * Unit tests for the PeriodFormatter class.
 * These tests cover the formatting and parsing of periods.
 */
public class TestPeriodFormatter extends TestCase {

    // Define commonly used DateTimeZone constants
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    // Define commonly used Chronology constants
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // Pre-calculated number of days from 1970 to 2002
    private long daysUntil2002 = 365 * 30 + 366 * 8; // Leap years included

    // Test time set to 2002-06-09
    private long TEST_TIME_NOW = 
        (daysUntil2002 + 31 + 28 + 31 + 30 + 31 + 9 - 1) * DateTimeConstants.MILLIS_PER_DAY;

    // Original system settings to be restored after tests
    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    // The PeriodFormatter instance used in tests
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
        // Set fixed current time for consistent test results
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);

        // Backup original system settings
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        // Set test-specific system settings
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);

        // Initialize the PeriodFormatter
        formatter = ISOPeriodFormat.standard();
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore original system settings
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);

        // Clear references
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
        formatter = null;
    }

    // Test printing a simple period
    public void testPrintSimplePeriod() {
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals("P1Y2M3W4DT5H6M7.008S", formatter.print(period));
    }

    // Test printing to a StringBuffer
    public void testPrintToStringBuffer() {
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        StringBuffer buffer = new StringBuffer();
        formatter.printTo(buffer, period);
        assertEquals("P1Y2M3W4DT5H6M7.008S", buffer.toString());

        // Test printing a null period
        buffer = new StringBuffer();
        try {
            formatter.printTo(buffer, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    // Test printing to a Writer
    public void testPrintToWriter() throws Exception {
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        CharArrayWriter writer = new CharArrayWriter();
        formatter.printTo(writer, period);
        assertEquals("P1Y2M3W4DT5H6M7.008S", writer.toString());

        // Test printing a null period
        writer = new CharArrayWriter();
        try {
            formatter.printTo(writer, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    // Test formatter locale methods
    public void testLocaleMethods() {
        PeriodFormatter frenchFormatter = formatter.withLocale(Locale.FRENCH);
        assertEquals(Locale.FRENCH, frenchFormatter.getLocale());
        assertSame(frenchFormatter, frenchFormatter.withLocale(Locale.FRENCH));

        PeriodFormatter nullLocaleFormatter = formatter.withLocale(null);
        assertEquals(null, nullLocaleFormatter.getLocale());
        assertSame(nullLocaleFormatter, nullLocaleFormatter.withLocale(null));
    }

    // Test formatter parse type methods
    public void testParseTypeMethods() {
        PeriodFormatter dayTimeFormatter = formatter.withParseType(PeriodType.dayTime());
        assertEquals(PeriodType.dayTime(), dayTimeFormatter.getParseType());
        assertSame(dayTimeFormatter, dayTimeFormatter.withParseType(PeriodType.dayTime()));

        PeriodFormatter nullTypeFormatter = formatter.withParseType(null);
        assertEquals(null, nullTypeFormatter.getParseType());
        assertSame(nullTypeFormatter, nullTypeFormatter.withParseType(null));
    }

    // Test printer and parser methods
    public void testPrinterParserMethods() {
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        PeriodFormatter customFormatter = new PeriodFormatter(formatter.getPrinter(), formatter.getParser());
        assertEquals(formatter.getPrinter(), customFormatter.getPrinter());
        assertEquals(formatter.getParser(), customFormatter.getParser());
        assertTrue(customFormatter.isPrinter());
        assertTrue(customFormatter.isParser());
        assertNotNull(customFormatter.print(period));
        assertNotNull(customFormatter.parsePeriod("P1Y2M3W4DT5H6M7.008S"));

        // Test with only printer
        customFormatter = new PeriodFormatter(formatter.getPrinter(), null);
        assertEquals(formatter.getPrinter(), customFormatter.getPrinter());
        assertNull(customFormatter.getParser());
        assertTrue(customFormatter.isPrinter());
        assertFalse(customFormatter.isParser());
        assertNotNull(customFormatter.print(period));
        try {
            customFormatter.parsePeriod("P1Y2M3W4DT5H6M7.008S");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected exception
        }

        // Test with only parser
        customFormatter = new PeriodFormatter(null, formatter.getParser());
        assertNull(customFormatter.getPrinter());
        assertEquals(formatter.getParser(), customFormatter.getParser());
        assertFalse(customFormatter.isPrinter());
        assertTrue(customFormatter.isParser());
        try {
            customFormatter.print(period);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // Expected exception
        }
        assertNotNull(customFormatter.parsePeriod("P1Y2M3W4DT5H6M7.008S"));
    }

    // Test parsing a simple period
    public void testParseSimplePeriod() {
        Period expectedPeriod = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(expectedPeriod, formatter.parsePeriod("P1Y2M3W4DT5H6M7.008S"));

        // Test parsing invalid string
        try {
            formatter.parsePeriod("ABC");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    // Test parsing with a specific parse type
    public void testParseWithParseType() {
        Period expectedPeriod = new Period(0, 0, 0, 4, 5, 6, 7, 8, PeriodType.dayTime());
        assertEquals(expectedPeriod, formatter.withParseType(PeriodType.dayTime()).parsePeriod("P4DT5H6M7.008S"));

        // Test parsing with incompatible parse type
        try {
            formatter.withParseType(PeriodType.dayTime()).parsePeriod("P3W4DT5H6M7.008S");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    // Test parsing into a MutablePeriod
    public void testParseIntoMutablePeriod() {
        MutablePeriod expectedPeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(expectedPeriod, formatter.parseMutablePeriod("P1Y2M3W4DT5H6M7.008S"));

        // Test parsing invalid string
        try {
            formatter.parseMutablePeriod("ABC");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    // Test parsing into an existing MutablePeriod
    public void testParseIntoExistingMutablePeriod() {
        MutablePeriod expectedPeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod resultPeriod = new MutablePeriod();
        assertEquals(20, formatter.parseInto(resultPeriod, "P1Y2M3W4DT5H6M7.008S", 0));
        assertEquals(expectedPeriod, resultPeriod);

        // Test parsing into a null period
        try {
            formatter.parseInto(null, "P1Y2M3W4DT5H6M7.008S", 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

        // Test parsing invalid string
        assertEquals(~0, formatter.parseInto(resultPeriod, "ABC", 0));
    }
}