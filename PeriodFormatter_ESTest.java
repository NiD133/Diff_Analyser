package org.joda.time.format;

import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Seconds;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for PeriodFormatter.
 * These tests avoid obscure builder internals and exercise the API in clear scenarios.
 */
public class PeriodFormatterTest {

    // Helper to build a simple literal-based formatter that can both print and parse.
    private static PeriodFormatter literalFormatter(String literal) {
        return new PeriodFormatterBuilder()
                .appendLiteral(literal)
                .toFormatter();
    }

    @Test
    public void printerAndParserArePresentForBuilderFormatter() {
        PeriodFormatter f = literalFormatter("X");
        assertTrue(f.isPrinter());
        assertTrue(f.isParser());
        assertNotNull(f.getPrinter());
        assertNotNull(f.getParser());
    }

    @Test
    public void isPrinterFalseWhenNoPrinter() {
        PeriodFormatter base = literalFormatter("x");
        PeriodFormatter f = new PeriodFormatter(null, base.getParser());
        assertFalse(f.isPrinter());
        assertTrue(f.isParser());
        assertNull(f.getPrinter());
        assertNotNull(f.getParser());
    }

    @Test
    public void isParserFalseWhenNoParser() {
        PeriodFormatter base = literalFormatter("x");
        PeriodFormatter f = new PeriodFormatter(base.getPrinter(), null);
        assertTrue(f.isPrinter());
        assertFalse(f.isParser());
        assertNotNull(f.getPrinter());
        assertNull(f.getParser());
    }

    @Test
    public void withLocaleReturnsNewInstanceOnChangeAndSameOnSame() {
        PeriodFormatter f = literalFormatter("X");

        PeriodFormatter english = f.withLocale(Locale.ENGLISH);
        assertNotSame(f, english);
        assertEquals(Locale.ENGLISH, english.getLocale());

        // Same locale returns same instance
        assertSame(english, english.withLocale(Locale.ENGLISH));

        // Switching back to null locale returns a new instance
        PeriodFormatter backToDefault = english.withLocale(null);
        assertNotSame(english, backToDefault);
        assertNull(backToDefault.getLocale());
    }

    @Test
    public void withParseTypeReturnsNewInstanceOnChangeAndSameOnSame() {
        PeriodFormatter f = literalFormatter("X");

        PeriodType dayTime = PeriodType.dayTime();
        PeriodType time = PeriodType.time();

        PeriodFormatter dt = f.withParseType(dayTime);
        assertNotSame(f, dt);
        assertEquals(dayTime, dt.getParseType());

        // Same type returns same instance
        assertSame(dt, dt.withParseType(dayTime));

        // Different type returns a new instance
        PeriodFormatter t = dt.withParseType(time);
        assertNotSame(dt, t);
        assertEquals(time, t.getParseType());
    }

    @Test
    public void printToAppendsLiteralToStringBuffer() {
        PeriodFormatter f = literalFormatter("X");
        StringBuilder sb = new StringBuilder();
        f.printTo(new StringBuffer(sb), Period.seconds(5));
        assertEquals("X", sb.toString());
    }

    @Test
    public void printToAppendsLiteralToWriter() throws IOException {
        PeriodFormatter f = literalFormatter("hello");
        StringWriter out = new StringWriter();
        f.printTo(out, Period.seconds(1));
        assertEquals("hello", out.toString());
    }

    @Test
    public void printThrowsWhenNoPrinter() {
        PeriodFormatter f = new PeriodFormatter(null, null);
        try {
            f.print(Period.seconds(1));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Printing not supported", e.getMessage());
        }
    }

    @Test
    public void printToThrowsWhenPeriodIsNull() {
        PeriodFormatter f = literalFormatter("X");
        try {
            f.printTo(new StringBuffer(), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Period must not be null", e.getMessage());
        }
    }

    @Test
    public void printToPropagatesIOExceptionFromWriter() {
        PeriodFormatter f = literalFormatter("X");
        Writer throwingWriter = new Writer() {
            @Override public void write(char[] cbuf, int off, int len) throws IOException { throw new IOException("boom"); }
            @Override public void flush() {}
            @Override public void close() {}
        };
        try {
            f.printTo(throwingWriter, Period.seconds(1));
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("boom", e.getMessage());
        }
    }

    @Test
    public void parsePeriodParsesMatchingLiteral() {
        PeriodFormatter f = literalFormatter("X");
        Period p = f.parsePeriod("X");
        assertNotNull(p);
        assertEquals(new Period(), p); // literal does not set any fields
    }

    @Test
    public void parsePeriodThrowsOnMismatch() {
        PeriodFormatter f = literalFormatter("X");
        try {
            f.parsePeriod("Y");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: \"Y\"", e.getMessage());
        }
    }

    @Test
    public void parsePeriodThrowsWhenNoParser() {
        PeriodFormatter f = new PeriodFormatter(null, null);
        try {
            f.parsePeriod("anything");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Parsing not supported", e.getMessage());
        }
    }

    @Test
    public void parseMutablePeriodThrowsOnNullText() {
        PeriodFormatter f = literalFormatter("X");
        try {
            f.parseMutablePeriod(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // Underlying parser will NPE on null text
        }
    }

    @Test
    public void parseIntoReturnsNewPositionOnSuccess() {
        PeriodFormatter f = literalFormatter("X");
        MutablePeriod mp = new MutablePeriod();
        int newPos = f.parseInto(mp, "XYZ", 0);
        assertEquals(1, newPos); // consumed one character literal
    }

    @Test
    public void parseIntoReturnsNegativeComplementOnFailure() {
        PeriodFormatter f = literalFormatter("X");
        MutablePeriod mp = new MutablePeriod();
        int result = f.parseInto(mp, "ABC", 0);
        assertTrue(result < 0);
        assertEquals(0, ~result); // failure at index 0
    }

    @Test
    public void parseIntoThrowsWhenPeriodIsNull() {
        PeriodFormatter f = literalFormatter("X");
        try {
            f.parseInto(null, "X", 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Period must not be null", e.getMessage());
        }
    }

    @Test
    public void immutabilityOfWithLocaleAndWithParseType() {
        PeriodFormatter f = literalFormatter("X");
        PeriodFormatter withLocale = f.withLocale(Locale.JAPANESE);
        PeriodFormatter withType = f.withParseType(PeriodType.yearDayTime());

        assertNotSame(f, withLocale);
        assertNotSame(f, withType);

        // Original remains usable and unchanged
        assertNull(f.getLocale());
        assertNull(f.getParseType());

        StringWriter w1 = new StringWriter();
        StringWriter w2 = new StringWriter();
        f.printTo(w1, Seconds.THREE.toPeriod());
        withLocale.printTo(w2, Seconds.THREE.toPeriod());
        assertEquals("X", w1.toString());
        assertEquals("X", w2.toString());
    }
}