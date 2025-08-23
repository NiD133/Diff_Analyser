package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link Years} class, focusing on comparison logic.
 */
public class YearsTest {

    /**
     * Verifies that isGreaterThan() returns false when a Years instance
     * is compared to itself. A value cannot be strictly greater than itself.
     */
    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingToItself() {
        // Arrange
        Years someYears = Years.years(-690);

        // Act
        boolean isGreater = someYears.isGreaterThan(someYears);

        // Assert
        assertFalse("A Years instance should not be considered greater than itself.", isGreater);
    }
}