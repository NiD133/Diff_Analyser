package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link RandomUtils} class.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@code randomInt()} on an insecure instance returns a non-negative integer
     * as per its contract.
     *
     * The method under test, {@code randomInt()}, is documented to return a random integer
     * between 0 (inclusive) and Integer.MAX_VALUE (exclusive). This test verifies that
     * the returned value adheres to this lower bound. We do not assert a specific random
     * value, as that would make the test non-deterministic and brittle.
     */
    @Test
    public void randomIntOnInsecureInstanceShouldReturnNonNegativeValue() {
        // Arrange: Get an instance of RandomUtils backed by a non-cryptographically secure RNG.
        RandomUtils insecureRandomUtils = RandomUtils.insecure();

        // Act: Generate a random integer.
        int randomNumber = insecureRandomUtils.randomInt();

        // Assert: Verify the generated number is within the expected range [0, Integer.MAX_VALUE).
        // We only check for non-negativity as it's the most critical part of the contract.
        assertTrue("The random integer must be non-negative.", randomNumber >= 0);
    }
}