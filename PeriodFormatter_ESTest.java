package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Test suite for PeriodFormatter functionality including printing, parsing,
 * locale handling, and error conditions.
 */
public class PeriodFormatterTest {

    // Test Data Constants
    private static final String LITERAL_TEXT = "PeriodFormat.months.list";
    private static final String EMPTY_STRING = "";
    private static final Locale GERMAN_LOCALE = Locale.GERMANY;
    private static final Locale KOREAN_LOCALE = Locale.KOREA;

    // Helper Methods
    private PeriodFormatter createLiteralFormatter(String literalText) {
        PeriodFormatterBuilder.Literal literal = new PeriodFormatterBuilder.Literal(literalText);
        return new PeriodFormatter(literal, literal);
    }

    private PeriodFormatter createEmptyFormatter() {
        PeriodFormatterBuilder.Literal emptyLiteral = PeriodFormatterBuilder.Literal.EMPTY;
        return new PeriodFormatter(emptyLiteral, emptyLiteral);
    }

    // Printing Tests
    @Test
    public void shouldPrintPeriodToWriter() throws IOException {
        // Given: A formatter with field formatting capabilities
        PeriodFormatterBuilder.FieldFormatter fieldFormatter = createFieldFormatter();
        PeriodType secondsType = Seconds.standardSeconds(100).getPeriodType();
        PeriodFormatter formatter = new PeriodFormatter(fieldFormatter, fieldFormatter, GERMAN_LOCALE, secondsType);
        
        StringWriter writer = new StringWriter();
        Period testPeriod = new Period(0, 1818, Integer.MAX_VALUE, 0, 623158436, 0, 0, 0);

        // When: Printing period to writer
        formatter.printTo(writer, testPeriod);

        // Then: Formatter should support printing
        assertTrue("Formatter should support printing", formatter.isPrinter());
    }

    @Test
    public void shouldPrintLiteralText() {
        // Given: A formatter with literal text
        PeriodFormatter formatter = createLiteralFormatter(LITERAL_TEXT);
        Period zeroPeriod = new Period();
        Minutes zeroMinutes = zeroPeriod.toStandardMinutes();

        // When: Printing the period
        String result = formatter.print(zeroMinutes);

        // Then: Should return the literal text
        assertEquals("Should print literal text", LITERAL_TEXT, result);
    }

    @Test
    public void shouldThrowExceptionWhenPrintingNotSupported() {
        // Given: A formatter without printer capability
        PeriodFormatter formatter = new PeriodFormatter(null, createEmptyFormatter().getParser());
        Minutes testMinutes = Minutes.MIN_VALUE;

        // When & Then: Should throw UnsupportedOperationException
        try {
            formatter.print(testMinutes);
            fail("Should throw UnsupportedOperationException when printing not supported");
        } catch (UnsupportedOperationException e) {
            assertEquals("Printing not supported", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhenPrintingNullPeriod() {
        // Given: A valid formatter
        PeriodFormatter formatter = createEmptyFormatter();

        // When & Then: Should throw IllegalArgumentException for null period
        try {
            formatter.print(null);
            fail("Should throw IllegalArgumentException for null period");
        } catch (IllegalArgumentException e) {
            assertEquals("Period must not be null", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhenPrintingToNullBuffer() {
        // Given: A formatter and valid period
        PeriodFormatter formatter = createEmptyFormatter();
        Seconds maxSeconds = Seconds.MAX_VALUE;

        // When & Then: Should throw NullPointerException for null buffer
        try {
            formatter.printTo((StringBuffer) null, maxSeconds);
            fail("Should throw NullPointerException for null buffer");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // Parsing Tests
    @Test
    public void shouldParseLiteralText() {
        // Given: A formatter with specific literal text
        String testLiteral = "}=pKmkg&.wlkU)`)%`";
        PeriodFormatter formatter = createLiteralFormatter(testLiteral);

        // When: Parsing the exact literal text
        Period result = formatter.parsePeriod(testLiteral);

        // Then: Should successfully parse
        assertNotNull("Should successfully parse literal text", result);
    }

    @Test
    public void shouldParseIntoExistingPeriod() {
        // Given: An empty formatter and existing mutable period
        PeriodFormatter formatter = createEmptyFormatter();
        Weeks oneWeek = Weeks.weeks(1);
        Minutes weekInMinutes = oneWeek.toStandardMinutes();
        MutablePeriod mutablePeriod = weekInMinutes.toMutablePeriod();

        // When: Parsing into existing period
        int parsePosition = formatter.parseInto(mutablePeriod, "/M:TICzBQXt^", 0);

        // Then: Should return starting position (no parsing occurred)
        assertEquals("Should return starting position", 0, parsePosition);
    }

    @Test
    public void shouldHandleParsePositionBeyondStringLength() {
        // Given: A formatter with years type
        PeriodFormatterBuilder.Literal emptyLiteral = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodType yearsType = PeriodType.years();
        PeriodFormatter formatter = new PeriodFormatter(emptyLiteral, emptyLiteral, null, yearsType);
        MutablePeriod mutablePeriod = new MutablePeriod(0L, yearsType);

        // When: Parsing with position beyond string length
        int result = formatter.parseInto(mutablePeriod, ",6DaFIC#Q<", 10);

        // Then: Should return the position as-is
        assertEquals("Should return position beyond string length", 10, result);
    }

    @Test
    public void shouldThrowExceptionWhenParsingNotSupported() {
        // Given: A formatter without parser capability
        PeriodFormatter formatter = new PeriodFormatter(null, null);

        // When & Then: Should throw UnsupportedOperationException
        try {
            formatter.parsePeriod("year");
            fail("Should throw UnsupportedOperationException when parsing not supported");
        } catch (UnsupportedOperationException e) {
            assertEquals("Parsing not supported", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionForInvalidFormat() {
        // Given: A formatter expecting specific format
        PeriodFormatter formatter = createEmptyFormatter();

        // When & Then: Should throw IllegalArgumentException for invalid format
        try {
            formatter.parseMutablePeriod("MIT");
            fail("Should throw IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            assertTrue("Should indicate invalid format", e.getMessage().contains("Invalid format"));
        }
    }

    @Test
    public void shouldThrowExceptionWhenParsingNullString() {
        // Given: A valid formatter
        PeriodFormatter formatter = createEmptyFormatter();

        // When & Then: Should throw NullPointerException for null string
        try {
            formatter.parsePeriod(null);
            fail("Should throw NullPointerException for null string");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void shouldThrowExceptionWhenParsingIntoNullPeriod() {
        // Given: A valid formatter
        PeriodFormatter formatter = createEmptyFormatter();

        // When & Then: Should throw IllegalArgumentException for null period
        try {
            formatter.parseInto(null, null, -1402);
            fail("Should throw IllegalArgumentException for null period");
        } catch (IllegalArgumentException e) {
            assertEquals("Period must not be null", e.getMessage());
        }
    }

    // Locale Tests
    @Test
    public void shouldReturnSameFormatterWhenSettingSameLocale() {
        // Given: A formatter with specific locale
        PeriodType millisType = PeriodType.millis();
        PeriodFormatter formatter = new PeriodFormatter(null, null, KOREAN_LOCALE, millisType);

        // When: Setting the same locale
        PeriodFormatter result = formatter.withLocale(KOREAN_LOCALE);

        // Then: Should return the same instance
        assertSame("Should return same formatter for same locale", formatter, result);
    }

    @Test
    public void shouldReturnDifferentFormatterWhenSettingDifferentLocale() {
        // Given: A formatter with one locale
        PeriodFormatter formatter = createLiteralFormatter("test");

        // When: Setting a different locale
        PeriodFormatter result = formatter.withLocale(KOREAN_LOCALE);

        // Then: Should return a different instance
        assertNotSame("Should return different formatter for different locale", formatter, result);
    }

    @Test
    public void shouldReturnCorrectLocale() {
        // Given: A formatter with Korean locale
        PeriodType secondsType = PeriodType.seconds();
        PeriodFormatter formatter = new PeriodFormatter(null, null, Locale.KOREAN, secondsType);

        // When: Getting the locale
        Locale result = formatter.getLocale();

        // Then: Should return Korean locale
        assertEquals("Should return Korean locale", "ko", result.getLanguage());
    }

    @Test
    public void shouldReturnNullLocaleWhenNotSet() {
        // Given: A formatter without locale
        PeriodType yearsType = PeriodType.years();
        PeriodFormatter formatter = new PeriodFormatter(null, null, null, yearsType);

        // When: Getting the locale
        Locale result = formatter.getLocale();

        // Then: Should return null
        assertNull("Should return null when locale not set", result);
    }

    // Parse Type Tests
    @Test
    public void shouldReturnSameFormatterWhenSettingSameParseType() {
        // Given: A formatter with specific parse type
        PeriodType millisType = PeriodType.millis();
        PeriodFormatter formatter = new PeriodFormatter(null, null, KOREAN_LOCALE, millisType);

        // When: Setting the same parse type
        PeriodFormatter result = formatter.withParseType(millisType);

        // Then: Should return the same instance
        assertSame("Should return same formatter for same parse type", formatter, result);
    }

    @Test
    public void shouldReturnDifferentFormatterWhenSettingDifferentParseType() {
        // Given: A formatter without parse type
        PeriodFormatter formatter = new PeriodFormatter(null, null, Locale.US, null);
        PeriodType yearsType = PeriodType.years();

        // When: Setting a parse type
        PeriodFormatter result = formatter.withParseType(yearsType);

        // Then: Should return a different instance
        assertNotSame("Should return different formatter for different parse type", formatter, result);
    }

    @Test
    public void shouldReturnCorrectParseType() {
        // Given: A formatter with years parse type
        PeriodType yearsType = PeriodType.years();
        PeriodFormatter formatter = new PeriodFormatter(null, null, null, yearsType);

        // When: Getting the parse type
        PeriodType result = formatter.getParseType();

        // Then: Should return years type with size 1
        assertEquals("Should return years type", 1, result.size());
    }

    @Test
    public void shouldReturnNullParseTypeWhenNotSet() {
        // Given: A formatter without parse type
        PeriodFormatter formatter = new PeriodFormatter(null, null);

        // When: Getting the parse type
        PeriodType result = formatter.getParseType();

        // Then: Should return null
        assertNull("Should return null when parse type not set", result);
    }

    // Capability Tests
    @Test
    public void shouldReturnTrueForIsPrinterWhenPrinterExists() {
        // Given: A formatter with printer capability
        PeriodFormatter formatter = createLiteralFormatter("test");

        // When & Then: Should return true for isPrinter
        assertTrue("Should return true when printer exists", formatter.isPrinter());
    }

    @Test
    public void shouldReturnFalseForIsPrinterWhenPrinterNull() {
        // Given: A formatter without printer capability
        PeriodFormatter formatter = new PeriodFormatter(null, createEmptyFormatter().getParser());

        // When & Then: Should return false for isPrinter
        assertFalse("Should return false when printer is null", formatter.isPrinter());
    }

    @Test
    public void shouldReturnTrueForIsParserWhenParserExists() {
        // Given: A formatter with parser capability
        PeriodType minutesType = PeriodType.minutes();
        PeriodFormatter formatter = new PeriodFormatter(null, createEmptyFormatter().getParser(), Locale.US, minutesType);

        // When & Then: Should return true for isParser
        assertTrue("Should return true when parser exists", formatter.isParser());
    }

    @Test
    public void shouldReturnFalseForIsParserWhenParserNull() {
        // Given: A formatter without parser capability
        PeriodFormatter formatter = new PeriodFormatter(null, null, Locale.SIMPLIFIED_CHINESE, PeriodType.minutes());

        // When & Then: Should return false for isParser
        assertFalse("Should return false when parser is null", formatter.isParser());
    }

    // Getter Tests
    @Test
    public void shouldReturnNullPrinterWhenNotSet() {
        // Given: A formatter without printer
        PeriodFormatter formatter = new PeriodFormatter(null, null);

        // When: Getting the printer
        // Then: Should return null
        assertNull("Should return null when printer not set", formatter.getPrinter());
    }

    @Test
    public void shouldReturnNullParserWhenNotSet() {
        // Given: A formatter without parser
        PeriodFormatter formatter = new PeriodFormatter(createEmptyFormatter().getPrinter(), null);

        // When: Getting the parser
        // Then: Should return null
        assertNull("Should return null when parser not set", formatter.getParser());
    }

    // Integration Tests
    @Test
    public void shouldSuccessfullyRoundTripParseAndPrint() {
        // Given: A formatter capable of both parsing and printing
        PeriodFormatterBuilder.SimpleAffix emptyAffix = new PeriodFormatterBuilder.SimpleAffix(EMPTY_STRING);
        PeriodFormatterBuilder.CompositeAffix compositeAffix = new PeriodFormatterBuilder.CompositeAffix(emptyAffix, emptyAffix);
        PeriodFormatterBuilder.FieldFormatter fieldFormatter = new PeriodFormatterBuilder.FieldFormatter(
            11, 11, 1350, true, -1158, null, compositeAffix, emptyAffix);
        
        PeriodFormatter formatter = new PeriodFormatter(fieldFormatter, fieldFormatter, GERMAN_LOCALE, PeriodType.time());

        // When: Parsing and then printing
        MutablePeriod parsed = formatter.parseMutablePeriod(EMPTY_STRING);
        String printed = formatter.print(parsed);

        // Then: Should successfully round-trip
        assertEquals("Should successfully round-trip empty string", EMPTY_STRING, printed);
    }

    @Test
    public void shouldHandleComplexParsingScenarios() {
        // Given: A formatter and existing period
        PeriodFormatter formatter = createEmptyFormatter();
        MutablePeriod period = MutablePeriod.parse(EMPTY_STRING, formatter);

        // When: Parsing into period with position beyond string length
        int result = formatter.parseInto(period, EMPTY_STRING, 514);

        // Then: Should return negative complement of position
        assertEquals("Should return negative complement", -515, result);
    }

    // Helper method to create a field formatter for testing
    private PeriodFormatterBuilder.FieldFormatter createFieldFormatter() {
        PeriodFormatterBuilder.FieldFormatter[] emptyArray = new PeriodFormatterBuilder.FieldFormatter[0];
        PeriodFormatterBuilder.PluralAffix pluralAffix = new PeriodFormatterBuilder.PluralAffix(
            "Parsing not supported", "Parsing not supported");
        
        return new PeriodFormatterBuilder.FieldFormatter(
            Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 
            true, 2078, emptyArray, pluralAffix, pluralAffix);
    }
}