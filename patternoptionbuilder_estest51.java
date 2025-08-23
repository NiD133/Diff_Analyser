package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the deprecated constructor can still be called without throwing an exception.
     * This ensures backward compatibility for a class that is primarily used statically.
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testConstructorInstantiation() {
        // The PatternOptionBuilder constructor is deprecated because it's a utility class
        // with only static methods. This test verifies that it can still be instantiated
        // without errors for legacy purposes.
        final PatternOptionBuilder builder = new PatternOptionBuilder();

        // Assert that the instance was successfully created.
        assertNotNull("Builder instance should not be null", builder);
    }
}