package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the JsonLocation class, focusing on its constructors and accessors.
 */
public class JsonLocationTest {

    /**
     * Tests that the constructor correctly initializes the location properties
     * and that the getter methods return the expected values.
     */
    @Test
    public void shouldCorrectlyStoreAndRetrieveLocationDetails() {
        // Arrange: Define the location parameters for clarity.
        final long expectedCharOffset = 314L;
        final int expectedLineNr = 0;
        final int expectedColumnNr = -2189;
        final int contentOffset = 2879;
        final int contentLength = 2879;

        // Create a dummy ContentReference, which is a required argument for the JsonLocation constructor.
        ContentReference inputContentReference = ContentReference.construct(
                false, /* isResource */
                null,  /* rawContent */
                contentOffset,
                contentLength,
                null   /* errorReportConfig */
        );

        // Act: Create a new JsonLocation instance using the constructor under test.
        JsonLocation location = new JsonLocation(
                inputContentReference,
                expectedCharOffset,
                expectedLineNr,
                expectedColumnNr
        );

        // Assert: Verify that all properties are stored and can be retrieved correctly.
        assertEquals("Character offset should match the constructor argument",
                expectedCharOffset, location.getCharOffset());
        assertEquals("Line number should match the constructor argument",
                expectedLineNr, location.getLineNr());
        assertEquals("Column number should match the constructor argument",
                expectedColumnNr, location.getColumnNr());

        // This specific constructor does not set the byte offset, so it should default to -1.
        assertEquals("Byte offset should default to -1",
                -1L, location.getByteOffset());

        // Verify the content reference is the same instance that was passed in.
        assertSame("The retrieved ContentReference should be the same instance as the input",
                inputContentReference, location.contentReference());
    }
}