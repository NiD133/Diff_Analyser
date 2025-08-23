package org.joda.time.format;

import org.joda.time.MutablePeriod;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PeriodFormatter_ESTestTest7 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that {@link PeriodFormatter#parseInto} correctly handles a successful
     * parse that consumes no characters when the starting position is at the end of the string.
     */
    @Test
    public void parseInto_whenPositionIsAtEndOfText_returnsUnchangedPosition() {
        // Arrange
        // A parser that always succeeds without consuming any input.
        PeriodParser noOpParser = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(null, noOpParser);

        String text = "any-text";
        int positionAtEnd = text.length();

        MutablePeriod period = new MutablePeriod();
        // Create a copy to verify the period is not modified by the parse operation.
        MutablePeriod originalPeriod = new MutablePeriod(period);

        // Act
        int newPosition = formatter.parseInto(period, text, positionAtEnd);

        // Assert
        // The parse should succeed without advancing the position.
        assertEquals("The returned position should be the initial position at the end of the string.",
                positionAtEnd, newPosition);

        // The period object should not have been modified.
        assertEquals("The period should remain unchanged after a no-op parse.",
                originalPeriod, period);
    }
}