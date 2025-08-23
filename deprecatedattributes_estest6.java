package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DeprecatedAttributes} class.
 */
class DeprecatedAttributesTest {

    @Test
    @DisplayName("The default DeprecatedAttributes instance should have an empty description")
    void defaultInstanceShouldHaveEmptyDescription() {
        // Arrange: The DeprecatedAttributes.DEFAULT constant is a pre-existing fixture.
        DeprecatedAttributes defaultAttributes = DeprecatedAttributes.DEFAULT;

        // Act: Retrieve the description from the default instance.
        String description = defaultAttributes.getDescription();

        // Assert: Verify that the description is an empty string.
        assertEquals("", description);
    }
}