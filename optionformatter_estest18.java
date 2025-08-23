package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Option;
import org.junit.Test;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    @Test
    public void toOptionalShouldReturnEmptyStringForEmptyInput() {
        // Arrange
        // A dummy option is required to create a formatter instance,
        // but its properties do not affect the toOptional() method.
        final Option dummyOption = new Option(null, "dummy");
        final OptionFormatter formatter = OptionFormatter.from(dummyOption);
        final String emptyInput = "";

        // Act
        final String result = formatter.toOptional(emptyInput);

        // Assert
        assertEquals("The toOptional method should return an empty string when the input is empty.", "", result);
    }
}