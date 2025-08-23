package com.fasterxml.jackson.annotation;

import org.junit.Test;
import java.util.Collections;
import java.util.Set;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonIgnoreProperties.Value} class, focusing on its factory and mutant methods.
 * This class was refactored from an auto-generated test to improve clarity.
 */
public class JsonIgnoreProperties_ESTestTest14 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Verifies that the {@code withMerge()} method returns a new {@link JsonIgnoreProperties.Value}
     * instance with the 'merge' property set to true, while keeping all other properties unchanged.
     */
    @Test
    public void withMergeShouldCreateNewInstanceWithMergeEnabledAndPreserveOtherProperties() {
        // Arrange: Create an initial Value instance with specific properties and merge disabled.
        final Set<String> ignoredProperties = Collections.emptySet();
        final boolean initialIgnoreUnknown = true;
        final boolean initialAllowGetters = false;
        final boolean initialAllowSetters = true;
        final boolean initialMerge = false; // Explicitly start with merge disabled

        JsonIgnoreProperties.Value originalValue = JsonIgnoreProperties.Value.construct(
                ignoredProperties,
                initialIgnoreUnknown,
                initialAllowGetters,
                initialAllowSetters,
                initialMerge
        );

        // Act: Call the method under test to create a new instance with merge enabled.
        JsonIgnoreProperties.Value mergedValue = originalValue.withMerge();

        // Assert: Verify the new instance has the correct properties.
        assertNotSame("A new instance should be created, not a modified one.", originalValue, mergedValue);

        // 1. Verify the 'merge' property was enabled
        assertTrue("The 'merge' property should now be true.", mergedValue.getMerge());

        // 2. Verify all other properties were preserved from the original instance
        assertEquals("Ignored properties should be unchanged.",
                originalValue.getIgnored(), mergedValue.getIgnored());
        assertEquals("'ignoreUnknown' property should be unchanged.",
                originalValue.getIgnoreUnknown(), mergedValue.getIgnoreUnknown());
        assertEquals("'allowGetters' property should be unchanged.",
                originalValue.getAllowGetters(), mergedValue.getAllowGetters());
        assertEquals("'allowSetters' property should be unchanged.",
                originalValue.getAllowSetters(), mergedValue.getAllowSetters());
    }
}