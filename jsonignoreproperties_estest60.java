package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonIgnoreProperties_ESTestTest60 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that the `JsonIgnoreProperties.Value.construct` factory method correctly
     * creates an instance with the specified properties.
     */
    @Test(timeout = 4000)
    public void constructShouldCorrectlySetAllProperties() {
        // Arrange: Define the properties for the Value object.
        // The original test used a roundabout way to get an empty set; this is more direct.
        Set<String> ignoredProperties = Collections.emptySet();
        boolean ignoreUnknown = false;
        boolean allowGetters = true;
        boolean allowSetters = true;
        boolean merge = true;

        // Act: Construct the Value object using the factory method under test.
        JsonIgnoreProperties.Value constructedValue = JsonIgnoreProperties.Value.construct(
                ignoredProperties,
                ignoreUnknown,
                allowGetters,
                allowSetters,
                merge
        );

        // Assert: Verify that the getters return the values used during construction.
        assertFalse("ignoreUnknown should be false", constructedValue.getIgnoreUnknown());
        assertTrue("allowGetters should be true", constructedValue.getAllowGetters());
        assertTrue("allowSetters should be true", constructedValue.getAllowSetters());
        assertTrue("merge should be true", constructedValue.getMerge());
    }
}