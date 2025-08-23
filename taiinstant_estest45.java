package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for the equals method of {@link TaiInstant}.
 */
public class TaiInstantTest {

    /**
     * Tests the reflexive property of the equals method, ensuring an instance is equal to itself.
     */
    @Test
    public void equals_whenComparedToItself_returnsTrue() {
        // Arrange: Create an instance of TaiInstant.
        // Using the TAI epoch (0 seconds, 0 nanoseconds) as a simple, clear example.
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 0L);

        // Act & Assert: An object must be equal to itself.
        // assertEquals uses the .equals() method for comparison and provides a clear failure message.
        assertEquals(instant, instant);
    }
}