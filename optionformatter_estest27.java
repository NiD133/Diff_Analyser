package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Option;
import org.junit.Test;

/**
 * Tests for the {@link OptionFormatter} class.
 */
public class OptionFormatterTest {

    @Test
    public void toSyntaxOptionShouldFormatRequiredShortOptionWithArgument() {
        // Arrange
        // Create an option that has a short name ("?Wf"), a long name, requires an argument,
        // and has a description. The formatter should prioritize the short name.
        Option optionWithArgument = new Option("?Wf", "long-name", true, "description");
        OptionFormatter formatter = OptionFormatter.from(optionWithArgument);

        // The default syntax for a required short option with an argument is "-<opt> <arg>"
        String expectedSyntax = "-?Wf <arg>";

        // Act
        // Generate the syntax string, explicitly marking the option as required.
        String actualSyntax = formatter.toSyntaxOption(true);

        // Assert
        assertEquals("The syntax for a required short option with an argument was not formatted as expected.",
                     expectedSyntax, actualSyntax);
    }
}