package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Verifies that subtracting a null Minutes object is treated as subtracting zero,
     * which should result in returning the same instance without modification.
     */
    @Test
    public void minus_whenArgumentIsNull_returnsSameInstance() {
        // Arrange
        Minutes zeroMinutes = Minutes.ZERO;

        // Act
        // The Javadoc for minus(Minutes) specifies that a null argument is treated as zero.
        Minutes result = zeroMinutes.minus((Minutes) null);

        // Assert
        // Since the operation is a no-op on an immutable, cached instance,
        // the original object itself should be returned.
        assertSame("Subtracting null should be a no-op and return the same instance", zeroMinutes, result);
    }
}