package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class JsonIgnoreProperties_ESTestTest25 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that the {@code withoutIgnored()} method creates a new {@code Value}
     * instance with an empty set of ignored properties, while preserving all other
     * configuration flags from the original instance.
     */
    @Test(timeout = 4000)
    public void withoutIgnoredShouldClearIgnoredPropertiesAndPreserveOtherFlags() {
        // Arrange: Create a Value instance with a specific configuration,
        // including properties to be ignored and various flags set.
        Set<String> initialIgnoredProperties = new HashSet<>(Arrays.asList("propA", "propB"));
        boolean ignoreUnknown = true;
        boolean allowGetters = false;
        boolean allowSetters = true;
        boolean merge = false;

        JsonIgnoreProperties.Value originalValue = JsonIgnoreProperties.Value.construct(
                initialIgnoredProperties,
                ignoreUnknown,
                allowGetters,
                allowSetters,
                merge
        );

        // Act: Call the method under test to create a new Value instance.
        JsonIgnoreProperties.Value resultValue = originalValue.withoutIgnored();

        // Assert: Verify the properties of the new Value instance.
        // 1. The new instance should have no ignored properties.
        assertTrue("The set of ignored properties should be empty after calling withoutIgnored()",
                resultValue.getIgnored().isEmpty());

        // 2. All other flags should be preserved from the original value.
        assertEquals("The 'ignoreUnknown' flag should be preserved",
                ignoreUnknown, resultValue.getIgnoreUnknown());
        assertEquals("The 'allowGetters' flag should be preserved",
                allowGetters, resultValue.getAllowGetters());
        assertEquals("The 'allowSetters' flag should be preserved",
                allowSetters, resultValue.getAllowSetters());
        assertEquals("The 'merge' flag should be preserved",
                merge, resultValue.getMerge());
        
        // 3. The original value should remain unchanged (confirming immutability).
        assertEquals("The original value's ignored properties should not be modified",
                initialIgnoredProperties, originalValue.getIgnored());
    }
}