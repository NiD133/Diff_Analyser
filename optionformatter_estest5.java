package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    @Test
    public void testToSyntaxOptionShouldReturnEmptyStringWhenOptionHasNoName() {
        // Arrange: Create an Option that has neither a short nor a long name.
        // This is an edge case for syntax generation.
        final Option optionWithNoName = new Option(null, null);
        final OptionFormatter formatter = OptionFormatter.from(optionWithNoName);

        // Act: Generate the syntax string, treating the option as optional.
        final String actualSyntax = formatter.toSyntaxOption(false);

        // Assert: The resulting syntax string should be empty because there is no
        // option name to display.
        assertEquals("The syntax for an option with no name should be empty", "", actualSyntax);
    }
}