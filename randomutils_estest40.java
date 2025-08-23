package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the static methods in {@link RandomUtils}.
 * This class focuses on a specific test case that was improved for clarity.
 */
public class RandomUtilsTest {

    /**
     * Tests that the deprecated {@link RandomUtils#nextLong()} method returns a non-negative value,
     * which is consistent with its contract of generating a random long between
     * 0 (inclusive) and Long.MAX_VALUE (exclusive).
     */
    @Test
    public void nextLong_shouldReturnNonNegativeValue() {
        // The method under test, RandomUtils.nextLong(), is deprecated.
        // This test verifies its fundamental contract.

        // Act: Call the method to get a random long.
        final long randomLong = RandomUtils.nextLong();

        // Assert: The returned value must be non-negative.
        assertTrue("The generated long must be non-negative, but was " + randomLong, randomLong >= 0L);
    }
}