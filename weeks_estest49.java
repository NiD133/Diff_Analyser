package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that adding a null Weeks object to an existing instance
     * is treated as a no-op and returns the original instance.
     *
     * The Javadoc for `plus(Weeks)` specifies that a null argument is
     * treated as adding zero weeks. For an immutable object, this operation
     * should ideally return the same instance without creating a new one.
     */
    @Test
    public void plus_whenAddingNull_shouldReturnSameInstance() {
        // Arrange: Create a Weeks instance to test with.
        // Using MAX_VALUE as a clear, non-trivial example.
        Weeks initialWeeks = Weeks.MAX_VALUE;

        // Act: Call the plus() method with a null argument.
        Weeks result = initialWeeks.plus((Weeks) null);

        // Assert: The method should return the exact same instance, not just an
        // equal one. This verifies the expected optimization for adding zero.
        assertSame("Adding null should return the same instance", initialWeeks, result);
        
        // For completeness, also verify the value remains unchanged.
        assertEquals("The number of weeks should not change", Integer.MAX_VALUE, result.getWeeks());
    }
}