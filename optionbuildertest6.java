package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for illegal character validation in {@link OptionBuilder#create(char)} and {@link OptionBuilder#create(String)}.
 */
class OptionBuilderTest {

    @Test
    @DisplayName("create(char) should throw IllegalArgumentException for an invalid character")
    void createWithInvalidCharOptionShouldThrowException() {
        // The double quote character (") is not a valid short option.
        // Option validation logic is handled by the Option class itself.
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create('"'));
    }

    @Test
    @DisplayName("create(String) should throw IllegalArgumentException for a name containing invalid characters")
    void createWithInvalidStringOptionShouldThrowException() {
        // The backtick character (`) is not a valid character within an option name.
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create("opt`"));
    }

    @Test
    @DisplayName("create(String) should succeed for a valid option name")
    void createWithValidStringOptionShouldSucceed() {
        // "opt" is a valid option name, so creating it should not throw an exception.
        assertDoesNotThrow(() -> OptionBuilder.create("opt"));
    }
}