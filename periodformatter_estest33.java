package org.joda.time.format;

import org.joda.time.PeriodType;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertNull;

/**
 * This test class contains an improved version of a test for the PeriodFormatter.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class PeriodFormatter_ESTestTest33 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that getLocale() returns null when the PeriodFormatter is constructed with a null Locale.
     */
    @Test
    public void getLocale_shouldReturnNull_whenFormatterIsConstructedWithNullLocale() {
        // Arrange: Create a formatter with a null Locale.
        // The printer, parser, and period type are required for the constructor,
        // but their specific values are not relevant to this test's outcome.
        PeriodPrinter dummyPrinter = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodParser dummyParser = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodType dummyPeriodType = PeriodType.standard();

        PeriodFormatter formatter = new PeriodFormatter(dummyPrinter, dummyParser, null, dummyPeriodType);

        // Act: Get the locale from the formatter.
        Locale actualLocale = formatter.getLocale();

        // Assert: The retrieved locale should be null, as specified during construction.
        assertNull("The locale should be null as it was set to null in the constructor.", actualLocale);
    }
}