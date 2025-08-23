package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OptionFormatter}.
 * This class focuses on verifying the formatting logic for command-line options.
 */
public class OptionFormatterTest {

    /**
     * Verifies that getBothOpt() returns an empty string when an Option
     * is created without a short or long name.
     */
    @Test
    public void getBothOpt_shouldReturnEmptyString_whenOptionHasNoNames() {
        // Arrange: Create an option with null for both the short and long name.
        final Option optionWithNoNames = new Option(null, null);
        final OptionFormatter formatter = OptionFormatter.from(optionWithNoNames);

        // Act: Get the formatted string representing both option names.
        final String formattedOpt = formatter.getBothOpt();

        // Assert: The result should be an empty string, as there are no names to format.
        assertEquals("The formatted string should be empty for an option with no names.", "", formattedOpt);
    }
}