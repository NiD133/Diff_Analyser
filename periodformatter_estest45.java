package org.joda.time.format;

import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Contains an improved test case for PeriodFormatter, focusing on understandability.
 * The original test was auto-generated and difficult to comprehend.
 */
public class PeriodFormatter_ESTestTest45 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that a custom formatter, configured to produce empty output for zero periods,
     * correctly handles a round trip of parsing an empty string and then printing
     * the resulting period.
     */
    @Test(timeout = 4000)
    public void testRoundTripOfEmptyStringWithCustomFormatter() {
        // Arrange: Create a highly specific, custom formatter using low-level components.
        // This setup is designed to parse an empty string into a zero-length period
        // and print a zero-length period back into an empty string.
        PeriodFormatterBuilder.SimpleAffix emptyAffix = new PeriodFormatterBuilder.SimpleAffix("");
        PeriodFormatterBuilder.CompositeAffix emptyCompositeAffix = new PeriodFormatterBuilder.CompositeAffix(emptyAffix, emptyAffix);

        // The FieldFormatter is configured with specific magic numbers (e.g., 11, 1350, -1158)
        // that define its behavior for an edge case, likely discovered by automated test generation.
        // This component acts as both the printer and the parser.
        PeriodFormatterBuilder.FieldFormatter customFormatterComponent = new PeriodFormatterBuilder.FieldFormatter(
                11, 11, 1350, true, -1158, null, emptyCompositeAffix, emptyAffix);

        PeriodFormatter formatter = new PeriodFormatter(
                customFormatterComponent, customFormatterComponent, Locale.GERMANY, PeriodType.time());

        // Act: 
        // 1. Parse an empty string, which should result in a zero-length period.
        MutablePeriod parsedPeriod = formatter.parseMutablePeriod("");
        // 2. Print that period back to a string.
        String resultString = formatter.print(parsedPeriod);

        // Assert: The final printed string should also be empty, completing the round trip.
        assertEquals("The printed representation of the parsed empty string should be empty.", "", resultString);
    }
}