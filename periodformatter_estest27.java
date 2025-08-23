package org.joda.time.format;

import org.junit.Test;
import org.joda.time.MutablePeriod;
import org.joda.time.format.PeriodFormatterBuilder.Composite;

import java.util.Collections;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

// The test class name is kept for consistency with the original file,
// but a more descriptive name like `PeriodFormatterParseTest` would be preferable in a real project.
public class PeriodFormatter_ESTestTest27 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Verifies that calling parseInto() on a PeriodFormatter that does not support parsing
     * throws an UnsupportedOperationException.
     */
    @Test
    public void parseInto_whenParserIsNotSupported_throwsUnsupportedOperationException() {
        // Arrange: Create a PeriodFormatter with a parser that is incapable of parsing.
        // A PeriodFormatterBuilder.Composite created with an empty list of components
        // acts as a non-functional parser.
        Composite unsupportedParser = new PeriodFormatterBuilder.Composite(Collections.emptyList());
        PeriodFormatter formatter = new PeriodFormatter(null, unsupportedParser);

        MutablePeriod period = new MutablePeriod();
        String anyText = "P1Y2M3D";
        int startPosition = 0;

        // Act & Assert
        try {
            formatter.parseInto(period, anyText, startPosition);
            fail("Expected an UnsupportedOperationException to be thrown.");
        } catch (UnsupportedOperationException e) {
            // This is the expected outcome.
            // The specific Composite parser implementation throws an exception with no message.
            assertNull("The exception message should be null.", e.getMessage());
        }
    }
}