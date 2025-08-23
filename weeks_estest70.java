package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Weeks}.
 */
public class WeeksTest {

    @Test
    public void weeksBetween_shouldReturnZero_whenInstantsAreTheSame() {
        // Arrange
        Instant instant = Instant.EPOCH;

        // Act
        Weeks result = Weeks.weeksBetween(instant, instant);

        // Assert
        assertEquals("Calculating weeks between the same instant should result in zero.", Weeks.ZERO, result);
    }
}