package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link OptionFormatter} class.
 */
public class OptionFormatterTest {

    /**
     * Verifies that getOpt() returns an empty string when the Option
     * was constructed with a null short name. This ensures that no
     * prefix or "null" string is returned.
     */
    @Test
    public void getOptShouldReturnEmptyStringForOptionWithNullShortName() {
        // Arrange: Create an Option that lacks a short name.
        final Option optionWithNullShortName = new Option(null, "long-name");
        final OptionFormatter formatter = OptionFormatter.from(optionWithNullShortName);

        // Act: Get the formatted short option string.
        final String formattedOpt = formatter.getOpt();

        // Assert: The result should be an empty string.
        assertEquals("Expected an empty string for an option with a null short name", "", formattedOpt);
    }
}