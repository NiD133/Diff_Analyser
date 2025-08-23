package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that negating a positive Seconds value results in a negative value.
     */
    @Test
    public void negated_shouldReturnNegativeValue_forPositiveInput() {
        // Arrange: Create a Seconds instance with a positive value.
        Seconds threeSeconds = Seconds.THREE;
        int expectedNegativeSeconds = -3;

        // Act: Call the negated() method.
        Seconds result = threeSeconds.negated();

        // Assert: Verify that the resulting Seconds object holds the correct negative value.
        assertEquals(expectedNegativeSeconds, result.getSeconds());
    }
}