package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the static methods of the RandomUtils class.
 */
public class RandomUtilsTest {

    /**
     * Tests that the deprecated static method {@link RandomUtils#nextBoolean()}
     * executes successfully and returns a valid boolean value.
     *
     * The main goal is to ensure the method can be called without throwing an exception.
     */
    @Test
    public void staticNextBooleanShouldReturnBooleanWithoutException() {
        // Act: Call the static method under test.
        // According to the class documentation, static methods use a secure random
        // generator by default.
        boolean result = RandomUtils.nextBoolean();

        // Assert: Verify that a valid boolean was returned.
        // This is a tautological check (a boolean is always true or false),
        // but it makes the test's intent to check the return value explicit
        // and ensures the variable is actually used.
        assertTrue("The result must be a valid boolean value", result || !result);
    }
}