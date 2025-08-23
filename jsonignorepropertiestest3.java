package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link JsonIgnoreProperties.Value} class, focusing on its factory methods.
 */
class JsonIgnorePropertiesValueTest {

    // A dummy class annotated with @JsonIgnoreProperties for testing purposes.
    // It specifies two properties to ignore ("foo", "bar") and sets ignoreUnknown to true.
    @JsonIgnoreProperties(value = { "foo", "bar" }, ignoreUnknown = true)
    private static final class AnnotatedClass {
    }

    @Test
    @DisplayName("Value.from() should correctly create an instance from an annotation")
    void from_shouldCreateValueFromAnnotation() {
        // Arrange
        JsonIgnoreProperties annotation = AnnotatedClass.class.getAnnotation(JsonIgnoreProperties.class);
        Set<String> expectedIgnoredProperties = asSet("foo", "bar");

        // Act
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.from(annotation);

        // Assert
        assertNotNull(value, "Value object should not be null");

        // Group assertions for properties explicitly set in the annotation
        assertAll("Properties from annotation should be correctly set",
            () -> assertEquals(expectedIgnoredProperties, value.getIgnored(),
                    "Ignored properties should match the 'value' attribute of the annotation"),
            () -> assertTrue(value.getIgnoreUnknown(),
                    "ignoreUnknown should match the 'ignoreUnknown' attribute of the annotation")
        );

        // Group assertions for properties not present in the annotation, which should have default values
        assertAll("Properties not in annotation should have defaults",
            () -> assertFalse(value.getAllowGetters(), "allowGetters should default to false"),
            () -> assertFalse(value.getAllowSetters(), "allowSetters should default to false"),
            () -> assertFalse(value.getMerge(), "merge should be false when created from an annotation")
        );
    }

    /**
     * Helper method to create a Set from a var-args array of strings.
     */
    private static Set<String> asSet(String... items) {
        return new LinkedHashSet<>(Arrays.asList(items));
    }
}