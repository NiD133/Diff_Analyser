package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

// Note: The class name and inheritance are preserved from the original EvoSuite-generated test.
// In a typical, human-written test suite, this test would be part of a class like "RandomUtilsTest".
public class RandomUtils_ESTestTest28 extends RandomUtils_ESTest_scaffolding {

    /**
     * Tests that calling randomLong() on an "insecure" RandomUtils instance
     * returns a non-negative value, as per the method's contract.
     *
     * The no-argument randomLong() is documented to return a value in the range
     * [0, Long.MAX_VALUE). This test verifies the lower bound of that range.
     * A specific random value cannot be asserted, but its properties can be.
     */
    @Test
    public void testInsecureRandomLongReturnsNonNegativeValue() {
        // Arrange: Obtain an insecure random number generator instance.
        RandomUtils randomUtils = RandomUtils.insecure();

        // Act: Generate a random long value.
        long result = randomUtils.randomLong();

        // Assert: The generated value must be non-negative.
        assertTrue("The random long should be non-negative, but was " + result, result >= 0);
    }
}