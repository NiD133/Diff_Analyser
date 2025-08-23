package org.apache.commons.lang3;

import org.junit.Test;

/**
 * Test suite for the {@link RandomUtils} class.
 * This class provides a more understandable and robust version of the original auto-generated test.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@link RandomUtils#nextBoolean()} executes without throwing an exception.
     *
     * <p>
     * A test for a method that produces random output cannot assert a specific
     * return value (e.g., always {@code true} or always {@code false}).
     * Therefore, this test simply verifies that the method can be called
     * successfully. The test passes if no exception is thrown, serving as a
     * basic but valid smoke test.
     * </p>
     */
    @Test
    public void nextBooleanShouldExecuteSuccessfully() {
        // When the nextBoolean() method is called...
        RandomUtils.nextBoolean();

        // ...the test passes if no exception is thrown.
        // The returned boolean value is intentionally not asserted because it is,
        // by definition, random and unpredictable in a unit test.
    }
}