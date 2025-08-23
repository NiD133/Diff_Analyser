package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Option;
import org.junit.Test;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    @Test
    public void testGetDescriptionShouldReturnTheConfiguredDescription() {
        // Arrange: Create an option with a clear, human-readable description.
        final String expectedDescription = "The file to process.";
        final Option option = new Option("f", expectedDescription);
        
        // Create a formatter for the option using default settings.
        final OptionFormatter formatter = OptionFormatter.from(option);

        // Act: Retrieve the description from the formatter.
        final String actualDescription = formatter.getDescription();

        // Assert: The returned description should match the one provided to the Option.
        assertEquals(expectedDescription, actualDescription);
    }
}