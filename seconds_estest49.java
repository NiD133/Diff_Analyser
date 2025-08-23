package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that adding zero to a Seconds instance returns the same instance,
     * which is an important optimization for immutable objects.
     */
    @Test
    public void plus_whenAddingZero_shouldReturnSameInstance() {
        // Arrange
        Seconds initialSeconds = Seconds.ZERO;

        // Act
        Seconds result = initialSeconds.plus(0);

        // Assert
        assertSame("Adding zero should return the same instance, not just an equal one.", initialSeconds, result);
    }
}