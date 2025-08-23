package org.joda.time.format;

import org.junit.Test;
import org.joda.time.Hours;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.PeriodFormatterBuilder.Composite;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * This test class contains tests for the PeriodFormatter class.
 * This particular test was improved for understandability.
 */
public class PeriodFormatter_ESTestTest20 { // Retaining original class name for context

    /**
     * Verifies that calling print() on a PeriodFormatter constructed with an
     * empty composite printer throws a NullPointerException.
     *
     * A composite printer requires at least one underlying printer element to function.
     * Providing an empty list of elements results in an invalid state that should
     * cause a failure upon use.
     */
    @Test(expected = NullPointerException.class)
    public void print_withEmptyCompositePrinter_shouldThrowNullPointerException() {
        // Arrange: Create a formatter with a composite printer that has no actual printer elements.
        // A Composite delegates printing to a list of printers/parsers. Here, we provide an empty list.
        List<Object> emptyElements = Collections.emptyList();
        Composite emptyComposite = new PeriodFormatterBuilder.Composite(emptyElements);

        // The parser is irrelevant for this print test, but the constructor requires one.
        // We can use the same empty composite instance since it implements both interfaces.
        PeriodFormatter formatter = new PeriodFormatter(emptyComposite, emptyComposite);

        ReadablePeriod periodToPrint = Hours.ONE;

        // Act: Attempt to print the period. This is expected to fail because the composite
        // has no sub-printers to delegate to, leading to an NPE internally.
        formatter.print(periodToPrint);

        // Assert: The @Test(expected) annotation handles the exception assertion.
        // If the line above does not throw an exception, the test will fail automatically.
    }
}