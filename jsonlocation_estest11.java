package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on its data-holding capabilities.
 */
public class JsonLocationTest {

    /**
     * Verifies that the JsonLocation constructor correctly assigns all provided location
     * properties and that the corresponding getter methods return those exact values.
     */
    @Test
    public void shouldCorrectlyStoreAndRetrieveLocationProperties() {
        // Arrange: Define the expected location properties.
        final long expectedByteOffset = -2758L;
        final long expectedCharOffset = 0L;
        final int expectedLineNumber = -3036;
        final int expectedColumnNumber = 0;
        // The actual content of the reference is not relevant for this state-check test.
        ContentReference dummyContentReference = ContentReference.rawReference(true, null);

        // Act: Create a JsonLocation instance with the test data.
        JsonLocation location = new JsonLocation(
            dummyContentReference,
            expectedByteOffset,
            expectedCharOffset,
            expectedLineNumber,
            expectedColumnNumber
        );

        // Assert: Verify that each getter returns the value provided to the constructor.
        assertEquals("Byte offset should match the constructor argument",
                expectedByteOffset, location.getByteOffset());
        assertEquals("Character offset should match the constructor argument",
                expectedCharOffset, location.getCharOffset());
        assertEquals("Line number should match the constructor argument",
                expectedLineNumber, location.getLineNr());
        assertEquals("Column number should match the constructor argument",
                expectedColumnNumber, location.getColumnNr());
    }
}