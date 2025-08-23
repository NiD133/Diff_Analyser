package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link RandomUtils}, focusing on the behavior of its
 * static and instance-based random generation methods.
 */
public class RandomUtilsTest {

    /**
     * Tests that the static {@code RandomUtils.nextFloat()} method returns a value
     * within the expected range [0, Float.MAX_VALUE).
     */
    @Test
    public void staticNextFloatShouldReturnFloatWithinExpectedRange() {
        // Act: Call the static method to generate a random float.
        // Note: As of commons-lang3 v3.16, static methods use a secure random source.
        final float randomFloat = RandomUtils.nextFloat();

        // Assert: Verify the float is within the documented range.
        assertTrue("The generated float should be non-negative.", randomFloat >= 0.0f);
        assertTrue("The generated float should be less than Float.MAX_VALUE.", randomFloat < Float.MAX_VALUE);
    }

    /**
     * Tests that the instance method {@code randomBoolean()} from an 'insecure'
     * source can be called successfully without throwing an exception.
     */
    @Test
    public void insecureRandomBooleanShouldExecuteWithoutError() {
        // Arrange: Get an insecure (non-cryptographically strong) RandomUtils instance.
        final RandomUtils insecureRandom = RandomUtils.insecure();

        // Act: Call the instance method.
        // The purpose of this test is to ensure the method call completes successfully.
        // Since the result can be either true or false, we cannot assert a specific value.
        insecureRandom.randomBoolean();

        // Assert (Implicit): The test passes if no exception is thrown.
    }
}