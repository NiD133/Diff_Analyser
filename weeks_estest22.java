package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that subtracting a zero-week period from another zero-week period
     * results in a zero-week period.
     */
    @Test
    public void minus_zeroFromZero_returnsZero() {
        // Arrange
        Weeks zeroWeeks = Weeks.ZERO;

        // Act
        Weeks result = zeroWeeks.minus(zeroWeeks);

        // Assert
        assertEquals(Weeks.ZERO, result);
    }
}