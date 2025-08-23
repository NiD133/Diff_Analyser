package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test case for the deprecated {@link OptionBuilder} class.
 *
 * Note: {@link OptionBuilder} is a static factory with a global state.
 * For a comprehensive test suite, its state should be reset before each test
 * to ensure test isolation.
 */
public class OptionBuilderTest {

    /**
     * Verifies that an Option created with an optional argument is configured correctly.
     * An option with an optional argument should have the 'hasOptionalArg' flag set
     * and expect exactly one argument.
     */
    @Test
    public void createOptionWithOptionalArg_shouldSetFlagAndArgumentCount() {
        // Arrange: Configure the static OptionBuilder for an option with a long name
        // and an optional argument.
        OptionBuilder.withLongOpt("file");
        OptionBuilder.hasOptionalArg();

        // Act: Create the Option instance from the builder's static state.
        Option option = OptionBuilder.create();

        // Assert: Verify the properties of the created Option.
        assertTrue("The option should be marked as having an optional argument.", option.hasOptionalArg());
        assertEquals("An option with an optional argument should be configured to accept 1 argument.", 1, option.getArgs());
        assertEquals("The long option name should be correctly set.", "file", option.getLongOpt());
    }
}