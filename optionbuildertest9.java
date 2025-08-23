package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the deprecated {@link OptionBuilder}.
 * This class focuses on verifying the creation of options and the state management
 * of the static builder.
 */
class OptionBuilderTest {

    /**
     * Verifies that an Option can be created with a full set of properties
     * using the fluent builder interface.
     */
    @Test
    void testCreateFullyFeaturedOption() {
        // when
        final Option option = OptionBuilder.withLongOpt("simple option")
            .hasArg()
            .isRequired()
            .hasArgs()
            .withType(Float.class)
            .withDescription("this is a simple option")
            .create('s');

        // then
        assertAll("Verify all properties of a fully configured option",
            () -> assertEquals("s", option.getOpt(), "Short option name"),
            () -> assertEquals("simple option", option.getLongOpt(), "Long option name"),
            () -> assertEquals("this is a simple option", option.getDescription(), "Description"),
            () -> assertEquals(Float.class, option.getType(), "Type"),
            () -> assertTrue(option.hasArg(), "Should have an argument"),
            () -> assertTrue(option.isRequired(), "Should be required"),
            () -> assertTrue(option.hasArgs(), "Should support multiple arguments")
        );
    }

    /**
     * The OptionBuilder uses static fields and is not thread-safe. A critical
     * feature is that its internal state is reset after each call to create().
     * This test verifies that behavior by creating a simple option and checking
     * that properties set in other configurations (like isRequired, hasArgs, type)
     * have been reset to their default values.
     */
    @Test
    void testBuilderIsResetAfterCreation() {
        // when
        final Option option = OptionBuilder.withLongOpt("dimple option")
            .hasArg()
            .withDescription("this is a dimple option")
            .create('d');

        // then
        assertAll("Verify properties of a simple option, confirming builder state was reset",
            () -> assertEquals("d", option.getOpt(), "Short option name"),
            () -> assertEquals("dimple option", option.getLongOpt(), "Long option name"),
            () -> assertEquals("this is a dimple option", option.getDescription(), "Description"),
            () -> assertTrue(option.hasArg(), "Should have an argument"),
            // Assert that properties from the previous test's configuration are reset to defaults
            () -> assertFalse(option.isRequired(), "isRequired should have been reset to its default (false)"),
            () -> assertFalse(option.hasArgs(), "hasArgs should have been reset to its default (false)"),
            () -> assertEquals(String.class, option.getType(), "Type should have been reset to its default (String)")
        );
    }
}