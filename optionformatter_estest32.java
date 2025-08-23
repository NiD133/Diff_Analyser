package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link OptionFormatter} class, focusing on syntax generation.
 */
public class OptionFormatterTest {

    /**
     * Verifies that the syntax for an Option with no short or long name is an empty string.
     * This is an edge case, but the formatter should handle it gracefully.
     */
    @Test
    public void toSyntaxOptionShouldReturnEmptyStringForOptionWithNoName() {
        // Arrange: Create an Option that has neither a short name nor a long name.
        final Option namelessOption = new Option(null, null);
        final OptionFormatter formatter = OptionFormatter.from(namelessOption);

        // Act: Generate the syntax string for the option.
        final String syntax = formatter.toSyntaxOption();

        // Assert: The resulting syntax string should be empty, as there is no option name to format.
        assertEquals("The syntax for a nameless option should be an empty string.", "", syntax);
    }
}