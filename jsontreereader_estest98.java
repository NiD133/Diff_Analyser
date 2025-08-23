package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import org.junit.Test;

/**
 * Unit tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling nextName() on a JsonTreeReader initialized with a null
     * JsonElement throws a NullPointerException. The constructor itself accepts null,
     * but subsequent read operations are expected to fail.
     */
    @Test(expected = NullPointerException.class)
    public void nextName_whenInitializedWithNull_throwsNullPointerException() {
        // Arrange: Create a reader with a null JsonElement.
        JsonTreeReader reader = new JsonTreeReader((JsonElement) null);

        // Act & Assert: Attempting to read the next name should throw a NullPointerException.
        // The assertion is handled by the @Test(expected=...) annotation.
        reader.nextName();
    }
}