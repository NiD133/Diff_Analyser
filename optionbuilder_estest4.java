package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the deprecated {@link OptionBuilder} class.
 */
public class OptionBuilderTest {

    /**
     * Verifies that an Option created via OptionBuilder has no arguments by default.
     *
     * The OptionBuilder uses a static, stateful approach. This test ensures that
     * when no argument-related methods (e.g., hasArg(), hasArgs()) are called,
     * the resulting Option object is correctly configured to not accept any arguments.
     */
    @Test
    public void testCreateOptionHasNoArgumentsByDefault() {
        // Arrange
        final String optionName = "o";
        final String longOptionName = "option";

        // Act
        // Build an option with a long name, but without specifying argument behavior.
        OptionBuilder.withLongOpt(longOptionName);
        Option option = OptionBuilder.create(optionName);

        // Assert
        // Verify the option was configured with the specified names.
        assertEquals("The long option name should be set correctly.", longOptionName, option.getLongOpt());
        assertEquals("The short option name should be set correctly.", optionName, option.getOpt());

        // The core assertion: by default, the option should not have arguments.
        // The internal value for this is Option.UNINITIALIZED (-1).
        // A more expressive and readable check is to use the hasArg() method.
        assertEquals("Default number of arguments should be UNINITIALIZED.", Option.UNINITIALIZED, option.getArgs());
        assertFalse("Option should not have an argument by default.", option.hasArg());
    }
}