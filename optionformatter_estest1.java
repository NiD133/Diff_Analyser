package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    /**
     * Verifies that getLongOpt() correctly prepends a custom long option prefix
     * to the option's long name.
     */
    @Test
    public void getLongOptShouldReturnLongOptionNameWithCustomPrefix() {
        // Arrange
        final String longOptionName = "file";
        final String customPrefix = "++";
        final String expectedFormattedLongOpt = "++file";

        // Create an option that has a long name.
        final Option option = new Option("f", longOptionName, true, "The file to process");

        // Build a formatter with a custom prefix for long options.
        final OptionFormatter formatter = OptionFormatter.builder()
                .setLongOptPrefix(customPrefix)
                .build(option);

        // Act
        final String actualFormattedLongOpt = formatter.getLongOpt();

        // Assert
        assertEquals("The formatted long option should include the custom prefix.",
                expectedFormattedLongOpt, actualFormattedLongOpt);
    }
}