package org.joda.time.format;

import java.io.CharArrayWriter;
import junit.framework.TestCase;
import org.joda.time.Period;

/**
 * Tests for the writer-based printing methods of {@link PeriodFormatter}.
 * This class focuses on the printTo(Writer, ReadablePeriod) method.
 */
public class PeriodFormatterWriterMethodTest extends TestCase {

    /**
     * Tests that a standard period is formatted correctly to a Writer.
     */
    public void testPrintTo_withWriter_printsStandardISOFormat() throws Exception {
        // Arrange
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8); // 1Y 2M 3W 4D 5h 6m 7s 8ms
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        CharArrayWriter out = new CharArrayWriter();
        String expected = "P1Y2M3W4DT5H6M7.008S";

        // Act
        formatter.printTo(out, period);

        // Assert
        assertEquals(expected, out.toString());
    }

    /**
     * Tests that attempting to print a null period to a Writer throws an exception.
     */
    public void testPrintTo_withWriter_throwsExceptionForNullPeriod() throws Exception {
        // Arrange
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        CharArrayWriter out = new CharArrayWriter();

        // Act & Assert
        try {
            formatter.printTo(out, null);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException ex) {
            // Expected exception, so the test passes.
        }
    }
}