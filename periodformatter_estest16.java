package org.joda.time.format;

import org.joda.time.ReadablePeriod;
import org.joda.time.Weeks;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PeriodFormatter_ESTestTest16 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that calling printTo() on a formatter that was not configured with a
     * printer throws an UnsupportedOperationException.
     */
    @Test
    public void printTo_whenFormatterCannotPrint_throwsUnsupportedOperationException() {
        // Arrange: Create a formatter that can only parse, not print.
        // The PeriodFormatter is given a null printer and a dummy parser.
        PeriodParser dummyParser = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatterWithoutPrinter = new PeriodFormatter(null, dummyParser);

        Writer writer = new StringWriter();
        ReadablePeriod periodToPrint = Weeks.TWO;

        // Act & Assert
        try {
            formatterWithoutPrinter.printTo(writer, periodToPrint);
            fail("Expected an UnsupportedOperationException to be thrown.");
        } catch (UnsupportedOperationException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("Printing not supported", e.getMessage());
        } catch (Exception e) {
            fail("Caught unexpected exception: " + e.getClass().getName());
        }
    }
}