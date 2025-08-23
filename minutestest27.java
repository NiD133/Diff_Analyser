package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class, focusing on its interaction with other Joda-Time types.
 */
public class MinutesTest {

    /**
     * Tests that adding a Minutes object to a LocalDateTime correctly advances the time.
     */
    @Test
    public void plus_whenAddedToLocalDateTime_advancesTimeByCorrectAmount() {
        // Arrange
        final Minutes minutesToAdd = Minutes.minutes(26);
        final LocalDateTime startDateTime = new LocalDateTime(2006, 6, 1, 0, 0);
        final LocalDateTime expectedDateTime = new LocalDateTime(2006, 6, 1, 0, 26);

        // Act
        final LocalDateTime resultDateTime = startDateTime.plus(minutesToAdd);

        // Assert
        assertEquals(expectedDateTime, resultDateTime);
    }
}