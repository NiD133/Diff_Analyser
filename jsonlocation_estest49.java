package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link JsonLocation} class.
 */
public class JsonLocationTest {

    /**
     * Tests the deprecated constructor {@link JsonLocation#JsonLocation(Object, long, int, int)}.
     * <p>
     * This test verifies that when a {@code JsonLocation} is created using this constructor:
     * <ul>
     *     <li>The source reference, character offset, line number, and column number are set correctly.</li>
     *     <li>The byte offset defaults to -1, as this constructor does not accept it as a parameter.</li>
     *     <li>The provided source reference object is stored and can be retrieved as the same instance.</li>
     * </ul>
     */
    @Test
    public void constructorWithObjectSourceShouldSetPropertiesAndDefaultByteOffset() {
        // Arrange
        ContentReference sourceContent = ContentReference.redacted();
        long charOffset = -578L;
        int line = 500;
        int column = 500;

        // Act
        // We explicitly cast to Object to invoke the deprecated constructor:
        // JsonLocation(Object srcRef, long totalChars, int lineNr, int columnNr)
        JsonLocation location = new JsonLocation((Object) sourceContent, charOffset, line, column);

        // Assert
        // Verify that the properties passed to the constructor are set correctly
        assertEquals(line, location.getLineNr());
        assertEquals(column, location.getColumnNr());
        assertEquals(charOffset, location.getCharOffset());

        // Verify that the byte offset is defaulted to -1
        assertEquals(-1L, location.getByteOffset());

        // Verify that the content reference is the exact same instance
        assertSame(sourceContent, location.contentReference());
    }
}