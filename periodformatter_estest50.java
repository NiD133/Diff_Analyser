package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Locale;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.ReadablePeriod;

public class PeriodFormatter_ESTestTest50 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that a custom formatter, built with an apparently invalid field type,
     * can parse an empty string into a zero-length period and subsequently print
     * that period back into an empty string. This verifies a specific edge case
     * in the formatter's round-trip behavior.
     */
    @Test
    public void testRoundTripOfEmptyPeriodWithCustomFormatter() {
        // Arrange: Create a highly specific formatter.
        // The key is the FieldFormatter constructed with an invalid field type (-1158),
        // which causes it to parse an empty string into a zero period and print
        // a zero period as an empty string.
        PeriodFormatterBuilder.SimpleAffix emptyAffix = new PeriodFormatterBuilder.SimpleAffix("");
        PeriodFormatterBuilder.CompositeAffix emptyCompositeAffix = new PeriodFormatterBuilder.CompositeAffix(emptyAffix, emptyAffix);

        // This FieldFormatter uses an invalid field type, which is central to the test's behavior.
        final int invalidFieldType = -1158;
        PeriodFormatterBuilder.FieldFormatter customFieldFormatter = new PeriodFormatterBuilder.FieldFormatter(
                11,   // minPrintedDigits
                11,   // printZeroSetting
                1350, // maxParsedDigits
                true, // rejectSignedValues
                invalidFieldType,
                null, // otherFormatters
                emptyCompositeAffix,
                emptyAffix
        );

        PeriodFormatter formatter = new PeriodFormatter(
                customFieldFormatter, // printer
                customFieldFormatter, // parser
                Locale.GERMANY,
                PeriodType.time()
        );

        // Act: Parse an empty string and then print the resulting period.
        MutablePeriod parsedPeriod = formatter.parseMutablePeriod("");
        StringBuffer outputBuffer = new StringBuffer();
        formatter.printTo(outputBuffer, (ReadablePeriod) parsedPeriod);

        // Assert: The parsed period should be zero, and the printed output should be an empty string.
        assertEquals("Parsing an empty string should result in a zero-length period.",
                new MutablePeriod(), parsedPeriod);
        assertEquals("Printing a zero-length period with this custom formatter should produce an empty string.",
                "", outputBuffer.toString());
    }
}