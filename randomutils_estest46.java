package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link RandomUtils} class.
 */
public class RandomUtilsTest {

    /**
     * Tests that the instance method {@code randomDouble()} returns a value within the
     * valid range of [0, Double.MAX_VALUE).
     */
    @Test
    public void testRandomDoubleInstanceMethodReturnsValueWithinExpectedRange() {
        // Arrange
        // The no-arg constructor is deprecated but was used in the original test.
        // It defaults to using a strong secure random generator.
        final RandomUtils randomUtils = new RandomUtils();

        // Act
        final double randomValue = randomUtils.randomDouble();

        // Assert
        // According to the Javadoc, the result must be non-negative and less than Double.MAX_VALUE.
        assertTrue("The generated double must be non-negative.", randomValue >= 0.0);
        assertTrue("The generated double must be less than Double.MAX_VALUE.", randomValue < Double.MAX_VALUE);
    }
}