package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Verifies that creating a {@link JsonIgnoreProperties.Value} instance
     * via the {@code forIgnoredProperties} factory method with an empty set
     * results in an object with the correct default boolean flag values.
     */
    @Test
    public void forIgnoredPropertiesWithEmptySetShouldHaveDefaultBooleanFlags() {
        // Arrange: Create an empty set of property names to ignore.
        Set<String> emptyIgnoredProperties = Collections.emptySet();

        // Act: Create a Value instance using the factory method.
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoredProperties(emptyIgnoredProperties);

        // Assert: Verify that the boolean properties have their expected default values.
        assertTrue("The set of ignored properties should be empty", value.getIgnored().isEmpty());
        assertFalse("'ignoreUnknown' should be false by default", value.getIgnoreUnknown());
        assertFalse("'allowGetters' should be false by default", value.getAllowGetters());
        assertFalse("'allowSetters' should be false by default", value.getAllowSetters());
        assertTrue("'merge' should be true by default", value.getMerge());
    }
}