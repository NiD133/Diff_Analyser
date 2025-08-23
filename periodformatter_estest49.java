package org.joda.time.format;

import org.joda.time.Period;
import org.joda.time.ReadablePeriod;
import org.junit.Test;

import java.io.Writer;

/**
 * This test class contains tests for the PeriodFormatter class.
 * This specific test focuses on the behavior of the printTo() method.
 */
public class PeriodFormatter_ESTestTest49 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Verifies that calling printTo() with a null Writer throws a NullPointerException.
     * The formatter's internal printer is responsible for this null check.
     */
    @Test(expected = NullPointerException.class)
    public void testPrintTo_withNullWriter_throwsNullPointerException() {
        // Arrange: Create a basic formatter. The actual printing logic is not important,
        // as the exception should be thrown before any printing occurs.
        PeriodFormatterBuilder.Literal emptyPrinterParser = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(emptyPrinterParser, emptyPrinterParser);

        // A simple, non-null period is needed to satisfy the method signature.
        ReadablePeriod anyPeriod = Period.ZERO;

        // Act & Assert: Attempting to print to a null writer should throw.
        // The @Test(expected=...) annotation handles the assertion.
        formatter.printTo((Writer) null, anyPeriod);
    }
}