package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite focusing on the merging logic of the {@link JsonIgnoreProperties.Value} class.
 * This suite verifies how different instances of {@code JsonIgnoreProperties.Value} are
 * combined.
 */
class JsonIgnorePropertiesValueMergeTest {

    /**
     * Verifies that {@link JsonIgnoreProperties.Value#mergeAll(JsonIgnoreProperties.Value...)}
     * correctly creates a union of ignored properties from multiple source instances.
     */
    @Test
    void mergeAllShouldCombineIgnoredPropertiesFromAllValues() {
        // Arrange: Create multiple Value instances, each configured to ignore a different property.
        // The `forIgnoredProperties` factory method provides a clear and direct way to set this up.
        JsonIgnoreProperties.Value valueIgnoringA = JsonIgnoreProperties.Value.forIgnoredProperties("a");
        JsonIgnoreProperties.Value valueIgnoringB = JsonIgnoreProperties.Value.forIgnoredProperties("b");
        JsonIgnoreProperties.Value valueIgnoringC = JsonIgnoreProperties.Value.forIgnoredProperties("c");

        // Act: Merge all the Value instances into a single new instance.
        JsonIgnoreProperties.Value mergedValue = JsonIgnoreProperties.Value.mergeAll(
                valueIgnoringA, valueIgnoringB, valueIgnoringC);

        // Assert: The merged instance should contain the combined set of all ignored properties.
        // A single assertEquals on two Sets checks for both size and content equality.
        Set<String> expectedIgnored = new HashSet<>(Arrays.asList("a", "b", "c"));
        Set<String> actualIgnored = mergedValue.getIgnored();

        assertEquals(expectedIgnored, actualIgnored);
    }
}