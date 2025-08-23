package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link OptionFormatter} class.
 */
public class OptionFormatterTest {

    /**
     * Verifies that getDescription() returns an empty string when the Option
     * it formats was created without a description.
     */
    @Test
    public void testGetDescriptionForOptionWithNoDescription() {
        // Arrange: Create an Option with a null description.
        // The Option(String opt, String description) constructor is used here.
        Option optionWithNoDescription = new Option(null, null);
        OptionFormatter formatter = OptionFormatter.from(optionWithNoDescription);

        // Act: Retrieve the description from the formatter.
        String description = formatter.getDescription();

        // Assert: The description should be an empty string, not null.
        assertEquals("Description should be an empty string for an option with no description text.", "", description);
    }
}