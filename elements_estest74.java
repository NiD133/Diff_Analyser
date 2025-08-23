package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements} class, focusing on selector-based methods.
 */
public class ElementsTest {

    /**
     * Verifies that the prevAll() method throws an IllegalStateException when passed an empty
     * CSS query string. The underlying QueryParser is responsible for validating the query.
     */
    @Test
    public void prevAll_shouldThrowIllegalStateException_whenQueryIsEmpty() {
        // Arrange: An empty Elements collection is sufficient for this test.
        // The exception is expected from the query parser before any elements are processed.
        Elements elements = new Elements();

        // Act & Assert: Verify that calling prevAll with an empty string throws the correct exception.
        try {
            elements.prevAll("");
            fail("Expected an IllegalStateException to be thrown for an empty CSS query.");
        } catch (IllegalStateException e) {
            // The QueryParser should reject empty query strings.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}