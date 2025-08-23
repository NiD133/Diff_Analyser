package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.MutablePeriod;

/**
 * Tests for {@link PeriodFormatter}.
 */
public class PeriodFormatter_ESTestTest46 {

    /**
     * Tests that parseInto() correctly indicates a parse failure when the starting
     * position is beyond the length of the input string.
     *
     * The contract of parseInto() specifies that on failure, it returns a negative number.
     * The bitwise complement (~) of this return value should be the position where the
     * parse failed. In this case, parsing fails immediately at the out-of-bounds
     * starting position.
     */
    @Test
    public void parseIntoShouldReturnFailureIndexWhenStartPositionIsOutOfBounds() {
        // Arrange
        // A formatter that successfully parses an empty string.
        PeriodFormatter formatter = new PeriodFormatter(
                PeriodFormatterBuilder.Literal.EMPTY,
                PeriodFormatterBuilder.Literal.EMPTY);

        MutablePeriod period = new MutablePeriod();
        String emptyText = "";
        int outOfBoundsPosition = 514;

        // The expected return value for a failure is the bitwise complement of the failure position.
        int expectedReturnValue = ~outOfBoundsPosition; // This evaluates to -515

        // Act
        int actualReturnValue = formatter.parseInto(period, emptyText, outOfBoundsPosition);

        // Assert
        assertEquals(
            "Expected return value to be the bitwise complement of the out-of-bounds start position",
            expectedReturnValue,
            actualReturnValue
        );
    }
}