package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the Seconds class.
 */
public class SecondsTest {

    /**
     * Tests that converting the minimum possible Seconds value to Minutes
     * correctly performs integer division.
     */
    @Test
    public void toStandardMinutes_fromMinValue_truncatesToCorrectNegativeValue() {
        // Arrange
        // Seconds.MIN_VALUE is backed by Integer.MIN_VALUE.
        // The conversion to minutes involves integer division by 60.
        final Seconds minSeconds = Seconds.MIN_VALUE;
        final int expectedMinutes = Integer.MIN_VALUE / 60; // -2,147,483,648 / 60 = -35,791,394

        // Act
        final Minutes actualMinutes = minSeconds.toStandardMinutes();

        // Assert
        assertEquals(expectedMinutes, actualMinutes.getMinutes());
    }
}