package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;

public class JsonIgnoreProperties_ESTestTest22 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that `withoutIgnoreUnknown()` creates a new Value instance
     * with the 'ignoreUnknown' property set to false, while preserving all other properties.
     */
    @Test(timeout = 4000)
    public void withoutIgnoreUnknown_shouldCreateNewInstanceWithIgnoreUnknownSetToFalse() {
        // Arrange: Create an initial Value instance with ignoreUnknown=true.
        Set<String> ignoredProperties = Collections.emptySet();
        JsonIgnoreProperties.Value originalValue = JsonIgnoreProperties.Value.construct(
                ignoredProperties,
                /* ignoreUnknown */ true,
                /* allowGetters */ true,
                /* allowSetters */ false,
                /* merge */ true
        );
        assertTrue("Precondition: originalValue should have ignoreUnknown set to true.",
                originalValue.getIgnoreUnknown());

        // Act: Call the method under test.
        JsonIgnoreProperties.Value updatedValue = originalValue.withoutIgnoreUnknown();

        // Assert: Verify the properties of the new Value instance.

        // 1. The 'ignoreUnknown' property should now be false.
        assertFalse("The updated value should have ignoreUnknown set to false.",
                updatedValue.getIgnoreUnknown());

        // 2. All other properties should be unchanged.
        assertTrue("allowGetters should remain true.", updatedValue.getAllowGetters());
        assertFalse("allowSetters should remain false.", updatedValue.getAllowSetters());
        assertTrue("merge should remain true.", updatedValue.getMerge());
        assertEquals("The set of ignored properties should be unchanged.",
                originalValue.getIgnored(), updatedValue.getIgnored());

        // 3. The method must return a new, distinct instance.
        assertNotSame("A new instance should have been created.", originalValue, updatedValue);
        assertNotEquals("The new instance should not be equal to the original.", originalValue, updatedValue);
    }
}