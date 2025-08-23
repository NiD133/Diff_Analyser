package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.util.Locale;
import org.joda.time.PeriodType;

/**
 * Unit tests for the {@link PeriodFormatter} class.
 */
public class PeriodFormatterTest {

    /**
     * Tests that the getParser() method correctly returns the parser instance
     * that was provided during the formatter's construction.
     */
    @Test(timeout = 4000)
    public void getParser_shouldReturnTheParserProvidedInConstructor() {
        // Arrange: Create a dummy parser and a formatter instance.
        // We use PeriodFormatterBuilder.FieldFormatter as a convenient test double
        // because it implements both PeriodPrinter and PeriodParser.
        // The specific constructor arguments for it are not relevant to this test.
        PeriodFormatterBuilder.FieldFormatter dummyParserPrinter = new PeriodFormatterBuilder.FieldFormatter(
                1, 1, 1, true, 1,
                new PeriodFormatterBuilder.FieldFormatter[0],
                new PeriodFormatterBuilder.PluralAffix("s", "p"),
                new PeriodFormatterBuilder.PluralAffix("s", "p")
        );

        PeriodPrinter printer = dummyParserPrinter;
        PeriodParser expectedParser = dummyParserPrinter;
        Locale locale = Locale.GERMANY;
        PeriodType periodType = PeriodType.standard(); // Any valid PeriodType will suffice.

        PeriodFormatter formatter = new PeriodFormatter(printer, expectedParser, locale, periodType);

        // Act: Retrieve the parser from the formatter.
        PeriodParser actualParser = formatter.getParser();

        // Assert: The retrieved parser should be the exact same instance.
        assertSame("getParser() should return the same instance provided to the constructor.",
                   expectedParser, actualParser);
    }
}