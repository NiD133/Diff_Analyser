package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the fluent factory methods of the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnorePropertiesValueTest {

    @Test
    public void withAndWithoutMethodsShouldCreateNewImmutableInstancesWithUpdatedProperties() {
        // Arrange: Create a base Value instance.
        // By default, forIgnoreUnknown(true) also implies merge=true, allowGetters=false, allowSetters=false.
        JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Act: Create new instances by chaining fluent "with" and "without" methods.
        // This demonstrates the immutability of the Value class.
        JsonIgnoreProperties.Value valueWithGetters = baseValue.withAllowGetters();
        JsonIgnoreProperties.Value finalValue = valueWithGetters.withoutMerge();

        // Assert: Verify the properties of each instance to confirm correctness and immutability.

        // 1. The base instance should remain unchanged.
        assertTrue("baseValue should have ignoreUnknown=true", baseValue.getIgnoreUnknown());
        assertFalse("baseValue should have default allowGetters=false", baseValue.getAllowGetters());
        assertFalse("baseValue should have default allowSetters=false", baseValue.getAllowSetters());
        assertTrue("baseValue should have default merge=true", baseValue.getMerge());

        // 2. The second instance should have allowGetters enabled.
        assertTrue("valueWithGetters should inherit ignoreUnknown=true", valueWithGetters.getIgnoreUnknown());
        assertTrue("withAllowGetters() should enable allowGetters", valueWithGetters.getAllowGetters());
        assertFalse("valueWithGetters should inherit allowSetters=false", valueWithGetters.getAllowSetters());
        assertTrue("valueWithGetters should inherit merge=true", valueWithGetters.getMerge());

        // 3. The final instance should have merge disabled.
        assertTrue("finalValue should inherit ignoreUnknown=true", finalValue.getIgnoreUnknown());
        assertTrue("finalValue should inherit allowGetters=true", finalValue.getAllowGetters());
        assertFalse("finalValue should inherit allowSetters=false", finalValue.getAllowSetters());
        assertFalse("withoutMerge() should disable merge", finalValue.getMerge());

        // 4. Confirm that new, distinct objects were created at each step.
        assertNotEquals("withAllowGetters() should create a new instance", baseValue, valueWithGetters);
        assertNotEquals("withoutMerge() should create a new instance", valueWithGetters, finalValue);
    }
}