package org.joda.time.format;

import org.joda.time.PeriodType;
import org.junit.Test;

import java.util.Locale;

/**
 * Tests for {@link PeriodFormatter}, focusing on specific parsing scenarios.
 */
public class PeriodFormatterTest {

    /**
     * Verifies that attempting to parse with a recursively defined formatter
     * correctly results in a StackOverflowError.
     * <p>
     * A circular dependency is intentionally created where a PeriodFormatter's parser
     * indirectly calls itself, leading to infinite recursion during the parsing process.
     */
    @Test(expected = StackOverflowError.class)
    public void parseMutablePeriodWithRecursiveParserShouldThrowStackOverflowError() {
        // Arrange: Create a formatter with a parser that recursively calls itself.

        // 1. Define a base literal component that does nothing. It serves as a placeholder.
        PeriodFormatterBuilder.Literal emptyLiteral = PeriodFormatterBuilder.Literal.EMPTY;

        // 2. Create a separator. This component will be made recursive later.
        String[] separatorVariants = new String[]{"", "qn", "qn", "qn", "qn"};
        PeriodFormatterBuilder.Separator recursiveSeparator = new PeriodFormatterBuilder.Separator(
                "qn", "", separatorVariants, emptyLiteral, emptyLiteral, true, false);

        // 3. Create a formatter that uses the separator as its parser.
        PeriodFormatter formatterWithRecursiveParser = new PeriodFormatter(
                emptyLiteral, recursiveSeparator, Locale.ENGLISH, PeriodType.yearDayTime());

        // 4. Create a dynamic parser that wraps the base formatter.
        PeriodParser dynamicParser = new PeriodFormat.DynamicWordBased(formatterWithRecursiveParser);

        // 5. This is the crucial step that creates the circular dependency.
        // We configure the separator (from step 2) to use the dynamic parser (from step 4).
        // The parsing chain is now:
        // formatter -> recursiveSeparator -> dynamicParser -> formatter -> ...
        recursiveSeparator.finish(emptyLiteral, dynamicParser);

        // Act: Attempt to parse a string with the formatter.
        // This will trigger the infinite recursion.
        formatterWithRecursiveParser.parseMutablePeriod("qn");

        // Assert: The expected StackOverflowError is verified by the @Test annotation.
    }
}