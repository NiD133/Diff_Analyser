package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void minus_whenSubtractingAValueFromItself_shouldReturnZero() {
        // Arrange
        Seconds twoSeconds = Seconds.TWO;
        Seconds expectedResult = Seconds.ZERO;

        // Act
        Seconds actualResult = twoSeconds.minus(twoSeconds);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}