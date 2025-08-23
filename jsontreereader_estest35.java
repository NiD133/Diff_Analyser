package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import java.io.IOException;
import org.junit.Test;

/**
 * Unit tests for the {@link JsonTreeReader} class.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that attempting to call {@code endObject()} on a reader initialized
     * with a null {@code JsonElement} throws a {@code NullPointerException}.
     * This scenario represents an invalid initial state for the reader, which should
     * cause it to fail immediately on any operation.
     */
    @Test(expected = NullPointerException.class)
    public void endObject_withNullInitialElement_throwsNullPointerException() throws IOException {
        // Arrange: Create a reader with a null JsonElement, putting it in an invalid state.
        JsonTreeReader reader = new JsonTreeReader((JsonElement) null);

        // Act & Assert: Calling endObject() is expected to throw a NullPointerException
        // because the reader's internal state is invalid.
        reader.endObject();
    }
}