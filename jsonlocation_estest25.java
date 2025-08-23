package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Tests that the internal {@code _wrap()} helper method returns the exact same instance
     * when the input object is already a {@link ContentReference}.
     * <p>
     * This behavior is an optimization to avoid redundant object creation.
     */
    @Test
    public void wrap_whenGivenContentReference_shouldReturnSameInstance() {
        // Arrange: Create a ContentReference instance to use as input.
        // The specific constructor arguments are not relevant to this test's logic.
        Object sourceObject = new Object();
        ContentReference originalContentReference = ContentReference.construct(
                false, sourceObject, 0, 0, null);

        // Act: Call the _wrap method with the existing ContentReference.
        ContentReference result = JsonLocation._wrap(originalContentReference);

        // Assert: The method should return the identical instance, not a new one.
        assertSame("The _wrap method should not create a new object if one is already provided.",
                originalContentReference, result);
    }
}