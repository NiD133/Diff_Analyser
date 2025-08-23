package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that isGreaterThan() returns false when a Minutes instance is compared to itself.
     * A value cannot be greater than itself.
     */
    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingAnInstanceToItself() {
        // Arrange
        Minutes oneMinute = Minutes.ONE;

        // Act & Assert
        assertFalse("A Minutes instance should not be greater than itself.", oneMinute.isGreaterThan(oneMinute));
    }
}