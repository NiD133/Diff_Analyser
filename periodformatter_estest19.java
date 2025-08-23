package org.joda.time.format;

import org.joda.time.PeriodType;
import org.joda.time.ReadablePeriod;
import org.joda.time.Years;
import org.junit.Test;

import java.util.Locale;

public class PeriodFormatter_ESTestTest19 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * This test verifies that the print method fails with a StackOverflowError
     * when the PeriodFormatter is configured with a recursive (self-referential) printer.
     * This is a robustness test against pathological configurations.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void printWithRecursiveFormatterThrowsStackOverflowError() {
        // Arrange: Create a formatter with a circular definition.
        String separatorText = "recursive-separator";
        PeriodFormatterBuilder.Literal dummyElement = new PeriodFormatterBuilder.Literal(separatorText);
        String[] separatorTextVariants = {separatorText};

        // A separator is designed to join other printer/parser components.
        PeriodFormatterBuilder.Separator recursiveSeparator = new PeriodFormatterBuilder.Separator(
                separatorText,
                separatorText,
                separatorTextVariants,
                dummyElement, // The printer component to be "separated"
                dummyElement, // The parser component to be "separated"
                false,
                false
        );

        // This is the crucial step: create a circular reference by telling the
        // separator to use *itself* as the component to be separated. When the formatter
        // tries to print, the separator will call itself infinitely.
        recursiveSeparator.finish(recursiveSeparator, recursiveSeparator);

        // Build the final formatter using this self-referential separator.
        PeriodFormatter formatter = new PeriodFormatter(
                recursiveSeparator, // printer
                recursiveSeparator, // parser
                Locale.GERMANY,
                PeriodType.standard()
        );

        ReadablePeriod anyPeriod = Years.ONE;

        // Act: Attempt to print a period using the recursive formatter.
        // The @Test(expected = StackOverflowError.class) annotation handles the assertion.
        formatter.print(anyPeriod);
    }
}