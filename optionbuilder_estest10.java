package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link OptionBuilder}.
 *
 * <p>Note: {@link OptionBuilder} is a deprecated class that uses static fields to build an Option.
 * This design is problematic as it can lead to state leaking between tests. To ensure test isolation,
 * we reset the builder's state before each test execution using a {@code @Before} method.
 */
public class OptionBuilderTest {

    /**
     * Resets the static state of OptionBuilder before each test.
     * This is crucial for test isolation because OptionBuilder maintains its configuration
     * in static fields. We use reflection to invoke the private {@code reset()} method.
     */
    @Before
    public void setUp() throws Exception {
        Method resetMethod = OptionBuilder.class.getDeclaredMethod("reset");
        resetMethod.setAccessible(true);
        resetMethod.invoke(null); // 'null' for a static method
    }

    @Test
    public void withArgNameDoesNotImplyOptionHasArgument() {
        // Arrange: Set an argument name for the next option.
        // This should not, by itself, indicate that the option takes an argument.
        final String argumentName = "class-name";
        OptionBuilder.withArgName(argumentName);

        // Act: Create an option with a short name.
        Option option = OptionBuilder.create('S');

        // Assert: Verify the option properties.
        assertEquals("S", option.getOpt());
        assertEquals("The argument name should be set", argumentName, option.getArgName());

        // Key assertion: The option should not be configured to accept an argument.
        assertFalse("Setting an arg name alone should not make the option have an argument", option.hasArg());
        assertEquals("Number of arguments should be uninitialized", Option.UNINITIALIZED, option.getArgs());
    }
}