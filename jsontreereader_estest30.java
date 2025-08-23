package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link JsonTreeReader}, focusing on its behavior with invalid or edge-case inputs.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that attempting to read a double from a {@code JsonTreeReader}
     * initialized with a {@code null} {@link JsonElement} results in a {@link NullPointerException}.
     * This tests the reader's handling of an invalid initial state where no element is provided.
     */
    @Test(expected = NullPointerException.class)
    public void nextDouble_whenReaderInitializedWithNull_shouldThrowNullPointerException() throws IOException {
        // Arrange: Create a reader with a null JsonElement, representing an invalid state.
        JsonTreeReader reader = new JsonTreeReader(null);

        // Act & Assert: Attempting to read a double should throw a NullPointerException.
        // The assertion is handled by the `expected` parameter of the @Test annotation.
        reader.nextDouble();
    }
}