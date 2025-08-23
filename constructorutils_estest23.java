package org.apache.commons.lang3.reflect;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    /**
     * Tests that the public, no-argument constructor can be called without error.
     *
     * The constructor for this utility class is marked as deprecated and is intended
     * only for use by tools that require a JavaBean instance. This test confirms
     * that instantiation is possible, fulfilling the contract for such tools and
     * ensuring test coverage.
     */
    @Test
    public void testPublicConstructorInstantiation() {
        // The main purpose is to ensure the constructor doesn't throw an exception.
        // An explicit assertion makes the test's success condition clear.
        final ConstructorUtils instance = new ConstructorUtils();
        assertNotNull("A new instance should be successfully created.", instance);
    }
}