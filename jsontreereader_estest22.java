package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import org.junit.Test;

/**
 * This class contains tests for {@link JsonTreeReader}.
 * The original test class name and inheritance are kept for context.
 */
public class JsonTreeReader_ESTestTest22 extends JsonTreeReader_ESTest_scaffolding {

    /**
     * Verifies that calling {@link JsonTreeReader#nextLong()} on a reader initialized
     * with a null {@link JsonElement} throws a {@link NullPointerException}.
     *
     * This is expected because the reader has no underlying element to process.
     */
    @Test(expected = NullPointerException.class)
    public void nextLong_whenInitializedWithNullElement_shouldThrowNullPointerException() {
        // Arrange: Create a reader with a null JsonElement, which is an invalid starting state.
        JsonTreeReader reader = new JsonTreeReader(null);

        // Act & Assert: Attempting to read a long value should fail immediately.
        // The @Test(expected) annotation handles the assertion.
        reader.nextLong();
    }
}