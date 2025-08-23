package com.fasterxml.jackson.annotation;

import org.junit.Test;
import java.util.Collections;
import java.util.Set;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class, focusing on its
 * factory methods for creating modified instances.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * This test verifies that the {@code withIgnoreUnknown()} method correctly creates a new
     * {@link JsonIgnoreProperties.Value} instance with the {@code ignoreUnknown} property
     * set to {@code true}, while preserving all other properties. It also confirms
     * the immutability of the original instance.
     */
    @Test
    public void withIgnoreUnknown_shouldCreateNewInstanceWithIgnoreUnknownEnabled() {
        // Arrange: Create an initial Value instance where ignoreUnknown is false.
        // Other properties are set to true to ensure they are correctly copied.
        JsonIgnoreProperties.Value originalValue = new JsonIgnoreProperties.Value(
            Collections.emptySet(), // ignored properties
            false,                  // ignoreUnknown
            true,                   // allowGetters
            true,                   // allowSetters
            true                    // merge
        );

        // Act: Call the method under test to create a new, updated instance.
        JsonIgnoreProperties.Value updatedValue = originalValue.withIgnoreUnknown();

        // Assert: Verify the new instance has the expected state.
        assertNotSame("A new instance should be created", originalValue, updatedValue);
        assertTrue("ignoreUnknown should be enabled in the new instance", updatedValue.getIgnoreUnknown());
        assertTrue("allowGetters property should be carried over", updatedValue.getAllowGetters());
        assertTrue("allowSetters property should be carried over", updatedValue.getAllowSetters());
        assertTrue("merge property should be carried over", updatedValue.getMerge());

        // Assert: Verify the original instance remains unchanged, confirming immutability.
        assertFalse("Original instance's ignoreUnknown flag should not be modified", originalValue.getIgnoreUnknown());
    }
}