package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that subtracting zero from a Seconds object returns the same instance,
     * verifying the optimization for no-op subtractions.
     */
    @Test
    public void minus_whenSubtractingZero_shouldReturnSameInstance() {
        // Arrange: Create an initial Seconds object.
        // Using the constant Seconds.ZERO is idiomatic and clear.
        Seconds initialSeconds = Seconds.ZERO;

        // Act: Subtract zero from the object.
        Seconds result = initialSeconds.minus(0);

        // Assert: The result should be the exact same instance, not just an equal one.
        // This confirms the immutability optimization where no new object is created.
        assertSame("Subtracting zero should return the same instance", initialSeconds, result);
    }
}