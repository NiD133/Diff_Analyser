package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertTrue;

// The original class name and inheritance are kept to maintain potential test suite integrity.
public class JsonIgnoreProperties_ESTestTest42 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that `findIgnoredForSerialization()` returns an empty set when `allowGetters` is true.
     * This behavior is documented to effectively disable serialization-time ignores for the
     * specified properties, which is useful for read-only properties.
     */
    @Test
    public void findIgnoredForSerializationShouldReturnEmptySetWhenAllowGettersIsTrue() {
        // Arrange
        // According to its Javadoc, findIgnoredForSerialization() should return an empty set
        // if getAllowGetters() is true, regardless of the actual ignored properties.
        // We use a non-empty set here to ensure the logic correctly overrides the list.
        Set<String> ignoredProperties = Collections.singleton("internalId");

        // Create a Value instance with a property to ignore, but with allowGetters enabled.
        // Constructor signature: Value(ignored, ignoreUnknown, allowGetters, allowSetters, merge)
        JsonIgnoreProperties.Value value = new JsonIgnoreProperties.Value(
                ignoredProperties,
                false, // ignoreUnknown
                true,  // allowGetters - this is the key flag for this test
                false, // allowSetters
                false  // merge
        );

        // Act
        Set<String> result = value.findIgnoredForSerialization();

        // Assert
        // The returned set must be empty because allowGetters is true.
        assertTrue(
            "The set of properties ignored for serialization should be empty when allowGetters is true.",
            result.isEmpty()
        );
    }
}