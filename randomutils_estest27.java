package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that the insecure version of {@link RandomUtils#randomFloat()}
     * consistently returns a value within the expected range [0, Float.MAX_VALUE).
     */
    @Test
    public void insecureRandomFloatShouldReturnFloatWithinExpectedRange() {
        // Arrange: Get an instance of the insecure RandomUtils.
        final RandomUtils insecureRandom = RandomUtils.insecure();

        // Act & Assert:
        // Call the method multiple times to increase confidence that it
        // consistently adheres to its contract.
        for (int i = 0; i < 100; i++) {
            final float result = insecureRandom.randomFloat();

            // The method's contract guarantees a float between 0 (inclusive)
            // and Float.MAX_VALUE (exclusive).
            assertTrue("The random float should be non-negative. Got: " + result, result >= 0.0f);
            assertTrue("The random float should be less than Float.MAX_VALUE. Got: " + result, result < Float.MAX_VALUE);
        }
    }
}