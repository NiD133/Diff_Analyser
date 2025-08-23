package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the toString() method of {@link DeprecatedAttributes}.
 */
@DisplayName("DeprecatedAttributes.toString()")
class DeprecatedAttributesToStringTest {

    private static final String DESCRIPTION = "Use Bar instead!";

    @Test
    @DisplayName("should include 'for removal' and 'since' when both are set")
    void shouldFormatWithForRemovalAndSince() {
        // Arrange
        final String expectedMessage = "Deprecated for removal since 2.0: " + DESCRIPTION;
        final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
            .setForRemoval(true)
            .setSince("2.0")
            .setDescription(DESCRIPTION)
            .get();

        // Act
        final String actualMessage = attributes.toString();

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("should include only 'for removal' when set")
    void shouldFormatWithForRemovalOnly() {
        // Arrange
        final String expectedMessage = "Deprecated for removal: " + DESCRIPTION;
        final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
            .setForRemoval(true)
            .setDescription(DESCRIPTION)
            .get();

        // Act
        final String actualMessage = attributes.toString();

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("should include only 'since' when set")
    void shouldFormatWithSinceOnly() {
        // Arrange
        final String expectedMessage = "Deprecated since 2.0: " + DESCRIPTION;
        final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
            .setSince("2.0")
            .setDescription(DESCRIPTION)
            .get();

        // Act
        final String actualMessage = attributes.toString();

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("should not include extra text when only description is set")
    void shouldFormatWithDescriptionOnly() {
        // Arrange
        final String expectedMessage = "Deprecated: " + DESCRIPTION;
        final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
            .setDescription(DESCRIPTION)
            .get();

        // Act
        final String actualMessage = attributes.toString();

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }
}