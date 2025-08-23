package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class, focusing on its behavior and state representation.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Verifies that the toString() method correctly represents a Value instance
     * created to ignore unknown properties.
     */
    @Test
    public void toStringShouldReflectThatUnknownPropertiesAreIgnored() {
        // Arrange: Create a Value instance configured to ignore unknown properties.
        // All other properties should retain their default values.
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // The expected string representation includes the configured 'ignoreUnknown=true'
        // and default values for all other fields.
        String expectedToString = "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=true,allowGetters=false,allowSetters=false,merge=true)";

        // Act: Generate the string representation of the Value object.
        String actualToString = value.toString();

        // Assert: The actual string should match the expected format and content.
        assertEquals(expectedToString, actualToString);
    }
}