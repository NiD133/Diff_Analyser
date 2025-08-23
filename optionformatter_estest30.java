package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link OptionFormatter} class.
 */
public class OptionFormatterTest {

    /**
     * Tests that getBothOpt() correctly formats an option that has both a short
     * and a long name, using the default prefixes and separator.
     */
    @Test
    public void getBothOptShouldFormatShortAndLongOptionsWithDefaultSettings() {
        // Arrange: Create an option with both a short and a long name.
        // The OptionFormatter will be created with default settings.
        Option option = new Option("v", "verbose", false, "Enable verbose mode.");
        OptionFormatter formatter = OptionFormatter.from(option);
        String expectedFormattedString = "-v, --verbose";

        // Act: Get the formatted string for both option names.
        String actualFormattedString = formatter.getBothOpt();

        // Assert: Verify that the output matches the expected format.
        assertEquals(expectedFormattedString, actualFormattedString);
    }
}