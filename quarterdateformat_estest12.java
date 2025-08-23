package org.jfree.chart.axis;

import org.junit.Test;

import java.text.FieldPosition;
import java.util.Date;
import java.util.TimeZone;

/**
 * Unit tests for the {@link QuarterDateFormat} class, focusing on exception handling.
 */
public class QuarterDateFormatTest {

    /**
     * Verifies that the format() method throws an ArrayIndexOutOfBoundsException
     * when the formatter is constructed with an empty array for quarter symbols.
     * The format method needs to look up a quarter symbol from this array, which
     * is impossible if the array is empty.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void formatWithEmptyQuarterSymbolsShouldThrowException() {
        // Arrange: Create a QuarterDateFormat instance with an empty symbol array.
        String[] emptyQuarterSymbols = new String[0];
        TimeZone defaultTimeZone = TimeZone.getDefault();
        QuarterDateFormat formatter = new QuarterDateFormat(defaultTimeZone, emptyQuarterSymbols, true);

        Date anyDate = new Date();
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);

        // Act & Assert: Calling format() should throw the expected exception
        // because it cannot find a symbol for the current quarter.
        formatter.format(anyDate, stringBuffer, fieldPosition);
    }
}