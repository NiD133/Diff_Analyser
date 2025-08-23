package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void dividedBy_one_returnsSameInstance() {
        // Arrange
        Minutes zeroMinutes = Minutes.ZERO;

        // Act
        Minutes result = zeroMinutes.dividedBy(1);

        // Assert
        // For immutable objects, operations that result in no change (like dividing by 1)
        // should return the same instance to save memory.
        assertSame("Dividing by one should return the same immutable instance", zeroMinutes, result);
    }
}