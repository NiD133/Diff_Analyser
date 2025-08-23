package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@link RandomUtils#nextBytes(int)} throws an IllegalArgumentException
     * when the provided count is negative.
     */
    @Test
    public void nextBytesShouldThrowExceptionForNegativeCount() {
        // Arrange: Define an invalid negative count.
        final int negativeCount = -1;

        // Act & Assert: Verify that calling nextBytes with a negative count throws
        // the expected exception.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> RandomUtils.nextBytes(negativeCount)
        );

        // Assert: Further verify that the exception message is correct.
        assertEquals("Count cannot be negative.", thrown.getMessage());
    }
}