package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void plus_addingZeroToZero_returnsZero() {
        // Arrange
        Weeks zeroWeeks = Weeks.ZERO;
        Weeks anotherZeroWeeks = Weeks.ZERO;

        // Act
        Weeks result = zeroWeeks.plus(anotherZeroWeeks);

        // Assert
        assertEquals(Weeks.ZERO, result);
    }
}