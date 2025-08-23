package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;

public class JsonIgnoreProperties_ValueTest {

    /**
     * This test verifies the behavior of the `withoutAllowGetters()` method.
     * It should create a new instance when the `allowGetters` property is changed,
     * but return the same instance if the property is already false, demonstrating idempotency.
     */
    @Test
    public void withoutAllowGettersShouldCreateNewInstanceOnChangeAndSameInstanceWhenUnchanged() {
        // Arrange: Create a Value instance with allowGetters enabled.
        Set<String> ignoredProperties = Collections.emptySet();
        JsonIgnoreProperties.Value valueWithGettersAllowed = JsonIgnoreProperties.Value.construct(
                ignoredProperties,
                false, // ignoreUnknown
                true,  // allowGetters
                true,  // allowSetters
                true   // merge
        );

        // --- Scenario 1: Change the value from true to false ---

        // Act: Call withoutAllowGetters(), which should change the state.
        JsonIgnoreProperties.Value valueWithGettersDisallowed = valueWithGettersAllowed.withoutAllowGetters();

        // Assert: A new instance is created, and only the allowGetters flag is updated.
        assertNotSame("A new instance should be created when the value changes.",
                valueWithGettersAllowed, valueWithGettersDisallowed);
        assertFalse("allowGetters should now be false.",
                valueWithGettersDisallowed.getAllowGetters());

        // Verify other properties remain unchanged
        assertTrue("allowSetters should remain true.", valueWithGettersDisallowed.getAllowSetters());
        assertFalse("ignoreUnknown should remain false.", valueWithGettersDisallowed.getIgnoreUnknown());
        assertTrue("merge should remain true.", valueWithGettersDisallowed.getMerge());


        // --- Scenario 2: Call when the value is already false ---

        // Act: Call withoutAllowGetters() again on the new instance.
        JsonIgnoreProperties.Value resultOfSecondCall = valueWithGettersDisallowed.withoutAllowGetters();

        // Assert: The same instance is returned because the state did not change.
        assertSame("The same instance should be returned when the value is already false.",
                valueWithGettersDisallowed, resultOfSecondCall);
    }
}