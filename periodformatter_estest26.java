package org.joda.time.format;

import org.junit.Test;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.format.PeriodParser;
import org.joda.time.format.PeriodPrinter;

/**
 * This test class contains tests for the PeriodFormatter class.
 * This specific test case focuses on its exception-handling behavior.
 */
public class PeriodFormatter_ESTestTest26 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Verifies that calling parseMutablePeriod with a null input string
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void parseMutablePeriod_shouldThrowNullPointerException_whenInputIsNull() {
        // Arrange: Create a basic formatter. The internal parser implementation
        // is what will be invoked and is expected to handle the null check.
        PeriodPrinter printer = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodParser parser = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(printer, parser);

        // Act: Attempt to parse a null string.
        // The @Test(expected) annotation will handle the assertion.
        formatter.parseMutablePeriod(null);
    }
}