package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void weeksFactory_createsInstanceWithCorrectValue() {
        // Arrange
        final int expectedWeeks = 3;

        // Act
        Weeks threeWeeks = Weeks.weeks(expectedWeeks);

        // Assert
        assertEquals("The number of weeks should match the value provided to the factory method.",
                expectedWeeks, threeWeeks.getWeeks());
    }
}