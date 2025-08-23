package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DeprecatedAttributes}.
 */
@DisplayName("Tests for DeprecatedAttributes")
class DeprecatedAttributesTest {

    @Test
    @DisplayName("An empty builder should create an instance with default values")
    void emptyBuilderShouldCreateDefaultInstance() {
        // Arrange: The expected object is the predefined default instance.
        final DeprecatedAttributes expectedDefaults = DeprecatedAttributes.DEFAULT;

        // Act: Create an instance from a new, unmodified builder.
        final DeprecatedAttributes actualInstance = DeprecatedAttributes.builder().get();

        // Assert: The created instance's properties must match the default properties.
        assertAll("Default attributes should match",
            () -> assertEquals(expectedDefaults.getDescription(), actualInstance.getDescription(), "description"),
            () -> assertEquals(expectedDefaults.getSince(), actualInstance.getSince(), "since"),
            () -> assertEquals(expectedDefaults.isForRemoval(), actualInstance.isForRemoval(), "forRemoval")
        );
    }
}