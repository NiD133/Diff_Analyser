package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.text.ParsePosition;
import java.util.Date;

/**
 * Tests for the {@link QuarterDateFormat#parse(String, ParsePosition)} method.
 */
public class QuarterDateFormat_ESTestTest14 {

    /**
     * Verifies that the parse() method always returns null, as it is
     * documented as not being implemented.
     */
    @Test
    public void parse_shouldAlwaysReturnNull() {
        // Arrange
        QuarterDateFormat formatter = new QuarterDateFormat();
        String anyString = "some-irrelevant-string";
        ParsePosition anyPosition = new ParsePosition(0);

        // Act
        Date result = formatter.parse(anyString, anyPosition);

        // Assert
        assertNull("The parse() method is not implemented and should always return null.", result);
    }
}