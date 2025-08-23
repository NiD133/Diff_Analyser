package com.fasterxml.jackson.annotation;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.*;

/**
 * This class contains tests for the {@link JsonIgnoreProperties.Value} class.
 * Note: The original test was auto-generated. This version has been refactored for clarity.
 */
public class JsonIgnoreProperties_ESTestTest41 {

    /**
     * Tests that `findIgnoredForDeserialization()` returns an empty set
     * when `allowSetters` is configured to be true.
     *
     * This behavior is expected because `allowSetters=true` effectively disables
     * the ignoring of any specified properties during the deserialization process.
     */
    @Test
    public void findIgnoredForDeserialization_whenAllowSettersIsEnabled_shouldReturnEmptySet() {
        // Arrange: Create a Value instance configured to ignore "internalId".
        // By default, allowSetters is false.
        final String[] propertiesToIgnore = {"internalId"};
        JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.forIgnoredProperties(propertiesToIgnore);

        // Act: Create a new Value instance with allowSetters enabled, and then
        // retrieve the set of properties to be ignored for deserialization.
        JsonIgnoreProperties.Value valueWithAllowSetters = initialValue.withAllowSetters();
        Set<String> ignoredForDeserialization = valueWithAllowSetters.findIgnoredForDeserialization();

        // Assert: The returned set should be empty because setters are explicitly allowed.
        assertTrue("Set of ignored properties for deserialization should be empty when allowSetters is true.",
                   ignoredForDeserialization.isEmpty());

        // Also, verify the state of the new and old Value objects to ensure correctness and immutability.
        assertTrue("The new instance should have allowSetters enabled.", valueWithAllowSetters.getAllowSetters());
        assertNotSame("withAllowSetters() should return a new, distinct instance.",
                      initialValue, valueWithAllowSetters);
        assertFalse("The original instance's allowSetters property should remain unchanged.",
                    initialValue.getAllowSetters());
    }
}