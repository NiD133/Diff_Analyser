package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link OptionFormatter} class.
 */
public class OptionFormatterTest {

    @Test
    public void isRequired_ShouldReturnTrue_WhenOptionIsRequired() {
        // Arrange: Create an option and explicitly mark it as required.
        // Using meaningful names for the option's properties improves readability,
        // even if they are not directly used in the assertion.
        final Option requiredOption = new Option("r", "required-opt", false, "A required option");
        requiredOption.setRequired(true);

        // Act: Create a formatter from the option and check its required status.
        final OptionFormatter formatter = OptionFormatter.from(requiredOption);
        final boolean isRequired = formatter.isRequired();

        // Assert: The formatter should correctly report that the option is required.
        assertTrue("The formatter should reflect that the underlying option is required.", isRequired);
    }
}