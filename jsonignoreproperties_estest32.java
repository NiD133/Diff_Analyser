package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the {@link JsonIgnoreProperties.Value#mergeAll(JsonIgnoreProperties.Value...)} method.
 */
public class JsonIgnoreProperties_ESTestTest32 {

    /**
     * Tests that merging multiple {@link JsonIgnoreProperties.Value} instances correctly
     * combines their properties. The expected behavior is:
     * <ul>
     *     <li>The set of ignored properties is the union of all individual sets.</li>
     *     <li>Boolean flags (like 'ignoreUnknown') are combined using a logical OR,
     *         meaning the result is 'true' if any of the input values has it as 'true'.</li>
     * </ul>
     */
    @Test
    public void mergeAllShouldCombineIgnoredPropertiesAndUseLogicalOrForBooleanFlags() {
        // Arrange
        // Create a value with specific ignored properties and default boolean flags.
        // Defaults are: ignoreUnknown=false, allowGetters=false, allowSetters=false, merge=true.
        JsonIgnoreProperties.Value valueWithIgnoredProps = JsonIgnoreProperties.Value.forIgnoredProperties("propA", "propB");

        // Create another value with no ignored properties but with key boolean flags enabled.
        JsonIgnoreProperties.Value valueWithTrueFlags = JsonIgnoreProperties.Value.construct(
                Collections.emptySet(),
                true, // ignoreUnknown
                true, // allowGetters
                true, // allowSetters
                true  // merge
        );

        // Act
        // Merge the two values, including a null in the list to ensure it's handled gracefully.
        JsonIgnoreProperties.Value mergedValue = JsonIgnoreProperties.Value.mergeAll(
                valueWithIgnoredProps, valueWithTrueFlags, null
        );

        // Assert
        assertNotNull("The merged value should not be null", mergedValue);

        // 1. Verify that boolean flags are true because `valueWithTrueFlags` had them set to true.
        assertTrue("ignoreUnknown should be true after merge", mergedValue.getIgnoreUnknown());
        assertTrue("allowGetters should be true after merge", mergedValue.getAllowGetters());
        assertTrue("allowSetters should be true after merge", mergedValue.getAllowSetters());
        assertTrue("merge flag should remain true", mergedValue.getMerge());

        // 2. Verify that the set of ignored properties is the union of all inputs.
        Set<String> expectedIgnored = new HashSet<>(Arrays.asList("propA", "propB"));
        assertEquals("Ignored properties should be the union of all inputs",
                expectedIgnored, mergedValue.getIgnored());
    }
}