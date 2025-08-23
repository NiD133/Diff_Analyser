package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Verifies that subtracting zero from a Minutes object results in an equal Minutes object.
     */
    @Test
    public void minusZeroShouldReturnAnEquivalentInstance() {
        // Arrange: Create an instance of Minutes representing zero.
        Minutes zeroMinutes = Minutes.ZERO;

        // Act: Subtract zero from the instance.
        Minutes result = zeroMinutes.minus(0);

        // Assert: The resulting object should be equal to the original.
        assertEquals(zeroMinutes, result);
    }
}