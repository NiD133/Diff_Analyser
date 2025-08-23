package com.google.gson.internal.bind;

import com.google.gson.JsonElement;
import org.junit.Test;

/**
 * Test suite for the {@link JsonTreeReader} class, focusing on its behavior
 * under specific edge-case conditions.
 *
 * Note: The original test class name 'JsonTreeReader_ESTestTest102' was renamed to
 * 'JsonTreeReaderTest' to follow standard naming conventions.
 */
public class JsonTreeReaderTest {

    /**
     * Verifies that calling {@code promoteNameToValue()} on a {@code JsonTreeReader}
     * that was initialized with a {@code null} {@code JsonElement} results in a
     * {@code NullPointerException}.
     *
     * This test ensures the method handles being in an invalid state gracefully
     * by failing fast, rather than causing unpredictable behavior later.
     */
    @Test(expected = NullPointerException.class)
    public void promoteNameToValue_whenInitializedWithNull_throwsNullPointerException() {
        // Arrange: Create a JsonTreeReader with a null JsonElement, putting it into
        // an uninitialized or invalid state.
        JsonTreeReader reader = new JsonTreeReader((JsonElement) null);

        // Act: Attempt to call the method under test.
        // Assert: The @Test(expected=...) annotation will automatically handle the
        // assertion, failing the test if a NullPointerException is not thrown.
        reader.promoteNameToValue();
    }
}