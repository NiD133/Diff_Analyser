package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Years class.
 */
public class YearsTest {

    /**
     * Tests that adding zero to a Years object returns the same instance.
     * For an immutable object, adding the identity value (zero) should be a no-op
     * and can be optimized to return the original instance.
     */
    @Test
    public void plus_whenAddingZero_returnsSameInstance() {
        // Arrange: Create an instance of Years with a non-zero value.
        Years initialYears = Years.years(5);

        // Act: Add zero to the Years object.
        Years result = initialYears.plus(0);

        // Assert: The operation should return the exact same instance, not just an equal one.
        assertSame("Adding zero should be an optimized no-op and return the same instance", initialYears, result);
    }
}