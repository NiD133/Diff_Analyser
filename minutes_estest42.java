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
        int expectedMinutes = 3;

        // Act
        Minutes minutes = Minutes.minutes(expectedMinutes);

        // Assert
        assertEquals(expectedMinutes, minutes.getMinutes());
    }
}