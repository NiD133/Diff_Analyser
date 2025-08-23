package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void multiplyingZeroWeeksByAnyNumber_shouldReturnZeroWeeks() {
        // Arrange
        // The canonical representation of zero weeks.
        Weeks zeroWeeks = Weeks.ZERO;
        int multiplier = -1060;

        // Act
        // Multiply zero weeks by a non-zero number.
        Weeks result = zeroWeeks.multipliedBy(multiplier);

        // Assert
        // The result of 0 * x should always be 0.
        assertEquals("Multiplying zero weeks by any number should result in zero weeks.",
                Weeks.ZERO, result);
    }
}