package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link JsonLocation} class.
 * This improved version replaces an auto-generated test with a clear,
 * human-readable one.
 */
public class JsonLocationTest {

    /**
     * Verifies that offsetDescription() returns a specific "UNKNOWN" message
     * when the byte offset is not available (i.e., is negative).
     */
    @Test(timeout = 4000)
    public void offsetDescriptionShouldReturnUnknownWhenByteOffsetIsNegative() {
        // Arrange: Create a JsonLocation with an unknown byte offset (-1).
        Object sourceObject = new Object();
        ContentReference contentReference = JsonLocation._wrap(sourceObject);

        long unknownByteOffset = -1L;
        // The other location parameters are not relevant for this specific behavior.
        long charOffset = 1L;
        int line = 1;
        int column = 1;

        JsonLocation location = new JsonLocation(contentReference, unknownByteOffset, charOffset, line, column);

        // Act: Get the offset description string.
        String description = location.offsetDescription();

        // Assert: The description should indicate that the byte offset is unknown.
        String expectedDescription = "byte offset: #UNKNOWN";
        assertEquals(expectedDescription, description);
    }
}