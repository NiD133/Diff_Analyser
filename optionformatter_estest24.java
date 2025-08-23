package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    /**
     * Verifies that getArgName() returns an empty string when the associated Option
     * is configured to not have an argument.
     */
    @Test
    public void getArgNameShouldReturnEmptyStringForOptionWithoutArgument() {
        // Arrange: Create an option that is explicitly configured to not have an argument.
        final Option optionWithoutArgument = new Option("o", "option", false, "An option without an argument");
        final OptionFormatter formatter = OptionFormatter.from(optionWithoutArgument);

        // Act: Get the formatted argument name.
        final String actualArgName = formatter.getArgName();

        // Assert: The result should be an empty string, as the option does not take an argument.
        assertEquals("", actualArgName);
    }
}