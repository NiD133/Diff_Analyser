package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite focuses on the JsonLocation class.
 * The original test was improved for clarity and maintainability.
 */
public class JsonLocationTest {

    /**
     * Tests the deprecated constructor that accepts a generic Object as a source reference.
     * This ensures that when a JsonLocation is created with specific character offset,
     * line, and column numbers, all properties are correctly initialized.
     */
    @Test
    @SuppressWarnings("deprecation") // Intentionally testing deprecated constructor for backward compatibility.
    public void constructorWithObjectRefShouldCorrectlySetLocationFields() {
        // ARRANGE
        // Define location parameters with descriptive names for clarity.
        final long charOffset = -578L;
        final int lineNumber = 500;
        final int columnNumber = 500;
        // This specific constructor does not handle byte offset, so it defaults to -1.
        final long expectedByteOffset = -1L;

        // Create a source reference to be passed to the JsonLocation constructor.
        // The constructor under test accepts a generic Object, which it then wraps internally.
        Object sourceObject = ContentReference.construct(true, "test content");

        // ACT
        // Instantiate JsonLocation using the deprecated constructor.
        JsonLocation location = new JsonLocation(sourceObject, charOffset, lineNumber, columnNumber);

        // ASSERT
        // Verify that all location properties are stored and retrieved correctly.
        assertEquals("Line number should match the constructor argument",
                lineNumber, location.getLineNr());
        assertEquals("Column number should match the constructor argument",
                columnNumber, location.getColumnNr());
        assertEquals("Character offset should match the constructor argument",
                charOffset, location.getCharOffset());
        assertEquals("Byte offset should default to -1 for this constructor",
                expectedByteOffset, location.getByteOffset());

        // Verify that the underlying content reference was correctly processed.
        ContentReference retrievedContentRef = location.contentReference();
        assertNotNull("The content reference should not be null", retrievedContentRef);
        assertTrue("The content reference should correctly indicate it has textual content",
                retrievedContentRef.hasTextualContent());
    }
}