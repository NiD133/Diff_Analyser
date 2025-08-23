package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void plus_addingZeroToMaxValue_shouldNotChangeValue() {
        // Arrange
        Minutes maxMinutes = Minutes.MAX_VALUE;

        // Act
        Minutes result = maxMinutes.plus(0);

        // Assert
        assertEquals("Adding zero should not change the value", maxMinutes, result);
    }
}