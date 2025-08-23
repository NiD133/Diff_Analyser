package org.joda.time.format;

import static org.junit.Assert.*;

import java.io.CharArrayWriter;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for PeriodFormatter behavior and configuration.
 * Tests focus on readability:
 * - clear test names
 * - shared fixtures/constants
 * - minimal duplication
 * - explicit assertions and failure messages
 */
public class PeriodFormatterTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    // Fixed "now" used by original tests (2002-06-09).
    // Calculated explicitly to avoid time zone dependencies.
    private static final long DAYS_1970_TO_2002 =
            365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
            366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 +
            365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
            366 + 365;
    private static final long FIXED_NOW_2002_06_09 =
            (DAYS_1970_TO_2002 + 31 + 28 + 31 + 30 + 31 + 9 - 1) * DateTimeConstants.MILLIS_PER_DAY;

    // Common sample data
    private static final Period SAMPLE_PERIOD = new Period(1, 2, 3, 4, 5, 6, 7, 8);
    private static final String SAMPLE_ISO = "P1Y2M3W4DT5H6M7.008S";

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    private PeriodFormatter isoFormatter;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(FIXED_NOW_2002_06_09);

        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);

        isoFormatter = ISOPeriodFormat.standard();
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);

        isoFormatter = null;
    }

    // Printing ----------------------------------------------------------------

    @Test
    public void print_simplePeriod_returnsIso8601() {
        assertEquals(SAMPLE_ISO, isoFormatter.print(SAMPLE_PERIOD));
    }

    @Test
    public void print_toStringBuffer_appendsFormattedText() {
        StringBuffer buf = new StringBuffer();
        isoFormatter.printTo(buf, SAMPLE_PERIOD);
        assertEquals(SAMPLE_ISO, buf.toString());
    }

    @Test
    public void print_toStringBuffer_nullPeriod_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> isoFormatter.printTo(new StringBuffer(), null));
    }

    @Test
    public void print_toWriter_writesFormattedText() {
        CharArrayWriter out = new CharArrayWriter();
        isoFormatter.printTo(out, SAMPLE_PERIOD);
        assertEquals(SAMPLE_ISO, out.toString());
    }

    @Test
    public void print_toWriter_nullPeriod_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> isoFormatter.printTo(new CharArrayWriter(), null));
    }

    // Configuration (locale, parse type, printer/parser presence) -------------

    @Test
    public void withLocale_and_getLocale_roundTrip() {
        PeriodFormatter fFrench = isoFormatter.withLocale(Locale.FRENCH);
        assertEquals(Locale.FRENCH, fFrench.getLocale());
        assertSame("with same locale should return same instance", fFrench, fFrench.withLocale(Locale.FRENCH));

        PeriodFormatter fNull = isoFormatter.withLocale(null);
        assertNull(fNull.getLocale());
        assertSame("with same null locale should return same instance", fNull, fNull.withLocale(null));
    }

    @Test
    public void withParseType_and_getParseType_roundTrip() {
        PeriodType dayTime = PeriodType.dayTime();
        PeriodFormatter fDayTime = isoFormatter.withParseType(dayTime);
        assertEquals(dayTime, fDayTime.getParseType());
        assertSame("with same parse type should return same instance", fDayTime, fDayTime.withParseType(dayTime));

        PeriodFormatter fNull = isoFormatter.withParseType(null);
        assertNull(fNull.getParseType());
        assertSame("with same null parse type should return same instance", fNull, fNull.withParseType(null));
    }

    @Test
    public void printerParser_combinations_behaveAsAdvertised() {
        PeriodFormatter full = new PeriodFormatter(isoFormatter.getPrinter(), isoFormatter.getParser());
        assertSame(isoFormatter.getPrinter(), full.getPrinter());
        assertSame(isoFormatter.getParser(), full.getParser());
        assertTrue(full.isPrinter());
        assertTrue(full.isParser());
        assertEquals(SAMPLE_ISO, full.print(SAMPLE_PERIOD));
        assertNotNull(full.parsePeriod(SAMPLE_ISO));

        PeriodFormatter printerOnly = new PeriodFormatter(isoFormatter.getPrinter(), null);
        assertSame(isoFormatter.getPrinter(), printerOnly.getPrinter());
        assertNull(printerOnly.getParser());
        assertTrue(printerOnly.isPrinter());
        assertFalse(printerOnly.isParser());
        assertEquals(SAMPLE_ISO, printerOnly.print(SAMPLE_PERIOD));
        assertThrows(UnsupportedOperationException.class, () -> printerOnly.parsePeriod(SAMPLE_ISO));

        PeriodFormatter parserOnly = new PeriodFormatter(null, isoFormatter.getParser());
        assertNull(parserOnly.getPrinter());
        assertSame(isoFormatter.getParser(), parserOnly.getParser());
        assertFalse(parserOnly.isPrinter());
        assertTrue(parserOnly.isParser());
        assertThrows(UnsupportedOperationException.class, () -> parserOnly.print(SAMPLE_PERIOD));
        assertNotNull(parserOnly.parsePeriod(SAMPLE_ISO));
    }

    // Parsing -----------------------------------------------------------------

    @Test
    public void parsePeriod_simpleIsoText_returnsExpectedPeriod() {
        assertEquals(SAMPLE_PERIOD, isoFormatter.parsePeriod(SAMPLE_ISO));
    }

    @Test
    public void parsePeriod_invalidText_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> isoFormatter.parsePeriod("ABC"));
    }

    @Test
    public void parsePeriod_withParseType_dayTime() {
        PeriodType dayTime = PeriodType.dayTime();
        Period expected = new Period(0, 0, 0, 4, 5, 6, 7, 8, dayTime);

        assertEquals(expected, isoFormatter.withParseType(dayTime).parsePeriod("P4DT5H6M7.008S"));
        assertThrows(IllegalArgumentException.class,
                () -> isoFormatter.withParseType(dayTime).parsePeriod("P3W4DT5H6M7.008S"));
    }

    @Test
    public void parseMutablePeriod_simpleIsoText_returnsExpectedMutablePeriod() {
        MutablePeriod expected = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals(expected, isoFormatter.parseMutablePeriod(SAMPLE_ISO));
    }

    @Test
    public void parseMutablePeriod_invalidText_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> isoFormatter.parseMutablePeriod("ABC"));
    }

    @Test
    public void parseInto_populatesTargetAndReturnsNewPosition() {
        MutablePeriod expected = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod result = new MutablePeriod();

        int newPos = isoFormatter.parseInto(result, SAMPLE_ISO, 0);
        assertEquals("Should return index immediately after parsed text", SAMPLE_ISO.length(), newPos);
        assertEquals(expected, result);
    }

    @Test
    public void parseInto_nullTarget_throwsIAE() {
        assertThrows(IllegalArgumentException.class, () -> isoFormatter.parseInto(null, SAMPLE_ISO, 0));
    }

    @Test
    public void parseInto_invalidText_returnsBitwiseComplementOfFailurePosition() {
        MutablePeriod result = new MutablePeriod();
        int failurePos = 0;
        int ret = isoFormatter.parseInto(result, "ABC", failurePos);
        assertEquals("Negative return is bitwise complement of failure position", ~failurePos, ret);
    }
}