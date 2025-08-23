package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void minus_whenSubtractingZero_shouldNotChangeValue() {
        // Arrange: Define the initial Years object.
        final Years threeYears = Years.THREE;

        // Act: Call the method under test with a value that should not cause a change.
        final Years result = threeYears.minus(0);

        // Assert: The resulting object should be equal to the original.
        assertEquals("Subtracting zero should result in an equal Years object.", threeYears, result);
    }
}