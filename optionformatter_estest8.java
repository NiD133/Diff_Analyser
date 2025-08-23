package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OptionFormatter}.
 */
// The original test extended a scaffolding class, which is preserved here.
// In a real-world scenario, the contents of this class would be reviewed
// to see if it could be replaced with standard JUnit features.
public class OptionFormatterTest extends OptionFormatter_ESTest_scaffolding {

    /**
     * Tests that getSince() returns the default string "--" when the Option
     * does not have a "since" value specified.
     */
    @Test
    public void getSince_whenOptionHasNoSinceValue_returnsDefaultString() {
        // Arrange
        // Create a basic option. The key for this test is that it has no deprecation
        // attributes, and therefore no "since" version is specified.
        final Option option = new Option(null, "an-option", false, "description");
        final OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        final String actualSince = formatter.getSince();

        // Assert
        final String expectedSince = "--";
        assertEquals("Expected the default 'since' string when none is provided", expectedSince, actualSince);
    }
}