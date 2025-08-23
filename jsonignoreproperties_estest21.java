package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * This test class contains tests for the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnoreProperties_ESTestTest21 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that the `withoutIgnoreUnknown()` method correctly creates a new
     * `JsonIgnoreProperties.Value` instance with the `ignoreUnknown` property set to false,
     * while preserving all other properties.
     */
    @Test(timeout = 4000)
    public void withoutIgnoreUnknownShouldCreateNewInstanceWithIgnoreUnknownDisabled() {
        // Arrange: Create an initial JsonIgnoreProperties.Value instance where ignoreUnknown is true.
        // We use a mock annotation to configure the initial state.
        JsonIgnoreProperties mockAnnotation = mock(JsonIgnoreProperties.class);
        doReturn(true).when(mockAnnotation).ignoreUnknown();
        doReturn(false).when(mockAnnotation).allowGetters();
        doReturn(false).when(mockAnnotation).allowSetters();
        doReturn(new String[]{"ignoredProp"}).when(mockAnnotation).value();

        JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.from(mockAnnotation);
        assertTrue("Precondition failed: Initial value should have ignoreUnknown=true", initialValue.getIgnoreUnknown());

        // Act: Call the method under test.
        JsonIgnoreProperties.Value updatedValue = initialValue.withoutIgnoreUnknown();

        // Assert: Verify the new instance has the expected properties and the original is unchanged.

        // 1. The new instance should have `ignoreUnknown` set to false.
        assertFalse("Updated value should have ignoreUnknown set to false", updatedValue.getIgnoreUnknown());

        // 2. Other properties should be copied from the original instance.
        assertEquals("Ignored properties should be preserved", initialValue.getIgnored(), updatedValue.getIgnored());
        assertEquals("allowGetters property should be preserved", initialValue.getAllowGetters(), updatedValue.getAllowGetters());
        assertEquals("allowSetters property should be preserved", initialValue.getAllowSetters(), updatedValue.getAllowSetters());

        // 3. The method must return a new, distinct instance.
        assertNotSame("A new instance should be returned", initialValue, updatedValue);
        assertNotEquals("The new instance should not be equal to the original", initialValue, updatedValue);

        // 4. The original instance should remain unchanged (verifying immutability).
        assertTrue("Original instance should not be modified", initialValue.getIgnoreUnknown());
    }
}