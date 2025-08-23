package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that the negated() method correctly inverts the sign of a positive Minutes value.
     */
    @Test
    public void testNegated_convertsPositiveToNegative() {
        // Arrange: Create an instance of Minutes with a positive value.
        final Minutes threeMinutes = Minutes.THREE;
        final int expectedNegativeValue = -3;

        // Act: Call the method under test.
        final Minutes result = threeMinutes.negated();

        // Assert: Verify that the result has the expected negative value.
        assertEquals(expectedNegativeValue, result.getMinutes());
    }
}