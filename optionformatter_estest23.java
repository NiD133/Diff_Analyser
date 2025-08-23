package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    @Test
    public void getArgNameShouldReturnDefaultFormattedNameWhenArgumentNameIsNotSet() {
        // Arrange: Create an option that requires an argument but does not have a specific name for it.
        // The 'true' flag indicates that the option has an argument.
        final Option optionWithUnnamedArg = new Option("f", "file", true, "File to process");
        final OptionFormatter formatter = OptionFormatter.from(optionWithUnnamedArg);

        // The expected format is the default argument name ("arg") enclosed
        // in the default delimiters ("<" and ">").
        final String expectedArgName = "<arg>";

        // Act
        final String actualArgName = formatter.getArgName();

        // Assert
        assertEquals("Formatter should use the default argument name when none is provided for an option that has an argument.",
                expectedArgName, actualArgName);
    }
}