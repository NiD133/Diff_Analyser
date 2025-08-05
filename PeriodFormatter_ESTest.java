package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Locale;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadablePeriod;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class PeriodFormatterTest extends PeriodFormatterTestScaffolding {

    @Test(timeout = 4000)
    public void testPrintPeriodToWriter() throws Throwable {
        PeriodFormatterBuilder.FieldFormatter[] fieldFormatters = new PeriodFormatterBuilder.FieldFormatter[0];
        PeriodFormatterBuilder.PluralAffix pluralAffix = new PeriodFormatterBuilder.PluralAffix("Parsing not supported", "Parsing not supported");
        PeriodFormatterBuilder.FieldFormatter fieldFormatter = new PeriodFormatterBuilder.FieldFormatter(
                Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, true, 2078, fieldFormatters, pluralAffix, pluralAffix);
        Locale locale = Locale.GERMANY;
        StringWriter writer = new StringWriter(2078);
        Period period = new Period(0, 1818, Integer.MAX_VALUE, 0, 623158436, 0, 0, 0);
        Duration duration = Duration.standardMinutes(-1814L);
        Seconds seconds = duration.toStandardSeconds();
        PeriodType periodType = seconds.getPeriodType();
        PeriodFormatter formatter = new PeriodFormatter(fieldFormatter, fieldFormatter, locale, periodType);
        
        formatter.printTo(writer, period);
        
        assertTrue(formatter.isPrinter());
    }

    @Test(timeout = 4000)
    public void testFormatterWithSameParseType() throws Throwable {
        String[] separators = {"E", "E", "E", "E", "E", "E"};
        LinkedList<Object> components = new LinkedList<>();
        PeriodFormatterBuilder.Composite composite = new PeriodFormatterBuilder.Composite(components);
        PeriodFormatterBuilder.Separator separator = new PeriodFormatterBuilder.Separator("E", "E", separators, composite, composite, false, true);
        Locale locale = Locale.KOREA;
        PeriodType periodType = PeriodType.millis();
        PeriodFormatter formatter = new PeriodFormatter(separator, null, locale, periodType);
        
        PeriodFormatter sameFormatter = formatter.withParseType(periodType);
        
        assertSame(sameFormatter, formatter);
    }

    @Test(timeout = 4000)
    public void testFormatterWithSameLocale() throws Throwable {
        PeriodType periodType = PeriodType.minutes();
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        PeriodFormatter formatter = new PeriodFormatter(null, null, locale, periodType);
        
        PeriodFormatter sameFormatter = formatter.withLocale(locale);
        
        assertSame(formatter, sameFormatter);
    }

    @Test(timeout = 4000)
    public void testPrintLiteral() throws Throwable {
        PeriodFormatterBuilder.Literal literal = new PeriodFormatterBuilder.Literal("PeriodFormat.months.list");
        Locale locale = Locale.KOREA;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal, locale, null);
        Period period = new Period();
        Minutes minutes = period.toStandardMinutes();
        
        String result = formatter.print(minutes);
        
        assertEquals("PeriodFormat.months.list", result);
    }

    @Test(timeout = 4000)
    public void testParsePeriodWithLiteral() throws Throwable {
        PeriodFormatterBuilder.Literal literal = new PeriodFormatterBuilder.Literal("}=pKmkg&.wlkU)`)%`");
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        
        Period period = formatter.parsePeriod("}=pKmkg&.wlkU)`)%`");
        
        assertNotNull(period);
    }

    @Test(timeout = 4000)
    public void testParseIntoMutablePeriod() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        Weeks weeks = Weeks.weeks(1);
        Minutes minutes = weeks.toStandardMinutes();
        MutablePeriod mutablePeriod = minutes.toMutablePeriod();
        
        int result = formatter.parseInto(mutablePeriod, "/M:TICzBQXt^", 0);
        
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testParseIntoMutablePeriodWithOffset() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodType periodType = PeriodType.years();
        PeriodFormatter formatter = new PeriodFormatter(literal, literal, null, periodType);
        MutablePeriod mutablePeriod = new MutablePeriod(0L, periodType);
        
        int result = formatter.parseInto(mutablePeriod, ",6DaFIC#Q<", 10);
        
        assertEquals(10, result);
    }

    @Test(timeout = 4000)
    public void testGetPrinterWhenNull() throws Throwable {
        PeriodFormatter formatter = new PeriodFormatter(null, null);
        
        PeriodPrinter printer = formatter.getPrinter();
        
        assertNull(printer);
    }

    @Test(timeout = 4000)
    public void testGetPrinterFromComposite() throws Throwable {
        LinkedList<Object> components = new LinkedList<>();
        PeriodFormatterBuilder.Composite composite = new PeriodFormatterBuilder.Composite(components);
        PeriodFormatter formatter = new PeriodFormatter(composite, composite);
        
        PeriodPrinter printer = formatter.getPrinter();
        
        assertSame(printer, composite);
    }

    @Test(timeout = 4000)
    public void testGetParserFromFieldFormatter() throws Throwable {
        PeriodFormatterBuilder.FieldFormatter[] fieldFormatters = new PeriodFormatterBuilder.FieldFormatter[0];
        PeriodFormatterBuilder.PluralAffix pluralAffix = new PeriodFormatterBuilder.PluralAffix("Parsing not supported", "Parsing not supported");
        PeriodFormatterBuilder.FieldFormatter fieldFormatter = new PeriodFormatterBuilder.FieldFormatter(
                Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, true, 2078, fieldFormatters, pluralAffix, pluralAffix);
        Locale locale = Locale.GERMANY;
        Duration duration = Duration.standardMinutes(-1814L);
        Seconds seconds = duration.toStandardSeconds();
        PeriodType periodType = seconds.getPeriodType();
        PeriodFormatter formatter = new PeriodFormatter(fieldFormatter, fieldFormatter, locale, periodType);
        
        PeriodParser parser = formatter.getParser();
        
        assertSame(fieldFormatter, parser);
    }

    @Test(timeout = 4000)
    public void testGetParseTypeWithHoursRemoved() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        Locale locale = Locale.ENGLISH;
        Hours hours = Hours.TWO;
        PeriodType periodType = hours.getPeriodType();
        PeriodType periodTypeWithoutHours = periodType.withHoursRemoved();
        PeriodFormatter formatter = new PeriodFormatter(literal, literal, locale, periodTypeWithoutHours);
        
        PeriodType resultType = formatter.getParseType();
        
        assertEquals(0, resultType.size());
    }

    @Test(timeout = 4000)
    public void testGetParseTypeForYears() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodType periodType = PeriodType.years();
        PeriodFormatter formatter = new PeriodFormatter(literal, literal, null, periodType);
        
        PeriodType resultType = formatter.getParseType();
        
        assertEquals(1, resultType.size());
    }

    @Test(timeout = 4000)
    public void testGetLocaleForKorean() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        Locale locale = Locale.KOREAN;
        PeriodType periodType = PeriodType.seconds();
        PeriodFormatter formatter = new PeriodFormatter(literal, literal, locale, periodType);
        
        Locale resultLocale = formatter.getLocale();
        
        assertEquals("ko", resultLocale.getLanguage());
    }

    @Test(timeout = 4000)
    public void testUnsupportedPrintOperation() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(null, literal);
        StringBuffer buffer = new StringBuffer("");
        Minutes minutes = Minutes.MIN_VALUE;
        
        try {
            formatter.printTo(buffer, minutes);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Printing not supported", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnPrintTo() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        Seconds seconds = Seconds.MAX_VALUE;
        
        try {
            formatter.printTo(null, seconds);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIOExceptionOnPrintTo() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(null, literal);
        PipedWriter writer = new PipedWriter();
        Weeks weeks = Weeks.TWO;
        
        try {
            formatter.printTo(writer, weeks);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionOnPrintTo() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        StringWriter writer = new StringWriter();
        
        try {
            formatter.printTo(writer, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Period must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testIOExceptionOnPrintToWithMinutes() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        PipedWriter writer = new PipedWriter();
        Weeks weeks = Weeks.TWO;
        Minutes minutes = weeks.toStandardMinutes();
        
        try {
            formatter.printTo(writer, minutes);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testStackOverflowErrorOnPrint() throws Throwable {
        String[] separators = {"AJq6"};
        PeriodFormatterBuilder.Literal literal = new PeriodFormatterBuilder.Literal("AJq6");
        PeriodFormatterBuilder.Separator separator = new PeriodFormatterBuilder.Separator("AJq6", "AJq6", separators, literal, literal, false, false);
        Locale locale = Locale.GERMANY;
        Years years = Years.MAX_VALUE;
        PeriodType periodType = years.getPeriodType();
        separator.finish(separator, separator);
        PeriodFormatter formatter = new PeriodFormatter(separator, separator, locale, periodType);
        
        try {
            formatter.print(years);
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnPrint() throws Throwable {
        LinkedList<Object> components = new LinkedList<>();
        PeriodFormatterBuilder.Composite composite = new PeriodFormatterBuilder.Composite(components);
        PeriodFormatter formatter = new PeriodFormatter(composite, composite);
        Hours hours = Hours.MAX_VALUE;
        
        try {
            formatter.print(hours);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionOnPrint() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        
        try {
            formatter.print(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Period must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testUnsupportedOperationExceptionOnParsePeriod() throws Throwable {
        PeriodFormatter formatter = new PeriodFormatter(null, null);
        
        try {
            formatter.parsePeriod("year");
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Parsing not supported", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testStackOverflowErrorOnParsePeriod() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        String[] separators = {"", "", "", "", "", ""};
        PeriodFormatterBuilder.Separator separator = new PeriodFormatterBuilder.Separator("", "", separators, literal, literal, true, true);
        PeriodFormatter formatter = new PeriodFormatter(literal, separator);
        PeriodFormat.DynamicWordBased dynamicWordBased = new PeriodFormat.DynamicWordBased(formatter);
        separator.finish(literal, dynamicWordBased);
        
        try {
            formatter.parsePeriod("");
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnParsePeriod() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        
        try {
            formatter.parsePeriod(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testStackOverflowErrorOnParseMutablePeriod() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        String[] separators = {"", "qn", "qn", "qn", "qn"};
        PeriodFormatterBuilder.Separator separator = new PeriodFormatterBuilder.Separator("qn", "", separators, literal, literal, true, false);
        Locale locale = Locale.ENGLISH;
        PeriodType periodType = PeriodType.yearDayTime();
        PeriodFormatter formatter = new PeriodFormatter(literal, separator, locale, periodType);
        PeriodFormat.DynamicWordBased dynamicWordBased = new PeriodFormat.DynamicWordBased(formatter);
        separator.finish(literal, dynamicWordBased);
        
        try {
            formatter.parseMutablePeriod("qn");
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnParseMutablePeriod() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        
        try {
            formatter.parseMutablePeriod(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testUnsupportedOperationExceptionOnParseInto() throws Throwable {
        LinkedList<Object> components = new LinkedList<>();
        PeriodFormatterBuilder.Composite composite = new PeriodFormatterBuilder.Composite(components);
        PeriodFormatter formatter = new PeriodFormatter(composite, composite);
        MutablePeriod mutablePeriod = new MutablePeriod();
        
        try {
            formatter.parseInto(mutablePeriod, "", -711);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnParseInto() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        MutablePeriod mutablePeriod = new MutablePeriod();
        
        try {
            formatter.parseInto(mutablePeriod, null, 3121);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionOnParseInto() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        
        try {
            formatter.parseInto(null, null, -1402);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Period must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionOnParseMutablePeriod() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        
        try {
            formatter.parseMutablePeriod("MIT");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: \"MIT\"", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionOnParseMutablePeriodWithLiteral() throws Throwable {
        PeriodFormatterBuilder.Literal literal = new PeriodFormatterBuilder.Literal("PeriodFormat.months.list");
        Locale locale = Locale.KOREA;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal, locale, null);
        
        try {
            formatter.parseMutablePeriod("v:");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: \"v:\"", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetParserWhenNull() throws Throwable {
        LinkedList<Object> components = new LinkedList<>();
        PeriodFormatterBuilder.Composite composite = new PeriodFormatterBuilder.Composite(components);
        PeriodFormatter formatter = new PeriodFormatter(composite, null);
        
        PeriodParser parser = formatter.getParser();
        
        assertNull(parser);
    }

    @Test(timeout = 4000)
    public void testGetLocaleWhenNull() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodType periodType = PeriodType.years();
        PeriodFormatter formatter = new PeriodFormatter(literal, literal, null, periodType);
        
        Locale locale = formatter.getLocale();
        
        assertNull(locale);
    }

    @Test(timeout = 4000)
    public void testUnsupportedOperationExceptionOnParseMutablePeriod() throws Throwable {
        PeriodType periodType = PeriodType.minutes();
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        PeriodFormatter formatter = new PeriodFormatter(null, null, locale, periodType);
        
        try {
            formatter.parseMutablePeriod("aXDKT&");
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Parsing not supported", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionOnParsePeriodWithLiteral() throws Throwable {
        PeriodFormatterBuilder.Literal literal = new PeriodFormatterBuilder.Literal("\"");
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        
        try {
            formatter.parsePeriod("Parsing not supported");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: \"Parsing not supported\"", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionOnPrintToWithNullPeriod() throws Throwable {
        PeriodFormatterBuilder.Literal literal = new PeriodFormatterBuilder.Literal("");
        StringBuffer buffer = new StringBuffer("~eGa!VG)p");
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        
        try {
            formatter.printTo(buffer, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Period must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testUnsupportedOperationExceptionOnPrint() throws Throwable {
        PeriodFormatter formatter = new PeriodFormatter(null, null);
        Seconds seconds = Seconds.ZERO;
        
        try {
            formatter.print(seconds);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Printing not supported", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testFormatterWithSameParseTypeForYears() throws Throwable {
        LinkedList<Object> components = new LinkedList<>();
        PeriodFormatterBuilder.Composite composite = new PeriodFormatterBuilder.Composite(components);
        PeriodFormatter formatter = new PeriodFormatter(null, composite);
        PeriodType periodType = PeriodType.years();
        
        PeriodFormatter sameFormatter = formatter.withParseType(periodType);
        PeriodFormatter anotherSameFormatter = sameFormatter.withParseType(periodType);
        
        assertNotSame(anotherSameFormatter, formatter);
    }

    @Test(timeout = 4000)
    public void testFormatterWithSameLocaleForKorean() throws Throwable {
        PeriodFormatterBuilder.Literal literal = new PeriodFormatterBuilder.Literal("\"");
        Locale locale = Locale.KOREAN;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        Locale clonedLocale = (Locale) locale.clone();
        
        PeriodFormatter sameLocaleFormatter = formatter.withLocale(locale);
        PeriodFormatter anotherSameLocaleFormatter = sameLocaleFormatter.withLocale(clonedLocale);
        
        assertNotSame(anotherSameLocaleFormatter, formatter);
    }

    @Test(timeout = 4000)
    public void testFormatterWithNullLocale() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        Locale locale = Locale.JAPANESE;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal, locale, null);
        
        PeriodFormatter nullLocaleFormatter = formatter.withLocale(null);
        
        assertNotSame(nullLocaleFormatter, formatter);
    }

    @Test(timeout = 4000)
    public void testIsParserTrue() throws Throwable {
        PeriodType periodType = PeriodType.minutes();
        LinkedList<Object> components = new LinkedList<>();
        PeriodFormatterBuilder.Composite composite = new PeriodFormatterBuilder.Composite(components);
        Locale locale = Locale.US;
        PeriodFormatter formatter = new PeriodFormatter(null, composite, locale, periodType);
        
        boolean isParser = formatter.isParser();
        
        assertTrue(isParser);
    }

    @Test(timeout = 4000)
    public void testIsParserFalse() throws Throwable {
        PeriodType periodType = PeriodType.minutes();
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        PeriodFormatter formatter = new PeriodFormatter(null, null, locale, periodType);
        
        boolean isParser = formatter.isParser();
        
        assertFalse(isParser);
    }

    @Test(timeout = 4000)
    public void testIsPrinterTrue() throws Throwable {
        PeriodFormatterBuilder.Literal literal = new PeriodFormatterBuilder.Literal("\"");
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        
        boolean isPrinter = formatter.isPrinter();
        
        assertTrue(isPrinter);
    }

    @Test(timeout = 4000)
    public void testIsPrinterFalse() throws Throwable {
        LinkedList<Object> components = new LinkedList<>();
        PeriodFormatterBuilder.Composite composite = new PeriodFormatterBuilder.Composite(components);
        PeriodFormatter formatter = new PeriodFormatter(null, composite);
        
        boolean isPrinter = formatter.isPrinter();
        
        assertFalse(isPrinter);
    }

    @Test(timeout = 4000)
    public void testParseMutablePeriodAndPrint() throws Throwable {
        PeriodFormatterBuilder.SimpleAffix simpleAffix = new PeriodFormatterBuilder.SimpleAffix("");
        PeriodFormatterBuilder.CompositeAffix compositeAffix = new PeriodFormatterBuilder.CompositeAffix(simpleAffix, simpleAffix);
        PeriodFormatterBuilder.FieldFormatter fieldFormatter = new PeriodFormatterBuilder.FieldFormatter(
                11, 11, 1350, true, -1158, null, compositeAffix, simpleAffix);
        Locale locale = Locale.GERMANY;
        PeriodType periodType = PeriodType.time();
        PeriodFormatter formatter = new PeriodFormatter(fieldFormatter, fieldFormatter, locale, periodType);
        
        MutablePeriod mutablePeriod = formatter.parseMutablePeriod("");
        String result = formatter.print(mutablePeriod);
        
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testParseIntoMutablePeriodAndCheckPosition() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        MutablePeriod mutablePeriod = MutablePeriod.parse("", formatter);
        
        int result = formatter.parseInto(mutablePeriod, "", 514);
        
        assertEquals(-515, result);
    }

    @Test(timeout = 4000)
    public void testParseIntoMutablePeriodWithNegativeOffset() throws Throwable {
        PeriodFormatterBuilder.SimpleAffix simpleAffix = new PeriodFormatterBuilder.SimpleAffix("");
        PeriodFormatterBuilder.CompositeAffix compositeAffix = new PeriodFormatterBuilder.CompositeAffix(simpleAffix, simpleAffix);
        PeriodFormatterBuilder.FieldFormatter fieldFormatter = new PeriodFormatterBuilder.FieldFormatter(
                11, 11, 1350, true, -1158, null, compositeAffix, simpleAffix);
        Locale locale = Locale.GERMANY;
        PeriodType periodType = PeriodType.time();
        PeriodFormatter formatter = new PeriodFormatter(fieldFormatter, fieldFormatter, locale, periodType);
        
        MutablePeriod mutablePeriod = formatter.parseMutablePeriod("");
        
        try {
            formatter.parseInto(mutablePeriod, "LimitChronology[", -1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetParseTypeWhenNull() throws Throwable {
        PeriodFormatter formatter = new PeriodFormatter(null, null);
        
        PeriodType periodType = formatter.getParseType();
        
        assertNull(periodType);
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionOnPrintToWithNullWriter() throws Throwable {
        PeriodFormatterBuilder.Literal literal = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(literal, literal);
        Weeks weeks = Weeks.MIN_VALUE;
        
        try {
            formatter.printTo(null, weeks);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testPrintToStringBuffer() throws Throwable {
        PeriodFormatterBuilder.SimpleAffix simpleAffix = new PeriodFormatterBuilder.SimpleAffix("");
        PeriodFormatterBuilder.CompositeAffix compositeAffix = new PeriodFormatterBuilder.CompositeAffix(simpleAffix, simpleAffix);
        PeriodFormatterBuilder.FieldFormatter fieldFormatter = new PeriodFormatterBuilder.FieldFormatter(
                11, 11, 1350, true, -1158, null, compositeAffix, simpleAffix);
        Locale locale = Locale.GERMANY;
        PeriodType periodType = PeriodType.time();
        PeriodFormatter formatter = new PeriodFormatter(fieldFormatter, fieldFormatter, locale, periodType);
        
        MutablePeriod mutablePeriod = formatter.parseMutablePeriod("");
        StringBuffer buffer = new StringBuffer();
        formatter.printTo(buffer, mutablePeriod);
        
        assertEquals(0, buffer.length());
    }
}