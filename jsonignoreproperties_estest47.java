package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite focuses on the behavior of the {@link JsonIgnoreProperties.Value} class.
 * It has been improved for clarity and maintainability.
 */
public class JsonIgnoreProperties_ESTestTest47 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Verifies that calling {@code withoutAllowSetters()} on a {@code Value} instance
     * returns a new, distinct instance where the {@code allowSetters} property is set to false,
     * while all other properties are preserved.
     */
    @Test
    public void withoutAllowSetters_shouldReturnNewInstanceWithAllowSettersDisabled() {
        // Arrange: Create a base Value instance that has 'allowSetters' enabled.
        // This provides the necessary starting state to test the 'without' method.
        JsonIgnoreProperties.Value valueWithSettersAllowed = JsonIgnoreProperties.Value
                .forIgnoredProperties("prop1", "prop2")
                .withAllowSetters();

        // Sanity check the initial state.
        assertTrue("Precondition failed: 'allowSetters' should be true.",
                valueWithSettersAllowed.getAllowSetters());

        // Act: Call the method under test to create a new instance.
        JsonIgnoreProperties.Value resultValue = valueWithSettersAllowed.withoutAllowSetters();

        // Assert: Verify the properties of the new instance.
        assertNotSame("A new instance should be returned, not the same one.",
                valueWithSettersAllowed, resultValue);
        assertFalse("The 'allowSetters' property should now be disabled.",
                resultValue.getAllowSetters());

        // Assert: Ensure that other properties were preserved from the original instance.
        assertEquals("Ignored properties should remain unchanged.",
                valueWithSettersAllowed.getIgnored(), resultValue.getIgnored());
        assertEquals("The 'ignoreUnknown' property should remain unchanged.",
                valueWithSettersAllowed.getIgnoreUnknown(), resultValue.getIgnoreUnknown());
        assertEquals("The 'allowGetters' property should remain unchanged.",
                valueWithSettersAllowed.getAllowGetters(), resultValue.getAllowGetters());
        assertEquals("The 'merge' property should remain unchanged.",
                valueWithSettersAllowed.getMerge(), resultValue.getMerge());
    }
}