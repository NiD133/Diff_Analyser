package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link OptionBuilder} class.
 * Note: OptionBuilder is a deprecated class that uses static fields for configuration,
 * which is not a thread-safe practice. Tests require resetting this static state
 * before each run to ensure isolation.
 */
public class OptionBuilderTest {

    /**
     * The OptionBuilder class uses static fields to build an Option. To ensure
     * that tests are independent and not affected by the state left from previous
     * tests, we must reset these fields before each test execution.
     * Since the {@code reset()} method in OptionBuilder is private, we use
     * reflection to invoke it.
     *
     * @throws Exception if the reflection-based reset fails.
     */
    @Before
    public void setUp() throws Exception {
        Method resetMethod = OptionBuilder.class.getDeclaredMethod("reset");
        resetMethod.setAccessible(true);
        resetMethod.invoke(null); // Pass null for a static method
    }

    @Test
    public void hasArgShouldReturnBuilderInstanceForChaining() {
        // This test verifies that hasArg() returns a non-null OptionBuilder instance.
        // This is the expected behavior for a fluent builder API, allowing method calls
        // to be chained together (e.g., OptionBuilder.hasArg().withDescription(...)).

        // Act: Call the method under test.
        OptionBuilder builder = OptionBuilder.hasArg();

        // Assert: Verify that a builder instance was returned.
        assertNotNull("Expected hasArg() to return a non-null builder instance.", builder);
    }
}