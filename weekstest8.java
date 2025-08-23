package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void getWeeks_shouldReturnTheValueTheInstanceWasCreatedWith() {
        // Arrange
        final int expectedWeeks = 20;
        Weeks weeks = Weeks.weeks(expectedWeeks);

        // Act
        int actualWeeks = weeks.getWeeks();

        // Assert
        assertEquals(expectedWeeks, actualWeeks);
    }
}