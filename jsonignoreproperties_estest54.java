package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;

public class JsonIgnoreProperties_ESTestTest54 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that the {@code JsonIgnoreProperties.Value.mergeAll()} method correctly
     * combines boolean properties from multiple {@code Value} instances.
     * <p>
     * The expected behavior is that for boolean flags like {@code ignoreUnknown},
     * the merged result is {@code true} if any of the input values have it set to {@code true}.
     * The test also verifies that the method handles {@code null} entries in the input array gracefully.
     */
    @Test
    public void mergeAllShouldCombineBooleanFlagsWithTrueAsTheWinningValue() {
        // Arrange
        Set<String> ignoredProperties = Collections.emptySet();

        // Create a base configuration where 'ignoreUnknown' is true.
        // This represents the expected outcome after the merge.
        JsonIgnoreProperties.Value configWithIgnoreUnknownTrue = JsonIgnoreProperties.Value.construct(
                ignoredProperties,
                true, // ignoreUnknown
                true, // allowGetters
                true, // allowSetters
                true  // merge
        );

        // Create a second configuration where 'ignoreUnknown' is false.
        JsonIgnoreProperties.Value configWithIgnoreUnknownFalse = JsonIgnoreProperties.Value.construct(
                ignoredProperties,
                false, // ignoreUnknown
                true,  // allowGetters
                true,  // allowSetters
                true   // merge
        );

        // Create an array containing the two configurations and several nulls to test robustness.
        JsonIgnoreProperties.Value[] valuesToMerge = new JsonIgnoreProperties.Value[8];
        valuesToMerge[0] = configWithIgnoreUnknownFalse;
        valuesToMerge[1] = configWithIgnoreUnknownTrue;

        // Act
        JsonIgnoreProperties.Value mergedConfig = JsonIgnoreProperties.Value.mergeAll(valuesToMerge);

        // Assert
        assertNotNull("The merged configuration should not be null", mergedConfig);

        // The merged value should be a new instance, not one of the inputs.
        assertNotSame("Merged config should be a new instance", configWithIgnoreUnknownTrue, mergedConfig);
        assertNotSame("Merged config should be a new instance", configWithIgnoreUnknownFalse, mergedConfig);

        // Verify that the boolean properties were merged correctly (true wins).
        assertTrue("ignoreUnknown should be true after merge", mergedConfig.getIgnoreUnknown());
        assertTrue("allowGetters should be true after merge", mergedConfig.getAllowGetters());
        assertTrue("allowSetters should be true after merge", mergedConfig.getAllowSetters());
        assertTrue("merge should be true after merge", mergedConfig.getMerge());

        // The merged configuration should be logically equal to the one where 'ignoreUnknown' was true.
        assertEquals("Merged config should be equal to the one with the 'strongest' (true) flags",
                configWithIgnoreUnknownTrue, mergedConfig);
    }
}