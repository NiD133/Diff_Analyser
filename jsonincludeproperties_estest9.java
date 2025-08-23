package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link JsonIncludeProperties.Value}.
 * This class contains tests that verify the behavior of JsonIncludeProperties.Value,
 * particularly in interactions with standard Java collections.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Verifies that attempting to remove a {@link JsonIncludeProperties.Value} object
     * from an empty {@code Set<String>} returns false.
     *
     * This test confirms the standard behavior of the Java Collections Framework.
     * The `remove` operation on an empty set should always return false, regardless
     * of the object provided.
     */
    @Test
    public void removingValueInstanceFromEmptyStringSetShouldReturnFalse() {
        // Arrange: Create an empty set of Strings and get a JsonIncludeProperties.Value instance.
        final Set<String> emptyStringSet = new LinkedHashSet<>();
        final JsonIncludeProperties.Value objectToAttemptRemoval = JsonIncludeProperties.Value.ALL;

        // Act: Attempt to remove the object from the empty set.
        final boolean wasRemoved = emptyStringSet.remove(objectToAttemptRemoval);

        // Assert: The remove operation must return false because the set is empty.
        assertFalse("An item cannot be removed from an empty set.", wasRemoved);
    }
}