package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    /**
     * Verifies that getLongOpt() returns an empty string for an Option
     * that was constructed without a long option name.
     */
    @Test
    public void getLongOptShouldReturnEmptyStringWhenOptionHasNoLongName() {
        // Arrange: Create an option that only has a short name ("o") and a description.
        // This specific constructor does not set a long option name.
        Option optionWithOnlyShortName = new Option("o", "A description for the option.");
        OptionFormatter formatter = OptionFormatter.from(optionWithOnlyShortName);

        // Act: Get the formatted long option string.
        String longOptString = formatter.getLongOpt();

        // Assert: The result should be an empty string, as no long option was defined.
        // The formatter should not return null or a prefix-only string (e.g., "--").
        assertEquals("The long option string should be empty", "", longOptString);
    }
}