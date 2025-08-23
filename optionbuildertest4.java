package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the deprecated {@link OptionBuilder}.
 * This test class focuses on verifying the creation of a fully configured Option.
 */
@SuppressWarnings("deprecation") // The class under test, OptionBuilder, is deprecated.
class OptionBuilderTest {

    @Test
    @DisplayName("OptionBuilder should create a fully configured option")
    void shouldCreateCompleteOptionWithAllPropertiesSet() {
        // Arrange
        final String longOpt = "simple-option";
        final String description = "this is a simple option";
        final char shortOpt = 's';

        // Act: Use the builder to create an option with multiple properties.
        final Option option = OptionBuilder
            .withLongOpt(longOpt)
            .hasArg()
            .isRequired()
            .hasArgs()
            .withType(Float.class)
            .withDescription(description)
            .create(shortOpt);

        // Assert: Verify that all properties were set correctly on the created Option.
        assertAll("Verify all properties of the created Option",
            () -> assertEquals(String.valueOf(shortOpt), option.getOpt(), "Short option name should match"),
            () -> assertEquals(longOpt, option.getLongOpt(), "Long option name should match"),
            () -> assertEquals(description, option.getDescription(), "Description should match"),
            () -> assertEquals(Float.class, option.getType(), "Type should be Float.class"),
            () -> assertTrue(option.hasArg(), "Option should have an argument"),
            () -> assertTrue(option.isRequired(), "Option should be required"),
            () -> assertTrue(option.hasArgs(), "Option should support multiple arguments")
        );
    }
}