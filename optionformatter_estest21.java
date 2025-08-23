package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link OptionFormatter} class.
 */
public class OptionFormatterTest {

    /**
     * Tests that the getOpt() method correctly formats a short option
     * by prepending the default prefix "-".
     */
    @Test
    public void getOptShouldReturnShortOptionWithDefaultPrefix() {
        // Arrange: Create an option with a short name and get a default formatter for it.
        Option option = new Option("v", "verbose", false, "Enable verbose output");
        OptionFormatter formatter = OptionFormatter.from(option);
        String expectedFormattedOpt = "-v";

        // Act: Call the method under test.
        String actualFormattedOpt = formatter.getOpt();

        // Assert: Verify that the output matches the expected prefixed format.
        assertEquals("The formatted short option should be prepended with the default '-' prefix.",
                expectedFormattedOpt, actualFormattedOpt);
    }
}