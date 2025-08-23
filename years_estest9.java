package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void negated_shouldReturnYearsWithOppositeSign() {
        // Arrange: Create a Years object representing 2 years.
        Years twoYears = Years.TWO;
        int expectedNegatedValue = -2;

        // Act: Negate the Years object.
        Years result = twoYears.negated();

        // Assert: The new Years object should represent -2 years.
        assertEquals(expectedNegatedValue, result.getYears());
    }
}