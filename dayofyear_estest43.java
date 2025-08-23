package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DayOfYear}.
 */
public class DayOfYearTest {

    @Test
    public void compareTo_whenComparedToItself_returnsZero() {
        // Arrange
        DayOfYear dayOfYear = DayOfYear.of(150);

        // Act
        int result = dayOfYear.compareTo(dayOfYear);

        // Assert
        assertEquals("A DayOfYear instance compared to itself should return 0.", 0, result);
    }
}