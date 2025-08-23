package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void toStandardHours_forNegativeWeeks_calculatesCorrectly() {
        // Arrange
        Weeks zeroWeeks = Weeks.ZERO;
        Weeks threeWeeks = Weeks.THREE;
        final int expectedWeeks = -3;
        final int expectedHours = -3 * 7 * 24; // -504 hours

        // Act
        // Subtract three weeks from zero to get a negative duration
        Weeks negativeThreeWeeks = zeroWeeks.minus(threeWeeks);
        Hours resultInHours = negativeThreeWeeks.toStandardHours();

        // Assert
        assertEquals("The result of 0 - 3 weeks should be -3 weeks.",
                expectedWeeks, negativeThreeWeeks.getWeeks());
        assertEquals("-3 weeks should convert to -504 hours.",
                expectedHours, resultInHours.getHours());
    }
}