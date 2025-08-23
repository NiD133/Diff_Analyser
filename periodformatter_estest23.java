package org.joda.time.format;

import org.junit.Test;

/**
 * This test case verifies the behavior of the PeriodFormatter when its
 * underlying parser is configured with a circular dependency.
 */
public class PeriodFormatter_ESTestTest23 {

    /**
     * Tests that a call to parsePeriod() throws a StackOverflowError when the
     * formatter is configured with a parser that recursively calls itself.
     *
     * <p>This scenario is created by setting up a chain of parsers where the
     * last one points back to the first, creating an infinite loop:</p>
     * <ol>
     *   <li>A {@link PeriodFormatter} is created with a {@code Separator} as its parser.</li>
     *   <li>A {@code DynamicWordBased} parser is created, which wraps the {@code PeriodFormatter}.</li>
     *   <li>The {@code Separator}'s internal parser is then set to be the {@code DynamicWordBased} parser.</li>
     * </ol>
     * <p>This results in the following recursive parsing chain:
     * {@code Formatter -> Separator -> DynamicWordBased -> Formatter -> ...}</p>
     */
    @Test(expected = StackOverflowError.class)
    public void parsePeriodWithCircularParserDependencyThrowsStackOverflowError() {
        // --- Arrange: Create a circular dependency in the parser structure ---

        // A dummy component that does nothing, used as a placeholder printer/parser.
        PeriodFormatterBuilder.Literal dummyComponent = PeriodFormatterBuilder.Literal.EMPTY;

        // A separator that will be part of the recursive loop.
        // It is initially configured with the dummy component as its parser.
        PeriodFormatterBuilder.Separator recursiveSeparator = new PeriodFormatterBuilder.Separator(
                "", "", null, dummyComponent, dummyComponent, true, true);

        // The main formatter whose parser is the separator.
        PeriodFormatter formatterWithCircularParser = new PeriodFormatter(dummyComponent, recursiveSeparator);

        // A wrapper parser that contains the main formatter.
        PeriodParser wrapperParser = new PeriodFormat.DynamicWordBased(formatterWithCircularParser);

        // Complete the circular dependency by setting the separator's internal parser to be the wrapper.
        // This creates the infinite recursion.
        recursiveSeparator.finish(dummyComponent, wrapperParser);

        // --- Act & Assert ---
        // Attempt to parse a string, which will trigger the infinite recursion.
        // The @Test(expected=...) annotation asserts that a StackOverflowError is thrown.
        formatterWithCircularParser.parsePeriod("");
    }
}