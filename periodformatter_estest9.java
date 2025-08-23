package org.joda.time.format;

import org.junit.Test;
import java.util.LinkedList;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link PeriodFormatter} class.
 */
public class PeriodFormatterTest {

    /**
     * Tests that the getPrinter() method returns the exact printer instance
     * that was supplied to the constructor.
     */
    @Test
    public void getPrinter_shouldReturnThePrinterSuppliedToTheConstructor() {
        // Arrange
        // A PeriodFormatterBuilder.Composite can act as both a printer and a parser,
        // which is required to construct a PeriodFormatter.
        LinkedList<Object> components = new LinkedList<>();
        PeriodFormatterBuilder.Composite printerAndParser = new PeriodFormatterBuilder.Composite(components);
        PeriodFormatter formatter = new PeriodFormatter(printerAndParser, printerAndParser);

        // Act
        PeriodPrinter actualPrinter = formatter.getPrinter();

        // Assert
        // The getter should return the exact same instance provided at construction.
        assertSame("The returned printer should be the same instance provided to the constructor.",
                     printerAndParser, actualPrinter);
    }
}