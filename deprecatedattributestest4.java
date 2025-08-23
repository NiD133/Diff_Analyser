package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DeprecatedAttributes} class.
 */
class DeprecatedAttributesTest {

    @Test
    @DisplayName("toString() on the DEFAULT instance should return a constant string")
    void toStringForDefaultInstanceShouldReturnConstantString() {
        // Arrange
        final String expectedRepresentation = "Deprecated";
        final DeprecatedAttributes defaultAttributes = DeprecatedAttributes.DEFAULT;

        // Act
        final String actualRepresentation = defaultAttributes.toString();

        // Assert
        assertEquals(expectedRepresentation, actualRepresentation,
                "The string representation of the default instance should be a specific, constant value.");
    }
}