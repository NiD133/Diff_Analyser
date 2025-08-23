package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that the getWeeks() method correctly returns the integer value
     * for the predefined constant Weeks.THREE.
     */
    @Test
    public void getWeeks_returnsCorrectValueForConstantThree() {
        // Arrange
        Weeks threeWeeks = Weeks.THREE;
        int expectedWeeks = 3;

        // Act
        int actualWeeks = threeWeeks.getWeeks();

        // Assert
        assertEquals(expectedWeeks, actualWeeks);
    }
}