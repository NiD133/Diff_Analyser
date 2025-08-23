package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void minus_whenArgumentIsNull_shouldReturnSameInstance() {
        // Arrange: According to the Javadoc, a null argument to minus() is treated as zero.
        // Subtracting zero is a no-op, so the method should return the original instance
        // as an optimization, without creating a new object.
        Weeks initialWeeks = Weeks.MIN_VALUE;

        // Act: Call the minus() method with a null argument.
        Weeks result = initialWeeks.minus((Weeks) null);

        // Assert: The returned object should be the exact same instance, not just an equal one.
        assertSame("Subtracting a null Weeks object should return the same instance", initialWeeks, result);
    }
}