package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class, focusing on its immutability and fluent API.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * This test verifies that the {@code withAllowSetters()} method is idempotent.
     * When called on an instance that already has {@code allowSetters} set to true,
     * it should return the same instance rather than creating a new one.
     * This is an important optimization for immutable objects.
     */
    @Test
    public void withAllowSetters_whenAlreadyEnabled_shouldReturnSameInstance() {
        // Arrange: Create an initial Value instance where allowSetters is false by default.
        JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.forIgnoredProperties("anyProperty");
        assertFalse("Precondition: initialValue should have allowSetters as false",
                initialValue.getAllowSetters());

        // Act:
        // 1. First call should create a new instance with allowSetters enabled.
        JsonIgnoreProperties.Value valueWithSettersEnabled = initialValue.withAllowSetters();
        // 2. Second call on the already-configured instance.
        JsonIgnoreProperties.Value resultOfSecondCall = valueWithSettersEnabled.withAllowSetters();

        // Assert:
        // Verify that the first call correctly enabled the property.
        assertTrue("withAllowSetters() should enable the allowSetters property",
                valueWithSettersEnabled.getAllowSetters());

        // The main assertion: verify that the second call returned the exact same instance.
        assertSame("Calling withAllowSetters() again should be idempotent and return the same instance",
                valueWithSettersEnabled, resultOfSecondCall);

        // Also, verify that other properties were not affected.
        assertFalse("allowGetters should remain unchanged", resultOfSecondCall.getAllowGetters());
        assertFalse("ignoreUnknown should remain unchanged", resultOfSecondCall.getIgnoreUnknown());
        assertTrue("merge should remain unchanged", resultOfSecondCall.getMerge());
    }
}