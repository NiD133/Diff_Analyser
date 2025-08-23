package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the static methods of {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that the deprecated {@link RandomUtils#nextDouble()} method returns a value
     * within the expected range of [0, Double.MAX_VALUE).
     *
     * A test for a random number generator should verify its properties and constraints,
     * not a specific return value.
     */
    @Test
    @SuppressWarnings("deprecation") // Intentionally testing a deprecated method
    public void nextDouble_shouldReturnDoubleWithinExpectedRange() {
        // When: A random double is generated
        double randomValue = RandomUtils.nextDouble();

        // Then: The value should conform to the method's contract
        assertTrue("The generated double should be non-negative.", randomValue >= 0.0);
        assertTrue("The generated double should be less than Double.MAX_VALUE.", randomValue < Double.MAX_VALUE);
    }
}