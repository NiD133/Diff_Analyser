package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DeprecatedAttributes}.
 */
public class DeprecatedAttributesTest {

    /**
     * Tests that the toString() method correctly formats the output when only a description is provided.
     * It also verifies that the description getter returns the correct value.
     */
    @Test
    public void testToStringWithDescriptionOnly() {
        // Arrange
        final String description = "This option is obsolete.";
        final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setDescription(description)
                .get();

        // Act
        final String actualToString = attributes.toString();

        // Assert
        final String expectedToString = "Deprecated: " + description;
        assertEquals(expectedToString, actualToString);
        assertEquals("The description getter should return the value set in the builder.",
                description, attributes.getDescription());
    }
}