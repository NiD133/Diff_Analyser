package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that multipliedBy() correctly calculates the new value
     * and that the original Seconds object remains unchanged, ensuring immutability.
     */
    @Test
    public void multipliedBy_shouldReturnCorrectProduct_andPreserveImmutability() {
        // Arrange: Define the initial value and the multiplier.
        final int initialValue = 1566;
        final int multiplier = 518;
        final Seconds initialSeconds = Seconds.seconds(initialValue);

        // Act: Perform the multiplication operation.
        final Seconds resultSeconds = initialSeconds.multipliedBy(multiplier);

        // Assert: Verify the result and the immutability of the original object.
        final int expectedValue = 811188; // 1566 * 518
        
        // Check that the new object has the correct multiplied value.
        assertEquals("The result of the multiplication is incorrect.", expectedValue, resultSeconds.getSeconds());
        
        // Check that the original object was not modified.
        assertEquals("The original Seconds object should remain unchanged.", initialValue, initialSeconds.getSeconds());
    }
}