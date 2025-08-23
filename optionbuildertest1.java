package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the deprecated {@link OptionBuilder}.
 */
class OptionBuilderTest {

    @Test
    @DisplayName("create(char) should build a simple option with a short name and description")
    void createWithChar_shouldSetBasicProperties() {
        // Arrange
        // The OptionBuilder uses a static, fluent interface, so arrangement and action are combined.

        // Act: Create an option with a short name 'o' and a description.
        final Option option = OptionBuilder.withDescription("option description").create('o');

        // Assert: Verify that all properties of the created option are set correctly.
        assertAll("Created option properties",
            () -> assertEquals("o", option.getOpt(), "The short name should be 'o'."),
            () -> assertEquals("option description", option.getDescription(), "The description should be set."),
            () -> assertFalse(option.hasArg(), "The option should not accept an argument by default.")
        );
    }
}