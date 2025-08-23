package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the static methods of {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@link RandomUtils#nextInt()} returns a non-negative integer.
     * According to its Javadoc, this method should generate a random integer
     * between 0 (inclusive) and Integer.MAX_VALUE (exclusive). This test
     * verifies the lower bound of this contract.
     */
    @Test
    public void nextInt_shouldReturnNonNegativeInteger() {
        // Act: Call the method under test.
        // This method is deprecated, but we still test its basic contract.
        @SuppressWarnings("deprecation")
        int randomNumber = RandomUtils.nextInt();

        // Assert: Verify the result is within the expected range.
        assertTrue("The generated random integer must be non-negative.", randomNumber >= 0);
    }
}