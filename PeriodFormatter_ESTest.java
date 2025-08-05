import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.format.PeriodParser;
import org.joda.time.format.PeriodPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A refactored and more understandable test suite for PeriodFormatter.
 */
class PeriodFormatterTest {

    private static final Period P1Y2M3W4D = new Period(1, 2, 3, 4, 5, 6, 7, 8);
    private static final Period EMPTY_PERIOD = new Period();
    private static final PeriodType YEARS_MONTHS = PeriodType.yearMonth();

    private PeriodFormatter yearsAndMonthsFormatter;

    @BeforeEach
    void setUp() {
        yearsAndMonthsFormatter = new PeriodFormatterBuilder()
                .appendYears()
                .appendSuffix(" year", " years")
                .appendSeparator(", ")
                .appendMonths()
                .appendSuffix(" month", " months")
                .toFormatter();
    }

    @Nested
    class StateAndConfiguration {

        @Test
        void isPrinter_returnsTrueIfPrinterIsSet() {
            // Arrange
            PeriodFormatter printerOnly = new PeriodFormatter(yearsAndMonthsFormatter.getPrinter(), null);
            PeriodFormatter noPrinter = new PeriodFormatter(null, yearsAndMonthsFormatter.getParser());

            // Assert
            assertTrue(printerOnly.isPrinter());
            assertFalse(noPrinter.isPrinter());
        }

        @Test
        void isParser_returnsTrueIfParserIsSet() {
            // Arrange
            PeriodFormatter parserOnly = new PeriodFormatter(null, yearsAndMonthsFormatter.getParser());
            PeriodFormatter noParser = new PeriodFormatter(yearsAndMonthsFormatter.getPrinter(), null);

            // Assert
            assertTrue(parserOnly.isParser());
            assertFalse(noParser.isParser());
        }

        @Test
        void withLocale_whenLocaleIsDifferent_returnsNewInstance() {
            // Arrange
            PeriodFormatter formatter = new PeriodFormatterBuilder().toFormatter().withLocale(Locale.ENGLISH);

            // Act
            PeriodFormatter newFormatter = formatter.withLocale(Locale.FRENCH);

            // Assert
            assertNotSame(formatter, newFormatter);
            assertEquals(Locale.FRENCH, newFormatter.getLocale());
        }

        @Test
        void withLocale_whenLocaleIsUnchanged_returnsSameInstance() {
            // Arrange
            PeriodFormatter formatter = new PeriodFormatterBuilder().toFormatter().withLocale(Locale.ENGLISH);

            // Act
            PeriodFormatter sameFormatter = formatter.withLocale(Locale.ENGLISH);

            // Assert
            assertSame(formatter, sameFormatter);
        }

        @Test
        void withParseType_whenTypeIsDifferent_returnsNewInstance() {
            // Arrange
            PeriodFormatter formatter = new PeriodFormatterBuilder().toFormatter(); // Has null parse type

            // Act
            PeriodFormatter newFormatter = formatter.withParseType(YEARS_MONTHS);

            // Assert
            assertNotSame(formatter, newFormatter);
            assertEquals(YEARS_MONTHS, newFormatter.getParseType());
        }

        @Test
        void withParseType_whenTypeIsUnchanged_returnsSameInstance() {
            // Arrange
            PeriodFormatter formatter = new PeriodFormatterBuilder().toFormatter().withParseType(YEARS_MONTHS);

            // Act
            PeriodFormatter sameFormatter = formatter.withParseType(YEARS_MONTHS);

            // Assert
            assertSame(formatter, sameFormatter);
        }
    }

    @Nested
    class Printing {

        @Test
        void print_formatsPeriodCorrectly() {
            // Arrange
            Period period = new Period(2, 3, 0, 0, 0, 0, 0, 0);

            // Act
            String result = yearsAndMonthsFormatter.print(period);

            // Assert
            assertEquals("2 years, 3 months", result);
        }

        @Test
        void print_withNullPeriod_throwsIllegalArgumentException() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                yearsAndMonthsFormatter.print(null);
            });
        }

        @Test
        void print_whenPrinterNotSet_throwsUnsupportedOperationException() {
            // Arrange
            PeriodFormatter parserOnly = new PeriodFormatter(null, yearsAndMonthsFormatter.getParser());

            // Act & Assert
            assertThrows(UnsupportedOperationException.class, () -> {
                parserOnly.print(P1Y2M3W4D);
            });
        }

        @Test
        void printToWriter_formatsPeriodCorrectly() throws IOException {
            // Arrange
            StringWriter writer = new StringWriter();
            Period period = new Period(1, 1, 0, 0, 0, 0, 0, 0);

            // Act
            yearsAndMonthsFormatter.printTo(writer, period);

            // Assert
            assertEquals("1 year, 1 month", writer.toString());
        }

        @Test
        void printToWriter_withNullWriter_throwsNullPointerException() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                yearsAndMonthsFormatter.printTo((Writer) null, P1Y2M3W4D);
            });
        }

        @Test
        void printToWriter_withNullPeriod_throwsIllegalArgumentException() {
            // Arrange
            StringWriter writer = new StringWriter();
            
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                yearsAndMonthsFormatter.printTo(writer, null);
            });
        }

        @Test
        void printToBuffer_formatsPeriodCorrectly() {
            // Arrange
            StringBuffer buffer = new StringBuffer();
            Period period = new Period(0, 5, 0, 0, 0, 0, 0, 0);

            // Act
            yearsAndMonthsFormatter.printTo(buffer, period);

            // Assert
            assertEquals("5 months", buffer.toString());
        }
    }

    @Nested
    class Parsing {

        @Test
        void parsePeriod_parsesStringCorrectly() {
            // Arrange
            String periodString = "2 years, 3 months";
            Period expectedPeriod = new Period(2, 3, 0, 0, 0, 0, 0, 0);

            // Act
            Period result = yearsAndMonthsFormatter.parsePeriod(periodString);

            // Assert
            assertEquals(expectedPeriod, result);
        }

        @Test
        void parsePeriod_withInvalidFormat_throwsIllegalArgumentException() {
            // Arrange
            String invalidString = "invalid period";

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                yearsAndMonthsFormatter.parsePeriod(invalidString);
            });
        }
        
        @Test
        void parsePeriod_withNullString_throwsNullPointerException() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                yearsAndMonthsFormatter.parsePeriod(null);
            });
        }

        @Test
        void parsePeriod_whenParserNotSet_throwsUnsupportedOperationException() {
            // Arrange
            PeriodFormatter printerOnly = new PeriodFormatter(yearsAndMonthsFormatter.getPrinter(), null);

            // Act & Assert
            assertThrows(UnsupportedOperationException.class, () -> {
                printerOnly.parsePeriod("1 year");
            });
        }

        @Test
        void parseMutablePeriod_parsesStringCorrectly() {
            // Arrange
            String periodString = "10 years";
            MutablePeriod expectedPeriod = new MutablePeriod(10, 0, 0, 0, 0, 0, 0, 0);

            // Act
            MutablePeriod result = yearsAndMonthsFormatter.parseMutablePeriod(periodString);

            // Assert
            assertEquals(expectedPeriod, result);
        }

        @Test
        void parseInto_parsesStringAndReturnsNewPosition() {
            // Arrange
            MutablePeriod period = new MutablePeriod();
            String text = "5 years, 3 months remaining";
            
            // Act
            int newPosition = yearsAndMonthsFormatter.parseInto(period, text, 0);

            // Assert
            assertEquals(new Period(5, 3, 0, 0, 0, 0, 0, 0), period.toPeriod());
            assertEquals("5 years, 3 months".length(), newPosition);
        }

        @Test
        void parseInto_withInvalidFormat_returnsNegativePosition() {
            // Arrange
            MutablePeriod period = new MutablePeriod();
            String text = "invalid text";

            // Act
            int failurePosition = yearsAndMonthsFormatter.parseInto(period, text, 0);

            // Assert
            assertEquals(~0, failurePosition);
            assertEquals(EMPTY_PERIOD, period.toPeriod()); // Period should be unchanged
        }
        
        @Test
        void parseInto_withNullPeriod_throwsIllegalArgumentException() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                yearsAndMonthsFormatter.parseInto(null, "1 year", 0);
            });
        }
        
        @Test
        void parseInto_withNegativePosition_throwsIndexOutOfBoundsException() {
            // Arrange
            MutablePeriod period = new MutablePeriod();
            
            // Act & Assert
            assertThrows(StringIndexOutOfBoundsException.class, () -> {
                yearsAndMonthsFormatter.parseInto(period, "1 year", -1);
            });
        }
    }
}