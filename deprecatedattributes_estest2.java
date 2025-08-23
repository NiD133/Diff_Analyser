package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for the {@link DeprecatedAttributes} class.
 */
public class DeprecatedAttributesTest {

    /**
     * Tests that the 'since' attribute can be set correctly using the builder,
     * and that other attributes retain their expected default values.
     */
    @Test
    public void testBuilderSetsSinceAndRetainsDefaults() {
        // Arrange
        final String expectedSince = "1.5";

        // Act
        final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setSince(expectedSince)
                .get();

        // Assert
        assertEquals("The 'since' attribute should match the value set.", expectedSince, attributes.getSince());
        assertEquals("The 'description' attribute should be empty by default.", "", attributes.getDescription());
        assertFalse("The 'forRemoval' attribute should be false by default.", attributes.isForRemoval());
    }
}