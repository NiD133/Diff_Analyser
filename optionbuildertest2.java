package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the deprecated {@link OptionBuilder}.
 */
@SuppressWarnings("deprecation") // The class under test is deprecated.
class OptionBuilderTest {

    @Test
    @DisplayName("An option created from a string should have the correct properties and no arguments by default")
    void createOptionFromStringSetsCorrectProperties() {
        // Arrange
        final String optionName = "o";
        final String optionDescription = "option description";

        // Act
        final Option option = OptionBuilder.withDescription(optionDescription).create(optionName);

        // Assert
        assertAll("Created option properties",
            () -> assertEquals(optionName, option.getOpt(), "The short name should be set correctly."),
            () -> assertEquals(optionDescription, option.getDescription(), "The description should be set correctly."),
            () -> assertFalse(option.hasArg(), "The option should not have an argument by default.")
        );
    }
}