package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that adding a null Seconds object to an existing instance
     * has no effect, as per the method's contract where null is treated as zero.
     */
    @Test
    public void plus_addingNull_shouldHaveNoEffect() {
        // Arrange: Start with a known Seconds value.
        Seconds initialSeconds = Seconds.ONE;

        // Act: Add a null Seconds object. The Javadoc states that a null
        // input should be treated as a zero-value period.
        Seconds result = initialSeconds.plus((Seconds) null);

        // Assert: The result should be equal to the initial value.
        assertEquals(initialSeconds, result);
    }
}