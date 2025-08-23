package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.MutablePeriod;
import org.joda.time.Weeks;

public class PeriodFormatter_ESTestTest6 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that parsing with a formatter that expects an empty string
     * succeeds immediately without consuming any characters or modifying the period.
     */
    @Test
    public void parseIntoWithEmptyFormatterShouldSucceedAndReturnInitialPosition() {
        // Arrange
        // An "empty" formatter is created using an empty literal. This formatter
        // is expected to parse nothing and succeed immediately at any position.
        PeriodFormatter emptyFormatter = new PeriodFormatter(
                PeriodFormatterBuilder.Literal.EMPTY, 
                PeriodFormatterBuilder.Literal.EMPTY
        );

        MutablePeriod period = new MutablePeriod(Weeks.ONE);
        MutablePeriod originalPeriod = period.copy(); // Create a copy to verify it's not modified.

        String anyText = "P1W";
        int initialPosition = 0;

        // Act
        // Attempt to parse the string. Since the formatter is empty, it should "match"
        // an empty string at the initial position and not advance.
        int newPosition = emptyFormatter.parseInto(period, anyText, initialPosition);

        // Assert
        // The parse should succeed, returning the initial position as no characters were consumed.
        assertEquals("The new position should be the same as the initial position", 
                     initialPosition, newPosition);

        // The period object should remain unchanged by the parse operation.
        assertEquals("The period object should not be modified", 
                     originalPeriod, period);
    }
}