package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.StringWriter;
import java.io.Writer;
import org.joda.time.ReadablePeriod;

// The original test class structure is maintained for context.
public class PeriodFormatter_ESTestTest17 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that printTo(Writer, ReadablePeriod) throws an IllegalArgumentException
     * when the provided period is null.
     */
    @Test
    public void printTo_withNullPeriod_throwsIllegalArgumentException() {
        // Arrange: Create a basic formatter and a writer.
        // An empty literal formatter is sufficient as the null check happens before formatting.
        PeriodFormatter formatter = new PeriodFormatter(
                PeriodFormatterBuilder.Literal.EMPTY, 
                PeriodFormatterBuilder.Literal.EMPTY);
        Writer writer = new StringWriter();
        ReadablePeriod nullPeriod = null;

        // Act & Assert: Attempt to print the null period and verify the resulting exception.
        try {
            formatter.printTo(writer, nullPeriod);
            fail("Expected an IllegalArgumentException to be thrown for a null period.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message clearly indicates the cause of the error.
            assertEquals("Period must not be null", e.getMessage());
        }
    }
}