package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for the {@link DeprecatedAttributes} class, focusing on its builder.
 */
public class DeprecatedAttributesTest {

    @Test
    public void shouldBuildWithDescriptionAndDefaultValuesForOthers() {
        // Arrange
        final String expectedDescription = "This option is deprecated; use --new-option instead.";
        final DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder();

        // Act
        builder.setDescription(expectedDescription);
        final DeprecatedAttributes attributes = builder.get();

        // Assert
        assertEquals("The description should be set as provided.", expectedDescription, attributes.getDescription());
        assertFalse("forRemoval should default to false when not set.", attributes.isForRemoval());
        assertEquals("since should default to an empty string when not set.", "", attributes.getSince());
    }
}