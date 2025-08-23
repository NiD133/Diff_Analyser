package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the deprecated {@link OptionBuilder} class.
 *
 * <p><b>Note:</b> {@link OptionBuilder} uses static fields to build an {@link Option},
 * which is not a thread-safe design. To ensure test isolation, the private static
 * {@code reset()} method is called via reflection before each test.
 */
// Renamed from OptionBuilderTestTest7 for clarity and to follow standard naming conventions.
public class OptionBuilderTest {

    /**
     * Resets the static state of OptionBuilder before each test to ensure isolation.
     * This is necessary because OptionBuilder uses static fields to configure options,
     * which can cause side effects between tests.
     */
    @BeforeEach
    void resetOptionBuilderState() throws Exception {
        // The reset() method is private, so we must use reflection to invoke it.
        Method resetMethod = OptionBuilder.class.getDeclaredMethod("reset");
        resetMethod.setAccessible(true);
        resetMethod.invoke(null); // 'null' for a static method
    }

    @Test
    @DisplayName("An option created with hasArgs(n) should have n arguments")
    void createOptionWithFixedNumberOfArguments() {
        // Arrange: Define the expected number of arguments for the option.
        final int expectedNumberOfArgs = 2;

        // Act: Create an option configured to have a specific number of arguments.
        final Option option = OptionBuilder.hasArgs(expectedNumberOfArgs).create('o');

        // Assert: Verify that the created option has the correct number of arguments.
        assertEquals(expectedNumberOfArgs, option.getArgs(),
            "The number of arguments should match the value set by hasArgs().");
    }
}