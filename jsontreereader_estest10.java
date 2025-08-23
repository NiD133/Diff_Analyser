package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import org.junit.Test;

/**
 * Unit tests for {@link JsonTreeReader}.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling skipValue() on a reader initialized with a null JsonElement
     * results in a NullPointerException. This is the expected behavior because the reader
     * has no element to operate on.
     */
    @Test(expected = NullPointerException.class)
    public void skipValueOnNullElementThrowsNullPointerException() {
        // Arrange: Create a JsonTreeReader with a null JsonElement, which is an invalid state.
        JsonTreeReader reader = new JsonTreeReader((JsonElement) null);

        // Act: Attempt to skip a value. This should fail because the internal state is invalid.
        reader.skipValue();

        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
    }
}