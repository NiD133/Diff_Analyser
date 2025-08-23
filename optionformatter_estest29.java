package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link OptionFormatter} class.
 */
public class OptionFormatterTest {

    /**
     * Tests that getBothOpt() correctly formats an option that only has a short name defined.
     * The expected output is the short name prefixed with the default hyphen.
     */
    @Test
    public void getBothOptShouldReturnPrefixedShortOptWhenOnlyShortOptIsDefined() {
        // Arrange: Create an option that has a short name but no long name.
        Option optionWithOnlyShortName = new Option("v", "Enable verbose mode");
        OptionFormatter formatter = OptionFormatter.from(optionWithOnlyShortName);
        
        String expectedFormattedString = "-v";

        // Act: Get the formatted string for the option.
        String actualFormattedString = formatter.getBothOpt();

        // Assert: Verify that the output is the correctly prefixed short option.
        assertEquals("Expected only the prefixed short option when no long option is present.",
                expectedFormattedString, actualFormattedString);
    }
}