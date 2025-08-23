package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeReader}.
 * This test class focuses on behavior when the reader is initialized with invalid state.
 */
// The original test class name and inheritance are preserved for context.
public class JsonTreeReader_ESTestTest19 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that calling nextNull() on a reader initialized with a null JsonElement
     * correctly throws a NullPointerException. This is expected because the reader
     * has no JSON tree to traverse.
     */
    @Test(expected = NullPointerException.class)
    public void nextNull_whenInitializedWithNull_throwsNullPointerException() {
        // Arrange: Create a reader with a null JsonElement, which is an invalid starting state.
        JsonTreeReader reader = new JsonTreeReader((JsonElement) null);

        // Act: Attempting to read a null value should fail immediately.
        reader.nextNull();
    }
}