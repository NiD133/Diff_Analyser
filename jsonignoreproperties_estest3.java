package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnoreProperties_ESTestTest3 {

    /**
     * Verifies that the toString() method of JsonIgnoreProperties.Value
     * produces a correct and human-readable string representation of its state.
     */
    @Test
    public void toStringShouldReturnCorrectStringRepresentation() {
        // Arrange
        Set<String> ignoredProperties = Collections.emptySet();
        boolean ignoreUnknown = false;
        boolean allowGetters = true;
        boolean allowSetters = false;
        boolean merge = true;

        // The expected string format for a Value object with the properties defined above.
        String expectedToString = "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=false,allowGetters=true,allowSetters=false,merge=true)";

        // Act
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.construct(
                ignoredProperties,
                ignoreUnknown,
                allowGetters,
                allowSetters,
                merge
        );
        String actualToString = value.toString();

        // Assert
        assertEquals(expectedToString, actualToString);
    }
}