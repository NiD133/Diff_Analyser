package org.joda.time.format;

import org.joda.time.Period;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This test class focuses on the parsing capabilities of PeriodFormatter.
 * The original test was auto-generated and has been improved for clarity.
 */
public class PeriodFormatter_ESTestTest5 {

    @Test
    public void parsePeriod_withLiteralOnlyFormatter_shouldReturnZeroPeriodWhenTextMatches() {
        // Arrange
        final String literalText = "TEXT";

        // A formatter component that only matches a specific literal string.
        // It can be used as both a printer and a parser.
        PeriodFormatterBuilder.Literal literalComponent = new PeriodFormatterBuilder.Literal(literalText);

        // Create a formatter that is configured to only print and parse the literal text.
        PeriodFormatter formatter = new PeriodFormatter(literalComponent, literalComponent);

        // Act
        Period result = formatter.parsePeriod(literalText);

        // Assert
        assertNotNull("The parsed period should not be null.", result);
        
        // Parsing a string that consists only of a literal, with no actual period fields
        // (like years, days, hours), should result in a zero-length period.
        assertEquals("A literal-only format should parse to a zero period.", Period.ZERO, result);
    }
}