package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that dividing a Years instance by 1 returns the same instance,
     * which is an important optimization for immutable objects.
     */
    @Test
    public void dividedByOne_shouldReturnSameInstance() {
        // Arrange
        final Years zeroYears = Years.ZERO;

        // Act
        final Years result = zeroYears.dividedBy(1);

        // Assert
        // For immutable objects, dividing by 1 should be a no-op and return
        // the original instance, not just an equal one.
        assertSame("Dividing by 1 should return the same instance", zeroYears, result);
    }
}