package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Minutes} class, focusing on comparison methods.
 */
public class MinutesTest {

    @Test
    public void isLessThan_shouldReturnTrue_whenComparingSmallerToLarger() {
        // Arrange
        Minutes smallerMinutes = Minutes.TWO;
        Minutes largerMinutes = Minutes.minutes(31);

        // Act
        boolean result = smallerMinutes.isLessThan(largerMinutes);

        // Assert
        assertTrue("Expected 2 minutes to be less than 31 minutes", result);
    }
}