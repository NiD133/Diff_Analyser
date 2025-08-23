package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that the getMinutes() method correctly returns the value
     * used to initialize the Minutes instance.
     */
    @Test
    public void getMinutes_shouldReturnTheCorrectValue() {
        // Arrange: Create a Minutes instance with a specific value.
        final int expectedMinutes = 20;
        Minutes minutesInstance = Minutes.minutes(expectedMinutes);

        // Act: Call the method under test.
        int actualMinutes = minutesInstance.getMinutes();

        // Assert: Verify that the returned value is correct.
        assertEquals(expectedMinutes, actualMinutes);
    }
}