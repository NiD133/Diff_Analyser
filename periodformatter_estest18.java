package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedWriter;
import java.io.Writer;
import org.joda.time.ReadablePeriod;
import org.joda.time.Weeks;

public class PeriodFormatter_ESTestTest18 {

    /**
     * Tests that an IOException from the underlying Writer is correctly propagated
     * by the printTo(Writer, ReadablePeriod) method.
     */
    @Test
    public void printToWriterShouldThrowIOExceptionWhenWriterFails() {
        // Arrange
        // A simple formatter that doesn't actually format anything, used for testing the printTo method.
        PeriodFormatter formatter = new PeriodFormatter(
                PeriodFormatterBuilder.Literal.EMPTY,
                PeriodFormatterBuilder.Literal.EMPTY);

        // A PipedWriter that is not connected to a PipedReader will throw an
        // IOException upon any write attempt. This simulates a faulty writer.
        Writer faultyWriter = new PipedWriter();
        ReadablePeriod anyPeriod = Weeks.TWO;

        // Act & Assert
        try {
            formatter.printTo(faultyWriter, anyPeriod);
            fail("Expected an IOException because the writer is not connected.");
        } catch (IOException e) {
            // The test passes if an IOException is caught.
            // We assert the message to ensure it's the specific exception we expect.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}