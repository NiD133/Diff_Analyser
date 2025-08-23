package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that a Seconds instance is not considered greater than itself.
     * The isGreaterThan method should return false for equal instances.
     */
    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingAnInstanceToItself() {
        // Arrange
        Seconds oneSecond = Seconds.ONE;

        // Act
        boolean isGreater = oneSecond.isGreaterThan(oneSecond);

        // Assert
        assertFalse("A Seconds instance should not be greater than itself.", isGreater);
    }
}