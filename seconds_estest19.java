package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that multiplying a Seconds instance by zero results in Seconds.ZERO
     * and does not modify the original instance.
     */
    @Test
    public void multipliedBy_zero_shouldReturnZeroSeconds() {
        // Arrange
        final Seconds initialSeconds = Seconds.seconds(1810);
        final Seconds expectedResult = Seconds.ZERO;

        // Act
        final Seconds actualResult = initialSeconds.multipliedBy(0);

        // Assert
        assertEquals("The result of multiplying by zero should be zero seconds.",
                     expectedResult, actualResult);
        assertEquals("The original Seconds object should remain unchanged (be immutable).",
                     1810, initialSeconds.getSeconds());
    }
}