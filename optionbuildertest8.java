package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for creating options with special characters using the deprecated {@link OptionBuilder}.
 * <p>
 * This suite verifies that {@link OptionBuilder#create(char)} correctly handles
 * special characters that are explicitly permitted as option names, such as '?' and '@',
 * and rejects invalid characters like a space.
 * </p>
 */
@DisplayName("OptionBuilder: Special Character Handling")
class OptionBuilderSpecialCharsTest {

    @DisplayName("Should create options for permitted special characters")
    @ParameterizedTest(name = "Character: ''{0}'' with description: ''{1}''")
    @CsvSource({
        "?, help options",
        "@, read from stdin"
    })
    void whenCreatingOptionWithPermittedSpecialChar_thenOptionIsCreatedSuccessfully(char optionChar, String description) {
        // Act
        final Option option = OptionBuilder.withDescription(description).create(optionChar);

        // Assert
        assertEquals(String.valueOf(optionChar), option.getOpt(),
            "The option's short name should match the provided special character.");
        assertEquals(description, option.getDescription(),
            "The option's description should be set correctly.");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for an invalid space character")
    void whenCreatingOptionWithInvalidSpaceChar_thenThrowIllegalArgumentException() {
        // Arrange
        final char invalidChar = ' ';

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> OptionBuilder.create(invalidChar),
            "Creating an option with a space character should be disallowed and throw an exception.");
    }
}