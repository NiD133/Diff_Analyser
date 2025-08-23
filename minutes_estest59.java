package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void minutesFactory_shouldCreateInstanceWithCorrectValue() {
        // Arrange
        final int expectedMinutes = 2;

        // Act
        Minutes twoMinutes = Minutes.minutes(expectedMinutes);

        // Assert
        assertEquals(expectedMinutes, twoMinutes.getMinutes());
    }
}